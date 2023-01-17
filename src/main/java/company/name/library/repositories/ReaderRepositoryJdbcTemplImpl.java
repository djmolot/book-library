package company.name.library.repositories;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.DaoLayerException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ReaderRepositoryJdbcTemplImpl implements ReaderRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Reader add(Reader reader) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO readers (name) VALUES (?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reader.getName());
            return ps;
        }, keyHolder);
        var generatedId = Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new DaoLayerException("Failed to save new reader to DB, no generated ID returned"));
        reader.setId(generatedId);
        return reader;
    }

    @Override
    public Optional<Reader> getById(Long id) {
        try {
            Reader reader = jdbcTemplate.queryForObject(
                    "SELECT * FROM readers WHERE id = ?;",
                    this::mapRowToReaderWithoutBooks,
                    id);
            return Optional.ofNullable(reader);
        } catch (DataAccessException e) {
            throw new DaoLayerException("Reader with ID " + id + " does not exist in DB. " + e.getMessage());
        }
    }

    @Override
    public List<Reader> getAll() {
        String sql = "SELECT r.id as reader_id, r.name, b.id as book_id, b.title, b.author "
                + "FROM books b RIGHT JOIN readers r ON b.reader_id = r.id;";
        return jdbcTemplate.query(sql, (ResultSet resultSet) -> {
            List<Reader> readers = new ArrayList<>();
            Long previousReaderId = 0L;
            Reader currentReader = null;
            while (resultSet.next()) {
                Long currentReaderId = resultSet.getLong("reader_id");
                if (!currentReaderId.equals(previousReaderId)) {
                    currentReader = new Reader();
                    currentReader.setId(currentReaderId);
                    currentReader.setName(resultSet.getString("name"));
                    currentReader.setBooks(new ArrayList<>());
                    readers.add(currentReader);
                    previousReaderId = currentReaderId;
                }
                if (resultSet.getLong("book_id") != 0) {
                    Book book = new Book();
                    book.setId(resultSet.getLong("book_id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    if (currentReader != null) {
                        currentReader.getBooks().add(book);
                    }
                }
            }
            return readers;
        });
    }

    @Override
    public void update(Reader reader) {

    }

    @Override
    public Optional<Reader> getReaderByBookId(Long bookId) {
        try {
            Reader reader = jdbcTemplate.queryForObject(
                    "SELECT r.id, r.name "
                            + "FROM books b JOIN readers r ON b.reader_id = r.id WHERE b.id = ?;",
                    this::mapRowToReaderWithoutBooks,
                    bookId);
            return Optional.ofNullable(reader);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Reader mapRowToReaderWithoutBooks(ResultSet resultSet, int rowNum)
        throws SQLException {
        Reader reader = new Reader();
        reader.setId(resultSet.getLong("id"));
        reader.setName(resultSet.getString("name"));
        return reader;
    }

}

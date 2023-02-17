package company.name.library.repositories;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.DaoLayerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Repository
public class BookRepositoryJdbcTemplImpl implements BookRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Book add(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into books (author, title) values (?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getAuthor());
            ps.setString(2, book.getTitle());
            return ps;
        }, keyHolder);
        var generatedId = Optional.ofNullable(keyHolder.getKeys())
                .map(keys -> keys.get("id"))
                .map(Long.class::cast)
                .orElseThrow(() -> new DaoLayerException(
                        "Failed to save new Book to DB, no generated ID returned. "));
        book.setId(generatedId);
        return book;
    }

    @Override
    public Optional<Book> getById(Long id) {
        try {
            Book book = jdbcTemplate.queryForObject(
                    "SELECT b.id, b.author, b.title, b.reader_id, r.name as reader_name "
                            + "FROM books b LEFT JOIN readers r ON b.reader_id = r.id WHERE b.id = ?;",
                    this::mapRowToBook,
                    id);
            return Optional.ofNullable(book);
        } catch (EmptyResultDataAccessException e) {
            log.info("Book with ID " + id + " does not exist in DB. " + e);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        Object[] args = new Object[] {id};
        return jdbcTemplate.update(sql, args) == 1;
    }

    @Override
    public List<Book> getAll() {
        return jdbcTemplate.query(
                "SELECT b.id, b.author, b.title, b.reader_id, "
                        + "r.name as reader_name "
                        + "FROM books b LEFT JOIN readers r ON b.reader_id = r.id ORDER BY b.id ASC;",
                this::mapRowToBook);
    }

    @Override
    public Book update(Book book) {
        int result = jdbcTemplate.update(
                "UPDATE books SET reader_id = ? WHERE id = ?;",
                book.getReader().map(Reader::getId).orElse(null),
                book.getId());
        if(result == 1) {
            return book;
        } else {
            return new Book();
        }
    }

    @Override
    public List<Book> getBooksByReaderId(Long readerId) {
        return jdbcTemplate.query(
                "SELECT id, author, title FROM books WHERE reader_id = ? ORDER BY id ASC;",
                this::mapRowToBookWithoutReader,
                readerId);
    }

    private Book mapRowToBook(ResultSet resultSet, int rowNum) throws SQLException {
        var book = mapRowToBookWithoutReader(resultSet, rowNum);
        if (resultSet.getObject("reader_id", Long.class) != null) {
            var reader = new Reader();
            reader.setId(resultSet.getLong("reader_id"));
            reader.setName(resultSet.getString("reader_name"));
            book.setReader(Optional.of(reader));
        } else {
            book.setReader(Optional.empty());
        }
        return book;
    }

    private Book mapRowToBookWithoutReader(ResultSet resultSet, int rowNum) throws SQLException {
        var book = new Book();
        book.setId(resultSet.getObject("id", Long.class));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        return book;
    }

}

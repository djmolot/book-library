package company.name.library.repositories;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.DaoLayerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ReaderRepositoryImpl implements ReaderRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Reader add(Reader reader) {
        String sql = """
            INSERT
            INTO
                readers
                (name, birth_date)
            VALUES
                (?, ?);
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, reader.getName());
                ps.setObject(2, reader.getBirthDate());
                return ps;
            }, keyHolder);
            reader.setId(getGeneratedId(keyHolder));
            return reader;
        } catch (DataAccessException e) {
            log.error("Error during adding reader to DB. " + e);
            throw new DaoLayerException("Error during adding reader to DB. " + e);
        }
    }

    @Override
    public Optional<Reader> getById(Long id) {
        String sql = """
            SELECT
                *
            FROM
                readers
            WHERE
                id = ?;
        """;
        try {
            Reader reader = jdbcTemplate.queryForObject(sql, this::mapRowToReaderWithoutBooks, id);
            return Optional.ofNullable(reader);
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("the query does not return exactly one row. " + e);
            return Optional.empty();
        } catch (DataAccessException e) {
            log.error("Error during getting reader by ID from DB. " + e);
            throw new DaoLayerException("Error during getting reader by ID from DB. " + e);
        }
    }

    @Override
    public List<Reader> getAll() {
        String sql = """
            SELECT
                r.id as reader_id,
                r.name,
                r.birth_date,
                b.id as book_id,
                b.title,
                b.author,
                b.borrow_date,
                b.max_borrow_time_in_days,
                b.restricted
            FROM
                books b
            RIGHT JOIN
                readers r
                    ON b.reader_id = r.id
            ORDER BY
                r.id;
        """;
        try {
            return jdbcTemplate.query(sql, this::mapResultSetToReaders);
        } catch (DataAccessException e) {
            log.error("Error during getting all readers from DB. " + e);
            throw new DaoLayerException("Error during getting all readers from DB. " + e);
        }
    }

    @Override
    public Optional<Reader> getReaderByBookId(Long bookId) {
        String sql = """
            SELECT
                r.id,
                r.name,
                r.birth_date
            FROM
                books b
            JOIN
                readers r
                    ON b.reader_id = r.id
            WHERE
                b.id = ?;
        """;
        try {
            Reader reader = jdbcTemplate.queryForObject(sql, this::mapRowToReaderWithoutBooks, bookId);
            return Optional.ofNullable(reader);
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("the query does not return exactly one row. " + e);
            return Optional.empty();
        } catch (DataAccessException e) {
            log.error("Error during getting the reader of book from DB. " + e);
            throw new DaoLayerException("Error during getting the reader of book from DB. " + e);
        }
    }

    @Override
    public void deleteAllAndRestartIdSequence() {
        jdbcTemplate.update("DELETE FROM readers");
        jdbcTemplate.update("ALTER SEQUENCE readers_id_seq RESTART WITH 1");
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        return Optional.ofNullable(keyHolder.getKeys())
                .map(keys -> keys.get("id"))
                .map(Long.class::cast)
                .orElseThrow(() -> new DaoLayerException(
                        "Failed to save new reader to DB, no generated ID returned."));
    }

    private Reader mapRowToReaderWithoutBooks(ResultSet resultSet, int rowNum) throws SQLException {
        Reader reader = new Reader();
        reader.setId(resultSet.getLong("id"));
        reader.setName(resultSet.getString("name"));
        reader.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        return reader;
    }

    private List<Reader> mapResultSetToReaders(ResultSet resultSet) throws SQLException {
        List<Reader> readers = new ArrayList<>();
        Long previousReaderId = 0L;
        Reader currentReader = null;
        while (resultSet.next()) {
            Long currentReaderId = resultSet.getLong("reader_id");
            if (!currentReaderId.equals(previousReaderId)) {
                currentReader = createReaderFromResultSet(resultSet);
                readers.add(currentReader);
                previousReaderId = currentReaderId;
            }
            if (resultSet.getLong("book_id") != 0) {
                Book book = createBookFromResultSet(resultSet);
                if (currentReader != null) {
                    currentReader.getBooks().add(book);
                }
            }
        }
        return readers;
    }

    private Reader createReaderFromResultSet(ResultSet resultSet) throws SQLException {
        Reader reader = new Reader();
        reader.setId(resultSet.getLong("reader_id"));
        reader.setName(resultSet.getString("name"));
        reader.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        reader.setBooks(new ArrayList<>());
        return reader;
    }

    private Book createBookFromResultSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("book_id"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        Date borrowDate = resultSet.getDate("borrow_date");
        if (borrowDate != null) {
            book.setBorrowDate(Optional.of(borrowDate.toLocalDate()));
        }
        book.setMaxBorrowTimeInDays(resultSet.getInt("max_borrow_time_in_days"));
        book.setRestricted(resultSet.getBoolean("restricted"));
        return book;
    }

}

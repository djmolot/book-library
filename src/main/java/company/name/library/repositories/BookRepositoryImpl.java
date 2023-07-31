package company.name.library.repositories;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.DaoLayerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Book add(Book book) {
        String sql = """
            INSERT INTO books (author, title, max_borrow_time_in_days, restricted) 
            VALUES (?, ?, ?, ?);
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, book.getAuthor());
                ps.setString(2, book.getTitle());
                ps.setInt(3, book.getMaxBorrowTimeInDays());
                ps.setBoolean(4, book.getRestricted());
                return ps;
            }, keyHolder);
            book.setId(getGeneratedId(keyHolder));
            return book;
        } catch (DataAccessException e) {
            log.error("Error during saving book to DB. " + e);
            throw new DaoLayerException("Error during saving book to DB. " + e);
        }
    }

    @Override
    public Optional<Book> getById(Long id) {
        String sql = """
            SELECT b.id, b.author, b.title, b.reader_id, b.borrow_date, b.max_borrow_time_in_days, 
            b.restricted, r.name as reader_name, r.birth_date 
            FROM books b LEFT JOIN readers r ON b.reader_id = r.id WHERE b.id = ?;
        """;
        try {
            Book book = jdbcTemplate.queryForObject(sql, this::mapRowToBook, id);
            return Optional.ofNullable(book);
        } catch (EmptyResultDataAccessException e) {
            log.info("Book with ID " + id + " does not exist in DB. " + e);
            return Optional.empty();
        } catch (DataAccessException e) {
            log.error("Error during getting book by ID {} from DB: {}", id, e.getLocalizedMessage());
            throw new DaoLayerException("Error during getting book by ID " + id
                    + "from DB: " + e.getLocalizedMessage());
        }
    }

    @Override
    public List<Book> getAll() {
        String sql = """
            SELECT b.id, b.author, b.title, b.reader_id, b.borrow_date, b.max_borrow_time_in_days, 
            b.restricted, r.name as reader_name, r.birth_date 
            FROM books b LEFT JOIN readers r ON b.reader_id = r.id ORDER BY b.id ASC;
        """;
        try {
            return jdbcTemplate.query(sql, this::mapRowToBook);
        } catch (DataAccessException e) {
            log.error("Error during getting all books from DB. " + e);
            throw new DaoLayerException("Error during getting all books from DB. " + e.getLocalizedMessage());
        }
    }

    @Override
    public Book updateBorrowDetails(Book book) {
        try {
            jdbcTemplate.update(
                    "UPDATE books SET reader_id = ?, borrow_date = ? WHERE id = ?;",
                    book.getReader().map(Reader::getId).orElse(null),
                    book.getBorrowDate().orElse(null),
                    book.getId());
            return book;
        } catch (DataAccessException e) {
            log.error("Failed to update the book in DB: {}", book);
            throw new DaoLayerException("Failed to update the book in DB: " + book + ". "
                    + e.getLocalizedMessage());
        }
    }

    @Override
    public List<Book> getBooksByReaderId(Long readerId) {
        String sql = """
            SELECT id, author, title, reader_id, borrow_date, max_borrow_time_in_days, restricted 
            FROM books WHERE reader_id = ? ORDER BY id ASC;
        """;
        try {
            return jdbcTemplate.query(sql, this::mapRowToBookWithoutReader, readerId);
        } catch (DataAccessException e) {
            log.error("Error during get books of reader with ID {} from DB.", readerId);
            throw new DaoLayerException("Error during get books of reader with ID "
                    + readerId + " from DB. " + e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteAllAndRestartIdSequence() {
        jdbcTemplate.update("DELETE FROM books");
        jdbcTemplate.update("ALTER SEQUENCE books_id_seq RESTART WITH 1");
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        return Optional.ofNullable(keyHolder.getKeys())
                .map(keys -> keys.get("id"))
                .map(Long.class::cast)
                .orElseThrow(() -> new DaoLayerException("Failed to save new Book to DB, no generated ID returned."));
    }

    private Book mapRowToBook(ResultSet resultSet, int rowNum) throws SQLException {
        var book = mapRowToBookWithoutReader(resultSet, rowNum);
        if (resultSet.getObject("reader_id", Long.class) != null) {
            Reader readerOfBook = mapReaderOfBook(resultSet);
            book.setReader(Optional.of(readerOfBook));
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
        Date borrowDate = resultSet.getDate("borrow_date");
        if (borrowDate != null) {
            book.setBorrowDate(Optional.of(borrowDate.toLocalDate()));
        } else {
            book.setReader(Optional.empty());
            book.setBorrowDate(Optional.empty());
        }
        book.setMaxBorrowTimeInDays(resultSet.getInt("max_borrow_time_in_days"));
        book.setRestricted(resultSet.getBoolean("restricted"));
        return book;
    }

    private Reader mapReaderOfBook(ResultSet resultSet) throws SQLException {
        var reader = new Reader();
        reader.setId(resultSet.getLong("reader_id"));
        reader.setName(resultSet.getString("reader_name"));
        reader.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        return reader;
    }

}

package company.name.library.repositories;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.DaoLayerException;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@Repository
public class BookRepositoryJdbcTemplImpl implements BookRepository {
    private JdbcTemplate jdbcTemplate;

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
        try {
            book.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        } catch (NullPointerException e) {
            throw new DaoLayerException("Failed to save new book, DB returned empty generated ID: " + e.getMessage());
        }
        return book;
    }

    @Override
    public Optional<Book> getById(Long id) {
        List<Book> results = jdbcTemplate.query(
                "SELECT b.id as book_id, b.author, b.title, b.reader_id, r.name as reader_name "
                        + "FROM books b LEFT JOIN readers r ON b.reader_id = r.id WHERE b.id = ?;",
                this::mapRowToBook,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public List<Book> getAll() {
        return jdbcTemplate.query(
                "SELECT b.id as book_id, b.author, b.title, b.reader_id as reader_id, "
                        + "r.name as reader_name "
                        + "FROM books b LEFT JOIN readers r ON b.reader_id = r.id ORDER BY b.id ASC;",
                this::mapRowToBook);
    }

    @Override
    public Book update(Book book) {
        if (book.getReader().isPresent()) {
            jdbcTemplate.update(
                    "UPDATE books SET reader_id = ? WHERE id = ?;",
                    book.getReader().get().getId(),
                    book.getId());
        } else {
            jdbcTemplate.update(
                    "UPDATE books SET reader_id = ? WHERE id = ?;",
                    null,
                    book.getId());
        }
        return book;
    }

    @Override
    public List<Book> getBooksByReaderId(Long readerId) {
        return jdbcTemplate.query(
                "SELECT id, author, title FROM books WHERE reader_id = ? ORDER BY id ASC;",
                this::mapRowToBookWithoutReader,
                readerId);
    }

    private Book mapRowToBook(ResultSet resultSet, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("book_id"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        if (resultSet.getObject("reader_id", Long.class) != null) {
            Reader reader = new Reader();
            reader.setId(resultSet.getLong("reader_id"));
            reader.setName(resultSet.getString("reader_name"));
            book.setReader(Optional.of(reader));
        } else {
            book.setReader(Optional.empty());
        }
        return book;
    }

    private Book mapRowToBookWithoutReader(ResultSet resultSet, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getObject("id", Long.class));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        return book;
    }

}

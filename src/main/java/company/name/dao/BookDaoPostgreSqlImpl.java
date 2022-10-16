package company.name.dao;

import company.name.exceptions.DaoLayerException;
import company.name.models.Book;
import company.name.models.Reader;
import company.name.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoPostgreSqlImpl implements BookDao {
    @Override
    public Book add(Book book) {
        String request = "INSERT INTO books (title, author) VALUES (?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getObject(1, Long.class);
                book.setId(id);
            }
            return book;
        } catch (SQLException e) {
            throw new DaoLayerException("Can't insert book " + book + " to DB. " + e.getMessage());
        }
    }

    @Override
    public Optional<Book> getById(Long id) {
        String request = "SELECT b.id as book_id, b.author, b.title, b.reader_id as reader_id, "
                + "r.name as reader_name "
                + "FROM books b LEFT JOIN readers r ON b.reader_id = r.id WHERE b.id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getCurrentBook(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't get book with ID" + id + " from DB", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> getAll() {
        List<Book> allBooks = new ArrayList<>();
        String query = "SELECT b.id as book_id, b.author, b.title, b.reader_id as reader_id, "
                + "r.name as reader_name "
                + "FROM books b LEFT JOIN readers r ON b.reader_id = r.id ORDER BY b.id ASC;";
        try (Connection connection = ConnectionUtil.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                allBooks.add(getCurrentBook(resultSet));
            }
            return allBooks;
        } catch (SQLException e) {
            throw new DaoLayerException(e);
        }
    }

    @Override
    public boolean update(Book book) {
        String request = "UPDATE books SET reader_id = ? WHERE id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(request)) {
            if (book.getReader() != null) {
                statement.setObject(1, book.getReader().getId());
            } else {
                statement.setNull(1, Types.BIGINT);
            }
            statement.setLong(2, book.getId());
            int numberOfUpdated = statement.executeUpdate();
            return numberOfUpdated != 0;
        } catch (SQLException e) {
            throw new DaoLayerException("Can't update reader of book in DB" + e.getMessage());
        }
    }

    @Override
    public List<Book> getBooksByReaderId(Long readerId) {
        List<Book> booksOfReader = new ArrayList<>();
        String request = "SELECT id, author, title FROM books WHERE reader_id = ? ORDER BY id ASC;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setLong(1, readerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                booksOfReader.add(getCurrentBookWithoutReader(resultSet));
            }
            return booksOfReader;
        } catch (SQLException e) {
            throw new DaoLayerException("Can't get all books of reader with ID " + readerId + ". "
                    + e.getMessage());
        }
    }

    private Book getCurrentBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getObject("book_id", Long.class));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        if (resultSet.getObject("reader_id", Long.class) != null) {
            Reader reader = new Reader();
            reader.setId(resultSet.getObject("reader_id", Long.class));
            reader.setName(resultSet.getString("reader_name"));
            book.setReader(reader);
        }
        return book;
    }

    private Book getCurrentBookWithoutReader(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getObject("id", Long.class));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        return book;
    }
}

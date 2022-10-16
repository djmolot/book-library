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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDaoPostgreSqlImpl implements ReaderDao {
    @Override
    public Reader add(Reader reader) throws DaoLayerException {
        String request = "INSERT INTO readers (name) VALUES (?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(request,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, reader.getName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                reader.setId(resultSet.getObject(1, Long.class));
            }
            return reader;
        } catch (SQLException e) {
            throw new DaoLayerException("Can't insert reader " + reader + " to DB. "
                    + e.getMessage());
        }
    }

    @Override
    public Optional<Reader> getById(Long id) {
        String request = "SELECT * FROM readers WHERE id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getCurrentReader(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Can't get reader with ID " + id + " from DB", e);
        }
    }

    @Override
    public List<Reader> getAll() {
        List<Reader> allReaders = new ArrayList<>();
        String query = "SELECT r.id as reader_id, r.name, b.id as book_id, b.title, b.author "
                + "FROM books b RIGHT JOIN readers r ON b.reader_id = r.id;";
        try (Connection connection = ConnectionUtil.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                processCurrentLine(resultSet, allReaders);
            }
            return allReaders;
        } catch (SQLException e) {
            throw new DaoLayerException("Can't get all readers from DB. " + e.getMessage());
        }
    }

    @Override
    public void update(Reader reader) {

    }

    @Override
    public Optional<Reader> getReaderByBookId(Long bookId) {
        String request = "SELECT b.reader_id as reader_id, r.name as reader_name "
                + "FROM books b JOIN readers r ON b.reader_id = r.id WHERE b.id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setLong(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Reader reader = new Reader();
                reader.setId(resultSet.getObject("reader_id", Long.class));
                reader.setName(resultSet.getString("reader_name"));
                return Optional.of(reader);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoLayerException("Can't get reader of book with ID" + bookId + " from DB"
                    + e.getMessage());
        }
    }

    private Reader getCurrentReader(ResultSet resultSet) throws SQLException {
        Reader reader = new Reader();
        reader.setId(resultSet.getObject("id", Long.class));
        reader.setName(resultSet.getString("name"));
        return reader;

    }

    private void processCurrentLine(ResultSet resultSet, List<Reader> allReaders)
            throws SQLException {
        Reader reader = new Reader();
        reader.setBooks(new ArrayList<>());
        reader.setId(resultSet.getObject("reader_id", Long.class));
        reader.setName(resultSet.getString("name"));
        if (resultSet.getObject("book_id", Long.class) == null) {
            allReaders.add(reader);
            return;
        }
        Book book = new Book();
        book.setId(resultSet.getObject("book_id", Long.class));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        if (allReaders.contains(reader)) {
            int indexOf = allReaders.indexOf(reader);
            Reader reader1 = allReaders.get(indexOf);
            reader1.getBooks().add(book);
            return;
        }
        reader.getBooks().add(book);
        allReaders.add(reader);
    }

}

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
                reader.setId(resultSet.getLong(1));
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
                return Optional.of(mapResultSetToReaderWithoutBooks(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoLayerException("Can't get reader with ID " + id + " from DB"
                    + e.getMessage());
        }
    }

    @Override
    public List<Reader> getAll() {
        String query = "SELECT r.id as reader_id, r.name, b.id as book_id, b.title, b.author "
                + "FROM books b RIGHT JOIN readers r ON b.reader_id = r.id;";
        try (Connection connection = ConnectionUtil.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            List<Reader> allReaders = mapResultSetToReaders(resultSet);
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
                reader.setId(resultSet.getLong("reader_id"));
                reader.setName(resultSet.getString("reader_name"));
                return Optional.of(reader);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoLayerException("Can't get reader of book with ID" + bookId + " from DB"
                    + e.getMessage());
        }
    }

    private Reader mapResultSetToReaderWithoutBooks(ResultSet resultSet) throws SQLException {
        Reader reader = new Reader();
        reader.setId(resultSet.getLong("id"));
        reader.setName(resultSet.getString("name"));
        return reader;

    }

    private List<Reader> mapResultSetToReaders(ResultSet resultSet)
            throws SQLException {
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
    }

}

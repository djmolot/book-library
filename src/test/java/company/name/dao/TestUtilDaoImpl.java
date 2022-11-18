package company.name.dao;

import company.name.exceptions.DaoLayerException;
import company.name.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestUtilDaoImpl implements TestUtilDao {
    @Override
    public int deleteAllBooks() {
        String request = "DELETE FROM books;";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoLayerException("Can't delete all books from test DB. "
                    + e.getMessage());
        }
    }

        @Override
    public int deleteAllReaders() {
        String request = "DELETE FROM readers;";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoLayerException("Can't delete all readers from test DB. "
                    + e.getMessage());
        }
    }
}

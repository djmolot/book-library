package company.name.dao;

import company.name.exceptions.DaoLayerException;
import company.name.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestUtilDao {
    public int deleteAllBooks() {
        return deleteAllEntriesFromTable("books");
    }

    public int deleteAllReaders() {
        return deleteAllEntriesFromTable("readers");
    }

    public int deleteAllEntriesFromTable(String tableName) {
        String request = String.format("TRUNCATE %s RESTART IDENTITY CASCADE;", tableName);
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(request)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoLayerException(
                    String.format("Can't delete all entries from DB table %s", tableName) + e.getMessage()
            );
        }
    }
}

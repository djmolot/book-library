package company.name.util;

import company.name.exceptions.DaoLayerException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/booklibrary";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DaoLayerException("Can't create connection to DB" + e.getMessage());
        }
    }
}

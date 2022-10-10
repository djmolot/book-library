package company.name.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//https://www.postgresqltutorial.com/postgresql-jdbc/connecting-to-postgresql-database/
public class ConnectionUtil2 {
    private static final String url = "jdbc:postgresql://localhost:5432/booklibrary";
    private static final String user = "postgres";
    private static final String password = "1234";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public static Connection connect() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Can't create connection to DB", e);
        }
        return connection;
    }
}

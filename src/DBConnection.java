
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        String url = requiredEnvironmentVariable("DB_URL");
        String user = requiredEnvironmentVariable("DB_USER");
        String password = requiredEnvironmentVariable("DB_PASSWORD");

        return DriverManager.getConnection(url, user, password);
    }

    private static String requiredEnvironmentVariable(String name)
            throws SQLException {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new SQLException(name + " environment variable is required");
        }
        return value;
    }
}

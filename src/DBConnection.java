
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL", "jdbc:mysql://localhost:3306/helpdesk_db");

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection() {

        if (PASSWORD == null || PASSWORD.isEmpty()) {
            System.out.println("Database password not configured!");
            return null;
        }

        try {
            Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println(
                    "MySQL Database Connected Successfully!");

            return con;

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Help Desk System is Ready!");

            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
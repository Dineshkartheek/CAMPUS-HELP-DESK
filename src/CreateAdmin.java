
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.Console;
import java.util.Arrays;

public class CreateAdmin {

    public static void main(String[] args) {

        Console console = System.console();

        if (console == null) {
            System.out.println(
                "Please run this program in a terminal."
            );
            return;
        }

        System.out.println("===== CAMPUSCARE ADMIN SETUP =====");
        System.out.println("1. Create Main Admin");
        System.out.println("2. Create Regular Admin");

        String option = console.readLine("Choose option: ");

        int choice;

        try {
            choice = Integer.parseInt(option);
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice!");
            return;
        }

        if (choice != 1 && choice != 2) {
            System.out.println("Invalid choice!");
            return;
        }

        String name = console.readLine("Enter Admin Name: ").trim();

        String email = console.readLine("Enter Admin Email: ").trim();

        char[] passwordChars =
            console.readPassword("Create Admin Password: ");

        if (passwordChars == null) {
            System.out.println("Password input failed!");
            return;
        }

        try {
            if (name.isEmpty()
                    || email.isEmpty()
                    || passwordChars.length < 8) {

                System.out.println(
                    "Invalid details! Password must have at least 8 characters."
                );
                return;
            }

            String password = new String(passwordChars);

            String role = (choice == 1)
                    ? "MAIN_ADMIN"
                    : "ADMIN";

            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            String sql =
                "INSERT INTO users " +
                "(full_name, email, password, role) " +
                "VALUES (?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection()) {

                if (con == null) {
                    System.out.println(
                        "Database connection unavailable!"
                    );
                    return;
                }

                try (PreparedStatement ps =
                        con.prepareStatement(sql)) {

                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setString(3, hashedPassword);
                    ps.setString(4, role);

                    ps.executeUpdate();

                    System.out.println(
                        role + " account created successfully!"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println("Account creation failed!");

            if (e instanceof java.sql.SQLException
                    && ((java.sql.SQLException) e).getErrorCode() == 1062) {
                System.out.println(
                    "This email is already registered!"
                );
            } else {
                e.printStackTrace();
            }

        } finally {
            Arrays.fill(passwordChars, ' ');
        }
    }
}

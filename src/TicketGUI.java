
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TicketGUI extends JFrame {

    private JTextField nameField, emailField;
    private JComboBox<String> categoryBox;
    private JTextArea descriptionArea;

    public TicketGUI() {

        setTitle("Help Desk Management System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("STUDENT TICKET REGISTRATION");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        nameField = new JTextField();
        emailField = new JTextField();

        String[] categories = {
            "Wi-Fi", "Hardware", "Software", "Network", "Other"
        };

        categoryBox = new JComboBox<>(categories);
        descriptionArea = new JTextArea(4, 20);

        JButton submitButton = new JButton("Submit Ticket");

        panel.add(new JLabel("Student Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);

        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));

        panel.add(new JLabel(""));
        panel.add(submitButton);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitTicket());
    }

    private void submitTicket() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String category = (String) categoryBox.getSelectedItem();
        String description = descriptionArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill all fields!");
            return;
        }

        String sql = "INSERT INTO tickets " +
            "(student_name, email, category, description) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, category);
            pst.setString(4, description);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Ticket Submitted Successfully!");

            nameField.setText("");
            emailField.setText("");
            descriptionArea.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TicketGUI().setVisible(true);
        });
    }
}

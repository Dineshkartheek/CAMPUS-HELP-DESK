
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateTicket extends JFrame {

    private JTextField ticketIdField;
    private JComboBox<String> statusBox;

    public UpdateTicket() {

        setTitle("Update Ticket Status");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        ticketIdField = new JTextField();

        String[] statuses = {
            "Pending", "In Progress", "Resolved"
        };

        statusBox = new JComboBox<>(statuses);

        JButton updateButton = new JButton("Update Status");

        panel.add(new JLabel("Enter Ticket ID:"));
        panel.add(ticketIdField);

        panel.add(new JLabel("Select New Status:"));
        panel.add(statusBox);

        panel.add(new JLabel(""));
        panel.add(updateButton);

        add(panel);

        updateButton.addActionListener(e -> updateStatus());
    }

    private void updateStatus() {

        String ticketId = ticketIdField.getText().trim();
        String status = (String) statusBox.getSelectedItem();

        if (ticketId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter Ticket ID!");
            return;
        }

        String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, status);
            pst.setInt(2, Integer.parseInt(ticketId));

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                    "Ticket Status Updated Successfully!");
                ticketIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Ticket ID Not Found!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Enter a valid numeric Ticket ID!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new UpdateTicket().setVisible(true);
        });
    }
}

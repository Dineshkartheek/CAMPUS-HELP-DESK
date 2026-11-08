
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewTickets extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewTickets() {

        setTitle("View All Tickets - Help Desk Management System");
        setSize(900, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {
            "Ticket ID", "Student Name", "Email",
            "Category", "Description", "Status"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton refreshButton = new JButton("Refresh Tickets");

        refreshButton.addActionListener(e -> loadTickets());

        add(scrollPane, BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        loadTickets();
    }

    private void loadTickets() {

        model.setRowCount(0);

        String sql = "SELECT ticket_id, student_name, email, " +
                     "category, description, status FROM tickets";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Object[] row = {
                    rs.getInt("ticket_id"),
                    rs.getString("student_name"),
                    rs.getString("email"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getString("status")
                };

                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Unable to load tickets: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new ViewTickets().setVisible(true);
        });
    }
}

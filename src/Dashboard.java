
import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {

        setTitle("Help Desk Management System");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("HELP DESK MANAGEMENT SYSTEM",
                SwingConstants.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 20));

        JButton registerButton = new JButton("Register New Ticket");
        JButton viewButton = new JButton("View All Tickets");
        JButton updateButton = new JButton("Update Ticket Status");
        JButton exitButton = new JButton("Exit");

        panel.add(title);
        panel.add(registerButton);
        panel.add(viewButton);
        panel.add(updateButton);
        panel.add(exitButton);

        add(panel);

        registerButton.addActionListener(e -> {
            new TicketGUI().setVisible(true);
        });

        viewButton.addActionListener(e -> {
            new ViewTickets().setVisible(true);
        });

        updateButton.addActionListener(e -> {
            new UpdateTicket().setVisible(true);
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}

package pages;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private String username;
    private String userRole;

    public AdminDashboard(String username, String userRole) {
        this.username = username;
        this.userRole = userRole;

        setTitle("ComFile - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(welcomeLabel);

        // User info
        JLabel userLabel = new JLabel("Logged in as: " + username + " (" + userRole + ")");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(new Color(130, 130, 140));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(userLabel);

        // Placeholder text
        JLabel placeholderLabel = new JLabel("Admin features will be implemented soon...");
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        placeholderLabel.setForeground(Color.GRAY);
        placeholderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(placeholderLabel);

        // Add to frame
        add(mainPanel);
        setVisible(true);
    }
}
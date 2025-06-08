package pages.Dashboard;

import javax.swing.*;
import java.awt.*;
import components.NavigationBar;
import components.NavigationBar.NavigationListener;
// import pages.BackupConfiguration;
import pages.ManageDocuments.MyDocuments;
import pages.Login;

public class SearchDocuments extends JFrame implements NavigationListener {
    private NavigationBar navigationBar;
    private String username;
    private String userRole;

    public SearchDocuments(String username, String userRole) {
        this.username = username;
        this.userRole = userRole;
        
        setTitle("ComFile - Search Documents");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Initialize NavigationBar
        navigationBar = new NavigationBar();
        navigationBar.setNavigationListener(this);
        navigationBar.setUserInfo(username, userRole);
        navigationBar.setActivePage("Documents");  // Set Documents as active since this is a document-related page

        // Create main content panel with placeholder text
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        JLabel placeholderLabel = new JLabel("Search Documents Page - Coming Soon", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(placeholderLabel, BorderLayout.CENTER);
        
        // Add components to frame
        setLayout(new BorderLayout());
        add(navigationBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }

    // NavigationListener methods
    @Override
    public void onHomeClicked() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new Dashboard(username, userRole);
        });
    }

    @Override
    public void onDocumentsClicked() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new MyDocuments(username, userRole);
        });
    }

    @Override
    public void onBackupClicked() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new BackupConfiguration(username, userRole);
        });
    }

    @Override
    public void onNotificationClicked() {
        System.out.println("Notification clicked");
    }

    @Override
    public void onLogoutClicked() {
        // Close current window
        this.dispose();
        
        // Redirect to Login page
        SwingUtilities.invokeLater(() -> {
            new Login();
        });
    }
} 
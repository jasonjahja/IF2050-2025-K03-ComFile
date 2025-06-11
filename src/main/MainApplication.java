package main;

import components.NavigationBar;
// import java.awt.event.ComponentEvent;
import pages.ManageDocuments.MyDocuments;
import pages.Admin.AdminDashboard;
import pages.Admin.UserManagementDashboard;
import pages.Admin.AddUserPage;
import pages.Admin.EditUserPage;
import pages.Login;

import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame implements NavigationBar.NavigationListener {
    private NavigationBar navigationBar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private MyDocuments documentsPage;
    private String currentUsername;
    private String currentUserRole;

    private static MainApplication instance;

    public MainApplication(String username, String role) {
        instance = this;
        this.currentUsername = username;
        this.currentUserRole = role;
        initializeApplication();
        createPages(username, role);
        setupLayout();
        setVisible(true);
    }

    private void initializeApplication() {
        setTitle("ComFile - Document Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        navigationBar = new NavigationBar();
        navigationBar.setNavigationListener(this);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
    }

    private void createPages(String username, String role) {
        // Set user info di navigation bar
        navigationBar.setUserInfo(username, role);

        // Standard pages for all users
        JPanel homePage;
        if ("Admin".equals(role)) {
            // Admin gets admin dashboard as home page
            homePage = new AdminDashboard(username, role, contentPanel);
        } else {
            // Regular users get standard home page
            homePage = createPlaceholderPage("Home Page", "Welcome to the Document Management System");
        }
        
        documentsPage = new MyDocuments(); // Document management page
        JPanel backupPage = createPlaceholderPage("Backup Page", "Backup and restore your documents");

        contentPanel.add(homePage, "HOME");
        contentPanel.add(documentsPage, "DOCUMENTS");
        contentPanel.add(backupPage, "BACKUP");

        // Add admin-specific pages if user is admin
        if ("Admin".equals(role)) {
            // Add admin pages - they will be dynamically added when needed
            // This allows for clean navigation between admin features
        }

        cardLayout.show(contentPanel, "HOME");
    }

    private JPanel createPlaceholderPage(String title, String description) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(248, 249, 250));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(248, 249, 250));

        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBackground(Color.WHITE);
        contentBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(50, 50, 50, 50)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentBox.add(titleLabel);
        contentBox.add(Box.createVerticalStrut(10));
        contentBox.add(descLabel);

        centerPanel.add(contentBox);
        page.add(centerPanel, BorderLayout.CENTER);

        return page;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(navigationBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    // ========== Public method to switch to admin pages ==========
    public void showAdminPage(String pageName) {
        if (!"Admin".equals(currentUserRole)) {
            JOptionPane.showMessageDialog(this, "Access denied. Admin privileges required.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        cardLayout.show(contentPanel, pageName);
    }

    // ========== Navigation Listener ==========

    @Override
    public void onHomeClicked() {
        cardLayout.show(contentPanel, "HOME");
    }

    @Override
    public void onDocumentsClicked() {
        if (documentsPage != null) {
            documentsPage.refreshDocuments();
        }
        cardLayout.show(contentPanel, "DOCUMENTS");
    }

    @Override
    public void onBackupClicked() {
        cardLayout.show(contentPanel, "BACKUP");
    }

    @Override
    public void onNotificationClicked() {
        JOptionPane.showMessageDialog(this,
                "You have 3 new notifications",
                "Notifications",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onLogoutClicked() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            dispose(); // Tutup window utama
            new Login(); // Tampilkan login lagi
        }
    }

    // ========== Untuk dipanggil setelah login ==========
    public static void startWithUser(String username, String role) {
        SwingUtilities.invokeLater(() -> new MainApplication(username, role));
    }

    // ========== Get instance for admin navigation ==========
    public static MainApplication getInstance() {
        return instance;
    }

    // ========== Public access to contentPanel and cardLayout for admin pages ==========
    public JPanel getContentPanel() {
        return contentPanel;
    }
    
    public CardLayout getCardLayout() {
        return cardLayout;
    }

    // ========== Main Awal: Login page ==========
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
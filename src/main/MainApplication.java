package main;

import components.NavigationBar;
// import java.awt.event.ComponentEvent;
import pages.ManageDocuments.MyDocuments;
import pages.Login;
import utils.DocumentDAO;
import pages.ManageDocuments.Document;

import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame implements NavigationBar.NavigationListener {
    private NavigationBar navigationBar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private MyDocuments documentsPage;

    private static MainApplication instance;

    public MainApplication(String username, String role) {
        instance = this;

        Document.users = DocumentDAO.loadAllUsers();
        Document.currentUser = Document.getUser(username);
        System.out.println("✅ SET currentUser: " + Document.currentUser.getUsername()
                + " | Role: " + Document.currentUser.getRole()
                + " | Dept: " + Document.currentUser.getDepartment());
        System.out.println("✅ CurrentUser loaded: " + Document.currentUser.getUsername() +
                " | Role: " + Document.currentUser.getRole() +
                " | Dept: " + Document.currentUser.getDepartment());

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

        // Tambahkan halaman-halaman
        JPanel homePage = createPlaceholderPage("Home Page", "Welcome to the Document Management System");
        JPanel documentsPage = new MyDocuments();
        JPanel backupPage = createPlaceholderPage("Backup Page", "Backup and restore your documents");

        contentPanel.add(homePage, "HOME");
        contentPanel.add(documentsPage, "DOCUMENTS");
        contentPanel.add(backupPage, "BACKUP");

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

    // ========== Main Awal: Login page ==========
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
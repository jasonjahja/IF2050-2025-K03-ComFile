package main;

import components.NavigationBar;
import pages.ManageDocuments.MyDocuments;
import pages.Login;
import utils.DocumentDAO;
import pages.ManageDocuments.Document;
import pages.Dashboard.Dashboard;
import pages.Admin.AdminDashboard;

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

        // Setup user from accesscontrol branch
        Document.users = DocumentDAO.loadAllUsers();
        Document.currentUser = Document.getUser(username);
        System.out.println("✅ SET currentUser: " + Document.currentUser.getUsername()
                + " | Role: " + Document.currentUser.getRole()
                + " | Dept: " + Document.currentUser.getDepartment());

        initializeApplication();
        createPages(username, role);
        setupLayout();
        setVisible(true);
    }

    public void initializeApplication() {
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

    public void createPages(String username, String role) {
        navigationBar.setUserInfo(username, role);
        navigationBar.setVisible(true);
        java.awt.Container parentContainer = contentPanel;

        if (role.equalsIgnoreCase("Admin")) {
            AdminDashboard adminDashboard = new AdminDashboard(username, role, parentContainer);
            contentPanel.add(adminDashboard, "ADMIN_DASHBOARD");
            cardLayout.show(contentPanel, "ADMIN_DASHBOARD");
        } else {
            Dashboard dashboard = new Dashboard(username, role);
            contentPanel.add(dashboard, "HOME");
            cardLayout.show(contentPanel, "HOME");
        }

        documentsPage = new MyDocuments();
        contentPanel.add(documentsPage, "DOCUMENTS");
    }

    public JPanel createPlaceholderPage(String title, String description) {
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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentBox.add(titleLabel);
        contentBox.add(Box.createVerticalStrut(10));
        contentBox.add(descLabel);

        centerPanel.add(contentBox);
        page.add(centerPanel, BorderLayout.CENTER);

        return page;
    }

    public void setupLayout() {
        setLayout(new BorderLayout());
        add(navigationBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    // ========== Navigation Listener ==========

    public MainApplication() {
        instance = this;
        setTitle("ComFile - Document Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Tambahkan ini
        initializeApplication();
        setupLayout();
    }


    public void showLoginPage() {
        if (contentPanel == null) {
            System.err.println("❌ contentPanel belum diinisialisasi!");
            return;
        }

        navigationBar.setVisible(false);

        contentPanel.removeAll();
        contentPanel.add(new Login(this));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    public void onHomeClicked() {
        if (Document.currentUser != null && Document.currentUser.getRole().equalsIgnoreCase("Admin")) {
            cardLayout.show(contentPanel, "ADMIN_DASHBOARD");
        } else {
            cardLayout.show(contentPanel, "HOME");
        }
    }

    @Override
    public void onDocumentsClicked() {
        if (documentsPage != null) {
            documentsPage.refreshDocumentsAsync(); // Pakai versi async
        }
        cardLayout.show(contentPanel, "DOCUMENTS");
    }

    @Override
    public void onLogoutClicked() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            dispose();
            JFrame frame = new JFrame("ComFile Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1080, 720);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new Login(frame));
            frame.setVisible(true);
        }
    }

    public static void startWithUser(String username, String role) {
        SwingUtilities.invokeLater(() -> new MainApplication(username, role));
    }

    // ========== Main Awal: Login page ==========
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        MainApplication app = new MainApplication();
        app.showLoginPage(); // Tampilkan login sebagai panel pertama
        app.setVisible(true);
    });
}

    public static MainApplication getInstance() {
        return instance;
    }
}

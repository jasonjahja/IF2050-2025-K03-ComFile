package main;

import components.NavigationBar;
import pages.ManageDocuments.MyDocuments;
import pages.Login;
import utils.DocumentDAO;
import pages.ManageDocuments.Document;
import pages.Dashboard.Dashboard;
import pages.Admin.AdminDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
        showLogoutConfirmation(this);
    }

    private void showLogoutConfirmation(Window parent) {
        JLayeredPane layeredPane = getLayeredPane();

        // Buat dark overlay
        JPanel darkOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 120)); // Semi-transparan
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        darkOverlay.setOpaque(false);
        darkOverlay.setBounds(0, 0, getWidth(), getHeight());

        layeredPane.add(darkOverlay, JLayeredPane.MODAL_LAYER);
        layeredPane.repaint();

        // Buat dialog logout
        JDialog dialog = new JDialog(parent, "Logout Confirmation", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(400, 420);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(30, 30, 30, 30)
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("img/logout-illustration.png"));
        iconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createVerticalStrut(20));

        JLabel msgLabel = new JLabel("<html>You will be <font color='#D62955'>logged out</font> from this account</html>");
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(msgLabel);

        panel.add(Box.createVerticalStrut(10));

        JLabel confirmLabel = new JLabel("Do you want to proceed?");
        confirmLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        confirmLabel.setForeground(new Color(120, 120, 120));
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(confirmLabel);

        panel.add(Box.createVerticalStrut(30));

        // Log Out button
        JButton logoutBtn = new JButton("Log Out") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(214, 41, 85)); // Red
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoutBtn.setOpaque(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutBtn.addActionListener(e -> {
            dialog.dispose();
            layeredPane.remove(darkOverlay);
            layeredPane.repaint();

            dispose();
            JFrame frame = new JFrame("ComFile Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1080, 720);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new Login(frame));
            frame.setVisible(true);
        });

        // Cancel button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setForeground(new Color(214, 41, 85));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(214, 41, 85)));
        cancelBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        cancelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);


        cancelBtn.addActionListener(e -> {
            dialog.dispose();
            layeredPane.remove(darkOverlay);
            layeredPane.repaint();
        });

        panel.add(logoutBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cancelBtn);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
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

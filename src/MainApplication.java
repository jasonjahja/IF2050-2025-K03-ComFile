import Dashboard.SearchDocuments;
import components.NavigationBar;
import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame implements NavigationBar.NavigationListener {
    private NavigationBar navigationBar;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainApplication() {
        initializeApplication();
        createPages();
        setupLayout();
        setVisible(true);
    }

    private void initializeApplication() {
        setTitle("Document Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize navigation bar
        navigationBar = new NavigationBar();
        navigationBar.setNavigationListener(this);
        navigationBar.setUserInfo("User", "Employee");

        // Initialize content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
    }

    private void createPages() {
        // Create Documents page
        // Pages
        SearchDocuments documentsPage = new SearchDocuments();

        // Create placeholder pages
        JPanel homePage = createPlaceholderPage("Home Page", "Welcome to the Document Management System");
        JPanel backupPage = createPlaceholderPage("Backup Page", "Backup and restore your documents");

        // Add pages to content panel
        contentPanel.add(homePage, "HOME");
        contentPanel.add(documentsPage, "DOCUMENTS");
        contentPanel.add(backupPage, "BACKUP");

        // Show home page by default
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

    // NavigationListener implementation
    @Override
    public void onHomeClicked() {
        cardLayout.show(contentPanel, "HOME");
        System.out.println("Navigated to Home");
    }

    @Override
    public void onDocumentsClicked() {
        cardLayout.show(contentPanel, "DOCUMENTS");
        System.out.println("Navigated to Documents");
    }

    @Override
    public void onBackupClicked() {
        cardLayout.show(contentPanel, "BACKUP");
        System.out.println("Navigated to Backup");
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
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainApplication();
        });
    }
}
package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NavigationBar extends JPanel {
    private NavigationListener navigationListener;
    private JLabel nameLabel;
    private JLabel roleLabel;
    private Map<String, JLabel> navLinks;
    private Color activeColor = new Color(61, 90, 254);
    private Color inactiveColor = new Color(50, 40, 70);
    private String currentPage = "";
    
    public interface NavigationListener {
        void onHomeClicked();
        void onDocumentsClicked();
        void onBackupClicked();
        void onNotificationClicked();
        void onLogoutClicked();
    }
    
    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }
    
    public void setUserInfo(String name, String role) {
        nameLabel.setText(name);
        roleLabel.setText(role);
    }
    
    private ImageIcon createImageIcon(String path) {
        try {
            String imgPath = System.getProperty("user.dir") + "/img/" + path;
            return new ImageIcon(imgPath);
        } catch (Exception e) {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private void setActivePage(String page) {
        currentPage = page;
        for (Map.Entry<String, JLabel> entry : navLinks.entrySet()) {
            if (entry.getKey().equals(page)) {
                entry.getValue().setForeground(activeColor);
            } else {
                entry.getValue().setForeground(inactiveColor);
            }
        }
    }
    
    public NavigationBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        setBackground(Color.WHITE);
        navLinks = new HashMap<>();

        // Logo 
        ImageIcon logoIcon = createImageIcon("logo.png");
        if (logoIcon != null) {
            Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(scaledLogo));
            logo.setBorder(new EmptyBorder(5, 20, 5, 0));
            logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            logo.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (navigationListener != null) {
                        navigationListener.onHomeClicked();
                        setActivePage("Home");
                    }
                }
            });
            add(logo, BorderLayout.WEST);
        }

        // Right Container
        JPanel rightContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightContainer.setBackground(Color.WHITE);

        // Menu links
        String[] links = {"Home", "Documents", "Backup"};
        for (String text : links) {
            JLabel link = new JLabel(text);
            link.setFont(new Font("Arial", Font.PLAIN, 14));
            link.setForeground(inactiveColor);
            link.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Add click listeners
            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (navigationListener != null) {
                        switch (text) {
                            case "Home":
                                navigationListener.onHomeClicked();
                                setActivePage("Home");
                                break;
                            case "Documents":
                                navigationListener.onDocumentsClicked();
                                setActivePage("Documents");
                                break;
                            case "Backup":
                                navigationListener.onBackupClicked();
                                setActivePage("Backup");
                                break;
                        }
                    }
                }
            });
            
            navLinks.put(text, link);
            rightContainer.add(link);
        }

        // Bell Icon
        ImageIcon bellIcon = createImageIcon("icon-bell.png");
        if (bellIcon != null) {
            Image scaledBell = bellIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            JLabel bellLabel = new JLabel(new ImageIcon(scaledBell));
            bellLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bellLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (navigationListener != null) {
                        navigationListener.onNotificationClicked();
                    }
                }
            });
            rightContainer.add(bellLabel);
        }

        // Profile Card
        JPanel profileCard = new JPanel(new BorderLayout());
        profileCard.setBackground(Color.WHITE);
        profileCard.setPreferredSize(new Dimension(180, 50));
        profileCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        
        // User Icon
        ImageIcon userIcon = createImageIcon("icon-user.png");
        if (userIcon != null) {
            Image scaledUser = userIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            JLabel userLabel = new JLabel(new ImageIcon(scaledUser));
            userLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
            profileCard.add(userLabel, BorderLayout.WEST);
        }

        // Name + Role
        JPanel namePanel = new JPanel();
        namePanel.setBackground(Color.WHITE);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        nameLabel = new JLabel("User");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel = new JLabel("Employee");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        namePanel.add(nameLabel);
        namePanel.add(roleLabel);
        profileCard.add(namePanel, BorderLayout.CENTER);

        // Logout Icon
        ImageIcon logoutIcon = createImageIcon("icon-logout.png");
        if (logoutIcon != null) {
            Image scaledLogout = logoutIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel logoutLabel = new JLabel(new ImageIcon(scaledLogout));
            logoutLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            logoutLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (navigationListener != null) {
                        navigationListener.onLogoutClicked();
                    }
                }
            });
            profileCard.add(logoutLabel, BorderLayout.EAST);
        }

        rightContainer.add(profileCard);
        add(rightContainer, BorderLayout.EAST);

        // Set Home as default active page
        setActivePage("Home");
    }
}
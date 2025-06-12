package components;

import utils.ImageLoader;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        void onLogoutClicked();
    }
    
    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }
    
    public void setUserInfo(String name, String role) {
        nameLabel.setText(name);
        roleLabel.setText(role);
    }
    
    public void setActivePage(String page) {
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
        ImageIcon logoIcon = ImageLoader.loadScaledImage("img/logo.png", 40, 40);
        if (logoIcon != null) {
            JLabel logo = new JLabel(logoIcon);
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
        String[] links = {"Home", "Documents"};
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
                        }
                    }
                }
            });
            
            navLinks.put(text, link);
            rightContainer.add(link);
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
        ImageIcon userIcon = ImageLoader.loadScaledImage("img/icon-user.png", 28, 28);
        if (userIcon != null) {
            JLabel userLabel = new JLabel(userIcon);
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
        ImageIcon logoutIcon = ImageLoader.loadScaledImage("img/log-out.png", 20, 20);
        if (logoutIcon != null) {
            JLabel logoutLabel = new JLabel(logoutIcon);
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
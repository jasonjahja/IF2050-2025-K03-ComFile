package pages;

import pages.Dashboard.Dashboard;
import main.MainApplication;
import utils.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.sql.*;

public class Login extends JFrame {
    private final PlaceholderTextField usernameField;
    private final PlaceholderPasswordField passwordField;

    public Login() {
        setTitle("ComFile Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // === LEFT: Illustration ===
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        add(imageLabel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = getSize();
                int imgWidth = size.width / 2;
                int imgHeight = size.height;
                ImageIcon rawIcon = new ImageIcon("img/login-image.png");
                Image scaled = rawIcon.getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            }
        });

        // === RIGHT: Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel contentWrapper = new JPanel(null);
        contentWrapper.setPreferredSize(new Dimension(520, 600));
        contentWrapper.setBackground(Color.WHITE);

        ImageIcon logoIcon = new ImageIcon("img/logo.png");
        Image logoImg = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(logoImg));
        logo.setBounds(60, 10, 40, 40);
        contentWrapper.add(logo);

        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcome.setBounds(60, 130, 400, 40);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        contentWrapper.add(welcome);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameLabel.setBounds(60, 220, 400, 20);
        contentWrapper.add(usernameLabel);

        usernameField = new PlaceholderTextField("oliviarhye");
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setBounds(60, 245, 400, 44);
        contentWrapper.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordLabel.setBounds(60, 330, 400, 20);
        contentWrapper.add(passwordLabel);

        JPanel passwordWrapper = new JPanel(new BorderLayout());
        passwordWrapper.setBounds(60, 355, 400, 44);
        passwordWrapper.setBackground(Color.WHITE);
        passwordWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        passwordField = new PlaceholderPasswordField("Enter your password");
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(new EmptyBorder(10, 10, 10, 10));
        passwordWrapper.add(passwordField, BorderLayout.CENTER);

        ImageIcon eyeIcon = new ImageIcon(new ImageIcon("img/eye.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        ImageIcon eyeSlashIcon = new ImageIcon(new ImageIcon("img/eye-slash.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));

        JLabel eyeToggle = new JLabel(eyeIcon);
        eyeToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeToggle.setHorizontalAlignment(SwingConstants.CENTER);
        eyeToggle.setPreferredSize(new Dimension(40, 44));
        passwordWrapper.add(eyeToggle, BorderLayout.EAST);
        contentWrapper.add(passwordWrapper);

        eyeToggle.addMouseListener(new MouseAdapter() {
            private boolean isVisible = false;
            @Override
            public void mouseClicked(MouseEvent e) {
                isVisible = !isVisible;
                passwordField.setEchoChar(isVisible ? (char) 0 : '\u2022');
                eyeToggle.setIcon(isVisible ? eyeSlashIcon : eyeIcon);
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(60, 445, 400, 44);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(Color.decode("#5A6ACF"));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        
        contentWrapper.add(loginButton);

        gbc.gridy = 0;
        formPanel.add(contentWrapper, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            String role = getUserRoleFromDatabase(username, password);
            if (role != null) {
                JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + role);
                this.dispose();
                
                // Use MainApplication for all users (including admins)
                MainApplication.startWithUser(username, role);
            } else {
                JOptionPane.showMessageDialog(this, "Username/password salah!");
            }
        });

        setVisible(true);
    }

    private String getUserRoleFromDatabase(String username, String password) {
        try (Connection conn = DBConnection.connect()) {
            if (conn == null) return null;
            String query = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;
        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.setFont(getFont().deriveFont(Font.PLAIN));
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 5, getHeight() / 2 + getFont().getSize() / 2 - 4);
                g2.dispose();
            }
        }
    }

    private static class PlaceholderPasswordField extends JPasswordField {
        private final String placeholder;
        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0 && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.setFont(getFont().deriveFont(Font.PLAIN));
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 5, getHeight() / 2 + getFont().getSize() / 2 - 4);
                g2.dispose();
            }
        }
    }
}
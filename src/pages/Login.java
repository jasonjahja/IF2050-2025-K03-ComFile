package pages;

import main.MainApplication;
import pages.Dashboard.Dashboard;
import pages.Admin.AdminDashboard;
import utils.DBConnection;
import utils.ImageLoader;
import utils.UserContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JPanel {
    private final PlaceholderTextField usernameField;
    private final PlaceholderPasswordField passwordField;
    private final JFrame parentFrame;

    public Login(JFrame parentFrame) {
        this.parentFrame = parentFrame;

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
                ImageIcon rawIcon = ImageLoader.loadImage("img/login-image.png");
                Image scaled = rawIcon.getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            }
        });


        // === RIGHT: Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(null); // biar ikut tinggi parent
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel contentWrapper = new JPanel(null);
        contentWrapper.setPreferredSize(new Dimension(520, 600));
        contentWrapper.setBackground(Color.WHITE);

        ImageIcon logoIcon = ImageLoader.loadImage("img/logo.png");
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
        usernameLabel.setBounds(60, 210, 400, 20);
        contentWrapper.add(usernameLabel);

        usernameField = new PlaceholderTextField("e.g. oliviarhye");
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        usernameField.setBounds(60, 240, 400, 44);
        contentWrapper.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordLabel.setBounds(60, 305, 400, 20);
        contentWrapper.add(passwordLabel);

        JPanel passwordWrapper = new JPanel(new BorderLayout());
        passwordWrapper.setBounds(60, 335, 400, 44);
        passwordWrapper.setBackground(Color.WHITE);
        passwordWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        passwordField = new PlaceholderPasswordField("**********");
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(new EmptyBorder(10, 10, 10, 10));
        passwordWrapper.add(passwordField, BorderLayout.CENTER);

        ImageIcon eyeIcon = ImageLoader.loadScaledImage("img/eye.png", 16, 16);
        ImageIcon eyeSlashIcon = ImageLoader.loadScaledImage("img/eye-slash.png", 16, 16);

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
        loginButton.setBounds(60, 475, 400, 44);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(Color.decode("#5A6ACF"));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        contentWrapper.add(loginButton);

        gbc.gridy = 0;
        formPanel.add(contentWrapper, gbc);

        loginButton.addMouseListener(new MouseAdapter() {
            final Color normalColor = Color.decode("#5A6ACF");
            final Color hoverColor = new Color(70, 86, 187);
            final Color pressedColor = new Color(55, 70, 150);

            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(normalColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                loginButton.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // If mouse is still over the button when released
                if (loginButton.getBounds().contains(e.getPoint())) {
                    loginButton.setBackground(hoverColor);
                } else {
                    loginButton.setBackground(normalColor);
                }
            }
        });



        JLabel loginErrorLabel = new JLabel();
        loginErrorLabel.setForeground(Color.RED);
        loginErrorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        loginErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginErrorLabel.setSize(400, 20);
        loginErrorLabel.setLocation(60, 525); // tombol Y + tinggi tombol + jarak
        contentWrapper.add(loginErrorLabel);

        // Create login action that can be reused
        ActionListener loginAction = e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            String role = getUserRoleFromDatabase(username, password);
            if (role != null) {
                // Set user context before recreating the application
                UserContext userContext = UserContext.getInstance();
                userContext.setCurrentUser(username, role);
                
                // Get the current window
                Window window = SwingUtilities.getWindowAncestor(this);
                
                // Check if the current window is a MainApplication
                if (window instanceof MainApplication) {
                    // If it's already a MainApplication, reuse it
                    MainApplication app = (MainApplication) window;
                    app.getContentPane().removeAll();
                    app.initializeApplication(); 
                    app.createPages(username, role);
                    app.setupLayout();
                    app.revalidate();
                    app.repaint();
                } else {
                    // If it's not a MainApplication (e.g., after logout), create a new one
                    if (window != null) {
                        window.dispose(); // Close the current window
                    }
                    
                    // Create a new MainApplication instance
                    SwingUtilities.invokeLater(() -> {
                        new MainApplication(username, role);
                    });
                }
            } else {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    new EmptyBorder(10, 10, 10, 10)
                ));
                passwordWrapper.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    new EmptyBorder(0,0,0, 0)
                ));
                loginErrorLabel.setText("Invalid username or password");
            }
        };

        // Add action listener to button
        loginButton.addActionListener(loginAction);

        // Add Enter key functionality to both username and password fields
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginAction.actionPerformed(null);
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        setVisible(true);
    }


    public Login() {
        this(null);
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
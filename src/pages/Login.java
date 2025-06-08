package pages;

import pages.Dashboard.Dashboard;
import pages.AdminDashboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.HashMap;

public class Login extends JFrame {
    private final PlaceholderTextField usernameField;
    private final PlaceholderPasswordField passwordField;
    private final HashMap<String, String> userPasswords = new HashMap<>();
    private final HashMap<String, String> role = new HashMap<>();

    public Login() {
        setTitle("ComFile Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // === LEFT: Illustration (dynamic resize) ===
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

        // === RIGHT: Form Panel with Centering ===
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

        // Logo CF (kiri atas)
        ImageIcon logoIcon = new ImageIcon("img/logo.png");
        Image logoImg = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(logoImg));
        logo.setBounds(60, 10, 40, 40);
        contentWrapper.add(logo);

        // Welcome Back (centered horizontally)
        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcome.setBounds(60, 130, 400, 40);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        contentWrapper.add(welcome);

        // Username Label + Field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameLabel.setBounds(60, 220, 400, 20);
        contentWrapper.add(usernameLabel);

        usernameField = new PlaceholderTextField("oliviarhye");
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setBounds(60, 245, 400, 44);
        contentWrapper.add(usernameField);

                // Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordLabel.setBounds(60, 330, 400, 20);
        contentWrapper.add(passwordLabel);

        // Password Wrapper with BorderLayout
        JPanel passwordWrapper = new JPanel(new BorderLayout());
        passwordWrapper.setBounds(60, 355, 400, 44);
        passwordWrapper.setBackground(Color.WHITE);
        passwordWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // Password Field
        passwordField = new PlaceholderPasswordField("Enter your password");
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(new EmptyBorder(10, 10, 10, 10));  // Standard padding
        passwordWrapper.add(passwordField, BorderLayout.CENTER);

        // Eye Icon
        ImageIcon rawEye = new ImageIcon("img/eye.png");
        ImageIcon rawEyeSlash = new ImageIcon("img/eye-slash.png");

        Image scaledEye = rawEye.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image scaledEyeSlash = rawEyeSlash.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        ImageIcon eyeIcon = new ImageIcon(scaledEye);
        ImageIcon eyeSlashIcon = new ImageIcon(scaledEyeSlash);

        JLabel eyeToggle = new JLabel(eyeIcon);
        eyeToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeToggle.setHorizontalAlignment(SwingConstants.CENTER);
        eyeToggle.setVerticalAlignment(SwingConstants.CENTER);
        eyeToggle.setPreferredSize(new Dimension(40, 44)); // Sempit, agar tidak potong border
        passwordWrapper.add(eyeToggle, BorderLayout.EAST);

        contentWrapper.add(passwordWrapper);

        // Toggle Action
        eyeToggle.addMouseListener(new MouseAdapter() {
            private boolean isVisible = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                isVisible = !isVisible;
                passwordField.setEchoChar(isVisible ? (char) 0 : '\u2022'); // Bullet char
                eyeToggle.setIcon(isVisible ? eyeSlashIcon : eyeIcon);
            }
        });

        // Login Button
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

        prepareUsers();

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (userPasswords.containsKey(username) && userPasswords.get(username).equals(password)) {
                String userRole = role.get(username);
                JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + userRole);

                // Tutup window login
                this.dispose();

                // Redirect ke dashboard sesuai role
                SwingUtilities.invokeLater(() -> {
                    if (userRole.equals("Admin")) {
                        new AdminDashboard(username, userRole);
                    } else {
                        new Dashboard(username, userRole);
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, "Username/password salah!");
            }
        });

        setVisible(true);
    }

    private void prepareUsers() {
        userPasswords.put("admincf", "admin123"); role.put("admincf", "Admin");
        userPasswords.put("lanasteiner", "lana123"); role.put("lanasteiner", "Manajer");
        userPasswords.put("candicewu", "candice123"); role.put("candicewu", "Manajer");
        userPasswords.put("michaelscott", "michael123"); role.put("michaelscott", "Manajer");
        userPasswords.put("andreawatson", "andrea123"); role.put("andreawatson", "Manajer");
        userPasswords.put("jonathanchoi", "jonathan123"); role.put("jonathanchoi", "Manajer");
        userPasswords.put("oliviarhye", "olivia123"); role.put("oliviarhye", "Karyawan");
        userPasswords.put("phoenixbaker", "phoenix123"); role.put("phoenixbaker", "Karyawan");
        userPasswords.put("drewcano", "drew123"); role.put("drewcano", "Karyawan");
        userPasswords.put("saraperez", "sara123"); role.put("saraperez", "Karyawan");
        userPasswords.put("kevindarma", "kevin123"); role.put("kevindarma", "Karyawan");
    }

    // ============ Placeholder TextField ============
    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;
        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.setFont(getFont().deriveFont(Font.PLAIN));
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 5, getHeight() / 2 + getFont().getSize() / 2 - 4);
                g2.dispose();
            }
        }
    }

    // ============ Placeholder PasswordField ============
    private static class PlaceholderPasswordField extends JPasswordField {
        private final String placeholder;
        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0 && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.setFont(getFont().deriveFont(Font.PLAIN));
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 5, getHeight() / 2 + getFont().getSize() / 2 - 4);
                g2.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
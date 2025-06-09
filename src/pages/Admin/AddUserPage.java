package pages.Admin;

import components.NavigationBar;
import utils.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddUserPage extends JFrame implements NavigationBar.NavigationListener {
    private String username;
    private String userRole;
    private NavigationBar navigationBar;
    private JLabel photoLabel;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JComboBox<String> departmentCombo;
    private JComboBox<String> statusCombo;
    private JPanel photoCircle;
    private java.io.File selectedPhotoFile;
    private String currentAvatarPath;

    public AddUserPage(String username, String userRole) {
        this.username = username;
        this.userRole = userRole;
        
        setTitle("ComFile - Add New User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Navigation Bar
        navigationBar = new NavigationBar();
        navigationBar.setNavigationListener(this);
        navigationBar.setUserInfo(username, userRole);
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        contentPanel.setBackground(Color.WHITE);
        
        // Page title
        JLabel pageTitle = new JLabel("Add New User");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(pageTitle);
        contentPanel.add(Box.createVerticalStrut(40));
        
        // Main form container
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.setMaximumSize(new Dimension(1200, 700));
        
        // Left side - Photo upload
        JPanel leftPanel = createPhotoUploadPanel();
        formContainer.add(leftPanel, BorderLayout.WEST);
        
        // Right side - Form fields
        JPanel rightPanel = createFormPanel();
        formContainer.add(rightPanel, BorderLayout.CENTER);
        
        contentPanel.add(formContainer);
        
        // Buttons panel
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(createButtonsPanel());
        
        // Setup frame
        setLayout(new BorderLayout());
        add(navigationBar, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createPhotoUploadPanel() {
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 60));
        photoPanel.setPreferredSize(new Dimension(350, 500));
        
        // Photo circle
        photoCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (selectedPhotoFile != null) {
                    // Draw the uploaded image
                    ImageIcon avatar = UserDAO.loadProfilePicture(currentAvatarPath, getWidth(), getHeight());
                    if (avatar != null) {
                        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, getWidth(), getHeight()));
                        g2.drawImage(avatar.getImage(), 0, 0, getWidth(), getHeight(), null);
                        return;
                    }
                }
                
                // Default circle background
                g2.setColor(new Color(156, 163, 175));
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        photoCircle.setPreferredSize(new Dimension(200, 200));
        photoCircle.setMaximumSize(new Dimension(200, 200));
        photoCircle.setOpaque(false);
        photoCircle.setLayout(new GridBagLayout());
        photoCircle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Photo content
        JPanel photoContent = new JPanel();
        photoContent.setLayout(new BoxLayout(photoContent, BoxLayout.Y_AXIS));
        photoContent.setOpaque(false);
        
        JLabel cameraIcon = new JLabel("📷");
        cameraIcon.setFont(new Font("SansSerif", Font.PLAIN, 32));
        cameraIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        cameraIcon.setForeground(Color.WHITE);
        
        JLabel uploadText = new JLabel("Click to add photo");
        uploadText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        uploadText.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadText.setForeground(Color.WHITE);
        
        photoContent.add(cameraIcon);
        photoContent.add(Box.createVerticalStrut(5));
        photoContent.add(uploadText);
        
        photoCircle.add(photoContent);
        
        // Add click listener for photo upload
        photoCircle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                uploadPhoto();
            }
        });
        
        photoPanel.add(Box.createVerticalGlue());
        photoPanel.add(photoCircle);
        photoPanel.add(Box.createVerticalGlue());
        
        return photoPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
        
        // Basic Information
        formPanel.add(createFormField("Full Name", fullNameField = new JTextField("Olivia Rhye")));
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(createFormField("Username", usernameField = new JTextField("Olivia Rhye")));
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(createPasswordField("Password"));
        formPanel.add(Box.createVerticalStrut(40));
        
        // Access Control Section
        JLabel accessControlTitle = new JLabel("Access Control");
        accessControlTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        accessControlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(accessControlTitle);
        formPanel.add(Box.createVerticalStrut(30));
        
        // Two-column layout for Role and Department
        JPanel twoColumnPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        twoColumnPanel.setBackground(Color.WHITE);
        twoColumnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        twoColumnPanel.setMaximumSize(new Dimension(800, 80));
        
        twoColumnPanel.add(createComboField("Role", roleCombo = new JComboBox<>(new String[]{"Karyawan", "Manajer", "Admin"})));
        twoColumnPanel.add(createComboField("Department", departmentCombo = new JComboBox<>(new String[]{"Design", "Product", "Marketing", "Finance", "Legal"})));
        
        formPanel.add(twoColumnPanel);
        formPanel.add(Box.createVerticalStrut(30));
        
        // Status field
        formPanel.add(createComboField("Status", statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"})));
        
        return formPanel;
    }
    
    private JPanel createFormField(String labelText, JTextField textField) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(800, 80));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        textField.setMaximumSize(new Dimension(800, 45));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(textField);
        
        return fieldPanel;
    }
    
    private JPanel createPasswordField(String labelText) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(800, 80));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel passwordContainer = new JPanel(new BorderLayout());
        passwordContainer.setMaximumSize(new Dimension(800, 45));
        passwordContainer.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));
        passwordContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        passwordField.setText("••••••••");
        
        JLabel eyeIcon = new JLabel("👁");
        eyeIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
        eyeIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        eyeIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        passwordContainer.add(passwordField, BorderLayout.CENTER);
        passwordContainer.add(eyeIcon, BorderLayout.EAST);
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(passwordContainer);
        
        return fieldPanel;
    }
    
    private JPanel createComboField(String labelText, JComboBox<String> comboBox) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setMaximumSize(new Dimension(380, 45));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBox.setBackground(Color.WHITE);
        
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(comboBox);
        
        return fieldPanel;
    }
    
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cancelBtn.setForeground(new Color(107, 114, 126));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> goBackToUserManagement());
        
        JButton createBtn = new JButton("Create User");
        createBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        createBtn.setForeground(Color.WHITE);
        createBtn.setBackground(new Color(79, 109, 245));
        createBtn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        createBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createBtn.addActionListener(e -> createUser());
        
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(createBtn);
        
        return buttonsPanel;
    }
    
    private void uploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$");
            }
            
            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPhotoFile = fileChooser.getSelectedFile();
            
            // Create temporary path for preview (we'll save it properly when creating the user)
            currentAvatarPath = selectedPhotoFile.getAbsolutePath();
            
            // Refresh the photo circle to show the new image
            photoCircle.repaint();
            
            System.out.println("Photo selected: " + selectedPhotoFile.getName());
        }
    }
    
    private void createUser() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleCombo.getSelectedItem();
        String department = (String) departmentCombo.getSelectedItem();
        String status = (String) statusCombo.getSelectedItem();
        
        // Validation
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if username already exists
        if (UserDAO.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists! Please choose a different username.", "Username Exists", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Handle profile picture
        String avatarPath = null;
        if (selectedPhotoFile != null) {
            avatarPath = UserDAO.saveProfilePicture(selectedPhotoFile, username);
            if (avatarPath == null) {
                JOptionPane.showMessageDialog(this, "Failed to save profile picture, but user will be created without it.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        // Add user to database with avatar
        boolean success = UserDAO.addUser(fullName, username, password, role, department, status, avatarPath);
        
        if (success) {
            System.out.println("✅ User created successfully: " + fullName + " (" + username + ")");
            JOptionPane.showMessageDialog(this, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            goBackToUserManagement();
        } else {
            System.err.println("❌ Failed to create user: " + fullName);
            // Clean up saved avatar if user creation failed
            if (avatarPath != null) {
                UserDAO.deleteProfilePicture(avatarPath);
            }
            JOptionPane.showMessageDialog(this, "Failed to create user! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void goBackToUserManagement() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new UserManagementDashboard(username, userRole);
        });
    }

    // NavigationListener implementation
    @Override
    public void onHomeClicked() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard(username, userRole);
        });
    }

    @Override
    public void onDocumentsClicked() {
        System.out.println("Documents clicked");
    }

    @Override
    public void onBackupClicked() {
        System.out.println("Backup clicked");
    }

    @Override
    public void onNotificationClicked() {
        JOptionPane.showMessageDialog(this, "Add User notifications", "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onLogoutClicked() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
} 
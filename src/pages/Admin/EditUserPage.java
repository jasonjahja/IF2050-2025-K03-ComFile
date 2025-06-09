package pages.Admin;

import components.NavigationBar;
import utils.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditUserPage extends JFrame implements NavigationBar.NavigationListener {
    private String username;
    private String userRole;
    private String selectedUser;
    private NavigationBar navigationBar;
    private boolean passwordVisible = false;
    private JPasswordField passwordField;
    private JLabel eyeIcon;
    private UserDAO.User currentUser;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JComboBox<String> roleComboBox;
    private JComboBox<String> departmentComboBox;
    private JComboBox<String> statusComboBox;

    public EditUserPage(String username, String userRole, String selectedUser) {
        this.username = username;
        this.userRole = userRole;
        this.selectedUser = selectedUser;
        
        // Load user data from database
        String extractedUsername = extractUsername(selectedUser);
        this.currentUser = UserDAO.getUserByUsername(extractedUsername);
        
        setTitle("ComFile - Edit User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        navigationBar = new NavigationBar();
        navigationBar.setNavigationListener(this);
        navigationBar.setUserInfo(username, userRole);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        contentPanel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel pageTitle = new JLabel("Edit User");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        headerPanel.add(pageTitle, BorderLayout.WEST);

        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Form panel
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel);

        // Setup frame
        setLayout(new BorderLayout());
        add(navigationBar, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        // Photo upload section
        JPanel photoSection = createPhotoUploadSection();
        formPanel.add(photoSection);
        formPanel.add(Box.createVerticalStrut(24));

        // Full Name field
        JPanel namePanel = createInputField("Full Name", currentUser != null ? currentUser.fullName : "");
        fullNameField = (JTextField) namePanel.getComponent(2);
        formPanel.add(namePanel);
        formPanel.add(Box.createVerticalStrut(16));

        // Username field
        JPanel usernameFieldPanel = createInputField("Username", currentUser != null ? currentUser.username : "");
        usernameField = (JTextField) usernameFieldPanel.getComponent(2);
        formPanel.add(usernameFieldPanel);
        formPanel.add(Box.createVerticalStrut(16));

        // Password field
        JPanel passwordPanel = createPasswordField();
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(24));

        // Access Control Section
        JLabel accessLabel = new JLabel("Access Control");
        accessLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        accessLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(accessLabel);
        formPanel.add(Box.createVerticalStrut(16));

        // Role and Department in a row
        JPanel roleDepPanel = new JPanel();
        roleDepPanel.setLayout(new BoxLayout(roleDepPanel, BoxLayout.X_AXIS));
        roleDepPanel.setBackground(Color.WHITE);
        roleDepPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleDepPanel.setMaximumSize(new Dimension(600, 80));

        JPanel rolePanel = createDropdownField("Role", new String[]{"Karyawan", "Manajer", "Admin"}, currentUser != null ? currentUser.role : "Karyawan");
        roleComboBox = (JComboBox<String>) rolePanel.getComponent(2);
        rolePanel.setPreferredSize(new Dimension(290, 80));
        rolePanel.setMaximumSize(new Dimension(290, 80));

        JPanel deptPanel = createDropdownField("Department", new String[]{"Design", "Product", "Marketing", "Finance", "Legal"}, currentUser != null ? currentUser.department : "Design");
        departmentComboBox = (JComboBox<String>) deptPanel.getComponent(2);
        deptPanel.setPreferredSize(new Dimension(290, 80));
        deptPanel.setMaximumSize(new Dimension(290, 80));

        roleDepPanel.add(rolePanel);
        roleDepPanel.add(Box.createHorizontalStrut(20));
        roleDepPanel.add(deptPanel);

        formPanel.add(roleDepPanel);
        formPanel.add(Box.createVerticalStrut(16));

        // Status field
        JPanel statusPanel = createDropdownField("Status", new String[]{"Active", "Inactive"}, currentUser != null ? currentUser.status : "Active");
        statusComboBox = (JComboBox<String>) statusPanel.getComponent(2);
        formPanel.add(statusPanel);
        formPanel.add(Box.createVerticalStrut(32));

        // Action buttons
        JPanel buttonsPanel = createButtonsPanel();
        formPanel.add(buttonsPanel);

        return formPanel;
    }

    private JPanel createPhotoUploadSection() {
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        photoPanel.setMaximumSize(new Dimension(600, 140));

        JLabel photoLabel = new JLabel("Photo");
        photoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        photoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        photoPanel.add(photoLabel);
        photoPanel.add(Box.createVerticalStrut(8));

        // Photo upload area
        JPanel uploadArea = new JPanel();
        uploadArea.setLayout(new BorderLayout());
        uploadArea.setBackground(new Color(249, 250, 251));
        uploadArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        uploadArea.setPreferredSize(new Dimension(120, 120));
        uploadArea.setMaximumSize(new Dimension(120, 120));
        uploadArea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel uploadContent = new JPanel();
        uploadContent.setLayout(new BoxLayout(uploadContent, BoxLayout.Y_AXIS));
        uploadContent.setBackground(new Color(249, 250, 251));

        JLabel cameraIcon = new JLabel("📷", JLabel.CENTER);
        cameraIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        cameraIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel uploadText = new JLabel("Click to add photo", JLabel.CENTER);
        uploadText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        uploadText.setForeground(new Color(107, 114, 126));
        uploadText.setAlignmentX(Component.CENTER_ALIGNMENT);

        uploadContent.add(cameraIcon);
        uploadContent.add(Box.createVerticalStrut(8));
        uploadContent.add(uploadText);

        uploadArea.add(uploadContent, BorderLayout.CENTER);
        photoPanel.add(uploadArea);

        return photoPanel;
    }

    private JPanel createInputField(String labelText, String value) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(600, 80));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField textField = new JTextField(value);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(textField);

        return fieldPanel;
    }

    private JPanel createPasswordField() {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.setMaximumSize(new Dimension(600, 80));

        JLabel label = new JLabel("Password");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel passwordContainer = new JPanel(new BorderLayout());
        passwordContainer.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));
        passwordContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        passwordField = new JPasswordField("********"); // Show placeholder for existing password
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        eyeIcon = new JLabel("👁");
        eyeIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
        eyeIcon.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 16));
        eyeIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                togglePasswordVisibility();
            }
        });

        passwordContainer.add(passwordField, BorderLayout.CENTER);
        passwordContainer.add(eyeIcon, BorderLayout.EAST);

        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(passwordContainer);

        return fieldPanel;
    }

    private JPanel createDropdownField(String labelText, String[] options, String selectedValue) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedItem(selectedValue);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(8));
        fieldPanel.add(comboBox);

        return fieldPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.setMaximumSize(new Dimension(600, 50));

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

        JButton updateBtn = new JButton("Update User");
        updateBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(new Color(79, 109, 245));
        updateBtn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        updateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updateBtn.addActionListener(e -> updateUser());

        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(Box.createHorizontalStrut(16));
        buttonsPanel.add(updateBtn);

        return buttonsPanel;
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            passwordField.setEchoChar('\0');
            eyeIcon.setText("🙈");
        } else {
            passwordField.setEchoChar('•');
            eyeIcon.setText("👁");
        }
    }



    private void goBackToUserManagement() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new UserManagementDashboard(username, userRole);
        });
    }

    private void updateUser() {
        if (currentUser != null) {
            String fullName = fullNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();
            String department = (String) departmentComboBox.getSelectedItem();
            String status = (String) statusComboBox.getSelectedItem();
            
            // Use existing password if not changed
            if (password.equals("********")) {
                password = currentUser.password;
            }
            
            boolean success = UserDAO.updateUser(currentUser.username, fullName, username, password, 
                                               role, department, status);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                goBackToUserManagement();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String extractUsername(String fullUserName) {
        // Extract username from "Full Name (@username)" format
        if (fullUserName.contains("(@") && fullUserName.contains(")")) {
            int start = fullUserName.indexOf("(@") + 2;
            int end = fullUserName.indexOf(")", start);
            return fullUserName.substring(start, end);
        }
        return fullUserName; // fallback
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
        JOptionPane.showMessageDialog(this, "Edit User notifications", "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onLogoutClicked() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
} 
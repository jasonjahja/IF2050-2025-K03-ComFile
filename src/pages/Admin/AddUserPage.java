package pages.Admin;

import components.NavigationBar;
import utils.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddUserPage extends JPanel {
    private String username;
    private String userRole;
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
    private java.awt.Container parentContainer;

    public AddUserPage(String username, String userRole, java.awt.Container parentContainer) {
        this.username = username;
        this.userRole = userRole;
        this.parentContainer = parentContainer;
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
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
        
        // Wrap content panel in a scroll pane
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
        fieldPanel.setMaximumSize(new Dimension(380, 80));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        comboBox.setMaximumSize(new Dimension(380, 45));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
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
        cancelBtn.setOpaque(true);
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
        createBtn.setOpaque(true);
        createBtn.setContentAreaFilled(true);
        createBtn.setBorderPainted(false);
        createBtn.setFocusPainted(false);
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
                return "Image files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPhotoFile = fileChooser.getSelectedFile();
            currentAvatarPath = selectedPhotoFile.getAbsolutePath();
            photoCircle.repaint();
        }
    }
    
    private void createUser() {
        // Get form data
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();
        String department = (String) departmentCombo.getSelectedItem();
        String status = (String) statusCombo.getSelectedItem();
        
        // Validate required fields
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Save to database
        try {
            boolean success = UserDAO.addUser(fullName, username, password, role, department, status, currentAvatarPath);
            if (success) {
                JOptionPane.showMessageDialog(this, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear form
                fullNameField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                roleCombo.setSelectedIndex(0);
                departmentCombo.setSelectedIndex(0);
                statusCombo.setSelectedIndex(0);
                selectedPhotoFile = null;
                currentAvatarPath = null;
                photoCircle.repaint();
                
                // Go back to user management
                goBackToUserManagement();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create user. Username might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void goBackToUserManagement() {
        if (parentContainer instanceof JPanel) {
            JPanel parent = (JPanel) parentContainer;
            CardLayout cardLayout = (CardLayout) parent.getLayout();
            cardLayout.show(parent, "USER_MANAGEMENT");
        }
    }
} 
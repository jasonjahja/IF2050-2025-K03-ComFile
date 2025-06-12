package pages.Admin;

import utils.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditUserPage extends JPanel {
    private String username;
    private String userRole;
    private String selectedUser;
    private boolean passwordVisible = false;
    private JPasswordField passwordField;
    private JLabel eyeIcon;
    private UserDAO.User currentUser;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JComboBox<String> roleComboBox;
    private JComboBox<String> departmentComboBox;
    private JComboBox<String> statusComboBox;
    private JPanel photoCircle;
    private java.io.File selectedPhotoFile;
    private String currentAvatarPath;
    private java.awt.Container parentContainer;

    public EditUserPage(String username, String userRole, String selectedUser, java.awt.Container parentContainer) {
        this.username = username;
        this.userRole = userRole;
        this.selectedUser = selectedUser;
        this.parentContainer = parentContainer;
        
        // Load user data from database
        String extractedUsername = extractUsername(selectedUser);
        this.currentUser = UserDAO.getUserByUsername(extractedUsername);
        this.currentAvatarPath = currentUser != null ? currentUser.avatarPath : null;
        
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

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
        
        // Basic Information
        formPanel.add(createFormField("Full Name", fullNameField = new JTextField(currentUser != null ? currentUser.fullName : "")));
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(createFormField("Username", usernameField = new JTextField(currentUser != null ? currentUser.username : "")));
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
        
        twoColumnPanel.add(createComboField("Role", roleComboBox = new JComboBox<>(new String[]{"Karyawan", "Manajer", "Admin"})));
        twoColumnPanel.add(createComboField("Department", departmentComboBox = new JComboBox<>(new String[]{"Design", "Product", "Marketing", "Finance", "Legal"})));
        
        // Set current values if user exists
        if (currentUser != null) {
            roleComboBox.setSelectedItem(currentUser.role);
            departmentComboBox.setSelectedItem(currentUser.department);
        }
        
        formPanel.add(twoColumnPanel);
        formPanel.add(Box.createVerticalStrut(30));
        
        // Status field
        formPanel.add(createComboField("Status", statusComboBox = new JComboBox<>(new String[]{"Active", "Inactive"})));
        if (currentUser != null) {
            statusComboBox.setSelectedItem(currentUser.status);
        }
        
        return formPanel;
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
                
                // Try to load current avatar
                String pathToLoad = selectedPhotoFile != null ? 
                    selectedPhotoFile.getAbsolutePath() : currentAvatarPath;
                    
                if (pathToLoad != null) {
                    ImageIcon avatar = UserDAO.loadProfilePicture(pathToLoad, getWidth(), getHeight());
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

        eyeIcon = new JLabel("👁");
        eyeIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
        eyeIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
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
        
        JButton updateBtn = new JButton("Update");
        updateBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(new Color(79, 109, 245));
        updateBtn.setOpaque(true);
        updateBtn.setContentAreaFilled(true);
        updateBtn.setBorderPainted(false);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        updateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updateBtn.addActionListener(e -> updateUser());
        
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(updateBtn);
        
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
            
            // Refresh the photo circle to show the new image
            photoCircle.repaint();
            
            System.out.println("Photo selected: " + selectedPhotoFile.getName());
        }
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
        if (parentContainer instanceof JPanel) {
            JPanel parent = (JPanel) parentContainer;
            CardLayout cardLayout = (CardLayout) parent.getLayout();
            cardLayout.show(parent, "USER_MANAGEMENT");
        }
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
            if (password.equals("••••••••")) {
                password = currentUser.password;
            }
            
            // Handle profile picture update
            String newAvatarPath = currentAvatarPath; // Keep existing by default
            if (selectedPhotoFile != null) {
                // Delete old avatar if it exists
                if (currentAvatarPath != null) {
                    UserDAO.deleteProfilePicture(currentAvatarPath);
                }
                
                // Save new avatar
                newAvatarPath = UserDAO.saveProfilePicture(selectedPhotoFile, username);
                if (newAvatarPath == null) {
                    JOptionPane.showMessageDialog(this, "Failed to save new profile picture, keeping existing one.", "Warning", JOptionPane.WARNING_MESSAGE);
                    newAvatarPath = currentAvatarPath; // Revert to existing
                }
            }
            
            boolean success = UserDAO.updateUser(currentUser.username, fullName, username, password, 
                                               role, department, status, newAvatarPath);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                goBackToUserManagement();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String extractUsername(String fullUserName) {
        // Extract username from format "Full Name (@username)"
        int startIndex = fullUserName.indexOf("(@") + 2;
        int endIndex = fullUserName.indexOf(")", startIndex);
        if (startIndex > 1 && endIndex > startIndex) {
            return fullUserName.substring(startIndex, endIndex);
        }
        return fullUserName; // fallback
    }
} 
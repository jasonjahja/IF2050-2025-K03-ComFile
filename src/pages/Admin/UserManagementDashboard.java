package pages.Admin;

import components.NavigationBar;
import utils.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserManagementDashboard extends JFrame implements NavigationBar.NavigationListener {
    private String username;
    private String userRole;
    private NavigationBar navigationBar;
    private JPanel tableContainer;
    private JPanel usersTableContainer;

    public UserManagementDashboard(String username, String userRole) {
        this.username = username;
        this.userRole = userRole;
        
        setTitle("ComFile - User Management");
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
        
        // Header with title and Add User button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel pageTitle = new JLabel("User Management");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        headerPanel.add(pageTitle, BorderLayout.WEST);
        
        JButton addUserBtn = createAddUserButton();
        headerPanel.add(addUserBtn, BorderLayout.EAST);
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Users table
        usersTableContainer = new JPanel();
        usersTableContainer.setLayout(new BoxLayout(usersTableContainer, BoxLayout.Y_AXIS));
        usersTableContainer.setBackground(Color.WHITE);
        usersTableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        createUsersTable();
        contentPanel.add(usersTableContainer);
        
        // Setup frame
        setLayout(new BorderLayout());
        add(navigationBar, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JButton createAddUserButton() {
        JButton addUserBtn = new JButton("👤 Add User");
        addUserBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        addUserBtn.setForeground(Color.WHITE);
        addUserBtn.setBackground(new Color(79, 109, 245));
        addUserBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addUserBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addUserBtn.addActionListener(e -> openAddUserPage());
        return addUserBtn;
    }
    
    private void createUsersTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create header
        tablePanel.add(createTableHeader());
        
        // Create data rows
        Object[][] userData = getUserData();
        for (Object[] row : userData) {
            tablePanel.add(createTableRow(row));
        }
        
        usersTableContainer.removeAll();
        usersTableContainer.add(tablePanel);
        usersTableContainer.revalidate();
        usersTableContainer.repaint();
    }
    
    private JPanel createTableHeader() {
        String[] headers = {"Full Name", "Status", "Role", "Username", "Department", ""};
        int[] columnWidths = {300, 120, 100, 130, 130, 80};
        
        JPanel headerRow = new JPanel();
        headerRow.setLayout(new BoxLayout(headerRow, BoxLayout.X_AXIS));
        headerRow.setBackground(new Color(249, 250, 251));
        headerRow.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        for (int i = 0; i < headers.length; i++) {
            JPanel columnPanel = new JPanel(new BorderLayout());
            columnPanel.setBackground(new Color(249, 250, 251));
            columnPanel.setPreferredSize(new Dimension(columnWidths[i], 21));
            columnPanel.setMinimumSize(new Dimension(columnWidths[i], 21));
            columnPanel.setMaximumSize(new Dimension(columnWidths[i], 21));
            
            if (!headers[i].isEmpty()) {
                JLabel label = new JLabel(headers[i]);
                label.setFont(new Font("SansSerif", Font.BOLD, 12));
                label.setForeground(new Color(107, 114, 126));
                columnPanel.add(label, BorderLayout.WEST);
            }
            
            headerRow.add(columnPanel);
            
            if (i < headers.length - 1) {
                headerRow.add(Box.createHorizontalStrut(10));
            }
        }
        
        return headerRow;
    }
    
    private JPanel createTableRow(Object[] row) {
        int[] columnWidths = {300, 120, 100, 130, 130, 80};
        
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(240, 240, 240)),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        for (int i = 0; i < columnWidths.length; i++) {
            JPanel columnPanel = new JPanel(new BorderLayout());
            columnPanel.setBackground(Color.WHITE);
            columnPanel.setPreferredSize(new Dimension(columnWidths[i], 41));
            columnPanel.setMinimumSize(new Dimension(columnWidths[i], 41));
            columnPanel.setMaximumSize(new Dimension(columnWidths[i], 41));
            
            if (i == 1) { // Status column (now index 1)
                columnPanel.add(createStatusBadge(), BorderLayout.WEST);
                
            } else if (i == 4) { // Department column (now index 4)
                columnPanel.add(createDepartmentBadge(row[i].toString()), BorderLayout.WEST);
                
            } else if (i == 5) { // Actions column (now index 5)
                columnPanel.add(createActionsPanel(row[0].toString()), BorderLayout.WEST);
                
            } else if (i < row.length) { // Regular columns
                String cellText = row[i].toString();
                if (i == 0 && cellText.length() > 35) { // Full Name column
                    cellText = cellText.substring(0, 32) + "...";
                }
                
                JLabel cellLabel = new JLabel(cellText);
                cellLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                cellLabel.setForeground(new Color(17, 24, 39));
                columnPanel.add(cellLabel, BorderLayout.WEST);
            }
            
            rowPanel.add(columnPanel);
            
            if (i < columnWidths.length - 1) {
                rowPanel.add(Box.createHorizontalStrut(10));
            }
        }
        
        return rowPanel;
    }
    
    private JPanel createStatusBadge() {
        JPanel statusContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusContainer.setBackground(Color.WHITE);
        
        JLabel statusLabel = new JLabel("● Active");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(34, 197, 94));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 197, 94), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        
        statusContainer.add(statusLabel);
        return statusContainer;
    }
    
    private JPanel createDepartmentBadge(String dept) {
        JPanel deptContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        deptContainer.setBackground(Color.WHITE);
        
        JLabel deptLabel = new JLabel(dept);
        deptLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        deptLabel.setOpaque(true);
        deptLabel.setBackground(Color.WHITE);
        
        Color deptColor;
        if (dept.equals("Design")) {
            deptColor = new Color(138, 126, 255);
        } else if (dept.equals("Product") || dept.equals("Marketing")) {
            deptColor = new Color(102, 153, 255);
        } else if (dept.equals("Finance")) {
            deptColor = new Color(102, 255, 178);
        } else if (dept.equals("Legal")) {
            deptColor = new Color(255, 153, 153);
        } else {
            deptColor = new Color(107, 114, 126);
        }
        
        deptLabel.setForeground(deptColor);
        deptLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(deptColor, 1),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        
        deptContainer.add(deptLabel);
        return deptContainer;
    }
    
    private JPanel createActionsPanel(String userName) {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionsPanel.setBackground(Color.WHITE);
        
        // Delete button
        JLabel deleteBtn = new JLabel();
        try {
            ImageIcon binIcon = new ImageIcon("img/icon-bin.png");
            Image binScaled = binIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            deleteBtn.setIcon(new ImageIcon(binScaled));
        } catch (Exception e) {
            deleteBtn.setText("🗑");
        }
        deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteBtn.setToolTipText("Delete");
        deleteBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDeleteConfirmation(userName);
            }
        });
        
        // Edit button
        JLabel editBtn = new JLabel();
        try {
            ImageIcon editIcon = new ImageIcon("img/icon-edit.png");
            Image editScaled = editIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            editBtn.setIcon(new ImageIcon(editScaled));
        } catch (Exception e) {
            editBtn.setText("✎");
        }
        editBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editBtn.setToolTipText("Edit");
        editBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openEditUserPage(userName);
            }
        });
        
        actionsPanel.add(deleteBtn);
        actionsPanel.add(editBtn);
        return actionsPanel;
    }
    
    private Object[][] getUserData() {
        // Get all users from database (no pagination)
        List<UserDAO.User> dbUsers = UserDAO.getAllUsers();
        
        Object[][] pageData = new Object[dbUsers.size()][5];
        
        for (int i = 0; i < dbUsers.size(); i++) {
            UserDAO.User user = dbUsers.get(i);
            pageData[i][0] = user.fullName + " (@" + user.username + ")";
            pageData[i][1] = "● " + user.status;
            pageData[i][2] = user.role;
            pageData[i][3] = user.username;
            pageData[i][4] = user.department;
        }
        
        return pageData;
    }
    

    
    private void showDeleteConfirmation(String userName) {
        JDialog dialog = new JDialog(this, "Confirm Delete", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
        
        // Warning panel
        JPanel warningPanel = new JPanel();
        warningPanel.setLayout(new BoxLayout(warningPanel, BoxLayout.Y_AXIS));
        warningPanel.setBackground(Color.WHITE);
        warningPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        
        JLabel warningIcon = new JLabel("⚠️", JLabel.CENTER);
        warningIcon.setFont(new Font("SansSerif", Font.PLAIN, 32));
        warningIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel warningText = new JLabel("Are you sure you want to", JLabel.CENTER);
        warningText.setFont(new Font("SansSerif", Font.BOLD, 16));
        warningText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel warningText2 = new JLabel("delete this user?", JLabel.CENTER);
        warningText2.setFont(new Font("SansSerif", Font.BOLD, 16));
        warningText2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        warningPanel.add(warningIcon);
        warningPanel.add(Box.createVerticalStrut(10));
        warningPanel.add(warningText);
        warningPanel.add(warningText2);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cancelBtn.setForeground(new Color(107, 114, 126));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> {
            // Delete user from database using username
            String usernameToDelete = extractUsername(userName);
            if (UserDAO.deleteUser(usernameToDelete)) {
                System.out.println("✅ User deleted successfully: " + userName);
                JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.err.println("❌ Failed to delete user: " + userName);
                JOptionPane.showMessageDialog(this, "Failed to delete user!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
            createUsersTable();
        });
        
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(deleteBtn);
        
        dialog.add(warningPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void openAddUserPage() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new AddUserPage(username, userRole);
        });
    }
    
    private void openEditUserPage(String selectedUser) {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new EditUserPage(username, userRole, selectedUser);
        });
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
        JOptionPane.showMessageDialog(this, "User Management notifications", "Notifications", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onLogoutClicked() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
} 
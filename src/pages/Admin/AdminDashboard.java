package pages.Admin;

import components.NavigationBar;
import utils.UserDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminDashboard extends JFrame implements NavigationBar.NavigationListener {
    private String username;
    private String userRole;
    private NavigationBar navigationBar;

    public AdminDashboard(String username, String userRole) {
        this.username = username;
        this.userRole = userRole;
        
        setTitle("ComFile - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // Initialize components
        navigationBar = new NavigationBar();
        navigationBar.setNavigationListener(this);
        navigationBar.setUserInfo(username, userRole);
        
        // Create main content panel with padding
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        contentPanel.setBackground(Color.WHITE);
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome back, " + username + "!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(welcomeLabel);
        
        // Add some vertical spacing
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Users section header with "See More" link
        JPanel usersHeader = new JPanel(new BorderLayout());
        usersHeader.setBackground(Color.WHITE);
        usersHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        usersHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel usersTitle = new JLabel("Users");
        usersTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        usersHeader.add(usersTitle, BorderLayout.WEST);
        
        JLabel usersSeeMore = new JLabel("See More →");
        usersSeeMore.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usersSeeMore.setForeground(new Color(90, 106, 207));
        usersSeeMore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        usersSeeMore.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openUserManagementPage();
            }
        });
        usersHeader.add(usersSeeMore, BorderLayout.EAST);
        
        contentPanel.add(usersHeader);
        contentPanel.add(Box.createVerticalStrut(16));
        
        // Users table panel
        JPanel usersTablePanel = createUsersTablePanel();
        contentPanel.add(usersTablePanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Documents section header with "See More" link
        JPanel documentsHeader = new JPanel(new BorderLayout());
        documentsHeader.setBackground(Color.WHITE);
        documentsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        documentsHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel documentsTitle = new JLabel("Recent Documents");
        documentsTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        documentsHeader.add(documentsTitle, BorderLayout.WEST);
        
        JLabel documentsSeeMore = new JLabel("See More →");
        documentsSeeMore.setFont(new Font("SansSerif", Font.PLAIN, 14));
        documentsSeeMore.setForeground(new Color(90, 106, 207));
        documentsSeeMore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        documentsSeeMore.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openDocumentsPage();
            }
        });
        documentsHeader.add(documentsSeeMore, BorderLayout.EAST);
        
        contentPanel.add(documentsHeader);
        contentPanel.add(Box.createVerticalStrut(16));
        
        // Documents table panel
        JPanel documentsTablePanel = createDocumentsTablePanel();
        contentPanel.add(documentsTablePanel);
        
        // Add components to frame
        setLayout(new BorderLayout());
        add(navigationBar, BorderLayout.NORTH);
        
        // Wrap content panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
    
    private JPanel createUsersTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(0, 0, 0, 0)
        ));
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        String[] headers = {"Full Name", "Status", "Role", "Username", "Department", "Actions"};
        
        // Get users from database
        List<UserDAO.User> dbUsers = UserDAO.getUsersForDashboard();
        Object[][] data = new Object[dbUsers.size()][6];
        
        for (int i = 0; i < dbUsers.size(); i++) {
            UserDAO.User user = dbUsers.get(i);
            data[i][0] = user.fullName + " (@" + user.username + ")";
            data[i][1] = "● " + user.status;
            data[i][2] = user.role;
            data[i][3] = user.username;
            data[i][4] = user.department;
            data[i][5] = "actions";
        }

        // Header with fixed column widths
        JPanel headerRow = new JPanel();
        headerRow.setLayout(new BoxLayout(headerRow, BoxLayout.X_AXIS));
        headerRow.setBackground(new Color(249, 250, 251));
        headerRow.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Define fixed column widths for consistent alignment
        int[] columnWidths = {300, 120, 100, 130, 130, 80}; // Full Name, Status, Role, Username, Department, Actions
        
        for (int i = 0; i < headers.length; i++) {
            JPanel columnPanel = new JPanel(new BorderLayout());
            columnPanel.setBackground(new Color(249, 250, 251));
            columnPanel.setPreferredSize(new Dimension(columnWidths[i], 21));
            columnPanel.setMinimumSize(new Dimension(columnWidths[i], 21));
            columnPanel.setMaximumSize(new Dimension(columnWidths[i], 21));
            
            JLabel label = new JLabel(headers[i]);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setForeground(new Color(107, 114, 126));
            columnPanel.add(label, BorderLayout.WEST);
            
            headerRow.add(columnPanel);
            
            // Add spacing between columns except for the last one
            if (i < headers.length - 1) {
                headerRow.add(Box.createHorizontalStrut(10));
            }
        }
        tablePanel.add(headerRow);

        // Rows with fixed column widths
        for (Object[] row : data) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            rowPanel.setBackground(Color.WHITE);
            rowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(240, 240, 240)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
            ));
            rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

            for (int i = 0; i < row.length; i++) {
                JPanel columnPanel = new JPanel(new BorderLayout());
                columnPanel.setBackground(Color.WHITE);
                columnPanel.setPreferredSize(new Dimension(columnWidths[i], 41));
                columnPanel.setMinimumSize(new Dimension(columnWidths[i], 41));
                columnPanel.setMaximumSize(new Dimension(columnWidths[i], 41));
                
                if (i == 1) { // Status column - outlined style
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
                    columnPanel.add(statusContainer, BorderLayout.WEST);
                    
                } else if (i == 4) { // Department column - outlined style
                    JPanel deptContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    deptContainer.setBackground(Color.WHITE);
                    
                    JLabel deptLabel = new JLabel(row[i].toString());
                    deptLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
                    deptLabel.setOpaque(true);
                    deptLabel.setBackground(Color.WHITE);
                    deptLabel.setHorizontalAlignment(JLabel.CENTER);
                    
                    // Set department colors for border and text
                    String dept = row[i].toString();
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
                    columnPanel.add(deptContainer, BorderLayout.WEST);
                    
                } else if (i == 5) { // Actions column
                    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
                    actionsPanel.setBackground(Color.WHITE);
                    
                    // Load icons - BIN FIRST, then EDIT
                    try {
                        String projectRoot = System.getProperty("user.dir");
                        
                        // Delete/Bin icon first
                        ImageIcon binOriginal = new ImageIcon("img/icon-bin.png");
                        Image binScaled = binOriginal.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                        ImageIcon binIcon = new ImageIcon(binScaled);
                        JLabel deleteLabel = new JLabel(binIcon);
                        deleteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        deleteLabel.setToolTipText("Delete");
                        
                        // Add click handler for delete
                        final String currentRowUser = row[0].toString(); // Full name with username
                        deleteLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                AdminDashboard.this.showDeleteConfirmation(currentRowUser);
                            }
                        });
                        
                        // Edit icon second
                        ImageIcon editOriginal = new ImageIcon("img/icon-edit.png");
                        Image editScaled = editOriginal.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                        ImageIcon editIcon = new ImageIcon(editScaled);
                        JLabel editLabel = new JLabel(editIcon);
                        editLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        editLabel.setToolTipText("Edit");
                        
                        // Add click handler for edit
                        editLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                AdminDashboard.this.openEditUserPage(currentRowUser);
                            }
                        });
                        
                        actionsPanel.add(deleteLabel); // Bin first
                        actionsPanel.add(editLabel);   // Edit second
                        
                    } catch (Exception e) {
                        // Fallback icons
                        final String currentRowUser = row[0].toString(); // Full name with username
                        
                        JLabel deleteLabel = new JLabel("🗑");
                        deleteLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));
                        deleteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        deleteLabel.setToolTipText("Delete");
                        deleteLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                AdminDashboard.this.showDeleteConfirmation(currentRowUser);
                            }
                        });
                        
                        JLabel editLabel = new JLabel("✎");
                        editLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));
                        editLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        editLabel.setToolTipText("Edit");
                        editLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                AdminDashboard.this.openEditUserPage(currentRowUser);
                            }
                        });
                        
                        actionsPanel.add(deleteLabel);
                        actionsPanel.add(editLabel);
                    }
                    
                    columnPanel.add(actionsPanel, BorderLayout.WEST);
                    
                } else {
                    // Regular columns with text truncation for long names
                    String cellText = row[i].toString();
                    if (i == 0 && cellText.length() > 35) { // Full Name column - truncate if too long
                        cellText = cellText.substring(0, 32) + "...";
                    }
                    
                    JLabel cellLabel = new JLabel(cellText);
                    cellLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    cellLabel.setForeground(new Color(17, 24, 39));
                    
                    columnPanel.add(cellLabel, BorderLayout.WEST);
                }
                
                rowPanel.add(columnPanel);
                
                // Add spacing between columns except for the last one
                if (i < row.length - 1) {
                    rowPanel.add(Box.createHorizontalStrut(10));
                }
            }
            tablePanel.add(rowPanel);
        }

        return tablePanel;
    }
    
    private JPanel createDocumentsTablePanel() {
        JPanel documentsContainer = new JPanel();
        documentsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        documentsContainer.setBackground(Color.WHITE);
        documentsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Document data
        String[][] documents = {
                {"K3C_DPPLOO002", "15 May 2025"},
                {"Document1", "13 May 2025"},
                {"Document2", "10 May 2025"}
        };

        // Create document cards
        for (String[] doc : documents) {
            JPanel card = createDocumentCard(doc[0], doc[1]);
            documentsContainer.add(card);
        }

        return documentsContainer;
    }

    private JPanel createDocumentCard(String title, String date) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setPreferredSize(new Dimension(200, 280));
        card.setMaximumSize(new Dimension(200, 280));

        // Document thumbnail
        JPanel thumbnailPanel = new JPanel();
        thumbnailPanel.setPreferredSize(new Dimension(168, 200));
        thumbnailPanel.setMaximumSize(new Dimension(168, 200));
        thumbnailPanel.setBackground(new Color(248, 250, 252));
        thumbnailPanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        thumbnailPanel.setLayout(new GridBagLayout());
        
        // Try to load doc-thumb.png, fallback to simple preview
        try {
            String projectRoot = System.getProperty("user.dir");
            String thumbPath = projectRoot + "/img/doc-thumb.png";
            ImageIcon thumbIcon = new ImageIcon(thumbPath);
            
            // Scale the image to fit nicely in the thumbnail
            Image thumbScaled = thumbIcon.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(thumbScaled);
            JLabel thumbLabel = new JLabel(scaledIcon);
            thumbnailPanel.add(thumbLabel);
            
        } catch (Exception e) {
            // Fallback: simple document preview
            JLabel docIcon = new JLabel("📄");
            docIcon.setFont(new Font("SansSerif", Font.PLAIN, 48));
            docIcon.setHorizontalAlignment(JLabel.CENTER);
            thumbnailPanel.add(docIcon);
        }

        // Document title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Document date
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(107, 114, 126));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateLabel.setHorizontalAlignment(JLabel.CENTER);

        card.add(thumbnailPanel);
        card.add(Box.createVerticalStrut(12));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(dateLabel);

        return card;
    }

    // NavigationListener implementation
    @Override
    public void onHomeClicked() {
        System.out.println("Home clicked from Admin Dashboard");
        }

        @Override
    public void onDocumentsClicked() {
        System.out.println("Documents clicked from Admin Dashboard");
    }

    @Override
    public void onBackupClicked() {
        System.out.println("Backup clicked from Admin Dashboard");
        }

        @Override
    public void onNotificationClicked() {
        JOptionPane.showMessageDialog(this, "Admin notifications", "Notifications", JOptionPane.INFORMATION_MESSAGE);
        }

        @Override
    public void onLogoutClicked() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void openUserManagementPage() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new UserManagementDashboard(username, userRole);
        });
    }
    
    private void openDocumentsPage() {
        System.out.println("Opening Documents page...");
        // TODO: Implement documents page navigation
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
                                 // Refresh the users table
                 SwingUtilities.invokeLater(() -> {
                     AdminDashboard.this.dispose();
                     new AdminDashboard(AdminDashboard.this.username, AdminDashboard.this.userRole);
                 });
            } else {
                System.err.println("❌ Failed to delete user: " + userName);
                JOptionPane.showMessageDialog(this, "Failed to delete user!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
        });
        
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(deleteBtn);
        
        dialog.add(warningPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
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
}

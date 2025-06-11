package pages.Admin;

import components.NavigationBar;
import utils.UserDAO;
import utils.DocumentDAO;
import pages.ManageDocuments.Document.Doc;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.Desktop;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminDashboard extends JPanel {
    private String username;
    private String userRole;
    private JPanel contentPanel;
    private java.awt.Container parentContainer;
    private Color defaultBorderColor = new Color(200, 200, 200);
    private Color hoverBorderColor = new Color(90, 106, 207);

    public AdminDashboard(String username, String userRole, java.awt.Container parentContainer) {
        this.username = username;
        this.userRole = userRole;
        this.parentContainer = parentContainer;
        
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Create main content panel with padding
        contentPanel = new JPanel();
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
        
        // Documents grid panel
        JPanel documentsGridPanel = createDocumentsGridPanel();
        contentPanel.add(documentsGridPanel);
        
        // Wrap content panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
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
        int[] columnWidths = {400, 150, 130, 160, 150, 100}; // Full Name, Status, Role, Username, Department, Actions
        
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
                    
                    // Get the username for this row
                    String rowUsername = row[3].toString();
                    
                    // Load icons - BIN FIRST, then EDIT
                    try {
                        // Delete button with trash icon - only show if not current admin
                        if (!rowUsername.equals(this.username)) {
                            ImageIcon binOriginal = new ImageIcon("img/icon-bin.png");
                            Image binScaled = binOriginal.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                            ImageIcon binIcon = new ImageIcon(binScaled);
                            JLabel deleteBtn = new JLabel(binIcon);
                            deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            deleteBtn.setToolTipText("Delete");
                            deleteBtn.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    showDeleteConfirmation(row[0].toString());
                                }
                                
                                @Override
                                public void mouseEntered(MouseEvent e) {
                                    deleteBtn.setForeground(Color.RED);
                                }
                                
                                @Override
                                public void mouseExited(MouseEvent e) {
                                    deleteBtn.setForeground(Color.BLACK);
                                }
                            });
                            
                            actionsPanel.add(deleteBtn);
                        }
                        
                        // Edit button with pencil icon  
                        ImageIcon editOriginal = new ImageIcon("img/icon-edit.png");
                        Image editScaled = editOriginal.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                        ImageIcon editIcon = new ImageIcon(editScaled);
                        JLabel editBtn = new JLabel(editIcon);
                        editBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        editBtn.setToolTipText("Edit");
                        editBtn.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                openEditUserPage(row[0].toString());
                            }
                            
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                editBtn.setForeground(new Color(90, 106, 207));
                            }
                            
                            @Override
                            public void mouseExited(MouseEvent e) {
                                editBtn.setForeground(Color.BLACK);
                            }
                        });
                        
                        actionsPanel.add(editBtn);
                        
                    } catch (Exception ex) {
                        // Fallback to text if icons fail
                        if (!rowUsername.equals(this.username)) {
                            JLabel deleteBtn = new JLabel("Delete");
                            deleteBtn.setForeground(Color.RED);
                            deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            actionsPanel.add(deleteBtn);
                        }
                    }
                    
                    columnPanel.add(actionsPanel, BorderLayout.WEST);
                    
                } else {
                    JLabel cellLabel = new JLabel(row[i].toString());
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
    
    private JPanel createDocumentsGridPanel() {
        JPanel documentsWrapper = new JPanel(new BorderLayout());
        documentsWrapper.setBackground(Color.WHITE);
        documentsWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        documentsWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        
        // Create grid with 5 columns for horizontal layout
        JPanel documentsGrid = new JPanel(new GridLayout(1, 5, 15, 15));
        documentsGrid.setBackground(Color.WHITE);
        documentsGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Get recent documents from database (limit 5, ordered by modified_date DESC)
        List<Doc> recentDocs = getRecentDocumentsFromDB(5);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        
        if (recentDocs.isEmpty()) {
            JLabel emptyMsg = new JLabel("No documents found.");
            emptyMsg.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emptyMsg.setHorizontalAlignment(SwingConstants.CENTER);
            documentsGrid.add(emptyMsg);
        } else {
            for (Doc doc : recentDocs) {
                File file = new File(doc.filePath);
                String date = file.exists() ? dateFormat.format(file.lastModified()) : dateFormat.format(doc.modifiedDate);
                documentsGrid.add(createDocumentCard(doc, date));
            }
        }
        
        documentsWrapper.add(documentsGrid, BorderLayout.CENTER);
        return documentsWrapper;
    }
    
    private List<Doc> getRecentDocumentsFromDB(int limit) {
        // Get all documents and sort by modified date, then limit
        return DocumentDAO.getAllDocumentsFromDB()
                .stream()
                .sorted((d1, d2) -> d2.modifiedDate.compareTo(d1.modifiedDate))
                .limit(limit)
                .toList();
    }
    
    private JPanel createDocumentCard(Doc doc, String dateText) {
        JPanel docCard = new JPanel(new BorderLayout());
        docCard.setPreferredSize(new Dimension(180, 240));
        docCard.setBackground(Color.WHITE);
        docCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(defaultBorderColor),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Load thumbnail
        ImageIcon docImage = null;
        try {
            String path = System.getProperty("user.dir") + "/img/doc-thumb.png";
            File imageFile = new File(path);
            if (imageFile.exists()) {
                docImage = new ImageIcon(imageFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setPreferredSize(new Dimension(160, 160));

        if (docImage == null || docImage.getImageLoadStatus() == MediaTracker.ERRORED) {
            JLabel docPlaceholder = new JLabel("📄");
            docPlaceholder.setFont(new Font("Arial", Font.PLAIN, 48));
            docPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
            imageContainer.add(docPlaceholder, BorderLayout.CENTER);
        } else {
            Image scaledDoc = docImage.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            JLabel docImageLabel = new JLabel(new ImageIcon(scaledDoc));
            docImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageContainer.add(docImageLabel, BorderLayout.CENTER);
        }

        imageContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        docCard.add(imageContainer, BorderLayout.CENTER);

        // Info Panel (Title + Date)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel docTitle = new JLabel(doc.title);
        docTitle.setFont(new Font("Arial", Font.BOLD, 12));
        docTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        docTitle.setBorder(new EmptyBorder(5, 0, 2, 0));

        JLabel docDate = new JLabel(dateText);
        docDate.setFont(new Font("Arial", Font.PLAIN, 10));
        docDate.setForeground(new Color(100, 100, 100));
        docDate.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(docTitle);
        infoPanel.add(docDate);

        docCard.add(infoPanel, BorderLayout.SOUTH);

        // Hover Effect
        docCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                docCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(hoverBorderColor, 2),
                        BorderFactory.createEmptyBorder(9, 9, 9, 9)
                ));
                docCard.setBackground(new Color(250, 250, 255));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                docCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(defaultBorderColor),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                docCard.setBackground(Color.WHITE);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            public void mouseClicked(MouseEvent e) {
                File file = new File(doc.filePath);
                if (file.exists()) {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                docCard,
                                "Failed to open document: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            docCard,
                            "File not found at: " + file.getAbsolutePath(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        return docCard;
    }
    
    private void openUserManagementPage() {
        if (parentContainer instanceof JPanel) {
            JPanel parent = (JPanel) parentContainer;
            CardLayout cardLayout = (CardLayout) parent.getLayout();
            
            // Add or switch to user management page
            boolean pageExists = false;
            for (Component comp : parent.getComponents()) {
                if (comp instanceof UserManagementDashboard) {
                    pageExists = true;
                    break;
                }
            }
            
            if (!pageExists) {
                UserManagementDashboard userMgmt = new UserManagementDashboard(username, userRole, parentContainer);
                parent.add(userMgmt, "USER_MANAGEMENT");
            }
            
            cardLayout.show(parent, "USER_MANAGEMENT");
        }
    }

    private void openDocumentsPage() {
        // Navigate to documents section in main app
        if (parentContainer instanceof JPanel) {
            JPanel parent = (JPanel) parentContainer;
            CardLayout cardLayout = (CardLayout) parent.getLayout();
            cardLayout.show(parent, "DOCUMENTS");
        }
    }
    
    private void showDeleteConfirmation(String userName) {
        // Extract username from display format "Full Name (@username)"
        String username = extractUsername(userName);
        
        // Check if admin is trying to delete their own account
        if (username.equals(this.username)) {
            JOptionPane.showMessageDialog(this, 
                "You cannot delete your own account!", 
                "Cannot Delete", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create custom dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Delete User");
        dialog.setModal(true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        JLabel messageLabel = new JLabel("Are you sure you want to delete this user?");
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel usernameLabel = new JLabel(userName);
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.GRAY);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(usernameLabel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cancelBtn.setForeground(new Color(107, 114, 126));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setOpaque(true);
        cancelBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        cancelBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
            }
        });
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 38, 38)); // Solid red background
        deleteBtn.setOpaque(true);
        deleteBtn.setContentAreaFilled(true); // Ensure background is painted
        deleteBtn.setBorderPainted(false); // Remove border
        deleteBtn.setFocusPainted(false); // Remove focus border
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        deleteBtn.addActionListener(e -> {
            // Perform deletion
            boolean success = UserDAO.deleteUser(username);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                // Refresh the dashboard
                SwingUtilities.invokeLater(() -> {
                    removeAll();
                    initializeComponents();
                    revalidate();
                    repaint();
                });
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonsPanel.add(cancelBtn);
        buttonsPanel.add(deleteBtn);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void openEditUserPage(String selectedUser) {
        if (parentContainer instanceof JPanel) {
            JPanel parent = (JPanel) parentContainer;
            CardLayout cardLayout = (CardLayout) parent.getLayout();
            
            // Remove existing edit user page if any
            for (Component comp : parent.getComponents()) {
                if (comp instanceof EditUserPage) {
                    parent.remove(comp);
                    break;
                }
            }
            
            EditUserPage editPage = new EditUserPage(username, userRole, selectedUser, parentContainer);
            parent.add(editPage, "EDIT_USER");
            cardLayout.show(parent, "EDIT_USER");
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

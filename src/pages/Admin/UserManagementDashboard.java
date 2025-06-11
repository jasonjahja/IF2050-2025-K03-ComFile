package pages.Admin;


import components.NavigationBar;
import utils.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class UserManagementDashboard extends JPanel {
   private String username;
   private String userRole;
   private JPanel tableContainer;
   private JPanel usersTableContainer;
   private java.awt.Container parentContainer;


   public UserManagementDashboard(String username, String userRole, java.awt.Container parentContainer) {
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
      
       // Wrap content panel in a scroll pane
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
       JLabel deleteBtn = new JLabel("🗑");
       deleteBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
       deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       deleteBtn.setToolTipText("Delete user");
       deleteBtn.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               showDeleteConfirmation(userName);
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
      
       // Edit button
       JLabel editBtn = new JLabel("✏");
       editBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
       editBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       editBtn.setToolTipText("Edit user");
       editBtn.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               openEditUserPage(userName);
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
      
       actionsPanel.add(deleteBtn);
       actionsPanel.add(editBtn);
      
       return actionsPanel;
   }
  
   private Object[][] getUserData() {
       List<UserDAO.User> users = UserDAO.getAllUsers();
       Object[][] userData = new Object[users.size()][5];
      
       for (int i = 0; i < users.size(); i++) {
           UserDAO.User user = users.get(i);
           userData[i][0] = user.fullName + " (@" + user.username + ")";
           userData[i][1] = user.status;
           userData[i][2] = user.role;
           userData[i][3] = user.username;
           userData[i][4] = user.department;
       }
      
       return userData;
   }
  
   private void showDeleteConfirmation(String userName) {
       // Extract username from display format "Full Name (@username)"
       String username = extractUsername(userName);
      
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
       cancelBtn.setBackground(Color.WHITE);
       cancelBtn.setBorder(BorderFactory.createCompoundBorder(
           BorderFactory.createLineBorder(Color.GRAY, 1),
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
       deleteBtn.setBackground(Color.RED);
       deleteBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
       deleteBtn.addActionListener(e -> {
           // Perform deletion
           boolean success = UserDAO.deleteUser(username);
           if (success) {
               JOptionPane.showMessageDialog(dialog, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
               dialog.dispose();
               // Refresh the user table
               createUsersTable();
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
  
   private void openAddUserPage() {
       if (parentContainer instanceof JPanel) {
           JPanel parent = (JPanel) parentContainer;
           CardLayout cardLayout = (CardLayout) parent.getLayout();
          
           // Remove existing add user page if any
           for (Component comp : parent.getComponents()) {
               if (comp instanceof AddUserPage) {
                   parent.remove(comp);
                   break;
               }
           }
          
           AddUserPage addPage = new AddUserPage(username, userRole, parentContainer);
           parent.add(addPage, "ADD_USER");
           cardLayout.show(parent, "ADD_USER");
       }
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


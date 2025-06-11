package components;
import pages.ManageDocuments.Document;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.event.ActionEvent;
import java.util.Map;
import pages.ManageDocuments.Document.User;
import utils.DocumentDAO;
import java.util.Set;
import java.util.HashSet;

public class AccessControl extends JDialog {
    private JPanel peopleListPanel;
    private JPanel ownerRowPanel;
    private Map<String, User> users;
    private JScrollPane scrollPane;
    private final java.util.Set<String> addedUsernames = new java.util.HashSet<>();
    private Document.Doc currentDoc;
    private JComboBox<String> groupDropdown;
    private JComboBox<String> roleDropdown;

    public AccessControl(JFrame parent, String docTitle, Document.Doc doc, Map<String, Document.User> users) {
        super(parent, "Share Document", true);
        this.users = users;
        this.currentDoc = doc;
        setSize(480, 400);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        this.users = users;

        PlaceholderTextField addPeopleField = new PlaceholderTextField("Add people");
        addPeopleField.addActionListener(e -> {
            String username = addPeopleField.getText().trim();
            if (username.isEmpty()) return;

            if (!users.containsKey(username)) {
                String fullName = username;
                String role = "Viewer";
                String department = Document.users.containsKey(username)
                        ? Document.users.get(username).getDepartment()
                        : "";

                DocumentDAO.saveUser(username, fullName, role, department);
                Document.users.put(username, new User(username, fullName, role, department));
                User updatedUser = DocumentDAO.getUserFromDB(username);
                if (updatedUser != null) {
                    Document.users.put(username, updatedUser);
                    users.put(username, updatedUser);
                }
            }

            if (addedUsernames.contains(username)) {
                JOptionPane.showMessageDialog(this, "User already added!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = users.get(username);
            DocumentDAO.addAccessToDoc(currentDoc.id, user.getUsername(), "Viewer");
            peopleListPanel.add(createPersonRow(user.getUsername(), "Viewer", user.getFullName()));
            Document.AccessPermission newAccess = new Document.AccessPermission(user, "Viewer");
            currentDoc.sharedWith.add(newAccess);
            currentDoc.accessPermissions.add(newAccess);
            addedUsernames.add(username);
            peopleListPanel.revalidate();
            peopleListPanel.repaint();
            updatePanelHeight();
            addPeopleField.setText("");

            int actualRowCount = 0;
            for (Component comp : peopleListPanel.getComponents()) {
                if (comp instanceof JPanel) actualRowCount++;
            }

            int newScrollHeight = Math.min(196, actualRowCount * 48);
            scrollPane.setPreferredSize(new Dimension(400, newScrollHeight));
            scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, newScrollHeight));
            scrollPane.revalidate();
            scrollPane.repaint();

            updatePanelHeight();
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // ===== Title =====
        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleWrapper.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Share \"" + docTitle + "\"");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleWrapper.add(titleLabel);
        mainPanel.add(titleWrapper);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ===== Add People =====
        JPanel inputWrapper = new JPanel(new BorderLayout());
        inputWrapper.setBackground(Color.WHITE);
        inputWrapper.setPreferredSize(new Dimension(300, 44));
        inputWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        inputWrapper.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        addPeopleField.setFont(new Font("Arial", Font.PLAIN, 14));
        addPeopleField.setForeground(Color.GRAY);
        addPeopleField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        inputWrapper.add(addPeopleField, BorderLayout.CENTER);
        mainPanel.add(inputWrapper);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // ===== People With Access =====
        JPanel accessLabelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        accessLabelWrapper.setBackground(Color.WHITE);
        JLabel accessLabel = new JLabel("People with access");
        accessLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accessLabelWrapper.add(accessLabel);
        mainPanel.add(accessLabelWrapper);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // === Owner row
        ownerRowPanel = createPersonRow(doc.owner.id, "Owner", doc.owner.getFullName());
        mainPanel.add(ownerRowPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        peopleListPanel = new JPanel();
        peopleListPanel.setLayout(new BoxLayout(peopleListPanel, BoxLayout.Y_AXIS));
        peopleListPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(peopleListPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(400, 196));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 196));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        for (Document.AccessPermission permission : doc.sharedWith) {
            if (!addedUsernames.contains(permission.user.id)) {
                peopleListPanel.add(createPersonRow(permission.user.id, permission.permission, permission.user.name));
                addedUsernames.add(permission.user.id);
            }
        }

        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        JPanel separatorWrapper = new JPanel(new BorderLayout());
        separatorWrapper.setBackground(Color.WHITE);
        separatorWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separatorWrapper.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        separatorWrapper.setBorder(new EmptyBorder(0, 8, 0, 8));
        JSeparator underline = new JSeparator();
        underline.setForeground(new Color(60, 60, 60));
        separatorWrapper.add(underline, BorderLayout.CENTER);
        mainPanel.add(separatorWrapper);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        separatorWrapper.setBackground(Color.BLACK);

        // ===== General Access =====
        JPanel generalLabelWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        generalLabelWrapper.setBackground(Color.WHITE);
        JLabel generalAccessLabel = new JLabel("General access");
        generalAccessLabel.setFont(new Font("Arial", Font.BOLD, 14));
        generalLabelWrapper.add(generalAccessLabel);

        mainPanel.add(generalLabelWrapper);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel accessRow = new JPanel();
        accessRow.setLayout(new BoxLayout(accessRow, BoxLayout.X_AXIS));
        accessRow.setBackground(Color.WHITE);

        String[] roles = {"Viewer", "Remove Access"};

        Set<String> combinedGroupSet = new HashSet<>();
        combinedGroupSet.add("Restricted");
        combinedGroupSet.addAll(DocumentDAO.getAllRoles());
        combinedGroupSet.addAll(DocumentDAO.getAllDepartments());
        combinedGroupSet.add("All Staff");

        String[] groups = combinedGroupSet.toArray(new String[0]);

        groupDropdown = new JComboBox<>(groups);
        groupDropdown.setFont(new Font("Arial", Font.PLAIN, 13));
        groupDropdown.setPreferredSize(new Dimension(200, 36));

        roleDropdown = new JComboBox<>(roles);
        roleDropdown.setFont(new Font("Arial", Font.PLAIN, 13));
        roleDropdown.setPreferredSize(new Dimension(120, 36));

        groupDropdown.setSelectedItem(doc.generalAccessGroup != null ? doc.generalAccessGroup : "Restricted");

        String selected = doc.generalAccessGroup != null ? doc.generalAccessGroup : "Restricted";

        boolean found = false;
        for (int i = 0; i < groupDropdown.getItemCount(); i++) {
            if (groupDropdown.getItemAt(i).equals(selected)) {
                found = true;
                break;
            }
        }

        if (found) {
            groupDropdown.setSelectedItem(selected);
        } else {
            System.out.println("Group not found in combo box: " + selected + " — forcing to Restricted");
            groupDropdown.setSelectedItem("Restricted");
        }

        if (!selected.equals(groupDropdown.getSelectedItem())) {
            System.out.println("Warning: Value not matched in combo box. Forcing fallback.");
            groupDropdown.setSelectedIndex(0);
        }

        if (doc.generalAccessRole == null || "Restricted".equals(doc.generalAccessGroup)) {
            roleDropdown.setSelectedItem(null);
            roleDropdown.setEnabled(false);
        } else {
            roleDropdown.setSelectedItem(doc.generalAccessRole);
            roleDropdown.setEnabled(true);
        }

        if ("Restricted".equals(doc.generalAccessGroup)) {
            roleDropdown.setSelectedItem(null);
            roleDropdown.setEnabled(false);
        }

        groupDropdown.addActionListener(e -> {
            String selectedGroup = (String) groupDropdown.getSelectedItem();
            if ("Restricted".equals(selectedGroup)) {
                roleDropdown.setSelectedItem(null);
                roleDropdown.setEnabled(false);
            } else {
                roleDropdown.setEnabled(true);
                if (roleDropdown.getSelectedItem() == null) {
                    roleDropdown.setSelectedItem("Viewer");
                }
            }
        });

        ImageIcon dropdownIcon = new ImageIcon("img/icon-dropdown.png");

        groupDropdown.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton(dropdownIcon);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                return button;
            }
        });

        groupDropdown.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 170, 190)),
                new EmptyBorder(5, 12, 5, 12)
        ));
        groupDropdown.setBackground(Color.WHITE);
        groupDropdown.setForeground(new Color(40, 30, 50));
        groupDropdown.setFont(new Font("Arial", Font.PLAIN, 15));

        roleDropdown.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton(dropdownIcon);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                return button;
            }
        });

        roleDropdown.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 170, 190)),
                new EmptyBorder(5, 12, 5, 12)
        ));
        roleDropdown.setBackground(Color.WHITE);
        roleDropdown.setForeground(new Color(40, 30, 50));
        roleDropdown.setFont(new Font("Arial", Font.PLAIN, 15));

        accessRow.add(groupDropdown);
        accessRow.add(Box.createRigidArea(new Dimension(12, 0)));
        accessRow.add(roleDropdown);
        accessRow.add(Box.createRigidArea(new Dimension(16, 0)));
        mainPanel.add(accessRow);

        JLabel hintText = new JLabel("Anyone in this group with the link can view");
        hintText.setFont(new Font("Arial", Font.PLAIN, 12));
        hintText.setForeground(Color.GRAY);
        hintText.setBorder(new EmptyBorder(8, 4, 8, 0));
        JPanel hintWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hintWrapper.setBackground(Color.WHITE);
        hintWrapper.add(hintText);
        mainPanel.add(hintWrapper);

        // ===== Done Button =====
        JButton doneButton = new JButton("Done");
        doneButton.setFont(new Font("Arial", Font.BOLD, 15));
        doneButton.setBackground(new Color(0x5A6ACF));
        doneButton.setForeground(Color.WHITE);
        doneButton.setFocusPainted(false);
        doneButton.setOpaque(true);
        doneButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        doneButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        doneButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        doneButton.addActionListener(e -> {
            String selectedGroup = (String) groupDropdown.getSelectedItem();
            String selectedRole = (String) roleDropdown.getSelectedItem();
            if ("Remove Access".equals(selectedRole)) {
                selectedGroup = "Restricted";
                selectedRole = null;
            }
            DocumentDAO.updateGeneralAccess(currentDoc.id, selectedGroup, selectedRole);
            currentDoc.generalAccessGroup = selectedGroup;
            currentDoc.generalAccessRole = selectedRole;

            dispose();
        });

        mainPanel.add(Box.createVerticalStrut(14));
        mainPanel.add(doneButton);

        add(mainPanel);
        updatePanelHeight();
        setVisible(true);
    }

    class PlaceholderTextField extends JTextField {
        private String placeholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setForeground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setFont(getFont().deriveFont(Font.PLAIN));
                g2.setColor(Color.GRAY);
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 2, (getHeight() + g2.getFontMetrics().getAscent()) / 2 - 2);
                g2.dispose();
            }
        }
    }

    private JPanel createPersonRow(String username, String role, String displayName) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(Color.WHITE);

        JPanel personRow = new JPanel(new BorderLayout());
        personRow.setBackground(Color.WHITE);
        personRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.setBackground(Color.WHITE);

        ImageIcon userIcon = new ImageIcon("img/icon-user.png");
        Image scaledIcon = userIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JLabel avatarLabel = new JLabel(new ImageIcon(scaledIcon));
        avatarLabel.setBorder(new EmptyBorder(0, 0, 0, 12));

        JPanel nameBlock = new JPanel();
        nameBlock.setLayout(new BoxLayout(nameBlock, BoxLayout.Y_AXIS));
        nameBlock.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        usernameLabel.setForeground(new Color(140, 140, 140));

        nameBlock.add(nameLabel);
        nameBlock.add(usernameLabel);

        leftPanel.add(avatarLabel);
        leftPanel.add(nameBlock);

        personRow.add(leftPanel, BorderLayout.WEST);

        if (role.equalsIgnoreCase("Owner")) {
            JLabel roleLabel = new JLabel(role);
            roleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            roleLabel.setForeground(new Color(140, 140, 140));
            roleLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
            personRow.add(roleLabel, BorderLayout.EAST);
        } else {
            String[] roleOptions = {"Viewer", "Editor", "Remove Access"};
            JComboBox<String> roleDropdown = new JComboBox<>(roleOptions);
            roleDropdown.setSelectedItem(role);
            roleDropdown.setFont(new Font("Arial", Font.PLAIN, 13));
            Dimension dropdownSize = new Dimension(120, 28);
            roleDropdown.setPreferredSize(dropdownSize);
            roleDropdown.setMaximumSize(dropdownSize);
            roleDropdown.setMinimumSize(dropdownSize);
            roleDropdown.setAlignmentY(Component.TOP_ALIGNMENT);
            roleDropdown.setBackground(Color.WHITE);
            roleDropdown.setForeground(new Color(60, 60, 60));

            roleDropdown.setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton button = new JButton(new ImageIcon("img/icon-dropdown.png"));
                    button.setBorder(BorderFactory.createEmptyBorder());
                    button.setContentAreaFilled(false);
                    return button;
                }
            });

            roleDropdown.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 170, 190)),
                    new EmptyBorder(2, 8, 2, 8)
            ));

            roleDropdown.addActionListener(e -> {
                String selectedRole = (String) roleDropdown.getSelectedItem();
                if ("Remove Access".equals(selectedRole)) {
                    groupDropdown.setSelectedItem("Restricted");

                    groupDropdown.dispatchEvent(new ActionEvent(groupDropdown, ActionEvent.ACTION_PERFORMED, ""));

                    roleDropdown.setSelectedItem(null);
                    roleDropdown.setEnabled(false);

                    System.out.println("General Access: Role di-set ke 'Remove Access' → Group auto 'Restricted', Role kosong.");
                }
            });

            personRow.add(roleDropdown, BorderLayout.EAST);
            wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        }

        wrapper.add(personRow);
        wrapper.add(Box.createRigidArea(new Dimension(0, 8)));

        return wrapper;
    }

    private void updateScrollPolicy() {
        int actualUserCount = 0;

        for (Component comp : peopleListPanel.getComponents()) {
            if (comp instanceof JPanel) actualUserCount++;
        }

        boolean needsScroll = actualUserCount > 3;

        scrollPane.setVerticalScrollBarPolicy(
                needsScroll ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
                        : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
        );
    }

    private void updatePanelHeight() {
        int addedUserCount = 0;
        for (Component comp : peopleListPanel.getComponents()) {
            if (comp instanceof JPanel panel && panel.getComponentCount() == 2) {
                addedUserCount++;
            }
        }

        int totalUserCount = addedUserCount + 1;
        int rowHeight = 48;
        int maxVisibleRows = 4;

        if (addedUserCount == 0) {
            scrollPane.setPreferredSize(new Dimension(400, 0));
            scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        } else {
            int visibleHeight = totalUserCount >= maxVisibleRows
                    ? maxVisibleRows * rowHeight
                    : totalUserCount * rowHeight;
            scrollPane.setPreferredSize(new Dimension(400, visibleHeight));
            scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, visibleHeight));

            scrollPane.setVerticalScrollBarPolicy(
                    totalUserCount > maxVisibleRows
                            ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
                            : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
            );
        }

        scrollPane.revalidate();
        scrollPane.repaint();

        int dialogHeight = 400 + Math.min(totalUserCount, maxVisibleRows) * rowHeight;
        dialogHeight = Math.min(dialogHeight, 560);
        setSize(getWidth(), dialogHeight);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame dummyFrame = new JFrame();
            dummyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dummyFrame.setSize(400, 300);

            // Dummy user
            Document.User dummyOwner = new Document.User("dummyuser", "Dummy User", "Owner");

            Map<String, Document.User> dummyUsers = new java.util.HashMap<>();
            dummyUsers.put(dummyOwner.getUsername(), dummyOwner);

            Document.Doc dummyDoc = new Document.Doc(
                    "dummy-id",
                    "File Name",
                    "Dummy description",
                    dummyOwner,
                    new java.util.Date(),
                    new java.util.Date(),
                    new java.util.Date(),
                    1,
                    "PDF",
                    1234,
                    "/path/to/file.pdf"
            );

            new AccessControl(dummyFrame, dummyDoc.title, dummyDoc, dummyUsers);
            dummyFrame.setVisible(false);
        });
    }
}
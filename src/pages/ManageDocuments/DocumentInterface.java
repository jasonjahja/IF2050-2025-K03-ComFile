package pages.ManageDocuments;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DocumentInterface {
    public static void main(String[] args) {
        JFrame frame = new JFrame("File Name");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        Color bgSoftLavender = new Color(245, 243, 247);
        Color textNormal = new Color(55, 40, 80);
        Color textHover = new Color(90, 70, 120);

        frame.getContentPane().setBackground(bgSoftLavender);

        // ========== Header ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 218, 230)));

        ImageIcon backIcon = new ImageIcon("img/icon-back.png");
        Image scaledBack = backIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel backLabel = new JLabel(" File Name", new ImageIcon(scaledBack), JLabel.LEFT);
        backLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        backLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.setForeground(textNormal);
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setForeground(textHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(textNormal);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Back clicked");
            }
        });
        header.add(backLabel, BorderLayout.WEST);

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 10));
        iconPanel.setOpaque(false);
        iconPanel.add(createIconWithText("img/icon-sharing.png", "Sharing", textNormal, textHover));
        iconPanel.add(createIconWithText("img/icon-backup.png", "Backup", textNormal, textHover));
        iconPanel.add(createIconWithText("img/icon-delete.png", "Delete", textNormal, textHover));
        header.add(iconPanel, BorderLayout.EAST);

        frame.add(header, BorderLayout.NORTH);

        // ========== Dokumen A4 ==========
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(bgSoftLavender);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 60, 0));

        JPanel docPanel = new JPanel();
        docPanel.setPreferredSize(new Dimension(794, 1123));
        docPanel.setBackground(Color.WHITE);
        docPanel.setLayout(new BorderLayout());
        docPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JTextArea textArea = new JTextArea("Ensure your data is safe and recoverable. ".repeat(100));
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.getCaret().setVisible(false);

        JScrollPane docScrollPane = new JScrollPane(textArea);
        docScrollPane.setBorder(null);
        docScrollPane.setOpaque(false);
        docScrollPane.getViewport().setOpaque(false);

        docPanel.add(docScrollPane, BorderLayout.CENTER);
        outerPanel.add(docPanel);

        JScrollPane scrollFrame = new JScrollPane(outerPanel);
        scrollFrame.setBorder(null);
        scrollFrame.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollFrame, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // ========== Fungsi Buat Tombol ==========
    private static JPanel createIconWithText(String iconPath, String labelText, Color textNormal, Color textHover) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon rawIcon = new ImageIcon(iconPath);
        Image scaledImg = rawIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImg));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLabel.setForeground(textNormal);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(textLabel);

        // Efek hover
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                textLabel.setForeground(textHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                textLabel.setForeground(textNormal);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (labelText.equals("Delete")) {
                    showDeleteConfirmation(SwingUtilities.getWindowAncestor(panel));
                } else {
                    System.out.println(labelText + " clicked");
                }
            }
        });

        return panel;
    }

    // ========== Delete Confirmation Dialog ==========
    private static void showDeleteConfirmation(Window parent) {
        // Custom colors
        Color dangerColor = new Color(214, 41, 85);  // Bright pink/red color
        Color textColor = new Color(67, 63, 78);     // Dark gray for text

        // Create glass pane for dark overlay
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        glassPane.setOpaque(false);
        ((JFrame) parent).setGlassPane(glassPane);
        glassPane.setVisible(true);

        // Create custom dialog
        JDialog dialog = new JDialog((JFrame)parent, "", true);
        dialog.setUndecorated(true); // Remove window decorations
        dialog.setBackground(Color.WHITE);

        // Main panel with rounded corners
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.dispose();
            }
        };
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Center content panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Delete illustration
        ImageIcon deleteIcon = new ImageIcon("img/delete-illustration.png");
        Image scaledImage = deleteIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Text content
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel warningLabel = new JLabel("<html><div style='text-align: center;'>This action cannot be <span style='color: rgb(214, 41, 85);'>undone</span>. Deleting this item<br/>will <span style='color: rgb(214, 41, 85);'>permanently remove</span> it from your account.</div></html>");
        warningLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        warningLabel.setForeground(textColor);
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel confirmLabel = new JLabel("Are you sure you want to proceed?");
        confirmLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        confirmLabel.setForeground(textColor);
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with proper spacing
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(warningLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(confirmLabel);
        centerPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Delete button
        JButton deleteButton = new JButton("Delete Document") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        deleteButton.setBackground(dangerColor);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        deleteButton.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setContentAreaFilled(false);

        // Cancel button
        JButton cancelButton = new JButton("Cancel") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(dangerColor);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cancelButton.setForeground(dangerColor);
        cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setContentAreaFilled(false);

        // Add button actions
        deleteButton.addActionListener(e -> {
            System.out.println("Document deleted");
            glassPane.setVisible(false);
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> {
            glassPane.setVisible(false);
            dialog.dispose();
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add shadow border to the main panel
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                mainPanel.getBorder()
        ));

        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
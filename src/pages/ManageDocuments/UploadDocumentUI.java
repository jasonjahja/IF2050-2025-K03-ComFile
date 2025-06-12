package pages.ManageDocuments;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import storage.DocumentStorage;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;

public class UploadDocumentUI extends JDialog {

    public UploadDocumentUI(JFrame parent, Runnable onUploadComplete) {
        super(parent, "Upload Document", true); // Make it modal
        setUndecorated(true); // <-- hilangkan title bar dan window control
        setSize(800, 600);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);
        setResizable(false);

        // Main wrapper dengan padding
        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setBackground(Color.WHITE);
        mainWrapper.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Upload Document");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("SansSerif", Font.PLAIN, 22));
        closeButton.setForeground(new Color(100, 100, 100));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        headerPanel.add(closeButton, BorderLayout.EAST);

        // Content Panel dengan GridBagLayout untuk centering
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        // Panel untuk upload area
        JPanel uploadPanel = new JPanel();
        uploadPanel.setLayout(new BoxLayout(uploadPanel, BoxLayout.Y_AXIS));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.setBorder(BorderFactory.createDashedBorder(new Color(200, 200, 200), 2, 5.0f, 5.0f, true));
        uploadPanel.setPreferredSize(new Dimension(600, 400));

        // Cloud icon
        JLabel iconLabel = new JLabel();
        try {
            URL imageUrl = getClass().getClassLoader().getResource("img/cloud.png");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaled));
            } else {
                iconLabel.setText("Icon");
            }
        } catch (Exception e) {
            iconLabel.setText("Icon");
        }
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Text labels
        JLabel selectText = new JLabel("Select a document to upload");
        selectText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        selectText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dragDropText = new JLabel("or drag and drop it here");
        dragDropText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dragDropText.setForeground(new Color(150, 150, 150));
        dragDropText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Upload button
        JButton uploadButton = new JButton("+ Upload Document") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        uploadButton.setBackground(Color.decode("#5A6ACF"));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadButton.setOpaque(false);
        uploadButton.setContentAreaFilled(false);
        uploadButton.setBorderPainted(false);
        uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        uploadButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.setPreferredSize(new Dimension(180, 40));
        uploadButton.setMinimumSize(new Dimension(180, 40));
        uploadButton.setMaximumSize(new Dimension(180, 40));

        uploadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color normalColor = Color.decode("#5A6ACF");
            final Color hoverColor = new Color(70, 86, 187);
            final Color pressedColor = new Color(55, 70, 150);

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                uploadButton.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                uploadButton.setBackground(normalColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                uploadButton.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                uploadButton.setBackground(hoverColor);
            }
        });

        uploadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a document to upload");

            int result = fileChooser.showOpenDialog(UploadDocumentUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                long maxSize = 10L * 1024 * 1024;
                if (selectedFile.length() > maxSize) {
                    JOptionPane.showMessageDialog(
                            UploadDocumentUI.this,
                            "File size exceeds maximum limit of 10 MB",
                            "Upload Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (DocumentStorage.isDocumentExists(selectedFile.getName())) {
                    JOptionPane.showMessageDialog(
                            UploadDocumentUI.this,
                            "A file with the same name already exists",
                            "Upload Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (DocumentStorage.storeDocument(selectedFile)) {
                    if (onUploadComplete != null) {
                        onUploadComplete.run();
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            UploadDocumentUI.this,
                            "Failed to store the file",
                            "Upload Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        JLabel sizeText = new JLabel("Maximum upload size 10 MB");
        sizeText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sizeText.setForeground(new Color(150, 150, 150));
        sizeText.setAlignmentX(Component.CENTER_ALIGNMENT);

        uploadPanel.add(Box.createVerticalStrut(50));
        uploadPanel.add(iconLabel);
        uploadPanel.add(Box.createVerticalStrut(20));
        uploadPanel.add(selectText);
        uploadPanel.add(Box.createVerticalStrut(5));
        uploadPanel.add(dragDropText);
        uploadPanel.add(Box.createVerticalStrut(30));
        uploadPanel.add(uploadButton);
        uploadPanel.add(Box.createVerticalStrut(15));
        uploadPanel.add(sizeText);
        uploadPanel.add(Box.createVerticalStrut(50));

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(Color.WHITE);
        topSection.add(headerPanel, BorderLayout.CENTER);
        topSection.add(separator, BorderLayout.SOUTH);

        mainWrapper.add(topSection, BorderLayout.NORTH);
        contentPanel.add(uploadPanel);
        mainWrapper.add(contentPanel, BorderLayout.CENTER);

        add(mainWrapper);

        getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, new Color(0, 0, 0, 30)));
        setVisible(true);
    }

    private JButton findUploadButton(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().contains("Upload")) {
                    return btn;
                }
            } else if (comp instanceof Container) {
                JButton btn = findUploadButton((Container) comp);
                if (btn != null) return btn;
            }
        }
        return null;
    }
}



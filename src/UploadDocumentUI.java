import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UploadDocumentUI extends JFrame {

    public UploadDocumentUI() {
        setTitle("Upload Document");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

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
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
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
            ImageIcon icon = new ImageIcon("img/cloud.png");
            Image scaled = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaled));
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
        JButton uploadButton = new JButton("+ Upload Document");
        uploadButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setBackground(Color.decode("#5A6ACF"));
        uploadButton.setFocusPainted(false);
        uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.setPreferredSize(new Dimension(180, 40));
        uploadButton.setMinimumSize(new Dimension(180, 40));
        uploadButton.setMaximumSize(new Dimension(180, 40));
        uploadButton.setOpaque(true);

        // Hover effect
        uploadButton.addMouseListener(new MouseAdapter() {
            private final Color defaultColor = Color.decode("#5A6ACF");
            private final Color hoverColor = Color.decode("#4857B0");

            @Override
            public void mouseEntered(MouseEvent e) {
                uploadButton.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                uploadButton.setBackground(defaultColor);
            }
        });

        // ✅ Logika pemilihan dan penyimpanan file
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select a document to upload");

                int result = fileChooser.showOpenDialog(UploadDocumentUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    
                    // Cek ukuran file (max 10MB)
                    long maxSize = 10L * 1024 * 1024; // 10MB in bytes
                    if (selectedFile.length() > maxSize) {
                        JOptionPane.showMessageDialog(
                            UploadDocumentUI.this,
                            "File size exceeds maximum limit of 10 MB",
                            "Upload Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    // Cek file duplikat
                    if (DocumentStorage.isDocumentExists(selectedFile.getName())) {
                        JOptionPane.showMessageDialog(
                            UploadDocumentUI.this,
                            "A file with the same name already exists",
                            "Upload Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    // Simpan file menggunakan DocumentStorage
                    if (DocumentStorage.storeDocument(selectedFile)) {
                        JOptionPane.showMessageDialog(
                            UploadDocumentUI.this,
                            "File uploaded successfully\nSize: " + DocumentStorage.getReadableFileSize(selectedFile),
                            "Upload Successful",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            UploadDocumentUI.this,
                            "Failed to store the file",
                            "Upload Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        // Ukuran maksimal label
        JLabel sizeText = new JLabel("Maximum upload size 10 MB");
        sizeText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sizeText.setForeground(new Color(150, 150, 150));
        sizeText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Susun upload panel
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

        // Separator setelah header
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(Color.WHITE);
        topSection.add(headerPanel, BorderLayout.CENTER);
        topSection.add(separator, BorderLayout.SOUTH);

        // Final Layouting
        mainWrapper.add(topSection, BorderLayout.NORTH);
        contentPanel.add(uploadPanel);
        mainWrapper.add(contentPanel, BorderLayout.CENTER);

        add(mainWrapper);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore look and feel errors
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UploadDocumentUI();
            }
        });
    }
}


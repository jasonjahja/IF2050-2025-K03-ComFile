package pages.ManageDocuments;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import storage.DocumentStorage;

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
        JButton uploadButton = new JButton("+ Upload Document") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); // rounded rectangle
                g2.dispose();
                super.paintComponent(g);
            }
        };

        uploadButton.setBackground(Color.decode("#5A6ACF")); // biru sesuai warna kamu
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Custom behavior: no border, no default background fill
        uploadButton.setOpaque(false);
        uploadButton.setContentAreaFilled(false);
        uploadButton.setBorderPainted(false);

        // Padding & font
        uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        uploadButton.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Optional: size lock
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.setPreferredSize(new Dimension(180, 40));
        uploadButton.setMinimumSize(new Dimension(180, 40));
        uploadButton.setMaximumSize(new Dimension(180, 40));

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

    public UploadDocumentUI(Runnable onUploadComplete) {
        this(); // panggil constructor utama untuk membangun UI

        // Ambil ulang tombol "upload" dari UI yang sudah dibuat
        JButton uploadButton = findUploadButton(this.getContentPane());

        if (uploadButton != null) {
            // Hapus semua listener default dari constructor pertama
            for (ActionListener al : uploadButton.getActionListeners()) {
                uploadButton.removeActionListener(al);
            }

            // Tambahkan action baru dengan callback
            uploadButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select a document to upload");

                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    long maxSize = 10L * 1024 * 1024;
                    if (selectedFile.length() > maxSize) {
                        JOptionPane.showMessageDialog(this, "File size exceeds 10MB", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (DocumentStorage.isDocumentExists(selectedFile.getName())) {
                        JOptionPane.showMessageDialog(this, "Duplicate file name", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (DocumentStorage.storeDocument(selectedFile)) {
                        JOptionPane.showMessageDialog(this, "Upload successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                        if (onUploadComplete != null) {
                            onUploadComplete.run(); // ✅ Refresh dokumen
                        }
                        dispose(); // Tutup window
                    } else {
                        JOptionPane.showMessageDialog(this, "Upload failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
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


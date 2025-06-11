package pages.ManageDocuments;

import components.SearchBar;
import components.Filter;
import pages.ManageDocuments.Document.Doc;
import utils.DocumentDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.util.stream.Collectors;

public class MyDocuments extends JPanel {
    private JPanel documentsGrid;
    private JPanel documentsWrapper;
    private Color defaultBorderColor = new Color(200, 200, 200);
    private Color hoverBorderColor = new Color(90, 106, 207);
    private static final int FILTER_WIDTH = 250;
    private SearchBar searchBar;
    private Filter filterComponent;
    private List<Doc> cachedDocs = new ArrayList<>();

    public MyDocuments() {
        initializeComponents();
        setupLayout();
        refreshDocumentsAsync();
    }

    private void initializeComponents() {
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        documentsGrid = new JPanel(new GridLayout(0, 4, 20, 20));
        documentsGrid.setBackground(new Color(248, 249, 250));
        documentsGrid.setBorder(new EmptyBorder(20, 20, 20, 20));

        documentsWrapper = new JPanel(new BorderLayout());
        documentsWrapper.setPreferredSize(null); // atau cukup hapus baris ini
        documentsWrapper.setBackground(new Color(248, 249, 250));
        documentsWrapper.removeAll();
        documentsWrapper.add(documentsGrid, BorderLayout.NORTH);
    }

    private void setupLayout() {
        JPanel mainContainer = new JPanel(null);
        mainContainer.setBackground(new Color(248, 249, 250));

        // === Filter Panel ===
        filterComponent = new Filter();
        filterComponent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        // filterComponent.setApplyAction(this::filterDocuments);
        // filterComponent.setOnResetAction(this::refreshDocuments);

        filterComponent.setApplyAction(this::filterAndSearch);
        filterComponent.setOnResetAction(this::filterAndSearch);

        // === Documents Scroll Area ===
        JPanel documentsContainer = new JPanel(new BorderLayout());
        documentsContainer.setBackground(new Color(248, 249, 250));

        JScrollPane documentsScrollPane = new JScrollPane(documentsWrapper);
        documentsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // ⬅️ paksa scrollbar tampil
        documentsScrollPane.setBorder(null);
        documentsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        documentsScrollPane.setBackground(new Color(248, 249, 250));

        documentsContainer.add(documentsScrollPane, BorderLayout.CENTER);

        mainContainer.add(filterComponent);
        mainContainer.add(documentsContainer);

        mainContainer.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                filterComponent.setBounds(20, 20, FILTER_WIDTH, filterComponent.getPreferredSize().height);
                documentsContainer.setBounds(
                        FILTER_WIDTH + 40, 0,
                        mainContainer.getWidth() - (FILTER_WIDTH + 40),
                        mainContainer.getHeight()
                );
            }
        });

        // === Header Panel (Title + Add Button) ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 249, 250));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("My Documents");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Document") {
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
        addButton.setBackground(new Color(90, 106, 207));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setOpaque(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        addButton.setFont(new Font("Arial", Font.BOLD, 14));

        addButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(new Color(70, 86, 187));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                addButton.setBackground(new Color(90, 106, 207));
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        addButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            // Buat panel semi-transparan untuk background gelap
            JPanel darkOverlay = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(new Color(0, 0, 0, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            darkOverlay.setOpaque(false);

            // Buat dialog upload
            JPanel uploadPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                    // Draw border
                    g2.setColor(new Color(200, 200, 200));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);

                    g2.dispose();
                }

                @Override
                public void paintBorder(Graphics g) {
                    // Don't paint the default border
                }
            };
            uploadPanel.setBackground(Color.WHITE);
            uploadPanel.setOpaque(false);

            // Header Panel
            JPanel uploadHeaderPanel = new JPanel(new BorderLayout());
            uploadHeaderPanel.setBackground(Color.WHITE);
            uploadHeaderPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

            JLabel titleLabel = new JLabel("Upload Document");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

            JButton closeButton = new JButton("×");
            closeButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
            closeButton.setForeground(new Color(100, 100, 100));
            closeButton.setBorderPainted(false);
            closeButton.setContentAreaFilled(false);
            closeButton.setFocusPainted(false);
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeButton.addActionListener(event -> {
                topFrame.getGlassPane().setVisible(false);
            });

            uploadHeaderPanel.add(titleLabel, BorderLayout.WEST);
            uploadHeaderPanel.add(closeButton, BorderLayout.EAST);

            // Content Panel
            JPanel contentPanel = new JPanel();
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(new EmptyBorder(20, 40, 30, 40));

            // Upload area dengan border putus-putus
            JPanel uploadArea = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Draw dashed rounded rectangle
                    Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
                    g2.setStroke(dashed);
                    g2.setColor(new Color(200, 200, 200));
                    g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                    g2.dispose();
                }
            };
            uploadArea.setLayout(new BoxLayout(uploadArea, BoxLayout.Y_AXIS));
            uploadArea.setBackground(Color.WHITE);
            uploadArea.setPreferredSize(new Dimension(500, 300));

            // Cloud icon
            JLabel iconLabel = new JLabel();
            try {
                String path = System.getProperty("user.dir") + "/img/cloud.png";
                File imageFile = new File(path);
                if (imageFile.exists()) {
                    ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                    Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    iconLabel.setIcon(new ImageIcon(scaled));
                }
            } catch (Exception ex) {
                iconLabel.setText("☁️");
                iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 48));
            }
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel selectText = new JLabel("Select a document to upload");
            selectText.setFont(new Font("SansSerif", Font.PLAIN, 16));
            selectText.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel dragDropText = new JLabel("or drag and drop it here");
            dragDropText.setFont(new Font("SansSerif", Font.PLAIN, 12));
            dragDropText.setForeground(new Color(150, 150, 150));
            dragDropText.setAlignmentX(Component.CENTER_ALIGNMENT);

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
            uploadButton.setBackground(new Color(90, 106, 207));
            uploadButton.setForeground(Color.WHITE);
            uploadButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            uploadButton.setFocusPainted(false);
            uploadButton.setContentAreaFilled(false);
            uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            uploadButton.addActionListener(event -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select a document to upload");

                int result = fileChooser.showOpenDialog(topFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    // Check file size
                    long maxSize = 10L * 1024 * 1024;
                    if (selectedFile.length() > maxSize) {
                        JOptionPane.showMessageDialog(
                                topFrame,
                                "File size exceeds maximum limit of 10 MB",
                                "Upload Failed",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    // Buat objek Doc
                    String filename = selectedFile.getName();
                    int dotIndex = filename.lastIndexOf('.');
                    String docTitle = (dotIndex > 0) ? filename.substring(0, dotIndex) : filename;
                    String fileType = (dotIndex > 0) ? filename.substring(dotIndex + 1) : "";
                    int sizeKB = (int) (selectedFile.length() / 1024);
                    Date now = new Date();

                    Doc newDoc = new Doc(
                            UUID.randomUUID().toString(),
                            docTitle,
                            "Uploaded via UI",
                            Document.currentUser,
                            now, now, now,
                            1,
                            fileType.toUpperCase(),
                            sizeKB,
                            selectedFile.getAbsolutePath()
                    );

                    // Simpan ke database
                    DocumentDAO.saveDocToDatabase(newDoc);

                    // Refresh UI
                    refreshDocuments();
                    topFrame.getGlassPane().setVisible(false);
                }
            });

            JLabel sizeText = new JLabel("Maximum upload size 10 MB");
            sizeText.setFont(new Font("SansSerif", Font.PLAIN, 12));
            sizeText.setForeground(new Color(150, 150, 150));
            sizeText.setAlignmentX(Component.CENTER_ALIGNMENT);

            uploadArea.add(Box.createVerticalStrut(40));
            uploadArea.add(iconLabel);
            uploadArea.add(Box.createVerticalStrut(20));
            uploadArea.add(selectText);
            uploadArea.add(Box.createVerticalStrut(5));
            uploadArea.add(dragDropText);
            uploadArea.add(Box.createVerticalStrut(30));
            uploadArea.add(uploadButton);
            uploadArea.add(Box.createVerticalStrut(5));
            uploadArea.add(sizeText);
            uploadArea.add(Box.createVerticalStrut(20));  // Tambah jarak ke bawah

            contentPanel.add(uploadArea);

            uploadPanel.add(uploadHeaderPanel, BorderLayout.NORTH);
            uploadPanel.add(contentPanel, BorderLayout.CENTER);

            // Add components to dark overlay
            darkOverlay.add(uploadPanel);

            // Set up glass pane
            topFrame.setGlassPane(darkOverlay);
            topFrame.getGlassPane().setVisible(true);
        });

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // === Combine SearchBar + Header ===
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(new Color(248, 249, 250));
        topContainer.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel searchBarWrapper = new JPanel(new BorderLayout());
        searchBarWrapper.setBackground(new Color(248, 249, 250));
        searchBar = new SearchBar();
        searchBarWrapper.add(searchBar, BorderLayout.CENTER);

        topContainer.add(headerPanel);
        topContainer.add(Box.createVerticalStrut(20)); // spacing
        topContainer.add(searchBarWrapper);
        topContainer.add(Box.createVerticalStrut(10)); // spacing

        add(topContainer, BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);

        searchBar.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterAndSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterAndSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterAndSearch(); }
        });
    }

    private void loadDocumentsFromDatabase() {
        documentsGrid.removeAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        List<Doc> allDocs = DocumentDAO.getAllDocumentsFromDB();
        List<Doc> visibleDocs = allDocs.stream()
                .filter(doc -> Document.hasAccess(doc, Document.currentUser))
                .collect(Collectors.toList());

        if (visibleDocs.isEmpty()) {
            JLabel emptyMsg = new JLabel("No documents found.");
            emptyMsg.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emptyMsg.setHorizontalAlignment(SwingConstants.CENTER);
            documentsGrid.add(emptyMsg);
        } else {
            for (Doc doc : visibleDocs) {
                File file = new File(doc.filePath);
                String date = file.exists() ? dateFormat.format(file.lastModified()) : dateFormat.format(doc.modifiedDate);
                documentsGrid.add(createDocumentCard(doc, date));
            }
        }

        documentsGrid.revalidate();
        documentsGrid.repaint();
    }

    private JPanel createDocumentCard(Doc doc, String dateText) {
        JPanel docCard = new JPanel(new BorderLayout());
        docCard.setPreferredSize(new Dimension(200, 280));
        docCard.setBackground(Color.WHITE);
        docCard.putClientProperty("docId", doc.id);
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
        imageContainer.setPreferredSize(new Dimension(180, 200));

        if (docImage == null || docImage.getImageLoadStatus() == MediaTracker.ERRORED) {
            JLabel docPlaceholder = new JLabel("📄");
            docPlaceholder.setFont(new Font("Arial", Font.PLAIN, 64));
            docPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
            imageContainer.add(docPlaceholder, BorderLayout.CENTER);
        } else {
            Image scaledDoc = docImage.getImage().getScaledInstance(160, 180, Image.SCALE_SMOOTH);
            JLabel docImageLabel = new JLabel(new ImageIcon(scaledDoc));
            docImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageContainer.add(docImageLabel, BorderLayout.CENTER);
        }

        imageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        docCard.add(imageContainer, BorderLayout.CENTER);

        // Info Panel (Title + Date)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        JLabel docTitle = new JLabel(doc.title);
        docTitle.setFont(new Font("Arial", Font.BOLD, 13));
        docTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        docTitle.setBorder(new EmptyBorder(5, 0, 2, 0));
        JLabel docDate = new JLabel(dateText);
        docDate.setFont(new Font("Arial", Font.PLAIN, 11));
        docDate.setForeground(new Color(100, 100, 100));
        docDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(docTitle);
        infoPanel.add(docDate);

        // Bottom Panel (Info + Options)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(infoPanel, BorderLayout.CENTER);

        //Options Button
        ImageIcon icon = new ImageIcon("img/icon-dots.png");
        Image scaled = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel optionsLabel = new JLabel(new ImageIcon(scaled));
        optionsLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        bottomPanel.add(optionsLabel, BorderLayout.EAST);
        docCard.add(bottomPanel, BorderLayout.SOUTH);

        // Popup Menu
        JPopupMenu popupMenu = new JPopupMenu();

        ImageIcon personIcon = null;
        ImageIcon binIcon = null;
        try {
            String personPath = System.getProperty("user.dir") + "/img/person.png";
            String binPath = System.getProperty("user.dir") + "/img/bin.png";
            File personFile = new File(personPath);
            File binFile = new File(binPath);
            if (personFile.exists()) personIcon = new ImageIcon(personFile.getAbsolutePath());
            if (binFile.exists()) binIcon = new ImageIcon(binFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JMenuItem manageAccess = new JMenuItem("Manage Access", personIcon);
        manageAccess.setFont(new Font("SansSerif", Font.BOLD, 12));
        manageAccess.addActionListener(event -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(documentsGrid);

            // Buat panel overlay gelap
            JPanel darkOverlay = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(new Color(0, 0, 0, 150)); // Semi-transparan hitam
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            darkOverlay.setOpaque(false);

            // Set ke glass pane
            topFrame.setGlassPane(darkOverlay);
            topFrame.getGlassPane().setVisible(true);

            // Tampilkan dialog AccessControl
            JDialog accessDialog = new components.AccessControl(topFrame, doc.title, doc, Document.users);

            // Ketika dialog ditutup, hilangkan overlay
            accessDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    topFrame.getGlassPane().setVisible(false);
                }
            });
        });

        JMenuItem deleteDoc = new JMenuItem("Delete Document", binIcon);
        deleteDoc.setForeground(new Color(214, 41, 85));
        deleteDoc.setFont(new Font("SansSerif", Font.BOLD, 12));

// Cek role sebelum menambahkan menu
        if (Document.currentUser != null &&
                (Document.currentUser.role.equalsIgnoreCase("Manajer") || Document.currentUser.role.equalsIgnoreCase("Admin"))) {
            popupMenu.add(manageAccess);
            popupMenu.addSeparator();
        }
        popupMenu.add(deleteDoc);

        optionsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                popupMenu.show(optionsLabel, e.getX(), e.getY());
            }
        });

        deleteDoc.addActionListener(event -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(documentsGrid);
            File fileToDelete = new File(doc.filePath);
            showDeleteConfirmation(parentWindow, doc.title, () -> {
                boolean fileDeleted = fileToDelete.delete();
                System.out.println("File deleted? " + fileDeleted);
                DocumentDAO.deleteDocumentById(doc.id);
                refreshDocumentsAsync();
            });
        });

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

    private void populateDocuments(List<File> filesToShow) {
        documentsGrid.removeAll();

        List<Doc> dummyDocs = DocumentDAO.getAllDocumentsFromDB();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        if (dummyDocs.isEmpty()) {
            JLabel emptyMsg = new JLabel("No documents found.");
            emptyMsg.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emptyMsg.setHorizontalAlignment(SwingConstants.CENTER);
            documentsGrid.add(emptyMsg);
        } else {
            for (File file : filesToShow) {
                String name = file.getName();
                String date = dateFormat.format(file.lastModified());

                Doc dummyDoc = new Doc(
                        UUID.randomUUID().toString(), name, "-", Document.currentUser,
                        new Date(), new Date(), new Date(), 1,
                        "UNKNOWN", 0, file.getAbsolutePath()
                );

                documentsGrid.add(createDocumentCard(dummyDoc, date));
            }
        }

        documentsGrid.revalidate();
        documentsGrid.repaint();
    }

    public void refreshDocuments() {
        loadDocumentsFromDatabase();
    }

    private void showDeleteConfirmation(Window parent, String fileName, Runnable onDelete) {
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
            System.out.println("Document deleted: " + fileName);
            onDelete.run();
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

    public void refreshDocumentsAsync() {
        SwingWorker<List<Doc>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Doc> doInBackground() {
                return DocumentDAO.getAllDocumentsFromDB();
            }

            @Override
            protected void done() {
                try {
                    List<Doc> all = get();
                    cachedDocs = all.stream()
                            .filter(doc -> Document.hasAccess(doc, Document.currentUser))
                            .collect(Collectors.toList());
                    loadDocumentsFromList(cachedDocs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    public void removeDocumentCard(String docId) {
        for (Component comp : documentsGrid.getComponents()) {
            if (comp instanceof JPanel panel) {
                Object tag = panel.getClientProperty("docId");
                if (tag != null && tag.equals(docId)) {
                    documentsGrid.remove(panel);
                    documentsGrid.revalidate();
                    documentsGrid.repaint();
                    return;
                }
            }
        }
    }

    private void loadDocumentsFromList(List<Doc> docs) {
        documentsGrid.removeAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        List<Doc> visibleDocs = docs.stream()
                .filter(doc -> Document.hasAccess(doc, Document.currentUser))
                .toList();
        if (visibleDocs.isEmpty()) {
            JLabel emptyMsg = new JLabel("No documents found.");
            emptyMsg.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emptyMsg.setHorizontalAlignment(SwingConstants.CENTER);
            documentsGrid.add(emptyMsg);
        } else {
            for (Doc doc : visibleDocs) {
                File file = new File(doc.filePath);
                String date = file.exists() ? dateFormat.format(file.lastModified()) : dateFormat.format(doc.modifiedDate);
                documentsGrid.add(createDocumentCard(doc, date));
            }
        }

        documentsGrid.revalidate();
        documentsGrid.repaint();
    }

    private void filterAndSearch() {
        SwingWorker<List<Doc>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Doc> doInBackground() {
                return getFilteredDocuments();
            }

            @Override
            protected void done() {
                try {
                    List<Doc> filtered = get();
                    documentsGrid.removeAll();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

                    for (Doc doc : filtered) {
                        File file = new File(doc.filePath);
                        String date = file.exists() ? dateFormat.format(file.lastModified()) : dateFormat.format(doc.modifiedDate);
                        documentsGrid.add(createDocumentCard(doc, date));
                    }

                    documentsGrid.revalidate();
                    documentsGrid.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private List<Doc> getFilteredDocuments() {
        String keyword = searchBar.getSearchText().toLowerCase();

        return cachedDocs.stream()
                .filter(doc -> {
                    boolean matchesSearch = doc.title.toLowerCase().contains(keyword);

                    String type = doc.fileType.toLowerCase();
                    boolean matchesType = true;
                    boolean anySelected = filterComponent.isPdfSelected() || filterComponent.isImageSelected()
                            || filterComponent.isDocxSelected() || filterComponent.isXlsxSelected();

                    if (anySelected) {
                        matchesType = (filterComponent.isPdfSelected() && type.equals("pdf")) ||
                                (filterComponent.isImageSelected() && (type.equals("jpg") || type.equals("jpeg") || type.equals("png"))) ||
                                (filterComponent.isDocxSelected() && type.equals("docx")) ||
                                (filterComponent.isXlsxSelected() && type.equals("xlsx"));
                    }

                    return matchesSearch && matchesType;
                }).toList();
    }
}
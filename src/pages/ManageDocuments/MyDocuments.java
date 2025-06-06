package pages.ManageDocuments;

import components.SearchBar;
import components.Filter;
import storage.DocumentStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyDocuments extends JPanel {
    private JPanel documentsGrid;
    private Color defaultBorderColor = new Color(200, 200, 200);
    private Color hoverBorderColor = new Color(90, 106, 207);
    private static final int FILTER_WIDTH = 250;

    public MyDocuments() {
        initializeComponents();
        setupLayout();
        loadDocumentsFromStorage();
    }

    private void initializeComponents() {
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 20, 20);
        documentsGrid = new JPanel(flowLayout);
        documentsGrid.setBackground(new Color(248, 249, 250));
        documentsGrid.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void setupLayout() {
        JPanel mainContainer = new JPanel(null);
        mainContainer.setBackground(new Color(248, 249, 250));

        // === Filter Panel ===
        Filter filterComponent = new Filter();
        filterComponent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        // === Documents Scroll Area ===
        JPanel documentsContainer = new JPanel(new BorderLayout());
        documentsContainer.setBackground(new Color(248, 249, 250));

        JScrollPane documentsScrollPane = new JScrollPane(documentsGrid);
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
            new UploadDocumentUI(() -> refreshDocuments());
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
        searchBarWrapper.add(new SearchBar(), BorderLayout.CENTER);

        topContainer.add(headerPanel);
        topContainer.add(Box.createVerticalStrut(20)); // spacing
        topContainer.add(searchBarWrapper);
        topContainer.add(Box.createVerticalStrut(10)); // spacing

        add(topContainer, BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);
    }

    private void loadDocumentsFromStorage() {
        documentsGrid.removeAll();

        List<File> uploaded = DocumentStorage.uploadedDocuments;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        if (uploaded.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                documentsGrid.add(createDocumentCard("K3C_DPPLOO0" + (i + 1), "15 May 2025"));
            }
        } else {
            for (File file : uploaded) {
                String name = file.getName();
                String date = dateFormat.format(file.lastModified());
                documentsGrid.add(createDocumentCard(name, date));
            }
        }

        documentsGrid.revalidate();
        documentsGrid.repaint();
    }

    private JPanel createDocumentCard(String titleText, String dateText) {
        JPanel docCard = new JPanel(new BorderLayout());
        docCard.setPreferredSize(new Dimension(180, 240));
        docCard.setBackground(Color.WHITE);
        docCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(defaultBorderColor),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));


        ImageIcon docImage = new ImageIcon("img/doc-thumb.png");
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setPreferredSize(new Dimension(160, 180));


        if (docImage.getImageLoadStatus() == MediaTracker.ERRORED) {
            JLabel docPlaceholder = new JLabel("📄");
            docPlaceholder.setFont(new Font("Arial", Font.PLAIN, 64));
            docPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
            imageContainer.add(docPlaceholder, BorderLayout.CENTER);
        } else {
            Image scaledDoc = docImage.getImage().getScaledInstance(140, 160, Image.SCALE_SMOOTH);
            JLabel docImageLabel = new JLabel(new ImageIcon(scaledDoc));
            docImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageContainer.add(docImageLabel, BorderLayout.CENTER);
        }


        imageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        docCard.add(imageContainer, BorderLayout.CENTER);


        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(Color.WHITE);


        JLabel docTitle = new JLabel(titleText);
        docTitle.setFont(new Font("Arial", Font.BOLD, 13));
        docTitle.setBorder(new EmptyBorder(5, 10, 0, 0));


        JLabel docDate = new JLabel(dateText);
        docDate.setFont(new Font("Arial", Font.PLAIN, 11));
        docDate.setBorder(new EmptyBorder(0, 10, 10, 0));


        labelPanel.add(docTitle);
        labelPanel.add(docDate);
        docCard.add(labelPanel, BorderLayout.SOUTH);


        docCard.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                docCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(hoverBorderColor, 2),
                        BorderFactory.createEmptyBorder(0, 0, 0, 0)
                ));
                docCard.setBackground(new Color(250, 250, 255));
                labelPanel.setBackground(new Color(250, 250, 255));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }


            public void mouseExited(MouseEvent e) {
                docCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(defaultBorderColor),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)
                ));
                docCard.setBackground(Color.WHITE);
                labelPanel.setBackground(Color.WHITE);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        return docCard;
    }

    private void populateDocuments() {
        documentsGrid.removeAll();

        List<File> uploaded = DocumentStorage.uploadedDocuments;

        if (uploaded.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                documentsGrid.add(createDocumentCard("K3C_DPPLOO0" + (i + 1), "15 May 2025"));
            }
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            for (File file : uploaded) {
                String name = file.getName();
                String date = dateFormat.format(file.lastModified());
                documentsGrid.add(createDocumentCard(name, date));
            }
        }

        documentsGrid.revalidate();
        documentsGrid.repaint();
    }

    public void refreshDocuments() {
        populateDocuments();
    }
}

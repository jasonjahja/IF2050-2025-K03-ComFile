package Dashboard;

import components.SearchBar;
import components.Filter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchDocuments extends JPanel {
    private JPanel documentsGrid;
    private static final int FILTER_WIDTH = 250;

    public SearchDocuments() {
        initializeComponents();
        setupLayout();
        loadSampleDocuments();
    }

    private void initializeComponents() {
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        // Documents grid
        documentsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        documentsGrid.setBackground(new Color(248, 249, 250));
        documentsGrid.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void setupLayout() {
        // Create the main container with null layout for absolute positioning
        JPanel mainContainer = new JPanel(null);
        mainContainer.setBackground(new Color(248, 249, 250));

        // Add the filter component with absolute positioning
        Filter filterComponent = new Filter();
        filterComponent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        // Create a container for the documents area
        JPanel documentsContainer = new JPanel(new BorderLayout());
        documentsContainer.setBackground(new Color(248, 249, 250));

        // Documents scroll pane
        JScrollPane documentsScrollPane = new JScrollPane(documentsGrid);
        documentsScrollPane.setBorder(null);
        documentsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        documentsScrollPane.setBackground(new Color(248, 249, 250));

        documentsContainer.add(documentsScrollPane, BorderLayout.CENTER);

        // Add components to the main container with absolute positioning
        mainContainer.add(filterComponent);
        mainContainer.add(documentsContainer);

        // Add a component listener to handle resizing
        mainContainer.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // Update filter component position and size with margin
                filterComponent.setBounds(20, 20, FILTER_WIDTH, filterComponent.getPreferredSize().height);
                
                // Update documents container position and size
                documentsContainer.setBounds(
                    FILTER_WIDTH + 40, 0,
                    mainContainer.getWidth() - (FILTER_WIDTH + 40),
                    mainContainer.getHeight()
                );
            }
        });

        // Add searchContainer and mainContainer to the panel
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBackground(new Color(248, 249, 250));
        searchContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        searchContainer.add(new SearchBar(), BorderLayout.CENTER);

        add(searchContainer, BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);
    }

    private void loadSampleDocuments() {
        String[] documentTitles = {
            "K3C_DPPLO002", "Document1", "Document2",
            "K3C_DPPLO002", "K3C_DPPLO002", "K3C_DPPLO002"
        };

        String[] documentDates = {
            "15 May 2025", "13 May 2025", "10 May 2025",
            "15 May 2025", "13 May 2025", "10 May 2025"
        };

        for (int i = 0; i < documentTitles.length; i++) {
            documentsGrid.add(createDocumentCard(documentTitles[i], documentDates[i]));
        }
    }

    private JPanel createDocumentCard(String title, String date) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));

        // Document preview area
        JPanel previewPanel = new JPanel();
        previewPanel.setBackground(new Color(250, 250, 250));
        previewPanel.setPreferredSize(new Dimension(0, 300));
        previewPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Sample text content
        JTextArea previewText = new JTextArea(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
            "nisi ut aliquip ex ea commodo consequat."
        );
        previewText.setEditable(false);
        previewText.setBackground(new Color(250, 250, 250));
        previewText.setFont(new Font("Arial", Font.PLAIN, 12));
        previewText.setLineWrap(true);
        previewText.setWrapStyleWord(true);
        previewText.setBorder(null);

        previewPanel.add(previewText);

        // Document info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);

        infoPanel.add(titleLabel, BorderLayout.NORTH);
        infoPanel.add(dateLabel, BorderLayout.SOUTH);

        card.add(previewPanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(63, 81, 181), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
            }
        });

        return card;
    }
}
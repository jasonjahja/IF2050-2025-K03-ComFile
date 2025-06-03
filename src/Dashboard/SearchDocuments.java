package Dashboard;

import components.SearchBar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchDocuments extends JPanel {
    private JPanel filterPanel;
    private JPanel documentsGrid;
    private JToggleButton pdfButton;
    private JToggleButton imageButton;
    private JToggleButton docxButton;
    private JToggleButton xlsxButton;
    private JComboBox<String> lastModifiedCombo;

    public SearchDocuments() {
        initializeComponents();
        setupLayout();
        loadSampleDocuments();
    }

    private void initializeComponents() {
        setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout());

        // Search container with SearchBar component
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBackground(new Color(248, 249, 250));
        searchContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        searchContainer.add(new SearchBar(), BorderLayout.CENTER);

        // Filter panel
        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        filterPanel.setPreferredSize(new Dimension(250, 0));

        // File Type section
        JLabel fileTypeLabel = new JLabel("File Type");
        fileTypeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel fileTypeButtons = new JPanel(new GridLayout(2, 2, 10, 8));
        fileTypeButtons.setBackground(Color.WHITE);
        
        pdfButton = createFilterButton("PDF");
        imageButton = createFilterButton("Image");
        docxButton = createFilterButton("Docx");
        xlsxButton = createFilterButton("XLSX");
        
        fileTypeButtons.add(pdfButton);
        fileTypeButtons.add(imageButton);
        fileTypeButtons.add(docxButton);
        fileTypeButtons.add(xlsxButton);

        // Last Modified section
        JLabel lastModifiedLabel = new JLabel("Last Modified");
        lastModifiedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        lastModifiedCombo = new JComboBox<>(new String[]{"Today", "Yesterday", "Last 7 days", "Last 30 days"});
        lastModifiedCombo.setBackground(Color.WHITE);
        lastModifiedCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        lastModifiedCombo.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        // Custom renderer to reduce height
        DefaultListCellRenderer customRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        };
        lastModifiedCombo.setRenderer(customRenderer);
        
        // Wrap combo box in a panel to prevent filling
        JPanel comboWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        comboWrapper.setBackground(Color.WHITE);
        comboWrapper.add(lastModifiedCombo);

        // Filter buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.PLAIN, 14));
        resetButton.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        resetButton.setFocusPainted(false);
        resetButton.setMargin(new Insets(2, 12, 2, 12));
        
        JButton applyButton = new JButton("Apply");
        applyButton.setBackground(new Color(63, 81, 181));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFont(new Font("Arial", Font.PLAIN, 14));
        applyButton.setBorderPainted(false);
        applyButton.setFocusPainted(false);
        applyButton.setMargin(new Insets(2, 12, 2, 12));
        
        buttonPanel.add(resetButton);
        buttonPanel.add(applyButton);

        // Add components to filter panel with reduced spacing
        filterPanel.add(fileTypeLabel);
        filterPanel.add(Box.createVerticalStrut(8));
        filterPanel.add(fileTypeButtons);
        filterPanel.add(Box.createVerticalStrut(20));
        filterPanel.add(lastModifiedLabel);
        filterPanel.add(Box.createVerticalStrut(8));
        filterPanel.add(comboWrapper);
        filterPanel.add(Box.createVerticalStrut(20));
        filterPanel.add(buttonPanel);

        // Documents grid
        documentsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        documentsGrid.setBackground(new Color(248, 249, 250));
        documentsGrid.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private JToggleButton createFilterButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 14));  // Set fixed height
        
        button.addActionListener(e -> {
            if (button.isSelected()) {
                button.setBackground(new Color(63, 81, 181));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }
        });
        
        return button;
    }

    private void setupLayout() {
        // Main content panel
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(248, 249, 250));

        // Documents scroll pane
        JScrollPane documentsScrollPane = new JScrollPane(documentsGrid);
        documentsScrollPane.setBorder(null);
        documentsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        documentsScrollPane.setBackground(new Color(248, 249, 250));

        mainContent.add(filterPanel, BorderLayout.WEST);
        mainContent.add(documentsScrollPane, BorderLayout.CENTER);

        // Add searchContainer and mainContent to the panel
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBackground(new Color(248, 249, 250));
        searchContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        searchContainer.add(new SearchBar(), BorderLayout.CENTER);

        add(searchContainer, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
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
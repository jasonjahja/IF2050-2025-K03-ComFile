package components;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchBar extends JPanel {
    private final String PLACEHOLDER = "Search document";
    private JTextField searchField;
    public JTextField getSearchField() {
        return searchField;
    }

    private ImageIcon createImageIcon(String path) {
        try {
            String projectRoot = System.getProperty("user.dir");
            String imgPath = projectRoot + "/img/" + path;
            return new ImageIcon(imgPath);
        } catch (Exception e) {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public SearchBar() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(new RoundedBorder(30, new Color(230, 230, 240)));

        // Panel horizontal dengan BoxLayout agar bisa align vertikal
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setOpaque(false);
        innerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Search icon
        ImageIcon searchIcon = createImageIcon("icon-search.png");
        if (searchIcon != null) {
            Image scaledSearch = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel searchLabel = new JLabel(new ImageIcon(scaledSearch));
            searchLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
            innerPanel.add(searchLabel);
            innerPanel.add(Box.createRigidArea(new Dimension(10, 0))); // spacing
        }

        // Text field
        searchField = new JTextField(25);
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        searchField.setAlignmentY(Component.CENTER_ALIGNMENT);
        searchField.setBorder(null);
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(new Color(130, 130, 140));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setText(PLACEHOLDER);

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(PLACEHOLDER)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(new Color(130, 130, 140));
                    searchField.setText(PLACEHOLDER);
                }
            }
        });

        innerPanel.add(searchField);
        add(innerPanel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(500, 40));
    }

    public String getSearchText() {
        String text = searchField.getText();
        return text.equals(PLACEHOLDER) ? "" : text;
    }

    // Border bulat custom
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color borderColor;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.borderColor = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 8, 8, 8);
        }
    }

    // Untuk testing mandiri
    public static void main(String[] args) {
        JFrame frame = new JFrame("Search Bar Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.add(new SearchBar());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

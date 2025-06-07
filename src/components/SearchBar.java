package components;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;

public class SearchBar extends JPanel {
    private final String PLACEHOLDER = "Search document";
    private JTextField searchField;

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
        setBorder(new RoundedBorder(30, new Color(230, 230, 240))); // Light purple border

        // Text field with placeholder
        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(new Color(130, 130, 140)); // Light gray
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setText(PLACEHOLDER);

        // Add placeholder behavior
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

        // Search icon
        ImageIcon searchIcon = createImageIcon("icon-search.png");
        if (searchIcon != null) {
            Image scaledSearch = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel searchLabel = new JLabel(new ImageIcon(scaledSearch));
            searchLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            add(searchLabel, BorderLayout.WEST);
        }

        // Tambahkan ke panel
        JPanel leftPadding = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPadding.setBackground(Color.WHITE);
        
        add(leftPadding, BorderLayout.WEST);
        add(searchField, BorderLayout.CENTER);

        setPreferredSize(new Dimension(500, 40));
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
            
            // Draw background first
            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
            
            // Then draw border
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 4, 4, 4);
        }
    }

    // Untuk testing
    public static void main(String[] args) {
        JFrame frame = new JFrame("Search Bar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new SearchBar());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

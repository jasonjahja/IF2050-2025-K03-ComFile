import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class MyDocuments extends JFrame {
    private Color defaultBorderColor = new Color(200, 200, 200);
    private Color hoverBorderColor = new Color(90, 106, 207);
    
    public MyDocuments() {
        setTitle("My Documents");
        setSize(1080, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Main Panel =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ===== Header (Title + Add Button) =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 40, 10, 40));

        JLabel title = new JLabel("My Documents");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton addButton = new JButton("+ Add Document");
        addButton.setBackground(new Color(90, 106, 207));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setOpaque(true);
        addButton.setContentAreaFilled(true);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        addButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Hover effect for add button
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

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // ===== Documents Grid =====
        JPanel documentPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        documentPanel.setBackground(Color.WHITE);
        documentPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        for (int i = 0; i < 8; i++) {
            JPanel docCard = new JPanel();
            docCard.setLayout(new BorderLayout());
            docCard.setPreferredSize(new Dimension(240, 300));
            docCard.setBackground(Color.WHITE);
            docCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(defaultBorderColor),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
            ));

            // Document Image
            ImageIcon docImage = new ImageIcon("img/doc-thumb.png");
            JPanel imageContainer = new JPanel(new BorderLayout());
            imageContainer.setBackground(Color.WHITE);
            imageContainer.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
            imageContainer.setPreferredSize(new Dimension(200, 240));
            
            if (docImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                JLabel docPlaceholder = new JLabel("📄");
                docPlaceholder.setFont(new Font("Arial", Font.PLAIN, 72));
                docPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
                imageContainer.add(docPlaceholder, BorderLayout.CENTER);
            } else {
                Image scaledDoc = docImage.getImage().getScaledInstance(180, 220, Image.SCALE_SMOOTH);
                JLabel docImageLabel = new JLabel(new ImageIcon(scaledDoc));
                docImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageContainer.add(docImageLabel, BorderLayout.CENTER);
            }
            
            imageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            docCard.add(imageContainer, BorderLayout.CENTER);

            // Title + Date
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
            labelPanel.setBackground(Color.WHITE);
            
            JLabel docTitle = new JLabel("K3C_DPPLOO0" + (i + 1));
            docTitle.setFont(new Font("Arial", Font.BOLD, 14));
            docTitle.setBorder(new EmptyBorder(5, 10, 0, 0));

            JLabel docDate = new JLabel("15 May 2025");
            docDate.setFont(new Font("Arial", Font.PLAIN, 12));
            docDate.setBorder(new EmptyBorder(0, 10, 10, 0));

            labelPanel.add(docTitle);
            labelPanel.add(docDate);
            docCard.add(labelPanel, BorderLayout.SOUTH);

            // Add hover effect
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

            documentPanel.add(docCard);
        }

        // ===== Add to Main Panel =====
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(documentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyDocuments::new);
    }
}
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyDocuments extends JFrame {
    private Color defaultBorderColor = new Color(200, 200, 200);
    private Color hoverBorderColor = new Color(90, 106, 207);

    private JPanel documentPanel;

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

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UploadDocumentUI();
            }
        });

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // ===== Documents Grid Panel (FlowLayout for wrap) =====
        documentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        documentPanel.setBackground(Color.WHITE);
        documentPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        populateDocuments();  // Memuat isi dokumen dari DocumentStorage

        // ===== Main Layout =====
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(documentPanel), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        // Auto refresh saat window aktif
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                refreshDocuments();
            }
        });
    }

    private void populateDocuments() {
        documentPanel.removeAll();

        List<File> uploaded = DocumentStorage.uploadedDocuments;

        if (uploaded.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                documentPanel.add(createDummyCard("K3C_DPPLOO0" + (i + 1), "15 May 2025"));
            }
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            for (File file : uploaded) {
                String name = file.getName();
                String date = dateFormat.format(file.lastModified());
                documentPanel.add(createDummyCard(name, date));
            }
        }

        documentPanel.revalidate();
        documentPanel.repaint();
    }

    private JPanel createDummyCard(String titleText, String dateText) {
        JPanel docCard = new JPanel(new BorderLayout());
        docCard.setPreferredSize(new Dimension(180, 240));  // ⬅️ lebih kecil
        docCard.setBackground(Color.WHITE);
        docCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(defaultBorderColor),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
        ));

        // Image
        ImageIcon docImage = new ImageIcon("img/doc-thumb.png");
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setPreferredSize(new Dimension(160, 180));  // ⬅️ smaller preview

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

        // Label Panel
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

        // Hover effect
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

    public void refreshDocuments() {
        populateDocuments();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyDocuments();
            }
        });
    }
}

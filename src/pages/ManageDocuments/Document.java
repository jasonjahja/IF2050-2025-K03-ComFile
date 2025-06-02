import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Document {
    public static void main(String[] args) {
        JFrame frame = new JFrame("File Name");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        Color bgSoftLavender = new Color(245, 243, 247);
        Color textNormal = new Color(55, 40, 80);
        Color textHover = new Color(90, 70, 120);

        frame.getContentPane().setBackground(bgSoftLavender);

        // ========== Header ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 218, 230)));

        ImageIcon backIcon = new ImageIcon("src/assets/icon-back.png");
        Image scaledBack = backIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel backLabel = new JLabel(" File Name", new ImageIcon(scaledBack), JLabel.LEFT);
        backLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        backLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.setForeground(textNormal);
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setForeground(textHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setForeground(textNormal);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Back clicked");
            }
        });
        header.add(backLabel, BorderLayout.WEST);

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 10));
        iconPanel.setOpaque(false);
        iconPanel.add(createIconWithText("src/assets/icon-sharing.png", "Sharing", textNormal, textHover));
        iconPanel.add(createIconWithText("src/assets/icon-backup.png", "Backup", textNormal, textHover));
        iconPanel.add(createIconWithText("src/assets/icon-delete.png", "Delete", textNormal, textHover));
        header.add(iconPanel, BorderLayout.EAST);

        frame.add(header, BorderLayout.NORTH);

        // ========== Dokumen A4 ==========
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(bgSoftLavender);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 60, 0));

        JPanel docPanel = new JPanel();
        docPanel.setPreferredSize(new Dimension(794, 1123));
        docPanel.setBackground(Color.WHITE);
        docPanel.setLayout(new BorderLayout());
        docPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JTextArea textArea = new JTextArea("Ensure your data is safe and recoverable. ".repeat(100));
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.getCaret().setVisible(false);

        JScrollPane docScrollPane = new JScrollPane(textArea);
        docScrollPane.setBorder(null);
        docScrollPane.setOpaque(false);
        docScrollPane.getViewport().setOpaque(false);

        docPanel.add(docScrollPane, BorderLayout.CENTER);
        outerPanel.add(docPanel);

        JScrollPane scrollFrame = new JScrollPane(outerPanel);
        scrollFrame.setBorder(null);
        scrollFrame.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollFrame, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // ========== Fungsi Buat Tombol ==========
    private static JPanel createIconWithText(String iconPath, String labelText, Color textNormal, Color textHover) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon rawIcon = new ImageIcon(iconPath);
        Image scaledImg = rawIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImg));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLabel.setForeground(textNormal);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(textLabel);

        // Efek hover
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                textLabel.setForeground(textHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                textLabel.setForeground(textNormal);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(labelText + " clicked");
            }
        });

        return panel;
    }
}
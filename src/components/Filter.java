package components;

import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;

public class Filter extends JPanel {

    private JCheckBox pdfCheckBox, imageCheckBox, docxCheckBox, xlsxCheckBox;
    private JComboBox<String> lastModifiedComboBox;
    private JButton resetButton, applyButton;
    public boolean isPdfSelected() { return pdfCheckBox.isSelected(); }
    public boolean isImageSelected() { return imageCheckBox.isSelected(); }
    public boolean isDocxSelected() { return docxCheckBox.isSelected(); }
    public boolean isXlsxSelected() { return xlsxCheckBox.isSelected(); }
    private Runnable onResetAction;

    public void setOnResetAction(Runnable r) {
        this.onResetAction = r;
    }

    public String getLastModifiedFilter() {
        return (String) lastModifiedComboBox.getSelectedItem();
    }

    public Filter() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // File Type Section
        JLabel fileTypeLabel = new JLabel("File Type");
        fileTypeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        fileTypeLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(fileTypeLabel);
        add(Box.createVerticalStrut(16));

        // Grid for checkboxes with proper alignment
        JPanel checkBoxPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        checkBoxPanel.setBackground(Color.WHITE);
        checkBoxPanel.setAlignmentX(LEFT_ALIGNMENT);
        // Remove any extra margins
        checkBoxPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        pdfCheckBox = createStyledCheckBox("PDF");
        imageCheckBox = createStyledCheckBox("Image");
        docxCheckBox = createStyledCheckBox("Docx");
        xlsxCheckBox = createStyledCheckBox("XLSX");

        checkBoxPanel.add(pdfCheckBox);
        checkBoxPanel.add(imageCheckBox);
        checkBoxPanel.add(docxCheckBox);
        checkBoxPanel.add(xlsxCheckBox);
        add(checkBoxPanel);

        add(Box.createVerticalStrut(20));
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        add(separator);
        add(Box.createVerticalStrut(20));

        // Last Modified Section
        JLabel lastModifiedLabel = new JLabel("Last Modified");
        lastModifiedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        lastModifiedLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(lastModifiedLabel);
        add(Box.createVerticalStrut(16));

        // Combo box panel
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        comboBoxPanel.setBackground(Color.WHITE);
        comboBoxPanel.setAlignmentX(LEFT_ALIGNMENT);

        String[] dateOptions = {"Today", "This Week", "This Month", "This Year"};
        lastModifiedComboBox = new JComboBox<>(dateOptions);
        lastModifiedComboBox.setPreferredSize(new Dimension(200, 30));
        lastModifiedComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lastModifiedComboBox.setBackground(Color.WHITE);
        lastModifiedComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        lastModifiedComboBox.setFocusable(false);


        comboBoxPanel.add(lastModifiedComboBox);
        add(comboBoxPanel);

        add(Box.createVerticalStrut(104));

        // Button panel with proper alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        resetButton = createStyledButton("Reset", false);
        applyButton = createStyledButton("Apply", true);

        applyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color normalColor = Color.decode("#5A6ACF");
            final Color hoverColor = new Color(70, 86, 187);
            final Color pressedColor = new Color(55, 70, 150);

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                applyButton.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                applyButton.setBackground(normalColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                applyButton.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                applyButton.setBackground(hoverColor);
            }
        });

        resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        applyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createHorizontalStrut(8));
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createHorizontalGlue());
        add(buttonPanel);

        // Event Listeners
        resetButton.addActionListener(e -> resetFilters());
        applyButton.addActionListener(e -> applyFilters());
    }

    private JCheckBox createStyledCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
                setBackground(Color.WHITE);
            }
        };
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkBox.setFocusPainted(false);
        checkBox.setContentAreaFilled(false);
        checkBox.setBorderPainted(false);
        checkBox.setIcon(new CustomCheckBoxIcon());
        checkBox.setIconTextGap(8);
        checkBox.setHorizontalAlignment(SwingConstants.LEFT);
        checkBox.setHorizontalTextPosition(SwingConstants.RIGHT);
        checkBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return checkBox;
    }


    class CustomCheckBoxIcon implements Icon {
        private final int size = 16;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            JCheckBox cb = (JCheckBox) c;
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Border
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(x, y, size, size, 6, 6);

            // Checkmark
            if (cb.isSelected()) {
                g2.setColor(new Color(100, 80, 200));
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(x + 4, y + 8, x + 7, y + 11);
                g2.drawLine(x + 7, y + 11, x + 12, y + 4);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }


    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        if (isPrimary) {
            button.setBackground(new Color(63, 81, 181));
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
        } else {
            button.setBackground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(6, 16, 6, 16)
            ));
        }
        button.setFocusPainted(false);
        if (isPrimary) {
            button.setMargin(new Insets(6, 16, 6, 16));
        }
        button.setPreferredSize(new Dimension(100, 32));
        button.setMaximumSize(new Dimension(100, 32));
        button.setMinimumSize(new Dimension(100, 32));
        return button;
    }

    private void resetFilters() {
        pdfCheckBox.setSelected(false);
        imageCheckBox.setSelected(false);
        docxCheckBox.setSelected(false);
        xlsxCheckBox.setSelected(false);
        lastModifiedComboBox.setSelectedIndex(0);

        if (onResetAction != null) {
            onResetAction.run();
        }
    }

    private void applyFilters() {
        // This method can be enhanced to return filter values or trigger events
        System.out.println("Applied Filters:");
        if (pdfCheckBox.isSelected()) System.out.println("- PDF");
        if (imageCheckBox.isSelected()) System.out.println("- Image");
        if (docxCheckBox.isSelected()) System.out.println("- Docx");
        if (xlsxCheckBox.isSelected()) System.out.println("- XLSX");
        System.out.println("Last Modified: " + lastModifiedComboBox.getSelectedItem());
    }

    public void setApplyAction(Runnable action) {
        applyButton.addActionListener(e -> action.run());
    }
}
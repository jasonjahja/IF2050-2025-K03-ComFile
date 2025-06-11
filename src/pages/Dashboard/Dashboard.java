package pages.Dashboard;


import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentEvent;
import components.NavigationBar;
import components.NavigationBar.NavigationListener;
import pages.ManageDocuments.MyDocuments;
import pages.Login;
import main.MainApplication;

public class Dashboard extends JPanel implements NavigationListener {
   private NavigationBar navigationBar;
   private JPanel searchPanel;
   private JLabel searchIcon;
   private String username;
   private String userRole;


   public Dashboard(String username, String userRole) {
       this.username = username;
       this.userRole = userRole;
      
       //setTitle("ComFile - Dashboard");
       //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //setSize(1000, 600);
       //setLocationRelativeTo(null);
      
       // Initialize components
       navigationBar = new NavigationBar();
       navigationBar.setNavigationListener(this);
       navigationBar.setUserInfo(username, userRole);
      
       // Create main content panel with padding
       JPanel contentPanel = new JPanel();
       contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
       contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
       contentPanel.setBackground(Color.WHITE);
      
       // Add welcome text
       JLabel welcomeLabel = new JLabel("Welcome to ComFile!");
       welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
       welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
       contentPanel.add(welcomeLabel);
      
       // Add some vertical spacing
       contentPanel.add(Box.createVerticalStrut(20));
      
       // Create search panel
       searchPanel = new JPanel(new BorderLayout());
       searchPanel.setBackground(Color.WHITE);
       searchPanel.setBorder(new RoundedBorder(20, new Color(230, 230, 240)));
       searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
       searchPanel.setPreferredSize(new Dimension(contentPanel.getWidth(), 40));
       searchPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));


       // Add placeholder text
       JLabel placeholderLabel = new JLabel("Search document");
       placeholderLabel.setForeground(new Color(130, 130, 140));
       placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
       placeholderLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
       searchPanel.add(placeholderLabel, BorderLayout.CENTER);


       // Add search icon
       searchIcon = new JLabel(new ImageIcon("img/icon-search.png"));
       searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
       searchPanel.add(searchIcon, BorderLayout.EAST);


       // Add click listener
       searchPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            MainApplication.getInstance().onDocumentsClicked();
        }
        });
    


       // Add search panel directly to content panel
       JPanel searchWrapper = new JPanel(new BorderLayout());
       searchWrapper.setBackground(Color.WHITE);
       searchWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
       searchWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
       searchWrapper.add(searchPanel, BorderLayout.CENTER);
       contentPanel.add(searchWrapper);
       contentPanel.add(Box.createVerticalStrut(30));


       // NOTIFICATION LABEL
       JLabel notifLabel = new JLabel("Important Notification");
       notifLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
       notifLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
       contentPanel.add(notifLabel);
       contentPanel.add(Box.createVerticalStrut(12));


       // NOTIFICATION CARD
       JPanel notifCard = new JPanel(new BorderLayout());
       notifCard.setBackground(new Color(246, 244, 249));
       notifCard.setBorder(new EmptyBorder(20, 20, 20, 20));
       notifCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
       notifCard.setAlignmentX(Component.LEFT_ALIGNMENT);


       // Circle icon with bell
       JPanel circleIcon = new JPanel() {
           @Override
           protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               g.setColor(Color.decode("#5A6ACF"));
               int size = Math.min(getWidth(), getHeight());
               g.fillOval((getWidth() - size) / 2, (getHeight() - size) / 2, size, size);
           }
       };
       circleIcon.setPreferredSize(new Dimension(48, 48));
       circleIcon.setMaximumSize(new Dimension(48, 48));
       circleIcon.setOpaque(false);
       circleIcon.setLayout(new GridBagLayout());


       JLabel bell = new JLabel(new ImageIcon("img/notification.png"));
       circleIcon.add(bell);
       notifCard.add(circleIcon, BorderLayout.WEST);


       // Notification text
       JLabel notifText = new JLabel("<html><b>Reminder:</b> 'Document1' will expire in 7 days.<br>" +
               "Please review or back up this document if necessary before 21 May 2025.<br>" +
               "<font size='2'>14 May 2025</font></html>");
       notifText.setFont(new Font("SansSerif", Font.PLAIN, 14));
       notifText.setBorder(new EmptyBorder(0, 15, 0, 0));
       notifCard.add(notifText, BorderLayout.CENTER);


       // Star icon
       JLabel star = new JLabel(new ImageIcon("img/Star.png"));
       notifCard.add(star, BorderLayout.EAST);


       contentPanel.add(notifCard);
       contentPanel.add(Box.createVerticalStrut(30));


       // SUGGESTED FILES TITLE
       // Header with "See More" link
        JPanel suggestedHeader = new JPanel(new BorderLayout());
        suggestedHeader.setBackground(Color.WHITE);
        suggestedHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        suggestedHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel suggestedTitle = new JLabel("Suggested Files");
        suggestedTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        suggestedHeader.add(suggestedTitle, BorderLayout.WEST);

        JLabel seeMore = new JLabel("See More →");
        seeMore.setFont(new Font("SansSerif", Font.PLAIN, 14));
        seeMore.setForeground(new Color(90, 106, 207));
        seeMore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        seeMore.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainApplication.getInstance().onDocumentsClicked(); // Redirect ke documents
            }
        });
        suggestedHeader.add(seeMore, BorderLayout.EAST);

        contentPanel.add(suggestedHeader);
        contentPanel.add(Box.createVerticalStrut(16));



       // CUSTOM TABLE PANEL
       JPanel tablePanel = new JPanel();
       tablePanel.setLayout(new GridLayout(0, 1));
       tablePanel.setBackground(Color.WHITE);
       tablePanel.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
               new EmptyBorder(0, 0, 0, 0)
       ));
       tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
       tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); // Set maximum height


       String[] headers = {"File Name", "Last Activity", "Owner"};
       Object[][] data = {
               {"K3C_DPPLOO002", "You created • 5:00 PM", "me"},
               {"K3C_SKPLOO", "You opened • 10 May", "michaelscott"},
               {"K3C_FormAsistensi3", "lanasteiner edited • 6:02 AM", "me"}
       };


       // Header
       JPanel headerRow = new JPanel(new GridLayout(1, 3));
       headerRow.setBackground(new Color(245, 245, 245));
       for (String header : headers) {
           JLabel label = new JLabel(header);
           label.setFont(new Font("SansSerif", Font.BOLD, 13));
           label.setBorder(new EmptyBorder(6, 20, 6, 0));
           headerRow.add(label);
       }
       tablePanel.add(headerRow);


       // Rows
       for (Object[] row : data) {
           JPanel rowPanel = new JPanel(new GridLayout(1, 3));
           rowPanel.setBackground(Color.WHITE);
           rowPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(240, 240, 240)));


           for (Object cell : row) {
               JLabel cellLabel = new JLabel(cell.toString());
               cellLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
               cellLabel.setBorder(new EmptyBorder(6, 20, 6, 0));
               rowPanel.add(cellLabel);
           }


           tablePanel.add(rowPanel);
       }


       contentPanel.add(tablePanel);


       // Add components to frame
       setLayout(new BorderLayout());
       // add(navigationBar, BorderLayout.NORTH);
      
       // Wrap content panel in a scroll pane
       JScrollPane scrollPane = new JScrollPane(contentPanel);
       scrollPane.setBorder(null);
       scrollPane.getVerticalScrollBar().setUnitIncrement(16);
       add(scrollPane, BorderLayout.CENTER);


       setVisible(true);
   }


   // Custom rounded border for search panel
   static class RoundedBorder extends AbstractBorder {
       private final int radius;
       private final Color borderColor;


       public RoundedBorder(int radius, Color color) {
           this.radius = radius;
           this.borderColor = color;
       }


       @Override
       public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
           Graphics2D g2d = (Graphics2D) g.create();
           g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          
           // Draw background
           g2d.setColor(Color.WHITE);
           g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
          
           // Draw border
           g2d.setColor(borderColor);
           g2d.setStroke(new BasicStroke(1));
           g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
          
           g2d.dispose();
       }


       @Override
       public Insets getBorderInsets(Component c) {
           return new Insets(4, 4, 4, 4);
       }
   }


   // NavigationListener methods
   @Override
   public void onHomeClicked() {
       // Already in home, do nothing
   }


   @Override
    public void onDocumentsClicked() {
        MainApplication.getInstance().onDocumentsClicked();
    }





   @Override
   public void onBackupClicked() {




   }


   @Override
   public void onNotificationClicked() {
       System.out.println("Notification clicked");
   }


   @Override
    public void onLogoutClicked() {
        MainApplication.getInstance().onLogoutClicked();
    }


}

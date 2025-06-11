package main;

import components.NavigationBar;
import pages.ManageDocuments.MyDocuments;
import pages.Login;
import pages.Dashboard.Dashboard;
import pages.Admin.AdminDashboard;

import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame implements NavigationBar.NavigationListener {
 private NavigationBar navigationBar;
 private JPanel contentPanel;
 private CardLayout cardLayout;
 private MyDocuments documentsPage;


 private static MainApplication instance;


 public MainApplication(String username, String role) {
     instance = this;
     initializeApplication();
     createPages(username, role);
     setupLayout();
     setVisible(true);
 }


 private void initializeApplication() {
     setTitle("ComFile - Document Management System");
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setSize(1400, 900);
     setLocationRelativeTo(null);


     try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
     } catch (Exception e) {
         e.printStackTrace();
     }


     navigationBar = new NavigationBar();
     navigationBar.setNavigationListener(this);

     cardLayout = new CardLayout();
     contentPanel = new JPanel(cardLayout);
 }

 private void createPages(String username, String role) {
  navigationBar.setUserInfo(username, role);

  java.awt.Container parentContainer = contentPanel;


  if (role.equalsIgnoreCase("Admin")) {
      AdminDashboard adminDashboard = new AdminDashboard(username, role, parentContainer);
      contentPanel.add(adminDashboard, "ADMIN_DASHBOARD");
      cardLayout.show(contentPanel, "ADMIN_DASHBOARD");
  } else {
      Dashboard dashboard = new Dashboard(username, role);
      contentPanel.add(dashboard, "HOME");
      cardLayout.show(contentPanel, "HOME");
  }

  JPanel documentsPage = new MyDocuments();
  JPanel backupPage = createPlaceholderPage("Backup Page", "Backup and restore your documents");

  contentPanel.add(documentsPage, "DOCUMENTS");
  contentPanel.add(backupPage, "BACKUP");
   
  }


 private JPanel createPlaceholderPage(String title, String description) {
     JPanel page = new JPanel(new BorderLayout());
     page.setBackground(new Color(248, 249, 250));


     JPanel centerPanel = new JPanel(new GridBagLayout());
     centerPanel.setBackground(new Color(248, 249, 250));


     JPanel contentBox = new JPanel();
     contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
     contentBox.setBackground(Color.WHITE);
     contentBox.setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(new Color(220, 220, 220)),
             BorderFactory.createEmptyBorder(50, 50, 50, 50)
     ));

     JLabel titleLabel = new JLabel(title);
     titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
     titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

     JLabel descLabel = new JLabel(description);
     descLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
     descLabel.setForeground(Color.GRAY);
     descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

     contentBox.add(titleLabel);
     contentBox.add(Box.createVerticalStrut(10));
     contentBox.add(descLabel);

     centerPanel.add(contentBox);
     page.add(centerPanel, BorderLayout.CENTER);

     return page;
 }

 private void setupLayout() {
     setLayout(new BorderLayout());
     add(navigationBar, BorderLayout.NORTH);
     add(contentPanel, BorderLayout.CENTER);
 }

 // ========== Navigation Listener ==========

 @Override
 public void onHomeClicked() {
     cardLayout.show(contentPanel, "HOME");
 }


 @Override
 public void onDocumentsClicked() {
     if (documentsPage != null) {
         documentsPage.refreshDocuments();
     }
     cardLayout.show(contentPanel, "DOCUMENTS");
 }

 @Override
 public void onBackupClicked() {

  }


 @Override
 public void onNotificationClicked() {
   
 }


 @Override
 public void onLogoutClicked() {
     int result = JOptionPane.showConfirmDialog(this,
             "Are you sure you want to logout?",
             "Confirm Logout",
             JOptionPane.YES_NO_OPTION);

     if (result == JOptionPane.YES_OPTION) {
         dispose();
         JFrame frame = new JFrame("ComFile Login");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(1080, 720);
         frame.setLocationRelativeTo(null);
         frame.setContentPane(new Login(null));
         frame.setVisible(true);
         }
 }

 public static void startWithUser(String username, String role) {
     SwingUtilities.invokeLater(() -> new MainApplication(username, role));
 }

 // ========== Main Awal: Login page ==========
 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> {
         JFrame frame = new JFrame("ComFile Login");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(1080, 720);
         frame.setLocationRelativeTo(null);
         frame.setContentPane(new Login(frame));
         frame.setVisible(true);
     });
 }


 public static MainApplication getInstance() {
     return instance;
 }
}


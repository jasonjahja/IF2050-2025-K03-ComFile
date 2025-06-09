package utils;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.Image;

public class UserDAO {
    
    public static class User {
        public String username;
        public String password;
        public String role;
        public String fullName;
        public String department;
        public String status;
        public String avatarPath;
        
        public User(String username, String password, String role, String fullName, 
                   String department, String status, String avatarPath) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.fullName = fullName;
            this.department = department;
            this.status = status;
            this.avatarPath = avatarPath;
        }
        
        // Constructor without avatar for backward compatibility
        public User(String username, String password, String role, String fullName, 
                   String department, String status) {
            this(username, password, role, fullName, department, status, null);
        }
    }
    
    // Get all users from database with pagination
    public static List<User> getAllUsers(int page, int limit) {
        List<User> users = new ArrayList<>();
        int offset = (page - 1) * limit;
        
        String query = "SELECT * FROM users ORDER BY username LIMIT ? OFFSET ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                User user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("full_name"),
                    rs.getString("department"),
                    rs.getString("status"),
                    rs.getString("avatar_path")
                );
                users.add(user);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error fetching users from database");
            e.printStackTrace();
        }
        
        return users;
    }
    
    // Get total users count for pagination
    public static int getTotalUsersCount() {
        String query = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error getting users count");
            e.printStackTrace();
        }
        
        return 0;
    }
    
    // Get all users from database (no pagination)
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        
        String query = "SELECT * FROM users ORDER BY username";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("full_name"),
                    rs.getString("department"),
                    rs.getString("status"),
                    rs.getString("avatar_path")
                );
                users.add(user);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error fetching all users from database");
            e.printStackTrace();
        }
        
        return users;
    }
    
    // Get users for dashboard (limited to 5)
    public static List<User> getUsersForDashboard() {
        return getAllUsers(1, 5);
    }
    
    // Add new user
    public static boolean addUser(String fullName, String username, String password, 
                                String role, String department, String status) {
        return addUser(fullName, username, password, role, department, status, null);
    }
    
    // Add new user with avatar
    public static boolean addUser(String fullName, String username, String password, 
                                String role, String department, String status, String avatarPath) {
        String query = "INSERT INTO users (username, password, role, full_name, department, status, avatar_path) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setString(4, fullName);
            stmt.setString(5, department);
            stmt.setString(6, status);
            stmt.setString(7, avatarPath);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("❌ Error adding user to database");
            e.printStackTrace();
            return false;
        }
    }
    
    // Update existing user
    public static boolean updateUser(String originalUsername, String fullName, String username, String password,
                                   String role, String department, String status) {
        return updateUser(originalUsername, fullName, username, password, role, department, status, null);
    }
    
    // Update existing user with avatar
    public static boolean updateUser(String originalUsername, String fullName, String username, String password,
                                   String role, String department, String status, String avatarPath) {
        String query = "UPDATE users SET full_name = ?, username = ?, password = ?, role = ?, " +
                      "department = ?, status = ?, avatar_path = ? WHERE username = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, fullName);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.setString(5, department);
            stmt.setString(6, status);
            stmt.setString(7, avatarPath);
            stmt.setString(8, originalUsername);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("❌ Error updating user in database");
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete user
    public static boolean deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting user from database");
            e.printStackTrace();
            return false;
        }
    }
    
    // Get user by username for edit
    public static User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("full_name"),
                    rs.getString("department"),
                    rs.getString("status"),
                    rs.getString("avatar_path")
                );
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error fetching user by username");
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Check if username exists
    public static boolean usernameExists(String username) {
        String query = "SELECT 1 FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (Exception e) {
            System.err.println("❌ Error checking username existence");
            e.printStackTrace();
            return false;
        }
    }
    
    // Profile picture utility methods
    public static String saveProfilePicture(java.io.File sourceFile, String username) {
        try {
            // Create avatars directory if it doesn't exist
            java.io.File avatarsDir = new java.io.File("img/avatars");
            if (!avatarsDir.exists()) {
                avatarsDir.mkdirs();
            }
            
            // Get file extension
            String fileName = sourceFile.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            
            // Create new filename: username + extension
            String newFileName = username + extension;
            java.io.File destFile = new java.io.File(avatarsDir, newFileName);
            
            // Copy file
            java.nio.file.Files.copy(sourceFile.toPath(), destFile.toPath(), 
                                   java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            
            // Return relative path
            return "img/avatars/" + newFileName;
            
        } catch (Exception e) {
            System.err.println("❌ Error saving profile picture: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public static void deleteProfilePicture(String avatarPath) {
        if (avatarPath != null && !avatarPath.isEmpty()) {
            try {
                java.io.File file = new java.io.File(avatarPath);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                System.err.println("❌ Error deleting profile picture: " + e.getMessage());
            }
        }
    }
    
    public static ImageIcon loadProfilePicture(String avatarPath, int width, int height) {
        if (avatarPath == null || avatarPath.isEmpty()) {
            return null;
        }
        
        try {
            java.io.File file = new java.io.File(avatarPath);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("❌ Error loading profile picture: " + e.getMessage());
        }
        
        return null;
    }
    
    // Initialize demo data if users table is empty
    public static void initializeDemoData() {
        if (getTotalUsersCount() == 0) {
            System.out.println("🔄 Initializing demo user data...");
            
            // Add demo users matching the JSON schema
            addUser("Admin Cf", "admincf", "admin123", "Admin", "Product", "Active");
            addUser("Andrea Watson", "andreawatson", "andrea123", "Manajer", "Legal", "Active");
            addUser("Candice Wu", "candicewu", "candice123", "Manajer", "Design", "Active");
            addUser("Drew Cano", "drewcano", "drew123", "Karyawan", "Finance", "Active");
            addUser("Jonathan Choi", "jonathanchoi", "jonathan123", "Manajer", "Product", "Active");
            addUser("Kevin Darma", "kevindarma", "kevin123", "Karyawan", "Product", "Active");
            addUser("Lana Steiner", "lanasteiner", "lana123", "Manajer", "Marketing", "Active");
            addUser("Michael Scott", "michaelscott", "michael123", "Manajer", "Finance", "Active");
            addUser("Olivia Rhye", "oliviarhye", "olivia123", "Karyawan", "Design", "Active");
            addUser("Phoenix Baker", "phoenixbaker", "phoenix123", "Karyawan", "Marketing", "Active");
            addUser("Sara Perez", "saraperez", "sara123", "Karyawan", "Legal", "Active");
            
            System.out.println("✅ Demo user data initialized successfully!");
        }
    }
} 
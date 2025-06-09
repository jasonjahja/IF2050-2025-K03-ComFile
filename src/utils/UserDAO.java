package utils;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class UserDAO {
    
    public static class User {
        public String username;
        public String password;
        public String role;
        public String fullName;
        public String department;
        public String status;
        
        public User(String username, String password, String role, String fullName, 
                   String department, String status) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.fullName = fullName;
            this.department = department;
            this.status = status;
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
                    rs.getString("status")
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
                    rs.getString("status")
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
        String query = "INSERT INTO users (username, password, role, full_name, department, status) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setString(4, fullName);
            stmt.setString(5, department);
            stmt.setString(6, status);
            
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
        String query = "UPDATE users SET full_name = ?, username = ?, password = ?, role = ?, " +
                      "department = ?, status = ? WHERE username = ?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, fullName);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.setString(5, department);
            stmt.setString(6, status);
            stmt.setString(7, originalUsername);
            
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
                    rs.getString("status")
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
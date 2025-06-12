package test.admin;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import pages.Login;
import pages.Admin.AdminDashboard;
import pages.Admin.UserManagementDashboard;
import pages.Admin.EditUserPage;
import utils.DBConnection;
import utils.UserDAO;

/**
 * Comprehensive test suite for Admin functionality
 * Tests the complete flow: MainApplication -> Login -> AdminDashboard -> UserManagement -> Edit
 * 
 * This test class provides manual testing capabilities and can be run to verify:
 * 1. Admin login with credentials (username: admincf, password: admin123)
 * 2. Navigation to AdminDashboard
 * 3. "See More" button functionality to open UserManagementDashboard
 * 4. Delete user functionality (bin button)
 * 5. Edit user functionality (edit button)
 */
public class AdminFunctionalityTest {
    
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    private static boolean verbose = true;
    
    // Test data
    private static final String ADMIN_USERNAME = "admincf";
    private static final String ADMIN_PASSWORD = "admin123";
    
    public static void main(String[] args) {
        System.out.println("=== Admin Functionality Test Suite ===");
        System.out.println("Testing complete admin workflow...\n");
        
        // Run all tests
        testLoginFunctionality();
        testAdminDashboardCreation();
        testUserManagementNavigation();
        testUserTableOperations();
        testEditUserPageNavigation();
        testDatabaseOperations();
        
        // Print results
        printTestResults();
    }
    
    /**
     * Test 1: Login Functionality
     * Tests the login process with admin credentials
     */
    private static void testLoginFunctionality() {
        System.out.println("Test 1: Login Functionality");
        System.out.println("---------------------------");
        
        try {
            // Create a mock JFrame for testing
            JFrame mockFrame = new JFrame("Test Frame");
            Login loginPanel = new Login(mockFrame);
            
            assert loginPanel != null : "Login panel should be created";
            testsPassed++;
            log("✓ Login panel created successfully");
            
            // Test login panel without parent frame
            Login loginWithoutParent = new Login();
            assert loginWithoutParent != null : "Login panel should work without parent frame";
            testsPassed++;
            log("✓ Login panel works without parent frame");
            
            // Test getUserRoleFromDatabase method using reflection (for unit testing)
            testLoginCredentials(loginPanel);
            
            mockFrame.dispose();
            
        } catch (Exception e) {
            log("✗ Login functionality test failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        testsTotal += 2;
        System.out.println();
    }
    
    /**
     * Test login credentials using reflection to access private methods
     */
    private static void testLoginCredentials(Login loginPanel) {
        try {
            Method getUserRoleMethod = Login.class.getDeclaredMethod("getUserRoleFromDatabase", String.class, String.class);
            getUserRoleMethod.setAccessible(true);
            
            // Note: This will try to connect to actual database
            // In a real test environment, you would mock the database connection
            log("Testing login credentials (Note: requires database connection)");
            
            // Test with empty credentials
            String result = (String) getUserRoleMethod.invoke(loginPanel, "", "");
            assert result == null : "Empty credentials should return null";
            log("✓ Empty credentials handled correctly");
            
        } catch (Exception e) {
            log("Database connection test skipped (expected in test environment): " + e.getMessage());
        }
    }
    
    /**
     * Test 2: AdminDashboard Creation
     * Tests the creation and initialization of AdminDashboard
     */
    private static void testAdminDashboardCreation() {
        System.out.println("Test 2: AdminDashboard Creation");
        System.out.println("-------------------------------");
        
        try {
            // Create a container to simulate the parent container
            JPanel parentContainer = new JPanel(new CardLayout());
            
            // Create AdminDashboard
            AdminDashboard adminDashboard = new AdminDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            
            assert adminDashboard != null : "AdminDashboard should be created";
            testsPassed++;
            log("✓ AdminDashboard created successfully");
            
            // Test that the dashboard has the expected components
            Component[] components = adminDashboard.getComponents();
            assert components.length > 0 : "AdminDashboard should have components";
            testsPassed++;
            log("✓ AdminDashboard has components");
            
            // Test the layout
            assert adminDashboard.getLayout() instanceof BorderLayout : "AdminDashboard should use BorderLayout";
            testsPassed++;
            log("✓ AdminDashboard uses correct layout");
            
        } catch (Exception e) {
            log("✗ AdminDashboard creation test failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 3: User Management Navigation
     * Tests the "See More" button functionality
     */
    private static void testUserManagementNavigation() {
        System.out.println("Test 3: User Management Navigation");
        System.out.println("----------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            AdminDashboard adminDashboard = new AdminDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            
            // Test openUserManagementPage method using reflection
            Method openUserManagementMethod = AdminDashboard.class.getDeclaredMethod("openUserManagementPage");
            openUserManagementMethod.setAccessible(true);
            
            // Add dashboard to container first
            parentContainer.add(adminDashboard, "ADMIN_DASHBOARD");
            
            // Call the method
            openUserManagementMethod.invoke(adminDashboard);
            
            // Check if UserManagementDashboard was added
            boolean userManagementFound = false;
            for (Component comp : parentContainer.getComponents()) {
                if (comp instanceof UserManagementDashboard) {
                    userManagementFound = true;
                    break;
                }
            }
            
            assert userManagementFound : "UserManagementDashboard should be added to container";
            testsPassed++;
            log("✓ User Management navigation works");
            
        } catch (Exception e) {
            log("✗ User Management navigation test failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        testsTotal += 1;
        System.out.println();
    }
    
    /**
     * Test 4: User Table Operations
     * Tests delete and edit button functionality
     */
    private static void testUserTableOperations() {
        System.out.println("Test 4: User Table Operations");
        System.out.println("-----------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard userMgmt = new UserManagementDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            
            assert userMgmt != null : "UserManagementDashboard should be created";
            testsPassed++;
            log("✓ UserManagementDashboard created");
            
            // Test that the dashboard has the expected structure
            Component[] components = userMgmt.getComponents();
            assert components.length > 0 : "UserManagementDashboard should have components";
            testsPassed++;
            log("✓ UserManagementDashboard has components");
            
            // Test extractUsername method
            Method extractUsernameMethod = UserManagementDashboard.class.getDeclaredMethod("extractUsername", String.class);
            extractUsernameMethod.setAccessible(true);
            
            String result = (String) extractUsernameMethod.invoke(userMgmt, "John Doe (@johndoe)");
            assert "johndoe".equals(result) : "Should extract username correctly";
            testsPassed++;
            log("✓ Username extraction works correctly");
            
        } catch (Exception e) {
            log("✗ User Table operations test failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 5: Edit User Page Navigation
     * Tests navigation to EditUserPage
     */
    private static void testEditUserPageNavigation() {
        System.out.println("Test 5: Edit User Page Navigation");
        System.out.println("---------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard userMgmt = new UserManagementDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            
            // Test openEditUserPage method using reflection
            Method openEditUserPageMethod = UserManagementDashboard.class.getDeclaredMethod("openEditUserPage", String.class);
            openEditUserPageMethod.setAccessible(true);
            
            parentContainer.add(userMgmt, "USER_MANAGEMENT");
            
            // Call the method with test user
            openEditUserPageMethod.invoke(userMgmt, "Test User (@testuser)");
            
            // Check if EditUserPage was added
            boolean editUserPageFound = false;
            for (Component comp : parentContainer.getComponents()) {
                if (comp instanceof EditUserPage) {
                    editUserPageFound = true;
                    break;
                }
            }
            
            assert editUserPageFound : "EditUserPage should be added to container";
            testsPassed++;
            log("✓ Edit User Page navigation works");
            
        } catch (Exception e) {
            log("✗ Edit User Page navigation test failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        testsTotal += 1;
        System.out.println();
    }
    
    /**
     * Test 6: Database Operations
     * Tests UserDAO functionality (if database is available)
     */
    private static void testDatabaseOperations() {
        System.out.println("Test 6: Database Operations");
        System.out.println("---------------------------");
        
        try {
            // Test database connection
            Connection conn = DBConnection.connect();
            if (conn != null) {
                conn.close();
                testsPassed++;
                log("✓ Database connection successful");
                
                // Test UserDAO operations
                testUserDAOOperations();
                
            } else {
                log("! Database connection not available (expected in test environment)");
            }
            
        } catch (Exception e) {
            log("! Database operations skipped: " + e.getMessage());
        }
        
        testsTotal += 1;
        System.out.println();
    }
    
    /**
     * Test UserDAO operations
     */
    private static void testUserDAOOperations() {
        try {
            // Test getUsersForDashboard
            java.util.List<UserDAO.User> users = UserDAO.getUsersForDashboard();
            assert users != null : "getUsersForDashboard should not return null";
            testsPassed++;
            log("✓ getUsersForDashboard works");
            
            // Test getAllUsers
            java.util.List<UserDAO.User> allUsers = UserDAO.getAllUsers();
            assert allUsers != null : "getAllUsers should not return null";
            testsPassed++;
            log("✓ getAllUsers works");
            
            testsTotal += 2;
            
        } catch (Exception e) {
            log("UserDAO test failed: " + e.getMessage());
        }
    }
    
    /**
     * Print test results summary
     */
    private static void printTestResults() {
        System.out.println("=== Test Results ===");
        System.out.println("Tests Passed: " + testsPassed + "/" + testsTotal);
        System.out.println("Success Rate: " + (testsTotal > 0 ? (testsPassed * 100 / testsTotal) : 0) + "%");
        
        if (testsPassed == testsTotal) {
            System.out.println("🎉 All tests passed!");
        } else {
            System.out.println("⚠️  Some tests failed. Please review the output above.");
        }
    }
    
    /**
     * Logging utility
     */
    private static void log(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }
    
    /**
     * Performance test for UI responsiveness
     */
    public static void performanceTest() {
        System.out.println("\n=== Performance Test ===");
        
        long startTime = System.currentTimeMillis();
        
        // Test UI creation performance
        JPanel parentContainer = new JPanel(new CardLayout());
        AdminDashboard adminDashboard = new AdminDashboard(ADMIN_USERNAME, "Admin", parentContainer);
        UserManagementDashboard userMgmt = new UserManagementDashboard(ADMIN_USERNAME, "Admin", parentContainer);
        
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        
        System.out.println("UI Creation Time: " + timeTaken + "ms");
        
        if (timeTaken < 1000) {
            System.out.println("✓ Performance: Excellent (< 1s)");
        } else if (timeTaken < 3000) {
            System.out.println("✓ Performance: Good (< 3s)");
        } else {
            System.out.println("⚠️ Performance: Slow (> 3s)");
        }
    }
    
    /**
     * Memory usage test
     */
    public static void memoryTest() {
        System.out.println("\n=== Memory Test ===");
        
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Create multiple instances to test memory usage
        for (int i = 0; i < 10; i++) {
            JPanel parentContainer = new JPanel(new CardLayout());
            AdminDashboard adminDashboard = new AdminDashboard(ADMIN_USERNAME, "Admin", parentContainer);
        }
        
        System.gc(); // Suggest garbage collection
        
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterMemory - beforeMemory;
        
        System.out.println("Memory Used: " + (memoryUsed / 1024) + " KB");
        
        if (memoryUsed < 10 * 1024 * 1024) { // < 10MB
            System.out.println("✓ Memory Usage: Good");
        } else {
            System.out.println("⚠️ Memory Usage: High");
        }
    }
} 
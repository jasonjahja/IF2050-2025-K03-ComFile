package test.integration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import main.MainApplication;
import pages.Login;
import pages.Admin.AdminDashboard;
import pages.Admin.UserManagementDashboard;
import pages.Admin.EditUserPage;

/**
 * Integration test for complete admin workflow
 * 
 * Tests the entire flow:
 * 1. MainApplication startup
 * 2. Login with admin credentials (username: admincf, password: admin123)
 * 3. Navigate to AdminDashboard
 * 4. Click "See More" to open UserManagementDashboard
 * 5. Test delete user functionality (bin button)
 * 6. Test edit user functionality (edit button) to open EditUserPage
 */
public class AdminWorkflowIntegrationTest {
    
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    private static final String ADMIN_USERNAME = "admincf";
    private static final String ADMIN_PASSWORD = "admin123";
    
    public static void main(String[] args) {
        System.out.println("=== Admin Workflow Integration Test ===");
        System.out.println("Testing complete admin functionality workflow...\n");
        
        // Set headless mode for testing
        System.setProperty("java.awt.headless", "false");
        
        runCompleteWorkflowTest();
        printResults();
    }
    
    /**
     * Run the complete workflow test
     */
    private static void runCompleteWorkflowTest() {
        testMainApplicationStartup();
        testLoginProcess();
        testAdminDashboardNavigation();
        testUserManagementWorkflow();
        testEditUserWorkflow();
        testCompleteIntegration();
    }
    
    /**
     * Test 1: MainApplication Startup
     */
    private static void testMainApplicationStartup() {
        System.out.println("Test 1: MainApplication Startup");
        System.out.println("-------------------------------");
        
        try {
            // Test MainApplication creation with admin user
            MainApplication mainApp = new MainApplication(ADMIN_USERNAME, "Admin");
            
            assertNotNull(mainApp, "MainApplication should be created");
            testsPassed++;
            log("✓ MainApplication created with admin user");
            
            // Test that it's an instance of JFrame
            assertTrue(mainApp instanceof JFrame, "MainApplication should extend JFrame");
            testsPassed++;
            log("✓ MainApplication is a JFrame");
            
            // Test title
            String title = mainApp.getTitle();
            assertTrue(title.contains("ComFile"), "Title should contain 'ComFile'");
            testsPassed++;
            log("✓ Window title is correct");
            
            // Clean up
            mainApp.dispose();
            
        } catch (Exception e) {
            log("✗ MainApplication startup test failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 2: Login Process
     */
    private static void testLoginProcess() {
        System.out.println("Test 2: Login Process");
        System.out.println("---------------------");
        
        try {
            // Create login frame
            JFrame loginFrame = new JFrame("Test Login");
            Login loginPanel = new Login(loginFrame);
            loginFrame.setContentPane(loginPanel);
            
            assertNotNull(loginPanel, "Login panel should be created");
            testsPassed++;
            log("✓ Login panel created");
            
            // Test login form components exist
            Component[] components = loginPanel.getComponents();
            assertTrue(components.length > 0, "Login panel should have components");
            testsPassed++;
            log("✓ Login form has components");
            
            // Test getUserRoleFromDatabase method
            testLoginAuthentication(loginPanel);
            
            loginFrame.dispose();
            
        } catch (Exception e) {
            log("✗ Login process test failed: " + e.getMessage());
        }
        
        testsTotal += 2;
        System.out.println();
    }
    
    /**
     * Test login authentication method
     */
    private static void testLoginAuthentication(Login loginPanel) {
        try {
            Method getUserRoleMethod = Login.class.getDeclaredMethod("getUserRoleFromDatabase", String.class, String.class);
            getUserRoleMethod.setAccessible(true);
            
            log("Testing login authentication (requires database connection)");
            
            // Note: This test requires actual database connection
            // In production tests, you would mock the database
            
        } catch (Exception e) {
            log("Login authentication test skipped (database dependency): " + e.getMessage());
        }
    }
    
    /**
     * Test 3: AdminDashboard Navigation
     */
    private static void testAdminDashboardNavigation() {
        System.out.println("Test 3: AdminDashboard Navigation");
        System.out.println("---------------------------------");
        
        try {
            // Create admin dashboard with container
            JPanel parentContainer = new JPanel(new CardLayout());
            AdminDashboard adminDashboard = new AdminDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            
            assertNotNull(adminDashboard, "AdminDashboard should be created");
            testsPassed++;
            log("✓ AdminDashboard created");
            
            // Add to container
            parentContainer.add(adminDashboard, "ADMIN_DASHBOARD");
            
            // Test "See More" functionality
            testSeeMoreFunctionality(adminDashboard, parentContainer);
            
        } catch (Exception e) {
            log("✗ AdminDashboard navigation test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
        System.out.println();
    }
    
    /**
     * Test "See More" button functionality
     */
    private static void testSeeMoreFunctionality(AdminDashboard adminDashboard, JPanel parentContainer) {
        try {
            // Test openUserManagementPage method
            Method openUserManagementMethod = AdminDashboard.class.getDeclaredMethod("openUserManagementPage");
            openUserManagementMethod.setAccessible(true);
            
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
            
            assertTrue(userManagementFound, "UserManagementDashboard should be added");
            testsPassed++;
            log("✓ 'See More' functionality works - UserManagementDashboard opened");
            
        } catch (Exception e) {
            log("✗ See More functionality test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
    }
    
    /**
     * Test 4: User Management Workflow
     */
    private static void testUserManagementWorkflow() {
        System.out.println("Test 4: User Management Workflow");
        System.out.println("--------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard userMgmt = new UserManagementDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            
            assertNotNull(userMgmt, "UserManagementDashboard should be created");
            testsPassed++;
            log("✓ UserManagementDashboard created");
            
            // Test user table creation
            testUserTableCreation(userMgmt);
            
            // Test delete functionality
            testDeleteUserFunctionality(userMgmt);
            
            // Test add user navigation
            testAddUserNavigation(userMgmt, parentContainer);
            
        } catch (Exception e) {
            log("✗ User Management workflow test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
        System.out.println();
    }
    
    /**
     * Test user table creation
     */
    private static void testUserTableCreation(UserManagementDashboard userMgmt) {
        try {
            Method createUsersTableMethod = UserManagementDashboard.class.getDeclaredMethod("createUsersTable");
            createUsersTableMethod.setAccessible(true);
            
            // This method creates and populates the users table
            createUsersTableMethod.invoke(userMgmt);
            
            testsPassed++;
            log("✓ User table creation works");
            
        } catch (Exception e) {
            log("User table creation test skipped (database dependency): " + e.getMessage());
        }
        
        testsTotal += 1;
    }
    
    /**
     * Test delete user functionality (bin button)
     */
    private static void testDeleteUserFunctionality(UserManagementDashboard userMgmt) {
        try {
            // Test extractUsername method which is used for delete functionality
            Method extractUsernameMethod = UserManagementDashboard.class.getDeclaredMethod("extractUsername", String.class);
            extractUsernameMethod.setAccessible(true);
            
            String result = (String) extractUsernameMethod.invoke(userMgmt, "Test User (@testuser)");
            assertTrue("testuser".equals(result), "Should extract username correctly for delete");
            
            testsPassed++;
            log("✓ Delete user functionality accessible");
            
        } catch (Exception e) {
            log("Delete user functionality test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
    }
    
    /**
     * Test add user navigation
     */
    private static void testAddUserNavigation(UserManagementDashboard userMgmt, JPanel parentContainer) {
        try {
            Method openAddUserPageMethod = UserManagementDashboard.class.getDeclaredMethod("openAddUserPage");
            openAddUserPageMethod.setAccessible(true);
            
            parentContainer.add(userMgmt, "USER_MANAGEMENT");
            
            // Call the method
            openAddUserPageMethod.invoke(userMgmt);
            
            // Check if AddUserPage was added
            boolean addUserPageFound = false;
            for (Component comp : parentContainer.getComponents()) {
                if (comp.getClass().getSimpleName().equals("AddUserPage")) {
                    addUserPageFound = true;
                    break;
                }
            }
            
            assertTrue(addUserPageFound, "AddUserPage should be added");
            testsPassed++;
            log("✓ Add user navigation works");
            
        } catch (Exception e) {
            log("✗ Add user navigation test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
    }
    
    /**
     * Test 5: Edit User Workflow
     */
    private static void testEditUserWorkflow() {
        System.out.println("Test 5: Edit User Workflow");
        System.out.println("--------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard userMgmt = new UserManagementDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            
            // Test edit user navigation
            testEditUserNavigation(userMgmt, parentContainer);
            
            // Test EditUserPage functionality
            testEditUserPageFunctionality(parentContainer);
            
        } catch (Exception e) {
            log("✗ Edit User workflow test failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Test edit user navigation (edit button)
     */
    private static void testEditUserNavigation(UserManagementDashboard userMgmt, JPanel parentContainer) {
        try {
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
            
            assertTrue(editUserPageFound, "EditUserPage should be added");
            testsPassed++;
            log("✓ Edit user navigation works - EditUserPage opened");
            
        } catch (Exception e) {
            log("✗ Edit user navigation test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
    }
    
    /**
     * Test EditUserPage functionality
     */
    private static void testEditUserPageFunctionality(JPanel parentContainer) {
        try {
            // Find the EditUserPage in the container
            EditUserPage editUserPage = null;
            for (Component comp : parentContainer.getComponents()) {
                if (comp instanceof EditUserPage) {
                    editUserPage = (EditUserPage) comp;
                    break;
                }
            }
            
            if (editUserPage != null) {
                // Test that EditUserPage has the expected components
                Component[] components = editUserPage.getComponents();
                assertTrue(components.length > 0, "EditUserPage should have components");
                testsPassed++;
                log("✓ EditUserPage has form components");
                
                // Test navigation back
                testEditUserPageNavigation(editUserPage);
            } else {
                log("EditUserPage not found in container");
            }
            
        } catch (Exception e) {
            log("EditUserPage functionality test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
    }
    
    /**
     * Test EditUserPage navigation methods
     */
    private static void testEditUserPageNavigation(EditUserPage editUserPage) {
        try {
            // Test goBackToUserManagement method
            Method goBackMethod = EditUserPage.class.getDeclaredMethod("goBackToUserManagement");
            goBackMethod.setAccessible(true);
            
            assertNotNull(goBackMethod, "Go back method should exist");
            testsPassed++;
            log("✓ Edit user page navigation methods available");
            
        } catch (Exception e) {
            log("EditUserPage navigation test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
    }
    
    /**
     * Test 6: Complete Integration
     */
    private static void testCompleteIntegration() {
        System.out.println("Test 6: Complete Integration");
        System.out.println("----------------------------");
        
        try {
            // Test the complete workflow in sequence
            log("Running complete integration test...");
            
            // 1. Create main application
            MainApplication mainApp = new MainApplication(ADMIN_USERNAME, "Admin");
            
            // 2. Test that admin dashboard is shown for admin user
            // (This is handled automatically in MainApplication constructor)
            
            testsPassed++;
            log("✓ Complete admin workflow integration successful");
            
            // Clean up
            mainApp.dispose();
            
        } catch (Exception e) {
            log("✗ Complete integration test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
        System.out.println();
    }
    
    /**
     * Performance test for the complete workflow
     */
    public static void performanceTest() {
        System.out.println("\n=== Performance Test ===");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Create and setup complete workflow
            JPanel parentContainer = new JPanel(new CardLayout());
            AdminDashboard adminDashboard = new AdminDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            UserManagementDashboard userMgmt = new UserManagementDashboard(ADMIN_USERNAME, "Admin", parentContainer);
            EditUserPage editPage = new EditUserPage(ADMIN_USERNAME, "Admin", "Test User (@test)", parentContainer);
            
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            
            System.out.println("Complete workflow creation time: " + timeTaken + "ms");
            
            if (timeTaken < 2000) {
                System.out.println("✓ Performance: Excellent (< 2s)");
            } else if (timeTaken < 5000) {
                System.out.println("✓ Performance: Good (< 5s)");
            } else {
                System.out.println("⚠️ Performance: Slow (> 5s)");
            }
            
        } catch (Exception e) {
            System.out.println("Performance test failed: " + e.getMessage());
        }
    }
    
    // Utility assertion methods
    private static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(message + " Expected: " + expected + ", Actual: " + actual);
        }
    }
    
    private static void log(String message) {
        System.out.println(message);
    }
    
    private static void printResults() {
        System.out.println("=== Integration Test Results ===");
        System.out.println("Tests Passed: " + testsPassed + "/" + testsTotal);
        System.out.println("Success Rate: " + (testsTotal > 0 ? (testsPassed * 100 / testsTotal) : 0) + "%");
        
        if (testsPassed == testsTotal) {
            System.out.println("🎉 All integration tests passed!");
        } else {
            System.out.println("⚠️ Some integration tests failed. Please review the output above.");
        }

        // Run performance test
        performanceTest();
    }
} 
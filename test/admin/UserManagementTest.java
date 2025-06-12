package test.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.List;

import pages.Admin.UserManagementDashboard;
import pages.Admin.EditUserPage;
import pages.Admin.AddUserPage;
import utils.UserDAO;

/**
 * Specific tests for User Management functionality
 * Tests the UserManagementDashboard operations including:
 * - User table display
 * - Delete user functionality (bin button)
 * - Edit user functionality (edit button)
 * - Add user functionality
 */
public class UserManagementTest {
    
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    private static final String TEST_ADMIN = "admincf";
    
    public static void main(String[] args) {
        System.out.println("=== User Management Test Suite ===");
        System.out.println("Testing user management operations...\n");
        
        runAllTests();
        printResults();
    }
    
    public static void runAllTests() {
        testUserManagementDashboardCreation();
        testUsersTableGeneration();
        testAddUserButton();
        testDeleteUserFunctionality();
        testEditUserFunctionality();
        testUserDataExtraction();
        testNavigationMethods();
    }
    
    /**
     * Test 1: UserManagementDashboard Creation
     */
    private static void testUserManagementDashboardCreation() {
        System.out.println("Test 1: UserManagementDashboard Creation");
        System.out.println("----------------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard dashboard = new UserManagementDashboard(TEST_ADMIN, "Admin", parentContainer);
            
            assertNotNull(dashboard, "UserManagementDashboard should be created");
            testsPassed++;
            log("✓ UserManagementDashboard created successfully");
            
            // Test layout
            assertTrue(dashboard.getLayout() instanceof BorderLayout, "Should use BorderLayout");
            testsPassed++;
            log("✓ Uses correct layout");
            
            // Test background color
            assertEquals(Color.WHITE, dashboard.getBackground(), "Background should be white");
            testsPassed++;
            log("✓ Background color is correct");
            
        } catch (Exception e) {
            log("✗ UserManagementDashboard creation failed: " + e.getMessage());
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 2: Users Table Generation
     */
    private static void testUsersTableGeneration() {
        System.out.println("Test 2: Users Table Generation");
        System.out.println("------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard dashboard = new UserManagementDashboard(TEST_ADMIN, "Admin", parentContainer);
            
            // Test getUserData method using reflection
            Method getUserDataMethod = UserManagementDashboard.class.getDeclaredMethod("getUserData");
            getUserDataMethod.setAccessible(true);
            
            Object[][] userData = (Object[][]) getUserDataMethod.invoke(dashboard);
            
            assertNotNull(userData, "User data should not be null");
            testsPassed++;
            log("✓ User data generated");
            
            // Test data structure
            if (userData.length > 0) {
                assertTrue(userData[0].length == 5, "Each user row should have 5 columns");
                testsPassed++;
                log("✓ User data has correct structure");
                
                // Test data format
                String firstUserInfo = (String) userData[0][0];
                assertTrue(firstUserInfo.contains("(@"), "User info should contain username in parentheses");
                testsPassed++;
                log("✓ User data format is correct");
            } else {
                log("! No user data available (expected if database is empty)");
                testsTotal--; // Adjust test count
            }
            
        } catch (Exception e) {
            log("✗ Users table generation test failed: " + e.getMessage());
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 3: Add User Button
     */
    private static void testAddUserButton() {
        System.out.println("Test 3: Add User Button");
        System.out.println("-----------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard dashboard = new UserManagementDashboard(TEST_ADMIN, "Admin", parentContainer);
            
            // Test createAddUserButton method using reflection
            Method createAddUserButtonMethod = UserManagementDashboard.class.getDeclaredMethod("createAddUserButton");
            createAddUserButtonMethod.setAccessible(true);
            
            JButton addUserBtn = (JButton) createAddUserButtonMethod.invoke(dashboard);
            
            assertNotNull(addUserBtn, "Add User button should be created");
            testsPassed++;
            log("✓ Add User button created");
            
            // Test button properties
            assertTrue(addUserBtn.getText().contains("Add User"), "Button should have correct text");
            testsPassed++;
            log("✓ Button text is correct");
            
            assertEquals(new Color(79, 109, 245), addUserBtn.getBackground(), "Button should have correct background color");
            testsPassed++;
            log("✓ Button styling is correct");
            
        } catch (Exception e) {
            log("✗ Add User button test failed: " + e.getMessage());
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 4: Delete User Functionality
     */
    private static void testDeleteUserFunctionality() {
        System.out.println("Test 4: Delete User Functionality");
        System.out.println("---------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard dashboard = new UserManagementDashboard(TEST_ADMIN, "Admin", parentContainer);
            
            // Test extractUsername method for delete functionality
            Method extractUsernameMethod = UserManagementDashboard.class.getDeclaredMethod("extractUsername", String.class);
            extractUsernameMethod.setAccessible(true);
            
            String username = (String) extractUsernameMethod.invoke(dashboard, "John Doe (@johndoe)");
            assertEquals("johndoe", username, "Should extract username correctly");
            testsPassed++;
            log("✓ Username extraction for delete works");
            
        } catch (Exception e) {
            log("✗ Delete user functionality test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
        System.out.println();
    }
    
    /**
     * Test 5: Edit User Functionality
     */
    private static void testEditUserFunctionality() {
        System.out.println("Test 5: Edit User Functionality");
        System.out.println("-------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard dashboard = new UserManagementDashboard(TEST_ADMIN, "Admin", parentContainer);
            
            // Test openEditUserPage method using reflection
            Method openEditUserPageMethod = UserManagementDashboard.class.getDeclaredMethod("openEditUserPage", String.class);
            openEditUserPageMethod.setAccessible(true);
            
            parentContainer.add(dashboard, "USER_MANAGEMENT");
            
            // Call edit user page
            openEditUserPageMethod.invoke(dashboard, "Test User (@testuser)");
            
            // Check if EditUserPage was added to container
            boolean editPageFound = false;
            for (Component comp : parentContainer.getComponents()) {
                if (comp instanceof EditUserPage) {
                    editPageFound = true;
                    break;
                }
            }
            
            assertTrue(editPageFound, "EditUserPage should be added to container");
            testsPassed++;
            log("✓ Edit user page navigation works");
            
        } catch (Exception e) {
            log("✗ Edit user functionality test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
        System.out.println();
    }
    
    /**
     * Test 6: User Data Extraction
     */
    private static void testUserDataExtraction() {
        System.out.println("Test 6: User Data Extraction");
        System.out.println("----------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard dashboard = new UserManagementDashboard(TEST_ADMIN, "Admin", parentContainer);
            
            // Test extractUsername with various formats
            Method extractUsernameMethod = UserManagementDashboard.class.getDeclaredMethod("extractUsername", String.class);
            extractUsernameMethod.setAccessible(true);
            
            String[] testCases = {
                "John Doe (@johndoe)",
                "Jane Smith (@jane.smith)", 
                "Admin User (@admin)",
                "Test (@test123)"
            };
            
            String[] expectedResults = {
                "johndoe",
                "jane.smith",
                "admin", 
                "test123"
            };
            
            for (int i = 0; i < testCases.length; i++) {
                String result = (String) extractUsernameMethod.invoke(dashboard, testCases[i]);
                assertEquals(expectedResults[i], result, "Should extract username correctly from: " + testCases[i]);
                testsPassed++;
            }
            
            log("✓ Username extraction works for all formats");
            
        } catch (Exception e) {
            log("✗ User data extraction test failed: " + e.getMessage());
        }
        
        testsTotal += 4;
        System.out.println();
    }
    
    /**
     * Test 7: Navigation Methods
     */
    private static void testNavigationMethods() {
        System.out.println("Test 7: Navigation Methods");
        System.out.println("--------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            UserManagementDashboard dashboard = new UserManagementDashboard(TEST_ADMIN, "Admin", parentContainer);
            
            // Test openAddUserPage method using reflection
            Method openAddUserPageMethod = UserManagementDashboard.class.getDeclaredMethod("openAddUserPage");
            openAddUserPageMethod.setAccessible(true);
            
            parentContainer.add(dashboard, "USER_MANAGEMENT");
            
            // Call add user page
            openAddUserPageMethod.invoke(dashboard);
            
            // Check if AddUserPage was added to container
            boolean addPageFound = false;
            for (Component comp : parentContainer.getComponents()) {
                if (comp instanceof AddUserPage) {
                    addPageFound = true;
                    break;
                }
            }
            
            assertTrue(addPageFound, "AddUserPage should be added to container");
            testsPassed++;
            log("✓ Add user page navigation works");
            
        } catch (Exception e) {
            log("✗ Navigation methods test failed: " + e.getMessage());
        }
        
        testsTotal += 1;
        System.out.println();
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
        System.out.println("=== Test Results ===");
        System.out.println("Tests Passed: " + testsPassed + "/" + testsTotal);
        System.out.println("Success Rate: " + (testsTotal > 0 ? (testsPassed * 100 / testsTotal) : 0) + "%");
        
        if (testsPassed == testsTotal) {
            System.out.println("🎉 All user management tests passed!");
        } else {
            System.out.println("⚠️ Some tests failed. Please review the output above.");
        }
    }
} 
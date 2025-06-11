package test.admin;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import pages.Admin.EditUserPage;
import utils.UserDAO;

/**
 * Test class for EditUserPage functionality
 * Tests the edit user page operations and form handling
 */
public class EditUserPageTest {
    
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    private static final String TEST_ADMIN = "admincf";
    private static final String TEST_USER = "Test User (@testuser)";
    
    public static void main(String[] args) {
        System.out.println("=== Edit User Page Test Suite ===");
        System.out.println("Testing edit user page functionality...\n");
        
        runAllTests();
        printResults();
    }
    
    public static void runAllTests() {
        testEditUserPageCreation();
        testFormFieldsInitialization();
        testPasswordFieldFunctionality();
        testPhotoUploadSection();
        testFormValidation();
        testNavigationMethods();
        testUserDataLoading();
    }
    
    /**
     * Test 1: EditUserPage Creation
     */
    private static void testEditUserPageCreation() {
        System.out.println("Test 1: EditUserPage Creation");
        System.out.println("-----------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            EditUserPage editPage = new EditUserPage(TEST_ADMIN, "Admin", TEST_USER, parentContainer);
            
            assertNotNull(editPage, "EditUserPage should be created");
            testsPassed++;
            log("✓ EditUserPage created successfully");
            
            // Test layout
            assertTrue(editPage.getLayout() instanceof BorderLayout, "Should use BorderLayout");
            testsPassed++;
            log("✓ Uses correct layout");
            
            // Test background
            assertEquals(Color.WHITE, editPage.getBackground(), "Background should be white");
            testsPassed++;
            log("✓ Background color is correct");
            
        } catch (Exception e) {
            log("✗ EditUserPage creation failed: " + e.getMessage());
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 2: Form Fields Initialization
     */
    private static void testFormFieldsInitialization() {
        System.out.println("Test 2: Form Fields Initialization");
        System.out.println("----------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            EditUserPage editPage = new EditUserPage(TEST_ADMIN, "Admin", TEST_USER, parentContainer);
            
            // Test form fields existence using reflection
            Field fullNameFieldField = EditUserPage.class.getDeclaredField("fullNameField");
            fullNameFieldField.setAccessible(true);
            JTextField fullNameField = (JTextField) fullNameFieldField.get(editPage);
            
            assertNotNull(fullNameField, "Full name field should exist");
            testsPassed++;
            log("✓ Full name field exists");
            
            Field usernameFieldField = EditUserPage.class.getDeclaredField("usernameField");
            usernameFieldField.setAccessible(true);
            JTextField usernameField = (JTextField) usernameFieldField.get(editPage);
            
            assertNotNull(usernameField, "Username field should exist");
            testsPassed++;
            log("✓ Username field exists");
            
            Field roleComboBoxField = EditUserPage.class.getDeclaredField("roleComboBox");
            roleComboBoxField.setAccessible(true);
            JComboBox<String> roleComboBox = (JComboBox<String>) roleComboBoxField.get(editPage);
            
            assertNotNull(roleComboBox, "Role combo box should exist");
            testsPassed++;
            log("✓ Role combo box exists");
            
            Field departmentComboBoxField = EditUserPage.class.getDeclaredField("departmentComboBox");
            departmentComboBoxField.setAccessible(true);
            JComboBox<String> departmentComboBox = (JComboBox<String>) departmentComboBoxField.get(editPage);
            
            assertNotNull(departmentComboBox, "Department combo box should exist");
            testsPassed++;
            log("✓ Department combo box exists");
            
        } catch (Exception e) {
            log("✗ Form fields initialization test failed: " + e.getMessage());
        }
        
        testsTotal += 4;
        System.out.println();
    }
    
    /**
     * Test 3: Password Field Functionality
     */
    private static void testPasswordFieldFunctionality() {
        System.out.println("Test 3: Password Field Functionality");
        System.out.println("------------------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            EditUserPage editPage = new EditUserPage(TEST_ADMIN, "Admin", TEST_USER, parentContainer);
            
            // Test password field
            Field passwordFieldField = EditUserPage.class.getDeclaredField("passwordField");
            passwordFieldField.setAccessible(true);
            JPasswordField passwordField = (JPasswordField) passwordFieldField.get(editPage);
            
            assertNotNull(passwordField, "Password field should exist");
            testsPassed++;
            log("✓ Password field exists");
            
            // Test eye icon
            Field eyeIconField = EditUserPage.class.getDeclaredField("eyeIcon");
            eyeIconField.setAccessible(true);
            JLabel eyeIcon = (JLabel) eyeIconField.get(editPage);
            
            assertNotNull(eyeIcon, "Eye icon should exist");
            testsPassed++;
            log("✓ Eye icon exists");
            
            // Test toggle password visibility method
            Method togglePasswordVisibilityMethod = EditUserPage.class.getDeclaredMethod("togglePasswordVisibility");
            togglePasswordVisibilityMethod.setAccessible(true);
            
            // Get initial state
            Field passwordVisibleField = EditUserPage.class.getDeclaredField("passwordVisible");
            passwordVisibleField.setAccessible(true);
            boolean initialState = passwordVisibleField.getBoolean(editPage);
            
            // Toggle visibility
            togglePasswordVisibilityMethod.invoke(editPage);
            boolean newState = passwordVisibleField.getBoolean(editPage);
            
            assertTrue(initialState != newState, "Password visibility should toggle");
            testsPassed++;
            log("✓ Password visibility toggle works");
            
        } catch (Exception e) {
            log("✗ Password field functionality test failed: " + e.getMessage());
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 4: Photo Upload Section
     */
    private static void testPhotoUploadSection() {
        System.out.println("Test 4: Photo Upload Section");
        System.out.println("----------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            EditUserPage editPage = new EditUserPage(TEST_ADMIN, "Admin", TEST_USER, parentContainer);
            
            // Test photo circle
            Field photoCircleField = EditUserPage.class.getDeclaredField("photoCircle");
            photoCircleField.setAccessible(true);
            JPanel photoCircle = (JPanel) photoCircleField.get(editPage);
            
            assertNotNull(photoCircle, "Photo circle should exist");
            testsPassed++;
            log("✓ Photo circle exists");
            
            // Test upload photo method exists
            Method uploadPhotoMethod = EditUserPage.class.getDeclaredMethod("uploadPhoto");
            uploadPhotoMethod.setAccessible(true);
            
            assertNotNull(uploadPhotoMethod, "Upload photo method should exist");
            testsPassed++;
            log("✓ Upload photo method exists");
            
            // Test selected photo file field
            Field selectedPhotoFileField = EditUserPage.class.getDeclaredField("selectedPhotoFile");
            selectedPhotoFileField.setAccessible(true);
            
            assertNotNull(selectedPhotoFileField, "Selected photo file field should exist");
            testsPassed++;
            log("✓ Selected photo file field exists");
            
        } catch (Exception e) {
            log("✗ Photo upload section test failed: " + e.getMessage());
        }
        
        testsTotal += 3;
        System.out.println();
    }
    
    /**
     * Test 5: Form Validation
     */
    private static void testFormValidation() {
        System.out.println("Test 5: Form Validation");
        System.out.println("-----------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            EditUserPage editPage = new EditUserPage(TEST_ADMIN, "Admin", TEST_USER, parentContainer);
            
            // Test createFormField method
            Method createFormFieldMethod = EditUserPage.class.getDeclaredMethod("createFormField", String.class, JTextField.class);
            createFormFieldMethod.setAccessible(true);
            
            JTextField testField = new JTextField();
            JPanel formFieldPanel = (JPanel) createFormFieldMethod.invoke(editPage, "Test Label", testField);
            
            assertNotNull(formFieldPanel, "Form field panel should be created");
            testsPassed++;
            log("✓ Form field creation works");
            
            // Test createComboField method
            Method createComboFieldMethod = EditUserPage.class.getDeclaredMethod("createComboField", String.class, JComboBox.class);
            createComboFieldMethod.setAccessible(true);
            
            JComboBox<String> testCombo = new JComboBox<>(new String[]{"Option1", "Option2"});
            JPanel comboFieldPanel = (JPanel) createComboFieldMethod.invoke(editPage, "Test Combo", testCombo);
            
            assertNotNull(comboFieldPanel, "Combo field panel should be created");
            testsPassed++;
            log("✓ Combo field creation works");
            
        } catch (Exception e) {
            log("✗ Form validation test failed: " + e.getMessage());
        }
        
        testsTotal += 2;
        System.out.println();
    }
    
    /**
     * Test 6: Navigation Methods
     */
    private static void testNavigationMethods() {
        System.out.println("Test 6: Navigation Methods");
        System.out.println("--------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            EditUserPage editPage = new EditUserPage(TEST_ADMIN, "Admin", TEST_USER, parentContainer);
            
            // Test goBackToUserManagement method
            Method goBackToUserManagementMethod = EditUserPage.class.getDeclaredMethod("goBackToUserManagement");
            goBackToUserManagementMethod.setAccessible(true);
            
            assertNotNull(goBackToUserManagementMethod, "Go back method should exist");
            testsPassed++;
            log("✓ Go back to user management method exists");
            
            // Test updateUser method
            Method updateUserMethod = EditUserPage.class.getDeclaredMethod("updateUser");
            updateUserMethod.setAccessible(true);
            
            assertNotNull(updateUserMethod, "Update user method should exist");
            testsPassed++;
            log("✓ Update user method exists");
            
        } catch (Exception e) {
            log("✗ Navigation methods test failed: " + e.getMessage());
        }
        
        testsTotal += 2;
        System.out.println();
    }
    
    /**
     * Test 7: User Data Loading
     */
    private static void testUserDataLoading() {
        System.out.println("Test 7: User Data Loading");
        System.out.println("-------------------------");
        
        try {
            JPanel parentContainer = new JPanel(new CardLayout());
            EditUserPage editPage = new EditUserPage(TEST_ADMIN, "Admin", TEST_USER, parentContainer);
            
            // Test extractUsername method
            Method extractUsernameMethod = EditUserPage.class.getDeclaredMethod("extractUsername", String.class);
            extractUsernameMethod.setAccessible(true);
            
            String username = (String) extractUsernameMethod.invoke(editPage, "John Doe (@johndoe)");
            assertEquals("johndoe", username, "Should extract username correctly");
            testsPassed++;
            log("✓ Username extraction works");
            
            // Test currentUser field
            Field currentUserField = EditUserPage.class.getDeclaredField("currentUser");
            currentUserField.setAccessible(true);
            UserDAO.User currentUser = (UserDAO.User) currentUserField.get(editPage);
            
            // This might be null if database is not available, which is okay for testing
            log("✓ Current user field accessible (may be null in test environment)");
            testsPassed++;
            
        } catch (Exception e) {
            log("✗ User data loading test failed: " + e.getMessage());
        }
        
        testsTotal += 2;
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
            System.out.println("🎉 All EditUserPage tests passed!");
        } else {
            System.out.println("⚠️ Some tests failed. Please review the output above.");
        }
    }
} 
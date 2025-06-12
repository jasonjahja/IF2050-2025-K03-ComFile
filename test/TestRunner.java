package test;

import test.admin.AdminFunctionalityTest;
import test.admin.UserManagementTest;
import test.admin.EditUserPageTest;
import test.admin.AdminInterfaceComponentTest;
import test.integration.AdminWorkflowIntegrationTest;

/**
 * Main test runner for all admin functionality tests
 * 
 * This class runs comprehensive tests for the admin workflow including:
 * - Login with admin credentials (username: admincf, password: admin123)
 * - AdminDashboard functionality
 * - UserManagementDashboard operations (See More button)
 * - Delete user functionality (bin button)
 * - Edit user functionality (edit button)
 * - EditUserPage operations
 * - Complete workflow integration tests
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║             ADMIN FUNCTIONALITY TEST SUITE              ║");
        System.out.println("║          Testing Complete Admin Workflow                ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Set system properties for testing
            System.setProperty("java.awt.headless", "false");
            
            // Run all test suites
            runAllTests();
            
            long endTime = System.currentTimeMillis();
                         System.out.println("\n" + repeat("=", 60));
             System.out.println("TOTAL TEST EXECUTION TIME: " + (endTime - startTime) + "ms");
             System.out.println(repeat("=", 60));
            
            printTestingInstructions();
            
        } catch (Exception e) {
            System.out.println("Test runner failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Run all test suites
     */
    private static void runAllTests() {
        System.out.println("🚀 Starting Admin Functionality Tests...\n");
        
                 // Test Suite 1: Basic Admin Functionality
         System.out.println("📋 TEST SUITE 1: ADMIN FUNCTIONALITY");
         System.out.println(repeat("=", 50));
         AdminFunctionalityTest.main(new String[]{});
         
         System.out.println("\n" + repeat("─", 60) + "\n");
         
         // Test Suite 2: User Management
         System.out.println("📋 TEST SUITE 2: USER MANAGEMENT");
         System.out.println(repeat("=", 50));
         UserManagementTest.main(new String[]{});
         
         System.out.println("\n" + repeat("─", 60) + "\n");
         
         // Test Suite 3: Edit User Page
         System.out.println("📋 TEST SUITE 3: EDIT USER PAGE");
         System.out.println(repeat("=", 50));
         EditUserPageTest.main(new String[]{});
         
         System.out.println("\n" + repeat("─", 60) + "\n");
         
                  // Test Suite 4: Interface Components
         System.out.println("📋 TEST SUITE 4: INTERFACE COMPONENTS");
         System.out.println(repeat("=", 50));
         AdminInterfaceComponentTest.main(new String[]{});
         
         System.out.println("\n" + repeat("─", 60) + "\n");
         
         // Test Suite 5: Integration Tests
         System.out.println("📋 TEST SUITE 5: WORKFLOW INTEGRATION");
         System.out.println(repeat("=", 50));
         AdminWorkflowIntegrationTest.main(new String[]{});
         
         System.out.println("\n🎯 All test suites completed!");
    }
    
    /**
     * Print testing instructions
     */
    private static void printTestingInstructions() {
                 System.out.println("\n" + "╔" + repeat("═", 58) + "╗");
         System.out.println("║                    TESTING SUMMARY                      ║");
         System.out.println("╚" + repeat("═", 58) + "╝");
        
        System.out.println("\n🧪 AUTOMATED TESTS COMPLETED");
        System.out.println("The automated tests verify the following functionality:");
        System.out.println("• Login panel creation and initialization");
        System.out.println("• AdminDashboard component structure");
        System.out.println("• UserManagementDashboard navigation ('See More' button)");
        System.out.println("• User table generation and display");
                 System.out.println("• Delete user functionality (bin button logic)");
         System.out.println("• Edit user functionality (edit button logic)");
         System.out.println("• EditUserPage form components and navigation");
         System.out.println("• All interface components per specification");
         System.out.println("• Complete workflow integration");
        
                         System.out.println("\n" + repeat("═", 60));
         System.out.println("🎉 ADMIN FUNCTIONALITY TESTING COMPLETE");
         System.out.println(repeat("═", 60));
    }
    
    /**
     * Print ASCII art separator (helper method)
     */
    private static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
} 
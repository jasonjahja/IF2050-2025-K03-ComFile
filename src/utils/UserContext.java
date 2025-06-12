package utils;

import pages.ManageDocuments.Document;

/**
 * UserContext manages the current logged-in user throughout the application.
 * This singleton class provides a centralized way to access user information
 * and ensures consistent user state across all components.
 */
public class UserContext {
    private static UserContext instance;
    private Document.User currentUser;
    private String currentUsername;
    private String currentRole;

    private UserContext() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the singleton instance of UserContext
     * @return the UserContext instance
     */
    public static UserContext getInstance() {
        if (instance == null) {
            instance = new UserContext();
        }
        return instance;
    }

    /**
     * Sets the current logged-in user
     * @param username the username of the logged-in user
     * @param role the role of the logged-in user
     */
    public void setCurrentUser(String username, String role) {
        this.currentUsername = username;
        this.currentRole = role;
        
        // Load user from Document users map
        if (Document.users != null && Document.users.containsKey(username)) {
            this.currentUser = Document.users.get(username);
        } else {
            // Create a basic user object if not found in users map
            this.currentUser = new Document.User(username, username, role, "");
        }
        
        // Also set the legacy Document.currentUser for backward compatibility
        Document.currentUser = this.currentUser;
        
        System.out.println("✅ UserContext: Set current user: " + username + " | Role: " + role);
    }

    /**
     * Gets the current logged-in user
     * @return the current user, or null if no user is logged in
     */
    public Document.User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets the current username
     * @return the current username, or null if no user is logged in
     */
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Gets the current user role
     * @return the current user role, or null if no user is logged in
     */
    public String getCurrentRole() {
        return currentRole;
    }

    /**
     * Checks if a user is currently logged in
     * @return true if a user is logged in, false otherwise
     */
    public boolean isUserLoggedIn() {
        return currentUser != null && currentUsername != null;
    }

    /**
     * Checks if the current user has admin privileges
     * @return true if the current user is an admin, false otherwise
     */
    public boolean isCurrentUserAdmin() {
        return currentRole != null && currentRole.equalsIgnoreCase("Admin");
    }

    /**
     * Checks if the current user has manager privileges
     * @return true if the current user is a manager, false otherwise
     */
    public boolean isCurrentUserManager() {
        return currentRole != null && (currentRole.equalsIgnoreCase("Manager") || currentRole.equalsIgnoreCase("Manajer"));
    }

    /**
     * Gets the current user's department
     * @return the current user's department, or empty string if not available
     */
    public String getCurrentUserDepartment() {
        return currentUser != null ? currentUser.getDepartment() : "";
    }

    /**
     * Clears the current user context (logout)
     */
    public void clearCurrentUser() {
        this.currentUser = null;
        this.currentUsername = null;
        this.currentRole = null;
        Document.currentUser = null;
        System.out.println("✅ UserContext: Cleared current user (logout)");
    }

    /**
     * Refreshes the current user data from the database
     */
    public void refreshCurrentUser() {
        if (currentUsername != null) {
            // Reload users from database
            Document.users = DocumentDAO.loadAllUsers();
            
            // Update current user
            if (Document.users.containsKey(currentUsername)) {
                this.currentUser = Document.users.get(currentUsername);
                Document.currentUser = this.currentUser;
                System.out.println("✅ UserContext: Refreshed user data for: " + currentUsername);
            }
        }
    }
} 
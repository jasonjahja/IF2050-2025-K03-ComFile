package pages.ManageDocuments;

import utils.DBConnection;
import utils.DocumentDAO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class Document {

    // ================== USER ==================
    public static class User {
        public String id;
        public String name;
        public String role;
        public String department;

        @Deprecated
        public User(String id, String name, String role) {
            this(id, name, role, "");
        }

        public User(String id, String name, String role, String department) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.department = department;
        }

        public String getUsername() {
            return id;
        }

        public String getRole() {
            return role;
        }

        public String getFullName() {
            return name;
        }

        public String getDepartment() {
            return department;
        }

        public static User getUser(String username) {
            return users.get(username);
        }
    }

    public static Map<String, String> userPasswords = new HashMap<>();
    public static Map<String, String> userRoles = new HashMap<>();
    public static Map<String, User> users = new HashMap<>();

    public static User currentUser;

    // ================== AKSES ==================
    public static class AccessPermission {
        public User user;
        public String permission;

        public AccessPermission(User user, String permission) {
            this.user = user;
            this.permission = permission;
        }
    }

    // ================== DOKUMEN ==================
    public static class Doc {
        public String id;
        public String title;
        public String content;
        public User owner;
        public Date createdDate;
        public Date modifiedDate;
        public Date lastOpenedDate;
        public int pages;
        public String fileType;
        public int fileSizeKB;
        public String filePath;

        public List<AccessPermission> sharedWith = new ArrayList<>();
        public List<AccessPermission> accessPermissions = new ArrayList<>();
        public String generalAccessGroup = "";
        public String generalAccessRole = "";

        public Doc(String id, String title, String content, User owner, Date createdDate,
                   Date modifiedDate, Date lastOpenedDate, int pages,
                   String fileType, int fileSizeKB, String filePath) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.owner = owner;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
            this.lastOpenedDate = lastOpenedDate;
            this.pages = pages;
            this.fileType = fileType;
            this.fileSizeKB = fileSizeKB;
            this.filePath = filePath;
        }
    }

    public static List<Doc> documentList = new ArrayList<>();

    // ================== INISIALISASI ==================
    static {
        prepareUsers();
        currentUser = users.get("phoenixbaker"); // login user

        User owner = currentUser;

        // Path dokumen lokal
        String basePath = "/Users/audrazelvania/Downloads/";
        String[] filenames = {
                "K3C_DPPLOO01.pdf", "sinyal analog.png", "sampling_signal.png",
                "K2_G6_DPPLOO03.pdf", "Instagram Feeds.png", "K3C_DPPLOO06.pdf",
                "II3130_Milestone 4_K3_T10.pdf", "II2220_ITRiskMgt_18222106_Audra Zelvania.xlsx", "II2220_TugasDF_18222106_Gojek.xlsx",
                "urea.png"
        };

        for (int i = 0; i < filenames.length; i++) {
            File file = new File(basePath + filenames[i]);
            Date modified = file.exists() ? new Date(file.lastModified()) : new GregorianCalendar(2025, Calendar.MAY, 15).getTime();
            int size = file.exists() ? (int) (file.length() / 1024) : 250;

            String fileType = "";
            int dotIndex = filenames[i].lastIndexOf('.');
            if (dotIndex != -1 && dotIndex < filenames[i].length() - 1) {
                fileType = filenames[i].substring(dotIndex + 1).toUpperCase();
            }

            Document.Doc newDoc = new Document.Doc(
                    UUID.randomUUID().toString(),
                    file.getName(),
                    "Uploaded via UI",
                    Document.currentUser,
                    new Date(), new Date(), new Date(),
                    (i + 1) * 10,
                    fileType,
                    size,
                    file.getAbsolutePath()
            );

            newDoc.generalAccessGroup = "Restricted";
            newDoc.generalAccessRole = null;

            documentList.add(newDoc);
            //users.clear();
            //users.putAll(DocumentDAO.loadAllUsers());
        }
    }

    public static void prepareUsers() {
        users.clear();
        users.putAll(DocumentDAO.loadAllUsers());

        currentUser = users.get("phoenixbaker");
    }

    public static boolean validateLogin(String username, String password) {
        return userPasswords.containsKey(username) && userPasswords.get(username).equals(password);
    }

    public static User getUser(String username) {
        return users.get(username);
    }

    public static Doc getDocumentById(String id) {
        for (Doc doc : documentList) {
            if (doc.id.equals(id)) return doc;
        }
        return null;
    }

    public static boolean hasAccess(Doc doc, User user) {
        // Safety check for null parameters
        if (user == null) {
            System.err.println("hasAccess called with null user");
            return false;
        }
        
        if (doc == null) {
            System.err.println("hasAccess called with null document");
            return false;
        }
        
        // Admin has access to everything
        if (user.role != null && user.role.equalsIgnoreCase("Admin")) {
            return true;
        }

        // Document owner has access
        if (doc.owner != null && doc.owner.id.equals(user.id)) {
            return true;
        }

        // Check explicit sharing permissions
        if (doc.sharedWith != null) {
            for (AccessPermission ap : doc.sharedWith) {
                if (ap.user != null && ap.user.id.equals(user.id)) {
                    return true;
                }
            }
        }

        // Check access permissions
        if (doc.accessPermissions != null) {
            for (AccessPermission ap : doc.accessPermissions) {
                if (ap.user != null && ap.user.id.equals(user.id)) {
                    return true;
                }
            }
        }

        // Check group-based access
        if (doc.generalAccessGroup != null && doc.generalAccessRole != null && 
            !doc.generalAccessGroup.equals("Restricted")) {
            
            String userRole = user.role != null ? user.role.trim().toLowerCase() : "";
            String userDept = user.department != null ? user.department.trim().toLowerCase() : "";
            String group = doc.generalAccessGroup.trim().toLowerCase();
            
            // Check if "All Staff" access is granted
            if (group.equals("all staff")) {
                return true;
            }
            
            // Check role/department match
            if (group.equals(userRole) || group.equals(userDept)) {
                return true;
            }
        }

        return false;
    }

    public static boolean canEdit(Doc doc, User user) {
        if (doc.owner.id.equals(user.id)) return true;
        for (AccessPermission ap : doc.accessPermissions) {
            if (ap.user.id.equals(user.id) && ap.permission.equals("Editor")) return true;
        }
        return user.role.equals(doc.generalAccessGroup) && doc.generalAccessRole.equals("Editor");
    }
}
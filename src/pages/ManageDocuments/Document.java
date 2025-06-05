package pages.ManageDocuments;

import java.util.*;

public class Document {

    // ================== USER ==================
    public static class User {
        public String id;
        public String name;
        public String role;

        public User(String id, String name, String role) {
            this.id = id;
            this.name = name;
            this.role = role;
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

        public List<AccessPermission> sharedWith = new ArrayList<>();
        public String generalAccessGroup = "";
        public String generalAccessRole = "";

        public Doc(String id, String title, String content, User owner, Date createdDate, Date modifiedDate, Date lastOpenedDate, int pages, String fileType, int fileSizeKB) {
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
        }
    }

    public static List<Doc> documentList = new ArrayList<>();

    // ================== INISIALISASI ==================
    static {
        prepareUsers();

        // Simulasi user login
        currentUser = users.get("phoenixbaker"); // misal phoenixbaker login

        User michael = users.get("michaelscott");
        User lana = users.get("lanasteiner");
        User candice = users.get("candicewu");
        User olivia = users.get("oliviarhye");

        Date sampleDate = new GregorianCalendar(2025, Calendar.MAY, 15).getTime();

// === Document 1 ===
        Doc doc001 = new Doc("doc001", "K3C_DPPLOO02", "Ensure your data is safe and recoverable. ".repeat(83), michael, sampleDate, sampleDate, sampleDate, 83, "PDF", 484);
        doc001.sharedWith.add(new AccessPermission(users.get("phoenixbaker"), "Editor"));
        doc001.generalAccessGroup = "Karyawan";
        doc001.generalAccessRole = "Viewer";
        documentList.add(doc001);

// === Document 2 ===
        Doc doc002 = new Doc("doc002", "Document1", "Lorem ipsum dolor sit amet. ".repeat(64), lana, sampleDate, sampleDate, sampleDate, 64, "DOCX", 320);
        doc002.sharedWith.add(new AccessPermission(users.get("drewcano"), "Viewer"));
        documentList.add(doc002);

// === Document 3 ===
        Doc doc003 = new Doc("doc003", "Document2", "Company policy update Q1. ".repeat(40), lana, sampleDate, sampleDate, sampleDate,  40, "XLSX", 210);
        doc003.generalAccessGroup = "Manajer";
        doc003.generalAccessRole = "Editor";
        documentList.add(doc003);

// === Document 4 ===
        Doc doc004 = new Doc("doc004", "Project Plan", "Initial project plan and goals. ".repeat(48), candice, sampleDate, sampleDate, sampleDate, 48, "Image", 200);
        doc004.sharedWith.add(new AccessPermission(users.get("kevindarma"), "Editor"));
        documentList.add(doc004);

// === Document 5 ===
        Doc doc005 = new Doc("doc005", "Meeting Notes", "Meeting notes from 10 May. ".repeat(30), michael, sampleDate, sampleDate, sampleDate, 30, "PDF", 250);
        doc005.generalAccessGroup = "Karyawan";
        doc005.generalAccessRole = "Viewer";
        documentList.add(doc005);

// === Document 6 ===
        Doc doc006 = new Doc("doc006", "Client Brief", "Client needs and requests. ".repeat(30), olivia, sampleDate, sampleDate, sampleDate, 30, "XLSX", 340);
        doc006.sharedWith.add(new AccessPermission(users.get("andreawatson"), "Editor"));
        documentList.add(doc006);

// === Document 7 ===
        Doc doc007 = new Doc("doc007", "Training Module", "Employee onboarding materials. ".repeat(70), lana, sampleDate, sampleDate, sampleDate, 70, "DOCX", 414);
        doc007.generalAccessGroup = "Karyawan";
        doc007.generalAccessRole = "Editor";
        documentList.add(doc007);

// === Document 8 ===
        Doc doc008 = new Doc("doc008", "Security Protocol", "Updated security protocol v2. ".repeat(65), candice, sampleDate, sampleDate, sampleDate, 65, "PDF", 250);
        doc008.sharedWith.add(new AccessPermission(users.get("phoenixbaker"), "Viewer"));
        documentList.add(doc008);

// === Document 9 ===
        Doc doc009 = new Doc("doc009", "Survey Results", "Employee satisfaction survey results. ".repeat(55), michael, sampleDate, sampleDate, sampleDate, 55, "PDF", 250);
        doc009.generalAccessGroup = "Manajer";
        doc009.generalAccessRole = "Viewer";
        documentList.add(doc009);

// === Document 10 ===
        Doc doc010 = new Doc("doc010", "Design Draft", "UI design draft for client X. ".repeat(35), olivia, sampleDate, sampleDate, sampleDate, 35, "IMAGE", 250);
        doc010.sharedWith.add(new AccessPermission(users.get("jonathanchoi"), "Editor"));
        documentList.add(doc010);
    }

    public static void prepareUsers() {
        userPasswords.put("lanasteiner", "lana123");     userRoles.put("lanasteiner", "Manajer");
        userPasswords.put("candicewu", "candice123");     userRoles.put("candicewu", "Manajer");
        userPasswords.put("michaelscott", "michael123");  userRoles.put("michaelscott", "Manajer");
        userPasswords.put("andreawatson", "andrea123");   userRoles.put("andreawatson", "Manajer");
        userPasswords.put("jonathanchoi", "jonathan123"); userRoles.put("jonathanchoi", "Manajer");
        userPasswords.put("oliviarhye", "olivia123");     userRoles.put("oliviarhye", "Karyawan");
        userPasswords.put("phoenixbaker", "phoenix123");  userRoles.put("phoenixbaker", "Karyawan");
        userPasswords.put("drewcano", "drew123");          userRoles.put("drewcano", "Karyawan");
        userPasswords.put("saraperez", "sara123");         userRoles.put("saraperez", "Karyawan");
        userPasswords.put("kevindarma", "kevin123");       userRoles.put("kevindarma", "Karyawan");

        // Masukkan ke users map
        for (String username : userPasswords.keySet()) {
            String role = userRoles.get(username);
            users.put(username, new User(username, username, role));
        }
    }

    // ================== UTIL FUNGSIONALITAS ==================

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
        if (doc.owner.id.equals(user.id)) return true;

        for (AccessPermission ap : doc.sharedWith) {
            if (ap.user.id.equals(user.id)) return true;
        }

        if (user.role.equals(doc.generalAccessGroup)) {
            return doc.generalAccessRole.equals("Viewer") || doc.generalAccessRole.equals("Editor");
        }

        return false;
    }

    public static boolean canEdit(Doc doc, User user) {
        if (doc.owner.id.equals(user.id)) return true;

        for (AccessPermission ap : doc.sharedWith) {
            if (ap.user.id.equals(user.id) && ap.permission.equals("Editor")) return true;
        }

        return user.role.equals(doc.generalAccessGroup) &&
                doc.generalAccessRole.equals("Editor");
    }
}
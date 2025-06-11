package pages.ManageDocuments;

import java.io.File;
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
        public String filePath;

        public List<AccessPermission> sharedWith = new ArrayList<>();
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

        User michael = users.get("michaelscott");

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

            Doc doc = new Doc(
                    "doc" + String.format("%03d", i + 1),
                    filenames[i].substring(0, dotIndex),
                    "This is a placeholder content for testing.",
                    michael,
                    modified, modified, modified,
                    (i + 1) * 10,
                    fileType,
                    size,
                    file.getAbsolutePath()
            );

            documentList.add(doc);
        }
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

        for (String username : userPasswords.keySet()) {
            String role = userRoles.get(username);
            users.put(username, new User(username, username, role));
        }
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
        return user.role.equals(doc.generalAccessGroup) && doc.generalAccessRole.equals("Editor");
    }
}
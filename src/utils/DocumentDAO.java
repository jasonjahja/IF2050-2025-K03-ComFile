package utils;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import utils.DBConnection;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import pages.ManageDocuments.Document.User;

import pages.ManageDocuments.Document;
import pages.ManageDocuments.Document.Doc;

public class DocumentDAO {

    public static void saveDocToDatabase(Doc doc) {
        String checkQuery = "SELECT 1 FROM documents WHERE id = ?";
        String insertQuery = "INSERT INTO documents (id, title, content, owner_id, created_date, modified_date, last_opened_date, pages, file_type, file_size_kb, file_path, general_access_group, general_access_role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect()) {

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, doc.id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Skip insert, sudah ada: " + doc.title);
                    return;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, doc.id);
                stmt.setString(2, doc.title);
                stmt.setString(3, doc.content);
                stmt.setString(4, doc.owner != null ? doc.owner.id : null);
                stmt.setTimestamp(5, new Timestamp(doc.createdDate.getTime()));
                stmt.setTimestamp(6, new Timestamp(doc.modifiedDate.getTime()));
                stmt.setTimestamp(7, new Timestamp(doc.lastOpenedDate.getTime()));
                stmt.setInt(8, doc.pages);
                stmt.setString(9, doc.fileType);
                stmt.setInt(10, doc.fileSizeKB);
                stmt.setString(11, doc.filePath);
                stmt.setString(12, doc.generalAccessGroup);
                stmt.setString(13, doc.generalAccessRole);

                stmt.executeUpdate();
                System.out.println("Berhasil simpan dokumen ke database: " + doc.title);
            }

        } catch (Exception e) {
            System.err.println("Gagal menyimpan dokumen: " + doc.title);
            e.printStackTrace();
        }
    }

    public static Map<String, List<Document.AccessPermission>> getAllSharedAccess() {
        Map<String, List<Document.AccessPermission>> accessMap = new HashMap<>();

        String query = "SELECT doc_id, username, role FROM document_access";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String docId = rs.getString("doc_id");
                String username = rs.getString("username");
                String role = rs.getString("role");

                Document.User user = Document.getUser(username);
                if (user == null) continue;

                accessMap.computeIfAbsent(docId, k -> new ArrayList<>())
                        .add(new Document.AccessPermission(user, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accessMap;
    }

    public static List<Document.Doc> getAllDocumentsFromDB() {
        List<Document.Doc> documents = new ArrayList<>();

        if (Document.users == null || Document.users.isEmpty()) {
            Document.users.putAll(loadAllUsers());
        }

        String query = "SELECT * FROM documents";
        Map<String, List<Document.AccessPermission>> accessMap = getAllSharedAccess();
        
        UserContext userContext = UserContext.getInstance();
        Document.User currentUser = userContext.getCurrentUser();
        
        System.out.println("🔍 Getting documents for user: " + 
            (currentUser != null ? currentUser.id + " (Role: " + currentUser.role + ")" : "null"));

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Check if current user is admin - admins see all documents
            boolean isAdmin = currentUser != null && "Admin".equalsIgnoreCase(currentUser.role);
            if (isAdmin) {
                System.out.println("👑 User is ADMIN - will see ALL documents");
            }

            int totalDocs = 0;
            int accessibleDocs = 0;

            while (rs.next()) {
                totalDocs++;
                String id = rs.getString("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String ownerId = rs.getString("owner_id");
                Date createdDate = new Date(rs.getTimestamp("created_date").getTime());
                Date modifiedDate = new Date(rs.getTimestamp("modified_date").getTime());
                Date lastOpenedDate = new Date(rs.getTimestamp("last_opened_date").getTime());
                int pages = rs.getInt("pages");
                String fileType = rs.getString("file_type");
                int fileSizeKB = rs.getInt("file_size_kb");
                String filePath = rs.getString("file_path");

                Document.User owner = Document.users.get(ownerId);

                Document.Doc doc = new Document.Doc(
                        id, title, content, owner,
                        createdDate, modifiedDate, lastOpenedDate,
                        pages, fileType, fileSizeKB, filePath
                );

                String rawGroup = rs.getString("general_access_group");
                String rawRole = rs.getString("general_access_role");

                doc.generalAccessGroup = (rawGroup == null || rawGroup.trim().isEmpty()) ? "Restricted" : rawGroup;
                doc.generalAccessRole = (rawRole != null && !rawRole.trim().isEmpty()) ? rawRole : null;

                doc.sharedWith = accessMap.getOrDefault(id, new ArrayList<>());
                doc.accessPermissions = new ArrayList<>(doc.sharedWith);

                // For admin users, add all documents without filtering
                if (isAdmin) {
                    documents.add(doc);
                    accessibleDocs++;
                    continue;
                }
                
                // For non-admin users, apply access control filters
                boolean isOwner = currentUser != null && owner != null && currentUser.id.equals(owner.id);
                boolean hasSharedAccess = doc.accessPermissions.stream()
                    .anyMatch(p -> p.user != null && p.user.id.equals(currentUser.id));
                boolean hasGeneralAccess = currentUser != null &&
                        doc.generalAccessRole != null &&
                        doc.generalAccessGroup != null &&
                        !doc.generalAccessGroup.equals("Restricted") &&
                        (doc.generalAccessGroup.equals(currentUser.department) || 
                         doc.generalAccessGroup.equalsIgnoreCase("All Staff") ||
                         doc.generalAccessGroup.equalsIgnoreCase(currentUser.role));

                if (isOwner || hasSharedAccess || hasGeneralAccess) {
                    documents.add(doc);
                    accessibleDocs++;
                }
            }
            
            System.out.println("📊 Documents: Total=" + totalDocs + ", Accessible=" + accessibleDocs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return documents;
    }

    public static void saveUser(String username, String fullName, String role, String department) {
        String query = "INSERT INTO users (username, full_name, role, department) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (username) DO UPDATE SET full_name = EXCLUDED.full_name, role = EXCLUDED.role, department = EXCLUDED.department";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, fullName);
            stmt.setString(3, role);
            stmt.setString(4, department);
            stmt.executeUpdate();

            Document.User fromDB = getUserFromDB(username);
            if (fromDB != null) {
                Document.users.put(username, fromDB);
            }
            System.out.println("saveUser updated cache: " + username + " | dept=" + department);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDocByTitle(String title) {
        String query = "DELETE FROM documents WHERE title = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDocumentById(String id) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "DELETE FROM documents WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Document.User> loadAllUsers() {
        Map<String, Document.User> map = new HashMap<>();
        String query = "SELECT username, full_name, role, department FROM users";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("username");
                String name = rs.getString("full_name");
                String role = rs.getString("role");
                String department = rs.getString("department");

                System.out.println("👤 Loading user: " + id + " | Role: " + role + " | Dept: " + department);

                map.put(id, new Document.User(id, name, role, department));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static void addAccessToDoc(String docId, String username, String role) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "INSERT INTO document_access (id, doc_id, username, role) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, docId);
            stmt.setString(3, username);
            stmt.setString(4, role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getAllDepartments() {
        Set<String> departments = new HashSet<>();
        String query = "SELECT DISTINCT department FROM users";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String dept = rs.getString("department");
                if (dept != null && !dept.isEmpty()) {
                    departments.add(dept);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return departments;
    }

    public static List<Document.AccessPermission> getSharedAccessForDoc(String docId) {
        List<Document.AccessPermission> accessList = new ArrayList<>();
        String query = "SELECT username, role FROM document_access WHERE doc_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, docId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                String role = rs.getString("role");
                Document.User user = Document.getUser(username);
                if (user != null) {
                    accessList.add(new Document.AccessPermission(user, role));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accessList;
    }

    public static void updateGeneralAccess(String docId, String group, String role) {
        String query = "UPDATE documents SET general_access_group = ?, general_access_role = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, group);
            stmt.setString(2, role);
            stmt.setString(3, docId);
            stmt.executeUpdate();
            System.out.println("General access updated in DB for doc: " + docId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getAllRoles() {
        Set<String> roles = new HashSet<>();
        String query = "SELECT DISTINCT role FROM users";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String role = rs.getString("role");
                if (role != null && !role.isEmpty()) {
                    roles.add(role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public static User getUserFromDB(String username) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Document.User(
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getString("department")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeAccessFromDoc(String docId, String username) {
        String query = "DELETE FROM document_access WHERE doc_id = ? AND username = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, docId);
            stmt.setString(2, username);
            stmt.executeUpdate();
            System.out.println("Access removed in DB for user: " + username + ", doc: " + docId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
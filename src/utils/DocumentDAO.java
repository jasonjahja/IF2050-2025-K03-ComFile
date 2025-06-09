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

import pages.ManageDocuments.Document;
import pages.ManageDocuments.Document.Doc;

public class DocumentDAO {

    // Simpan dokumen ke database, skip jika sudah ada berdasarkan ID
    public static void saveDocToDatabase(Doc doc) {
        String checkQuery = "SELECT 1 FROM documents WHERE id = ?";
        String insertQuery = "INSERT INTO documents (id, title, content, owner_id, created_date, modified_date, last_opened_date, pages, file_type, file_size_kb, file_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect()) {

            // Cek apakah dokumen sudah ada
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, doc.id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("⏩ Skip insert, sudah ada: " + doc.title);
                    return;
                }
            }

            // Simpan dokumen baru
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

                stmt.executeUpdate();
                System.out.println("✅ Berhasil simpan dokumen ke database: " + doc.title);
            }

        } catch (Exception e) {
            System.err.println("❌ Gagal menyimpan dokumen: " + doc.title);
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

        String query = "SELECT * FROM documents";

        Map<String, List<Document.AccessPermission>> accessMap = getAllSharedAccess();

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
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

                doc.generalAccessGroup = rs.getString("general_access_group");
                doc.generalAccessRole = rs.getString("general_access_role");

                // Gunakan shared access yang sudah di-load semua sebelumnya
                doc.sharedWith = accessMap.getOrDefault(id, new ArrayList<>());
                doc.accessPermissions = new ArrayList<>(doc.sharedWith);

                documents.add(doc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return documents;
    }

    public static void saveUser(String username, String fullName, String role) {
        String query = "INSERT INTO users (username, full_name, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, fullName);
            stmt.setString(3, role);
            stmt.executeUpdate();

            // update cache juga
            Document.users.put(username, new Document.User(username, fullName, role));
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
            System.out.println("✅ General access updated in DB for doc: " + docId);
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

}
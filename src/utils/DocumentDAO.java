package utils;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

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

    // Ambil semua dokumen dari database
    public static List<Doc> getAllDocumentsFromDB() {
        List<Doc> documents = new ArrayList<>();
        String query = "SELECT * FROM documents";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Doc doc = new Doc(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        Document.getUser(rs.getString("owner_id")),
                        rs.getTimestamp("created_date"),
                        rs.getTimestamp("modified_date"),
                        rs.getTimestamp("last_opened_date"),
                        rs.getInt("pages"),
                        rs.getString("file_type"),
                        rs.getInt("file_size_kb"),
                        rs.getString("file_path")
                );
                System.out.println("📄 Found doc from DB: " + doc.title); // ⬅️ debug log
                documents.add(doc);
            }

        } catch (Exception e) {
            System.err.println("❌ Gagal mengambil dokumen dari database");
            e.printStackTrace();
        }

        return documents;
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

}
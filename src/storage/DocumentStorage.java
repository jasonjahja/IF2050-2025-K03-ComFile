package storage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class DocumentStorage {
    // List static yang akan menyimpan dokumen hasil upload
    public static List<File> uploadedDocuments = new ArrayList<>();
    
    // Directory untuk menyimpan file yang diupload
    private static final String UPLOAD_DIR = "uploads";
    
    // Inisialisasi directory uploads jika belum ada
    static {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }
            // Load existing documents
            File[] existingFiles = uploadPath.toFile().listFiles();
            if (existingFiles != null) {
                for (File file : existingFiles) {
                    if (file.isFile()) {
                        uploadedDocuments.add(file);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating upload directory: " + e.getMessage());
        }
    }

    // Method untuk menyimpan file yang diupload
    public static boolean storeDocument(File sourceFile) {
        try {
            // Buat path tujuan dengan nama file yang sama
            Path targetPath = Paths.get(UPLOAD_DIR, sourceFile.getName());
            
            // Copy file ke directory uploads
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // Tambahkan file yang sudah disimpan ke list
            File storedFile = targetPath.toFile();
            uploadedDocuments.add(storedFile);
            
            return true;
        } catch (IOException e) {
            System.err.println("Error storing document: " + e.getMessage());
            return false;
        }
    }

    // Method untuk mendapatkan semua dokumen yang sudah diupload
    public static List<File> getAllDocuments() {
        return new ArrayList<>(uploadedDocuments);
    }

    // Method untuk menghapus dokumen
    public static boolean deleteDocument(File file) {
        try {
            if (file.delete()) {
                uploadedDocuments.remove(file);
                return true;
            }
            return false;
        } catch (SecurityException e) {
            System.err.println("Error deleting document: " + e.getMessage());
            return false;
        }
    }

    // Method untuk mengecek apakah file sudah ada di storage
    public static boolean isDocumentExists(String fileName) {
        return uploadedDocuments.stream()
                .anyMatch(file -> file.getName().equals(fileName));
    }

    // Method untuk mendapatkan ukuran file dalam format yang readable
    public static String getReadableFileSize(File file) {
        long size = file.length();
        if (size <= 0) return "0 B";
        
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        
        return String.format("%.1f %s", 
            size / Math.pow(1024, digitGroups), 
            units[digitGroups]);
    }
}

package utils;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection connect() {
        try {
            String url = "jdbc:postgresql://ep-shy-leaf-a8yseowb-pooler.eastus2.azure.neon.tech/neondb?sslmode=require";
            String user = "neondb_owner";
            String password = "npg_5xOmPolVg8hv";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("➡️ DBConnection.connect() called from: " + Thread.currentThread().getStackTrace()[2]);
            return conn;
        } catch (Exception e) {
            System.err.println("❌ Gagal connect ke database:");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        if (connect() != null) {
            System.out.println("✅ Koneksi ke database berhasil!");
        } else {
            System.out.println("❌ Gagal konek ke database!");
        }
    }
}
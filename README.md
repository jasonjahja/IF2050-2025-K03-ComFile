# 📋 Sistem Manajemen Dokumen

## Penjelasan Singkat Aplikasi

Sistem Manajemen Dokumen adalah aplikasi desktop berbasis Java Swing yang dirancang untuk mengelola dokumen secara efisien dengan fitur-fitur berikut:

- **Autentikasi User**: Sistem login dengan role Manager dan Karyawan
- **Manajemen Dokumen**: Upload, download, dan organisasi file
- **Kontrol Akses**: Pembagian akses dokumen berdasarkan peran
- **Database Cloud**: Menggunakan PostgreSQL yang di-host di Neon
- **UI Modern**: Interface yang user-friendly dengan Java Swing

## 🚀 Cara Menjalankan Aplikasi

### Metode 1: Menggunakan Make (Direkomendasikan)

```bash
# Clone repository
git clone <repository-url>
cd document-management-system

# Lihat informasi lengkap aplikasi
make info

# Jalankan langsung
make run

# Atau buat JAR file dulu
make build
java -jar dist/document-management-system.jar
```

### Metode 2: Manual Build

```bash
# Kompilasi source code
mkdir -p out/production
javac -cp "lib/*" -d out/production $(find src -name "*.java")

# Jalankan aplikasi
java -cp "lib/*:out/production" MainApplication
```

### Metode 3: CI/CD (GitHub Actions)

1. Push code ke branch `main` atau `develop`
2. Pipeline akan otomatis berjalan
3. Download JAR file dari Artifacts atau Releases

## 📦 Daftar Modul yang Diimplementasi

### 1. **Modul Autentikasi User**
- **Pembagian Tugas**: Login/Logout system, role management
- **File Utama**: `src/pages/ManageDocuments/Document.java`
- **Fitur**: 
  - Login dengan username/password
  - Role-based access (Manager/Karyawan)
  - Session management

### 2. **Modul Manajemen Dokumen**
- **Pembagian Tugas**: Upload, download, delete dokumen
- **File Utama**: `src/pages/ManageDocuments/MyDocuments.java`
- **Fitur**:
  - Upload file (PDF, DOCX, XLSX, gambar)
  - Download dan buka dokumen
  - Hapus dokumen dengan konfirmasi

### 3. **Modul UI Components**
- **Pembagian Tugas**: Navigation bar, search bar, filter
- **File Utama**: 
  - `src/components/NavigationBar.java`
  - `src/components/SearchBar.java`
  - `src/components/Filter.java`
- **Fitur**:
  - Navigation menu responsif
  - Search dan filter dokumen
  - UI components yang reusable

### 4. **Modul Kontrol Akses**
- **Pembagian Tugas**: Permission management, sharing
- **File Utama**: `src/components/AccessControl.java`
- **Fitur**:
  - Sharing dokumen antar user
  - Permission levels (read, write, admin)
  - Access control matrix

### 5. **Modul Database Connection**
- **Pembagian Tugas**: Database connectivity, DAO pattern
- **File Utama**: 
  - `src/utils/DBConnection.java`
  - `src/utils/DocumentDAO.java`
  - `src/utils/UserDAO.java`
- **Fitur**:
  - Koneksi ke Neon PostgreSQL
  - CRUD operations
  - Transaction management

## 🗄️ Daftar Tabel Basis Data

### 1. **Tabel `users`**
| Atribut | Tipe Data | Keterangan |
|---------|-----------|------------|
| `id` | SERIAL PRIMARY KEY | ID unik user |
| `username` | VARCHAR(50) UNIQUE | Username untuk login |
| `password_hash` | VARCHAR(255) | Password yang sudah di-hash |
| `full_name` | VARCHAR(100) | Nama lengkap user |
| `role` | VARCHAR(20) | Role: 'Manajer' atau 'Karyawan' |
| `created_at` | TIMESTAMP | Waktu pembuatan akun |
| `updated_at` | TIMESTAMP | Waktu update terakhir |

### 2. **Tabel `documents`**
| Atribut | Tipe Data | Keterangan |
|---------|-----------|------------|
| `id` | SERIAL PRIMARY KEY | ID unik dokumen |
| `title` | VARCHAR(255) | Judul dokumen |
| `filename` | VARCHAR(255) | Nama file asli |
| `file_path` | VARCHAR(500) | Path penyimpanan file |
| `file_size` | BIGINT | Ukuran file dalam bytes |
| `mime_type` | VARCHAR(100) | Tipe MIME file |
| `owner_id` | INTEGER | ID pemilik (FK ke users) |
| `created_at` | TIMESTAMP | Waktu upload |
| `updated_at` | TIMESTAMP | Waktu update terakhir |

### 3. **Tabel `document_access`**
| Atribut | Tipe Data | Keterangan |
|---------|-----------|------------|
| `id` | SERIAL PRIMARY KEY | ID unik permission |
| `document_id` | INTEGER | ID dokumen (FK) |
| `user_id` | INTEGER | ID user yang diberi akses (FK) |
| `permission_level` | VARCHAR(20) | Level akses: 'read', 'write', 'admin' |
| `granted_by` | INTEGER | ID user yang memberikan akses (FK) |
| `granted_at` | TIMESTAMP | Waktu pemberian akses |

### 4. **Tabel `document_versions`**
| Atribut | Tipe Data | Keterangan |
|---------|-----------|------------|
| `id` | SERIAL PRIMARY KEY | ID unik versi |
| `document_id` | INTEGER | ID dokumen (FK) |
| `version_number` | INTEGER | Nomor versi |
| `file_path` | VARCHAR(500) | Path file versi ini |
| `file_size` | BIGINT | Ukuran file versi |
| `created_by` | INTEGER | ID user pembuat versi (FK) |
| `created_at` | TIMESTAMP | Waktu pembuatan versi |

### 5. **Tabel `audit_log`**
| Atribut | Tipe Data | Keterangan |
|---------|-----------|------------|
| `id` | SERIAL PRIMARY KEY | ID unik log |
| `user_id` | INTEGER | ID user yang melakukan aksi (FK) |
| `action` | VARCHAR(50) | Jenis aksi (CREATE, READ, UPDATE, DELETE) |
| `entity_type` | VARCHAR(50) | Tipe entitas (DOCUMENT, USER, ACCESS) |
| `entity_id` | INTEGER | ID entitas yang diaksi |
| `description` | TEXT | Deskripsi detail aksi |
| `ip_address` | INET | IP address user |
| `user_agent` | TEXT | Browser/client info |
| `created_at` | TIMESTAMP | Waktu aksi |

## 🔄 CI/CD Pipeline (Tanpa Docker)

### Cara Menjalankan CI/CD:

1. **Push ke Repository**:
   ```bash
   git add .
   git commit -m "Update aplikasi"
   git push origin main
   ```

2. **Pipeline Otomatis Berjalan**:
   - ✅ Test compilation
   - ✅ Build JAR file
   - ✅ Security scan
   - ✅ Create release (jika di main branch)

3. **Download Hasil**:
   - JAR file dari GitHub Releases
   - Atau dari Artifacts di Actions tab

### Pipeline Stages:

| Stage | Deskripsi | Output |
|-------|-----------|--------|
| **Test** | Kompilasi dan validasi code | Test reports |
| **Build** | Buat executable JAR file | JAR artifact |
| **Security** | Scan keamanan dependencies | Security report |
| **Release** | Buat release otomatis | GitHub release |

## 🛠️ Requirements

- **Java 17** atau lebih tinggi
- **Koneksi Internet** (untuk database Neon)
- **Make** (opsional, untuk build automation)

## 👤 User Testing

### Manager Users:
```
Username: lanasteiner    | Password: lana123
Username: candicewu      | Password: candice123
Username: michaelscott   | Password: michael123
```

### Karyawan Users:
```
Username: oliviarhye     | Password: olivia123
Username: phoenixbaker   | Password: phoenix123
Username: drewcano       | Password: drew123
```

## 📋 Perintah Make yang Tersedia

```bash
make help          # Tampilkan bantuan
make info          # Informasi lengkap aplikasi
make run           # Jalankan aplikasi langsung
make build         # Buat file JAR
make test          # Jalankan pengujian
make clean         # Bersihkan file build
make demo          # Tampilkan cara demo aplikasi
make ci            # Pipeline CI/CD lokal
```

## 🔧 Troubleshooting

### Error: "Could not find or load main class"
```bash
# Pastikan kompilasi berhasil
make clean compile

# Atau cek classpath
java -cp "lib/*:out/production" MainApplication
```

### Error: Database Connection
```bash
# Cek koneksi internet
ping google.com

# Cek database credentials di src/utils/DBConnection.java
```

### Error: GUI tidak muncul
```bash
# Pastikan DISPLAY environment variable ter-set (Linux)
export DISPLAY=:0

# Atau jalankan dengan virtual display
Xvfb :99 -screen 0 1024x768x24 &
export DISPLAY=:99
```

## 📞 Support

Untuk bantuan dan pertanyaan:
1. Cek [Issues](../../issues) di GitHub
2. Review pipeline logs di [Actions](../../actions)
3. Konsultasi dokumentasi Make: `make help`

---

**Dibuat dengan ❤️ menggunakan Java Swing dan PostgreSQL** 
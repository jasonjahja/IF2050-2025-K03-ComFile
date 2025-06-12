# 📋 ComFile - Sistem Manajemen Dokumen

## 👥 Informasi Kelompok

**Kelompok K3C**

| Nama | NIM |
|------|-----|
| Audra Zelvania P. H. | 18222106 |
| Angelica Aliwinata | 18222113 |
| Jason Jahja | 18222116 |
| Sekar Anindita N. | 18222125 |
| Anindita Widya S. | 18222128 |

## ⚡ Quick Start

### **🖥️ Windows**
```powershell
git clone https://github.com/jasonjahja/IF2050-2025-K03-ComFile.git
cd IF2050-2025-K03-ComFile
.\build.bat
.\run.bat
```

### **🐧 Linux/macOS**
```bash
git clone https://github.com/jasonjahja/IF2050-2025-K03-ComFile.git
cd IF2050-2025-K03-ComFile
make build-fat
java -jar dist/document-management-system-fat.jar
```

### **👤 Login Testing**
- **Admin:** `admincf / admin123`
- **Manager:** `lanasteiner / lana123`
- **Karyawan:** `oliviarhye / olivia123`

---

## 📖 Penjelasan Singkat Aplikasi

ComFile adalah aplikasi desktop berbasis Java Swing yang dirancang untuk mengelola dokumen secara efisien dengan fitur-fitur berikut:

- **Autentikasi Pengguna**: Sistem login dengan peran Admin, Manager, dan Karyawan
- **Manajemen Akun**: Sistem pengelolaan pengguna yang dimiliki oleh Admin
- **Manajemen Dokumen**: Upload, download, dan organisasi berkas
- **Kontrol Akses**: Pembagian akses dokumen berdasarkan peran pengguna
- **Database Cloud**: Menggunakan PostgreSQL yang di-host di Neon
- **Antarmuka Modern**: UI yang user-friendly dengan Java Swing

## 🚀 Cara Menjalankan Aplikasi

### ⚠️ **Perbedaan Platform**

| Platform | Build System | Path Separator | Contoh Command |
|----------|--------------|----------------|----------------|
| **Windows** | Batch Scripts | `;` (semicolon) | `.\build.bat` |
| **Linux/Unix/macOS** | Makefile | `:` (colon) | `make build` |

---

### **🖥️ Windows (Direkomendasikan)**

```powershell
# Clone repository
git clone https://github.com/jasonjahja/IF2050-2025-K03-ComFile.git
cd IF2050-2025-K03-ComFile

# Kompilasi dan buat JAR file (dengan Fat JAR untuk database)
.\build.bat

# Jalankan aplikasi (otomatis gunakan Fat JAR)
.\run.bat
```

**Output yang Dihasilkan:**
- `dist\document-management-system.jar` (regular JAR)
- `dist\document-management-system-fat.jar` (dengan dependencies)

---

### **🐧 Linux/Unix/macOS**

```bash
# Clone repository
git clone https://github.com/jasonjahja/IF2050-2025-K03-ComFile.git
cd IF2050-2025-K03-ComFile

# Lihat informasi lengkap aplikasi
make info

# Jalankan langsung
make run

# Atau buat JAR file dulu
make build              # Regular JAR
make build-fat          # Fat JAR (dengan semua dependencies)

# Jalankan JAR
java -jar dist/document-management-system-fat.jar
```

---

### **🔧 Manual Build (Advanced)**

```bash
# Kompilasi source code
mkdir -p out/production
javac -cp "lib/*" -d out/production $(find src -name "*.java")

# Jalankan aplikasi
java -cp "lib/*:out/production" main.MainApplication
```

---

### **📚 Database Connectivity Issue Fix**

Jika mengalami error database:
```
"No suitable driver found for jdbc:postgresql://..."
```

**Solusi:**
1. **Windows:** `.\build.bat` otomatis membuat Fat JAR
2. **Linux/macOS:** `make build-fat`
3. Gunakan Fat JAR yang sudah include PostgreSQL driver

## 📦 Daftar Modul yang Diimplementasi

### 1. **Modul Autentikasi Pengguna**
- **Pembagian Tugas**: Sistem Login/Logout, manajemen peran pengguna
- **File Utama**: `src/pages/Login.java`, `src/pages/ManageDocuments/Document.java`
- **Fitur**: 
  - Login dengan username/password
  - Role-based access (Admin/Manager/Karyawan)
  - Session management

### 2. **Modul Manajemen Dokumen**
- **Pembagian Tugas**: Upload, download, delete dokumen
- **File Utama**: `src/pages/ManageDocuments/MyDocuments.java`
- **Fitur**:
  - Upload file (PDF, DOCX, XLSX, gambar)
  - Download dan buka dokumen
  - Hapus dokumen dengan konfirmasi

### 3. **Modul Komponen Antarmuka**
- **Pembagian Tugas**: Navigation bar, search bar, filter
- **File Utama**: 
  - `src/components/NavigationBar.java`
  - `src/components/SearchBar.java`
  - `src/components/Filter.java`
- **Fitur**:
  - Menu navigasi responsif
  - Pencarian dan filter dokumen
  - Komponen UI yang dapat digunakan kembali

### 4. **Modul Kontrol Akses**
- **Pembagian Tugas**: Manajemen perizinan, berbagi dokumen
- **File Utama**: `src/components/AccessControl.java`
- **Fitur**:
  - Berbagi dokumen antar pengguna
  - Tingkat perizinan (read, write, admin)
  - Matriks kontrol akses

### 5. **Modul Manajemen Admin**
- **Pembagian Tugas**: Pengelolaan pengguna sistem
- **File Utama**: 
  - `src/pages/Admin/AdminDashboard.java`
  - `src/pages/Admin/UserManagementDashboard.java`
  - `src/pages/Admin/AddUserPage.java`
  - `src/pages/Admin/EditUserPage.java`
- **Fitur**:
  - Dashboard admin
  - Manajemen pengguna (CRUD)
  - Monitoring aktivitas sistem

### 6. **Modul Koneksi Database**
- **Pembagian Tugas**: Konektivitas database, pola DAO
- **File Utama**: 
  - `src/utils/DBConnection.java`
  - `src/utils/DocumentDAO.java`
  - `src/utils/UserDAO.java`
- **Fitur**:
  - Koneksi ke Neon PostgreSQL
  - Operasi CRUD
  - Manajemen transaksi

## 🗄️ Daftar Tabel Basis Data

### 1. **Tabel `users`**
| Atribut | Tipe Data | Keterangan |
|---------|-----------|------------|
| `id` | SERIAL PRIMARY KEY | ID unik user |
| `username` | VARCHAR(50) UNIQUE | Username untuk login |
| `password_hash` | VARCHAR(255) | Password yang sudah di-hash |
| `full_name` | VARCHAR(100) | Nama lengkap user |
| `role` | VARCHAR(20) | Peran: 'Admin', 'Manajer', atau 'Karyawan' |
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

### **📋 Wajib**
- **Java 17** atau lebih tinggi
- **Koneksi Internet** (untuk database Neon PostgreSQL)

### **📋 Opsional (Sesuai Platform)**

| Platform | Build Tool | Install Command |
|----------|------------|-----------------|
| **Windows** | Batch Scripts | ✅ Sudah tersedia (`build.bat`, `run.bat`) |
| **Linux/Ubuntu** | Make | `sudo apt install make` |
| **macOS** | Make | `brew install make` |

### **📚 Dependencies (Auto-included)**
- `postgresql-42.7.2.jar` - PostgreSQL JDBC Driver
- Java Swing (built-in JDK)
- Semua dependencies dikemas dalam Fat JAR

## 👤 Akun Pengujian

### Admin
```
Username: admincf        | Password: admin123
```

### Manager
```
Username: lanasteiner    | Password: lana123
Username: candicewu      | Password: candice123
Username: michaelscott   | Password: michael123
```

### Karyawan
```
Username: oliviarhye     | Password: olivia123
Username: phoenixbaker   | Password: phoenix123
Username: drewcano       | Password: drew123
```

## 📋 Perintah Build yang Tersedia

### **🖥️ Windows Commands**

```powershell
.\build.bat        # Kompilasi + buat JAR (regular + fat)
.\run.bat          # Jalankan aplikasi (prioritas Fat JAR)
```

### **🐧 Linux/Unix/macOS Commands**

```bash
make help          # Tampilkan bantuan lengkap
make info          # Informasi aplikasi dan platform
make run           # Jalankan aplikasi langsung
make build         # Buat regular JAR (dengan external deps)
make build-fat     # Buat Fat JAR (semua deps ter-include)
make test          # Jalankan pengujian
make clean         # Bersihkan file build
make demo          # Tampilkan cara demo aplikasi
make ci            # Pipeline CI/CD lokal
```

## 🔧 Pemecahan Masalah

### **❌ Error: "Could not find or load main class"**

**Windows:**
```powershell
# Rebuild aplikasi
.\build.bat

# Jalankan ulang
.\run.bat
```

**Linux/macOS:**
```bash
# Pastikan kompilasi berhasil
make clean compile

# Atau periksa classpath
java -cp "lib/*:out/production" main.MainApplication
```

---

### **❌ Error: "No suitable driver found for jdbc:postgresql"**

**Solusi (Database Driver Issue):**

**Windows:**
```powershell
# build.bat otomatis membuat Fat JAR yang sudah include PostgreSQL driver
.\build.bat
.\run.bat  # Akan prioritaskan Fat JAR
```

**Linux/macOS:**
```bash
# Buat Fat JAR dengan semua dependencies
make build-fat
java -jar dist/document-management-system-fat.jar
```

**Verifikasi:**
- ✅ `dist/document-management-system-fat.jar` harus ada
- ✅ File size lebih besar (~6MB) karena include PostgreSQL driver

---

### **❌ Error: Koneksi Database**
```bash
# 1. Periksa koneksi internet
ping google.com

# 2. Test koneksi ke Neon PostgreSQL
telnet ep-shy-leaf-a8yseowb-pooler.eastus2.azure.neon.tech 5432

# 3. Periksa kredensial database di src/utils/DBConnection.java
```

---

### **❌ Error: GUI tidak muncul (Linux/macOS)**
```bash
# Pastikan DISPLAY environment variable ter-set
export DISPLAY=:0

# Atau jalankan dengan virtual display
Xvfb :99 -screen 0 1024x768x24 &
export DISPLAY=:99
```

---

### **❌ Error: "make: command not found" (Windows)**
```powershell
# Gunakan batch scripts untuk Windows
.\build.bat    # Bukan make build
.\run.bat      # Bukan make run

# Atau install make untuk Windows (opsional):
# choco install make
```

## 📄 Informasi Build

### **📦 File Output**

| Build Method | Output Files | Size | Use Case |
|--------------|--------------|------|----------|
| **Windows** (`.\build.bat`) | `document-management-system.jar`<br/>`document-management-system-fat.jar` | ~2MB<br/>~6MB | Regular JAR<br/>Self-contained JAR |
| **Linux/macOS** (`make build`) | `document-management-system.jar` | ~2MB | Regular JAR (perlu lib/) |
| **Linux/macOS** (`make build-fat`) | `document-management-system-fat.jar` | ~6MB | Self-contained JAR |

### **🗃️ Project Structure**
```
IF2050-2025-K03-ComFile/
├── src/                    # Source code
├── lib/                    # Dependencies (postgresql-42.7.2.jar)
├── out/                    # Compiled classes
├── dist/                   # JAR output files
├── build.bat              # Windows build script
├── run.bat                # Windows run script
├── Makefile               # Unix build configuration
└── requirements.txt       # Dependencies list
```

Lihat file `requirements.txt` untuk daftar lengkap dependensi dan persyaratan sistem.
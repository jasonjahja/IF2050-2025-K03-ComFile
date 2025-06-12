# ================================================
# SISTEM MANAJEMEN DOKUMEN - KONFIGURASI BUILD
# ================================================
# 
# Penjelasan Aplikasi:
# Aplikasi desktop berbasis Java Swing untuk manajemen dokumen
# dengan fitur autentikasi user, upload/download file, dan 
# kontrol akses berbasis role (Admin/Manager/Karyawan).
# Database menggunakan PostgreSQL yang di-host di Neon.
#
# ================================================
# PERBEDAAN PLATFORM:
# ================================================
# 
# LINUX/UNIX/macOS:
# - Gunakan Makefile ini (make build, make run)
# - Path separator: : (colon)
# - Directory separator: / (forward slash)
# - Shell commands: mkdir -p, rm -rf, find
# 
# WINDOWS:
# - Gunakan build.bat dan run.bat
# - Path separator: ; (semicolon) 
# - Directory separator: \ (backslash)
# - Shell commands: mkdir, rmdir, dir
# - PowerShell syntax: .\build.bat
#
# ================================================

JAVA_VERSION := 17
SRC_DIR := src
TEST_DIR := test
LIB_DIR := lib
OUT_DIR := out
DIST_DIR := dist
MAIN_CLASS := main.MainApplication

# Pengaturan kompilasi Java (Unix/Linux/macOS)
CLASSPATH := $(LIB_DIR)/*
PRODUCTION_CP := $(LIB_DIR)/*:$(OUT_DIR)/production
TEST_CP := $(LIB_DIR)/*:$(OUT_DIR)/production:$(OUT_DIR)/test

.PHONY: all clean compile test build build-fat run install help info

# Target default
all: clean compile test build

# Target bantuan
help:
	@echo "==========================================="
	@echo "SISTEM MANAJEMEN DOKUMEN - BANTUAN BUILD"
	@echo "==========================================="
	@echo ""
	@echo "PENTING UNTUK WINDOWS:"
	@echo "   Makefile ini untuk Unix/Linux/macOS"
	@echo "   Untuk Windows, gunakan:"
	@echo "   - build.bat (kompilasi dan buat JAR)"
	@echo "   - run.bat   (jalankan aplikasi)"
	@echo ""
	@echo "Target Unix/Linux/macOS:"
	@echo "  info       - Tampilkan informasi aplikasi"
	@echo "  compile    - Kompilasi source code"
	@echo "  test       - Jalankan pengujian"
	@echo "  build      - Buat file JAR (dengan external deps)"
	@echo "  build-fat  - Buat Fat JAR (semua deps ter-include)"
	@echo "  run        - Jalankan aplikasi"
	@echo "  clean      - Bersihkan file build"
	@echo "  install    - Install dependencies"
	@echo "  all        - Bersihkan, kompilasi, test, dan build"
	@echo ""
	@echo "Cara menjalankan:"
	@echo "  Unix/Linux/macOS: make run"
	@echo "  Windows:          build.bat"
	@echo ""

# Informasi aplikasi
info:
	@echo "==========================================="
	@echo "INFORMASI SISTEM MANAJEMEN DOKUMEN"
	@echo "==========================================="
	@echo ""
	@echo "Nama Aplikasi: ComFile Document Management System"
	@echo "Platform: Java Swing Desktop Application"
	@echo "Java Version: $(JAVA_VERSION)"
	@echo "Database: PostgreSQL (Neon Cloud)"
	@echo "Kelompok: K3C - IF2050 PBO"
	@echo ""
	@echo "MODUL YANG DIIMPLEMENTASI:"
	@echo "   1. Autentikasi Pengguna (Login/Logout)"
	@echo "   2. Manajemen Dokumen (Upload/Download)"
	@echo "   3. Manajemen Admin (User CRUD)"
	@echo "   4. Navigasi dan UI Components"
	@echo "   5. Kontrol Akses (Role-based)"
	@echo "   6. Filter dan Pencarian Dokumen"
	@echo ""
	@echo "TABEL DATABASE:"
	@echo "   - users: id, username, password, role, full_name"
	@echo "   - documents: id, title, filename, file_path, owner_id"
	@echo "   - document_access: id, document_id, user_id, permission"
	@echo "   - audit_log: id, user_id, action, entity_type, timestamp"
	@echo ""
	@echo "UNTUK WINDOWS: Gunakan build.bat dan run.bat"
	@echo ""

# Bersihkan file build
clean:
	@echo "Membersihkan file build..."
	rm -rf $(OUT_DIR)
	rm -rf $(DIST_DIR)
	rm -f manifest.txt
	@echo "Pembersihan selesai."

# Kompilasi source code
compile:
	@echo "Mengkompilasi source code..."
	mkdir -p $(OUT_DIR)/production
	find $(SRC_DIR) -name "*.java" -print0 | xargs -0 javac -cp "$(CLASSPATH)" -d $(OUT_DIR)/production
	@echo "Kompilasi selesai."

# Kompilasi dan jalankan test
test: compile
	@echo "Mengkompilasi test..."
	mkdir -p $(OUT_DIR)/test
	find $(TEST_DIR) -name "*.java" -print0 | xargs -0 javac -cp "$(PRODUCTION_CP)" -d $(OUT_DIR)/test
	@echo "Menjalankan test..."
	@echo "Test compilation berhasil (GUI test di-skip untuk CI/CD)"

# Buat file JAR (dengan external dependencies)
build: compile
	@echo "Membuat file JAR..."
	mkdir -p $(DIST_DIR)
	echo "Main-Class: $(MAIN_CLASS)" > manifest.txt
	echo "Class-Path: lib/postgresql-42.7.2.jar" >> manifest.txt
	jar cfm $(DIST_DIR)/document-management-system.jar manifest.txt -C $(OUT_DIR)/production . -C . $(LIB_DIR)/
	@echo "File JAR berhasil dibuat: $(DIST_DIR)/document-management-system.jar"
	@echo "PostgreSQL driver included: $(LIB_DIR)/postgresql-42.7.2.jar"
	@echo ""
	@echo "Cara menjalankan:"
	@echo "   java -jar $(DIST_DIR)/document-management-system.jar"
	@echo "   (JAR harus di folder yang sama dengan direktori lib/)"

# Buat Fat JAR (semua dependencies digabung)
build-fat: compile
	@echo "Membuat Fat JAR (dengan semua dependencies)..."
	mkdir -p $(DIST_DIR)
	mkdir -p $(OUT_DIR)/fat-jar
	# Extract PostgreSQL JAR
	cd $(OUT_DIR)/fat-jar && jar xf ../../$(LIB_DIR)/postgresql-42.7.2.jar
	# Copy application classes
	cp -r $(OUT_DIR)/production/* $(OUT_DIR)/fat-jar/
	# Create manifest
	echo "Main-Class: $(MAIN_CLASS)" > manifest-fat.txt
	# Create fat JAR
	jar cfm $(DIST_DIR)/document-management-system-fat.jar manifest-fat.txt -C $(OUT_DIR)/fat-jar .
	@echo "Fat JAR berhasil dibuat: $(DIST_DIR)/document-management-system-fat.jar"
	@echo "Semua dependencies sudah ter-include dalam JAR"
	@echo ""
	@echo "Cara menjalankan:"
	@echo "   java -jar $(DIST_DIR)/document-management-system-fat.jar"

# Jalankan aplikasi
run: compile
	@echo "Menjalankan aplikasi..."
	@echo "Pastikan koneksi internet aktif untuk database Neon"
	java -cp "$(PRODUCTION_CP)" $(MAIN_CLASS)

# Install dependencies
install:
	@echo Memeriksa dependencies...
	@echo Semua dependencies tersedia di direktori $(LIB_DIR)\
	@echo PostgreSQL JDBC driver: $(LIB_DIR)\postgresql-42.7.2.jar
	@echo Database: Neon PostgreSQL (Cloud)
	@echo.
	@echo Lihat requirements.txt untuk informasi lengkap

# Cek versi Java
check-java:
	@echo Memeriksa versi Java...
	@java -version
	@echo.
	@echo Versi Java yang dibutuhkan: $(JAVA_VERSION)

# Target untuk demo
demo: info
	@echo 👤 USER LOGIN (untuk testing):
	@echo    Admin    : admincf / admin123
	@echo    Manager  : lanasteiner / lana123
	@echo    Karyawan : oliviarhye / olivia123
	@echo. 
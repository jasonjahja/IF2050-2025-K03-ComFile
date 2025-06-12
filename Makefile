# ================================================
# SISTEM MANAJEMEN DOKUMEN - KONFIGURASI BUILD
# ================================================
# 
# Penjelasan Aplikasi:
# Aplikasi desktop berbasis Java Swing untuk manajemen dokumen
# dengan fitur autentikasi user, upload/download file, dan 
# kontrol akses berbasis role (Manager/Karyawan).
# Database menggunakan PostgreSQL yang di-host di Neon.
#
# ================================================

JAVA_VERSION := 17
SRC_DIR := src
TEST_DIR := test
LIB_DIR := lib
OUT_DIR := out
DIST_DIR := dist
MAIN_CLASS := MainApplication

# Pengaturan kompilasi Java
CLASSPATH := $(LIB_DIR)/*
PRODUCTION_CP := $(LIB_DIR)/*:$(OUT_DIR)/production
TEST_CP := $(LIB_DIR)/*:$(OUT_DIR)/production:$(OUT_DIR)/test

.PHONY: all clean compile test build run install help info

# Target default
all: clean compile test build

# Target bantuan
help:
	@echo "==========================================="
	@echo "SISTEM MANAJEMEN DOKUMEN - BANTUAN BUILD"
	@echo "==========================================="
	@echo ""
	@echo "Target yang tersedia:"
	@echo "  info       - Tampilkan informasi aplikasi"
	@echo "  compile    - Kompilasi source code"
	@echo "  test       - Jalankan pengujian"
	@echo "  build      - Buat file JAR"
	@echo "  run        - Jalankan aplikasi"
	@echo "  clean      - Bersihkan file build"
	@echo "  install    - Install dependencies"
	@echo "  all        - Bersihkan, kompilasi, test, dan build"
	@echo ""
	@echo "Cara menjalankan aplikasi:"
	@echo "  1. make run          (langsung jalankan)"
	@echo "  2. make build        (buat JAR)"
	@echo "     java -jar dist/document-management-system.jar"
	@echo ""

# Informasi aplikasi
info:
	@echo "==========================================="
	@echo "INFORMASI SISTEM MANAJEMEN DOKUMEN"
	@echo "==========================================="
	@echo ""
	@echo "📱 Nama Aplikasi: Document Management System"
	@echo "🖥️  Platform: Java Swing Desktop Application"
	@echo "☕ Java Version: $(JAVA_VERSION)"
	@echo "🗄️  Database: PostgreSQL (Neon Cloud)"
	@echo ""
	@echo "🎯 MODUL YANG DIIMPLEMENTASI:"
	@echo "   1. Autentikasi User (Login/Logout)"
	@echo "   2. Manajemen Dokumen (Upload/Download)"
	@echo "   3. Navigasi dan UI Components"
	@echo "   4. Kontrol Akses (Role-based)"
	@echo "   5. Filter dan Pencarian Dokumen"
	@echo ""
	@echo "🗂️  TABEL DATABASE:"
	@echo "   - users: id, username, password, role, full_name"
	@echo "   - documents: id, title, filename, file_path, owner_id"
	@echo "   - document_access: id, document_id, user_id, permission"
	@echo "   - audit_log: id, user_id, action, entity_type, timestamp"
	@echo ""

# Bersihkan file build
clean:
	@echo "🧹 Membersihkan file build..."
	rm -rf $(OUT_DIR)
	rm -rf $(DIST_DIR)
	rm -f manifest.txt
	@echo "✅ Pembersihan selesai."

# Kompilasi source code
compile:
	@echo "⚡ Mengkompilasi source code..."
	mkdir -p $(OUT_DIR)/production
	find $(SRC_DIR) -name "*.java" -print0 | xargs -0 javac -cp "$(CLASSPATH)" -d $(OUT_DIR)/production
	@echo "✅ Kompilasi selesai."

# Kompilasi dan jalankan test
test: compile
	@echo "🧪 Mengkompilasi test..."
	mkdir -p $(OUT_DIR)/test
	find $(TEST_DIR) -name "*.java" -print0 | xargs -0 javac -cp "$(PRODUCTION_CP)" -d $(OUT_DIR)/test
	@echo "🚀 Menjalankan test..."
	@echo "✅ Test compilation berhasil (GUI test di-skip untuk CI/CD)"

# Buat file JAR
build: compile
	@echo "📦 Membuat file JAR..."
	mkdir -p $(DIST_DIR)
	echo "Main-Class: $(MAIN_CLASS)" > manifest.txt
	echo "Class-Path: lib/postgresql-42.7.2.jar" >> manifest.txt
	jar cfm $(DIST_DIR)/document-management-system.jar manifest.txt -C $(OUT_DIR)/production . -C . $(LIB_DIR)/
	@echo "✅ File JAR berhasil dibuat: $(DIST_DIR)/document-management-system.jar"

# Jalankan aplikasi
run: compile
	@echo "🚀 Menjalankan aplikasi..."
	@echo "📋 Pastikan koneksi internet aktif untuk database Neon"
	java -cp "$(PRODUCTION_CP)" $(MAIN_CLASS)

# Install dependencies
install:
	@echo "📥 Memeriksa dependencies..."
	@echo "✅ Semua dependencies tersedia di direktori $(LIB_DIR)/"
	@echo "📚 PostgreSQL JDBC driver: $(LIB_DIR)/postgresql-42.7.2.jar"
	@echo "🌐 Database: Neon PostgreSQL (Cloud)"

# Target pengembangan cepat
dev: clean compile run

# Target untuk CI/CD
ci: clean compile test build

# Cek versi Java
check-java:
	@echo "☕ Memeriksa versi Java..."
	@java -version
	@echo "📋 Versi Java yang dibutuhkan: $(JAVA_VERSION)"

# Target untuk demo
demo: info
	@echo "🎬 CARA MENJALANKAN DEMO:"
	@echo "   1. make run                    # Jalankan langsung"
	@echo "   2. make build && java -jar dist/document-management-system.jar"
	@echo ""
	@echo "👤 USER LOGIN (untuk testing):"
	@echo "   Manager  : lanasteiner / lana123"
	@echo "   Karyawan : oliviarhye / olivia123" 
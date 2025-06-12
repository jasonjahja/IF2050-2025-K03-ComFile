@echo off
REM ================================================
REM SISTEM MANAJEMEN DOKUMEN - RUN SCRIPT WINDOWS
REM ================================================

echo.
echo ======================================
echo SISTEM MANAJEMEN DOKUMEN - MENJALANKAN
echo ======================================
echo.

REM Check if Fat JAR file exists (prioritas tertinggi)
if exist "dist\document-management-system-fat.jar" (
    echo 🚀 Menjalankan aplikasi dari Fat JAR file...
    echo 📋 Pastikan koneksi internet aktif untuk database Neon
    echo 📚 Menggunakan Fat JAR dengan PostgreSQL driver ter-include
    echo.
    java -jar dist\document-management-system-fat.jar
) else if exist "dist\document-management-system.jar" (
    echo 🚀 Menjalankan aplikasi dari JAR file...
    echo 📋 Pastikan koneksi internet aktif untuk database Neon
    echo ⚠️  Note: Jika database error, jalankan build.bat ulang untuk Fat JAR
    echo.
    java -jar dist\document-management-system.jar
) else (
    REM Check if compiled classes exist
    if exist "out\production\main\MainApplication.class" (
        echo 🚀 Menjalankan aplikasi dari compiled classes...
        echo 📋 Pastikan koneksi internet aktif untuk database Neon
        echo.
        java -cp "lib\*;out\production" main.MainApplication
    ) else (
        echo ❌ Aplikasi belum dikompilasi!
        echo.
        echo Silakan jalankan build.bat terlebih dahulu:
        echo   build.bat
        echo.
        pause
        exit /b 1
    )
)

echo.
echo 👤 USER LOGIN TESTING:
echo   Admin    : admincf / admin123
echo   Manager  : lanasteiner / lana123
echo   Karyawan : oliviarhye / olivia123
echo.
pause 
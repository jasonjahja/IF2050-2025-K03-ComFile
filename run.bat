@echo off
REM ================================================
REM SISTEM MANAJEMEN DOKUMEN - RUN SCRIPT WINDOWS
REM ================================================

echo.
echo ======================================
echo SISTEM MANAJEMEN DOKUMEN - MENJALANKAN
echo ======================================
echo.

REM Check if JAR file exists
if exist "dist\document-management-system.jar" (
    echo 🚀 Menjalankan aplikasi dari JAR file...
    echo 📋 Pastikan koneksi internet aktif untuk database Neon
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
echo   Manager  : lanasteiner / lana123
echo   Karyawan : oliviarhye / olivia123
echo.
pause 
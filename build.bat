@echo off
REM ================================================
REM SISTEM MANAJEMEN DOKUMEN - BUILD SCRIPT WINDOWS
REM ================================================

echo.
echo =========================================
echo SISTEM MANAJEMEN DOKUMEN - BUILD SCRIPT
echo =========================================
echo.

REM Check Java version
echo [1/5] Checking Java version...
java -version
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java not found! Please install Java 17+
    pause
    exit /b 1
)

echo.
echo [2/5] Creating output directories...
if not exist "out\production" mkdir out\production
if not exist "dist" mkdir dist

echo.
echo [3/5] Compiling all Java source files...
REM Compile all Java files in one command with proper classpath
javac -cp "lib\*" -d out\production ^
    src\main\MainApplication.java ^
    src\components\AccessControl.java ^
    src\components\Filter.java ^
    src\components\NavigationBar.java ^
    src\components\SearchBar.java ^
    src\pages\Login.java ^
    src\pages\Dashboard\Dashboard.java ^
    src\pages\Admin\AddUserPage.java ^
    src\pages\Admin\AdminDashboard.java ^
    src\pages\Admin\EditUserPage.java ^
    src\pages\Admin\UserManagementDashboard.java ^
    src\pages\ManageDocuments\Document.java ^
    src\pages\ManageDocuments\DocumentInterface.java ^
    src\pages\ManageDocuments\MyDocuments.java ^
    src\pages\ManageDocuments\UploadDocumentUI.java ^
    src\utils\DBConnection.java ^
    src\utils\DocumentDAO.java ^
    src\utils\UserDAO.java ^
    src\storage\DocumentStorage.java

if %ERRORLEVEL% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo ✅ Compilation successful!

echo.
echo [4/5] Creating JAR manifest...
echo Main-Class: main.MainApplication > manifest.txt
echo Class-Path: lib/postgresql-42.7.2.jar >> manifest.txt

echo.
echo [5/5] Creating JAR file...
jar cfm dist\document-management-system.jar manifest.txt -C out\production . -C . lib\

if %ERRORLEVEL% neq 0 (
    echo ERROR: JAR creation failed!
    pause
    exit /b 1
)

echo.
echo [6/6] Creating Fat JAR (with all dependencies)...
if not exist "out\fat-jar" mkdir out\fat-jar

REM Extract PostgreSQL JAR
cd out\fat-jar
jar xf ..\..\lib\postgresql-42.7.2.jar
cd ..\..

REM Copy application classes
xcopy /E /Y out\production\* out\fat-jar\

REM Create manifest for fat JAR
echo Main-Class: main.MainApplication > manifest-fat.txt

REM Create fat JAR
jar cfm dist\document-management-system-fat.jar manifest-fat.txt -C out\fat-jar .

if %ERRORLEVEL% neq 0 (
    echo ERROR: Fat JAR creation failed!
    pause
    exit /b 1
)

echo.
echo ✅ Build completed successfully!
echo 📦 JAR file created: dist\document-management-system.jar
echo 📦 Fat JAR created: dist\document-management-system-fat.jar
echo.
echo 🚀 CARA MENJALANKAN:
echo   Regular JAR: java -jar dist\document-management-system.jar
echo   Fat JAR:     java -jar dist\document-management-system-fat.jar
echo   Direct:      java -cp "lib\*;out\production" main.MainApplication
echo.
echo 💡 Jika database error, gunakan Fat JAR (semua dependencies ter-include)
echo.
pause 
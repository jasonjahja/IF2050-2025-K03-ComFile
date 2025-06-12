@echo off
echo ================================================
echo  COMFILE - BUILD EXECUTABLE APPLICATION
echo ================================================
echo.
echo Building application that can be directly run...
echo.

:: Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Java not found! Install Java 17+ first.
    pause
    exit /b 1
)

:: Create output directories
if not exist "out\production" mkdir out\production
if not exist "dist" mkdir dist
if not exist "dist\executable" mkdir dist\executable

echo Compiling source code...
:: Compile all Java source files in one command to resolve dependencies
javac -cp "lib\*" -d out\production -sourcepath src src\main\MainApplication.java src\pages\Login.java src\pages\Dashboard\Dashboard.java src\pages\Admin\*.java src\pages\ManageDocuments\*.java src\components\*.java src\utils\*.java src\storage\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check the error messages above and fix the Java compilation issues.
    echo.
    pause
    exit /b 1
)

echo Compilation successful!

echo.
echo Creating Fat JAR (all dependencies included)...

:: Create temp directory for fat jar
if not exist "out\fat-jar" mkdir out\fat-jar

:: Extract PostgreSQL JAR
echo   Extracting PostgreSQL driver...
cd out\fat-jar
jar xf ..\..\lib\postgresql-42.7.2.jar
cd ..\..

:: Copy application classes
echo   Copying application classes...
xcopy /S /Y out\production\* out\fat-jar\ >nul

:: Copy images and resources
echo   Copying images and resources...
if exist "img" (
    if not exist "out\fat-jar\img" mkdir out\fat-jar\img
    xcopy /S /Y img\* out\fat-jar\img\ >nul
    echo     Copied ALL images from img/ directory
)
if exist "src\uploads" (
    if not exist "out\fat-jar\uploads" mkdir out\fat-jar\uploads
    xcopy /S /Y src\uploads\* out\fat-jar\uploads\ >nul
    echo     Copied uploads directory
)

:: Create manifest for executable JAR
echo Main-Class: main.MainApplication > manifest-executable.txt
echo.>> manifest-executable.txt

:: Create executable Fat JAR
echo   Building executable JAR...
jar cfm dist\executable\ComFile.jar manifest-executable.txt -C out\fat-jar .

if %ERRORLEVEL% NEQ 0 (
    echo JAR creation failed!
    pause
    exit /b 1
)

echo Fat JAR created successfully!

echo.
echo Creating launcher scripts...

:: Create Windows launcher script
echo @echo off > dist\executable\ComFile.bat
echo echo ================================================ >> dist\executable\ComFile.bat
echo echo  COMFILE DOCUMENT MANAGEMENT SYSTEM >> dist\executable\ComFile.bat
echo echo ================================================ >> dist\executable\ComFile.bat
echo echo Starting application... >> dist\executable\ComFile.bat
echo echo. >> dist\executable\ComFile.bat
echo java -jar ComFile.jar >> dist\executable\ComFile.bat
echo if %%ERRORLEVEL%% NEQ 0 ( >> dist\executable\ComFile.bat
echo     echo. >> dist\executable\ComFile.bat
echo     echo Application failed to start! >> dist\executable\ComFile.bat
echo     echo Make sure Java 17+ is installed. >> dist\executable\ComFile.bat
echo     echo Check if all required classes are in the JAR. >> dist\executable\ComFile.bat
echo     pause >> dist\executable\ComFile.bat
echo ^) >> dist\executable\ComFile.bat

:: Create Linux launcher script
echo #!/bin/bash > dist\executable\ComFile.sh
echo echo "================================================" >> dist\executable\ComFile.sh
echo echo " COMFILE DOCUMENT MANAGEMENT SYSTEM" >> dist\executable\ComFile.sh
echo echo "================================================" >> dist\executable\ComFile.sh
echo echo "Starting application..." >> dist\executable\ComFile.sh
echo echo >> dist\executable\ComFile.sh
echo java -jar ComFile.jar >> dist\executable\ComFile.sh

echo.
echo Creating README for executable...

:: Create README for the executable
echo # ComFile Document Management System > dist\executable\README.txt
echo ======================================= >> dist\executable\README.txt
echo. >> dist\executable\README.txt
echo HOW TO RUN THE APPLICATION: >> dist\executable\README.txt
echo. >> dist\executable\README.txt
echo EASY (Windows): >> dist\executable\README.txt
echo   Double-click ComFile.bat >> dist\executable\README.txt
echo. >> dist\executable\README.txt
echo MANUAL (All OS): >> dist\executable\README.txt
echo   java -jar ComFile.jar >> dist\executable\README.txt
echo. >> dist\executable\README.txt
echo IMPORTANT FILES: >> dist\executable\README.txt
echo   - ComFile.jar = MAIN EXECUTABLE >> dist\executable\README.txt
echo   - ComFile.bat = Windows Launcher >> dist\executable\README.txt
echo   - ComFile.sh = Linux/macOS Launcher >> dist\executable\README.txt
echo. >> dist\executable\README.txt
echo REQUIREMENTS: >> dist\executable\README.txt
echo - Java 17 or higher >> dist\executable\README.txt
echo - Internet connection (for Neon database) >> dist\executable\README.txt
echo. >> dist\executable\README.txt
echo USER LOGIN (for testing): >> dist\executable\README.txt
echo   Admin    : admincf / admin123 >> dist\executable\README.txt
echo   Manager  : lanasteiner / lana123 >> dist\executable\README.txt
echo   Employee : oliviarhye / olivia123 >> dist\executable\README.txt
echo. >> dist\executable\README.txt
echo DATABASE: >> dist\executable\README.txt
echo PostgreSQL on Neon Cloud (no database setup needed) >> dist\executable\README.txt

:: Clean up temporary files
del manifest-executable.txt >nul 2>&1
rmdir /S /Q out\fat-jar >nul 2>&1

echo.
echo ================================================
echo EXECUTABLE BUILD COMPLETED!
echo ================================================
echo.
echo Executable files available at: dist\executable\
echo.
echo ComFile.jar (Executable JAR)
echo ComFile.bat (Windows Launcher)
echo ComFile.sh (Linux/macOS Launcher)  
echo README.txt (Usage guide)
echo.
echo HOW TO RUN:
echo   EASY: Double-click ComFile.bat (Windows)
echo   MANUAL: java -jar ComFile.jar
echo.
echo NOTE:
echo   - ComFile.jar = Main executable file
echo   - ComFile.bat = Windows launcher script
echo   - ComFile.sh = Linux/macOS launcher script
echo.
echo JAR Size: 
for %%I in (dist\executable\ComFile.jar) do echo   %%~zI bytes
echo.
pause 
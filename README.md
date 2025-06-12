# ComFile - Document Management System

## Group Information

**Group K3C**

| Name | Student ID |
|------|-----|
| Audra Zelvania P. H. | 18222106 |
| Angelica Aliwinata | 18222113 |
| Jason Jahja | 18222116 |
| Sekar Anindita N. | 18222125 |
| Anindita Widya S. | 18222128 |

## Quick Start

### **Windows**
```powershell
git clone https://github.com/jasonjahja/IF2050-2025-K03-ComFile.git
cd IF2050-2025-K03-ComFile
.\build.bat
cd dist\executable
# Double-click ComFile.bat to run!
```

### **Login Testing**
- **Admin:** `admincf / admin123`
- **Manager:** `lanasteiner / lana123`
- **Employee:** `oliviarhye / olivia123`

---

## Application Overview

ComFile is a Java Swing desktop application designed for efficient document management with the following features:

- **User Authentication**: Login system with Admin, Manager, and Employee roles
- **Account Management**: User management system owned by Admin
- **Document Management**: Upload, download, and file organization
- **Access Control**: Document access sharing based on user roles
- **Cloud Database**: Uses PostgreSQL hosted on Neon

---

## How to Build and Run

### **Step 1: Build Application**

```powershell
# Clone repository
git clone https://github.com/jasonjahja/IF2050-2025-K03-ComFile.git
cd IF2050-2025-K03-ComFile

# Build complete executable with all images and launcher
.\build.bat
```

### **Step 2: Run Application**

```powershell
# Go to executable folder
cd dist\executable

# Run application (choose one):
# 1. Double-click ComFile.bat (EASIEST)
# 2. Or run manually: java -jar ComFile.jar
```

**Generated Output:**
- `dist\executable\ComFile.jar` (7MB - complete executable with all dependencies and images)
- `dist\executable\ComFile.bat` (Windows launcher - double-click to run)
- `dist\executable\ComFile.sh` (Linux launcher)
- `dist\executable\README.txt` (usage guide)

---

## Requirements

- **Java 17 or higher**
- **Internet connection** (for Neon PostgreSQL database)
- **Windows OS** (for the batch script build process)

---

## Implemented Modules

### 1. **User Authentication Module**
- **Main Files**: `src/pages/Login.java`, `src/pages/ManageDocuments/Document.java`
- **Features**: 
  - Login with username/password with Enter key support
  - Role-based access (Admin/Manager/Employee)
  - Session management

### 2. **Document Management Module**
- **Main Files**: `src/pages/ManageDocuments/MyDocuments.java`
- **Features**:
  - Upload files (PDF, DOCX, XLSX, images)
  - Download and open documents
  - Delete documents with confirmation

### 3. **Interface Components Module**
- **Main Files**: 
  - `src/components/NavigationBar.java`
  - `src/components/SearchBar.java`
  - `src/components/Filter.java`
- **Features**:
  - Responsive navigation menu
  - Document search and filter
  - Reusable UI components

### 4. **Access Control Module**
- **Main Files**: `src/components/AccessControl.java`
- **Features**:
  - Share documents between users
  - Permission levels (read, write, admin)
  - Access control matrix

### 5. **Admin Management Module**
- **Main Files**: 
  - `src/pages/Admin/AdminDashboard.java`
  - `src/pages/Admin/UserManagementDashboard.java`
  - `src/pages/Admin/AddUserPage.java`
  - `src/pages/Admin/EditUserPage.java`
- **Features**:
  - Admin dashboard
  - User management (CRUD)
  - System activity monitoring

### 6. **Database Connection Module**
- **Main Files**: `src/utils/DBConnection.java`, `src/utils/UserDAO.java`, `src/utils/DocumentDAO.java`
- **Features**:
  - Connection to Neon PostgreSQL
  - User data CRUD operations
  - Document metadata management

---

## Troubleshooting

### **Error: "Could not find or load main class"**

```powershell
# Rebuild application
.\build.bat

# Navigate to executable folder
cd dist\executable

# Run again
.\ComFile.bat
```

---

### **Error: "No suitable driver found for jdbc:postgresql"**

**Solution:** The build process automatically includes the PostgreSQL driver. If you get this error:

```powershell
# Rebuild to ensure driver is included
.\build.bat
cd dist\executable
.\ComFile.bat
```

**Verification:** `ComFile.jar` should be approximately 7MB (includes PostgreSQL driver + all images)

---

### **Error: Images/Icons not showing**

**Solution:** The build process automatically includes all images. If images are missing:

```powershell
# Rebuild to include all images
.\build.bat
cd dist\executable
.\ComFile.bat
```

**Expected JAR contents:**
- 26 image files (.png)
- `img/avatars/` folder
- All UI icons (navigation, buttons, etc.)

---

### **Error: Database Connection**

```bash
# 1. Check internet connection
ping google.com

# 2. Verify database connectivity
# The application uses Neon PostgreSQL cloud database
# No local database setup required
```

---

### **Error: Java not found**

```powershell
# Install Java 17 or higher
# Download from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
# OR use OpenJDK: https://adoptium.net/

# Verify installation
java -version
```

---

## Project Structure
```
IF2050-2025-K03-ComFile/
├── src/                     # Source code
├── img/                     # ALL images (26 files + avatars)
├── lib/                     # Dependencies (postgresql-42.7.2.jar)
├── dist/                    # Output files
│   └── executable/          # Ready-to-distribute files
│       ├── ComFile.jar                        # Executable JAR (7MB)
│       ├── ComFile.bat                        # Windows launcher
│       ├── ComFile.sh                         # Linux launcher
│       └── README.txt                         # Usage instructions
├── build.bat               # Build script (creates executable)
└── requirements.txt        # Dependencies list
```

## Features

### **Enhanced Login Experience**
- **Enter Key Support**: After entering username or password, press `Enter` to login
- **Click to Login**: Traditional button click still works
- **Faster Login**: More convenient for keyboard users

### **Complete Self-Contained Application**
- **No External Dependencies**: All libraries included in JAR
- **All Images Included**: UI icons, avatars, and graphics embedded
- **Cross-Platform Launchers**: Windows batch script and Linux shell script
- **Ready for Distribution**: Single folder contains everything needed

---

See `requirements.txt` file for complete list of dependencies and system requirements.
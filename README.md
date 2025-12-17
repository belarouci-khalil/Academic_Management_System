# 🎓 Academic Management System - Final Version

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MongoDB](https://img.shields.io/badge/MongoDB-4.11-green.svg)](https://www.mongodb.com/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **A complete, production-ready Academic Management System built with Java Swing and MongoDB. Features modern UI, role-based access control, and comprehensive CRUD operations for managing students, teachers, subjects, groups, and grades.**

## 📋 Table of Contents

- [Features](#-features)
- [Technologies Used](#-technologies-used)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Usage](#-usage)
- [Project Structure](#-project-structure)
- [Key Features](#-key-features)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)

## ✨ Features

### 🔐 Authentication & Authorization
- **Secure login system** with role-based access control
- **Three user roles**: Administrator, Teacher, Student
- **Session management** with singleton pattern
- **Automatic credential generation**: Username = FirstName, Password = LastName

### 👨‍💼 Administrator Dashboard
- **Complete CRUD operations** for:
  - Student management
  - Teacher management
  - Subject management
  - Group management
- **Assignment features**:
  - Assign teachers to subjects
  - Assign students to groups
  - Assign subjects to groups
- **Modern table views** with search and filter capabilities
- **Data validation** and error handling

### 👨‍🏫 Teacher Dashboard
- **View assigned subjects** and groups
- **Grade management**:
  - Add grades for students (0-20 scale)
  - Edit existing grades
  - Add comments to grades
- **Restrictions**: Can only grade students in their assigned groups
- **Subject filtering** for easy navigation

### 👨‍🎓 Student Dashboard
- **View personal grades** (read-only)
- **Grade details**: Subject, Grade, Teacher, Comment, Date
- **Subject filtering** to view grades by subject
- **Clean, user-friendly interface**

### 🎨 Modern UI Design
- **Beautiful gradient backgrounds**
- **Custom styled components** (ModernButton, StyledPanel)
- **Responsive layouts** with proper spacing
- **Focus effects** on input fields
- **Professional color scheme**
- **System look and feel** integration

## 🛠️ Technologies Used

### Core Technologies
- **Java 17** - Modern Java features and performance
- **Java Swing** - Desktop GUI framework
- **MongoDB 4.11** - NoSQL database for flexible data storage
- **Maven 3.6+** - Build automation and dependency management

### Design Patterns & Principles
- **3-Layer Architecture**: Presentation → Business Logic → Data Access
- **Repository Pattern** - Abstraction for data access
- **Service Layer** - Business logic separation
- **Singleton Pattern** - Database connection and session management
- **OOP Principles**:
  - **Encapsulation** - Private fields with getters/setters
  - **Inheritance** - User → Administrator, Teacher, Student
  - **Polymorphism** - Method overriding (`showInformation()`)
  - **Abstraction** - Abstract classes and interfaces
- **SOLID Principles** - Clean, maintainable code structure

### Testing
- **JUnit 5** - Unit testing framework
- **Backend testing** - Comprehensive test suite

## 🏗️ Architecture

```
┌─────────────────────────────────────┐
│      PRESENTATION LAYER (UI)        │
│  - LoginForm                        │
│  - AdminDashboard                   │
│  - TeacherDashboard                 │
│  - StudentDashboard                 │
│  - Forms (CRUD operations)          │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│      BUSINESS LOGIC LAYER            │
│  - UserService                      │
│  - StudentService                   │
│  - TeacherService                   │
│  - SubjectService                   │
│  - GradeService                     │
│  - GroupService                     │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│      DATA ACCESS LAYER              │
│  - IUserRepository                  │
│  - IStudentRepository               │
│  - ISubjectRepository               │
│  - IGradeRepository                 │
│  - IGroupRepository                 │
│  - MongoDB Implementations          │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│           MONGODB DATABASE          │
│  - Collections: users, groups,      │
│    subjects, grades, etc.           │
└─────────────────────────────────────┘
```

## 📦 Installation

### Prerequisites

- **Java 17 or higher** - [Download Java](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **MongoDB** - [Download MongoDB](https://www.mongodb.com/try/download/community)

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/academic-management-system.git
cd academic-management-system
```

### Step 2: Start MongoDB

**Windows:**
```bash
mongod
```

**Linux/Mac:**
```bash
sudo systemctl start mongod
# or
mongod
```

MongoDB should be running on `localhost:27017` (default port).

### Step 3: Build the Project

```bash
mvn clean compile
```

### Step 4: Run the Application

**Option 1: Using Maven**
```bash
mvn exec:java -Dexec.mainClass="com.example.academic.ui.LoginForm"
```

**Option 2: Using Batch File (Windows)**
```bash
run.bat
```

**Option 3: Direct Java Execution**
```bash
java -cp target/classes com.example.academic.ui.LoginForm
```

## 🚀 Usage

### Login Credentials

The system automatically generates credentials:
- **Username** = First Name
- **Password** = Last Name (case-sensitive)

**Example Users:**
- Admin: Username = "Admin", Password = "User"
- Teacher: Username = "John", Password = "Doe"
- Student: Username = "Jane", Password = "Smith"

### Administrator Workflow

1. **Login** with admin credentials
2. **Manage Entities**:
   - Create/Edit/Delete Students, Teachers, Subjects, Groups
3. **Assignments**:
   - Assign teachers to subjects
   - Assign students to groups
   - Assign subjects to groups

### Teacher Workflow

1. **Login** with teacher credentials
2. **View** assigned subjects and groups
3. **Add Grades**:
   - Select student from assigned group
   - Select subject you teach
   - Enter grade (0-20)
   - Add optional comment

### Student Workflow

1. **Login** with student credentials
2. **View** all your grades
3. **Filter** by subject if needed

## 📁 Project Structure

```
academic-management-system/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── academic/
│   │                   ├── database/
│   │                   │   └── MongoDBConnection.java
│   │                   ├── enums/
│   │                   │   └── UserRole.java
│   │                   ├── exception/
│   │                   │   ├── DatabaseException.java
│   │                   │   ├── InvalidCredentialsException.java
│   │                   │   └── UserNotFoundException.java
│   │                   ├── model/
│   │                   │   ├── User.java (abstract)
│   │                   │   ├── Administrator.java
│   │                   │   ├── Teacher.java
│   │                   │   ├── Student.java
│   │                   │   ├── Subject.java
│   │                   │   ├── Group.java
│   │                   │   └── Grade.java
│   │                   ├── repository/
│   │                   │   ├── IUserRepository.java
│   │                   │   ├── IStudentRepository.java
│   │                   │   ├── ISubjectRepository.java
│   │                   │   ├── IGradeRepository.java
│   │                   │   ├── IGroupRepository.java
│   │                   │   └── [Implementations]
│   │                   ├── service/
│   │                   │   ├── UserService.java
│   │                   │   ├── StudentService.java
│   │                   │   ├── TeacherService.java
│   │                   │   ├── SubjectService.java
│   │                   │   ├── GradeService.java
│   │                   │   └── GroupService.java
│   │                   ├── ui/
│   │                   │   ├── LoginForm.java
│   │                   │   ├── AdminDashboard.java
│   │                   │   ├── TeacherDashboard.java
│   │                   │   ├── StudentDashboard.java
│   │                   │   ├── components/
│   │                   │   │   ├── ModernButton.java
│   │                   │   │   └── StyledPanel.java
│   │                   │   └── forms/
│   │                   │       ├── StudentForm.java
│   │                   │       ├── TeacherForm.java
│   │                   │       ├── SubjectForm.java
│   │                   │       ├── GroupForm.java
│   │                   │       └── GradeForm.java
│   │                   ├── util/
│   │                   │   └── SessionManager.java
│   │                   └── test/
│   │                       └── BackendTest.java
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── academic/
│                       └── service/
│                           └── StudentServiceTest.java
├── pom.xml
├── README.md
└── .gitignore
```

## 🔑 Key Features

### Data Management
- ✅ **Complete CRUD operations** for all entities
- ✅ **Data validation** and error handling
- ✅ **MongoDB integration** with connection pooling
- ✅ **Automatic database initialization**

### Security & Access Control
- ✅ **Role-based authentication**
- ✅ **Session management**
- ✅ **Permission-based features**
- ✅ **Input validation**

### User Experience
- ✅ **Modern, intuitive UI**
- ✅ **Responsive design**
- ✅ **Error messages** and user feedback
- ✅ **Keyboard shortcuts** (Enter to submit)
- ✅ **Focus management**

### Code Quality
- ✅ **Clean architecture**
- ✅ **SOLID principles**
- ✅ **Design patterns**
- ✅ **Exception handling**
- ✅ **Comprehensive testing**

## 📸 Screenshots

> *Note: Add screenshots of your application here to showcase the UI*

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


## 👨‍💻 Author

**Your Name**
- GitHub: [@belarouci-khalil](https://github.com/belarouci-khalil)
- Email: khalilbelarouci@gmail.com

## 🙏 Acknowledgments

- Java Swing community for UI components
- MongoDB for excellent NoSQL database
- Maven for build automation
- All contributors and testers

## 📊 Project Stats

- **Language**: Java
- **Lines of Code**: 5000+
- **Classes**: 30+
- **Design Patterns**: 5+
- **Database Collections**: 5+

**Keywords**: Java, Swing, MongoDB, Academic Management System, Student Management, Grade Management, Desktop Application, OOP, Design Patterns, Maven, CRUD Operations, Role-Based Access Control

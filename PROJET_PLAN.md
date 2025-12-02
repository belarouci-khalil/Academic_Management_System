# Plan Détaillé - Système d'Information Académique

## 📋 Vue d'ensemble du projet

Système de gestion académique avec 3 rôles utilisateurs (Administrateur, Enseignant, Étudiant) utilisant Java, POO, et base de données.

---

## 🏗️ Architecture du Projet

### Structure des couches (Architecture en 3 couches)

```
┌─────────────────────────────────────┐
│         COUCHE PRÉSENTATION (UI)     │
│    - LoginForm                       │
│    - AdminDashboard                  │
│    - TeacherDashboard                │
│    - StudentDashboard                │
│    - Forms (CRUD operations)         │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│      COUCHE LOGIQUE MÉTIER          │
│    - UserService                    │
│    - StudentService                 │
│    - TeacherService                 │
│    - SubjectService                 │
│    - GradeService                   │
│    - GroupService                   │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│      COUCHE ACCÈS AUX DONNÉES       │
│    - IUserRepository                │
│    - IStudentRepository             │
│    - ITeacherRepository             │
│    - ISubjectRepository             │
│    - IGradeRepository               │
│    - IGroupRepository               │
│    - Implémentations concrètes      │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│         BASE DE DONNÉES             │
│    - MySQL / MS SQL / SQLite        │
└─────────────────────────────────────┘
```

---

## 📁 Structure des Packages

```
com.example.academic
├── model
│   ├── User.java (classe abstraite)
│   ├── Administrator.java
│   ├── Teacher.java
│   ├── Student.java
│   ├── Subject.java
│   ├── Group.java
│   ├── Grade.java
│   └── enums
│       └── UserRole.java
├── repository
│   ├── interfaces
│   │   ├── IUserRepository.java
│   │   ├── IStudentRepository.java
│   │   ├── ITeacherRepository.java
│   │   ├── ISubjectRepository.java
│   │   ├── IGradeRepository.java
│   │   └── IGroupRepository.java
│   └── impl
│       ├── UserRepository.java
│       ├── StudentRepository.java
│       ├── TeacherRepository.java
│       ├── SubjectRepository.java
│       ├── GradeRepository.java
│       └── GroupRepository.java
├── service
│   ├── UserService.java
│   ├── StudentService.java
│   ├── TeacherService.java
│   ├── SubjectService.java
│   ├── GradeService.java
│   └── GroupService.java
├── ui
│   ├── LoginForm.java
│   ├── AdminDashboard.java
│   ├── TeacherDashboard.java
│   ├── StudentDashboard.java
│   ├── forms
│   │   ├── StudentForm.java
│   │   ├── TeacherForm.java
│   │   ├── SubjectForm.java
│   │   ├── GroupForm.java
│   │   └── GradeForm.java
│   └── components
│       ├── StudentTable.java
│       ├── GradeTable.java
│       └── SearchFilter.java
├── database
│   ├── DatabaseConnection.java
│   ├── DatabaseInitializer.java
│   └── DatabaseConfig.java
├── exception
│   ├── UserNotFoundException.java
│   ├── InvalidCredentialsException.java
│   ├── GradeNotFoundException.java
│   └── DatabaseException.java
└── util
    ├── PasswordHasher.java
    └── Validator.java
```

---

## 🗄️ Modèle de Base de Données

### Tables SQL

```sql
-- Table Users (table de base pour tous les utilisateurs)
CREATE TABLE Users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Groups
CREATE TABLE Groups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Subjects
CREATE TABLE Subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Students (hérite de Users)
CREATE TABLE Students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    group_id INT,
    student_number VARCHAR(20) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE SET NULL
);

-- Table Teachers (hérite de Users)
CREATE TABLE Teachers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    employee_number VARCHAR(20) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- Table Teacher_Subject (relation many-to-many)
CREATE TABLE Teacher_Subject (
    teacher_id INT NOT NULL,
    subject_id INT NOT NULL,
    PRIMARY KEY (teacher_id, subject_id),
    FOREIGN KEY (teacher_id) REFERENCES Teachers(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES Subjects(id) ON DELETE CASCADE
);

-- Table Group_Subject (relation many-to-many)
CREATE TABLE Group_Subject (
    group_id INT NOT NULL,
    subject_id INT NOT NULL,
    PRIMARY KEY (group_id, subject_id),
    FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES Subjects(id) ON DELETE CASCADE
);

-- Table Grades
CREATE TABLE Grades (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    teacher_id INT NOT NULL,
    grade DECIMAL(4,2) NOT NULL CHECK (grade >= 0 AND grade <= 20),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES Students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES Subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES Teachers(id) ON DELETE CASCADE
);
```

---

## 🎯 Étapes de Développement Détaillées

### PHASE 1 : Configuration et Infrastructure (Jour 1-2)

#### 1.1 Configuration du Projet
- [ ] Mettre à jour `pom.xml` avec les dépendances :
  - MySQL Connector / SQLite JDBC
  - Swing ou JavaFX pour l'UI
  - JUnit pour les tests (optionnel)

#### 1.2 Configuration de la Base de Données
- [ ] Créer `DatabaseConfig.java` pour les paramètres de connexion
- [ ] Créer `DatabaseConnection.java` (Singleton pattern)
- [ ] Créer `DatabaseInitializer.java` pour créer les tables
- [ ] Tester la connexion à la base de données

#### 1.3 Création des Enums
- [ ] Créer `UserRole.java` (ADMIN, TEACHER, STUDENT)

---

### PHASE 2 : Modèle de Données (Jour 3-4)

#### 2.1 Classe de Base User (Abstraction + Héritage)
```java
// User.java - Classe abstraite
public abstract class User {
    protected int id;
    protected String username;
    protected String password;
    protected UserRole role;
    protected String firstName;
    protected String lastName;
    
    // Constructeurs, getters, setters
    // Méthode abstraite pour polymorphisme
    public abstract void showInformation();
}
```

#### 2.2 Classes Filles (Héritage)
- [ ] `Administrator.java` extends User
- [ ] `Teacher.java` extends User
- [ ] `Student.java` extends User
- [ ] Implémenter `showInformation()` différemment dans chaque classe (Polymorphisme)

#### 2.3 Autres Modèles
- [ ] `Subject.java` (Encapsulation avec getters/setters privés)
- [ ] `Group.java`
- [ ] `Grade.java` (avec validation)

---

### PHASE 3 : Couche d'Accès aux Données (Jour 5-6)

#### 3.1 Interfaces Repository (Abstraction)
- [ ] `IUserRepository.java`
  ```java
  public interface IUserRepository {
      User findByUsername(String username) throws UserNotFoundException;
      User findById(int id) throws UserNotFoundException;
      void save(User user) throws DatabaseException;
      void delete(int id) throws DatabaseException;
      List<User> findAll();
  }
  ```

- [ ] `IStudentRepository.java`
- [ ] `ITeacherRepository.java`
- [ ] `ISubjectRepository.java`
- [ ] `IGradeRepository.java`
- [ ] `IGroupRepository.java`

#### 3.2 Implémentations Concrètes
- [ ] `UserRepository.java` implémente `IUserRepository`
- [ ] `StudentRepository.java` implémente `IStudentRepository`
- [ ] `TeacherRepository.java` implémente `ITeacherRepository`
- [ ] `SubjectRepository.java` implémente `ISubjectRepository`
- [ ] `GradeRepository.java` implémente `IGradeRepository`
- [ ] `GroupRepository.java` implémente `IGroupRepository`

**Points importants :**
- Utiliser PreparedStatement pour éviter les injections SQL
- Gérer les exceptions de base de données
- Fermer les ressources (try-with-resources)

---

### PHASE 4 : Couche Service (Jour 7-8)

#### 4.1 Services Métier
- [ ] `UserService.java`
  - Méthode `authenticate(String username, String password)`
  - Validation des données
  - Gestion des erreurs

- [ ] `StudentService.java`
  - CRUD operations
  - Validation métier

- [ ] `TeacherService.java`
- [ ] `SubjectService.java`
- [ ] `GradeService.java`
  - Vérifier que le teacher peut noter ce subject
  - Validation des notes (0-20)

- [ ] `GroupService.java`
  - Assigner students à groups
  - Assigner subjects à groups

**Responsabilités des Services :**
- Logique métier
- Validation
- Appels aux repositories
- Gestion des exceptions

---

### PHASE 5 : Gestion des Exceptions (Jour 9)

#### 5.1 Exceptions Personnalisées
- [ ] `UserNotFoundException.java` extends Exception
- [ ] `InvalidCredentialsException.java` extends Exception
- [ ] `GradeNotFoundException.java` extends Exception
- [ ] `DatabaseException.java` extends Exception

#### 5.2 Gestion des Erreurs
- [ ] Try-catch dans les services
- [ ] Messages d'erreur clairs pour l'utilisateur
- [ ] Logging des erreurs

---

### PHASE 6 : Interface Utilisateur (Jour 10-15)

#### 6.1 Formulaire de Connexion
- [ ] `LoginForm.java`
  - Champs : username, password
  - Bouton : Login
  - Validation : champs non vides
  - Gestion erreur : credentials invalides

#### 6.2 Dashboard Administrateur
- [ ] `AdminDashboard.java`
  - Menu avec options :
    - Gérer Groups
    - Gérer Teachers
    - Gérer Students
    - Gérer Subjects
    - Assigner Teachers à Subjects
    - Assigner Students/Subjects à Groups
  - Tableaux avec recherche/filtre
  - Boutons : Add, Edit, Delete

#### 6.3 Dashboard Enseignant
- [ ] `TeacherDashboard.java`
  - Liste des subjects enseignés
  - Formulaire pour entrer/modifier notes
  - `GradeForm.java` :
    - Sélectionner student (de son group)
    - Sélectionner subject (qu'il enseigne)
    - Entrer note (0-20)
    - Commentaire
  - Validation : note entre 0 et 20

#### 6.4 Dashboard Étudiant
- [ ] `StudentDashboard.java`
  - Tableau des notes (read-only)
  - Colonnes : Subject, Grade, Teacher, Comment, Date
  - Filtre par subject (optionnel)
  - Affichage uniquement des notes de l'étudiant connecté

#### 6.5 Formulaires CRUD
- [ ] `StudentForm.java` (pour Admin)
- [ ] `TeacherForm.java` (pour Admin)
- [ ] `SubjectForm.java` (pour Admin)
- [ ] `GroupForm.java` (pour Admin)
- [ ] `GradeForm.java` (pour Teacher)

**Caractéristiques des formulaires :**
- Validation des champs (non vides, formats corrects)
- Messages d'erreur clairs
- Boutons : Save, Cancel
- Gestion des erreurs

---

### PHASE 7 : Logique de Connexion et Sécurité (Jour 16)

#### 7.1 Authentification
- [ ] Dans `UserService.authenticate()` :
  - Vérifier username existe
  - Vérifier password (hashé ou en clair selon requirements)
  - Retourner User avec son rôle
  - Lancer `InvalidCredentialsException` si échec

#### 7.2 Gestion des Sessions
- [ ] Créer `SessionManager.java` (Singleton)
  - Stocker User actuel
  - Méthodes : `getCurrentUser()`, `logout()`

#### 7.3 Redirection selon Rôle
- [ ] Après login réussi :
  - ADMIN → `AdminDashboard`
  - TEACHER → `TeacherDashboard`
  - STUDENT → `StudentDashboard`

---

### PHASE 8 : Fonctionnalités Spécifiques (Jour 17-18)

#### 8.1 Assignations (Admin)
- [ ] Assigner Teacher à Subject
  - Formulaire : sélectionner teacher, sélectionner subject(s)
  - Vérifier que teacher n'enseigne pas déjà ce subject

- [ ] Assigner Student à Group
  - Formulaire : sélectionner student, sélectionner group
  - Vérifier que student n'est pas déjà dans un group

- [ ] Assigner Subject à Group
  - Formulaire : sélectionner group, sélectionner subject(s)

#### 8.2 Restrictions d'Accès
- [ ] Teacher ne peut noter que ses propres subjects
- [ ] Teacher ne peut noter que les students de ses groups
- [ ] Student ne voit que ses propres notes

---

### PHASE 9 : Recherche et Filtres (Jour 19)

#### 9.1 Composants de Recherche
- [ ] `SearchFilter.java` (composant réutilisable)
- [ ] Implémenter dans :
  - AdminDashboard (recherche students, teachers, subjects)
  - TeacherDashboard (recherche students, subjects)
  - StudentDashboard (filtre par subject)

---

### PHASE 10 : Tests et Validation (Jour 20)

#### 10.1 Tests Fonctionnels
- [ ] Tester login avec chaque rôle
- [ ] Tester CRUD operations pour Admin
- [ ] Tester entrée de notes pour Teacher
- [ ] Tester visualisation pour Student
- [ ] Tester assignations
- [ ] Tester validations et erreurs

#### 10.2 Validation Finale
- [ ] Vérifier tous les requirements fonctionnels
- [ ] Vérifier tous les requirements techniques
- [ ] Vérifier principes OOP
- [ ] Vérifier SOLID principles

---

## 🔑 Points Clés OOP à Implémenter

### 1. Encapsulation
```java
public class Student {
    private int id;
    private String firstName;
    // Getters et setters pour accès contrôlé
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { 
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        this.firstName = firstName; 
    }
}
```

### 2. Héritage
```java
User (classe abstraite)
├── Administrator extends User
├── Teacher extends User
└── Student extends User
```

### 3. Polymorphisme
```java
// Dans chaque classe fille
@Override
public void showInformation() {
    // Implémentation différente selon le rôle
}
```

### 4. Abstraction
- Interfaces Repository (IUserRepository, etc.)
- Classe abstraite User
- Méthodes abstraites dans User

### 5. SOLID Principles
- **S**ingle Responsibility : Chaque classe a une responsabilité unique
- **O**pen/Closed : Extensible via interfaces
- **L**iskov Substitution : Les classes filles peuvent remplacer User
- **I**nterface Segregation : Interfaces spécifiques (IStudentRepository, etc.)
- **D**ependency Inversion : Services dépendent d'interfaces, pas d'implémentations

---

## 📦 Dépendances Maven (pom.xml)

```xml
<dependencies>
    <!-- Base de données -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    <!-- OU SQLite -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.42.0.0</version>
    </dependency>
    
    <!-- UI - Swing (inclus dans JDK) ou JavaFX -->
    <!-- Pour JavaFX, ajouter selon votre version -->
</dependencies>
```

---

## 🚀 Ordre d'Implémentation Recommandé

1. **Infrastructure** : DB, Config, Connection
2. **Modèles** : User, Student, Teacher, etc.
3. **Repositories** : Interfaces puis implémentations
4. **Services** : Logique métier
5. **UI** : Login → Dashboards → Forms
6. **Fonctionnalités** : Assignations, restrictions
7. **Améliorations** : Recherche, filtres, validation

---

## ✅ Checklist Finale

### Fonctionnalités
- [ ] Login par rôle fonctionnel
- [ ] Admin peut gérer Groups, Teachers, Students, Subjects
- [ ] Admin peut assigner Teachers à Subjects
- [ ] Admin peut assigner Students/Subjects à Groups
- [ ] Teacher peut entrer/modifier notes
- [ ] Student peut voir ses notes
- [ ] Login automatique : username=firstName, password=lastName

### Techniques
- [ ] Base de données configurée
- [ ] Modèle objet complet
- [ ] Repositories avec interfaces
- [ ] Gestion d'erreurs complète
- [ ] Architecture en 3 couches

### OOP
- [ ] Encapsulation (champs privés, getters/setters)
- [ ] Héritage (User → Admin/Teacher/Student)
- [ ] Polymorphisme (showInformation() différente)
- [ ] Abstraction (interfaces Repository)

### UI
- [ ] Login form
- [ ] Admin dashboard avec menu
- [ ] Teacher form pour notes
- [ ] Student form pour visualisation
- [ ] Validation et vérification d'erreurs
- [ ] Recherche/filtres

---

## 📝 Notes Importantes

1. **Sécurité** : Pour production, hasher les mots de passe (BCrypt)
2. **Validation** : Toujours valider les entrées utilisateur
3. **Exceptions** : Utiliser des exceptions personnalisées avec messages clairs
4. **Code propre** : Suivre les conventions Java, commenter le code
5. **Tests** : Tester chaque fonctionnalité après implémentation

---

Ce plan vous donne une roadmap complète pour développer le système étape par étape. Commencez par la Phase 1 et progressez méthodiquement !


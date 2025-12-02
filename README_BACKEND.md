# Backend - Système d'Information Académique avec MongoDB

## 📋 Configuration Requise

1. **Java 17+** installé
2. **MongoDB** installé et démarré
   - Par défaut, MongoDB doit être accessible sur `localhost:27017`
   - La base de données `academic_system` sera créée automatiquement

## 🚀 Installation et Démarrage

### 1. Démarrer MongoDB

```bash
# Windows
mongod

# Linux/Mac
sudo systemctl start mongod
# ou
mongod
```

### 2. Compiler le projet

```bash
mvn compile
```

### 3. Exécuter les tests backend

```bash
mvn exec:java -Dexec.mainClass="com.example.academic.test.BackendTest"
```

Ou directement avec Java :

```bash
java -cp target/classes com.example.academic.test.BackendTest
```

## 📁 Structure du Backend

```
src/main/java/com/example/academic/
├── model/              # Modèles de données (OOP)
│   ├── User.java       # Classe abstraite
│   ├── Administrator.java
│   ├── Teacher.java
│   ├── Student.java
│   ├── Subject.java
│   ├── Group.java
│   └── Grade.java
├── repository/         # Accès aux données (Abstraction)
│   ├── interfaces/    # Interfaces Repository
│   └── impl/          # Implémentations MongoDB
├── service/           # Logique métier
│   ├── UserService.java
│   ├── StudentService.java
│   ├── TeacherService.java
│   ├── SubjectService.java
│   ├── GroupService.java
│   └── GradeService.java
├── database/          # Configuration MongoDB
│   └── MongoDBConnection.java
├── exception/         # Exceptions personnalisées
│   ├── UserNotFoundException.java
│   ├── InvalidCredentialsException.java
│   └── DatabaseException.java
├── util/             # Utilitaires
│   └── SessionManager.java
└── test/             # Tests
    └── BackendTest.java
```

## 🧪 Tests Backend

La classe `BackendTest` teste toutes les fonctionnalités :

1. **Création de Groupes** - Crée des groupes d'étudiants
2. **Création de Matières** - Crée des matières enseignées
3. **Création d'Utilisateurs** - Crée Admin, Teachers, Students
4. **Assignations** - Assigne teachers à subjects, subjects à groups
5. **Authentification** - Teste le login pour chaque rôle
6. **Gestion des Notes** - Ajoute et récupère des notes
7. **Requêtes** - Teste toutes les requêtes et le polymorphisme

## 🔑 Fonctionnalités Implémentées

### ✅ Authentification
- Login par username/password
- Génération automatique : `username = firstName`, `password = lastName`
- Support de 3 rôles : ADMIN, TEACHER, STUDENT

### ✅ Gestion CRUD
- **Groups** : Créer, lire, mettre à jour, supprimer
- **Subjects** : Créer, lire, mettre à jour, supprimer
- **Users** : Créer (Admin, Teacher, Student), lire, supprimer
- **Grades** : Créer, lire, mettre à jour, supprimer

### ✅ Assignations
- Assigner Teacher à Subject
- Assigner Student à Group
- Assigner Subject à Group

### ✅ Restrictions
- Teacher ne peut noter que ses propres subjects
- Teacher ne peut noter que les students de ses groups
- Validation des notes (0-20)

### ✅ Principes OOP
- **Encapsulation** : Champs privés avec getters/setters
- **Héritage** : User → Administrator, Teacher, Student
- **Polymorphisme** : Méthode `showInformation()` différente par classe
- **Abstraction** : Interfaces Repository

## 📊 Collections MongoDB

Le système crée automatiquement ces collections :

- `users` - Tous les utilisateurs (Admin, Teacher, Student)
- `groups` - Groupes d'étudiants
- `subjects` - Matières enseignées
- `grades` - Notes des étudiants
- `teacher_subjects` - Relations Teacher-Subject (many-to-many)
- `group_subjects` - Relations Group-Subject (many-to-many)

## 🔧 Configuration MongoDB

Par défaut, la connexion utilise :
- **Host** : `localhost`
- **Port** : `27017`
- **Database** : `academic_system`

Pour changer la configuration, modifiez `MongoDBConnection.java` :

```java
private static final String CONNECTION_STRING = "mongodb://localhost:27017";
private static final String DATABASE_NAME = "academic_system";
```

## 📝 Exemple d'Utilisation

```java
// Créer un service
UserService userService = new UserService();

// Créer un admin
User admin = userService.createUser("Admin", "System", UserRole.ADMIN);

// Authentifier
User user = userService.authenticate("Admin", "System");

// Créer un groupe
GroupService groupService = new GroupService();
Group group = groupService.createGroup("Groupe A", "Description");

// Créer un étudiant
StudentService studentService = new StudentService();
Student student = studentService.createStudent("Pierre", "Durand", group.getId());
```

## ✅ Prochaines Étapes

Une fois que tous les tests backend passent, vous pouvez :

1. ✅ Vérifier que MongoDB contient les données
2. ✅ Tester manuellement chaque service
3. ✅ Créer l'interface utilisateur Java Swing

## 🐛 Dépannage

### Erreur : "Impossible de se connecter à MongoDB"
- Vérifiez que MongoDB est démarré : `mongod`
- Vérifiez que le port 27017 est accessible

### Erreur : "Collection not found"
- Les collections sont créées automatiquement au premier insert
- Vérifiez que MongoDB est accessible

### Erreur de compilation
- Vérifiez que Maven a téléchargé les dépendances : `mvn clean install`
- Vérifiez la version de Java : `java -version` (doit être 17+)


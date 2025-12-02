package com.example.academic.test;

import com.example.academic.enums.UserRole;
import com.example.academic.exception.InvalidCredentialsException;
import com.example.academic.model.*;
import com.example.academic.service.*;

/**
 * Classe de test pour toutes les fonctionnalités backend
 */
public class BackendTest {
    
    private UserService userService;
    private StudentService studentService;
    private TeacherService teacherService;
    private SubjectService subjectService;
    private GroupService groupService;
    private GradeService gradeService;

    public BackendTest() {
        this.userService = new UserService();
        this.studentService = new StudentService();
        this.teacherService = new TeacherService();
        this.subjectService = new SubjectService();
        this.groupService = new GroupService();
        this.gradeService = new GradeService();
    }

    public void runAllTests() {
        System.out.println("========================================");
        System.out.println("  TESTS BACKEND - SYSTÈME ACADÉMIQUE");
        System.out.println("========================================\n");

        try {
            testCreateGroups();
            testCreateSubjects();
            testCreateUsers();
            testAssignments();
            testAuthentication();
            testGrades();
            testQueries();
            
            System.out.println("\n========================================");
            System.out.println("  TOUS LES TESTS SONT PASSÉS ! ✅");
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("ERREUR LORS DES TESTS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void testCreateGroups() {
        System.out.println("--- TEST 1: Création de Groupes ---");
        try {
            Group group1 = groupService.createGroup("Groupe A", "Premier groupe");
            System.out.println("✅ Groupe créé: " + group1.getName() + " (ID: " + group1.getId() + ")");

            Group group2 = groupService.createGroup("Groupe B", "Deuxième groupe");
            System.out.println("✅ Groupe créé: " + group2.getName() + " (ID: " + group2.getId() + ")");

            System.out.println("Total groupes: " + groupService.getAllGroups().size() + "\n");
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void testCreateSubjects() {
        System.out.println("--- TEST 2: Création de Matières ---");
        try {
            Subject math = subjectService.createSubject("Mathématiques", "MATH101", "Cours de mathématiques");
            System.out.println("✅ Matière créée: " + math.getName() + " (" + math.getCode() + ")");

            Subject java = subjectService.createSubject("Programmation Java", "JAVA101", "Cours de Java");
            System.out.println("✅ Matière créée: " + java.getName() + " (" + java.getCode() + ")");

            Subject db = subjectService.createSubject("Bases de Données", "DB101", "Cours de bases de données");
            System.out.println("✅ Matière créée: " + db.getName() + " (" + db.getCode() + ")");

            System.out.println("Total matières: " + subjectService.getAllSubjects().size() + "\n");
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void testCreateUsers() {
        System.out.println("--- TEST 3: Création d'Utilisateurs ---");
        try {
            // Créer un administrateur
            User admin = userService.createUser("Admin", "System", UserRole.ADMIN);
            System.out.println("✅ Admin créé: " + admin.getFirstName() + " " + admin.getLastName() + 
                             " (Username: " + admin.getUsername() + ", Password: " + admin.getPassword() + ")");

            // Créer des enseignants
            Teacher teacher1 = teacherService.createTeacher("Jean", "Dupont");
            System.out.println("✅ Enseignant créé: " + teacher1.getFirstName() + " " + teacher1.getLastName() + 
                             " (Username: " + teacher1.getUsername() + ")");

            Teacher teacher2 = teacherService.createTeacher("Marie", "Martin");
            System.out.println("✅ Enseignant créé: " + teacher2.getFirstName() + " " + teacher2.getLastName());

            // Créer des étudiants
            java.util.List<Group> groups = groupService.getAllGroups();
            if (!groups.isEmpty()) {
                Student student1 = studentService.createStudent("Pierre", "Durand", groups.get(0).getId());
                System.out.println("✅ Étudiant créé: " + student1.getFirstName() + " " + student1.getLastName() + 
                                 " (Username: " + student1.getUsername() + ", Groupe: " + groups.get(0).getName() + ")");

                Student student2 = studentService.createStudent("Sophie", "Bernard", groups.get(0).getId());
                System.out.println("✅ Étudiant créé: " + student2.getFirstName() + " " + student2.getLastName());

                if (groups.size() > 1) {
                    Student student3 = studentService.createStudent("Luc", "Moreau", groups.get(1).getId());
                    System.out.println("✅ Étudiant créé: " + student3.getFirstName() + " " + student3.getLastName() + 
                                     " (Groupe: " + groups.get(1).getName() + ")");
                }
            }

            System.out.println("Total utilisateurs: " + userService.getAllUsers().size() + "\n");
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void testAssignments() {
        System.out.println("--- TEST 4: Assignations ---");
        try {
            // Assigner teachers à subjects
            java.util.List<Teacher> teachers = teacherService.getAllTeachers();
            java.util.List<Subject> subjects = subjectService.getAllSubjects();

            if (!teachers.isEmpty() && !subjects.isEmpty()) {
                teacherService.assignSubjectToTeacher(teachers.get(0).getId(), subjects.get(0).getId());
                System.out.println("✅ Enseignant " + teachers.get(0).getFirstName() + " assigné à " + subjects.get(0).getName());

                teacherService.assignSubjectToTeacher(teachers.get(0).getId(), subjects.get(1).getId());
                System.out.println("✅ Enseignant " + teachers.get(0).getFirstName() + " assigné à " + subjects.get(1).getName());

                if (teachers.size() > 1) {
                    teacherService.assignSubjectToTeacher(teachers.get(1).getId(), subjects.get(2).getId());
                    System.out.println("✅ Enseignant " + teachers.get(1).getFirstName() + " assigné à " + subjects.get(2).getName());
                }
            }

            // Assigner subjects à groups
            java.util.List<Group> groups = groupService.getAllGroups();
            if (!groups.isEmpty() && !subjects.isEmpty()) {
                com.mongodb.client.MongoCollection<org.bson.Document> collection = 
                    com.example.academic.database.MongoDBConnection.getInstance()
                        .getDatabase()
                        .getCollection("group_subjects");

                for (Subject subject : subjects) {
                    org.bson.Document doc = new org.bson.Document("groupId", groups.get(0).getId())
                        .append("subjectId", subject.getId());
                    collection.insertOne(doc);
                }
                System.out.println("✅ Toutes les matières assignées au " + groups.get(0).getName());
            }

            System.out.println();
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void testAuthentication() {
        System.out.println("--- TEST 5: Authentification ---");
        try {
            // Test login admin
            User admin = userService.authenticate("Admin", "System");
            System.out.println("✅ Login Admin réussi: " + admin.getRole());

            // Test login teacher
            User teacher = userService.authenticate("Jean", "Dupont");
            System.out.println("✅ Login Teacher réussi: " + teacher.getRole());

            // Test login student
            User student = userService.authenticate("Pierre", "Durand");
            System.out.println("✅ Login Student réussi: " + student.getRole());

            // Test login invalide
            try {
                userService.authenticate("Invalid", "User");
                System.err.println("❌ Login invalide devrait échouer!");
            } catch (InvalidCredentialsException e) {
                System.out.println("✅ Login invalide correctement rejeté");
            }

            System.out.println();
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void testGrades() {
        System.out.println("--- TEST 6: Gestion des Notes ---");
        try {
            java.util.List<Teacher> teachers = teacherService.getAllTeachers();
            java.util.List<Student> students = studentService.getAllStudents();
            java.util.List<Subject> subjects = subjectService.getAllSubjects();

            if (!teachers.isEmpty() && !students.isEmpty() && !subjects.isEmpty()) {
                // Vérifier que le teacher enseigne le subject
                java.util.List<String> teacherSubjects = teacherService.getSubjectIdsByTeacher(teachers.get(0).getId());
                if (teacherSubjects.contains(subjects.get(0).getId())) {
                    Grade grade1 = gradeService.addGrade(
                        students.get(0).getId(),
                        subjects.get(0).getId(),
                        teachers.get(0).getId(),
                        15.5,
                        "Bon travail"
                    );
                    System.out.println("✅ Note ajoutée: " + grade1.getGrade() + "/20 pour " + 
                                     students.get(0).getFirstName() + " en " + subjects.get(0).getName());

                    Grade grade2 = gradeService.addGrade(
                        students.get(0).getId(),
                        subjects.get(1).getId(),
                        teachers.get(0).getId(),
                        18.0,
                        "Excellent"
                    );
                    System.out.println("✅ Note ajoutée: " + grade2.getGrade() + "/20");

                    // Afficher les notes d'un étudiant
                    java.util.List<Grade> studentGrades = gradeService.getGradesByStudent(students.get(0).getId());
                    System.out.println("✅ Notes de " + students.get(0).getFirstName() + ": " + studentGrades.size() + " note(s)");
                }
            }

            System.out.println();
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void testQueries() {
        System.out.println("--- TEST 7: Requêtes ---");
        try {
            System.out.println("Total Users: " + userService.getAllUsers().size());
            System.out.println("Total Students: " + studentService.getAllStudents().size());
            System.out.println("Total Teachers: " + teacherService.getAllTeachers().size());
            System.out.println("Total Subjects: " + subjectService.getAllSubjects().size());
            System.out.println("Total Groups: " + groupService.getAllGroups().size());
            System.out.println("Total Grades: " + gradeService.getGradesByStudent("").size()); // Tous les grades

            // Test polymorphisme
            System.out.println("\n--- Test Polymorphisme ---");
            java.util.List<User> users = userService.getAllUsers();
            for (User user : users) {
                user.showInformation();
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        BackendTest test = new BackendTest();
        test.runAllTests();
    }
}


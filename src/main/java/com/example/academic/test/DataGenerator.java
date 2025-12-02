package com.example.academic.test;

import com.example.academic.model.*;
import com.example.academic.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Générateur de données de test avec noms français et algériens
 */
public class DataGenerator {
    
    private GroupService groupService;
    private SubjectService subjectService;
    private TeacherService teacherService;
    private StudentService studentService;
    private GradeService gradeService;
    private Random random;
    
    // Prénoms mixtes (français et algériens)
    private String[] prenoms = {
        "Jean", "Marie", "Pierre", "Sophie", "Luc", "Isabelle", "Thomas", "Catherine",
        "Nicolas", "Julie", "Antoine", "Emilie", "François", "Camille", "David", "Laura",
        "Julien", "Marion", "Olivier", "Claire", "Vincent", "Amélie", "Sébastien", "Pauline",
        "Mohamed", "Ahmed", "Ali", "Omar", "Youssef", "Hassan", "Karim", "Said",
        "Amine", "Bilal", "Mehdi", "Nassim", "Rachid", "Samir", "Tarek", "Zakaria",
        "Fatima", "Aicha", "Khadija", "Salima", "Nadia", "Samira", "Leila", "Yasmine",
        "Amira", "Djamila", "Soraya", "Malika", "Nour", "Ines"
    };
    
    // Noms mixtes (français et algériens)
    private String[] noms = {
        "Martin", "Bernard", "Dubois", "Thomas", "Robert", "Richard", "Petit", "Durand",
        "Leroy", "Moreau", "Simon", "Laurent", "Lefebvre", "Michel", "Garcia", "David",
        "Bertrand", "Roux", "Vincent", "Fournier", "Morel", "Girard", "André", "Lefevre",
        "Benali", "Bouazza", "Bensaid", "Boumediene", "Cherif", "Dahmani", "Hamdi", "Kadri",
        "Larbi", "Mansouri", "Nacer", "Ouali", "Rahmani", "Saadi", "Taleb", "Ziani",
        "Boukhalfa", "Chaoui", "Dridi", "Fellah", "Guerfi", "Haddad", "Khelifa", "Mazari",
        "Nedjari", "Ouahabi", "Rahmouni", "Slimani", "Touati", "Zerrouki"
    };
    
    // Matières
    private String[][] matieres = {
        {"Mathématiques", "MATH101", "Algèbre, géométrie et analyse"},
        {"Physique", "PHYS101", "Mécanique, thermodynamique et optique"},
        {"Chimie", "CHIM101", "Chimie générale et organique"},
        {"Programmation Java", "JAVA101", "Bases de la programmation orientée objet"},
        {"Bases de Données", "DB101", "SQL, MongoDB et modélisation"},
        {"Réseaux", "RES101", "Architecture réseau et protocoles"},
        {"Systèmes d'Exploitation", "OS101", "Linux, Windows et gestion des processus"},
        {"Algorithmique", "ALGO101", "Structures de données et complexité"},
        {"Anglais Technique", "ANG101", "Communication professionnelle en anglais"},
        {"Gestion de Projet", "GEST101", "Méthodologies agiles et gestion d'équipe"},
        {"Intelligence Artificielle", "IA101", "Machine Learning et Deep Learning"},
        {"Sécurité Informatique", "SEC101", "Cryptographie et sécurité des systèmes"}
    };
    
    // Groupes
    private String[][] groupes = {
        {"Groupe A - L1", "Première année - Groupe A"},
        {"Groupe B - L1", "Première année - Groupe B"},
        {"Groupe C - L1", "Première année - Groupe C"},
        {"Groupe A - L2", "Deuxième année - Groupe A"},
        {"Groupe B - L2", "Deuxième année - Groupe B"},
        {"Groupe A - L3", "Troisième année - Groupe A"},
        {"Groupe B - L3", "Troisième année - Groupe B"},
        {"Groupe Master 1", "Master première année"},
        {"Groupe Master 2", "Master deuxième année"}
    };
    
    public DataGenerator() {
        this.groupService = new GroupService();
        this.subjectService = new SubjectService();
        this.teacherService = new TeacherService();
        this.studentService = new StudentService();
        this.gradeService = new GradeService();
        this.random = new Random();
    }
    
    public void generateAllData() {
        System.out.println("========================================");
        System.out.println("  GÉNÉRATION DE DONNÉES DE TEST");
        System.out.println("========================================\n");
        
        try {
            // 1. Créer les groupes
            System.out.println("📚 Création des groupes...");
            List<Group> groups = createGroups();
            System.out.println("✅ " + groups.size() + " groupes créés\n");
            
            // 2. Créer les matières
            System.out.println("📖 Création des matières...");
            List<Subject> subjects = createSubjects();
            System.out.println("✅ " + subjects.size() + " matières créées\n");
            
            // 3. Créer les enseignants
            System.out.println("👨‍🏫 Création des enseignants...");
            List<Teacher> teachers = createTeachers();
            System.out.println("✅ " + teachers.size() + " enseignants créés\n");
            
            // 4. Créer les étudiants
            System.out.println("🎓 Création des étudiants...");
            List<Student> students = createStudents(groups);
            System.out.println("✅ " + students.size() + " étudiants créés\n");
            
            // 5. Assigner enseignants à matières
            System.out.println("🔗 Assignation enseignants → matières...");
            assignTeachersToSubjects(teachers, subjects);
            System.out.println("✅ Assignations effectuées\n");
            
            // 6. Assigner matières à groupes
            System.out.println("🔗 Assignation matières → groupes...");
            assignSubjectsToGroups(subjects, groups);
            System.out.println("✅ Assignations effectuées\n");
            
            // 7. Créer des notes
            System.out.println("📝 Création des notes...");
            createGrades(teachers, students, subjects);
            System.out.println("✅ Notes créées\n");
            
            System.out.println("========================================");
            System.out.println("  GÉNÉRATION TERMINÉE AVEC SUCCÈS ! ✅");
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la génération: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private List<Group> createGroups() {
        List<Group> groups = new ArrayList<>();
        for (String[] groupe : this.groupes) {
            try {
                Group group = groupService.createGroup(groupe[0], groupe[1]);
                groups.add(group);
                System.out.println("  ✓ " + group.getName());
            } catch (Exception e) {
                // Groupe existe déjà, essayer de le trouver dans la liste
                try {
                    List<Group> allGroups = groupService.getAllGroups();
                    for (Group g : allGroups) {
                        if (g.getName().equals(groupe[0])) {
                            groups.add(g);
                            break;
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("  ✗ Erreur: " + e.getMessage());
                }
            }
        }
        return groups;
    }
    
    private List<Subject> createSubjects() {
        List<Subject> subjects = new ArrayList<>();
        for (String[] matiere : this.matieres) {
            try {
                Subject subject = subjectService.createSubject(matiere[0], matiere[1], matiere[2]);
                subjects.add(subject);
                System.out.println("  ✓ " + subject.getName() + " (" + subject.getCode() + ")");
            } catch (Exception e) {
                // Matière existe déjà, la récupérer
                try {
                    Subject existing = subjectService.findByCode(matiere[1]);
                    subjects.add(existing);
                } catch (Exception ex) {
                    System.err.println("  ✗ Erreur: " + e.getMessage());
                }
            }
        }
        return subjects;
    }
    
    private List<Teacher> createTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        
        // Créer 15 enseignants avec noms mixtes
        int totalTeachers = 15;
        for (int i = 0; i < totalTeachers; i++) {
            String prenom = prenoms[random.nextInt(prenoms.length)];
            String nom = noms[random.nextInt(noms.length)];
            
            try {
                Teacher teacher = teacherService.createTeacher(prenom, nom);
                teachers.add(teacher);
                System.out.println("  ✓ " + teacher.getFirstName() + " " + teacher.getLastName() + 
                                 " (Username: " + teacher.getUsername() + ")");
            } catch (Exception e) {
                System.err.println("  ✗ Erreur pour " + prenom + " " + nom + ": " + e.getMessage());
            }
        }
        
        return teachers;
    }
    
    private List<Student> createStudents(List<Group> groups) {
        List<Student> students = new ArrayList<>();
        
        // Créer 60 étudiants avec noms mixtes
        int totalStudents = 60;
        for (int i = 0; i < totalStudents; i++) {
            String prenom = prenoms[random.nextInt(prenoms.length)];
            String nom = noms[random.nextInt(noms.length)];
            
            // Assigner aléatoirement à un groupe
            String groupId = null;
            if (!groups.isEmpty()) {
                groupId = groups.get(random.nextInt(groups.size())).getId();
            }
            
            try {
                Student student = studentService.createStudent(prenom, nom, groupId);
                students.add(student);
                if (i < 5 || i % 10 == 0) { // Afficher quelques exemples
                    System.out.println("  ✓ " + student.getFirstName() + " " + student.getLastName() + 
                                     " (Username: " + student.getUsername() + ")");
                }
            } catch (Exception e) {
                System.err.println("  ✗ Erreur pour " + prenom + " " + nom + ": " + e.getMessage());
            }
        }
        
        System.out.println("  ... et " + (totalStudents - 5) + " autres étudiants");
        return students;
    }
    
    private void assignTeachersToSubjects(List<Teacher> teachers, List<Subject> subjects) {
        // Chaque enseignant enseigne 2-4 matières
        for (Teacher teacher : teachers) {
            int numSubjects = 2 + random.nextInt(3); // 2 à 4 matières
            List<Subject> assignedSubjects = new ArrayList<>();
            
            for (int i = 0; i < numSubjects && i < subjects.size(); i++) {
                Subject subject = subjects.get(random.nextInt(subjects.size()));
                if (!assignedSubjects.contains(subject)) {
                    try {
                        teacherService.assignSubjectToTeacher(teacher.getId(), subject.getId());
                        assignedSubjects.add(subject);
                        System.out.println("  ✓ " + teacher.getFirstName() + " → " + subject.getName());
                    } catch (Exception e) {
                        // Ignorer si déjà assigné
                    }
                }
            }
        }
    }
    
    private void assignSubjectsToGroups(List<Subject> subjects, List<Group> groups) {
        // Assigner toutes les matières à chaque groupe (ou la plupart)
        for (Group group : groups) {
            for (Subject subject : subjects) {
                try {
                    com.mongodb.client.MongoCollection<org.bson.Document> collection = 
                        com.example.academic.database.MongoDBConnection.getInstance()
                            .getDatabase()
                            .getCollection("group_subjects");
                    
                    org.bson.Document existing = collection.find(
                        new org.bson.Document("groupId", group.getId())
                            .append("subjectId", subject.getId())
                    ).first();
                    
                    if (existing == null) {
                        org.bson.Document doc = new org.bson.Document("groupId", group.getId())
                            .append("subjectId", subject.getId());
                        collection.insertOne(doc);
                    }
                } catch (Exception e) {
                    // Ignorer
                }
            }
            System.out.println("  ✓ " + group.getName() + " → " + subjects.size() + " matières");
        }
    }
    
    private void createGrades(List<Teacher> teachers, List<Student> students, List<Subject> subjects) {
        int totalGrades = 0;
        
        // Pour chaque enseignant, créer des notes pour ses étudiants
        for (Teacher teacher : teachers) {
            try {
                List<String> teacherSubjectIds = teacherService.getSubjectIdsByTeacher(teacher.getId());
                
                // Trouver les étudiants dans les groupes de ce teacher
                List<Student> eligibleStudents = new ArrayList<>();
                for (Student student : students) {
                    if (student.getGroupId() != null && !student.getGroupId().isEmpty()) {
                        eligibleStudents.add(student);
                    }
                }
                
                // Créer 5-15 notes par enseignant
                int numGrades = 5 + random.nextInt(11);
                for (int i = 0; i < numGrades && !teacherSubjectIds.isEmpty() && !eligibleStudents.isEmpty(); i++) {
                    String subjectId = teacherSubjectIds.get(random.nextInt(teacherSubjectIds.size()));
                    Student student = eligibleStudents.get(random.nextInt(eligibleStudents.size()));
                    
                    // Vérifier que le student est dans un group qui a ce subject
                    try {
                        com.mongodb.client.MongoCollection<org.bson.Document> collection = 
                            com.example.academic.database.MongoDBConnection.getInstance()
                                .getDatabase()
                                .getCollection("group_subjects");
                        
                        org.bson.Document relation = collection.find(
                            new org.bson.Document("groupId", student.getGroupId())
                                .append("subjectId", subjectId)
                        ).first();
                        
                        if (relation != null) {
                            double grade = 8 + random.nextDouble() * 12; // Note entre 8 et 20
                            grade = Math.round(grade * 10.0) / 10.0; // Arrondir à 1 décimale
                            
                            String[] comments = {
                                "Bon travail", "Excellent", "Très bien", "Bien", "Satisfaisant",
                                "Peut mieux faire", "À revoir", "Très bon travail", "Bien préparé",
                                "Bon effort", "Continue comme ça", "Excellent rendu"
                            };
                            
                            String comment = comments[random.nextInt(comments.length)];
                            
                            try {
                                gradeService.addGrade(
                                    student.getId(),
                                    subjectId,
                                    teacher.getId(),
                                    grade,
                                    comment
                                );
                                totalGrades++;
                            } catch (Exception e) {
                                // Ignorer si note existe déjà
                            }
                        }
                    } catch (Exception e) {
                        // Ignorer
                    }
                }
            } catch (Exception e) {
                // Ignorer
            }
        }
        
        System.out.println("  ✓ " + totalGrades + " notes créées");
    }
    
    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator();
        generator.generateAllData();
    }
}


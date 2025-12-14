package com.example.academic.test;

import com.example.academic.model.Group;
import com.example.academic.model.Student;
import com.example.academic.service.GroupService;
import com.example.academic.service.StudentService;

/**
 * Tests pour la fonctionnalité d'assignation étudiant-groupe
 */
public class StudentGroupAssignmentTest {
    
    private StudentService studentService;
    private GroupService groupService;
    
    public StudentGroupAssignmentTest() {
        this.studentService = new StudentService();
        this.groupService = new GroupService();
    }
    
    public void runAllTests() {
        System.out.println("========================================");
        System.out.println("  TESTS ASSIGNATION ÉTUDIANT-GROUPE");
        System.out.println("========================================\n");
        
        int passed = 0;
        int failed = 0;
        
        try {
            if (testAssignStudentToGroup()) {
                passed++;
                System.out.println("✅ TEST 1: Assignation étudiant à groupe - RÉUSSI\n");
            } else {
                failed++;
                System.out.println("❌ TEST 1: Assignation étudiant à groupe - ÉCHOUÉ\n");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("❌ TEST 1: Erreur - " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }
        
        try {
            if (testRemoveStudentFromGroup()) {
                passed++;
                System.out.println("✅ TEST 2: Retrait étudiant d'un groupe - RÉUSSI\n");
            } else {
                failed++;
                System.out.println("❌ TEST 2: Retrait étudiant d'un groupe - ÉCHOUÉ\n");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("❌ TEST 2: Erreur - " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }
        
        try {
            if (testRemoveStudentFromGroupWithEmptyString()) {
                passed++;
                System.out.println("✅ TEST 3: Retrait avec chaîne vide - RÉUSSI\n");
            } else {
                failed++;
                System.out.println("❌ TEST 3: Retrait avec chaîne vide - ÉCHOUÉ\n");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("❌ TEST 3: Erreur - " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }
        
        try {
            if (testChangeStudentGroup()) {
                passed++;
                System.out.println("✅ TEST 4: Changement de groupe - RÉUSSI\n");
            } else {
                failed++;
                System.out.println("❌ TEST 4: Changement de groupe - ÉCHOUÉ\n");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("❌ TEST 4: Erreur - " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }
        
        try {
            if (testGetStudentsByGroup()) {
                passed++;
                System.out.println("✅ TEST 5: Récupération étudiants par groupe - RÉUSSI\n");
            } else {
                failed++;
                System.out.println("❌ TEST 5: Récupération étudiants par groupe - ÉCHOUÉ\n");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("❌ TEST 5: Erreur - " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }
        
        try {
            if (testMultipleStudentsSameGroup()) {
                passed++;
                System.out.println("✅ TEST 6: Assignation multiple - RÉUSSI\n");
            } else {
                failed++;
                System.out.println("❌ TEST 6: Assignation multiple - ÉCHOUÉ\n");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("❌ TEST 6: Erreur - " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }
        
        try {
            if (testAssignmentPersistence()) {
                passed++;
                System.out.println("✅ TEST 7: Persistance de l'assignation - RÉUSSI\n");
            } else {
                failed++;
                System.out.println("❌ TEST 7: Persistance de l'assignation - ÉCHOUÉ\n");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("❌ TEST 7: Erreur - " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }
        
        System.out.println("========================================");
        System.out.println("  RÉSULTATS: " + passed + " réussi(s), " + failed + " échoué(s)");
        System.out.println("========================================");
    }
    
    private boolean testAssignStudentToGroup() {
        System.out.println("--- TEST 1: Assigner un étudiant à un groupe ---");
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test Assign", "Description du groupe test");
            if (group == null || group.getId() == null) {
                System.err.println("  ❌ Échec: Groupe non créé");
                return false;
            }
            System.out.println("  ✓ Groupe créé: " + group.getName() + " (ID: " + group.getId() + ")");
            
            // Créer un étudiant
            Student student = studentService.createStudent("TestAssign", "StudentAssign", null);
            if (student == null || student.getId() == null) {
                System.err.println("  ❌ Échec: Étudiant non créé");
                return false;
            }
            if (student.getGroupId() != null) {
                System.err.println("  ❌ Échec: L'étudiant devrait être sans groupe initialement");
                return false;
            }
            System.out.println("  ✓ Étudiant créé: " + student.getFirstName() + " " + student.getLastName());
            
            // Assigner l'étudiant au groupe
            studentService.assignToGroup(student.getId(), group.getId());
            System.out.println("  ✓ Assignation effectuée");
            
            // Vérifier que l'assignation a été effectuée
            Student updatedStudent = studentService.findById(student.getId());
            if (updatedStudent == null) {
                System.err.println("  ❌ Échec: Étudiant non retrouvé après assignation");
                return false;
            }
            if (!group.getId().equals(updatedStudent.getGroupId())) {
                System.err.println("  ❌ Échec: L'étudiant n'est pas assigné au groupe. " +
                    "Attendu: " + group.getId() + ", Obtenu: " + updatedStudent.getGroupId());
                return false;
            }
            System.out.println("  ✓ Vérification: Étudiant assigné au groupe " + group.getName());
            return true;
        } catch (Exception e) {
            System.err.println("  ❌ Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean testRemoveStudentFromGroup() {
        System.out.println("--- TEST 2: Retirer un étudiant d'un groupe (null) ---");
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test Remove", "Description");
            if (group == null) {
                System.err.println("  ❌ Échec: Groupe non créé");
                return false;
            }
            
            // Créer un étudiant avec un groupe
            Student student = studentService.createStudent("TestRemove", "StudentRemove", group.getId());
            if (student == null) {
                System.err.println("  ❌ Échec: Étudiant non créé");
                return false;
            }
            if (!group.getId().equals(student.getGroupId())) {
                System.err.println("  ❌ Échec: L'étudiant devrait avoir un groupe initialement");
                return false;
            }
            System.out.println("  ✓ Étudiant créé avec groupe: " + group.getName());
            
            // Retirer l'étudiant du groupe (assigner à null)
            studentService.assignToGroup(student.getId(), null);
            System.out.println("  ✓ Retrait effectué (null)");
            
            // Vérifier que le retrait a été effectué
            Student updatedStudent = studentService.findById(student.getId());
            if (updatedStudent == null) {
                System.err.println("  ❌ Échec: Étudiant non retrouvé après retrait");
                return false;
            }
            if (updatedStudent.getGroupId() != null) {
                System.err.println("  ❌ Échec: L'étudiant devrait être sans groupe. " +
                    "GroupId actuel: " + updatedStudent.getGroupId());
                return false;
            }
            System.out.println("  ✓ Vérification: Étudiant retiré du groupe");
            return true;
        } catch (Exception e) {
            System.err.println("  ❌ Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean testRemoveStudentFromGroupWithEmptyString() {
        System.out.println("--- TEST 3: Retirer un étudiant d'un groupe (chaîne vide) ---");
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test Remove2", "Description");
            if (group == null) {
                System.err.println("  ❌ Échec: Groupe non créé");
                return false;
            }
            
            // Créer un étudiant avec un groupe
            Student student = studentService.createStudent("TestRemove2", "StudentRemove2", group.getId());
            if (student == null) {
                System.err.println("  ❌ Échec: Étudiant non créé");
                return false;
            }
            System.out.println("  ✓ Étudiant créé avec groupe");
            
            // Retirer l'étudiant du groupe (assigner à chaîne vide)
            studentService.assignToGroup(student.getId(), "");
            System.out.println("  ✓ Retrait effectué (chaîne vide)");
            
            // Vérifier que le retrait a été effectué
            Student updatedStudent = studentService.findById(student.getId());
            if (updatedStudent == null) {
                System.err.println("  ❌ Échec: Étudiant non retrouvé");
                return false;
            }
            if (updatedStudent.getGroupId() != null) {
                System.err.println("  ❌ Échec: L'étudiant devrait être sans groupe. " +
                    "GroupId actuel: " + updatedStudent.getGroupId());
                return false;
            }
            System.out.println("  ✓ Vérification: Étudiant retiré (chaîne vide convertie en null)");
            return true;
        } catch (Exception e) {
            System.err.println("  ❌ Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean testChangeStudentGroup() {
        System.out.println("--- TEST 4: Changer un étudiant de groupe ---");
        try {
            // Créer deux groupes
            Group group1 = groupService.createGroup("Groupe A Change", "Premier groupe");
            Group group2 = groupService.createGroup("Groupe B Change", "Deuxième groupe");
            if (group1 == null || group2 == null) {
                System.err.println("  ❌ Échec: Groupes non créés");
                return false;
            }
            System.out.println("  ✓ Deux groupes créés");
            
            // Créer un étudiant avec le premier groupe
            Student student = studentService.createStudent("TestChange", "StudentChange", group1.getId());
            if (student == null) {
                System.err.println("  ❌ Échec: Étudiant non créé");
                return false;
            }
            if (!group1.getId().equals(student.getGroupId())) {
                System.err.println("  ❌ Échec: Étudiant devrait être dans le premier groupe");
                return false;
            }
            System.out.println("  ✓ Étudiant créé dans " + group1.getName());
            
            // Changer l'étudiant vers le deuxième groupe
            studentService.assignToGroup(student.getId(), group2.getId());
            System.out.println("  ✓ Changement effectué");
            
            // Vérifier le changement
            Student updatedStudent = studentService.findById(student.getId());
            if (updatedStudent == null) {
                System.err.println("  ❌ Échec: Étudiant non retrouvé");
                return false;
            }
            if (!group2.getId().equals(updatedStudent.getGroupId())) {
                System.err.println("  ❌ Échec: Étudiant devrait être dans le deuxième groupe. " +
                    "Attendu: " + group2.getId() + ", Obtenu: " + updatedStudent.getGroupId());
                return false;
            }
            if (group1.getId().equals(updatedStudent.getGroupId())) {
                System.err.println("  ❌ Échec: Étudiant ne devrait plus être dans le premier groupe");
                return false;
            }
            System.out.println("  ✓ Vérification: Étudiant changé vers " + group2.getName());
            return true;
        } catch (Exception e) {
            System.err.println("  ❌ Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean testGetStudentsByGroup() {
        System.out.println("--- TEST 5: Récupérer les étudiants d'un groupe ---");
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test Get", "Description");
            if (group == null) {
                System.err.println("  ❌ Échec: Groupe non créé");
                return false;
            }
            
            // Créer plusieurs étudiants
            Student student1 = studentService.createStudent("TestGet1", "StudentGet1", group.getId());
            Student student2 = studentService.createStudent("TestGet2", "StudentGet2", group.getId());
            Student student3 = studentService.createStudent("TestGet3", "StudentGet3", null);
            
            if (student1 == null || student2 == null || student3 == null) {
                System.err.println("  ❌ Échec: Étudiants non créés");
                return false;
            }
            System.out.println("  ✓ Trois étudiants créés (2 avec groupe, 1 sans)");
            
            // Récupérer les étudiants du groupe
            java.util.List<Student> studentsInGroup = studentService.getStudentsByGroup(group.getId());
            if (studentsInGroup == null) {
                System.err.println("  ❌ Échec: Liste null");
                return false;
            }
            if (studentsInGroup.size() < 2) {
                System.err.println("  ❌ Échec: Devrait contenir au moins 2 étudiants. " +
                    "Trouvé: " + studentsInGroup.size());
                return false;
            }
            System.out.println("  ✓ " + studentsInGroup.size() + " étudiant(s) trouvé(s) dans le groupe");
            
            // Vérifier que les étudiants assignés sont dans la liste
            boolean found1 = studentsInGroup.stream()
                .anyMatch(s -> s.getId().equals(student1.getId()));
            boolean found2 = studentsInGroup.stream()
                .anyMatch(s -> s.getId().equals(student2.getId()));
            
            if (!found1 || !found2) {
                System.err.println("  ❌ Échec: student1 ou student2 non trouvé dans la liste");
                return false;
            }
            
            // Vérifier que l'étudiant sans groupe n'est pas dans la liste
            boolean found3 = studentsInGroup.stream()
                .anyMatch(s -> s.getId().equals(student3.getId()));
            if (found3) {
                System.err.println("  ❌ Échec: student3 ne devrait pas être dans la liste");
                return false;
            }
            
            System.out.println("  ✓ Vérification: Liste correcte");
            return true;
        } catch (Exception e) {
            System.err.println("  ❌ Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean testMultipleStudentsSameGroup() {
        System.out.println("--- TEST 6: Assignation multiple - plusieurs étudiants au même groupe ---");
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test Multiple", "Description");
            if (group == null) {
                System.err.println("  ❌ Échec: Groupe non créé");
                return false;
            }
            
            // Créer plusieurs étudiants sans groupe
            Student student1 = studentService.createStudent("TestMulti1", "StudentMulti1", null);
            Student student2 = studentService.createStudent("TestMulti2", "StudentMulti2", null);
            Student student3 = studentService.createStudent("TestMulti3", "StudentMulti3", null);
            
            if (student1 == null || student2 == null || student3 == null) {
                System.err.println("  ❌ Échec: Étudiants non créés");
                return false;
            }
            System.out.println("  ✓ Trois étudiants créés sans groupe");
            
            // Assigner tous les étudiants au même groupe
            studentService.assignToGroup(student1.getId(), group.getId());
            studentService.assignToGroup(student2.getId(), group.getId());
            studentService.assignToGroup(student3.getId(), group.getId());
            System.out.println("  ✓ Tous les étudiants assignés au groupe");
            
            // Vérifier que tous sont assignés
            Student updated1 = studentService.findById(student1.getId());
            Student updated2 = studentService.findById(student2.getId());
            Student updated3 = studentService.findById(student3.getId());
            
            if (updated1 == null || updated2 == null || updated3 == null) {
                System.err.println("  ❌ Échec: Étudiants non retrouvés");
                return false;
            }
            
            if (!group.getId().equals(updated1.getGroupId()) ||
                !group.getId().equals(updated2.getGroupId()) ||
                !group.getId().equals(updated3.getGroupId())) {
                System.err.println("  ❌ Échec: Tous les étudiants ne sont pas assignés");
                return false;
            }
            
            // Vérifier avec getStudentsByGroup
            java.util.List<Student> studentsInGroup = studentService.getStudentsByGroup(group.getId());
            if (studentsInGroup == null || studentsInGroup.size() < 3) {
                System.err.println("  ❌ Échec: Le groupe devrait contenir au moins 3 étudiants. " +
                    "Trouvé: " + (studentsInGroup != null ? studentsInGroup.size() : 0));
                return false;
            }
            
            System.out.println("  ✓ Vérification: " + studentsInGroup.size() + " étudiant(s) dans le groupe");
            return true;
        } catch (Exception e) {
            System.err.println("  ❌ Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean testAssignmentPersistence() {
        System.out.println("--- TEST 7: Persistance de l'assignation après récupération ---");
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test Persist", "Description");
            if (group == null) {
                System.err.println("  ❌ Échec: Groupe non créé");
                return false;
            }
            
            // Créer un étudiant
            Student student = studentService.createStudent("TestPersist", "StudentPersist", null);
            if (student == null) {
                System.err.println("  ❌ Échec: Étudiant non créé");
                return false;
            }
            if (student.getGroupId() != null) {
                System.err.println("  ❌ Échec: Étudiant devrait être sans groupe initialement");
                return false;
            }
            System.out.println("  ✓ Étudiant créé sans groupe");
            
            // Assigner au groupe
            studentService.assignToGroup(student.getId(), group.getId());
            System.out.println("  ✓ Assignation effectuée");
            
            // Récupérer l'étudiant plusieurs fois pour vérifier la persistance
            Student retrieved1 = studentService.findById(student.getId());
            Student retrieved2 = studentService.findById(student.getId());
            
            if (retrieved1 == null || retrieved2 == null) {
                System.err.println("  ❌ Échec: Étudiants non retrouvés");
                return false;
            }
            
            if (!group.getId().equals(retrieved1.getGroupId()) ||
                !group.getId().equals(retrieved2.getGroupId())) {
                System.err.println("  ❌ Échec: Assignation non persistante");
                return false;
            }
            
            // Vérifier via getAllStudents
            java.util.List<Student> allStudents = studentService.getAllStudents();
            Student found = allStudents.stream()
                .filter(s -> s.getId().equals(student.getId()))
                .findFirst()
                .orElse(null);
            
            if (found == null) {
                System.err.println("  ❌ Échec: Étudiant non trouvé dans getAllStudents");
                return false;
            }
            if (!group.getId().equals(found.getGroupId())) {
                System.err.println("  ❌ Échec: Assignation non persistante dans getAllStudents");
                return false;
            }
            
            System.out.println("  ✓ Vérification: Assignation persistante");
            return true;
        } catch (Exception e) {
            System.err.println("  ❌ Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
        StudentGroupAssignmentTest test = new StudentGroupAssignmentTest();
        test.runAllTests();
    }
}


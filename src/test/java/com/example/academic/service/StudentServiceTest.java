package com.example.academic.service;

import com.example.academic.enums.UserRole;
import com.example.academic.exception.UserNotFoundException;
import com.example.academic.model.Group;
import com.example.academic.model.Student;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour StudentService - Assignation étudiant-groupe
 */
public class StudentServiceTest {
    
    private StudentService studentService;
    private GroupService groupService;
    private UserService userService;
    
    @BeforeEach
    public void setUp() {
        studentService = new StudentService();
        groupService = new GroupService();
        userService = new UserService();
    }
    
    @Test
    @DisplayName("Test: Assigner un étudiant à un groupe")
    public void testAssignStudentToGroup() {
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test", "Description du groupe test");
            assertNotNull(group);
            assertNotNull(group.getId());
            
            // Créer un étudiant
            Student student = studentService.createStudent("Test", "Student", null);
            assertNotNull(student);
            assertNotNull(student.getId());
            assertNull(student.getGroupId(), "L'étudiant ne devrait pas avoir de groupe initialement");
            
            // Assigner l'étudiant au groupe
            studentService.assignToGroup(student.getId(), group.getId());
            
            // Vérifier que l'assignation a été effectuée
            Student updatedStudent = studentService.findById(student.getId());
            assertNotNull(updatedStudent);
            assertEquals(group.getId(), updatedStudent.getGroupId(), 
                "L'étudiant devrait être assigné au groupe");
            
            System.out.println("✅ Test assignation réussie: " + student.getFirstName() + 
                " assigné au groupe " + group.getName());
        } catch (Exception e) {
            fail("Erreur lors du test d'assignation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test: Retirer un étudiant d'un groupe (assigner à null)")
    public void testRemoveStudentFromGroup() {
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test 2", "Description");
            assertNotNull(group);
            
            // Créer un étudiant avec un groupe
            Student student = studentService.createStudent("Test2", "Student2", group.getId());
            assertNotNull(student);
            assertEquals(group.getId(), student.getGroupId(), 
                "L'étudiant devrait avoir un groupe initialement");
            
            // Retirer l'étudiant du groupe (assigner à null)
            studentService.assignToGroup(student.getId(), null);
            
            // Vérifier que le retrait a été effectué
            Student updatedStudent = studentService.findById(student.getId());
            assertNotNull(updatedStudent);
            assertNull(updatedStudent.getGroupId(), 
                "L'étudiant ne devrait plus avoir de groupe");
            
            System.out.println("✅ Test retrait réussie: " + student.getFirstName() + 
                " retiré du groupe");
        } catch (Exception e) {
            fail("Erreur lors du test de retrait: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test: Retirer un étudiant d'un groupe (assigner à chaîne vide)")
    public void testRemoveStudentFromGroupWithEmptyString() {
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test 3", "Description");
            assertNotNull(group);
            
            // Créer un étudiant avec un groupe
            Student student = studentService.createStudent("Test3", "Student3", group.getId());
            assertNotNull(student);
            assertEquals(group.getId(), student.getGroupId());
            
            // Retirer l'étudiant du groupe (assigner à chaîne vide)
            studentService.assignToGroup(student.getId(), "");
            
            // Vérifier que le retrait a été effectué
            Student updatedStudent = studentService.findById(student.getId());
            assertNotNull(updatedStudent);
            assertNull(updatedStudent.getGroupId(), 
                "L'étudiant ne devrait plus avoir de groupe (chaîne vide convertie en null)");
            
            System.out.println("✅ Test retrait avec chaîne vide réussie");
        } catch (Exception e) {
            fail("Erreur lors du test de retrait avec chaîne vide: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test: Changer un étudiant de groupe")
    public void testChangeStudentGroup() {
        try {
            // Créer deux groupes
            Group group1 = groupService.createGroup("Groupe A", "Premier groupe");
            Group group2 = groupService.createGroup("Groupe B", "Deuxième groupe");
            assertNotNull(group1);
            assertNotNull(group2);
            
            // Créer un étudiant avec le premier groupe
            Student student = studentService.createStudent("Test4", "Student4", group1.getId());
            assertNotNull(student);
            assertEquals(group1.getId(), student.getGroupId());
            
            // Changer l'étudiant vers le deuxième groupe
            studentService.assignToGroup(student.getId(), group2.getId());
            
            // Vérifier le changement
            Student updatedStudent = studentService.findById(student.getId());
            assertNotNull(updatedStudent);
            assertEquals(group2.getId(), updatedStudent.getGroupId(), 
                "L'étudiant devrait être dans le deuxième groupe");
            assertNotEquals(group1.getId(), updatedStudent.getGroupId(), 
                "L'étudiant ne devrait plus être dans le premier groupe");
            
            System.out.println("✅ Test changement de groupe réussie");
        } catch (Exception e) {
            fail("Erreur lors du test de changement de groupe: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test: Assigner un étudiant inexistant devrait échouer")
    public void testAssignNonExistentStudent() {
        try {
            Group group = groupService.createGroup("Groupe Test", "Description");
            assertNotNull(group);
            
            // Essayer d'assigner un étudiant avec un ID invalide
            assertThrows(IllegalArgumentException.class, () -> {
                studentService.assignToGroup("invalid_id_12345", group.getId());
            }, "Devrait lancer une exception pour un étudiant inexistant");
            
            System.out.println("✅ Test exception étudiant inexistant réussie");
        } catch (Exception e) {
            fail("Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test: Récupérer les étudiants d'un groupe")
    public void testGetStudentsByGroup() {
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test 5", "Description");
            assertNotNull(group);
            
            // Créer plusieurs étudiants
            Student student1 = studentService.createStudent("Test5a", "Student5a", group.getId());
            Student student2 = studentService.createStudent("Test5b", "Student5b", group.getId());
            Student student3 = studentService.createStudent("Test5c", "Student5c", null);
            
            assertNotNull(student1);
            assertNotNull(student2);
            assertNotNull(student3);
            
            // Récupérer les étudiants du groupe
            java.util.List<Student> studentsInGroup = studentService.getStudentsByGroup(group.getId());
            assertNotNull(studentsInGroup);
            assertTrue(studentsInGroup.size() >= 2, 
                "Devrait contenir au moins les 2 étudiants assignés");
            
            // Vérifier que les étudiants assignés sont dans la liste
            boolean found1 = studentsInGroup.stream()
                .anyMatch(s -> s.getId().equals(student1.getId()));
            boolean found2 = studentsInGroup.stream()
                .anyMatch(s -> s.getId().equals(student2.getId()));
            
            assertTrue(found1, "student1 devrait être dans la liste");
            assertTrue(found2, "student2 devrait être dans la liste");
            
            // Vérifier que l'étudiant sans groupe n'est pas dans la liste
            boolean found3 = studentsInGroup.stream()
                .anyMatch(s -> s.getId().equals(student3.getId()));
            assertFalse(found3, "student3 ne devrait pas être dans la liste");
            
            System.out.println("✅ Test récupération étudiants par groupe réussie: " + 
                studentsInGroup.size() + " étudiant(s) trouvé(s)");
        } catch (Exception e) {
            fail("Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test: Assignation multiple - plusieurs étudiants au même groupe")
    public void testMultipleStudentsSameGroup() {
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test 6", "Description");
            assertNotNull(group);
            
            // Créer plusieurs étudiants sans groupe
            Student student1 = studentService.createStudent("Test6a", "Student6a", null);
            Student student2 = studentService.createStudent("Test6b", "Student6b", null);
            Student student3 = studentService.createStudent("Test6c", "Student6c", null);
            
            // Assigner tous les étudiants au même groupe
            studentService.assignToGroup(student1.getId(), group.getId());
            studentService.assignToGroup(student2.getId(), group.getId());
            studentService.assignToGroup(student3.getId(), group.getId());
            
            // Vérifier que tous sont assignés
            Student updated1 = studentService.findById(student1.getId());
            Student updated2 = studentService.findById(student2.getId());
            Student updated3 = studentService.findById(student3.getId());
            
            assertEquals(group.getId(), updated1.getGroupId());
            assertEquals(group.getId(), updated2.getGroupId());
            assertEquals(group.getId(), updated3.getGroupId());
            
            // Vérifier avec getStudentsByGroup
            java.util.List<Student> studentsInGroup = studentService.getStudentsByGroup(group.getId());
            assertTrue(studentsInGroup.size() >= 3, 
                "Le groupe devrait contenir au moins 3 étudiants");
            
            System.out.println("✅ Test assignation multiple réussie: " + 
                studentsInGroup.size() + " étudiant(s) dans le groupe");
        } catch (Exception e) {
            fail("Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test: Persistance de l'assignation après récupération")
    public void testAssignmentPersistence() {
        try {
            // Créer un groupe
            Group group = groupService.createGroup("Groupe Test 7", "Description");
            assertNotNull(group);
            
            // Créer un étudiant
            Student student = studentService.createStudent("Test7", "Student7", null);
            assertNotNull(student);
            assertNull(student.getGroupId());
            
            // Assigner au groupe
            studentService.assignToGroup(student.getId(), group.getId());
            
            // Récupérer l'étudiant plusieurs fois pour vérifier la persistance
            Student retrieved1 = studentService.findById(student.getId());
            assertEquals(group.getId(), retrieved1.getGroupId());
            
            Student retrieved2 = studentService.findById(student.getId());
            assertEquals(group.getId(), retrieved2.getGroupId());
            
            // Vérifier via getAllStudents
            java.util.List<Student> allStudents = studentService.getAllStudents();
            Student found = allStudents.stream()
                .filter(s -> s.getId().equals(student.getId()))
                .findFirst()
                .orElse(null);
            
            assertNotNull(found);
            assertEquals(group.getId(), found.getGroupId(), 
                "L'assignation devrait être persistante");
            
            System.out.println("✅ Test persistance réussie");
        } catch (Exception e) {
            fail("Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


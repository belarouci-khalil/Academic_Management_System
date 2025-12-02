package com.example.academic.repository;

import com.example.academic.database.MongoDBConnection;
import com.example.academic.exception.DatabaseException;
import com.example.academic.model.Grade;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du repository Grade pour MongoDB
 */
public class GradeRepository implements IGradeRepository {
    private final MongoCollection<Document> collection;

    public GradeRepository() {
        this.collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("grades");
    }

    @Override
    public Grade findById(String id) throws DatabaseException {
        try {
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            if (doc == null) {
                throw new DatabaseException("Grade avec id '" + id + "' non trouvé");
            }
            return Grade.fromDocument(doc);
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la recherche du grade: " + e.getMessage());
        }
    }

    @Override
    public void save(Grade grade) {
        Document doc = grade.toDocument();
        collection.insertOne(doc);
        grade.setId(doc.getObjectId("_id").toString());
    }

    @Override
    public void update(Grade grade) throws DatabaseException {
        try {
            Document doc = grade.toDocument();
            Document filter = new Document("_id", new ObjectId(grade.getId()));
            Document update = new Document("$set", doc);
            update.remove("_id");
            
            Document result = collection.findOneAndUpdate(filter, update);
            if (result == null) {
                throw new DatabaseException("Grade avec id '" + grade.getId() + "' non trouvé");
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la mise à jour du grade: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) throws DatabaseException {
        try {
            Document result = collection.findOneAndDelete(Filters.eq("_id", new ObjectId(id)));
            if (result == null) {
                throw new DatabaseException("Grade avec id '" + id + "' non trouvé");
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la suppression du grade: " + e.getMessage());
        }
    }

    @Override
    public List<Grade> findByStudentId(String studentId) {
        List<Grade> grades = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("studentId", studentId))) {
            grades.add(Grade.fromDocument(doc));
        }
        return grades;
    }

    @Override
    public List<Grade> findBySubjectId(String subjectId) {
        List<Grade> grades = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("subjectId", subjectId))) {
            grades.add(Grade.fromDocument(doc));
        }
        return grades;
    }

    @Override
    public List<Grade> findByTeacherId(String teacherId) {
        List<Grade> grades = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("teacherId", teacherId))) {
            grades.add(Grade.fromDocument(doc));
        }
        return grades;
    }

    @Override
    public List<Grade> findByStudentAndSubject(String studentId, String subjectId) {
        List<Grade> grades = new ArrayList<>();
        Document filter = new Document("studentId", studentId)
                .append("subjectId", subjectId);
        for (Document doc : collection.find(filter)) {
            grades.add(Grade.fromDocument(doc));
        }
        return grades;
    }

    @Override
    public List<Grade> findAll() {
        List<Grade> grades = new ArrayList<>();
        for (Document doc : collection.find()) {
            grades.add(Grade.fromDocument(doc));
        }
        return grades;
    }
}


package com.example.academic.repository;

import com.example.academic.database.MongoDBConnection;
import com.example.academic.exception.DatabaseException;
import com.example.academic.model.Subject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du repository Subject pour MongoDB
 */
public class SubjectRepository implements ISubjectRepository {
    private final MongoCollection<Document> collection;

    public SubjectRepository() {
        this.collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("subjects");
    }

    @Override
    public Subject findById(String id) throws DatabaseException {
        try {
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            if (doc == null) {
                throw new DatabaseException("Subject avec id '" + id + "' non trouvé");
            }
            return Subject.fromDocument(doc);
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la recherche du subject: " + e.getMessage());
        }
    }

    @Override
    public Subject findByCode(String code) throws DatabaseException {
        try {
            Document doc = collection.find(Filters.eq("code", code)).first();
            if (doc == null) {
                throw new DatabaseException("Subject avec code '" + code + "' non trouvé");
            }
            return Subject.fromDocument(doc);
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la recherche du subject: " + e.getMessage());
        }
    }

    @Override
    public void save(Subject subject) {
        Document doc = subject.toDocument();
        collection.insertOne(doc);
        subject.setId(doc.getObjectId("_id").toString());
    }

    @Override
    public void update(Subject subject) throws DatabaseException {
        try {
            Document doc = subject.toDocument();
            Document filter = new Document("_id", new ObjectId(subject.getId()));
            Document update = new Document("$set", doc);
            update.remove("_id");
            
            Document result = collection.findOneAndUpdate(filter, update);
            if (result == null) {
                throw new DatabaseException("Subject avec id '" + subject.getId() + "' non trouvé");
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la mise à jour du subject: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) throws DatabaseException {
        try {
            Document result = collection.findOneAndDelete(Filters.eq("_id", new ObjectId(id)));
            if (result == null) {
                throw new DatabaseException("Subject avec id '" + id + "' non trouvé");
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la suppression du subject: " + e.getMessage());
        }
    }

    @Override
    public List<Subject> findAll() {
        List<Subject> subjects = new ArrayList<>();
        for (Document doc : collection.find()) {
            subjects.add(Subject.fromDocument(doc));
        }
        return subjects;
    }

    @Override
    public boolean existsByCode(String code) {
        return collection.countDocuments(Filters.eq("code", code)) > 0;
    }
}


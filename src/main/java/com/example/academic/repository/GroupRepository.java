package com.example.academic.repository;

import com.example.academic.database.MongoDBConnection;
import com.example.academic.exception.DatabaseException;
import com.example.academic.model.Group;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du repository Group pour MongoDB
 */
public class GroupRepository implements IGroupRepository {
    private final MongoCollection<Document> collection;

    public GroupRepository() {
        this.collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("groups");
    }

    @Override
    public Group findById(String id) throws DatabaseException {
        try {
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            if (doc == null) {
                throw new DatabaseException("Group avec id '" + id + "' non trouvé");
            }
            return Group.fromDocument(doc);
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la recherche du group: " + e.getMessage());
        }
    }

    @Override
    public Group findByName(String name) throws DatabaseException {
        try {
            Document doc = collection.find(Filters.eq("name", name)).first();
            if (doc == null) {
                throw new DatabaseException("Group avec name '" + name + "' non trouvé");
            }
            return Group.fromDocument(doc);
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la recherche du group: " + e.getMessage());
        }
    }

    @Override
    public void save(Group group) {
        Document doc = group.toDocument();
        collection.insertOne(doc);
        group.setId(doc.getObjectId("_id").toString());
    }

    @Override
    public void update(Group group) throws DatabaseException {
        try {
            Document doc = group.toDocument();
            Document filter = new Document("_id", new ObjectId(group.getId()));
            Document update = new Document("$set", doc);
            update.remove("_id");
            
            Document result = collection.findOneAndUpdate(filter, update);
            if (result == null) {
                throw new DatabaseException("Group avec id '" + group.getId() + "' non trouvé");
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la mise à jour du group: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) throws DatabaseException {
        try {
            Document result = collection.findOneAndDelete(Filters.eq("_id", new ObjectId(id)));
            if (result == null) {
                throw new DatabaseException("Group avec id '" + id + "' non trouvé");
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la suppression du group: " + e.getMessage());
        }
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();
        for (Document doc : collection.find()) {
            groups.add(Group.fromDocument(doc));
        }
        return groups;
    }

    @Override
    public boolean existsByName(String name) {
        return collection.countDocuments(Filters.eq("name", name)) > 0;
    }
}


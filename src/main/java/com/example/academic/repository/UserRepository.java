package com.example.academic.repository;

import com.example.academic.database.MongoDBConnection;
import com.example.academic.exception.UserNotFoundException;
import com.example.academic.model.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du repository User pour MongoDB
 */
public class UserRepository implements IUserRepository {
    private final MongoCollection<Document> collection;

    public UserRepository() {
        this.collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("users");
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        // Recherche case-insensitive : chercher tous les users et comparer en minuscules
        for (Document doc : collection.find()) {
            String dbUsername = doc.getString("username");
            if (dbUsername != null && dbUsername.equalsIgnoreCase(username)) {
                return User.fromDocument(doc);
            }
        }
        throw new UserNotFoundException("User avec username '" + username + "' non trouvé");
    }

    @Override
    public User findById(String id) throws UserNotFoundException {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (doc == null) {
            throw new UserNotFoundException("User avec id '" + id + "' non trouvé");
        }
        return User.fromDocument(doc);
    }

    @Override
    public void save(User user) {
        Document doc = user.toDocument();
        collection.insertOne(doc);
        user.setId(doc.getObjectId("_id").toString());
    }

    @Override
    public void update(User user) throws UserNotFoundException {
        Document doc = user.toDocument();
        doc.remove("_id"); // Retirer _id du document avant l'update
        
        Document filter = new Document("_id", new ObjectId(user.getId()));
        Document update = new Document("$set", doc);
        
        // Gérer les champs null pour les Student (comme groupId)
        if (user instanceof Student) {
            Student student = (Student) user;
            Document unsetDoc = new Document();
            
            // Si groupId est null, on doit le retirer de la base de données
            if (student.getGroupId() == null) {
                unsetDoc.append("groupId", "");
            }
            
            // Si on a des champs à unset, les ajouter à l'update
            if (!unsetDoc.isEmpty()) {
                update.append("$unset", unsetDoc);
            }
        }
        
        Document result = collection.findOneAndUpdate(filter, update);
        if (result == null) {
            throw new UserNotFoundException("User avec id '" + user.getId() + "' non trouvé");
        }
    }

    @Override
    public void delete(String id) throws UserNotFoundException {
        Document result = collection.findOneAndDelete(Filters.eq("_id", new ObjectId(id)));
        if (result == null) {
            throw new UserNotFoundException("User avec id '" + id + "' non trouvé");
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            users.add(User.fromDocument(doc));
        }
        return users;
    }

    @Override
    public boolean existsByUsername(String username) {
        // Vérification case-insensitive
        for (Document doc : collection.find()) {
            String dbUsername = doc.getString("username");
            if (dbUsername != null && dbUsername.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
}


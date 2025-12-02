package com.example.academic.model;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Classe Group - Représente un groupe d'étudiants
 * Utilise l'encapsulation
 */
public class Group {
    private String id;
    private String name;
    private String description;

    public Group() {
    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Document toDocument() {
        Document doc = new Document();
        if (id != null && !id.isEmpty()) {
            doc.append("_id", new ObjectId(id));
        }
        doc.append("name", name);
        if (description != null) {
            doc.append("description", description);
        }
        return doc;
    }

    public static Group fromDocument(Document doc) {
        Group group = new Group(
            doc.getString("name"),
            doc.getString("description")
        );
        group.setId(doc.getObjectId("_id").toString());
        return group;
    }

    // Getters et Setters (Encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


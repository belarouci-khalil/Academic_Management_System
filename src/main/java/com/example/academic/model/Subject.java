package com.example.academic.model;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Classe Subject - Représente une matière
 * Utilise l'encapsulation
 */
public class Subject {
    private String id;
    private String name;
    private String code;
    private String description;

    public Subject() {
    }

    public Subject(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public Document toDocument() {
        Document doc = new Document();
        if (id != null && !id.isEmpty()) {
            doc.append("_id", new ObjectId(id));
        }
        doc.append("name", name)
           .append("code", code);
        if (description != null) {
            doc.append("description", description);
        }
        return doc;
    }

    public static Subject fromDocument(Document doc) {
        Subject subject = new Subject(
            doc.getString("name"),
            doc.getString("code"),
            doc.getString("description")
        );
        subject.setId(doc.getObjectId("_id").toString());
        return subject;
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
            throw new IllegalArgumentException("Subject name cannot be empty");
        }
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code cannot be empty");
        }
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


package com.example.academic.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Singleton pour la connexion MongoDB
 */
public class MongoDBConnection {
    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Configuration par défaut
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "academic_system";

    private MongoDBConnection() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Connexion MongoDB établie avec succès!");
        } catch (Exception e) {
            System.err.println("Erreur de connexion MongoDB: " + e.getMessage());
            throw new RuntimeException("Impossible de se connecter à MongoDB", e);
        }
    }

    public static MongoDBConnection getInstance() {
        if (instance == null) {
            synchronized (MongoDBConnection.class) {
                if (instance == null) {
                    instance = new MongoDBConnection();
                }
            }
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Connexion MongoDB fermée");
        }
    }

    // Méthode pour changer la connexion (utile pour les tests)
    public static void setConnectionString(String connectionString, String databaseName) {
        // Cette méthode peut être utilisée pour configurer une autre base de données
    }
}


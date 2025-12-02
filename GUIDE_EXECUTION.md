# 🚀 Guide d'Exécution de l'Interface

## 📋 Prérequis

1. **Java 17+** installé
2. **Maven** installé (ou utilisez le wrapper Maven)
3. **MongoDB** installé et démarré

## 🔧 Étape 1 : Démarrer MongoDB

Avant de lancer l'application, MongoDB doit être en cours d'exécution.

### Windows
```bash
# Si MongoDB est installé comme service, il démarre automatiquement
# Sinon, ouvrez un terminal et exécutez :
mongod
```

### Linux/Mac
```bash
# Démarrer MongoDB
sudo systemctl start mongod
# ou
mongod
```

Vérifiez que MongoDB fonctionne sur `localhost:27017`

---

## 🎯 Étape 2 : Créer des Données de Test (Optionnel mais Recommandé)

Avant de tester l'interface, créez des données de test en exécutant les tests backend :

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.example.academic.test.BackendTest"
```

Cela créera :
- 1 Administrateur (username: "Admin", password: "System")
- 2 Enseignants (username: "Jean", password: "Dupont" et username: "Marie", password: "Martin")
- 3 Étudiants (username: "Pierre", password: "Durand", etc.)
- 3 Groupes
- 3 Matières
- Des assignations et notes

---

## 🖥️ Étape 3 : Lancer l'Interface

### Méthode 1 : Avec Maven (Recommandé)

```bash
# Compiler le projet
mvn compile

# Lancer l'interface
mvn exec:java -Dexec.mainClass="com.example.academic.ui.LoginForm"
```

Ou en une seule commande :
```bash
mvn clean compile exec:java -Dexec.mainClass="com.example.academic.ui.LoginForm"
```

### Méthode 2 : Avec Maven Exec Plugin (Plus Simple)

```bash
mvn exec:java
```

### Méthode 3 : Compiler puis Exécuter avec Java

```bash
# Compiler
mvn compile

# Exécuter
java -cp "target/classes;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-sync\4.11.1\mongodb-driver-sync-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\bson\4.11.1\bson-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-core\4.11.1\mongodb-driver-core-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\bson\4.11.1\bson-4.11.1.jar" com.example.academic.ui.LoginForm
```

**Note :** Cette méthode est complexe car il faut inclure toutes les dépendances. Utilisez plutôt Maven.

### Méthode 4 : Créer un JAR Exécutable

```bash
# Créer un JAR avec toutes les dépendances
mvn clean package

# Exécuter le JAR
java -jar target/java-project-1.0-SNAPSHOT.jar
```

---

## 🔐 Étape 4 : Se Connecter

Une fois l'interface lancée, vous verrez le formulaire de connexion.

### Identifiants de Test (après avoir exécuté BackendTest)

**Administrateur :**
- Username: `Admin`
- Password: `System`

**Enseignant :**
- Username: `Jean`
- Password: `Dupont`

**Étudiant :**
- Username: `Pierre`
- Password: `Durand`

**Note :** Selon les requirements, le username = prénom et password = nom.

---

## 📝 Commandes Rapides

### Script Windows (run.bat)
Créez un fichier `run.bat` :
```batch
@echo off
echo Démarrage de l'application...
mvn clean compile exec:java -Dexec.mainClass="com.example.academic.ui.LoginForm"
pause
```

### Script Linux/Mac (run.sh)
Créez un fichier `run.sh` :
```bash
#!/bin/bash
echo "Démarrage de l'application..."
mvn clean compile exec:java -Dexec.mainClass="com.example.academic.ui.LoginForm"
```

Rendez-le exécutable :
```bash
chmod +x run.sh
./run.sh
```

---

## 🐛 Dépannage

### Erreur : "Impossible de se connecter à MongoDB"
- Vérifiez que MongoDB est démarré : `mongod`
- Vérifiez que le port 27017 est accessible
- Vérifiez la connexion : `mongosh` ou `mongo`

### Erreur : "ClassNotFoundException"
- Compilez d'abord : `mvn compile`
- Vérifiez que toutes les dépendances sont téléchargées : `mvn dependency:resolve`

### Erreur : "NoClassDefFoundError"
- Les dépendances MongoDB ne sont pas dans le classpath
- Utilisez `mvn exec:java` au lieu de `java` directement

### L'interface ne s'affiche pas
- Vérifiez que vous avez Java 17+
- Vérifiez les logs dans la console
- Essayez de lancer avec `mvn clean compile exec:java`

### Pas de données dans l'interface
- Exécutez d'abord `BackendTest` pour créer des données
- Vérifiez que MongoDB contient des données

---

## ✅ Vérification Rapide

1. ✅ MongoDB démarré
2. ✅ Projet compilé (`mvn compile`)
3. ✅ Données de test créées (optionnel)
4. ✅ Interface lancée (`mvn exec:java`)

---

## 🎯 Commandes Utiles

```bash
# Compiler uniquement
mvn compile

# Nettoyer et compiler
mvn clean compile

# Exécuter les tests backend
mvn exec:java -Dexec.mainClass="com.example.academic.test.BackendTest"

# Exécuter l'interface
mvn exec:java -Dexec.mainClass="com.example.academic.ui.LoginForm"

# Voir toutes les dépendances
mvn dependency:tree
```

---

## 💡 Astuce

Pour faciliter l'exécution, vous pouvez créer un script batch/shell qui fait tout automatiquement :
1. Vérifie que MongoDB est démarré
2. Compile le projet
3. Lance l'interface

Bon développement ! 🚀


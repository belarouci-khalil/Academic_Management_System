# Projet Java

Un projet Java simple avec Maven.

## Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur (optionnel)

## Structure du projet

```
.
├── pom.xml                 # Configuration Maven
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── App.java
└── README.md
```

## Compilation

Avec Maven :
```bash
mvn compile
```

Avec javac directement :
```bash
javac src/main/java/com/example/App.java
```

## Exécution

Avec Maven :
```bash
mvn exec:java -Dexec.mainClass="com.example.App"
```

Avec java directement :
```bash
java -cp src/main/java com.example.App
```


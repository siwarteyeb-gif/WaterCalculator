#  WaterCalculator — ThinkGreen

> Mini-projet Java Avancé | Module POO | Thème : ThinkGreen  
> Application de calcul de consommation d'eau adaptée au contexte tunisien.

---

## Description

WaterCalculator est une application JavaFX permettant de :
- Calculer la consommation d'eau selon **4 usages** : Douche, Vaisselle, Arrosage, Agriculture
- Comparer la consommation aux **normes OMS**
- Générer des **recommandations personnalisées**
- Visualiser l'historique via des **graphiques interactifs**

---

##  Architecture

```
WaterCalculator/
├── src/
│   ├── com/watercalculator/
│   │   ├── Main.java                          ← Point d'entrée JavaFX
│   │   ├── models/
│   │   │   ├── User.java                      ← Modèle utilisateur
│   │   │   ├── Consommation.java              ← Classe abstraite (POO)
│   │   │   ├── ConsommationDouche.java        ← Héritage
│   │   │   ├── ConsommationVaisselle.java     ← Héritage
│   │   │   ├── ConsommationArrosage.java      ← Héritage
│   │   │   └── ConsommationAgriculture.java   ← Héritage
│   │   ├── db/
│   │   │   ├── DatabaseConnection.java        ← Singleton JDBC
│   │   │   ├── UserDAO.java                   ← CRUD utilisateurs
│   │   │   └── ConsommationDAO.java           ← CRUD consommations
│   │   ├── controllers/
│   │   │   ├── LoginController.java
│   │   │   ├── RegisterController.java
│   │   │   ├── DashboardController.java
│   │   │   └── CalculatorController.java
│   │   └── utils/
│   │       ├── Session.java                   ← Gestion session
│   │       └── SceneManager.java              ← Navigation entre scènes
│   └── resources/
│       ├── fxml/
│       │   ├── Login.fxml
│       │   ├── Register.fxml
│       │   ├── Dashboard.fxml
│       │   └── Calculator.fxml
│       └── css/
│           └── style.css
└── database.sql                               ← Script BDD complet
```

---

##  Installation (Eclipse)

### 1. Prérequis
- Java JDK 11+
- Eclipse IDE with e(fx)clipse plugin
- MySQL Server (XAMPP ou standalone)
- Fichiers JAR requis dans `/lib` :
  - `javafx-sdk` (tous les jars du dossier lib)
  - `mysql-connector-java-8.x.x.jar`

### 2. Base de données
```sql
-- Dans MySQL Workbench ou phpMyAdmin :
-- Importer le fichier database.sql
source /chemin/vers/database.sql;
```

### 3. Configuration Eclipse
1. **File → New → Java Project** → Nommer `WaterCalculator`
2. Copier tout le contenu dans `src/`
3. **Clic droit projet → Build Path → Configure Build Path**
4. Onglet **Libraries → Add External JARs** → Ajouter tous les JARs JavaFX + MySQL
5. Onglet **Source** → vérifier que `src` est listé
6. **Run Configurations → VM Arguments** :
```
--module-path /chemin/vers/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

### 4. Connexion BDD
Modifier dans `DatabaseConnection.java` :
```java
private static final String URL      = "jdbc:mysql://localhost:3306/watercalculator";
private static final String USER     = "root";
private static final String PASSWORD = ""; 
```

---

## Comptes de test

| Email              | Mot de passe | Ville    |
|--------------------|--------------|----------|
| siwar@gmail.com   | siwar123       | mednine   |
| nadia@gmail.com    |nadia123       | tunis     |

---

##  Concepts POO utilisés

| Concept        | Application                                      |
|----------------|--------------------------------------------------|
| Héritage       | `Consommation` → 4 sous-classes spécialisées     |
| Polymorphisme  | `calculerConsommation()` et `getRecommandation()`|
| Encapsulation  | Getters/Setters sur tous les modèles             |
| Abstraction    | Classe abstraite `Consommation`                  |
| Singleton      | `DatabaseConnection.getInstance()`               |

---

##  Normes OMS utilisées
- Eau domestique (douche) : **50 L/jour/personne**
- Vaisselle : **15 L/personne/repas**
- Arrosage jardin : **10 L/m²**
- Agriculture : **5 000 L/hectare/jour** (varie selon culture)
"# WaterCalculator" 

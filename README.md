<<<<<<< HEAD
# MiniApp-SQL-Java
Mini application en architecture Client-Serveur : « Gestion des Rendez-vous d'un Cabinet Médical »
=======
# 🏥 Système de Gestion de Cabinet Médical (Projet Java)

Ce dépôt contient un **projet universitaire**, développé en *binôme*.  
Il s'agit d'une application **Java avec JavaFX et Oracle DB**, qui simule la gestion complète d'un cabinet médical.

---

## ✨ Fonctionnalités

- 🔐 **Authentification** — Connexion sécurisée pour la secrétaire  
- 👥 **Gestion des Patients** — Ajouter, modifier, supprimer, rechercher  
- 🩺 **Gestion des Médecins** — Ajouter, modifier, supprimer  
- 📅 **Gestion des Rendez-vous** — Planifier, annuler, supprimer, filtrer par médecin  
- 🔍 **Recherche en temps réel** — Par nom ou téléphone dans la liste des patients  
- ✅ **Vérification de disponibilité** — Détecte automatiquement les conflits de créneaux  

---

## 📸 Aperçu de l'Application

| Vue | Description |
|-----|-------------|
| 🔐 Login | Authentification avec identifiant et mot de passe |
| 🏠 Menu Principal | Navigation vers les trois modules |
| 👥 Patients | Tableau CRUD avec recherche dynamique |
| 🩺 Médecins | Tableau CRUD avec gestion des spécialités |
| 📅 Rendez-vous | Formulaire de prise de RDV avec filtre par médecin |

---

## 🗂️ Structure du Projet

    src/
    ├── vue/
    │   ├── MainApp.java          # Point d'entrée JavaFX
    │   ├── LoginView.java        # Vue de connexion
    │   ├── MainView.java         # Menu principal
    │   ├── PatientView.java      # Gestion des patients
    │   ├── MedecinView.java      # Gestion des médecins
    │   ├── RendezVousView.java   # Gestion des rendez-vous
    │   └── AlertUtil.java        # Utilitaire pour les boîtes de dialogue
    ├── modele/
    │   ├── Patient.java          # Modèle Patient
    │   ├── Medecin.java          # Modèle Médecin
    │   └── RendezVous.java       # Modèle RendezVous
    ├── dao/
    │   ├── PatientDAO.java       # Accès aux données Patient
    │   ├── MedecinDAO.java       # Accès aux données Médecin
    │   └── RendezVousDAO.java    # Accès aux données RendezVous
    └── connexion/
        └── ConnexionBD.java      # Connexion JDBC Oracle

## 🛠️ Technologies Utilisées

| Technologie | Rôle |
|-------------|------|
| ☕ Java 17+ | Langage principal |
| 🎨 JavaFX | Interface graphique |
| 🗄️ Oracle XE | Base de données |
| 🔌 JDBC | Connexion Java ↔ Oracle |
| 📦 ojdbc | Driver Oracle pour Java |

---

## ⚙️ Installation et Lancement

### Prérequis
- Java 17 ou supérieur
- JavaFX SDK
- Oracle Database XE
- Driver JDBC Oracle (`ojdbc8.jar`)

### Configuration de la base de données

Exécuter les scripts SQL suivants dans Oracle :

```sql
-- Création des séquences
CREATE SEQUENCE seq_Patient   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_Medecin   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_RendezVous START WITH 1 INCREMENT BY 1;

-- Création des tables
CREATE TABLE Patient (
    Num_Patient    NUMBER PRIMARY KEY,
    Nom            VARCHAR2(50),
    Prenom         VARCHAR2(50),
    Date_Naissance DATE,
    Telephone      VARCHAR2(20),
    Adresse        VARCHAR2(100)
);

CREATE TABLE Medecin (
    Num_Medecin  NUMBER PRIMARY KEY,
    Nom          VARCHAR2(50),
    Prenom       VARCHAR2(50),
    Specialite   VARCHAR2(50),
    Telephone    VARCHAR2(20)
);

CREATE TABLE RendezVous (
    Num_RendezVous        NUMBER PRIMARY KEY,
    Num_Patient           NUMBER REFERENCES Patient(Num_Patient),
    Num_Medecin           NUMBER REFERENCES Medecin(Num_Medecin),
    Date_Heure_RendezVous TIMESTAMP,
    Statut                VARCHAR2(20)
);
```

### Modifier les identifiants de connexion

Dans `connexion/ConnexionBD.java` :

```java
private static final String URL      = "jdbc:oracle:thin:@localhost:1521:XE";
private static final String LOGIN    = "votre_utilisateur";
private static final String PASSWORD = "votre_mot_de_passe";
```

---

## 🎯 Objectif du Projet

Ce projet a été réalisé dans le cadre de notre cours universitaire.  
Il nous a permis de pratiquer et de renforcer nos compétences en :

- Architecture **MVC** (Modèle - Vue - Contrôleur) en Java
- Conception d'interfaces graphiques avec **JavaFX**
- Connexion et manipulation d'une base de données **Oracle** via **JDBC**
- Utilisation des **séquences Oracle** pour la génération automatique des clés primaires
- Implémentation des opérations **CRUD** complètes
- Gestion des **contraintes métier** (disponibilité des médecins, validation des formulaires)

---

## 👥 Auteurs

- Mekhdani Douaa 
- Zyat Maria
---

## 🎓 Informations Académiques

| | |
|---|---|
| **Université** | USTHB — Université des Sciences et de la Technologie Houari Boumediene |
| **Faculté** | Faculté d'Informatique |
| **Module** | Base de Données 1 |
| **Groupe** | L2.ACAD.B |
| **Année** | 2025 / 2026 |
| **Enseignant** | R. Boudour |

---

## 📄 Licence

Ce projet est réalisé à des fins pédagogiques dans le cadre du module **Base de Données 1** à l'**USTHB**.
Vous pouvez librement réutiliser ou adapter le code pour l’apprentissage.
>>>>>>> 8e7bc0f (Correct capitalization of teacher's name)

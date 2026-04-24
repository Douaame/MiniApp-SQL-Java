// ============================================================
// PatientDAO.java
// Classe d'accès aux données pour la table PATIENT.
// Fournit les opérations CRUD : afficher, ajouter, modifier,
// supprimer et rechercher un patient en base Oracle.
// ============================================================

package dao;

import connexion.ConnexionBD;
import modele.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // --------------------------------------------------------
    // Attribut principal
    // --------------------------------------------------------

    /** Connexion partagée à la base de données Oracle */
    private Connection conn;


    // ============================================================
    // CONSTRUCTEUR
    // Récupère la connexion unique via le Singleton ConnexionBD
    // ============================================================
    public PatientDAO() {
        conn = ConnexionBD.getConnexion();
    }


    // ============================================================
    // MÉTHODES CRUD + RECHERCHE
    // ============================================================

    /**
     * Récupère la liste complète des patients depuis la base.
     *
     * @return Liste de tous les patients, vide si aucun résultat
     */
    public List<Patient> afficherPatients() {
        List<Patient> liste = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Patient");

            // Parcours des résultats et construction des objets Patient
            while (rs.next()) {
                Patient p = new Patient(
                    rs.getInt("Num_Patient"),
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getString("Date_Naissance"),
                    rs.getString("Telephone"),
                    rs.getString("Adresse")
                );
                liste.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur afficherPatients : " + e.getMessage());
        }

        return liste;
    }

    /**
     * Insère un nouveau patient en base.
     * Le numéro est généré automatiquement par la séquence Oracle seq_Patient.
     * La date est convertie via TO_DATE() au format DD-MM-YYYY.
     *
     * @param nom           Nom de famille du patient
     * @param prenom        Prénom du patient
     * @param dateNaissance Date de naissance au format DD-MM-YYYY
     * @param telephone     Numéro de téléphone
     * @param adresse       Adresse postale
     */
    public void ajouterPatient(String nom, String prenom, String dateNaissance,
                                String telephone, String adresse) {
        try {
            // TO_DATE() convertit la chaîne saisie en date Oracle
            // seq_Patient.NEXTVAL génère automatiquement la clé primaire
            String sql = "INSERT INTO Patient VALUES " +
                         "(seq_Patient.NEXTVAL, ?, ?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, dateNaissance);
            ps.setString(4, telephone);
            ps.setString(5, adresse);

            ps.executeUpdate();
            System.out.println("Patient ajouté !");

        } catch (SQLException e) {
            System.out.println("Erreur ajouterPatient : " + e.getMessage());
        }
    }

    /**
     * Supprime définitivement un patient de la base.
     *
     * @param numPatient Identifiant du patient à supprimer
     */
    public void supprimerPatient(int numPatient) {
        try {
            String sql = "DELETE FROM Patient WHERE Num_Patient = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, numPatient);
            ps.executeUpdate();
            System.out.println("Patient supprimé !");

        } catch (SQLException e) {
            System.out.println("Erreur supprimerPatient : " + e.getMessage());
        }
    }

    /**
     * Met à jour les informations d'un patient existant.
     * La date de naissance n'est pas modifiable depuis la vue.
     * Le numéro (clé primaire) sert de critère de recherche et ne change pas.
     *
     * @param numPatient Identifiant du patient à modifier
     * @param nom        Nouveau nom de famille
     * @param prenom     Nouveau prénom
     * @param telephone  Nouveau numéro de téléphone
     * @param adresse    Nouvelle adresse postale
     */
    public void modifierPatient(int numPatient, String nom, String prenom,
                                 String telephone, String adresse) {
        try {
            String sql = "UPDATE Patient SET Nom=?, Prenom=?, Telephone=?, Adresse=? " +
                         "WHERE Num_Patient=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, telephone);
            ps.setString(4, adresse);
            ps.setInt(5, numPatient); // critère WHERE — toujours en dernier

            ps.executeUpdate();
            System.out.println("Patient modifié !");

        } catch (SQLException e) {
            System.out.println("Erreur modifierPatient : " + e.getMessage());
        }
    }

    /**
     * Recherche des patients par nom ou téléphone (insensible à la casse).
     * Utilise LIKE avec wildcards pour une recherche partielle.
     * UPPER() côté Oracle assure l'insensibilité à la casse sur le nom.
     *
     * @param recherche Texte saisi dans la barre de recherche
     * @return Liste des patients correspondant au critère, vide si aucun résultat
     */
    public List<Patient> rechercherPatient(String recherche) {
        List<Patient> liste = new ArrayList<>();

        try {
            // UPPER() des deux côtés pour ignorer la casse sur le nom
            // % avant et après pour une recherche "contient"
            String sql = "SELECT * FROM Patient " +
                         "WHERE UPPER(Nom) LIKE UPPER(?) OR Telephone LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + recherche + "%");
            ps.setString(2, "%" + recherche + "%");

            ResultSet rs = ps.executeQuery();

            // Parcours des résultats et construction des objets Patient
            while (rs.next()) {
                Patient p = new Patient(
                    rs.getInt("Num_Patient"),
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getString("Date_Naissance"),
                    rs.getString("Telephone"),
                    rs.getString("Adresse")
                );
                liste.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur rechercherPatient : " + e.getMessage());
        }

        return liste;
    }
}
// ============================================================
// MedecinDAO.java
// Classe d'accès aux données pour la table MEDECIN.
// Fournit les opérations CRUD : afficher, ajouter,
// modifier et supprimer un médecin en base Oracle.
// ============================================================

package dao;

import connexion.ConnexionBD;
import modele.Medecin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {

    // --------------------------------------------------------
    // Attribut principal
    // --------------------------------------------------------

    /** Connexion partagée à la base de données Oracle */
    private Connection conn;


    // ============================================================
    // CONSTRUCTEUR
    // Récupère la connexion unique via le Singleton ConnexionBD
    // ============================================================
    public MedecinDAO() {
        conn = ConnexionBD.getConnexion();
    }


    // ============================================================
    // MÉTHODES CRUD
    // ============================================================

    /**
     * Récupère la liste complète des médecins depuis la base.
     *
     * @return Liste de tous les médecins, vide si aucun résultat
     */
    public List<Medecin> afficherMedecins() {
        List<Medecin> liste = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Medecin");

            // Parcours des résultats et construction des objets Medecin
            while (rs.next()) {
                Medecin m = new Medecin(
                    rs.getInt("Num_Medecin"),
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getString("Specialite"),
                    rs.getString("Telephone")
                );
                liste.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Erreur afficherMedecins : " + e.getMessage());
        }

        return liste;
    }

    /**
     * Insère un nouveau médecin en base.
     * Le numéro est généré automatiquement par la séquence Oracle seq_Medecin.
     *
     * @param nom        Nom de famille du médecin
     * @param prenom     Prénom du médecin
     * @param specialite Spécialité médicale
     * @param telephone  Numéro de téléphone
     */
    public void ajouterMedecin(String nom, String prenom,
                                String specialite, String telephone) {
        try {
            // seq_Medecin.NEXTVAL génère automatiquement la clé primaire
            String sql = "INSERT INTO Medecin VALUES (seq_Medecin.NEXTVAL, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, specialite);
            ps.setString(4, telephone);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur ajouterMedecin : " + e.getMessage());
        }
    }

    /**
     * Met à jour les informations d'un médecin existant.
     * Le numéro (clé primaire) sert de critère de recherche et ne change pas.
     *
     * @param numMedecin Identifiant du médecin à modifier
     * @param nom        Nouveau nom de famille
     * @param prenom     Nouveau prénom
     * @param specialite Nouvelle spécialité
     * @param telephone  Nouveau numéro de téléphone
     */
    public void modifierMedecin(int numMedecin, String nom, String prenom,
                                 String specialite, String telephone) {
        try {
            String sql = "UPDATE Medecin SET Nom=?, Prenom=?, Specialite=?, Telephone=? " +
                         "WHERE Num_Medecin=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, specialite);
            ps.setString(4, telephone);
            ps.setInt(5, numMedecin); // critère WHERE — toujours en dernier

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur modifierMedecin : " + e.getMessage());
        }
    }

    /**
     * Supprime définitivement un médecin de la base.
     *
     * @param numMedecin Identifiant du médecin à supprimer
     */
    public void supprimerMedecin(int numMedecin) {
        try {
            String sql = "DELETE FROM Medecin WHERE Num_Medecin = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, numMedecin);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur supprimerMedecin : " + e.getMessage());
        }
    }
}
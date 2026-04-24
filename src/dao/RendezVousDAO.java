// ============================================================
// RendezVousDAO.java
// Classe d'accès aux données pour la table RENDEZ_VOUS.
// Fournit les opérations : ajouter, afficher, annuler,
// supprimer, filtrer et vérifier la disponibilité des médecins.
// Utilise try-with-resources pour fermer automatiquement
// les connexions après chaque opération.
// ============================================================

package dao;

import connexion.ConnexionBD;
import modele.RendezVous;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {

    // ============================================================
    // MÉTHODES CRUD
    // ============================================================

    /**
     * Insère un nouveau rendez-vous en base.
     * Le numéro est généré automatiquement par la séquence Oracle seq_RendezVous.
     *
     * @param rdv Objet RendezVous contenant les données à insérer
     * @return true si l'insertion a réussi, false en cas d'erreur
     */
    public boolean ajouterRendezVous(RendezVous rdv) {
        String sql = "INSERT INTO RendezVous VALUES (seq_RendezVous.NEXTVAL, ?, ?, ?, ?)";

        try (Connection con = ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rdv.getNumPatient());
            ps.setInt(2, rdv.getNumMedecin());
            ps.setTimestamp(3, rdv.getDateHeureRendezVous());
            ps.setString(4, rdv.getStatut());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur ajouterRendezVous : " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère la liste complète de tous les rendez-vous depuis la base.
     *
     * @return Liste de tous les RDV, vide si aucun résultat
     */
    public List<RendezVous> getTousRendezVous() {
        List<RendezVous> liste = new ArrayList<>();
        String sql = "SELECT * FROM RendezVous";

        try (Connection con = ConnexionBD.getConnexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // Parcours des résultats et construction des objets RendezVous
            while (rs.next()) {
                liste.add(new RendezVous(
                    rs.getInt("Num_RendezVous"),
                    rs.getInt("Num_Patient"),
                    rs.getInt("Num_Medecin"),
                    rs.getTimestamp("Date_Heure_RendezVous"),
                    rs.getString("Statut")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur getTousRendezVous : " + e.getMessage());
        }

        return liste;
    }

    /**
     * Annule un rendez-vous en changeant son statut à "Annulé".
     * Le RDV reste en base mais n'est plus considéré comme actif.
     *
     * @param idRdv Identifiant du rendez-vous à annuler
     * @return true si l'annulation a réussi, false en cas d'erreur
     */
    public boolean annulerRendezVous(int idRdv) {
        String sql = "UPDATE RendezVous SET Statut = 'Annulé' WHERE Num_RendezVous = ?";

        try (Connection con = ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idRdv);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur annulerRendezVous : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime définitivement un rendez-vous de la base.
     *
     * @param idRdv Identifiant du rendez-vous à supprimer
     * @return true si la suppression a réussi, false en cas d'erreur
     */
    public boolean supprimerRendezVous(int idRdv) {
        String sql = "DELETE FROM RendezVous WHERE Num_RendezVous = ?";

        try (Connection con = ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idRdv);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur supprimerRendezVous : " + e.getMessage());
            return false;
        }
    }


    // ============================================================
    // MÉTHODES DE FILTRAGE
    // ============================================================

    /**
     * Récupère tous les rendez-vous d'un médecin donné.
     * Utilisé par le filtre dans RendezVousView.
     *
     * @param idMedecin Identifiant du médecin à filtrer
     * @return Liste des RDV du médecin, vide si aucun résultat
     */
    public List<RendezVous> getRDVParMedecin(int idMedecin) {
        List<RendezVous> liste = new ArrayList<>();
        String sql = "SELECT * FROM RendezVous WHERE Num_Medecin = ?";

        try (Connection con = ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedecin);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(new RendezVous(
                    rs.getInt("Num_RendezVous"),
                    rs.getInt("Num_Patient"),
                    rs.getInt("Num_Medecin"),
                    rs.getTimestamp("Date_Heure_RendezVous"),
                    rs.getString("Statut")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur getRDVParMedecin : " + e.getMessage());
        }

        return liste;
    }

    /**
     * Récupère tous les rendez-vous planifiés pour une date donnée.
     * TRUNC() côté Oracle tronque l'heure pour comparer uniquement la date.
     *
     * @param date Date à filtrer (java.sql.Date)
     * @return Liste des RDV du jour, vide si aucun résultat
     */
    public List<RendezVous> getRDVParDate(Date date) {
        List<RendezVous> liste = new ArrayList<>();
        String sql = "SELECT * FROM RendezVous WHERE TRUNC(Date_Heure_RendezVous) = ?";

        try (Connection con = ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(new RendezVous(
                    rs.getInt("Num_RendezVous"),
                    rs.getInt("Num_Patient"),
                    rs.getInt("Num_Medecin"),
                    rs.getTimestamp("Date_Heure_RendezVous"),
                    rs.getString("Statut")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur getRDVParDate : " + e.getMessage());
        }

        return liste;
    }


    // ============================================================
    // MÉTHODES UTILITAIRES
    // ============================================================

    /**
     * Vérifie si un médecin est disponible à un créneau donné.
     * TRUNC(date, 'MI') tronque à la minute près pour comparer les créneaux.
     * Un médecin est disponible s'il n'a aucun RDV "Planifié" à ce créneau.
     *
     * @param idMedecin Identifiant du médecin à vérifier
     * @param dateHeure Timestamp du créneau souhaité
     * @return true si le médecin est libre, false s'il est déjà occupé
     */
    public boolean isMedecinDisponible(int idMedecin, Timestamp dateHeure) {
        String sql = "SELECT COUNT(*) FROM RendezVous "          +
                     "WHERE Num_Medecin = ? "                    +
                     "AND TRUNC(Date_Heure_RendezVous, 'MI') = TRUNC(?, 'MI') " +
                     "AND Statut = 'Planifié'";

        try (Connection con = ConnexionBD.getConnexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedecin);
            ps.setTimestamp(2, dateHeure);
            ResultSet rs = ps.executeQuery();
            rs.next();

            // COUNT(*) = 0 → aucun RDV existant → médecin disponible
            return rs.getInt(1) == 0;

        } catch (SQLException e) {
            System.out.println("Erreur isMedecinDisponible : " + e.getMessage());
            return false; // par sécurité, on bloque si erreur
        }
    }

    /**
     * Récupère la liste des médecins sous forme "ID - Nom".
     * Utilisé pour alimenter des listes déroulantes textuelles.
     *
     * @return Liste de chaînes au format "1 - Dupont"
     */
    public List<String> getMedecins() {
        List<String> liste = new ArrayList<>();
        String sql = "SELECT Num_Medecin, Nom FROM Medecin";

        try (Connection con = ConnexionBD.getConnexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Format : "1 - Dupont" pour affichage dans un ComboBox texte
                liste.add(rs.getInt("Num_Medecin") + " - " + rs.getString("Nom"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur getMedecins : " + e.getMessage());
        }

        return liste;
    }

    /**
     * Récupère uniquement les identifiants de tous les médecins.
     * Utilisé pour alimenter les ComboBox d'entiers dans RendezVousView.
     *
     * @return Liste des identifiants (Integer) de tous les médecins
     */
    public List<Integer> getAllMedecinIds() {
        List<Integer> liste = new ArrayList<>();
        String sql = "SELECT Num_Medecin FROM Medecin";

        try (Connection con = ConnexionBD.getConnexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(rs.getInt("Num_Medecin"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur getAllMedecinIds : " + e.getMessage());
        }

        return liste;
    }
}
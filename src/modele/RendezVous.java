// ============================================================
// RendezVous.java
// Modèle représentant un rendez-vous entre un patient et un médecin.
// Utilisé par RendezVousDAO et RendezVousView.
// ============================================================

package modele;

import java.sql.Timestamp;

public class RendezVous {

    // --------------------------------------------------------
    // Attributs — correspondent aux colonnes de la table RENDEZ_VOUS
    // --------------------------------------------------------

    /** Identifiant unique du rendez-vous (clé primaire en base) */
    private int numRendezVous;

    /** Référence au patient concerné (clé étrangère → PATIENT) */
    private int numPatient;

    /** Référence au médecin concerné (clé étrangère → MEDECIN) */
    private int numMedecin;

    /** Date et heure du rendez-vous (format SQL Timestamp) */
    private Timestamp dateHeureRendezVous;

    /** Statut du rendez-vous : "Planifié", "Annulé", etc. */
    private String statut;


    // ============================================================
    // CONSTRUCTEURS
    // Deux constructeurs selon le contexte d'utilisation
    // ============================================================

    /**
     * Constructeur complet — utilisé lors de la lecture depuis la base.
     * Inclut le numRendezVous déjà généré par Oracle (séquence/trigger).
     *
     * @param numRendezVous      Identifiant unique du RDV
     * @param numPatient         Identifiant du patient
     * @param numMedecin         Identifiant du médecin
     * @param dateHeureRendezVous Date et heure du RDV
     * @param statut             Statut du RDV
     */
    public RendezVous(int numRendezVous, int numPatient, int numMedecin,
                      Timestamp dateHeureRendezVous, String statut) {
        this.numRendezVous        = numRendezVous;
        this.numPatient           = numPatient;
        this.numMedecin           = numMedecin;
        this.dateHeureRendezVous  = dateHeureRendezVous;
        this.statut               = statut;
    }

    /**
     * Constructeur partiel — utilisé lors de l'insertion en base.
     * Le numRendezVous est omis car généré automatiquement par Oracle.
     *
     * @param numPatient         Identifiant du patient
     * @param numMedecin         Identifiant du médecin
     * @param dateHeureRendezVous Date et heure du RDV
     * @param statut             Statut initial du RDV (ex : "Planifié")
     */
    public RendezVous(int numPatient, int numMedecin,
                      Timestamp dateHeureRendezVous, String statut) {
        this.numPatient           = numPatient;
        this.numMedecin           = numMedecin;
        this.dateHeureRendezVous  = dateHeureRendezVous;
        this.statut               = statut;
    }


    // ============================================================
    // GETTERS
    // Accesseurs en lecture — utilisés par PropertyValueFactory
    // dans RendezVousView pour alimenter les colonnes du TableView
    // ============================================================

    /** @return L'identifiant unique du rendez-vous */
    public int getNumRendezVous()              { return numRendezVous; }

    /** @return L'identifiant du patient concerné */
    public int getNumPatient()                 { return numPatient; }

    /** @return L'identifiant du médecin concerné */
    public int getNumMedecin()                 { return numMedecin; }

    /** @return La date et heure du rendez-vous */
    public Timestamp getDateHeureRendezVous()  { return dateHeureRendezVous; }

    /** @return Le statut actuel du rendez-vous */
    public String getStatut()                  { return statut; }


    // ============================================================
    // SETTERS
    // Mutateurs — permettent la modification après instanciation
    // (ex : mise à jour du statut après annulation)
    // ============================================================

    /** @param numRendezVous Nouvel identifiant du RDV */
    public void setNumRendezVous(int numRendezVous)                    { this.numRendezVous = numRendezVous; }

    /** @param numPatient Nouvel identifiant du patient */
    public void setNumPatient(int numPatient)                          { this.numPatient = numPatient; }

    /** @param numMedecin Nouvel identifiant du médecin */
    public void setNumMedecin(int numMedecin)                          { this.numMedecin = numMedecin; }

    /** @param dateHeureRendezVous Nouvelle date et heure du RDV */
    public void setDateHeureRendezVous(Timestamp dateHeureRendezVous)  { this.dateHeureRendezVous = dateHeureRendezVous; }

    /** @param statut Nouveau statut (ex : "Annulé") */
    public void setStatut(String statut)                               { this.statut = statut; }


    // ============================================================
    // toString()
    // Représentation textuelle du RDV — utile pour le débogage
    // ============================================================

    /**
     * @return Résumé lisible du rendez-vous sous forme de chaîne
     */
    @Override
    public String toString() {
        return "RDV #"       + numRendezVous  +
               " | Patient: " + numPatient     +
               " | Médecin: " + numMedecin     +
               " | "          + dateHeureRendezVous +
               " | "          + statut;
    }
}
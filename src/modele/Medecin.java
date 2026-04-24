// ============================================================
// Medecin.java
// Modèle représentant un médecin du cabinet.
// Utilisé par MedecinDAO et MedecinView.
// ============================================================

package modele;

public class Medecin {

    // --------------------------------------------------------
    // Attributs — correspondent aux colonnes de la table MEDECIN
    // --------------------------------------------------------

    /** Identifiant unique du médecin (clé primaire en base) */
    private int numMedecin;

    /** Nom de famille du médecin */
    private String nom;

    /** Prénom du médecin */
    private String prenom;

    /** Spécialité médicale (ex : Cardiologue, Généraliste...) */
    private String specialite;

    /** Numéro de téléphone du médecin */
    private String telephone;


    // ============================================================
    // CONSTRUCTEUR
    // Initialise tous les attributs à partir des données de la base
    // ============================================================

    /**
     * @param numMedecin  Identifiant unique du médecin
     * @param nom         Nom de famille
     * @param prenom      Prénom
     * @param specialite  Spécialité médicale
     * @param telephone   Numéro de téléphone
     */
    public Medecin(int numMedecin, String nom, String prenom,
                   String specialite, String telephone) {
        this.numMedecin  = numMedecin;
        this.nom         = nom;
        this.prenom      = prenom;
        this.specialite  = specialite;
        this.telephone   = telephone;
    }


    // ============================================================
    // GETTERS
    // Accesseurs en lecture seule — utilisés par PropertyValueFactory
    // dans MedecinView pour alimenter les colonnes du TableView
    // ============================================================

    /** @return L'identifiant unique du médecin */
    public int getNumMedecin()    { return numMedecin; }

    /** @return Le nom de famille du médecin */
    public String getNom()        { return nom; }

    /** @return Le prénom du médecin */
    public String getPrenom()     { return prenom; }

    /** @return La spécialité médicale du médecin */
    public String getSpecialite() { return specialite; }

    /** @return Le numéro de téléphone du médecin */
    public String getTelephone()  { return telephone; }
}

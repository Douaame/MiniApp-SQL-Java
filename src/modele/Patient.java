// ============================================================
// Patient.java
// Modèle représentant un patient du cabinet médical.
// Utilisé par PatientDAO et PatientView.
// ============================================================

package modele;

public class Patient {

    // --------------------------------------------------------
    // Attributs — correspondent aux colonnes de la table PATIENT
    // --------------------------------------------------------

    /** Identifiant unique du patient (clé primaire en base) */
    private int numPatient;

    /** Nom de famille du patient */
    private String nom;

    /** Prénom du patient */
    private String prenom;

    /** Date de naissance au format DD-MM-YYYY */
    private String dateNaissance;

    /** Numéro de téléphone du patient */
    private String telephone;

    /** Adresse postale du patient */
    private String adresse;


    // ============================================================
    // CONSTRUCTEUR
    // Initialise tous les attributs à partir des données de la base
    // ============================================================

    /**
     * @param numPatient    Identifiant unique du patient
     * @param nom           Nom de famille
     * @param prenom        Prénom
     * @param dateNaissance Date de naissance (format DD-MM-YYYY)
     * @param telephone     Numéro de téléphone
     * @param adresse       Adresse postale
     */
    public Patient(int numPatient, String nom, String prenom,
                   String dateNaissance, String telephone, String adresse) {
        this.numPatient    = numPatient;
        this.nom           = nom;
        this.prenom        = prenom;
        this.dateNaissance = dateNaissance;
        this.telephone     = telephone;
        this.adresse       = adresse;
    }


    // ============================================================
    // GETTERS
    // Accesseurs en lecture seule — utilisés par PropertyValueFactory
    // dans PatientView pour alimenter les colonnes du TableView
    // ============================================================

    /** @return L'identifiant unique du patient */
    public int getNumPatient()       { return numPatient; }

    /** @return Le nom de famille du patient */
    public String getNom()           { return nom; }

    /** @return Le prénom du patient */
    public String getPrenom()        { return prenom; }

    /** @return La date de naissance au format DD-MM-YYYY */
    public String getDateNaissance() { return dateNaissance; }

    /** @return Le numéro de téléphone du patient */
    public String getTelephone()     { return telephone; }

    /** @return L'adresse postale du patient */
    public String getAdresse()       { return adresse; }
}
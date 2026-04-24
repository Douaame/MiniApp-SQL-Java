// ============================================================
// ConnexionBD.java
// Classe utilitaire de connexion à la base de données Oracle.
// Fournit une connexion JDBC à chaque appel via getConnexion().
// Utilisée par tous les DAO du projet.
// ============================================================

package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD {

    // --------------------------------------------------------
    // Paramètres de connexion Oracle
    // --------------------------------------------------------

    /** URL JDBC de la base Oracle locale
     *  Format : jdbc:oracle:thin:@[hôte]:[port]:[SID] */
    private static final String URL      = "jdbc:oracle:thin:@localhost:1521:XE";

    /** Nom d'utilisateur Oracle */
    private static final String LOGIN    = "Mekhdani";

    /** Mot de passe Oracle */
    private static final String PASSWORD = "Douaa";


    // ============================================================
    // MÉTHODE PRINCIPALE : getConnexion()
    // Crée et retourne une nouvelle connexion à chaque appel.
    // Appelée dans chaque DAO via try-with-resources.
    // ============================================================

    /**
     * Établit une connexion JDBC à la base de données Oracle.
     * Charge d'abord le driver Oracle, puis ouvre la connexion.
     *
     * @return Un objet Connection actif, ou null en cas d'échec
     */
    public static Connection getConnexion() {
        try {
            // Chargement du driver JDBC Oracle
            // Nécessaire pour enregistrer le driver avant toute connexion
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Ouverture de la connexion avec les paramètres définis
            Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            System.out.println("Connexion réussie !");
            return connexion;

        } catch (ClassNotFoundException e) {
            // Driver Oracle absent du classpath (ojdbc manquant dans les libs)
            System.out.println("Driver introuvable : " + e.getMessage());
            return null;

        } catch (SQLException e) {
            // Échec de connexion : mauvais identifiants, base arrêtée, port incorrect...
            System.out.println("Erreur de connexion : " + e.getMessage());
            return null;
        }
    }
}
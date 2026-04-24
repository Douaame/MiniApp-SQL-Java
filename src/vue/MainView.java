// ============================================================
// MainView.java
// Vue du menu principal : point de navigation central vers les
// modules Patients, Médecins, Rendez-vous et Déconnexion.
// ============================================================

package vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {

    // ============================================================
    // MÉTHODE PRINCIPALE : afficher()
    // Point d'entrée de la vue — construit et affiche le menu
    // ============================================================
    public void afficher(Stage stage) {

        // --------------------------------------------------------
        // 1. EN-TÊTE : LOGO + TITRE + SOUS-TITRE
        // Identité visuelle du cabinet affichée en haut de la fenêtre
        // --------------------------------------------------------

    	 // Logo du cabinet (chargé depuis les ressources)
        ImageView logo = new ImageView(
            new Image(getClass().getResourceAsStream("/images/logo.png"))
        );
        logo.setFitWidth(100);
        logo.setFitHeight(100);

        // Titre principal du cabinet
        Label titre = new Label("Cabinet Médical");
        titre.setStyle(
            "-fx-font-size: 32px;"         +
            "-fx-font-weight: bold;"        +
            "-fx-text-fill: #2e7d32;"       +  // vert foncé
            "-fx-font-family: 'Segoe UI';"
        );

        // Sous-titre décrivant le système
        Label sousTitre = new Label("Système de gestion des rendez-vous");
        sousTitre.setStyle(
            "-fx-font-size: 13px;"         +
            "-fx-text-fill: #66bb6a;"      +  // vert clair
            "-fx-font-family: 'Segoe UI';"
        );

        // Regroupement logo + titre + sous-titre dans un bloc centré
        VBox header = new VBox(5, logo, titre, sousTitre);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));

        // --------------------------------------------------------
        // 2. CRÉATION DES BOUTONS DE NAVIGATION
        // Chaque bouton charge une icône et redirige vers un module
        // --------------------------------------------------------

        // Boutons principaux avec icônes (via creerBouton)
        Button btnPatients   = creerBouton("Gestion des Patients",    "/images/patients.png");
        Button btnMedecins   = creerBouton("Gestion des Médecins",    "/images/medecins.png");
        Button btnRendezVous = creerBouton("Gestion des Rendez-vous", "/images/rdv.png");

        // Bouton de déconnexion — style rouge pour le distinguer
        Button btnLogout = creerBouton("Déconnexion", "/images/logout.png");
        btnLogout.setStyle(
            "-fx-background-color: #ef5350;" +  // rouge
            "-fx-text-fill: white;"           +
            "-fx-font-size: 12px;"            +
            "-fx-padding: 6 16;"              +
            "-fx-background-radius: 8;"
        );

        // --------------------------------------------------------
        // 3. LIAISON DES BOUTONS À LEURS VUES RESPECTIVES
        // Chaque bouton remplace la scène courante par le module cible
        // --------------------------------------------------------
        btnPatients.setOnAction(e   -> new PatientView().afficher(stage));
        btnMedecins.setOnAction(e   -> new MedecinView().afficher(stage));
        btnRendezVous.setOnAction(e -> new RendezVousView().afficher(stage));

        // Déconnexion → retour à la vue de login
        btnLogout.setOnAction(e -> new LoginView().afficher(stage));

        // --------------------------------------------------------
        // 4. ASSEMBLAGE DE LA MISE EN PAGE (Layout)
        // Structure verticale centrée : header → boutons → déconnexion
        // --------------------------------------------------------

        // Groupe des boutons principaux de navigation
        VBox boutons = new VBox(15, btnPatients, btnMedecins, btnRendezVous);
        boutons.setAlignment(Pos.CENTER);
        boutons.setPadding(new Insets(20));

        // Conteneur principal — btnLogout séparé en bas pour le distinguer
        VBox root = new VBox(20, header, boutons, btnLogout);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f0fff4;"); // vert très clair

        // --------------------------------------------------------
        // 5. CONFIGURATION ET AFFICHAGE DE LA FENÊTRE
        // --------------------------------------------------------
        Scene scene = new Scene(root, 450, 500);

        stage.setTitle("Cabinet Médical");
        stage.setScene(scene);
        stage.show();
    }


    // ============================================================
    // MÉTHODES UTILITAIRES PRIVÉES
    // ============================================================

    /**
     * Crée un bouton stylisé avec une icône chargée depuis les ressources.
     * Si l'image est introuvable, le bouton s'affiche sans icône (pas de crash).
     *
     * @param texte     Libellé affiché sur le bouton
     * @param imagePath Chemin de l'icône dans les ressources (ex: "/images/rdv.png")
     * @return          Le bouton stylisé avec icône prêt à l'emploi
     */
    private Button creerBouton(String texte, String imagePath) {
        Button btn = new Button(texte);

        // Tentative de chargement de l'icône — silencieux si absente
        try {
            ImageView icon = new ImageView(
                new Image(getClass().getResourceAsStream(imagePath))
            );
            icon.setFitWidth(24);
            icon.setFitHeight(24);
            btn.setGraphic(icon);
        } catch (Exception e) {
            // Image non trouvée : le bouton reste fonctionnel sans icône
            System.out.println("Image non trouvée : " + imagePath);
        }

        // Style commun à tous les boutons de navigation
        btn.setPrefWidth(280);
        btn.setStyle(
            "-fx-background-color: #81c784;" +  // vert moyen
            "-fx-text-fill: white;"           +
            "-fx-font-size: 14px;"            +
            "-fx-padding: 10 20;"             +
            "-fx-background-radius: 8;"
        );

        return btn;
    }
}
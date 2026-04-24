// ============================================================
// LoginView.java
// Vue de connexion : authentification de la secrétaire avant
// d'accéder au menu principal du cabinet médical.
// ============================================================

package vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {

    public void afficher(Stage stage) {

        // --------------------------------------------------------
        // 1. LOGO
        // --------------------------------------------------------
        ImageView logo = new ImageView(
            new Image(getClass().getResourceAsStream("/images/login.png"))
        );
        logo.setFitWidth(90);
        logo.setFitHeight(90);

        // --------------------------------------------------------
        // 2. EN-TÊTE : TITRE ET SOUS-TITRE
        // --------------------------------------------------------
        Label titre = new Label("Cabinet Médical");
        titre.setStyle(
            "-fx-font-size: 26px;"        +
            "-fx-font-weight: bold;"       +
            "-fx-text-fill: #2e7d32;"      +
            "-fx-font-family: 'Segoe UI';"
        );

        Label sousTitre = new Label("Espace Secrétaire");
        sousTitre.setStyle(
            "-fx-font-size: 13px;"        +
            "-fx-text-fill: #66bb6a;"     +
            "-fx-font-family: 'Segoe UI';"
        );

        // --------------------------------------------------------
        // 3. CHAMPS DE SAISIE
        // --------------------------------------------------------
        TextField tfLogin = new TextField();
        tfLogin.setPromptText("Login");
        tfLogin.setMaxWidth(250);

        PasswordField tfPassword = new PasswordField();
        tfPassword.setPromptText("Mot de passe");
        tfPassword.setMaxWidth(250);

        // --------------------------------------------------------
        // 4. BOUTON DE CONNEXION
        // --------------------------------------------------------
        Button btnConnecter = new Button("Se connecter");
        btnConnecter.setStyle(
            "-fx-background-color: #66bb6a;" +
            "-fx-text-fill: white;"           +
            "-fx-font-size: 14px;"            +
            "-fx-padding: 10 30;"             +
            "-fx-background-radius: 8;"
        );

        // --------------------------------------------------------
        // 5. LABEL D'ERREUR
        // --------------------------------------------------------
        Label lblErreur = new Label("");
        lblErreur.setStyle("-fx-text-fill: red;");

        // --------------------------------------------------------
        // 6. ACTION : VÉRIFICATION DES IDENTIFIANTS
        // --------------------------------------------------------
        btnConnecter.setOnAction(e -> {
            String login    = tfLogin.getText();
            String password = tfPassword.getText();

            if (login.equals("Aurea_Care") && password.equals("1234")) {
                new MainView().afficher(stage);
            } else {
                lblErreur.setText("Login ou mot de passe incorrect !");
            }
        });

        // --------------------------------------------------------
        // 7. TOUCHE ENTRÉE — raccourci clavier
        // Fonctionne depuis les deux champs
        // --------------------------------------------------------
        tfLogin.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER)
                btnConnecter.fire();
        });

        tfPassword.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER)
                btnConnecter.fire();
        });

        // --------------------------------------------------------
        // 8. ASSEMBLAGE DE LA MISE EN PAGE
        // --------------------------------------------------------
        VBox root = new VBox(15,
            logo, titre, sousTitre,
            tfLogin, tfPassword,
            btnConnecter, lblErreur
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f0fff4;");

        // --------------------------------------------------------
        // 9. CONFIGURATION ET AFFICHAGE DE LA FENÊTRE
        // --------------------------------------------------------
        Scene scene = new Scene(root, 400, 430);
        scene.getStylesheets().add(
            getClass().getResource("/images/style.css").toExternalForm()
        );

        // Icône personnalisée dans la barre de titre
        stage.getIcons().add(
            new Image(getClass().getResourceAsStream("/images/Icon.png"))
        );
        stage.setTitle("Connexion — Cabinet Médical");
        stage.setScene(scene);
        stage.show();
    }
}
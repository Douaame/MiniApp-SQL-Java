// ============================================================
// MedecinView.java
// Vue de gestion des médecins : affichage, ajout, modification
// et suppression via une interface JavaFX.
// ============================================================

package vue;

import dao.MedecinDAO;
import modele.Medecin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MedecinView {

    // --------------------------------------------------------
    // Attributs principaux
    // --------------------------------------------------------

    /** Tableau d'affichage des médecins */
    private TableView<Medecin> table = new TableView<>();

    /** Objet d'accès aux données (DAO) pour les opérations en base */
    private MedecinDAO dao = new MedecinDAO();


    // ============================================================
    // MÉTHODE PRINCIPALE : afficher()
    // ============================================================
    public void afficher(Stage stage) {

        // --------------------------------------------------------
        // 1. CONFIGURATION DES COLONNES DU TABLEAU
        // --------------------------------------------------------
        TableColumn<Medecin, Integer> colNum = new TableColumn<>("N°");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numMedecin"));
        colNum.setPrefWidth(50);

        TableColumn<Medecin, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setPrefWidth(185);

        TableColumn<Medecin, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colPrenom.setPrefWidth(185);

        TableColumn<Medecin, String> colSpec = new TableColumn<>("Spécialité");
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        colSpec.setPrefWidth(200);

        TableColumn<Medecin, String> colTel = new TableColumn<>("Téléphone");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colTel.setPrefWidth(130);

        table.getColumns().add(colNum);
        table.getColumns().add(colNom);
        table.getColumns().add(colPrenom);
        table.getColumns().add(colSpec);
        table.getColumns().add(colTel);

        // Le tableau s'étire pour remplir l'espace disponible
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        // --------------------------------------------------------
        // 2. CHARGEMENT INITIAL DES DONNÉES
        // --------------------------------------------------------
        chargerMedecins();

        // --------------------------------------------------------
        // 3. TITRE DE LA PAGE
        // --------------------------------------------------------
        Label titrePage = new Label("Gestion des Médecins");
        titrePage.setStyle(
            "-fx-font-size: 20px;"        +
            "-fx-font-weight: bold;"       +
            "-fx-text-fill: #2e7d32;"      +
            "-fx-font-family: 'Segoe UI';"
        );
        HBox headerBox = new HBox(titrePage);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10, 20, 0, 20));

        // --------------------------------------------------------
        // 4. CRÉATION DES BOUTONS D'ACTION
        // --------------------------------------------------------
        Button btnAjouter   = styliserBouton("Ajouter",   "#66bb6a");
        Button btnModifier  = styliserBouton("Modifier",  "#ffb74d");
        Button btnSupprimer = styliserBouton("Supprimer", "#ef5350");
        Button btnRetour    = styliserBouton("Retour",    "#90a4ae");

        // --------------------------------------------------------
        // 5. ACTION : AJOUTER UN MÉDECIN
        // --------------------------------------------------------
        btnAjouter.setOnAction(e -> {
            Stage stageAjouter = new Stage();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            TextField tfNom    = new TextField();
            TextField tfPrenom = new TextField();
            TextField tfSpec   = new TextField();
            TextField tfTel    = new TextField();

            grid.add(new Label("Nom :"),        0, 0); grid.add(tfNom,    1, 0);
            grid.add(new Label("Prénom :"),      0, 1); grid.add(tfPrenom, 1, 1);
            grid.add(new Label("Spécialité :"), 0, 2); grid.add(tfSpec,   1, 2);
            grid.add(new Label("Téléphone :"),  0, 3); grid.add(tfTel,    1, 3);

            // Bouton Valider stylisé
            Button btnValider = styliserBouton("Valider", "#66bb6a");
            grid.add(btnValider, 1, 4);

            btnValider.setOnAction(ev -> {
                dao.ajouterMedecin(
                    tfNom.getText(),
                    tfPrenom.getText(),
                    tfSpec.getText(),
                    tfTel.getText()
                );
                chargerMedecins();
                stageAjouter.close();
            });

            // Touche ENTER pour valider
            grid.setOnKeyPressed(ev -> {
                if (ev.getCode() == javafx.scene.input.KeyCode.ENTER)
                    btnValider.fire();
            });

            Scene sceneAjouter = new Scene(grid, 350, 230);
            appliquerThemePopup(stageAjouter, sceneAjouter, grid);

            stageAjouter.setTitle("Ajouter un Médecin");
            stageAjouter.setScene(sceneAjouter);
            stageAjouter.show();
        });

        // --------------------------------------------------------
        // 6. ACTION : MODIFIER UN MÉDECIN
        // --------------------------------------------------------
        btnModifier.setOnAction(e -> {
            Medecin medecinSelectionne = table.getSelectionModel().getSelectedItem();

            // Aucun médecin sélectionné → avertissement
            if (medecinSelectionne == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setContentText("Veuillez sélectionner un médecin !");
                alert.showAndWait();
                return;
            }

            Stage stageModifier = new Stage();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            // Champs pré-remplis avec les données actuelles
            TextField tfNom    = new TextField(medecinSelectionne.getNom());
            TextField tfPrenom = new TextField(medecinSelectionne.getPrenom());
            TextField tfSpec   = new TextField(medecinSelectionne.getSpecialite());
            TextField tfTel    = new TextField(medecinSelectionne.getTelephone());

            grid.add(new Label("Nom :"),        0, 0); grid.add(tfNom,    1, 0);
            grid.add(new Label("Prénom :"),      0, 1); grid.add(tfPrenom, 1, 1);
            grid.add(new Label("Spécialité :"), 0, 2); grid.add(tfSpec,   1, 2);
            grid.add(new Label("Téléphone :"),  0, 3); grid.add(tfTel,    1, 3);

            // Bouton Valider stylisé
            Button btnValider = styliserBouton("Valider", "#66bb6a");
            grid.add(btnValider, 1, 4);

            btnValider.setOnAction(ev -> {
                dao.modifierMedecin(
                    medecinSelectionne.getNumMedecin(), // clé primaire (non modifiable)
                    tfNom.getText(),
                    tfPrenom.getText(),
                    tfSpec.getText(),
                    tfTel.getText()
                );
                chargerMedecins();
                stageModifier.close();
            });

            // Touche ENTER pour valider
            grid.setOnKeyPressed(ev -> {
                if (ev.getCode() == javafx.scene.input.KeyCode.ENTER)
                    btnValider.fire();
            });

            Scene sceneModifier = new Scene(grid, 350, 230);
            appliquerThemePopup(stageModifier, sceneModifier, grid);

            stageModifier.setTitle("Modifier un Médecin");
            stageModifier.setScene(sceneModifier);
            stageModifier.show();
        });

        // --------------------------------------------------------
        // 7. ACTION : SUPPRIMER UN MÉDECIN
        // --------------------------------------------------------
        btnSupprimer.setOnAction(e -> {
            Medecin medecinSelectionne = table.getSelectionModel().getSelectedItem();

            if (medecinSelectionne == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setContentText("Veuillez sélectionner un médecin !");
                alert.showAndWait();
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setContentText("Voulez-vous vraiment supprimer ce médecin ?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    dao.supprimerMedecin(medecinSelectionne.getNumMedecin());
                    chargerMedecins();
                }
            });
        });

        // --------------------------------------------------------
        // 8. ACTION : RETOUR AU MENU PRINCIPAL
        // --------------------------------------------------------
        btnRetour.setOnAction(e -> new MainView().afficher(stage));

        // --------------------------------------------------------
        // 9. ASSEMBLAGE DE LA MISE EN PAGE
        // --------------------------------------------------------

        // Tableau dans un VBox avec marges pour qu'il ne colle pas aux bords
        VBox centerBox = new VBox(10, headerBox, table);
        centerBox.setPadding(new Insets(10, 20, 10, 20));
        VBox.setVgrow(table, Priority.ALWAYS);

        // Barre de boutons en bas
        HBox boutons = new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnRetour);
        boutons.setPadding(new Insets(10, 20, 10, 20));

        // Conteneur principal
        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setBottom(boutons);
        root.setStyle("-fx-background-color: #f0f4ff;");

        // --------------------------------------------------------
        // 10. CONFIGURATION ET AFFICHAGE DE LA FENÊTRE
        // --------------------------------------------------------
        Scene scene = new Scene(root, 780, 520);
        scene.getStylesheets().add(
            getClass().getResource("/images/style.css").toExternalForm()
        );

        stage.setTitle("Gestion des Médecins");
        stage.setScene(scene);
        stage.show();
    }


    // ============================================================
    // MÉTHODES UTILITAIRES PRIVÉES
    // ============================================================

    /**
     * Charge la liste complète des médecins et met à jour le tableau.
     */
    private void chargerMedecins() {
        ObservableList<Medecin> liste = FXCollections.observableArrayList(
            dao.afficherMedecins()
        );
        table.setItems(liste);
    }

    /**
     * Applique le thème visuel (fond + CSS + icône) sur une fenêtre pop-up.
     *
     * @param stage  La fenêtre pop-up à styliser
     * @param scene  La scène de la fenêtre
     * @param grid   Le GridPane racine du formulaire
     */
    private void appliquerThemePopup(Stage stage, Scene scene, GridPane grid) {
        // Fond vert clair cohérent avec les autres vues
        grid.setStyle("-fx-background-color: #f0fff4;");

        // Chargement du CSS global
        scene.getStylesheets().add(
            getClass().getResource("/images/style.css").toExternalForm()
        );

        // Icône identique à la fenêtre principale
        stage.getIcons().add(
            new Image(getClass().getResourceAsStream("/images/Icon.png"))
        );
    }

    /**
     * Crée et stylise un bouton avec une couleur de fond personnalisée.
     *
     * @param texte   Libellé affiché sur le bouton
     * @param couleur Code couleur hexadécimal (ex : "#66bb6a")
     * @return        Le bouton stylisé prêt à l'emploi
     */
    private Button styliserBouton(String texte, String couleur) {
        Button btn = new Button(texte);
        btn.setStyle(
            "-fx-background-color: " + couleur + ";" +
            "-fx-text-fill: white;"                  +
            "-fx-font-size: 13px;"                   +
            "-fx-padding: 8 16;"                     +
            "-fx-background-radius: 6;"
        );
        return btn;
    }
}
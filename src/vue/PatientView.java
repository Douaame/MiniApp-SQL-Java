// ============================================================
// PatientView.java
// Vue de gestion des patients : affichage, ajout, modification,
// suppression et recherche via une interface JavaFX.
// ============================================================

package vue;

import dao.PatientDAO;
import modele.Patient;
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

public class PatientView {

    // --------------------------------------------------------
    // Attributs principaux
    // --------------------------------------------------------

    /** Tableau d'affichage des patients */
    private TableView<Patient> table = new TableView<>();

    /** Objet d'accès aux données (DAO) pour les opérations en base */
    private PatientDAO dao = new PatientDAO();


    // ============================================================
    // MÉTHODE PRINCIPALE : afficher()
    // ============================================================
    public void afficher(Stage stage) {

        // --------------------------------------------------------
        // 1. CONFIGURATION DES COLONNES DU TABLEAU
        // --------------------------------------------------------
        TableColumn<Patient, Integer> colNum = new TableColumn<>("N°");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numPatient"));
        colNum.setPrefWidth(50);

        TableColumn<Patient, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setPrefWidth(185);

        TableColumn<Patient, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colPrenom.setPrefWidth(185);

        TableColumn<Patient, String> colTel = new TableColumn<>("Téléphone");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colTel.setPrefWidth(130);

        TableColumn<Patient, String> colAdresse = new TableColumn<>("Adresse");
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colAdresse.setPrefWidth(200);

        table.getColumns().add(colNum);
        table.getColumns().add(colNom);
        table.getColumns().add(colPrenom);
        table.getColumns().add(colTel);
        table.getColumns().add(colAdresse);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.setMinHeight(300);

        // --------------------------------------------------------
        // 2. CHARGEMENT INITIAL DES DONNÉES
        // --------------------------------------------------------
        chargerPatients();

        // --------------------------------------------------------
        // 3. TITRE DE LA PAGE
        // --------------------------------------------------------
        Label titrePage = new Label("Gestion des Patients");
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
        // 5. ACTION : AJOUTER UN PATIENT
        // --------------------------------------------------------
        btnAjouter.setOnAction(e -> {
            Stage stageAjouter = new Stage();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            final TextField tfNom     = new TextField();
            final TextField tfPrenom  = new TextField();
            final TextField tfDate    = new TextField();
            tfDate.setPromptText("DD-MM-YYYY");
            final TextField tfTel     = new TextField();
            final TextField tfAdresse = new TextField();

            grid.add(new Label("Nom :"),            0, 0); grid.add(tfNom,     1, 0);
            grid.add(new Label("Prénom :"),          0, 1); grid.add(tfPrenom,  1, 1);
            grid.add(new Label("Date Naissance :"), 0, 2); grid.add(tfDate,    1, 2);
            grid.add(new Label("Téléphone :"),       0, 3); grid.add(tfTel,     1, 3);
            grid.add(new Label("Adresse :"),         0, 4); grid.add(tfAdresse, 1, 4);

            // Bouton Valider stylisé
            Button btnValider = styliserBouton("Valider", "#66bb6a");
            grid.add(btnValider, 1, 5);

            // Action du bouton Valider
            btnValider.setOnAction(ev -> {
                dao.ajouterPatient(
                    tfNom.getText(),
                    tfPrenom.getText(),
                    tfDate.getText(),
                    tfTel.getText(),
                    tfAdresse.getText()
                );
                chargerPatients();
                stageAjouter.close();
            });

            // Touche ENTER pour valider
            grid.setOnKeyPressed(ev -> {
                if (ev.getCode() == javafx.scene.input.KeyCode.ENTER)
                    btnValider.fire();
            });

            // Application du thème sur le popup
            Scene sceneAjouter = new Scene(grid, 350, 270);
            appliquerThemePopup(stageAjouter, sceneAjouter, grid);

            stageAjouter.setTitle("Ajouter un Patient");
            stageAjouter.setScene(sceneAjouter);
            stageAjouter.show();
        });

        // --------------------------------------------------------
        // 6. ACTION : SUPPRIMER UN PATIENT
        // --------------------------------------------------------
        btnSupprimer.setOnAction(e -> {
            Patient patientSelectionne = table.getSelectionModel().getSelectedItem();

            if (patientSelectionne == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setContentText("Veuillez sélectionner un patient !");
                alert.showAndWait();
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setContentText("Voulez-vous vraiment supprimer ce patient ?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    dao.supprimerPatient(patientSelectionne.getNumPatient());
                    chargerPatients();
                }
            });
        });

        // --------------------------------------------------------
        // 7. ACTION : MODIFIER UN PATIENT
        // --------------------------------------------------------
        btnModifier.setOnAction(e -> {
            Patient patientSelectionne = table.getSelectionModel().getSelectedItem();

            if (patientSelectionne == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setContentText("Veuillez sélectionner un patient !");
                alert.showAndWait();
                return;
            }

            Stage stageModifier = new Stage();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            final TextField tfNom     = new TextField(patientSelectionne.getNom());
            final TextField tfPrenom  = new TextField(patientSelectionne.getPrenom());
            final TextField tfTel     = new TextField(patientSelectionne.getTelephone());
            final TextField tfAdresse = new TextField(patientSelectionne.getAdresse());

            grid.add(new Label("Nom :"),        0, 0); grid.add(tfNom,     1, 0);
            grid.add(new Label("Prénom :"),      0, 1); grid.add(tfPrenom,  1, 1);
            grid.add(new Label("Téléphone :"),   0, 2); grid.add(tfTel,     1, 2);
            grid.add(new Label("Adresse :"),     0, 3); grid.add(tfAdresse, 1, 3);

            // Bouton Valider stylisé
            Button btnValider = styliserBouton("Valider", "#66bb6a");
            grid.add(btnValider, 1, 4);

            btnValider.setOnAction(ev -> {
                dao.modifierPatient(
                    patientSelectionne.getNumPatient(),
                    tfNom.getText(),
                    tfPrenom.getText(),
                    tfTel.getText(),
                    tfAdresse.getText()
                );
                chargerPatients();
                stageModifier.close();
            });

            // Touche ENTER pour valider
            grid.setOnKeyPressed(ev -> {
                if (ev.getCode() == javafx.scene.input.KeyCode.ENTER)
                    btnValider.fire();
            });

            // Application du thème sur le popup
            Scene sceneModifier = new Scene(grid, 350, 240);
            appliquerThemePopup(stageModifier, sceneModifier, grid);

            stageModifier.setTitle("Modifier un Patient");
            stageModifier.setScene(sceneModifier);
            stageModifier.show();
        });

        // --------------------------------------------------------
        // 8. ACTION : RETOUR AU MENU PRINCIPAL
        // --------------------------------------------------------
        btnRetour.setOnAction(e -> new MainView().afficher(stage));

        // --------------------------------------------------------
        // 9. BARRE DE RECHERCHE EN TEMPS RÉEL
        // --------------------------------------------------------
        TextField txtRecherche = new TextField();
        txtRecherche.setPromptText("Rechercher par nom ou téléphone...");

        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                chargerPatients();
            } else {
                ObservableList<Patient> listeFiltree = FXCollections.observableArrayList(
                    dao.rechercherPatient(newValue)
                );
                table.setItems(listeFiltree);
            }
        });

        // --------------------------------------------------------
        // 10. ASSEMBLAGE DE LA MISE EN PAGE
        // --------------------------------------------------------
        HBox top = new HBox(10, txtRecherche);
        top.setPadding(new Insets(10));

        VBox center = new VBox(10, headerBox, top, table);
        center.setPadding(new Insets(10, 20, 10, 20));
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        HBox boutons = new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnRetour);
        boutons.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setBottom(boutons);
        root.setStyle("-fx-background-color: #f0f4ff;");

        // --------------------------------------------------------
        // 11. CONFIGURATION ET AFFICHAGE DE LA FENÊTRE
        // --------------------------------------------------------
        Scene scene = new Scene(root, 750, 520);
        scene.getStylesheets().add(
            getClass().getResource("/images/style.css").toExternalForm()
        );

        stage.setTitle("Gestion des Patients");
        stage.setScene(scene);
        stage.show();
    }


    // ============================================================
    // MÉTHODES UTILITAIRES PRIVÉES
    // ============================================================

    /**
     * Charge la liste complète des patients et met à jour le tableau.
     */
    private void chargerPatients() {
        ObservableList<Patient> liste = FXCollections.observableArrayList(
            dao.afficherPatients()
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
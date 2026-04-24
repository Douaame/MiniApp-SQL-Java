// ============================================================
// PatientView.java
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PatientView {

    private TableView<Patient> table = new TableView<>();
    private PatientDAO dao = new PatientDAO();

    public void afficher(Stage stage) {

        // --------------------------------------------------------
        // 1. COLONNES DU TABLEAU
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
        // 2. CHARGEMENT INITIAL
        // --------------------------------------------------------
        chargerPatients();

        // --------------------------------------------------------
        // 3. TITRE AVEC ICÔNE
        // --------------------------------------------------------
        ImageView icone = new ImageView(
            new Image(getClass().getResourceAsStream("/images/patients.png"))
        );
        icone.setFitWidth(28);
        icone.setFitHeight(28);

        Label titrePage = new Label("Gestion des Patients");
        titrePage.setStyle(
            "-fx-font-size: 20px;"        +
            "-fx-font-weight: bold;"       +
            "-fx-text-fill: #2e7d32;"      +
            "-fx-font-family: 'Segoe UI';"
        );

        HBox headerBox = new HBox(10, icone, titrePage);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10, 20, 0, 20));

        // --------------------------------------------------------
        // 4. BOUTONS D'ACTION
        // --------------------------------------------------------
        Button btnAjouter   = styliserBouton("Ajouter",   "#66bb6a");
        Button btnModifier  = styliserBouton("Modifier",  "#ffb74d");
        Button btnSupprimer = styliserBouton("Supprimer", "#ef5350");
        Button btnRetour    = styliserBouton("Retour",    "#90a4ae");

        // --------------------------------------------------------
        // 5. ACTION : AJOUTER
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

            Button btnValider = styliserBouton("Valider", "#66bb6a");
            grid.add(btnValider, 1, 5);

            btnValider.setOnAction(ev -> {
                dao.ajouterPatient(
                    tfNom.getText(), tfPrenom.getText(),
                    tfDate.getText(), tfTel.getText(), tfAdresse.getText()
                );
                chargerPatients();
                AlertUtil.info("Patient ajouté avec succès !");
                stageAjouter.close();
            });

            grid.setOnKeyPressed(ev -> {
                if (ev.getCode() == javafx.scene.input.KeyCode.ENTER)
                    btnValider.fire();
            });

            Scene sceneAjouter = new Scene(grid, 350, 270);
            appliquerThemePopup(stageAjouter, sceneAjouter, grid);
            stageAjouter.setTitle("Ajouter un Patient");
            stageAjouter.setScene(sceneAjouter);
            stageAjouter.show();
        });

        // --------------------------------------------------------
        // 6. ACTION : SUPPRIMER
        // --------------------------------------------------------
        btnSupprimer.setOnAction(e -> {
            Patient p = table.getSelectionModel().getSelectedItem();

            if (p == null) {
                AlertUtil.attention("Veuillez sélectionner un patient !");
                return;
            }

            AlertUtil.confirmation("Voulez-vous vraiment supprimer ce patient ?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dao.supprimerPatient(p.getNumPatient());
                        chargerPatients();
                        AlertUtil.info("Patient supprimé avec succès !");
                    }
                });
        });

        // --------------------------------------------------------
        // 7. ACTION : MODIFIER
        // --------------------------------------------------------
        btnModifier.setOnAction(e -> {
            Patient p = table.getSelectionModel().getSelectedItem();

            if (p == null) {
                AlertUtil.attention("Veuillez sélectionner un patient !");
                return;
            }

            Stage stageModifier = new Stage();
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            final TextField tfNom     = new TextField(p.getNom());
            final TextField tfPrenom  = new TextField(p.getPrenom());
            final TextField tfTel     = new TextField(p.getTelephone());
            final TextField tfAdresse = new TextField(p.getAdresse());

            grid.add(new Label("Nom :"),      0, 0); grid.add(tfNom,     1, 0);
            grid.add(new Label("Prénom :"),    0, 1); grid.add(tfPrenom,  1, 1);
            grid.add(new Label("Téléphone :"), 0, 2); grid.add(tfTel,     1, 2);
            grid.add(new Label("Adresse :"),   0, 3); grid.add(tfAdresse, 1, 3);

            Button btnValider = styliserBouton("Valider", "#66bb6a");
            grid.add(btnValider, 1, 4);

            btnValider.setOnAction(ev -> {
                dao.modifierPatient(
                    p.getNumPatient(),
                    tfNom.getText(), tfPrenom.getText(),
                    tfTel.getText(), tfAdresse.getText()
                );
                chargerPatients();
                AlertUtil.info("Patient modifié avec succès !");
                stageModifier.close();
            });

            grid.setOnKeyPressed(ev -> {
                if (ev.getCode() == javafx.scene.input.KeyCode.ENTER)
                    btnValider.fire();
            });

            Scene sceneModifier = new Scene(grid, 350, 240);
            appliquerThemePopup(stageModifier, sceneModifier, grid);
            stageModifier.setTitle("Modifier un Patient");
            stageModifier.setScene(sceneModifier);
            stageModifier.show();
        });

        // --------------------------------------------------------
        // 8. RETOUR
        // --------------------------------------------------------
        btnRetour.setOnAction(e -> new MainView().afficher(stage));

        // --------------------------------------------------------
        // 9. RECHERCHE EN TEMPS RÉEL
        // --------------------------------------------------------
        TextField txtRecherche = new TextField();
        txtRecherche.setPromptText("Rechercher par nom ou téléphone...");
        txtRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                chargerPatients();
            } else {
                table.setItems(FXCollections.observableArrayList(
                    dao.rechercherPatient(newVal)
                ));
            }
        });

        // --------------------------------------------------------
        // 10. ASSEMBLAGE
        // --------------------------------------------------------
        HBox top = new HBox(10, txtRecherche);
        top.setPadding(new Insets(10));

        VBox center = new VBox(10, headerBox, top, table);
        center.setPadding(new Insets(10, 20, 10, 20));
        VBox.setVgrow(table, Priority.ALWAYS);

        HBox boutons = new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnRetour);
        boutons.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setBottom(boutons);
        root.setStyle("-fx-background-color: #f0f4ff;");

        Scene scene = new Scene(root, 750, 520);
        scene.getStylesheets().add(
            getClass().getResource("/images/style.css").toExternalForm()
        );
        stage.setTitle("Gestion des Patients");
        stage.setScene(scene);
        stage.show();
    }

    private void chargerPatients() {
        table.setItems(FXCollections.observableArrayList(dao.afficherPatients()));
    }

    private void appliquerThemePopup(Stage stage, Scene scene, GridPane grid) {
        grid.setStyle("-fx-background-color: #f0fff4;");
        scene.getStylesheets().add(
            getClass().getResource("/images/style.css").toExternalForm()
        );
        stage.getIcons().add(
            new Image(getClass().getResourceAsStream("/images/Icon.png"))
        );
    }

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
// ============================================================
// MedecinView.java
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MedecinView {

    private TableView<Medecin> table = new TableView<>();
    private MedecinDAO dao = new MedecinDAO();

    public void afficher(Stage stage) {

        // --------------------------------------------------------
        // 1. COLONNES DU TABLEAU
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
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        // --------------------------------------------------------
        // 2. CHARGEMENT INITIAL
        // --------------------------------------------------------
        chargerMedecins();

        // --------------------------------------------------------
        // 3. TITRE AVEC ICÔNE
        // --------------------------------------------------------
        ImageView icone = new ImageView(
            new Image(getClass().getResourceAsStream("/images/medecins.png"))
        );
        icone.setFitWidth(28);
        icone.setFitHeight(28);

        Label titrePage = new Label("Gestion des Médecins");
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

            TextField tfNom    = new TextField();
            TextField tfPrenom = new TextField();
            TextField tfSpec   = new TextField();
            TextField tfTel    = new TextField();

            grid.add(new Label("Nom :"),        0, 0); grid.add(tfNom,    1, 0);
            grid.add(new Label("Prénom :"),      0, 1); grid.add(tfPrenom, 1, 1);
            grid.add(new Label("Spécialité :"), 0, 2); grid.add(tfSpec,   1, 2);
            grid.add(new Label("Téléphone :"),  0, 3); grid.add(tfTel,    1, 3);

            Button btnValider = styliserBouton("Valider", "#66bb6a");
            grid.add(btnValider, 1, 4);

            btnValider.setOnAction(ev -> {
                dao.ajouterMedecin(
                    tfNom.getText(), tfPrenom.getText(),
                    tfSpec.getText(), tfTel.getText()
                );
                chargerMedecins();
                AlertUtil.info("Médecin ajouté avec succès !");
                stageAjouter.close();
            });

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
        // 6. ACTION : MODIFIER
        // --------------------------------------------------------
        btnModifier.setOnAction(e -> {
            Medecin m = table.getSelectionModel().getSelectedItem();

            if (m == null) {
                AlertUtil.attention("Veuillez sélectionner un médecin !");
                return;
            }

            Stage stageModifier = new Stage();
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            TextField tfNom    = new TextField(m.getNom());
            TextField tfPrenom = new TextField(m.getPrenom());
            TextField tfSpec   = new TextField(m.getSpecialite());
            TextField tfTel    = new TextField(m.getTelephone());

            grid.add(new Label("Nom :"),        0, 0); grid.add(tfNom,    1, 0);
            grid.add(new Label("Prénom :"),      0, 1); grid.add(tfPrenom, 1, 1);
            grid.add(new Label("Spécialité :"), 0, 2); grid.add(tfSpec,   1, 2);
            grid.add(new Label("Téléphone :"),  0, 3); grid.add(tfTel,    1, 3);

            Button btnValider = styliserBouton("Valider", "#66bb6a");
            grid.add(btnValider, 1, 4);

            btnValider.setOnAction(ev -> {
                dao.modifierMedecin(
                    m.getNumMedecin(),
                    tfNom.getText(), tfPrenom.getText(),
                    tfSpec.getText(), tfTel.getText()
                );
                chargerMedecins();
                AlertUtil.info("Médecin modifié avec succès !");
                stageModifier.close();
            });

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
        // 7. ACTION : SUPPRIMER
        // --------------------------------------------------------
        btnSupprimer.setOnAction(e -> {
            Medecin m = table.getSelectionModel().getSelectedItem();

            if (m == null) {
                AlertUtil.attention("Veuillez sélectionner un médecin !");
                return;
            }

            AlertUtil.confirmation("Voulez-vous vraiment supprimer ce médecin ?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dao.supprimerMedecin(m.getNumMedecin());
                        chargerMedecins();
                        AlertUtil.info("Médecin supprimé avec succès !");
                    }
                });
        });

        // --------------------------------------------------------
        // 8. RETOUR
        // --------------------------------------------------------
        btnRetour.setOnAction(e -> new MainView().afficher(stage));

        // --------------------------------------------------------
        // 9. ASSEMBLAGE
        // --------------------------------------------------------
        VBox centerBox = new VBox(10, headerBox, table);
        centerBox.setPadding(new Insets(10, 20, 10, 20));
        VBox.setVgrow(table, Priority.ALWAYS);

        HBox boutons = new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnRetour);
        boutons.setPadding(new Insets(10, 20, 10, 20));

        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setBottom(boutons);
        root.setStyle("-fx-background-color: #f0f4ff;");

        Scene scene = new Scene(root, 780, 520);
        scene.getStylesheets().add(
            getClass().getResource("/images/style.css").toExternalForm()
        );
        stage.setTitle("Gestion des Médecins");
        stage.setScene(scene);
        stage.show();
    }

    private void chargerMedecins() {
        table.setItems(FXCollections.observableArrayList(dao.afficherMedecins()));
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
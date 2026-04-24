// ============================================================
// RendezVousView.java
// ============================================================

package vue;

import dao.RendezVousDAO;
import dao.PatientDAO;
import dao.MedecinDAO;
import modele.RendezVous;
import modele.Patient;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RendezVousView {

    private RendezVousDAO dao        = new RendezVousDAO();
    private PatientDAO    patientDao = new PatientDAO();
    private MedecinDAO    medecinDao = new MedecinDAO();

    private TableView<RendezVous>    table           = new TableView<>();
    private ObservableList<RendezVous> data          = FXCollections.observableArrayList();

    // ComboBox affichant "ID - Nom Prénom" pour patients et médecins
    private ComboBox<String>  cbPatient       = new ComboBox<>();
    private ComboBox<String>  cbMedecin       = new ComboBox<>();
    private DatePicker        dpDate          = new DatePicker();
    private ComboBox<String>  cbHeure         = new ComboBox<>();
    private TextField         tfIdRdv         = new TextField();
    private ComboBox<String>  cbFiltreMedecin = new ComboBox<>();

    public void afficher(Stage stage) {

        // --------------------------------------------------------
        // 1. COLONNES DU TABLEAU
        // --------------------------------------------------------
        TableColumn<RendezVous, Integer> colId = new TableColumn<>("N° RDV");
        colId.setCellValueFactory(new PropertyValueFactory<>("numRendezVous"));
        colId.setMaxWidth(80);

        TableColumn<RendezVous, Integer> colPatient = new TableColumn<>("Patient");
        colPatient.setCellValueFactory(new PropertyValueFactory<>("numPatient"));
        colPatient.setMaxWidth(100);

        TableColumn<RendezVous, Integer> colMedecin = new TableColumn<>("Médecin");
        colMedecin.setCellValueFactory(new PropertyValueFactory<>("numMedecin"));
        colMedecin.setMaxWidth(100);

        TableColumn<RendezVous, Timestamp> colDate = new TableColumn<>("Date / Heure");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateHeureRendezVous"));
        colDate.setMinWidth(180);

        TableColumn<RendezVous, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setMaxWidth(120);

        table.getColumns().add(colId);
        table.getColumns().add(colPatient);
        table.getColumns().add(colMedecin);
        table.getColumns().add(colDate);
        table.getColumns().add(colStatut);
        table.setItems(data);
        table.setPlaceholder(new Label("Aucun rendez-vous disponible"));
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-font-size: 13px;");
        VBox.setVgrow(table, Priority.ALWAYS);

        // --------------------------------------------------------
        // 2. SÉLECTION AUTOMATIQUE
        // --------------------------------------------------------
        table.setOnMouseClicked(e -> {
            RendezVous selected = table.getSelectionModel().getSelectedItem();
            if (selected != null)
                tfIdRdv.setText(String.valueOf(selected.getNumRendezVous()));
        });

        // --------------------------------------------------------
        // 3. CHARGEMENT INITIAL
        // --------------------------------------------------------
        chargerDonnees();

        // --------------------------------------------------------
        // 4. TITRE AVEC ICÔNE
        // --------------------------------------------------------
        ImageView icone = new ImageView(
            new Image(getClass().getResourceAsStream("/images/rdv.png"))
        );
        icone.setFitWidth(28);
        icone.setFitHeight(28);

        Label titrePage = new Label("Gestion des Rendez-vous");
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
        // 5. FORMULAIRE — ComboBox avec ID + Nom + Prénom
        // --------------------------------------------------------
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10, 20, 10, 20));

        // Remplissage des ComboBox patients : "ID - Nom Prénom"
        for (Patient p : patientDao.afficherPatients()) {
            cbPatient.getItems().add(
                p.getNumPatient() + " - " + p.getNom() + " " + p.getPrenom()
            );
        }
        cbPatient.setPromptText("Choisir patient");
        cbPatient.setPrefWidth(220);

        // Remplissage des ComboBox médecins : "ID - Nom Prénom"
        for (Medecin m : medecinDao.afficherMedecins()) {
            cbMedecin.getItems().add(
                m.getNumMedecin() + " - " + m.getNom() + " " + m.getPrenom()
            );
            cbFiltreMedecin.getItems().add(
                m.getNumMedecin() + " - " + m.getNom() + " " + m.getPrenom()
            );
        }
        cbMedecin.setPromptText("Choisir médecin");
        cbMedecin.setPrefWidth(220);

        // Créneaux horaires
        cbHeure.getItems().addAll(
            "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00"
        );

        // Style DatePicker
        dpDate.setStyle(
            "-fx-border-color: #a5d6a7;" +
            "-fx-border-radius: 6;"      +
            "-fx-background-radius: 6;"
        );
        dpDate.getEditor().setStyle("-fx-background-color: white;");

        form.add(new Label("Patient :"),  0, 0); form.add(cbPatient,    1, 0);
        form.add(new Label("Médecin :"),  0, 1); form.add(cbMedecin,    1, 1);
        form.add(new Label("Date :"),     0, 2); form.add(dpDate,       1, 2);
        form.add(new Label("Heure :"),    0, 3); form.add(cbHeure,      1, 3);
        form.add(new Label("N° RDV :"),   0, 4); form.add(tfIdRdv,      1, 4);

        // --------------------------------------------------------
        // 6. BOUTONS
        // --------------------------------------------------------
        Button btnAjouter    = styliserBouton("Ajouter",    "#66bb6a");
        Button btnAnnuler    = styliserBouton("Annuler",    "#ef6b82");
        Button btnSupprimer  = styliserBouton("Supprimer",  "#ef5350");
        Button btnActualiser = styliserBouton("Actualiser", "#42a5f5");
        Button btnRetour     = styliserBouton("Retour",     "#90a4ae");

        btnAjouter.setOnAction(e    -> ajouterRDV());
        btnAnnuler.setOnAction(e    -> annulerRDV());
        btnSupprimer.setOnAction(e  -> supprimerRDV());
        btnActualiser.setOnAction(e -> chargerDonnees());
        btnRetour.setOnAction(e     -> new MainView().afficher(stage));

        HBox boutons = new HBox(10, btnAjouter, btnAnnuler, btnSupprimer, btnActualiser, btnRetour);
        boutons.setPadding(new Insets(10, 20, 10, 20));

        // --------------------------------------------------------
        // 7. FILTRE PAR MÉDECIN
        // --------------------------------------------------------
        cbFiltreMedecin.setPromptText("Filtrer médecin");
        cbFiltreMedecin.setPrefWidth(200);

        Button btnFiltrer = styliserBouton("Filtrer", "#42a5f5");
        btnFiltrer.setOnAction(e -> filtrerParMedecin());

        Button btnTout = styliserBouton("Tout", "#90a4ae");
        btnTout.setOnAction(e -> chargerDonnees());

        HBox filtres = new HBox(10, new Label("Médecin :"), cbFiltreMedecin, btnFiltrer, btnTout);
        filtres.setPadding(new Insets(10, 20, 0, 20));
        filtres.setAlignment(Pos.CENTER_LEFT);

        // --------------------------------------------------------
        // 8. ASSEMBLAGE
        // --------------------------------------------------------
        VBox root = new VBox(10, headerBox, filtres, table, form, boutons);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f0f4ff;");

        Scene scene = new Scene(root, 680, 700);
        scene.getStylesheets().add(
            getClass().getResource("/images/style.css").toExternalForm()
        );
        stage.setTitle("Gestion des Rendez-vous");
        stage.setScene(scene);
        stage.show();
    }

    // ============================================================
    // MÉTHODES D'ACTION
    // ============================================================

    private void chargerDonnees() {
        data.clear();
        data.addAll(dao.getTousRendezVous());
    }

    private void ajouterRDV() {
        try {
            if (cbPatient.getValue() == null || cbMedecin.getValue() == null ||
                dpDate.getValue() == null || cbHeure.getValue() == null) {
                AlertUtil.erreur("Veuillez remplir tous les champs !");
                return;
            }

            // Extraction de l'ID depuis "ID - Nom Prénom"
            int idPatient = Integer.parseInt(cbPatient.getValue().split(" - ")[0].trim());
            int idMedecin = Integer.parseInt(cbMedecin.getValue().split(" - ")[0].trim());

            LocalDateTime dateTime = LocalDateTime.of(
                dpDate.getValue(),
                LocalTime.parse(cbHeure.getValue())
            );
            Timestamp ts = Timestamp.valueOf(dateTime);

            if (!dao.isMedecinDisponible(idMedecin, ts)) {
                AlertUtil.erreur("Médecin non disponible à ce créneau !");
                return;
            }

            dao.ajouterRendezVous(new RendezVous(idPatient, idMedecin, ts, "Planifié"));
            chargerDonnees();
            AlertUtil.info("Rendez-vous ajouté avec succès !");

        } catch (Exception e) {
            AlertUtil.erreur("Erreur dans les données saisies !");
        }
    }

    private void annulerRDV() {
        try {
            int id = Integer.parseInt(tfIdRdv.getText());
            AlertUtil.confirmation("Voulez-vous vraiment annuler ce rendez-vous ?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dao.annulerRendezVous(id);
                        chargerDonnees();
                        AlertUtil.info("Rendez-vous annulé !");
                    }
                });
        } catch (Exception e) {
            AlertUtil.erreur("Veuillez sélectionner un rendez-vous dans le tableau !");
        }
    }

    private void supprimerRDV() {
        try {
            int id = Integer.parseInt(tfIdRdv.getText());
            AlertUtil.confirmation("Voulez-vous vraiment supprimer ce rendez-vous ?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dao.supprimerRendezVous(id);
                        chargerDonnees();
                        AlertUtil.info("Rendez-vous supprimé !");
                    }
                });
        } catch (Exception e) {
            AlertUtil.erreur("Veuillez sélectionner un rendez-vous dans le tableau !");
        }
    }

    private void filtrerParMedecin() {
        if (cbFiltreMedecin.getValue() != null) {
            // Extraction de l'ID depuis "ID - Nom Prénom"
            int idMedecin = Integer.parseInt(
                cbFiltreMedecin.getValue().split(" - ")[0].trim()
            );
            data.clear();
            data.addAll(dao.getRDVParMedecin(idMedecin));
        }
    }

    private Button styliserBouton(String texte, String couleur) {
        Button btn = new Button(texte);
        btn.setStyle(
            "-fx-background-color: " + couleur + ";" +
            "-fx-text-fill: white;"                  +
            "-fx-font-weight: bold;"                 +
            "-fx-font-size: 13px;"                   +
            "-fx-padding: 8 16;"                     +
            "-fx-background-radius: 6;"              +
            "-fx-cursor: hand;"
        );
        return btn;
    }
}
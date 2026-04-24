// ============================================================
// AlertUtil.java
// Utilitaire pour afficher des boîtes de dialogue stylisées
// avec icône personnalisée et validation par touche ENTER.
// Utilisée par toutes les vues du projet.
// ============================================================

package vue;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AlertUtil {

    /**
     * Affiche une boîte de dialogue stylisée.
     * Applique l'icône du cabinet + validation par ENTER.
     *
     * @param type    Type de l'alerte (ERROR, WARNING, INFORMATION, CONFIRMATION)
     * @param titre   Titre de la fenêtre
     * @param message Message affiché
     * @return        L'Alert affichée (utile pour CONFIRMATION)
     */
    public static Alert afficherAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null); // supprime le header gris par défaut
        alert.setContentText(message);

        // --------------------------------------------------------
        // Icône personnalisée dans la barre de titre du popup
        // --------------------------------------------------------
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(
            new Image(AlertUtil.class.getResourceAsStream("/images/Icon.png"))
        );

        // --------------------------------------------------------
        // Style du DialogPane — thème vert cohérent
        // --------------------------------------------------------
        alert.getDialogPane().setStyle(
            "-fx-background-color: #f0fff4;"       +
            "-fx-border-color: #a5d6a7;"           +
            "-fx-border-radius: 8;"                +
            "-fx-background-radius: 8;"            +
            "-fx-font-family: 'Segoe UI';"         +
            "-fx-font-size: 13px;"
        );

        // Style du bouton OK/Confirmer
        alert.getDialogPane().lookupButton(ButtonType.OK).setStyle(
            "-fx-background-color: #66bb6a;"  +
            "-fx-text-fill: white;"            +
            "-fx-font-weight: bold;"           +
            "-fx-background-radius: 6;"        +
            "-fx-padding: 6 16;"
        );

        // Style du bouton Annuler si présent (CONFIRMATION)
        if (type == Alert.AlertType.CONFIRMATION) {
            alert.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle(
                "-fx-background-color: #ef5350;" +
                "-fx-text-fill: white;"           +
                "-fx-font-weight: bold;"          +
                "-fx-background-radius: 6;"       +
                "-fx-padding: 6 16;"
            );
        }

        // --------------------------------------------------------
        // Validation par touche ENTER
        // --------------------------------------------------------
        alert.getDialogPane().setOnKeyPressed(ev -> {
            if (ev.getCode() == javafx.scene.input.KeyCode.ENTER) {
                alertStage.close();
                alert.setResult(ButtonType.OK);
            }
        });

        return alert;
    }

    // Raccourcis pratiques pour chaque type
    public static void erreur(String message) {
        afficherAlert(Alert.AlertType.ERROR, "Erreur", message).showAndWait();
    }

    public static void info(String message) {
        afficherAlert(Alert.AlertType.INFORMATION, "Information", message).showAndWait();
    }

    public static void attention(String message) {
        afficherAlert(Alert.AlertType.WARNING, "Attention", message).showAndWait();
    }

    public static java.util.Optional<ButtonType> confirmation(String message) {
        return afficherAlert(Alert.AlertType.CONFIRMATION, "Confirmation", message).showAndWait();
    }
}
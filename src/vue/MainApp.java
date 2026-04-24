// ============================================================
// MainApp.java
// Point d'entrée de l'application JavaFX du Cabinet Médical.
// Lance la vue de connexion au démarrage.
// ============================================================

package vue;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    // ============================================================
    // MÉTHODE start() — appelée automatiquement par JavaFX
    // au lancement de l'application (après launch())
    // ============================================================
    @Override
    public void start(Stage stage) {
    	// --------------------------------------------------------
        // Icône personnalisée de la fenêtre (barre de titre + taskbar)
        // Remplace l'icône JavaFX par défaut (tasse de café)
        // --------------------------------------------------------
        stage.getIcons().add(
            new javafx.scene.image.Image(
                getClass().getResourceAsStream("/images/Icon.png")
            )
        );
        // Affiche la fenêtre de connexion comme première vue
        new LoginView().afficher(stage);
    }

    // ============================================================
    // MÉTHODE main() — point d'entrée Java standard
    // Délègue le démarrage à JavaFX via launch()
    // ============================================================
    public static void main(String[] args) {
        launch(args); // initialise le thread JavaFX et appelle start()
    }
}
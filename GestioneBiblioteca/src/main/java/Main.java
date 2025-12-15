
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il MENU PRINCIPALE invece che la schermata libri
        Parent root = FXMLLoader.load(getClass().getResource("/view/MenuPrincipale.fxml"));
        
        primaryStage.setTitle("Biblioteca Manager - Home");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
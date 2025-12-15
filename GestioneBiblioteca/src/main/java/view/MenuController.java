/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import gestori.*;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {

    
    private GestoreLibri gestoreLibri = new GestoreLibri();
    private GestoreUtenti gestoreUtenti = new GestoreUtenti();
    // private GestorePrestiti gestorePrestiti = new GestorePrestiti();

    @FXML
    private void vaiALibri(ActionEvent event) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LibriView.fxml")); // Assicurati del percorso!
            Parent root = loader.load();

            LibriController controller = loader.getController();

            controller.setGestore(this.gestoreLibri);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestione Libri");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void vaiAUtenti(ActionEvent event) {
         try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UtentiView.fxml")); // Assicurati del percorso!
            Parent root = loader.load();

            UtentiController controller = loader.getController();

            controller.setGestore(this.gestoreUtenti);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestione Utenti");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    private void vaiAPrestiti(ActionEvent event) {
        System.out.println("Schermata prestiti non ancora implementata!");
        
    }
}

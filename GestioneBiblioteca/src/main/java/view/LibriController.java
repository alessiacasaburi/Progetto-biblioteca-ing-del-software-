/**
 * * @author Alessandro
 */
package view;

import entita.Libro;
import gestori.GestoreLibri;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;

public class LibriController {

    // Collegamenti col file FXML
    @FXML private TextField txtRicerca;
    @FXML private TableView<Libro> tabellaLibri;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colAutori;
    @FXML private TableColumn<Libro, Integer> colAnno;
    @FXML private TableColumn<Libro, Integer> colCopie;

    private GestoreLibri gestore;

    
    @FXML
    public void initialize() {
        // Configura le colonne
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("Isbn"));
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("Titolo"));
        colAutori.setCellValueFactory(cellData -> new SimpleStringProperty(String.join(", ", cellData.getValue().getAutori())));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        colCopie.setCellValueFactory(new PropertyValueFactory<>("copieDisponibili"));
        
        // Listener ricerca
        txtRicerca.textProperty().addListener((obs, oldVal, newVal) -> {
            tabellaLibri.setItems(gestore.ricercaTestuale(newVal));
        });
    }

    public void setGestore(GestoreLibri gestore) {
        this.gestore = gestore;
        tabellaLibri.setItems(gestore.getLista());
    }

    // --- GESTIONE BOTTONI  ---

    @FXML
    private void handleModifica() {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            apriDialogLibro(selezionato);
        } else {
            alert("Nessun libro selezionato", "Seleziona un libro da modificare.");
        }
    }

    // --- GESTIONE CLICK SUL BOTTONE NUOVO ---
    @FXML
    private void handleNuovo(){
        // Passiamo 'null' perché è un NUOVO libro, non uno esistente
        apriDialogLibro(null);
    }
// --- GESTIONE CLICK SUL BOTTONE ELIMINA ---
    @FXML
    private void handleElimina() {
        
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();

        
        if (selezionato != null) {
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma Eliminazione?");
            alert.setHeaderText("Stai per eliminare 1 copia di : " + selezionato.getTitolo());
            alert.setContentText("Dopo averlo eliminato non potrai recuperarla");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
               
                gestore.rimuovi(selezionato); 
               
            }
        } else {
            
            alert("Nessun libro selezionato", "Seleziona un libro da eliminare.");
        }
    }
    // --- LOGICA DELLA FINESTRA DI REGISTRAZIONE ---
    private void apriDialogLibro(Libro libroDaModificare) {
        
        Dialog<Libro> dialog = new Dialog<>();
        dialog.setTitle(libroDaModificare == null ? "Nuovo Libro" : "Modifica Libro");
        dialog.setHeaderText(libroDaModificare == null ? "Inserisci i dati del nuovo libro" : "Modifica i dati esistenti");

        
        ButtonType btnSalvaType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvaType, ButtonType.CANCEL);

        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtIsbn = new TextField();
        TextField txtTitolo = new TextField();
        TextField txtAutori = new TextField();
        TextField txtAnno = new TextField();
        TextField txtCopie = new TextField();
        
        
        txtIsbn.setPromptText("digitare solo le cifre");
        txtTitolo.setPromptText("Titolo del libro");
        txtAutori.setPromptText("Autore 1, Autore 2...");

        // Se stiamo modificando, riempiamo i campi con i dati vecchi
        if (libroDaModificare != null) {
            txtIsbn.setText(libroDaModificare.getIsbn());
            txtIsbn.setDisable(true); // L'ISBN non si cambia mai!
            txtTitolo.setText(libroDaModificare.getTitolo());
            txtAutori.setText(String.join(", ", libroDaModificare.getAutori()));
            txtAnno.setText(String.valueOf(libroDaModificare.getAnnoPubblicazione()));
            txtCopie.setText(String.valueOf(libroDaModificare.getCopieDisponibili()));
        }

        // Aggiungiamo i campi alla griglia (Colonna, Riga)
        grid.add(new Label("ISBN:"), 0, 0);
        grid.add(txtIsbn, 1, 0);
        grid.add(new Label("Titolo:"), 0, 1);
        grid.add(txtTitolo, 1, 1);
        grid.add(new Label("Autori:"), 0, 2);
        grid.add(txtAutori, 1, 2);
        grid.add(new Label("Anno di pubblicazione:"), 0, 3);
        grid.add(txtAnno, 1, 3);
        grid.add(new Label("Numero di Copie:"), 0, 4);
        grid.add(txtCopie, 1, 4);

        dialog.getDialogPane().setContent(grid);

       
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvaType) {
                try {
                   
                    String titolo = txtTitolo.getText();
                    String isbn = txtIsbn.getText();
                    
                    List<String> autori = Arrays.asList(txtAutori.getText().split("\\s*,\\s*"));
                    int anno = Integer.parseInt(txtAnno.getText());
                    int copie = Integer.parseInt(txtCopie.getText());

                    
                    if (libroDaModificare == null) {
                        return new Libro(titolo, autori, anno, isbn, copie);
                    } else {
                        libroDaModificare.setTitolo(titolo);
                        libroDaModificare.setAutori(autori);
                        libroDaModificare.setAnnoPubblicazione(anno);
                        libroDaModificare.setCopieDisponibili(copie);
                        gestore.salvaModifiche();  
                        tabellaLibri.refresh();
                        return libroDaModificare;
                    }
                } catch (Exception ex) {
                    
                    alert("Errore", ex.getMessage());
                    return null;
                }
            }
            return null;
        });

        
        Optional<Libro> result = dialog.showAndWait();

        // 6. Se l'utente ha salvato correttamente
        result.ifPresent(libro -> {
            if (libroDaModificare == null) {
                
                try {
                    gestore.aggiungi(libro);
                    
                } catch (IllegalArgumentException e) {
                    alert("Errore", e.getMessage()); 
                }
            } else {
                
            }
        });
    }
    
    @FXML
    private void handleIndietro(javafx.event.ActionEvent event) {
        try {
            
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/MenuPrincipale.fxml"));
            javafx.scene.Parent root = loader.load();

            
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            
            // Crea la nuova scena e impostala
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.setTitle("Biblioteca Manager - Home");
            stage.show();
            
        } catch (java.io.IOException e) {
            e.printStackTrace();
            alert("Errore", "Impossibile tornare al menu principale.");
        }
    }

    
    private void alert(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

}
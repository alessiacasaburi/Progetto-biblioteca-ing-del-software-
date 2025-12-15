package view;
/**
 * * @author Annamaria
 */
import entita.Libro;
import entita.Prestito;
import entita.Utente;
import gestori.GestoreLibri;
import gestori.GestorePrestiti;
import gestori.GestoreUtenti;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class PrestitiController {

    @FXML private TextField txtRicerca;
    @FXML private TableView<Prestito> tabellaPrestiti;

    // --- COLONNE AGGIORNATE IN BASE AL TUO FXML ---
    @FXML private TableColumn<Prestito, String> colNome;
    @FXML private TableColumn<Prestito, String> colCognome;
    @FXML private TableColumn<Prestito, String> colLibro;
    @FXML private TableColumn<Prestito, String> colDataInizio;
    @FXML private TableColumn<Prestito, String> colDataFine;
    @FXML private TableColumn<Prestito, String> colRestituito; 

    private GestorePrestiti gestorePrestiti;
    private GestoreLibri gestoreLibri;
    private GestoreUtenti gestoreUtenti;

    @FXML
    public void initialize() {
        // 1. Configurazione Colonne Complesse (Dati annidati)
        
        // Estraiamo il NOME dall'oggetto Utente dentro Prestito
        colNome.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getUtente().getNome()));

        // Estraiamo il COGNOME dall'oggetto Utente dentro Prestito
        colCognome.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getUtente().getCognome()));

        // Estraiamo il TITOLO dal Libro
        colLibro.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLibro().getTitolo()));

        // 2. Configurazione Date
        colDataInizio.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataScadenza"));
        colRestituito.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getPrestitoAttivo() ? "Sì" : "No"));

        // 4. Listener Ricerca
        txtRicerca.textProperty().addListener((obs, oldVal, newVal) -> {
            if (gestorePrestiti != null) {
                //tabellaPrestiti.setItems(gestoreUtenti.ricercaAnagrafica(newVal));
            }
        });
    }

    // Riceve i gestori dal MenuPrincipale
    public void setGestori(GestorePrestiti gp, GestoreLibri gl, GestoreUtenti gu) {
        this.gestorePrestiti = gp;
        this.gestoreLibri = gl;
        this.gestoreUtenti = gu;
        tabellaPrestiti.setItems(gp.getLista());
    }

    // --- AZIONI ---

    @FXML
    private void handleIndietro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipale.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestione Biblioteca - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- GESTIONE CLICK SUL BOTTONE NUOVO PRESTITO ---
    @FXML
    private void handleNuovo() {
        // Passiamo 'null' perché è un NUOVO prestito
        apriDialogPrestito(null);
    }

    
    @FXML
    private void handleModifica() {
        Prestito selezionato = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            apriDialogPrestito(selezionato);
        } else {
            alert("Nessuna selezione", "Seleziona un prestito da modificare.");
        }
    }

    // --- LOGICA DELLA FINESTRA DI REGISTRAZIONE (PRESTITO) ---
    private void apriDialogPrestito(Prestito prestitoDaModificare) {

        Dialog<Prestito> dialog = new Dialog<>();
        dialog.setTitle(prestitoDaModificare == null ? "Nuovo Prestito" : "Modifica Prestito");
        dialog.setHeaderText(prestitoDaModificare == null ? "Compila i dati del prestito" : "Modifica i dati del prestito");

        ButtonType btnSalvaType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvaType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // --- 1. SETUP DEI CAMPI ---
        ComboBox<Utente> cmbUtenti = new ComboBox<>(gestoreUtenti.getLista());
        cmbUtenti.setPromptText("Seleziona Utente...");
        
        cmbUtenti.setConverter(new javafx.util.StringConverter<Utente>() {
            @Override
            public String toString(Utente u) {
                return (u == null) ? null : u.getMatricola() + " - " + u.getCognome() + " " + u.getNome();
            }
            @Override
            public Utente fromString(String string) { return null; }
        });

        ComboBox<Libro> cmbLibri = new ComboBox<>(gestoreLibri.getLista());
        cmbLibri.setPromptText("Seleziona Libro...");
        
        cmbLibri.setConverter(new javafx.util.StringConverter<Libro>() {
            @Override
            public String toString(Libro l) {
                return (l == null) ? null : l.getTitolo() + " (" + l.getIsbn() + ")";
            }
            @Override
            public Libro fromString(String string) { return null; }
        });

        DatePicker dataInizio = new DatePicker(LocalDate.now());
        DatePicker dataScadenza = new DatePicker(LocalDate.now().plusDays(30));  

        dataInizio.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                dataScadenza.setValue(newVal.plusDays(15)); 
            }
        });

        if (prestitoDaModificare != null) {
            cmbUtenti.setValue(prestitoDaModificare.getUtente());
            cmbLibri.setValue(prestitoDaModificare.getLibro());
            
            cmbUtenti.setDisable(true); 
            cmbLibri.setDisable(true);
            dataInizio.setValue(prestitoDaModificare.getDataInizio());
            dataScadenza.setValue(prestitoDaModificare.getDataScadenza());
        }

        grid.add(new Label("Utente:"), 0, 0);
        grid.add(cmbUtenti, 1, 0);
        grid.add(new Label("Libro:"), 0, 1);
        grid.add(cmbLibri, 1, 1);
        grid.add(new Label("Data Inizio:"), 0, 2);
        grid.add(dataInizio, 1, 2);
        grid.add(new Label("Data Scadenza:"), 0, 3);
        grid.add(dataScadenza, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvaType) {
                try {
                    
                    if (prestitoDaModificare == null) { 
                            return new Prestito(
                            cmbUtenti.getValue(), 
                            cmbLibri.getValue(), 
                            dataInizio.getValue(), 
                            dataScadenza.getValue()
                        );
                    } else {
                        
                        prestitoDaModificare.setDataInizio(dataInizio.getValue());
                        prestitoDaModificare.setDataScadenza(dataScadenza.getValue());
                        
                        gestorePrestiti.salvaModifiche();
                        tabellaPrestiti.refresh();
                        return prestitoDaModificare;
                    }

                } catch (Exception ex) {
                    
                    alert("Errore", ex.getMessage());
                    return null;
                }
            }
            return null;
        });

        
        Optional<Prestito> result = dialog.showAndWait();

        result.ifPresent(prestito -> {
            
            if (prestitoDaModificare == null) {
                try {
                    gestorePrestiti.aggiungi(prestito);
                    gestoreLibri.salvaModifiche();  

                } catch (Exception e) {
                    alert("Errore", e.getMessage()); 
                }
            }
        });
    }
    @FXML
    private void handleRestituzione() {
        Prestito selezionato = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            if (!selezionato.isPrestitoAttivo()) {
                alert("Attenzione", "Questo prestito è già stato chiuso.");
                return;
            }
            selezionato.setPrestitoConcluso();
            gestorePrestiti.salvaModifiche();
            tabellaPrestiti.refresh(); 
        } else {
            alert("Attenzione", "Seleziona un prestito.");
        }
    }

    @FXML
    private void handleElimina() {
        Prestito selezionato = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            gestorePrestiti.rimuovi(selezionato);
        } else {
            alert("Attenzione", "Seleziona una riga da eliminare.");
        }
    }

    private void alert(String titolo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
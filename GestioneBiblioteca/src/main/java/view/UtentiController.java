/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import entita.Utente;
import gestori.GestoreUtenti;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class UtentiController {

    // --- COLLEGAMENTI FXML ---
    @FXML private TextField txtRicerca;
    @FXML private TableView<Utente> tabellaUtenti;
    
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colEmail;
    @FXML private TableColumn<Utente, Integer> colPrestiti; // Numero prestiti attivi

    private GestoreUtenti gestore;

    // --- INIZIALIZZAZIONE ---
    @FXML
    public void initialize() {
        // 1. Configura le colonne della tabella
        // Le stringhe tra virgolette devono corrispondere ai nomi delle variabili nella classe Utente
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Assicurati che nella classe Utente ci sia un metodo getPrestitiAttivi() o una variabile 'prestitiAttivi'
        colPrestiti.setCellValueFactory(new PropertyValueFactory<>("prestitiAttivi")); 

        // 2. Listener per la barra di ricerca
        txtRicerca.textProperty().addListener((obs, oldVal, newVal) -> {
            if (gestore != null) {
                tabellaUtenti.setItems(gestore.ricercaAnagrafica(newVal));
            }
        });
    }

    // Metodo chiamato dal MenuPrincipale per passare i dati
    public void setGestore(GestoreUtenti gestore) {
        this.gestore = gestore;
        tabellaUtenti.setItems(gestore.getLista());
    }

    // --- NAVIGAZIONE (INDIETRO) ---
    @FXML
    private void handleIndietro(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipale.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestione Biblioteca - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alert("Errore Navigazione", "Impossibile tornare al menu.");
        }
    }

    // --- GESTIONE CRUD (Create, Update, Delete) ---

    @FXML
    private void handleNuovo() {
        // Passiamo null perché è un nuovo utente
        apriDialogUtente(null);
    }

    @FXML
    private void handleModifica() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            apriDialogUtente(selezionato);
        } else {
            alert("Nessuna selezione", "Seleziona un utente da modificare.");
        }
    }

    @FXML
    private void handleElimina() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();

        if (selezionato != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma Eliminazione");
            alert.setHeaderText("Stai per eliminare l'utente: " + selezionato.getNome() + " " + selezionato.getCognome());
            alert.setContentText("Questa operazione è irreversibile.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Rimuove e salva automaticamente se il gestore è fatto bene
                boolean rimosso = gestore.rimuovi(selezionato);
                if (!rimosso) {
                    alert("Errore", "Impossibile rimuovere l'utente (forse ha prestiti attivi?).");
                }
            }
        } else {
            alert("Nessuna selezione", "Seleziona un utente da eliminare.");
        }
    }

    // --- LOGICA DEL DIALOG (Finestra Modifica/Nuovo) ---
    private void apriDialogUtente(Utente utenteDaModificare) {
        Dialog<Utente> dialog = new Dialog<>();
        dialog.setTitle(utenteDaModificare == null ? "Nuovo Utente" : "Modifica Utente");
        dialog.setHeaderText("Inserisci i dati dell'utente");

        // Bottoni
        ButtonType btnSalvaType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSalvaType, ButtonType.CANCEL);

        // Layout Griglia
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Campi di input
        TextField txtNome = new TextField();
        TextField txtCognome = new TextField();
        TextField txtMatricola = new TextField();
        TextField txtEmail = new TextField();

        txtNome.setPromptText("Nome");
        txtCognome.setPromptText("Cognome");
        txtMatricola.setPromptText("Matricola Univoca");
        txtEmail.setPromptText("email@studenti.unisa.it");

        // Se Modifica -> Riempi i campi
        if (utenteDaModificare != null) {
            txtNome.setText(utenteDaModificare.getNome());
            txtCognome.setText(utenteDaModificare.getCognome());
            txtMatricola.setText(utenteDaModificare.getMatricola());
            txtMatricola.setDisable(true); // Impediamo di cambiare la matricola (ID) in modifica
            txtEmail.setText(utenteDaModificare.getEmail());
        }

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Cognome:"), 0, 1);
        grid.add(txtCognome, 1, 1);
        grid.add(new Label("Matricola:"), 0, 2);
        grid.add(txtMatricola, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(txtEmail, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convertitore risultato
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSalvaType) {
                String nome = txtNome.getText();
                String cognome = txtCognome.getText();
                String matricola = txtMatricola.getText();
                String email = txtEmail.getText();

                // Validazione semplice
                if (nome.isEmpty() || cognome.isEmpty() || matricola.isEmpty()) {
                    return null; // O gestisci errore
                }

                if (utenteDaModificare == null) {
                    // CREAZIONE NUOVO OGGETTO
                    // Assumo che il costruttore sia: Utente(nome, cognome, matricola, email)
                    return new Utente(nome, cognome, matricola, email); 
                } else {
                    // AGGIORNAMENTO OGGETTO ESISTENTE
                    utenteDaModificare.setNome(nome);
                    utenteDaModificare.setCognome(cognome);
                    // utenteDaModificare.setMatricola(matricola); // Matricola bloccata
                    utenteDaModificare.setEmail(email);
                    return utenteDaModificare;
                }
            }
            return null;
        });

        Optional<Utente> result = dialog.showAndWait();

        result.ifPresent(utente -> {
            if (utenteDaModificare == null) {
                // NUOVO
                try {
                    gestore.aggiungi(utente);
                } catch (IllegalArgumentException e) {
                    alert("Errore", "Utente già esistente o dati non validi.");
                }
            } else {
                // MODIFICA
                gestore.salvaModifiche(); // Metodo dell'interfaccia ManagerGenerale
                tabellaUtenti.refresh();
            }
        });
    }

    private void alert(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
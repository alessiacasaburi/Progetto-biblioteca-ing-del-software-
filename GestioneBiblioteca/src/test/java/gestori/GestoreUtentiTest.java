package view;

import entita.Utente;
import gestori.GestoreUtenti;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.util.Optional;

public class UtentiController {

    // ===== FXML =====
    @FXML private TextField txtRicerca;

    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colEmail;
    @FXML private TableColumn<Utente, Integer> colPrestiti;

    private GestoreUtenti gestore;

    // ===== INIT =====
    @FXML
    public void initialize() {

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Prestiti attivi
        colPrestiti.setCellValueFactory(cell ->
                new SimpleIntegerProperty(
                        (int) cell.getValue()
                                .getPrestitiAttivi()
                                .stream()
                                .filter(p -> p.isPrestitoAttivo())
                                .count()
                ).asObject()
        );

        // 🎨 stile tabella
        tabellaUtenti.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #aed6f1;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;"
        );

        tabellaUtenti.skinProperty().addListener((obs, o, n) -> {
            if (n != null) {
                tabellaUtenti.lookup(".column-header-background").setStyle(
                        "-fx-background-color: #d6eaf8;"
                );
            }
        });
    }

    // ===== INIEZIONE GESTORE =====
    public void setGestore(GestoreUtenti gestore) {
        this.gestore = gestore;
        tabellaUtenti.setItems(gestore.getLista());

        txtRicerca.textProperty().addListener((obs, oldVal, newVal) ->
                tabellaUtenti.setItems(gestore.ricercaAnagrafica(newVal))
        );
    }

    // ===== BOTTONI =====

    @FXML
    private void handleNuovo() {
        apriDialog(null);
    }

    @FXML
    private void handleModifica() {
        Utente u = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (u == null) {
            alert("Attenzione", "Seleziona un utente dalla tabella.");
            return;
        }
        apriDialog(u);
    }

    @FXML
    private void handleElimina() {
        Utente u = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (u == null) {
            alert("Attenzione", "Seleziona un utente da eliminare.");
            return;
        }

        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione");
        conferma.setHeaderText("Stai eliminando l'utente:");
        conferma.setContentText(u.toString());

        if (conferma.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            gestore.rimuovi(u);
        }
    }

    // 🔥 ORA TORNA DAVVERO ALLA HOME
    @FXML
    private void handleIndietro(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/MenuPrincipale.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Biblioteca Manager - Home");
            stage.show();

        } catch (Exception e) {
            alert("Errore", "Impossibile tornare alla Home.");
            e.printStackTrace();
        }
    }

    // ===== DIALOG UTENTE =====
    private void apriDialog(Utente u) {

        Dialog<Utente> dialog = new Dialog<>();
        dialog.setTitle(u == null ? "Nuovo Utente" : "Modifica Utente");
        dialog.setHeaderText("Inserisci i dati dell'utente");

        dialog.getDialogPane().setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffffff, #ebf5fb);" +
                "-fx-border-color: #85c1e9;" +
                "-fx-border-radius: 16;" +
                "-fx-background-radius: 16;"
        );

        ButtonType salva = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salva, ButtonType.CANCEL);

        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salva);
        btnSalva.setStyle(
                "-fx-background-color: #5dade2;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;"
        );

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        grid.setPadding(new Insets(20));

        String fieldStyle =
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: #aed6f1;";

        String labelStyle =
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #2e86c1;";

        TextField nome = new TextField();
        nome.setStyle(fieldStyle);

        TextField cognome = new TextField();
        cognome.setStyle(fieldStyle);

        TextField matricola = new TextField();
        matricola.setStyle(fieldStyle);

        TextField email = new TextField();
        email.setStyle(fieldStyle);

        if (u != null) {
            nome.setText(u.getNome());
            cognome.setText(u.getCognome());
            matricola.setText(u.getMatricola());
            matricola.setDisable(true);
            email.setText(u.getEmail());
        }

        grid.addRow(0, new Label("Nome"), nome);
        grid.addRow(1, new Label("Cognome"), cognome);
        grid.addRow(2, new Label("Matricola"), matricola);
        grid.addRow(3, new Label("Email"), email);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == salva) {
                try {
                    if (u == null) {
                        return new Utente(
                                matricola.getText(),
                                nome.getText(),
                                cognome.getText(),
                                email.getText()
                        );
                    } else {
                        String nuovoNome = nome.getText();
                        String nuovoCognome = cognome.getText();
                        String nuovaEmail = email.getText();

                        Utente temp = new Utente(
                                u.getMatricola(),
                                nuovoNome,
                                nuovoCognome,
                                nuovaEmail
                        );
                        temp.verificamailmatr();

                        u.setNome(nuovoNome);
                        u.setCognome(nuovoCognome);
                        u.setEmail(nuovaEmail);

                        gestore.salvaModifiche();
                        tabellaUtenti.refresh();
                        return u;
                    }
                } catch (Exception e) {
                    alert("Errore", e.getMessage());
                }
            }
            return null;
        });

        Optional<Utente> result = dialog.showAndWait();

        result.ifPresent(utente -> {
            if (u == null) {
                try {
                    gestore.aggiungi(utente);
                } catch (Exception e) {
                    alert("Errore", e.getMessage());
                }
            }
        });
    }

    // ===== ALERT =====
    private void alert(String titolo, String messaggio) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(titolo);
        a.setHeaderText(null);
        a.setContentText(messaggio);
        a.showAndWait();
    }
}

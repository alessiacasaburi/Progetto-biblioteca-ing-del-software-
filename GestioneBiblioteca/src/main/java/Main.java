import entita.Prestito;
import entita.Libro;
import gestori.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    
    private GestoreLibri gestoreLibri = new GestoreLibri();
    private GestoreUtenti gestoreUtenti = new GestoreUtenti();
    private GestorePrestiti gestorePrestiti = new GestorePrestiti();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestione Biblioteca Universitaria");

        // Contenitore principale a Schede
        TabPane tabPane = new TabPane();
        
        // Creiamo le 3 schede
        Tab tabLibri = new Tab("Gestione Libri", creaInterfacciaLibri());
        Tab tabUtenti = new Tab("Gestione Utenti", creaInterfacciaUtenti());
        Tab tabPrestiti = new Tab("Gestione Prestiti", creaInterfacciaPrestiti());

        // Impediamo che le schede si possano chiudere con la X
        tabLibri.setClosable(false);
        tabUtenti.setClosable(false);
        tabPrestiti.setClosable(false);

        tabPane.getTabs().addAll(tabLibri, tabUtenti, tabPrestiti);

        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ---------------------------------------------------
    // SCHEDA 1: INTERFACCIA LIBRI
    // ---------------------------------------------------
    private VBox creaInterfacciaLibri() {
        VBox layout = new VBox(10); // 10px di spazio verticale
        layout.setPadding(new Insets(15));

        // 1. Barra di Ricerca
        HBox ricercaBox = new HBox(10);
        TextField txtRicerca = new TextField();
        txtRicerca.setPromptText("Cerca per titolo, autore o ISBN...");
        txtRicerca.setPrefWidth(300);
        Button btnCerca = new Button("Cerca");
        Button btnReset = new Button("Mostra Tutti");
        ricercaBox.getChildren().addAll(txtRicerca, btnCerca, btnReset);

        // 2. Tabella
        TableView<Libro> tabellaLibri = new TableView<>();
        
        // Colonne (Devono corrispondere ai getters di Libro: getTitolo, getIsbn...)
        TableColumn<Libro, String> colTitolo = new TableColumn<>("Titolo");
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        
        TableColumn<Libro, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn")); // Chiama getIsbn()
        
        TableColumn<Libro, Integer> colCopie = new TableColumn<>("Copie Disp.");
        colCopie.setCellValueFactory(new PropertyValueFactory<>("copieDisponibili"));

        tabellaLibri.getColumns().addAll(colTitolo, colIsbn, colCopie);
        
        // Colleghiamo i dati: La tabella "osserva" la lista del gestore
        tabellaLibri.setItems(gestoreLibri.getLista());
        
        // Logica pulsanti ricerca
        btnCerca.setOnAction(e -> {
            ObservableList<Libro> risultati = gestoreLibri.ricercaTestuale(txtRicerca.getText());
            tabellaLibri.setItems(risultati);
        });
        
        btnReset.setOnAction(e -> {
            txtRicerca.clear();
            tabellaLibri.setItems(gestoreLibri.getLista());
        });

        // 3. Pulsanti Azioni (Aggiungi/Rimuovi)
        HBox bottoniBox = new HBox(10);
        Button btnNuovo = new Button("Nuovo Libro");
        Button btnElimina = new Button("Elimina Selezionato");
        
        // Stile "Pericoloso" per il tasto elimina
        btnElimina.setStyle("-fx-background-color: #ffcccc; -fx-text-fill: red;");

        // Azione Elimina
        btnElimina.setOnAction(e -> {
            Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();
            if (selezionato != null) {
                gestoreLibri.rimuovi(selezionato);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Seleziona un libro da eliminare.");
                alert.show();
            }
        });

        // Azione Nuovo (Apre una dialog - DA IMPLEMENTARE DOPO)
        btnNuovo.setOnAction(e -> {
            // Qui apriremo una nuova finestrella per inserire i dati
            System.out.println("Apertura finestra nuovo libro...");
        });

        bottoniBox.getChildren().addAll(btnNuovo, btnElimina);

        layout.getChildren().addAll(ricercaBox, tabellaLibri, bottoniBox);
        return layout;
    }

    // ---------------------------------------------------
    // SCHEDA 2: INTERFACCIA UTENTI
    // ---------------------------------------------------
    private VBox creaInterfacciaUtenti() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        
        Label lblTitolo = new Label("Gestione Utenti (Da completare)");
        // Qui copierai la stessa logica di creaInterfacciaLibri, cambiando i tipi
        
        layout.getChildren().add(lblTitolo);
        return layout;
    }

    // ---------------------------------------------------
    // SCHEDA 3: INTERFACCIA PRESTITI
    // ---------------------------------------------------
    private VBox creaInterfacciaPrestiti() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        
        Label lblTitolo = new Label("Gestione Prestiti (Da completare)");
        
        TableView<Prestito> tabellaPrestiti = new TableView<>();
        // TODO: Configurare colonne Utente, Libro, Data Scadenza
        
        tabellaPrestiti.setItems(gestorePrestiti.getLista());
        
        layout.getChildren().addAll(lblTitolo, tabellaPrestiti);
        return layout;
    }
}
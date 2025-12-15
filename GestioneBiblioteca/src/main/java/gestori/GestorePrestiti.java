package gestori;


import entita.Prestito;
import entita.Libro;
import entita.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** 
 * @brief Classe responsabile della gestione delle operazioni di prestito.
 * @author Annamaria
 */
public class GestorePrestiti implements ManagerGenerale<Prestito> {

    /** Lista contenente tutti i prestiti gestiti. */
    private ObservableList<Prestito> listaPrestiti;
    private static final String FILE_PRESTITI = "archivio_prestiti.dat";

    /**
     * @brief Costruttore della classe GestorePrestiti. Carica la lista da file.
     */
    public GestorePrestiti() {
          this.listaPrestiti = Salvataggio.caricaLista(FILE_PRESTITI);
    }
    
    /**
     * @brief Costruttore per i test. Inizializza la lista vuota senza caricare da file.
     * @param forTest Flag per indicare l'uso in ambiente di test.
     */
    public GestorePrestiti(boolean forTest) {
        if (forTest) {
            this.listaPrestiti = FXCollections.observableArrayList();
        } else {
            this.listaPrestiti = Salvataggio.caricaLista(FILE_PRESTITI);
        }
    }
    

    /**
     * @brief Registra un nuovo prestito nel sistema.
     * @param prestito il prestito da registrare nel sistema.
     * Verifica che l'utente non abbia più di 3 prestiti attivi e che il libro sia disponibile,
     * successivamente aggiorna la lista dei prestiti generale e la lista dei prestiti dell'utente.
     */
    @Override
    public void aggiungi(Prestito prestito) {
        try {
            Utente u = prestito.getUtente();
            Libro l = prestito.getLibro();

            
            u.verificaPrestitiAttivi(); 
            l.isDisponibile(); 

           
            l.decrementaDisponibilita();
            u.aggiungiPrestito(prestito); 
            listaPrestiti.add(prestito);
            
           

        } catch (Exception e) {
            
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @brief Gestisce la RESTITUZIONE del libro.
     * @param prestito il prestito da terminare
     */
    public void terminaPrestito(Prestito prestito) {
        if (prestito.isPrestitoAttivo()) {
            prestito.setPrestitoConcluso();
            prestito.getLibro().incrementaDisponibilita();
            
            salvaModifiche();
        }
    }
    

   
    
    /**
     * @brief Restituisce solo i prestiti attualmente attivi (non restituiti).
     */
    public ObservableList<Prestito> getPrestitiAttivi() {
        ObservableList<Prestito> attivi = FXCollections.observableArrayList();
        for (Prestito p : listaPrestiti) {
            if (p.isPrestitoAttivo()) {
                attivi.add(p);
            }
        }
        return attivi;
    }
    
    /*
     * Cerca corrispondenze nel titolo/ISBN del libro e nel nome/cognome/matricola dell'utente.
     * @param testo La stringa da cercare.
     * @return Una ObservableList contenente i prestiti che soddisfano i criteri.
     */
    public ObservableList<Prestito> ricercaTestuale(String testo) {
        
        ObservableList<Prestito> risultati = FXCollections.observableArrayList();
        
        if (testo == null || testo.isEmpty()) {
            return this.listaPrestiti; 
        }
        
        String testoLower = testo.toLowerCase();
        
        for (Prestito p : this.listaPrestiti) {
            boolean matchLibroTitolo = p.getLibro().getTitolo().toLowerCase().contains(testoLower);
            boolean matchLibroIsbn = p.getLibro().getIsbn().toLowerCase().contains(testoLower);
           
            boolean matchUtenteNome = p.getUtente().getNome().toLowerCase().contains(testoLower);
            boolean matchUtenteCognome = p.getUtente().getCognome().toLowerCase().contains(testoLower);
            boolean matchUtenteMatricola = p.getUtente().getMatricola().toLowerCase().contains(testoLower);
            
            if (matchLibroTitolo || matchLibroIsbn || matchUtenteNome || matchUtenteCognome || matchUtenteMatricola) {
                risultati.add(p);
            }
        }
          return risultati;
    }
    /**
     * @brief Restituisce la lista dei prestiti.
     */
    @Override
    public ObservableList<Prestito> getLista() {
        return listaPrestiti;
    }
    
    /**
     * @brief Cerca un prestito nel sistema.
     * @param oggetto il prestito da cercare.
     * Scorre tutta la lista e verifica se esiste un prestito con gli stessi parametri matricola,
     * ISBN e data di inizio.
     */
    @Override
    public Prestito cerca(Prestito oggetto) {
        
        for (Prestito p : listaPrestiti) {
            if (p.getUtente().getMatricola().equals(oggetto.getUtente().getMatricola()) &&
                p.getLibro().getIsbn().equals(oggetto.getLibro().getIsbn()) &&
                p.getDataInizio().isEqual(oggetto.getDataInizio())) {
                return p;
            }
        }
       return null;
    }
    
    /**
     * @brief Rimuove un prestito dalla lista prestiti (Cancellazione completa).
     * Usare 'terminaPrestito' per le restituzioni normali.
     * @param prestito il prestito da rimuovere
     */
    @Override
    public boolean rimuovi(Prestito prestito) {
        boolean rimosso = listaPrestiti.remove(prestito);
        if (rimosso) {
            salvaModifiche(); // Chiamata al metodo che incapsula Salvataggio
        }
        return rimosso;
    }
    /**
     * Salva lo stato corrente della lista sul file.
     * Da utilizzare dopo modifiche agli oggetti (set) che non alterano la dimensione della lista.
     */
    
    @Override
    public void salvaModifiche() {
    
    Salvataggio.salvaLista(listaPrestiti, FILE_PRESTITI);
    }
}
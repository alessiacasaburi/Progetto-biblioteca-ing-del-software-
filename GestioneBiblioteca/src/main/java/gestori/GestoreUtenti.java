package gestori;

import entita.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GestoreUtenti implements ManagerGenerale<Utente> {

    private ObservableList<Utente> listaUtenti;
    private static final String FILE_UTENTI = "archivio_utenti.dat";

    public GestoreUtenti() {
        this.listaUtenti = Salvataggio.caricaLista(FILE_UTENTI);
    }

    /**
     * Ricerca per nome, cognome o matricola
     */
    public ObservableList<Utente> ricercaAnagrafica(String testo) {
        if (testo == null || testo.isEmpty()) {
            return listaUtenti;
        }
        ObservableList<Utente> risultati = FXCollections.observableArrayList();
        String testoLower = testo.toLowerCase();

        for (Utente u : listaUtenti) {
            if (u.getCognome().toLowerCase().contains(testoLower) || 
                u.getNome().toLowerCase().contains(testoLower) || 
                u.getMatricola().toLowerCase().contains(testoLower)) {
                risultati.add(u);
            }
        }
        return risultati;
    }

    @Override
    public void aggiungi(Utente nuovoUtente) {
        if (nuovoUtente == null) {
            throw new IllegalArgumentException("Impossibile aggiungere utente nullo.");
        }

        Utente esistente = cerca(nuovoUtente);
        if (esistente != null) {
            throw new IllegalArgumentException("Utente già presente (Matricola: " + esistente.getMatricola() + ")");
        }

        listaUtenti.add(nuovoUtente);
        // SALVATAGGIO AUTOMATICO
        Salvataggio.salvaLista(listaUtenti, FILE_UTENTI);
    }

    @Override
    public boolean rimuovi(Utente utente) {
        boolean rimosso = listaUtenti.remove(utente);
        if (rimosso) {
            // SALVATAGGIO AUTOMATICO
            Salvataggio.salvaLista(listaUtenti, FILE_UTENTI);
        }
        return rimosso;
    }

    @Override
    public void salvaModifiche() {
        Salvataggio.salvaLista(listaUtenti, FILE_UTENTI);
    }

    @Override
    public ObservableList<Utente> getLista() {
        return listaUtenti;
    }

    @Override
    public Utente cerca(Utente oggetto) {
        if (oggetto == null || oggetto.getMatricola() == null) return null;
        
        for (Utente u : listaUtenti) {
            if (u.getMatricola().equalsIgnoreCase(oggetto.getMatricola())) {
                return u;
            }
        }
        return null;
    }
}
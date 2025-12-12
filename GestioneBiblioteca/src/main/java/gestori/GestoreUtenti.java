/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestori;

import model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @brief Classe responsabile della gestione dell'anagrafica utenti.
 * * Mantiene la lista di tutti gli utenti registrati alla biblioteca e permette
 * operazioni di inserimento, rimozione e ricerca.
 * * @author Alessia
 */
public class GestoreUtenti implements ManagerGenerale<Utente> {

    /** Lista contenente tutti gli utenti registrati. */
    private ObservableList<Utente> listaUtenti;

    /**
     * @brief Costruttore della classe GestoreUtenti.
     * * Inizializza la lista degli utenti come una collezione vuota.
     */
    public GestoreUtenti() {
        this.listaUtenti = FXCollections.observableArrayList();
    }
    
    /**
     * @brief Cerca utenti per Cognome, Matricola o Nome.
     * @param testo La stringa digitata nell'interfaccia grafica.
     * @return la lista di utenti che hanno rilevato una corrispondenza nei campi con la stringa in ingresso.
     */
    public ObservableList<Utente> ricercaAnagrafica(String testo) {
        ObservableList<Utente> risultati = FXCollections.observableArrayList();

        if (testo == null || testo.isEmpty()) {
            return listaUtenti; 
        }
        
        String testoLower = testo.toLowerCase();

        /**
         * Il blocco successivo effettua la ricerca vera e propria: il testo inserito viene prima convertito in minuscolo
         * per rendere il confronto case‑insensitive, poi si scorre l’intera lista degli utenti e per ciascuno si
         * controlla se il nome, il cognome o la matricola contengono la stringa ricercata. Se almeno uno dei campi
         * presenta una corrispondenza, quell’utente viene aggiunto alla lista dei risultati che verrà poi restituita.
         */
        for (Utente u : listaUtenti) {
            
            boolean matchCognome = u.getCognome().toLowerCase().contains(testoLower);
            boolean matchMatricola = u.getMatricola().contains(testoLower);
            boolean matchNome = u.getNome().toLowerCase().contains(testoLower);

            if (matchCognome || matchMatricola || matchNome) {
                risultati.add(u);
            }
        }

        return risultati;
    }

    /**
     * @brief Registra un nuovo utente nel sistema.
     * @param utente L'oggetto di tipo Utente da aggiungere alla lista utenti della biblioteca.
     * si richiama il metodo che verifica la validità delle credenziali e poi controlla che la matricola non sia già presente nella lista.
     * Se tutte le verifiche sono superate, l'utente viene aggiunto alla collezione degli utenti registrati.
     */
   
    @Override
    public void aggiungi(Utente utente) {

        utente.verificamailmatr();
           
        if (cerca(utente) != null) {
            throw new IllegalArgumentException("Errore: Matricola già registrata nel sistema.");
        }
        listaUtenti.add(utente);// Se tutte le verifiche sono superate, l'utente viene aggiunto alla lista principale.
    }

    /**
     * @brief Rimuove un utente dal sistema.
     * @param utente L'oggetto Utente da rimuovere.
     * @return true se la rimozione è avvenuta con successo, false altrimenti.
     */
    @Override
    public boolean rimuovi(Utente utente) {
        return listaUtenti.remove(utente);
    }

    /**
     * @brief Restituisce la lista completa degli utenti.
     * @return La lista contenente gli utenti.
     */
    @Override
    public ObservableList<Utente> getLista() {
        return listaUtenti;
    }
    
    /**
     * @brief Cerca un utente specifico nell'anagrafica basandosi sulla matricola.
     * @param oggetto L'utente da cercare all'interno della lista utenti.
     * verifica che l'utente e che il campo matricola e esistano
     * scorre la lista utenti e verifica che non esistano utenti che abbiano quella matricola.
     * @return L'oggetto Utente trovato, oppure null se non esiste.
     */

    @Override
    public Utente cerca(Utente oggetto) {
        if (oggetto == null || oggetto.getMatricola() == null) {
            return null;
        }

        for (Utente u : listaUtenti) {
            
            if (u.getMatricola().equals(oggetto.getMatricola())) {
                return u;
            }
        }
        return null;
    }
}
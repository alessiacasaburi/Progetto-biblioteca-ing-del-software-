/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestori;

import model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @brief Classe responsabile della gestione dell'anagrafica utenti.
 * * Mantiene la lista di tutti gli utenti registrati alla biblioteca e permette
 * operazioni di inserimento, rimozione e ricerca.
 * * @author Alessandro
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
     * @brief Registra un nuovo utente nel sistema.
     * @param utente L'oggetto Utente da aggiungere alla lista.
     */
    @Override
    public void aggiungi(Utente utente) {
        // TODO: Implementare 
    }

    /**
     * @brief Rimuove un utente dal sistema.
     * @param utente L'oggetto Utente da rimuovere.
     * @return true se la rimozione è avvenuta con successo, false altrimenti.
     */
    @Override
    public boolean rimuovi(Utente utente) {
        // TODO: Implementare 
        return false;
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
     * @brief Cerca un utente specifico nell'anagrafica.
     * @param oggetto L'utente da trovare.
     * @return L'oggetto Utente trovato, oppure null se non esiste.
     */
    @Override
    public Utente cerca(Utente oggetto) {
        // TODO: Implementare 
        return null;
    }
}
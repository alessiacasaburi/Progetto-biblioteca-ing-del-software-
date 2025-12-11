/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestori;

/**
 * @brief Classe responsabile della gestione delle operazioni di prestito.
 * * Mantiene lo storico dei prestiti, permette di aggiungerne di nuovi e fornisce
 * funzionalità di ricerca e filtro per utente o stato del prestito.
 * * @author Alessandro
 */
import model.*;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GestorePrestiti implements ManagerGenerale<Prestito> {

    /** Lista contenente tutti i prestiti gestiti. */
    private ObservableList<Prestito> listaPrestiti;

    /**
     * @brief Costruttore della classe GestorePrestiti.
     * * Inizializza la collezione dei prestiti.
     */
    public GestorePrestiti() {
        this.listaPrestiti = FXCollections.observableArrayList();
    }

    /**
     * @brief Registra un nuovo prestito nel sistema.
     * @param prestito L'oggetto Prestito da aggiungere alla lista.
     */
    @Override
    public void aggiungi(Prestito prestito) {
        // TODO: Implementare 
    }

    /**
     * @brief Rimuove un prestito dal sistema.
     * @param prestito L'oggetto Prestito da rimuovere.
     * @return true se la rimozione ha successo, false altrimenti.
     */
    @Override
    public boolean rimuovi(Prestito prestito) {
        // TODO: Implementare 
        return false;
    }

    /**
     * @brief Restituisce la lista completa dei prestiti.
     * @return La ObservableList contenente lo storico dei prestiti.
     */
    @Override
    public ObservableList<Prestito> getLista() {
        return listaPrestiti;
    }

    /**
     * @brief Cerca un prestito specifico.
     * @param oggetto Il prestito da cercare.
     * @return L'oggetto Prestito trovato, oppure null.
     */
    @Override
    public Prestito cerca(Prestito oggetto) {
        // TODO: Implementare 
        return null;
    }

    /**
     * @brief Filtra i prestiti associati a un determinato utente.
     * @param utente L'utente di cui si vogliono visualizzare i prestiti.
     * @return Una lista di prestiti effettuati dall'utente specificato.
     */
    public List<Prestito> getPrestitiDiUtente(Utente utente) {
        // TODO: Implementare 
        return null;
    }

    /**
     * @brief Filtra i prestiti attualmente attivi (non ancora restituiti).
     * @return Una lista contenente solo i prestiti con stato attivo.
     */
    public List<Prestito> getPrestitiAttivi() {
        // TODO: Implementare 
        return null;
    }
}
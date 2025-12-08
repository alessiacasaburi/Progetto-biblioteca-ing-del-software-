/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gestori;

/**
 * @brief Classe responsabile della gestione dell'inventario dei libri.
 * * Implementa l'interfaccia ManagerGenerale specializzandola per la classe Libro,
 * permettendo l'aggiunta, la rimozione e la ricerca all'interno di una lista osservabile.
 * * @author Alessandro
 */
import model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GestoreLibri implements ManagerGenerale<Libro> {

    /** Collezione osservabile che contiene tutti i libri presenti nel sistema. */
    private ObservableList<Libro> listaLibri;

    /**
     * @brief Costruttore della classe GestoreLibri.
     * * Inizializza la lista dei libri come una collezione vuota.
     */
    public GestoreLibri() {
        this.listaLibri = FXCollections.observableArrayList();
    }

    /**
     * @brief Aggiunge un nuovo libro al catalogo.
     * @param libro L'oggetto Libro da inserire nella lista.
     */
    @Override
    public void aggiungi(Libro libro) {
        // TODO: Implementare 
    }

    /**
     * @brief Rimuove un libro dal catalogo.
     * @param libro L'oggetto Libro da rimuovere.
     * @return true se l'operazione è andata a buon fine, false altrimenti.
     */
    @Override
    public boolean rimuovi(Libro libro) {
        // TODO: Implementare 
        return false;
    }

    /**
     * @brief Restituisce la lista attuale dei libri.
     * * @return La lista contenente i libri.
     */
    @Override
    public ObservableList<Libro> getLista() {
        return listaLibri;
    }

    /**
     * @brief Cerca un libro specifico nel catalogo.
     * @param oggetto Il libro (o l'oggetto con i criteri di ricerca) da trovare.
     * @return L'oggetto Libro corrispondente se trovato, null altrimenti.
     */
    @Override
    public Libro cerca(Libro oggetto) {
        // TODO: Implementare 
        return null;
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestori;

import javafx.collections.ObservableList;

/**
 * @brief Interfaccia generica per la gestione delle entità del sistema.
 * * Definisce i metodi fondamentali (CRUD) che ogni classe di gestione (es. GestoreLibri, GestoreUtenti) deve implementare.
 * * @tparam T Il tipo di oggetto gestito dall'implementazione (es. Libro, Utente, Prestito).
 * @author Alessandro
 */
public interface ManagerGenerale<T> {
    
    /**
     * @brief Aggiunge un nuovo elemento alla collezione gestita.
     * @param elemento L'oggetto di tipo T da inserire nel sistema.
     */
    void aggiungi(T elemento);
    
    /**
     * @brief Rimuove un elemento esistente dalla collezione.
     * @param elemento L'oggetto di tipo T da rimuovere.
     * @return true se l'elemento è stato rimosso con successo, false se non trovato o se l'operazione fallisce.
     */
    boolean rimuovi(T elemento);
    
    /**
     * @brief Restituisce l'intera lista degli elementi gestiti.
     * @return Una ObservableList contenente tutti gli oggetti di tipo T.
     */
    ObservableList<T> getLista();
   
    /**
     * @brief Cerca un elemento specifico nella collezione.
     * @param oggetto L'oggetto da cercare (o un oggetto contenente i criteri di ricerca).
     * @return L'oggetto trovato di tipo T, oppure null se non presente.
     */
    T cerca(T oggetto); 
   
    
    /**
     * Salva lo stato corrente della lista sul file.
     * Da utilizzare dopo modifiche agli oggetti (set) che non alterano la dimensione della lista.
     */
    void salvaModifiche();
}
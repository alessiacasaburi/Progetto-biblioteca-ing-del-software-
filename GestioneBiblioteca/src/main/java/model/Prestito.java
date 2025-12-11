 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 * @brief Rappresenta una transazione di prestito all'interno della biblioteca.
 * * Questa classe associa un Utente a un Libro, tracciando la data di inizio,
 * la data di scadenza prevista e lo stato corrente del prestito (attivo o concluso),
 * la variabile prestito attivo evidenzia velocemente i prestiti in ritardo.
 * * @author Alessandro
 */
public class Prestito {

    private Utente utente;
    private Libro libro;
    private LocalDate dataInizio;
    private LocalDate dataScadenza;
    private boolean prestitoAttivo;

    /**
     * @brief Costruttore della classe Prestito.
     * * Inizializza un nuovo prestito e lo imposta automaticamente come attivo (true).
     * * @param utente        L'oggetto Utente che richiede il prestito.
     * @param libro         L'oggetto Libro che viene prestato.
     * @param dataInizio    La data in cui ha inizio il prestito.
     * @param dataScadenza  La data entro cui il libro deve essere restituito.
     */
    public Prestito(Utente utente, Libro libro, LocalDate dataInizio, LocalDate dataScadenza) {
        this.utente = utente;
        this.libro = libro;
        this.dataInizio = dataInizio;
        this.dataScadenza = dataScadenza;
        this.prestitoAttivo = true; 
    }

    /**
     * @brief Verifica lo stato attuale del prestito.
     * @return true se il libro non è ancora stato restituito, false altrimenti.
     */
    public boolean isPrestitoAttivo() {
        return prestitoAttivo;
    }
    /**
     * @brief Modifica lo stato del prestito.
     * * Utilizzato principalmente per chiudere un prestito (impostandolo a false) al momento della restituzione.
     * * @param prestitoAttivo true per renderlo attivo, false per indicare che è concluso.
     */
    public void setPrestitoAttivo(boolean prestitoAttivo){
        this.prestitoAttivo = prestitoAttivo;
    }
    
    /**
     * @brief Restituisce l'utente associato a questo prestito.
     * @return L'oggetto Utente.
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * @brief Restituisce il libro oggetto del prestito.
     * @return L'oggetto Libro.
     */
    public Libro getLibro() {
        return libro;
    }

    /**
     * @brief Restituisce la data di scadenza del prestito.
     * @return La data entro cui il libro va restituito.
     */
    public LocalDate getDataScadenza() {
        return dataScadenza;
    }
    
    /**
     * @brief Restituisce la data di inizio del prestito.
     * @return La data in cui il prestito è stato registrato.
     */
    public LocalDate getDataInizio() {
        return dataInizio;
    }
}
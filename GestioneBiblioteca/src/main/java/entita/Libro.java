/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entita;   

import java.util.List;
import java.io.Serializable;

/**
 * @brief Rappresenta un libro nel catalogo della biblioteca.
 * * Questa classe contiene le informazioni principali (titolo, autori, ISBN, annodipublicazione)
 * e gestisce anche le disponibilità.
 * * @author Alessandro
 */
public class Libro implements Serializable {

    private String titolo;
    private List<String> autori;
    private int annoPubblicazione;
    private String isbn;
    private int copieDisponibili;

    /**
     * @brief Costruttore della classe Libro.
     * * Inizializza un nuovooggetto della classe librpo e il numero iniziale di copie.
     * @param titolo            Il titolo del libro.
     * @param autori            La lista degli autori del libro.
     * @param annoPubblicazione L'anno in cui il libro è stato pubblicato.
     * @param isbn              Il codice ISBN univoco del libro.
     * @param copieDisponibili  Il numero di copie fisiche disponibili per il prestito.
     */
    public Libro(String titolo, List<String> autori, int annoPubblicazione, String isbn, int copieDisponibili) {
        this.titolo = titolo;
        this.autori = autori;
        this.annoPubblicazione = annoPubblicazione;
        this.isbn = isbn;
        this.copieDisponibili = copieDisponibili;
        verificaisbn();
    }

    /**
     * @brief Restituisce il titolo del libro.
     * @return Una stringa contenente il titolo.
     */
    public String getTitolo() {
        if (this.titolo == null) {
        return ""; 
    }
        
        return titolo;
    }

    /**
     * @brief Imposta un nuovo titolo per il libro.
     * @param titolo Il nuovo titolo da assegnare.
     */
    public void setTitolo(String titolo) {
        
        this.titolo = titolo;
    }
    
    /**
     * @brief Imposta un nuovo anno di pubblicazione per il libro.
     * @param anno Il nuovo anno da assegnare.
     */
    public void setAnnoPubblicazione(int anno) {
        this.annoPubblicazione = anno;
    }

    /**
     * @brief Restituisce la lista degli autori.
     * @return Una lista di stringhe con i nomi degli autori.
     */
    public List<String> getAutori() {
        return autori;
    }

    /**
     * @brief Imposta la lista degli autori.
     * @param autori La nuova lista di autori.
     */
    public void setAutori(List<String> autori) {
        this.autori = autori;
    }

    /**
     * @brief Restituisce l'anno di pubblicazione.
     * @return L'anno come intero.
     */
    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    /**
     * @brief Restituisce il codice ISBN.
     * @return Una stringa contenente l'ISBN.
     */
    public String getIsbn() {
        return isbn;
    }
    
    /**
     * @brief Restituisce il numero di copie attualmente disponibili.
     * @return Il numero di copie disponibili.
     */
    public int getCopieDisponibili() {
        return copieDisponibili;
    }

    /**
     * @brief Imposta manualmente il numero di copie disponibili.
     * @param n Il nuovo numero di copie.
     */
    public void setCopieDisponibili(int n) {
        this.copieDisponibili = n;
    }

    /**
     * @brief Verifica se ci sono copie disponibili del libro.
    * @throws IllegalArgumentException se il libro non è disponibile (copie <= 0).
    */
    public boolean isDisponibile() {
    //  Lancia un errore se necessario.
        if (this.copieDisponibili <= 2) {
        throw new IllegalArgumentException("Errore: Impossibile prestare. Copie disponibili insufficienti.");
    }
        return true;
    }

    /**
     * @brief Aumenta di una unità il numero di copie disponibili.
     * * Da utilizzare quando un libro viene restituito.
     */
    public void incrementaDisponibilita() {
        this.copieDisponibili++;
    }

    /**
     * @brief Diminuisce di una unità il numero di copie disponibili.
     * * Da utilizzare quando viene registrato un nuovo prestito.
     */
    public void decrementaDisponibilita() {
        this.copieDisponibili--;
    }
    
    /**
     * @brief Verifica la validità del codice ISBN del libro.
     * * Controlla che il campo ISBN non sia nullo e che corrisponda esattamente 
     * a una sequenza di 13 cifre numeriche.
     * * @return true se il codice ISBN è valido.
     * @throws IllegalArgumentException Se l'ISBN è nullo o non rispetta il formato di 13 cifre.
     */
     public boolean verificaisbn() {
       if(this.isbn== null|| !this.isbn.matches("\\d{13}")){
           throw new IllegalArgumentException("isbn non valido");
        }
       return true;
    }  

    @Override
    /**
     * @brief Stampa un libro come stringa 
     ** Da utilizzare per visualizzare la lista libri.
     * @return il libro come stringa
     */
    public String toString() {
        return titolo + " (" + isbn + ")";
    }
}

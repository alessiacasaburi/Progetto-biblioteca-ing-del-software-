/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 * @brief Rappresenta un libro nel catalogo della biblioteca.
 * * Questa classe contiene le informazioni principali (titolo, autori, ISBN, annodipublicazione)
 * e gestisce anche le disponibilità.
 * * @author Alessandro
 */
public class Libro {

    private String titolo;
    private List<String> autori;
    private int annoPubblicazione;
    private String isbn;
    private int copieDisponibili;

    /**
     * @brief Costruttore della classe Libro.
     * * Inizializza un nuovooggetto della classe librpo e il numero iniziale di copie.
     * * @param isbn              Il codice ISBN univoco del libro.
     * @param titolo            Il titolo del libro.
     * @param autori            La lista degli autori del libro.
     * @param annoPubblicazione L'anno in cui il libro è stato pubblicato.
     * @param copieDisponibili  Il numero di copie fisiche disponibili per il prestito.
     */
    public Libro(String isbn, String titolo, List<String> autori, int annoPubblicazione, int copieDisponibili) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.autori = autori;
        this.annoPubblicazione = annoPubblicazione;
        this.copieDisponibili = copieDisponibili;
    }

    /**
     * @brief Restituisce il titolo del libro.
     * @return Una stringa contenente il titolo.
     */
    public String getTitolo() {
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
     * @brief Verifica se il libro è disponibile per il prestito.
     * * Controlla se il numero di copie disponibili è maggiore di zero.
     * * @return true se ci sono copie disponibili, false altrimenti.
     */
    public boolean isDisponibile() {
        // TODO: Implementare
        return false;
    }

    /**
     * @brief Aumenta di una unità il numero di copie disponibili.
     * * Da utilizzare quando un libro viene restituito.
     */
    public void incrementaDisponibilita() {
        // TODO: Implementare 
    }

    /**
     * @brief Diminuisce di una unità il numero di copie disponibili.
     * * Da utilizzare quando viene registrato un nuovo prestito.
     */
    public void decrementaDisponibilita() {
        // TODO: Implementare 
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief Rappresenta un utente registrato nella biblioteca.
 * * Questa classe gestisce i dati anagrafici dell'utente (nome, cognome, matricola, email)
 * e mantiene la lista dei prestiti attualmente attivi associati all'utente stesso.
 * * @author Alessandro
 */
public class Utente {

    
    private String nome;
    private String cognome;
    private String matricola;
    private String email;
    private List<Prestito> prestitiAttivi;

    /**
     * @brief Costruttore della classe Utente.
     * * Inizializza un nuovo utente con i dati forniti e crea una lista vuota per i prestiti.
     * * @param matricola Il codice identificativo univoco (es. numero di matricola universitaria).
     * @param nome      Il nome dell'utente.
     * @param cognome   Il cognome dell'utente.
     * @param email     L'indirizzo email dell'utente.
     */
    public Utente(String matricola, String nome, String cognome, String email) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.prestitiAttivi = new ArrayList<>();
    }

    /**
     * @brief Restituisce la matricola dell'utente.
     * @return Una stringa che rappresenta la matricola.
     */
    public String getMatricola() {
        return matricola;
    }

    /**
     * @brief Restituisce il nome dell'utente.
     * @return Il nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * @brief Imposta un nuovo nome per l'utente.
     * @param nome Il nuovo nome da assegnare.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @brief Restituisce il cognome dell'utente.
     * @return Il cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * @brief Imposta un nuovo cognome per l'utente.
     * @param cognome Il nuovo cognome da assegnare.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @brief Restituisce l'email dell'utente.
     * @return L'indirizzo email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @brief Imposta un nuovo indirizzo email per l'utente.
     * @param email La nuova email da assegnare.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @brief Restituisce la lista dei prestiti attivi dell'utente.
     * @return Una lista di oggetti di tipo Prestito.
     */
    public List<Prestito> getPrestitiAttivi() {
        return prestitiAttivi;
    }
    
    /**
     * @brief Aggiunge il prestito alla lista prestiti degli utenti 
     * @param p l'oggetto della classe prestiti da aggiungere alla lista.
     *  Controlla che lo stesso prestito non esista già nella lista.
     */
    public void aggiungiPrestito(Prestito p) {
    if(!prestitiAttivi.contains(p)) {
           prestitiAttivi.add(p);
        }
    }
    
     /**
     * @brief Rimuove il prestito dalla lista prestiti degli utenti 
     * @param p l'oggetto della classe prestiti da rimuovere dalla lista.
     */
    public void rimuoviPrestito(Prestito p) {
        prestitiAttivi.remove(p);
    }
    
    /**
     * @brief Verifica la validità della matricola o dell'email.
     * * Controlla se i formati della matricola e dell'email rispettano i criteri stabiliti.
     * * @return true se i dati sono validi, false altrimenti.
     */
    public boolean verificamailmatr() {
       if(this.matricola== null|| !this.matricola.matches("\\d{10}")){
           throw new IllegalArgumentException("Matricola non valida");
        }
       if(!this.email.endsWith("@unisa.it")&& this.email.endsWith("@studenti.unisa.it")){
           throw new IllegalArgumentException("Email non valida");
        }
       return true;
    }  
    
    @Override
    
     /**
     * @brief Stampa utente come stringa
     * * Da utilizzare per visualizzare la lista utenti.
     */
    public String toString() {
        return cognome + " " + nome + " (" + matricola + ")";
    }
}
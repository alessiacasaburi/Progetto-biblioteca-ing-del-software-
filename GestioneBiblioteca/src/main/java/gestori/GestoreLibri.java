/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestori;

/**
 * @brief Classe responsabile della gestione dell'inventario dei libri.
 * * Implementa l'interfaccia ManagerGenerale specializzandola per la classe Libro,
 * permettendo l'aggiunta, la rimozione e la ricerca all'interno di una lista osservabile.
 * * @author Alessandro
 */
import Entita.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GestoreLibri implements ManagerGenerale<Libro> {

    /** Collezione osservabile che contiene tutti i libri presenti nel sistema. */
    private ObservableList<Libro> listaLibri;
    private static final String FILE_LIBRI = "archivio_libri.dat";
       
    
    /**
     * @brief Costruttore della classe GestoreLibri.
     * * Inizializza la lista dei libri come una collezione vuota.
     */
    public GestoreLibri() {
        this.listaLibri = Salvataggio.caricaLista(FILE_LIBRI);
    }
    
    /**
     * @brief implementazione della ricerca testuale
     * @param testo stringa il quale contenuto rappresenta la nostra ricerca
     * La stringa passata come parametro viene coonfrontata con tutti i titoli autori e isbn dei libri presenti nella biblioteca.
     * @return Una ObservableList contenente solo i libri che hanno una congruenza con la stringa passata.
     */
    public ObservableList<Libro> ricercaTestuale(String testo) {
        
        ObservableList<Libro> risultati = FXCollections.observableArrayList();
       
        if (testo == null || testo.isEmpty()) {
            return listaLibri; 
        }
        
        String testoLower = testo.toLowerCase();

        for (Libro l : listaLibri) {
            boolean matchTitolo = l.getTitolo().toLowerCase().contains(testoLower);
            boolean matchISBN = l.getIsbn().toLowerCase().contains(testoLower);
            
            boolean matchAutori = false;
            for (String autore : l.getAutori()) {
                if (autore.toLowerCase().contains(testoLower)) {
                    matchAutori = true;
                    break; 
                }
            }

            if (matchTitolo || matchISBN || matchAutori) {
                risultati.add(l);
            }
        }
        return risultati;
    }
    
    /**
     * @brief Aggiunge un nuovo libro al catalogo.
     * @param libro L'oggetto Libro da inserire nella lista.
     * verifica che isbn non sia nullo o vuoto e lancio un eccezione e verifico
     * che il codice isbn sia univoco confrontandolo nella lista libri
     */
    @Override
    public void aggiungi(Libro libro) {
        if (libro.getIsbn() == null || libro.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("L'ISBN non può essere vuoto.");
        }
        
        for (Libro l : listaLibri) {
            if (l.getIsbn().equals(libro.getIsbn())) {
                throw new IllegalArgumentException("Errore: Esiste già un libro con questo ISBN (" + libro.getIsbn() + ")" + "Titolo" + libro.getTitolo());
            }
        }
        listaLibri.add(libro);
        Salvataggio.salvaLista(listaLibri, FILE_LIBRI);
    }
  
    /**
     * @brief Rimuove un libro dal catalogo.
     * @param libro L'oggetto Libro da rimuovere.
     * @return true se l'operazione è andata a buon fine, false altrimenti.
     */
    @Override
    public boolean rimuovi(Libro libro) {
        boolean rimosso = listaLibri.remove(libro);
        if (rimosso) {
            Salvataggio.salvaLista(listaLibri, FILE_LIBRI);
        }
        return rimosso;
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
     * @brief Cerca un libro specifico nel catalogo confrontando il campo ISBN.
     * verifica che il libro e codice isbn esistano altrimenti ritornano nul
     * @param oggetto Il libro che si intende cercare.
     * @return L'oggetto Libro corrispondente se trovato, null altrimenti.
     */
    @Override
    public Libro cerca(Libro oggetto) {
        if (oggetto == null || oggetto.getIsbn() == null) {
            return null;
        }
        
        for (Libro l : listaLibri) {
            // Confronto SOLO gli ISBN 
            if (l.getIsbn().equalsIgnoreCase(oggetto.getIsbn())) {
                return l; 
            }
        }
        return null; 
    }

}
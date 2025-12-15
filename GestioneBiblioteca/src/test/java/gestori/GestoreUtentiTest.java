/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestori;

/**
 *
 * @author Alessia
 */

import entita.Utente;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Suite per la classe GestoreUtenti.
 */
public class GestoreUtentiTest {

    private GestoreUtenti gestore;
    private Utente utente1;
    private Utente utente2;
    private Utente utenteInvalido;

    @BeforeEach
    public void setUp() {
        // Inizializza un nuovo gestore per ogni test
        gestore = new GestoreUtenti();

        // Utenti validi per test standard
        utente1 = new Utente("Mario", "Rossi","M001", "mario.rossi@unisa.it");
        utente2 = new Utente("Luisa", "Bianchi", "M002", "luisa.bianchi@unisa.it");
        
        // Utente con matricola corta per simulare un potenziale errore di validazione
        // Nota: Assumo che il metodo Utente.verificamailmatr() lanci un'eccezione
        // se i dati non sono validi (es. matricola troppo corta).
        utenteInvalido = new Utente("Pippo", "Franco","1", "test@invalido.it");
    }

    // --- Test per AGGIUNGI ---

    @Test
    void testAggiungi_Successo() {
        gestore.aggiungi(utente1);
        
        assertEquals(1, gestore.getLista().size(), "La lista deve contenere un utente.");
        assertTrue(gestore.getLista().contains(utente1), "L'utente aggiunto deve essere presente.");
    }

    @Test
    void testAggiungi_MatricolaGiaEsistente() {
        gestore.aggiungi(utente1); // Aggiunge il primo utente

        // Tenta di aggiungere un nuovo utente con la stessa matricola (M001)
        Utente utenteDuplicato = new Utente("Giovanni", "Verdi", "M001", "g.verdi@unisa.it");

        // Verifica che venga lanciata l'eccezione
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestore.aggiungi(utenteDuplicato);
        }, "Deve lanciare un'eccezione se la matricola è già registrata.");

        assertTrue(exception.getMessage().contains("Matricola già registrata"), 
                   "Il messaggio di errore deve indicare la duplicazione della matricola.");
        assertEquals(1, gestore.getLista().size(), "La dimensione della lista non deve cambiare.");
    }
    
    @Test
    void testAggiungi_UtenteNonValido() {
        // Il metodo aggiungi chiama utente.verificamailmatr()
        
        // Verifica che l'eccezione venga lanciata dal metodo interno di Utente
        // NOTA: Devi assicurarti che Utente.verificamailmatr() lanci un'eccezione
        // se l'utenteInvalido non è valido (es. IllegalArgumentException o IllegalStateException)
        
        assertThrows(RuntimeException.class, () -> { 
            gestore.aggiungi(utenteInvalido);
        }, "Deve lanciare un'eccezione se l'utente fallisce la validazione interna.");

        assertEquals(0, gestore.getLista().size(), "L'utente non valido non deve essere aggiunto.");
    }

    // --- Test per RIMUOVI ---

    @Test
    void testRimuovi_Successo() {
        gestore.aggiungi(utente1);
        gestore.aggiungi(utente2);

        assertTrue(gestore.rimuovi(utente1), "La rimozione del primo utente deve riuscire.");
        assertEquals(1, gestore.getLista().size(), "La lista deve avere una dimensione ridotta.");
        assertFalse(gestore.getLista().contains(utente1), "L'utente rimosso non deve essere in lista.");
    }

    @Test
    void testRimuovi_UtenteNonEsistente() {
        gestore.aggiungi(utente1);

        assertFalse(gestore.rimuovi(utente2), "La rimozione di un utente non presente deve fallire.");
        assertEquals(1, gestore.getLista().size(), "La dimensione della lista non deve cambiare.");
    }

    // --- Test per CERCA (per Matricola) ---

    @Test
    void testCerca_Successo() {
        gestore.aggiungi(utente1);
        gestore.aggiungi(utente2);

        // Oggetto Utente con solo matricola usata per la ricerca
        Utente ricerca = new Utente( null, null,utente1.getMatricola(), null); 
        
        Utente trovato = gestore.cerca(ricerca);
        
        assertNotNull(trovato, "L'utente deve essere trovato.");
        assertEquals(utente1.getMatricola(), trovato.getMatricola(), "La matricola trovata deve corrispondere.");
        assertEquals(utente1, trovato, "L'oggetto Utente trovato deve essere lo stesso.");
    }

    @Test
    void testCerca_NonTrovato() {
        gestore.aggiungi(utente1);
        
        // Oggetto Utente con matricola inesistente
        Utente ricerca = new Utente(null, null,"M999", null); 
        
        assertNull(gestore.cerca(ricerca), "La ricerca deve restituire null se l'utente non esiste.");
    }
    
    @Test
    void testCerca_OggettoNullo() {
        assertNull(gestore.cerca(null), "La ricerca con oggetto nullo deve restituire null.");
    }
    
    // --- Test per RICERCA ANAGRAFICA (Ricerca Grafica) ---

    @Test
    void testRicercaAnagrafica_TestoVuotoO_Nullo() {
        gestore.aggiungi(utente1);
        gestore.aggiungi(utente2);
        
        // Testo nullo
        assertEquals(2, gestore.ricercaAnagrafica(null).size(), "Con testo nullo deve tornare tutta la lista.");
        // Testo vuoto
        assertEquals(2, gestore.ricercaAnagrafica("").size(), "Con testo vuoto deve tornare tutta la lista.");
    }

    @Test
    void testRicercaAnagrafica_RicercaPerCognome_CaseInsensitive() {
        gestore.aggiungi(utente1); // Rossi
        
        // Ricerca in minuscolo
        ObservableList<Utente> risultati = gestore.ricercaAnagrafica("rossi");
        
        assertEquals(1, risultati.size(), "Deve trovare un risultato per 'rossi'.");
        assertTrue(risultati.contains(utente1), "Deve trovare l'utente Rossi.");
    }
    
    @Test
    void testRicercaAnagrafica_RicercaPerMatricolaParziale() {
        gestore.aggiungi(utente1); // M001
        gestore.aggiungi(utente2); // M002
        
        // Ricerca per "00"
        ObservableList<Utente> risultati = gestore.ricercaAnagrafica("00");
        
        assertEquals(2, risultati.size(), "Deve trovare entrambi gli utenti per '00'.");
    }
    
    @Test
    void testRicercaAnagrafica_NessunaCorrispondenza() {
        gestore.aggiungi(utente1);
        
        ObservableList<Utente> risultati = gestore.ricercaAnagrafica("XYZ");
        
        assertTrue(risultati.isEmpty(), "Non deve trovare risultati per 'XYZ'.");
    }
    
    // --- Test per GETLISTA ---

    @Test
    void testGetLista() {
        gestore.aggiungi(utente1);
        assertEquals(1, gestore.getLista().size(), "GetLista deve restituire la lista corrente.");
    }
}
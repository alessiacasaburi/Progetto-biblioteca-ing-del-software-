package gestori;

import entita.Libro;
import entita.Prestito;
import entita.Utente;
import java.time.LocalDate;
import java.util.Arrays; // Fondamentale per le liste di autori
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Antonietta Franzese
 */
public class GestorePrestitiTest {

    private GestorePrestiti gestore;
    private Utente utenteValido;
    private Libro libroDisponibile;
    private Libro libroNonDisponibile;
    
    // Dati validi standard
    private static final String MATRICOLA_A = "1111111111"; 
    private static final String EMAIL_VALIDA = "test@unisa.it";

    @BeforeEach
    public void setUp() {
        // Inizializza un Gestore pulito prima di ogni test
        gestore = new GestorePrestiti(true);

        // Oggetti di Entità per i test
        try {
            utenteValido = new Utente(MATRICOLA_A, "Mario", "Rossi", EMAIL_VALIDA);
        } catch (Exception e) { fail("Setup fallito su Utente: " + e.getMessage()); }

        // Libro con 3 copie disponibile
        libroDisponibile = new Libro("Titolo A", Arrays.asList("Autore Uno"), 2020, "ISBN-A", 3);
        
        // Libro con 0 copie disponibile
        libroNonDisponibile = new Libro("Titolo B", Arrays.asList("Autore Due"), 2020, "ISBN-B", 0); 
    }

    @Test
    public void testAggiungiPrestito_Successo() {
        Prestito prestito = new Prestito(utenteValido, libroDisponibile, LocalDate.now(), LocalDate.now().plusDays(30));
        
        // Esecuzione
        gestore.aggiungi(prestito);

        // Controllo stato finale
        assertEquals(1, gestore.getLista().size(), "Il gestore deve contenere il nuovo prestito.");
        assertEquals(2, libroDisponibile.getCopieDisponibili(), "La disponibilità del libro deve decrementare a 2."); 
        assertEquals(1, utenteValido.getPrestitiAttivi().size(), "I prestiti attivi dell'utente devono incrementare a 1.");
    }

    @Test
    public void testAggiungiPrestito_LibroNonDisponibile_Fallimento() {
        Prestito prestito = new Prestito(utenteValido, libroNonDisponibile, LocalDate.now(), LocalDate.now().plusDays(30));
        
        assertThrows(RuntimeException.class, () -> {
            gestore.aggiungi(prestito);
        }, "Deve fallire se il libro non ha copie sufficienti (<= 2).");
        
        assertEquals(0, gestore.getLista().size(), "La lista prestiti non deve essere modificata.");
    }

    @Test
    public void testAggiungiPrestito_UtenteLimiteRaggiunto_Fallimento() {
        // CORREZIONE QUI: Uso Arrays.asList e parametri corretti per i libri temporanei
        utenteValido.aggiungiPrestito(new Prestito(utenteValido, new Libro("L1", Arrays.asList("A1"), 2020, "ISBN1", 3), LocalDate.now().minusDays(3), LocalDate.now().plusDays(3)));
        utenteValido.aggiungiPrestito(new Prestito(utenteValido, new Libro("L2", Arrays.asList("A2"), 2020, "ISBN2", 3), LocalDate.now().minusDays(2), LocalDate.now().plusDays(3)));
        utenteValido.aggiungiPrestito(new Prestito(utenteValido, new Libro("L3", Arrays.asList("A3"), 2020, "ISBN3", 3), LocalDate.now().minusDays(1), LocalDate.now().plusDays(3)));
        
        // Prestito 4 (fallimento atteso)
        // CORREZIONE QUI: Arrays.asList invece di null
        Libro libroOK = new Libro("Titolo C", Arrays.asList("Autore C"), 2020, "ISBN-C", 3);
        Prestito prestitoQuarto = new Prestito(utenteValido, libroOK, LocalDate.now(), LocalDate.now().plusDays(30));

        assertThrows(Exception.class, () -> { 
            gestore.aggiungi(prestitoQuarto);
        }, "Deve fallire se l'utente supera il limite di 3 prestiti.");
        
        assertEquals(3, libroOK.getCopieDisponibili(), "La disponibilità del libro non deve decrementare.");
        assertEquals(0, gestore.getLista().size(), "La lista prestiti del gestore non deve crescere.");
    }
    
    @Test
    public void testTerminaPrestito_Successo() {
        Prestito prestito = new Prestito(utenteValido, libroDisponibile, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        
        libroDisponibile.decrementaDisponibilita(); 
        assertEquals(2, libroDisponibile.getCopieDisponibili(), "Pre-condizione: Disponibilità 2.");
        assertTrue(prestito.isPrestitoAttivo(), "Pre-condizione: Prestito attivo.");

        gestore.terminaPrestito(prestito);

        assertFalse(prestito.isPrestitoAttivo(), "Il prestito deve risultare concluso.");
        assertEquals(3, libroDisponibile.getCopieDisponibili(), "La disponibilità del libro deve incrementare a 3."); 
        assertNotNull(prestito.getDataRestituzione(), "La data di restituzione deve essere impostata.");
    }
    
    @Test
    public void testTerminaPrestito_GiaConcluso() {
        Prestito prestito = new Prestito(utenteValido, libroDisponibile, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        libroDisponibile.setCopieDisponibili(2);
        
        prestito.setPrestitoConcluso();
        libroDisponibile.incrementaDisponibilita();
        int disponibilitaDopoPrimaConclusione = libroDisponibile.getCopieDisponibili();
        
        gestore.terminaPrestito(prestito); 

        assertEquals(disponibilitaDopoPrimaConclusione, libroDisponibile.getCopieDisponibili(), 
                     "La disponibilità non deve incrementare una seconda volta."); 
        assertFalse(prestito.isPrestitoAttivo(), "Lo stato deve restare concluso.");
    }

    
    @Test
    public void testGetPrestitiAttivi_ListaVuota() {
        assertTrue(gestore.getLista().isEmpty(), "Pre-condizione: la lista totale deve essere vuota.");
        assertTrue(gestore.getPrestitiAttivi().isEmpty(), "La lista dei prestiti attivi deve essere vuota.");
    }

    @Test
    public void testGetPrestitiAttivi() {
        // CORREZIONE QUI: Arrays.asList invece di stringa semplice
        Libro l1 = new Libro("L1", Arrays.asList("T1"), 2020, "ISBN-1", 3);
        Prestito p1 = new Prestito(utenteValido, l1, LocalDate.now(), LocalDate.now().plusDays(30));
        gestore.aggiungi(p1);
        
        // CORREZIONE QUI: Arrays.asList invece di stringa semplice
        Libro l2 = new Libro("L2", Arrays.asList("T2"), 2020, "ISBN-2", 3);
        Prestito p2 = new Prestito(utenteValido, l2, LocalDate.now().minusDays(10), LocalDate.now().plusDays(30));
        p2.setPrestitoConcluso(); 
        l2.incrementaDisponibilita();
        
        gestore.getLista().add(p2); 
        
        assertEquals(2, gestore.getLista().size(), "La lista totale deve avere 2 prestiti.");
        assertEquals(1, gestore.getPrestitiAttivi().size(), "La lista attiva deve avere 1 prestito.");
        assertTrue(gestore.getPrestitiAttivi().contains(p1), "Il prestito attivo deve essere p1.");
    }

    @Test
    public void testCercaPrestito_Successo() {
        LocalDate data = LocalDate.now();
        Prestito prestitoAtteso = new Prestito(utenteValido, libroDisponibile, data, data.plusDays(30));
        
        gestore.aggiungi(prestitoAtteso);
        
        Prestito risultato = gestore.cerca(prestitoAtteso);
        
        assertNotNull(risultato, "Il prestito deve essere trovato.");
        assertSame(prestitoAtteso, risultato, "Gli oggetti Prestito devono essere gli stessi in memoria.");
    }
}
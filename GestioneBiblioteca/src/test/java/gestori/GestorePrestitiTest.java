package gestori;

import entita.Libro;
import entita.Prestito;
import entita.Utente;
import java.time.LocalDate;
import java.util.Arrays;
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

        // Libro con 3 copie disponibile per soddisfare la regola aziendale (copie > 2 per il prestito)
        libroDisponibile = new Libro("ISBN-A", "Titolo A", Arrays.asList("Autore Uno"), 2020, 3);
        
        // Libro con 0 copie disponibile (per test di fallimento)
        libroNonDisponibile = new Libro("ISBN-B", "Titolo B", Arrays.asList("Autore Due"), 2020, 0); 
    }


    @Test
    public void testAggiungiPrestito_Successo() {
        Prestito prestito = new Prestito(utenteValido, libroDisponibile, LocalDate.now(), LocalDate.now().plusDays(30));
        
        // Esecuzione
        gestore.aggiungi(prestito);

        // Controllo stato finale
        assertEquals(1, gestore.getLista().size(), "Il gestore deve contenere il nuovo prestito.");
        // Verifica che le copie siano scese da 3 a 2
        assertEquals(2, libroDisponibile.getCopieDisponibili(), "La disponibilità del libro deve decrementare a 2."); 
        assertEquals(1, utenteValido.getPrestitiAttivi().size(), "I prestiti attivi dell'utente devono incrementare a 1.");
    }

    @Test
    public void testAggiungiPrestito_LibroNonDisponibile_Fallimento() {
        // Questo test dovrebbe fallire perché 0 copie non soddisfa la condizione (0 <= 2 è vero, quindi lancia eccezione)
        Prestito prestito = new Prestito(utenteValido, libroNonDisponibile, LocalDate.now(), LocalDate.now().plusDays(30));
        
        assertThrows(RuntimeException.class, () -> {
            gestore.aggiungi(prestito);
        }, "Deve fallire se il libro non ha copie sufficienti (<= 2).");
        
        // Verifica che la lista non sia cresciuta
        assertEquals(0, gestore.getLista().size(), "La lista prestiti non deve essere modificata.");
    }

    @Test
    public void testAggiungiPrestito_UtenteLimiteRaggiunto_Fallimento() {
        // Simula 3 prestiti attivi per l'utente (il limite è 3)
        // Nota: Qui usiamo nuovi libri con 3 copie per garantire che la validazione di Libro non fallisca in questo setup.
        utenteValido.aggiungiPrestito(new Prestito(utenteValido, new Libro("1", "T1", null, 2020, 3), LocalDate.now().minusDays(3), LocalDate.now().plusDays(3)));
        utenteValido.aggiungiPrestito(new Prestito(utenteValido, new Libro("2", "T2", null, 2020, 3), LocalDate.now().minusDays(2), LocalDate.now().plusDays(3)));
        utenteValido.aggiungiPrestito(new Prestito(utenteValido, new Libro("3", "T3", null, 2020, 3), LocalDate.now().minusDays(1), LocalDate.now().plusDays(3)));
        
        // Prestito 4 (fallimento atteso)
        Libro libroOK = new Libro("ISBN-C", "Titolo C", null, 2020, 3);
        Prestito prestitoQuarto = new Prestito(utenteValido, libroOK, LocalDate.now(), LocalDate.now().plusDays(30));

        // Ci aspettiamo l'eccezione lanciata da Utente
        assertThrows(Exception.class, () -> { 
            gestore.aggiungi(prestitoQuarto);
        }, "Deve fallire se l'utente supera il limite di 3 prestiti.");
        
        // Verifica che la disponibilità del libro non sia stata toccata
        assertEquals(3, libroOK.getCopieDisponibili(), "La disponibilità del libro non deve decrementare.");
        assertEquals(0, gestore.getLista().size(), "La lista prestiti del gestore non deve crescere.");
    }
    
    @Test
    public void testTerminaPrestito_Successo() {
        // Prepara un prestito fittizio
        Prestito prestito = new Prestito(utenteValido, libroDisponibile, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        
        // Simula che il libro sia stato precedentemente prestato (copie da 3 a 2)
        libroDisponibile.decrementaDisponibilita(); 
        assertEquals(2, libroDisponibile.getCopieDisponibili(), "Pre-condizione: Disponibilità 2.");
        assertTrue(prestito.isPrestitoAttivo(), "Pre-condizione: Prestito attivo.");

        // Esecuzione
        gestore.terminaPrestito(prestito);

        // Controllo stato finale
        assertFalse(prestito.isPrestitoAttivo(), "Il prestito deve risultare concluso.");
        // Verifica che le copie siano tornate da 2 a 3
        assertEquals(3, libroDisponibile.getCopieDisponibili(), "La disponibilità del libro deve incrementare a 3."); 
        assertNotNull(prestito.getDataRestituzione(), "La data di restituzione deve essere impostata.");
    }
    
    @Test
    public void testTerminaPrestito_GiaConcluso() {
        // 1. Prepara un prestito fittizio
        Prestito prestito = new Prestito(utenteValido, libroDisponibile, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        libroDisponibile.setCopieDisponibili(2); // Simula che sia stato prestato (da 3 a 2)
        
        // 2. Concludilo una volta (copie passano da 2 a 3)
        prestito.setPrestitoConcluso();
        libroDisponibile.incrementaDisponibilita();
        int disponibilitaDopoPrimaConclusione = libroDisponibile.getCopieDisponibili(); // Dovrebbe essere 3
        
        // 3. Tenta di terminarlo di nuovo
        gestore.terminaPrestito(prestito); 

        // Controlla che la disponibilità sia ancora 3
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
        // Prestito 1 (Attivo) - richiede 3 copie iniziali
        Libro l1 = new Libro("L1", "T1", null, 2020, 3);
        Prestito p1 = new Prestito(utenteValido, l1, LocalDate.now(), LocalDate.now().plusDays(30));
        gestore.aggiungi(p1);
        
        // Prestito 2 (Concluso) 
        Libro l2 = new Libro("L2", "T2", null, 2020, 3);
        Prestito p2 = new Prestito(utenteValido, l2, LocalDate.now().minusDays(10), LocalDate.now().plusDays(30));
        p2.setPrestitoConcluso(); 
        l2.incrementaDisponibilita(); // Simula la restituzione
        
        // Aggiungiamo il prestito concluso forzando l'aggiunta alla lista del gestore
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
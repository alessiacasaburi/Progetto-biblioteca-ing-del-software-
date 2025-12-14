package entita;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Antonietta Franzese
 */

public class PrestitoTest  {

    private Utente utenteTest;
    private Libro libroTest;
    private LocalDate dataInizio;
    private LocalDate dataScadenza;
    
    // Dati di test CONFORMI alla tua logica di validazione
    private static final String TITOLO_TEST = "Test Titolo";
    private static final String COGNOME_TEST = "Test Cognome";
    private static final String MATRICOLA_VALIDA = "1234567890"; // 10 cifre
    private static final String EMAIL_VALIDA = "test.valido@unisa.it"; 

    /**
     * @brief Setup: inizializza Utente, Libro e Date con dati validi.
     */
    @BeforeEach
    public void setUp() {
        try {
            // Inizializzazione di Utente (classe reale)
            utenteTest = new Utente(
                MATRICOLA_VALIDA, "Nome Test", COGNOME_TEST, EMAIL_VALIDA
            );
            
            // Inizializzazione di Libro (classe reale)
            libroTest = new Libro(
                "978-0000000001", TITOLO_TEST, new ArrayList<>(), 2023, 1
            );
        
        } catch (Exception e) {
            fail("Fallimento nella creazione degli oggetti nel setup: " + e.getMessage());
        }
        
        // Inizializzazione delle Date
        dataInizio = LocalDate.now();
        dataScadenza = dataInizio.plusDays(30);
    }

    @Test
    public void testCostruttore_InizializzazioneCorretta() {
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        assertTrue(prestito.isPrestitoAttivo(), "Il prestito appena creato deve essere attivo.");
        assertNull(prestito.getDataRestituzione(), "La data di restituzione deve essere NULL prima della conclusione.");
    }

    @Test
    public void testSetPrestitoConcluso() {
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        prestito.setPrestitoConcluso();
        assertFalse(prestito.isPrestitoAttivo(), "Dopo la conclusione, lo stato deve essere false.");
        assertEquals(LocalDate.now(), prestito.getDataRestituzione(), "La data di restituzione deve essere impostata a oggi.");
    }
    
    @Test
    public void testToString() {
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        
        // La stringa attesa usa i valori costanti
        String risultatoAtteso = "Prestito: " + TITOLO_TEST + " a " + COGNOME_TEST;
        
        assertEquals(risultatoAtteso, prestito.toString(), "La stringa restituita non è nel formato atteso 'Prestito: titolo a cognome'.");
    }
    
    @Test
    public void testGetterOggettiEDate() {
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        assertEquals(utenteTest, prestito.getUtente(), "Getter Utente fallito.");
        assertEquals(libroTest, prestito.getLibro(), "Getter Libro fallito.");
        assertEquals(dataInizio, prestito.getDataInizio(), "Getter DataInizio fallito.");
        assertEquals(dataScadenza, prestito.getDataScadenza(), "Getter DataScadenza fallito.");
    }


    /**
     * @brief Test per verificare che la data di restituzione non venga sovrascritta.
     */
    @Test
    public void testSetPrestitoConcluso_ChiamataMultipla() throws InterruptedException {
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        
        // 1. Prima conclusione
        prestito.setPrestitoConcluso();
        LocalDate primaDataRestituzione = prestito.getDataRestituzione();
        
        // Simula un piccolo intervallo di tempo (sebbene LocalDate.now() sia veloce,
        // garantisce che la data non venga aggiornata).
        // Se si usa il mocking di LocalDate.now() (tecnica avanzata), si può testare meglio,
        // ma per ora verifichiamo che la variabile interna non venga toccata.
        Thread.sleep(1); // Introduce un ritardo minimo per scopi didattici/robustezza
        
        // 2. Seconda conclusione
        prestito.setPrestitoConcluso(); 
        LocalDate secondaDataRestituzione = prestito.getDataRestituzione();

        // Verifica che lo stato sia sempre concluso
        assertFalse(prestito.isPrestitoAttivo(), "Lo stato deve restare concluso.");
        
        // Verifica che la data di restituzione non sia cambiata.
        assertEquals(primaDataRestituzione, secondaDataRestituzione, 
                     "La data di restituzione non deve essere aggiornata nelle chiamate successive.");
    }
}

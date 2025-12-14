package entita;

import java.time.LocalDate;
import java.util.Arrays; 
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
            // CORREZIONE: Uso Arrays.asList per passare una lista di stringhe corretta
            libroTest = new Libro(
              TITOLO_TEST, Arrays.asList("Autore Test"), 2023, "978-0000000001", 1
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
        // CORREZIONE: Passati solo 4 parametri (senza dataRestituzione che è null all'inizio)
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        
        assertTrue(prestito.isPrestitoAttivo(), "Il prestito appena creato deve essere attivo.");
        
        // CORREZIONE: getDataRestituzione con la D maiuscola
        assertNull(prestito.getDataRestituzione(), "La data di restituzione deve essere NULL prima della conclusione.");
    }

    @Test
    public void testSetPrestitoConcluso() {
        // CORREZIONE: Passati solo 4 parametri
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        
        prestito.setPrestitoConcluso();
        
        assertFalse(prestito.isPrestitoAttivo(), "Dopo la conclusione, lo stato deve essere false.");
        
        // CORREZIONE: getDataRestituzione con la D maiuscola
        assertEquals(LocalDate.now(), prestito.getDataRestituzione(), "La data di restituzione deve essere impostata a oggi.");
    }
    
    @Test
    public void testToString() {
        // CORREZIONE: Passati solo 4 parametri
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        
        // La stringa attesa usa i valori costanti
        String risultatoAtteso = "Prestito: " + TITOLO_TEST + " a " + COGNOME_TEST;
        
        assertEquals(risultatoAtteso, prestito.toString(), "La stringa restituita non è nel formato atteso 'Prestito: titolo a cognome'.");
    }
    
    @Test
    public void testGetterOggettiEDate() {
        // CORREZIONE: Passati solo 4 parametri
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
        // CORREZIONE: Passati solo 4 parametri
        Prestito prestito = new Prestito(utenteTest, libroTest, dataInizio, dataScadenza);
        
        // 1. Prima conclusione
        prestito.setPrestitoConcluso();
        // CORREZIONE: getDataRestituzione con la D maiuscola
        LocalDate primaDataRestituzione = prestito.getDataRestituzione();
        
        // Simula un piccolo intervallo di tempo
        Thread.sleep(1); 
        
        // 2. Seconda conclusione
        prestito.setPrestitoConcluso(); 
        // CORREZIONE: getDataRestituzione con la D maiuscola
        LocalDate secondaDataRestituzione = prestito.getDataRestituzione();

        // Verifica che lo stato sia sempre concluso
        assertFalse(prestito.isPrestitoAttivo(), "Lo stato deve restare concluso.");
        
        // Verifica che la data di restituzione non sia cambiata.
        assertEquals(primaDataRestituzione, secondaDataRestituzione, 
                      "La data di restituzione non deve essere aggiornata nelle chiamate successive.");
    }
}
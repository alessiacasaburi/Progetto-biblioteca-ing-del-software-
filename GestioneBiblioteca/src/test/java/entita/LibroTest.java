package entita;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test Suite per la classe Libro.
 * Copre: Logica di business, Valori limite, Eccezioni e Integrità dati.
 */
public class LibroTest {

    private Libro libro;
    private List<String> autoriStd;

    // Eseguito PRIMA di ogni test per garantire un ambiente pulito
    @BeforeEach
    public void setUp() {
        autoriStd = Arrays.asList("J.K. Rowling");
        libro = new Libro("9788869183157", "Harry Potter", autoriStd, 1997, 5);
        System.out.println("\n--- Avvio nuovo test ---");
    }

    @Test
    @DisplayName("Test A: Gestione Disponibilità e Valori Limite (0)")
    public void testLogicaDisponibilita() {
        System.out.println("Verifica flusso disponibilità:");

        // 1. Stato iniziale (5 copie)
        assertTrue(libro.isDisponibile(), "ERR: Con 5 copie deve essere disponibile");
        System.out.println(" - 5 copie: Disponibile [OK]");

        // 2. Decremento
        libro.decrementaDisponibilita();
        assertEquals(4, libro.getCopieDisponibili());
        System.out.println(" - Decremento: copie scese a 4 [OK]");

        // 3. Valore Limite (Boundary Value Analysis): Esattamente 0 copie
        libro.setCopieDisponibili(0);
        assertFalse(libro.isDisponibile(), "ERR: Con 0 copie NON deve essere disponibile");
        System.out.println(" - 0 copie (Limite): Non disponibile [OK]");
        
        // 4. Incremento da 0
        libro.incrementaDisponibilita(); // Ora 1
        assertTrue(libro.isDisponibile());
        System.out.println(" - Incremento: tornato disponibile (1 copia) [OK]");
    }

    @Test
    @DisplayName("Test B: Validazione ISBN (Input Space & Exceptions)")
    public void testValidazioneISBN() {
        System.out.println("Verifica validazione ISBN:");

        // 1. Caso Positivo (Happy Path)
        assertDoesNotThrow(() -> libro.verificaisbn());
        System.out.println(" - ISBN corretto (13 cifre): Accettato [OK]");

        // 2. Caso Input Invalido: Lunghezza errata (Meno di 13)
        libro = new Libro("123", "Test", autoriStd, 2020, 1);
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> libro.verificaisbn());
        System.out.println(" - ISBN corto: " + e1.getMessage() + " [OK]");

        // 3. Caso Input Invalido: Caratteri non numerici
        libro = new Libro("123456789012A", "Test", autoriStd, 2020, 1);
        assertThrows(IllegalArgumentException.class, () -> libro.verificaisbn());
        System.out.println(" - ISBN alfanumerico: Rifiutato [OK]");

        // 4. Caso Speciale: Null
        libro = new Libro(null, "Test", autoriStd, 2020, 1);
        assertThrows(IllegalArgumentException.class, () -> libro.verificaisbn());
        System.out.println(" - ISBN Null: Rifiutato [OK]");
    }

    @Test
    @DisplayName("Test C: Integrità Dati (Getter/Setter/ToString)")
    public void testDatiBase() {
        System.out.println("Verifica consistenza dati:");
        
        // Verifica che i dati immessi siano recuperati correttamente
        assertEquals("Harry Potter", libro.getTitolo());
        assertEquals(1997, libro.getAnnoPubblicazione());
        
        // Modifica e verifica (Setter)
        libro.setTitolo("Nuovo Titolo");
        assertEquals("Nuovo Titolo", libro.getTitolo());
        System.out.println(" - Getter e Setter: Funzionanti [OK]");

        // ToString check
        String output = libro.toString();
        assertTrue(output.contains("Nuovo Titolo") && output.contains("9788869183157"));
        System.out.println(" - ToString format: " + output + " [OK]");
    }
}
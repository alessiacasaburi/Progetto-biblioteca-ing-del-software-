/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 * @brief Classe di test per la classe Libro.
 * @author Annamaria
 */
public class LibroTest {

    private Libro libro;
    private List<String> autori;

    /**
     * @brief Setup iniziale prima di ogni test.
     * Crea un oggetto Libro valido per evitare di ripeterlo in ogni metodo.
     */
    @BeforeEach
    public void setUp() {
        autori = new ArrayList<>(Arrays.asList("J.K. Rowling"));
        // ISBN di 13 cifre valido per il test
        libro = new Libro("Harry Potter", autori, 1997, "9788869183157", 10);
    }

    /**
     * @brief Test del costruttore e dei getter.
     * Verifica che l'oggetto venga inizializzato con i valori corretti.
     */
    @Test
    public void testCostruttoreEGetter() {
        assertEquals("Harry Potter", libro.getTitolo());
        assertEquals(autori, libro.getAutori());
        assertEquals(1997, libro.getAnnoPubblicazione());
        assertEquals("9788869183157", libro.getIsbn());
        assertEquals(10, libro.getCopieDisponibili());
    }

    /**
     * @brief Test per ISBN valido.
     * Verifica che il metodo verificaisbn ritorni true per un codice corretto.
     */
    @Test
    public void testVerificaIsbnValido() {
        assertTrue(libro.verificaisbn());
    }

    /**
     * @brief Test per ISBN non valido (lunghezza errata).
     * Si aspetta che venga lanciata una IllegalArgumentException.
     */
    @Test
    public void testIsbnLunghezzaErrata() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Libro("Test", autori, 2020, "123", 5);
        });
        assertEquals("isbn non valido", exception.getMessage());
    }

    /**
     * @brief Test per ISBN non valido (caratteri non numerici).
     * Si aspetta che venga lanciata una IllegalArgumentException.
     */
    @Test
    public void testIsbnCaratteriNonValidi() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Libro("Test", autori, 2020, "123456789012A", 5);
        });
        assertEquals("isbn non valido", exception.getMessage());
    }
    
    /**
     * @brief Test per ISBN nullo.
     */
    @Test
    public void testIsbnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro("Test", autori, 2020, null, 5);
        });
    }

    /**
     * @brief Test della disponibilità (isDisponibile).
     */
    @Test
    public void testIsDisponibile() {
        libro.setCopieDisponibili(1);
        assertTrue(libro.isDisponibile(), "è disponibile con 1 copia");

        libro.setCopieDisponibili(0);
        assertFalse(libro.isDisponibile(), "non è disponibile con 0 copie");
    }

    /**
     * @brief Test decremento disponibilità.
     */
    @Test
    public void testDecrementaDisponibilita() {
        int copieIniziali = libro.getCopieDisponibili(); 
        libro.decrementaDisponibilita();
        assertEquals(copieIniziali - 1, libro.getCopieDisponibili());
    }

    /**
     * @brief Test incremento disponibilità.
     */
    @Test
    public void testIncrementaDisponibilita() {
        int copieIniziali = libro.getCopieDisponibili();
        libro.incrementaDisponibilita();
        assertEquals(copieIniziali + 1, libro.getCopieDisponibili());
    }

    /**
     * @brief Test dei Setter.
     */
    @Test
    public void testSetters() {
        libro.setTitolo("Nuovo Titolo");
        assertEquals("Nuovo Titolo", libro.getTitolo());

        libro.setAnnoPubblicazione(2023);
        assertEquals(2023, libro.getAnnoPubblicazione());
    }

    /**
     * @brief Test del metodo toString.
     */
    @Test
    public void testToString() {
        String expected = "Harry Potter (9788869183157)";
        assertEquals(expected, libro.toString());
    }
}
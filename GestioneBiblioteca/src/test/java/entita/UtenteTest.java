/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entita;

/**
 *
 * @author Alessia
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class UtenteTest {

    private Utente utente;
    private String matricolaValida = "0512105899";
    private String emailValidaStudente = "m.rossi@studenti.unisa.it";

    // Oggetti di supporto per i prestiti
    private Libro libroTest;
    
    // Utilizziamo @BeforeEach in JUnit 5
    @BeforeEach 
    void setUp() {
        // Inizializza un utente pulito prima di ogni test
        utente = new Utente(matricolaValida, "Mario", "Rossi", emailValidaStudente);
        
        // Inizializza gli oggetti dipendenti con i tuoi costruttori reali
        List<String> autori = Arrays.asList("Autore A");
        libroTest = new Libro("Test Book", autori, 2020,"978-1234567890", 5);
    }

    // =================================================================
    // TEST 1: COSTRUTTORE E GETTER/SETTER
    // =================================================================
    
    @Test
    void testCostruttoreEGetters() {
        assertEquals("Mario", utente.getNome());
        assertEquals("Rossi", utente.getCognome());
        assertEquals(matricolaValida, utente.getMatricola());
        assertEquals(emailValidaStudente, utente.getEmail());
        assertTrue(utente.getPrestitiAttivi().isEmpty(), "La lista prestiti dovrebbe essere vuota all'inizio");
    }

    @Test
    void testSetters() {
        utente.setNome("Luigi");
        utente.setCognome("Verdi");
        utente.setEmail("l.verdi@unisa.it");
        
        assertEquals("Luigi", utente.getNome());
        assertEquals("Verdi", utente.getCognome());
        assertEquals("l.verdi@unisa.it", utente.getEmail());
    }
    
    @Test
    void testToString() {
        String expected = "Rossi Mario(0512105899)";
        assertEquals(expected, utente.toString());
    }


    @Test
    void testVerificaMailMatr_MatricolaValida() {
        // Non deve lanciare eccezioni
        assertDoesNotThrow(() -> utente.verificamailmatr());
    }

    @Test
    void testVerificaMailMatr_MatricolaNonValida() {
        Utente uSbagliato = new Utente("123", "A", "B", emailValidaStudente);
        
        // Deve lanciare IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            uSbagliato.verificamailmatr();
        }, "Dovrebbe lanciare eccezione per matricola non valida (meno di 10 cifre)");
    }
    
    @Test
    void testVerificaMailMatr_EmailValidaStudenti() {
        utente.setEmail("valid.student@studenti.unisa.it");
        assertDoesNotThrow(() -> utente.verificamailmatr());
    }
    
    @Test
    void testVerificaMailMatr_EmailNonValida() {
        utente.setEmail("hacker@gmail.com"); 
        
        // Deve lanciare IllegalArgumentException (conferma il bug logico)
        assertThrows(IllegalArgumentException.class, () -> {
            utente.verificamailmatr();
        }, "Dovrebbe lanciare eccezione per email non UNISA");
    }

    
    @Test
    void testAggiungiPrestitoESoloOggettiUnici() {
        Prestito p1 = new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2));
        
        // 1. Aggiungo il primo prestito
        utente.aggiungiPrestito(p1);
        assertEquals(1, utente.getPrestitiAttivi().size());
        
        // 2. Aggiungo lo stesso oggetto una seconda volta
        utente.aggiungiPrestito(p1);
        // La dimensione deve rimanere 1
        assertEquals(1, utente.getPrestitiAttivi().size(), "Lo stesso prestito non deve essere aggiunto due volte");
    }

    @Test
    void testVerificaPrestitiAttivi_LimiteNonRaggiunto() {
        // Aggiungo 2 prestiti attivi (vengono creati come attivi)
        utente.aggiungiPrestito(new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2)));
        utente.aggiungiPrestito(new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2)));
        
        // Controllo: (2 < 3). Non deve lanciare eccezione.
        assertDoesNotThrow(() -> utente.verificaPrestitiAttivi());
    }

    @Test
    void testVerificaPrestitiAttivi_LimiteRaggiunto() {
        // Aggiungo 3 prestiti attivi (limite raggiunto)
        utente.aggiungiPrestito(new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2)));
        utente.aggiungiPrestito(new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2)));
        utente.aggiungiPrestito(new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2)));
        
        // La chiamata a verificaPrestitiAttivi() deve lanciare Exception
        Exception thrown = assertThrows(Exception.class, () -> {
            utente.verificaPrestitiAttivi();
        }, "Dovrebbe lanciare Exception se ci sono già 3 prestiti attivi.");
        
        // Verifico anche il messaggio dell'eccezione
        assertEquals("L'utente non può richiedere il prestito: limite raggiunto.", thrown.getMessage());
    }
    
    @Test
    void testVerificaPrestitiAttivi_PrestitoRestituitoNonConta() {
        // Prestito 1 e 2: Attivi
        Prestito p1 = new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2));
        Prestito p2 = new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2));

        // Prestito 3: Concluso/Restituito (non conta per il limite)
        Prestito p3 = new Prestito(utente, libroTest, LocalDate.now(), LocalDate.now().plusWeeks(2));
        p3.setPrestitoConcluso(); 
        
        // Aggiungo tutti i prestiti alla lista (3 oggetti totali, 2 attivi)
        utente.aggiungiPrestito(p1);
        utente.aggiungiPrestito(p2);
        utente.aggiungiPrestito(p3);
        
        // Se il contatore attivi è 2 (P1, P2), non deve lanciare eccezione
        assertDoesNotThrow(() -> utente.verificaPrestitiAttivi(), "La verifica deve ignorare i prestiti conclusi.");
    }
}

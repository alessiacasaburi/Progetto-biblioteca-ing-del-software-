package gestori;
/**
 * @brief Classe responsabile dei test della gestione dell'inventario dei libri.
 * * @author Alessandro
 */

import entita.Libro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GestoreLibriTest {

    private GestoreLibri gestoreLibri;
    private String fileTest = "archivio_libri.dat"; 

   
    @BeforeEach
    public void setup() {
        // Assicuriamoci di partire puliti eliminando eventuali file vecchi prima di creare il gestore
        cleanup(); 
        gestoreLibri = new GestoreLibri();
    }

    
    @AfterEach
    public void cleanup() {
        // Rimuove il file di salvataggio dopo ogni test per non sporcare i test successivi
        File file = new File(fileTest);
        if (file.exists()) {
            file.delete();
        }
    }

    // Test per l'aggiunta e la persistenza su file 
    @Test
    public void testAggiungiLibroEScrittura() {
        // Preparare alcuni libri di test
        List<String> autori1 = Arrays.asList("Autore Uno", "Autore Due");
        Libro libro1 = new Libro( "Libro Uno", autori1, 2024,"1111111111111", 5);
        
        List<String> autori2 = Arrays.asList("Autore Tre");
        Libro libro2 = new Libro( "Libro Due", autori2, 2023,"2222222222222", 2);

        
        gestoreLibri.aggiungi(libro1);
        gestoreLibri.aggiungi(libro2);

        // Verifica che il file sia stato creato fisicamente
        File file = new File(fileTest);
        assertTrue(file.exists());

        // Ora prova a simulare un riavvio dell'applicazione
        // Creiamo una NUOVA istanza del gestore che dovrebbe leggere dal file appena creato
        GestoreLibri gestoreImportato = new GestoreLibri();
        ObservableList<Libro> listaImportata = gestoreImportato.getLista();

        // Verifica che i libri siano stati letti correttamente dal file
        assertNotNull(gestoreImportato);
        assertFalse(listaImportata.isEmpty());
        assertEquals(2, listaImportata.size());

        // Verifichiamo il contenuto del primo libro importato
        Libro libroImportato1 = gestoreImportato.cerca(libro1);
        assertNotNull(libroImportato1);
        assertEquals("Libro Uno", libroImportato1.getTitolo());
        assertEquals("1111111111111", libroImportato1.getIsbn());
        assertEquals(2024, libroImportato1.getAnnoPubblicazione());
        
    }

    // Test per la rimozione e aggiornamento del file
    @Test
    public void testRimuoviLibro() {
        // Setup dati
        Libro libro = new Libro( "Libro Da Rimuovere", Arrays.asList("Autore"), 2020,"9999999999999", 1);
        gestoreLibri.aggiungi(libro);
        
        
        assertEquals(1, gestoreLibri.getLista().size());
        
        
        boolean esito = gestoreLibri.rimuovi(libro);
        assertTrue(esito);
        
        
        assertTrue(gestoreLibri.getLista().isEmpty());
        
        
        GestoreLibri gestoreReload = new GestoreLibri();
        assertTrue(gestoreReload.getLista().isEmpty()); 
    }

    // Test per la gestione dei duplicati (Simile ai controlli di integrità)
    @Test
    public void testAggiungiDuplicato() {
        Libro libro = new Libro( "Libro Originale", Arrays.asList("A"), 2022,"4444444444444", 1);
        gestoreLibri.aggiungi(libro);

        Libro duplicato = new Libro( "Libro Copia", Arrays.asList("B"), 2022,"4444444444444", 1);

        // Verifica che lanci l'eccezione come previsto
        assertThrows(IllegalArgumentException.class, () -> {
            gestoreLibri.aggiungi(duplicato);
        });
        
        // Verifica che la lista non sia aumentata
        assertEquals(1, gestoreLibri.getLista().size());
    }

    // Test per la ricerca testuale (Simile a testLeggiFileCsv con filtri)
    @Test
    public void testRicercaTestuale() {
        // Popoliamo la rubrica/libreria
        gestoreLibri.aggiungi(new Libro( "Java Programming", Arrays.asList("Gos"), 2020,"1001001001000", 10));
        gestoreLibri.aggiungi(new Libro( "Python Basics", Arrays.asList("Rossum"), 2019,"2002002002000", 5));
        gestoreLibri.aggiungi(new Libro( "Advanced Java", Arrays.asList("prova"), 2021,"3003003003000", 3));

        // Test ricerca per Titolo
        ObservableList<Libro> risultatiJava = gestoreLibri.ricercaTestuale("Java");
        assertEquals(2, risultatiJava.size()); // Dovrebbe trovare "Java Programming" e "Advanced Java"
        
        // Test ricerca per Autore
        ObservableList<Libro> risultatiPython = gestoreLibri.ricercaTestuale("Rossum");
        assertEquals(1, risultatiPython.size());
        assertEquals("Python Basics", risultatiPython.get(0).getTitolo());

        // Test ricerca Fallita
        ObservableList<Libro> risultatiVuoti = gestoreLibri.ricercaTestuale("C++");
        assertTrue(risultatiVuoti.isEmpty());
    }
}
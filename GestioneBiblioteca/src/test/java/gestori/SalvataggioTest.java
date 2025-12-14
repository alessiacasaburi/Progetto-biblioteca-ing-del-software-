/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestori;

/**
 *
 * @author Annamaria
 */

import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Verifica la serializzazione (scrittura) e deserializzazione (lettura) su file.
 */
public class SalvataggioTest {

    // Nome del file temporaneo usato SOLO per i test
    private static final String FILE_TEST = "test_database.dat";

    /**
     * @brief Pulizia post-test (TearDown).
     * Questo metodo viene eseguito automaticamente DOPO ogni singolo test.
     * Serve a garantire che il file di prova venga cancellato, lasciando l'ambiente pulito.
     */
    @AfterEach
    public void tearDown() {
        File f = new File(FILE_TEST);
        if (f.exists()) {
            boolean cancellato = f.delete();
            if (!cancellato) {
                System.err.println("Attenzione: Impossibile cancellare il file di test.");
            }
        }
    }

    @Test
    @DisplayName("Test A: Salvataggio e Caricamento (Flusso Standard)")
    public void testSalvaECarica() {
        System.out.println("Test A: Verifica salvataggio e lettura...");

        // 1. PREPARAZIONE DATI
        // Usiamo una lista normale (ArrayList) come input, come nel tuo programma reale
        List<ElementoTest> listaOriginale = new ArrayList<>();
        listaOriginale.add(new ElementoTest(1, "Mario Rossi"));
        listaOriginale.add(new ElementoTest(2, "Luigi Bianchi"));

        // 2. AZIONE: Salvataggio
        Salvataggio.salvaLista(listaOriginale, FILE_TEST);

        // Verifica che il file sia stato fisicamente creato
        File f = new File(FILE_TEST);
        assertTrue(f.exists(), "Il file .dat dovrebbe esistere dopo il salvataggio");
        assertTrue(f.length() > 0, "Il file non dovrebbe essere vuoto");

        // 3. AZIONE: Caricamento
        ObservableList<ElementoTest> listaCaricata = Salvataggio.caricaLista(FILE_TEST);

        // 4. VERIFICA DATI
        assertNotNull(listaCaricata, "La lista caricata non deve essere null");
        assertEquals(2, listaCaricata.size(), "Il numero di elementi salvati e caricati deve coincidere");
        
        // Verifica profonda dei contenuti
        assertEquals(listaOriginale.get(0).getNome(), listaCaricata.get(0).getNome());
        assertEquals(listaOriginale.get(1).getId(), listaCaricata.get(1).getId());

        System.out.println(" - Salvataggio e lettura: OK");
    }

    @Test
    @DisplayName("Test B: Gestione file inesistente")
    public void testCaricamentoFileMancante() {
        System.out.println("Test B: Verifica comportamento senza file...");

        // Proviamo a caricare un file che NON esiste (grazie al tearDown siamo sicuri che non c'è)
        // La tua classe controlla "if (!file.exists())" e restituisce una lista vuota
        ObservableList<String> risultato = Salvataggio.caricaLista("file_inesistente_xyz.dat");

        // Verifiche
        assertNotNull(risultato, "Il metodo non deve restituire null se il file manca");
        assertTrue(risultato.isEmpty(), "Deve restituire una lista vuota, non generare errori");
        
        System.out.println(" - Gestione file mancante: OK");
    }

    @Test
    @DisplayName("Test C: Sovrascrittura dati")
    public void testSovrascrittura() {
        System.out.println("Test C: Verifica sovrascrittura...");

        // 1. Salviamo una prima lista
        List<String> listaUno = new ArrayList<>();
        listaUno.add("Vecchio Dato");
        Salvataggio.salvaLista(listaUno, FILE_TEST);

        // 2. Salviamo una seconda lista sullo STESSO file
        List<String> listaDue = new ArrayList<>();
        listaDue.add("Nuovo Dato");
        Salvataggio.salvaLista(listaDue, FILE_TEST); // Dovrebbe sovrascrivere

        // 3. Carichiamo
        ObservableList<String> caricata = Salvataggio.caricaLista(FILE_TEST);

        // 4. Verifica: deve esserci solo il dato nuovo
        assertEquals(1, caricata.size());
        assertEquals("Nuovo Dato", caricata.get(0));
        
        System.out.println(" - Sovrascrittura: OK");
    }

    // =========================================================
    // CLASSE FINTA (Mock) PER IL TEST
    // Serve a testare il salvataggio senza dipendere dalla classe Utente o Libro.
    // Se un domani rompi la classe Utente, questo test continuerà a funzionare
    // confermandoti che il problema non è nel Salvataggio.
    // =========================================================
    static class ElementoTest implements Serializable {
        private int id;
        private String nome;

        public ElementoTest(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public int getId() { return id; }
        public String getNome() { return nome; }
    }
}
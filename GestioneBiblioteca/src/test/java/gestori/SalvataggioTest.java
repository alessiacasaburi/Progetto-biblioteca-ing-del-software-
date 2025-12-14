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
     * @brief Pulizia post-test.
     * Questo metodo viene eseguito automaticamente dopo ogni singolo test.
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

       
        List<ElementoTest> listaOriginale = new ArrayList<>();
        listaOriginale.add(new ElementoTest(1, "Mario Rossi"));
        listaOriginale.add(new ElementoTest(2, "Luigi Bianchi"));

        // Salvataggio
        Salvataggio.salvaLista(listaOriginale, FILE_TEST);

        // Verifica che il file sia stato fisicamente creato
        File f = new File(FILE_TEST);
        assertTrue(f.exists(), "Il file .dat dovrebbe esistere dopo il salvataggio");
        assertTrue(f.length() > 0, "Il file non dovrebbe essere vuoto");

        // Caricamento
        ObservableList<ElementoTest> listaCaricata = Salvataggio.caricaLista(FILE_TEST);

        //verifica dati
        assertNotNull(listaCaricata, "La lista caricata non deve essere null");
        assertEquals(2, listaCaricata.size(), "Il numero di elementi salvati e caricati deve coincidere");
        
        
        assertEquals(listaOriginale.get(0).getNome(), listaCaricata.get(0).getNome());
        assertEquals(listaOriginale.get(1).getId(), listaCaricata.get(1).getId());

        System.out.println(" - Salvataggio e lettura: OK");
    }

    @Test
    @DisplayName("Test B: Gestione file inesistente")
    public void testCaricamentoFileMancante() {
        System.out.println("Test B: Verifica comportamento senza file...");

        // Proviamo a caricare un file che NON esiste
        ObservableList<String> risultato = Salvataggio.caricaLista("file_inesistente_xyz.dat");

        assertNotNull(risultato, "Il metodo non deve restituire null se il file manca");
        assertTrue(risultato.isEmpty(), "Deve restituire una lista vuota, non generare errori");
        
        System.out.println(" - Gestione file mancante: OK");
    }

    @Test
    @DisplayName("Test C: Sovrascrittura dati")
    public void testSovrascrittura() {
        System.out.println("Test C: Verifica sovrascrittura...");

        //salvo una prima lista
        List<String> listaUno = new ArrayList<>();
        listaUno.add("Vecchio Dato");
        Salvataggio.salvaLista(listaUno, FILE_TEST);

        // salvo una seconda lista sullo stesso file
        List<String> listaDue = new ArrayList<>();
        listaDue.add("Nuovo Dato");
        Salvataggio.salvaLista(listaDue, FILE_TEST); // Dovrebbe sovrascrivere

        
        ObservableList<String> caricata = Salvataggio.caricaLista(FILE_TEST);

        //verifica che ci sia solo il dato nuovo
        assertEquals(1, caricata.size());
        assertEquals("Nuovo Dato", caricata.get(0));
        
        System.out.println(" - Sovrascrittura: OK");
    }

    //creo una classe statica che serve per verificare il corretto funzionamento di salvataggio 
    //a prescindere dalle classi utente e libro
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
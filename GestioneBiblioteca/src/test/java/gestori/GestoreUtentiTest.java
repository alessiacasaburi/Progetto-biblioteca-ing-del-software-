package gestori;

import entita.Utente;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di unità e di integrazione per la classe GestoreUtenti.
 * Verifica le logiche di business come l'aggiunta, la rimozione e la ricerca
 * degli utenti, integrandosi con la classe Salvataggio per la persistenza.
 */
public class GestoreUtentiTest {

    private GestoreUtenti gestore;
    private Utente u1;
    private Utente u2;
    private Utente u3;

    @BeforeEach
    void setUp() {
        // Inizializza un nuovo gestore per ogni test (isolamento garantito)
        gestore = new GestoreUtenti();
        
        // Pulizia della lista interna all'inizio di ogni test.
        // ESSENZIALE: Evita che i dati scritti da un test interferiscano
        // con il test successivo, forzando Salvataggio a salvare uno stato pulito.
        if (gestore.getLista() != null) {
            gestore.getLista().clear();
            // Forza il salvataggio della lista vuota, se necessario
            Salvataggio.salvaLista(gestore.getLista(), "archivio_libri.dat"); 
        }

        // Creo utenti di test
        u1 = new Utente("1000000001", "Mario", "Rossi", "m.rossi@studenti.unisa.it");
        u2 = new Utente("1000000002", "Luigi", "Verdi", "l.verdi@unisa.it");
        // Utente con matricola duplicata per testare l'univocità
        u3 = new Utente("1000000001", "Paolo", "Bianchi", "p.bianchi@unisa.it"); 
    }


    
    @Test
    void testCostruttore_ListaInizializzataCorrettamente() {
        // Verifica che la lista esista e non sia null (grazie alla logica di Salvataggio)
        assertNotNull(gestore.getLista());
        // Dopo la pulizia in @BeforeEach, la lista deve essere vuota
        assertTrue(gestore.getLista().isEmpty()); 
    }

   

    @Test
    void testAggiungi_Successo() {
        gestore.aggiungi(u1);
        assertEquals(1, gestore.getLista().size());
        assertTrue(gestore.getLista().contains(u1));
    }

    @Test
    void testAggiungi_MatricolaDuplicataLanciaEccezione() {
        // 1. Aggiungo u1
        gestore.aggiungi(u1);
        
        // 2. Verifico che l'aggiunta di u3 (stessa matricola) fallisca con eccezione
        assertThrows(IllegalArgumentException.class, () -> {
            gestore.aggiungi(u3);
        }, "Aggiungere un utente con matricola duplicata deve lanciare eccezione.");
        
        // 3. La lista deve contenere solo un utente
        assertEquals(1, gestore.getLista().size());
    }
    
    @Test
    void testAggiungi_DatiUtenteNonValidiLanciaEccezione() {
        // Utente con matricola troppo corta (fallisce Utente.verificamailmatr)
        Utente uInvalido = new Utente("123", "Nome", "Cog", "a@studenti.unisa.it");
        
        assertThrows(IllegalArgumentException.class, () -> {
            gestore.aggiungi(uInvalido);
        }, "L'aggiunta deve lanciare eccezione se Utente.verificamailmatr fallisce.");
    }
    

    @Test
    void testRimuovi_Successo() {
        gestore.aggiungi(u1);
        
        boolean rimosso = gestore.rimuovi(u1);
        
        assertTrue(rimosso, "L'utente dovrebbe essere rimosso con successo.");
        assertTrue(gestore.getLista().isEmpty());
    }

    @Test
    void testRimuovi_NonEsistente() {
        gestore.aggiungi(u1);
        
        boolean rimosso = gestore.rimuovi(u2); // u2 non è nella lista
        
        assertFalse(rimosso, "Rimuovere un utente non esistente deve restituire false.");
        assertEquals(1, gestore.getLista().size());
    }

    
    @Test
    void testCerca_TrovatoPerMatricola() {
        gestore.aggiungi(u1);
        gestore.aggiungi(u2);
        
        // Creo un utente "placeholder" con solo la matricola per la ricerca
        Utente ricerca = new Utente(u1.getMatricola(), null, null, null);
        Utente trovato = gestore.cerca(ricerca); 
        
        assertNotNull(trovato);
        assertEquals(u1, trovato, "Il metodo cerca deve restituire l'oggetto Utente corretto.");
    }
    
    @Test
    void testCerca_NonTrovato() {
        gestore.aggiungi(u1);
        
        // Cerco u2 (non aggiunto)
        Utente trovato = gestore.cerca(u2); 
        
        assertNull(trovato, "La ricerca di un utente non presente deve restituire null.");
    }
    
    @Test
    void testCerca_OggettoNullo() {
        assertNull(gestore.cerca(null), "La ricerca con oggetto nullo deve restituire null.");
    }
    
    @Test
    void testCerca_MatricolaNulla() {
        Utente uMancante = new Utente(null, "A", "B", "c@unisa.it");
        assertNull(gestore.cerca(uMancante), "La ricerca con matricola nulla deve restituire null.");
    }

    
    @Test
    void testRicercaAnagrafica_StringaVuotaOResetta() {
        gestore.aggiungi(u1);
        gestore.aggiungi(u2);
        
        // Cerca con stringa vuota o null: deve restituire l'intera lista.
        assertEquals(2, gestore.ricercaAnagrafica("").size());
        assertEquals(2, gestore.ricercaAnagrafica(null).size());
    }
    
    @Test
    void testRicercaAnagrafica_RicercaPerCognomeCaseInsensitive() {
        gestore.aggiungi(u1); // Rossi
        gestore.aggiungi(u2); // Verdi
        
        ObservableList<Utente> risultati = gestore.ricercaAnagrafica("ROSSI");
        
        assertEquals(1, risultati.size());
        assertEquals(u1, risultati.get(0));
    }
    
    @Test
    void testRicercaAnagrafica_RicercaPerNomeCaseInsensitive() {
        gestore.aggiungi(u1); // Mario
        gestore.aggiungi(u2); // Luigi
        
        ObservableList<Utente> risultati = gestore.ricercaAnagrafica("mario");
        
        assertEquals(1, risultati.size());
        assertEquals(u1, risultati.get(0));
    }
    
    @Test
    void testRicercaAnagrafica_RicercaPerMatricola() {
        gestore.aggiungi(u1); // 1000000001
        gestore.aggiungi(u2); // 1000000002
        
        ObservableList<Utente> risultati = gestore.ricercaAnagrafica("002");
        
        assertEquals(1, risultati.size());
        assertEquals(u2, risultati.get(0));
    }
}
    
    
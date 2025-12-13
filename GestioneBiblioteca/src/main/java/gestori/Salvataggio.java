/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestori;

import java.io.*;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Salvataggio {

    /**
     * Salva una qualsiasi lista di oggetti su un file.
     * @param lista La lista da salvare (Libri, Utenti o Prestiti).
     * @param nomeFile Il percorso/nome del file (es. "libri.dat").
     */
    public static <T> void salvaLista(List<T> lista, String nomeFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeFile))) {
            oos.writeObject(new java.util.ArrayList<>(lista));
            System.out.println("Dati salvati correttamente in " + nomeFile);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio su " + nomeFile + ": " + e.getMessage());
        }
    }

    /**
     * Carica una lista da file.
     * @param nomeFile Il nome del file da leggere.
     * @return Una ObservableList pronta per essere usata dai Gestori.
     */
    @SuppressWarnings("unchecked")
    public static <T> ObservableList<T> caricaLista(String nomeFile) {
        File file = new File(nomeFile);
        if (!file.exists()) {
            // Se il file non esiste (prima volta che avvii l'app), ritorna lista vuota
            return FXCollections.observableArrayList();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeFile))) {
            List<T> listaCaricata = (List<T>) ois.readObject();
            return FXCollections.observableArrayList(listaCaricata);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore nel caricamento da " + nomeFile + ": " + e.getMessage());
            return FXCollections.observableArrayList(); 
        }
    }
}
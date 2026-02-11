package org.example;

import org.example.exceptions.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Magazzino {
    private static Magazzino instance;
    private Map<Integer, Farmaco> farmaci;
    private int nextId = 1;

    private Magazzino() {
        farmaci = new HashMap<>();
        inserimentoFarmaciAutomatico();
    }

    public static Magazzino getInstance() {
        if (instance == null) {
            instance = new Magazzino();
        }
        return instance;
    }

    public Map<Integer, Farmaco> getFarmaci() {
        return farmaci;
    }

    public boolean ricercaFarmaci(String nomeFarmaco) {
        boolean trovato = false;
        for (Farmaco f : farmaci.values()) {
            if (f.getNome().equalsIgnoreCase(nomeFarmaco) && f.isArrivato()) {
                System.out.println(f.getId() + " " + f.getNome() + " " + f.getQuantita() + " " + f.getScadenza());
                trovato = true;
            }
        }
        return trovato;
    }

    public Farmaco selezionaFarmacoById(int id) {
        Farmaco farmaco = getFarmacoByid(id);
        if (farmaco != null) {
            int q = farmaco.getQuantita();
            farmaco.setQuantita(--q);
        }
        return farmaco;
    }

    public Farmaco getFarmacoByid(int id) {
        if (id <= 0 || !farmaci.containsKey(id)) {
            throw new EntitaNonTrovataException("Farmaco non trovato per ID: " + id);
        }
        Farmaco f = farmaci.get(id);
        if (f.getScadenza().isBefore(LocalDate.now())) {
            throw new FarmacoScadutoException("Il farmaco " + f.getNome() + " è scaduto il " + f.getScadenza());
        }
        if (f.getQuantita() == 0) {
            throw new FarmacoNonDisponibileException("Scorte esaurite per il farmaco " + f.getNome());
        }
        return f;
    }

    private void inserimentoFarmaciAutomatico() {
        // 3 with same name, different expiry/quantity
        farmaci.put(nextId,
                new Farmaco("Tachipirina", nextId++, 50, LocalDate.of(2025, 12, 31), true, "fornitore@example.com"));
        farmaci.put(nextId,
                new Farmaco("Tachipirina", nextId++, 30, LocalDate.of(2026, 6, 15), true, "fornitore@example.com"));
        farmaci.put(nextId,
                new Farmaco("Tachipirina", nextId++, 20, LocalDate.of(2024, 10, 10), true, "fornitore@example.com"));

        // 4 others
        farmaci.put(nextId, new Farmaco("Oki", nextId++, 100, LocalDate.of(2027, 1, 1), true, "fornitore@example.com"));
        farmaci.put(nextId,
                new Farmaco("Brufen", nextId++, 40, LocalDate.of(2025, 5, 20), true, "fornitore@example.com"));
        farmaci.put(nextId,
                new Farmaco("Bentelan", nextId++, 10, LocalDate.of(2024, 12, 1), true, "fornitore@example.com"));
        farmaci.put(nextId,
                new Farmaco("Aspirina", nextId++, 60, LocalDate.of(2026, 3, 30), true, "fornitore@example.com"));
        farmaci.put(nextId,
                new Farmaco("Finito", nextId++, 1, LocalDate.of(2026, 3, 30), true, "fornitore@example.com"));
        farmaci.put(nextId,
                new Farmaco("Aspirina", nextId++, 60, LocalDate.of(2026, 3, 3), true, "fornitore@example.com"));
    }

    public void inserisciOrdine(String nome, int quantita) {
        if (quantita <= 0) {
            System.out.println("Errore: Quantità deve essere positiva.");
            return;
        }
        // Default scadenza 1 anno, fornitore default
        Farmaco nuovo = new Farmaco(nome, nextId, quantita, LocalDate.now().plusYears(2), false,
                "fornitore@example.com");
        farmaci.put(nextId, nuovo);
        System.out.println("Ordine inserito: " + nome + " (ID: " + nextId + ")");
        nextId++;
    }

    public void controllaScorte() {
        System.out.println("\n--- Controllo Scorte ---");
        boolean alert = false;
        for (Farmaco f : farmaci.values()) {
            if (f.getScadenza().isBefore(LocalDate.now())) {
                System.out
                        .println("SCADUTO: " + f.getNome() + " (ID: " + f.getId() + ") - Scadenza: " + f.getScadenza());
                alert = true;
            } else {
                if (f.getScadenza().isBefore(LocalDate.now().plusMonths(1))) {
                    System.out
                            .println("IN SCADENZA " + f.getNome() + " (ID: " + f.getId() + ") - Scadenza: "
                                    + f.getScadenza());
                    alert = true;
                }
            }

            if (f.getQuantita() < 5) {
                System.out.println(
                        "IN ESAURIMENTO: " + f.getNome() + " (ID: " + f.getId() + ") - Quantità: " + f.getQuantita());
                alert = true;
            }

            if (!f.isArrivato()) {
                System.out.println(
                        "IN ARRIVO: " + f.getNome() + " (ID: " + f.getId() + ") - Quantità: " + f.getQuantita());
                alert = true;
            }

        }
        if (!alert) {
            System.out.println("Tutti i farmaci sono in regola.");
        }
    }
}

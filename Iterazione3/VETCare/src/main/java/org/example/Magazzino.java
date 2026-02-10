package org.example;

import org.example.exceptions.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Magazzino {
    private static Magazzino instance;
    private Map<Integer, Farmaco> farmaci;

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
            if (f.getNome().equalsIgnoreCase(nomeFarmaco)) {
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
        farmaci.put(1, new Farmaco("Tachipirina", 1, 50, LocalDate.of(2025, 12, 31)));
        farmaci.put(2, new Farmaco("Tachipirina", 2, 30, LocalDate.of(2026, 6, 15)));
        farmaci.put(3, new Farmaco("Tachipirina", 3, 20, LocalDate.of(2024, 10, 10)));

        // 4 others
        farmaci.put(4, new Farmaco("Oki", 4, 100, LocalDate.of(2027, 1, 1)));
        farmaci.put(5, new Farmaco("Brufen", 5, 40, LocalDate.of(2025, 5, 20)));
        farmaci.put(6, new Farmaco("Bentelan", 6, 10, LocalDate.of(2024, 12, 1)));
        farmaci.put(7, new Farmaco("Aspirina", 7, 60, LocalDate.of(2026, 3, 30)));
        farmaci.put(8, new Farmaco("Finito", 8, 1, LocalDate.of(2026, 3, 30)));
    }
}

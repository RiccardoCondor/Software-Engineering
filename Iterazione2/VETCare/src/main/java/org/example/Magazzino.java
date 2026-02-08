package org.example;

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
        if (nomeFarmaco == null || nomeFarmaco.trim().isEmpty()) {
            System.out.println("Inserisci un nome valido per il farmaco.");
            return false;
        }
        System.out.println("Risultati ricerca per: " + nomeFarmaco);
        boolean trovato = false;
        for (Farmaco f : farmaci.values()) {
            if (f.getNome().equalsIgnoreCase(nomeFarmaco)) {
                System.out.println("ID: " + f.getId() +
                        ", Nome: " + f.getNome() +
                        ", Quantità: " + f.getQuantita() +
                        ", Scadenza: " + f.getScadenza());
                trovato = true;
            }
        }
        if (!trovato) {
            System.out.println("Nessun farmaco trovato con il nome: " + nomeFarmaco);
        }
        return trovato;
    }

    public Farmaco selezionaFarmacoById(int id) {
        Farmaco farmaco = getFarmacoByid(id);
        if(farmaco != null){
        int q = farmaco.getQuantita();
        farmaco.setQuantita(--q);
        }
        return farmaco;
    }

    public Farmaco getFarmacoByid(int id) {
        if (id <= 0 || !farmaci.containsKey(id)) {
            System.out.println("ID non valido o farmaco non trovato.");
            return null;
        }
        if (farmaci.get(id).getScadenza().isBefore(LocalDate.now())) {
            System.out.println("Farmaco Scaduto.");
            return null;
        }
        int q = farmaci.get(id).getQuantita();
        if (q == 0) {
            System.out.println("scorte esaurite!");
            return null;
        }
        return farmaci.get(id);
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
        farmaci.put(8, new Farmaco("Finito", 7, 1, LocalDate.of(2026, 3, 30)));
    }
}

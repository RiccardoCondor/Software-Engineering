package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Laboratorio {
    private static Laboratorio instance;
    private Map<Integer, Esame> esamiMap;
    private Map<Integer, Esame> esamiPassati;

    private Laboratorio() {
        esamiMap = new HashMap<>();
        esamiPassati = new HashMap<>();
    }

    public static Laboratorio getInstance() {
        if (instance == null) {
            instance = new Laboratorio();
        }
        return instance;
    }

    public Map<Integer, Esame> getEsamiMap() {
        return esamiMap;
    }

    public Map<Integer, Esame> getEsamiPassati() {
        return esamiPassati;
    }

    public void aggiungiEsame(Esame esame) {
        esamiMap.put(esame.getId(), esame);
    }

    public void aggiungiEsamePassato(Esame esame) {
        esamiPassati.put(esame.getId(), esame);
    }

    public Esame getEsame(int id) {
        return esamiMap.get(id);
    }

    private int ctr = 0;

    public int produciesame(String tipo, int microchip) {
        int id = ++ctr;
        java.time.LocalDate now = java.time.LocalDate.now();
        Esame esame = null;
        if(tipo == null)return -1;
        if (tipo.equalsIgnoreCase("urine")) {
            esame = new EsameUrine(now, id, "Valori urine: " + (int) (Math.random() * 100), microchip);
        } else if (tipo.equalsIgnoreCase("sangue")) {
            esame = new EsameSangue(now, id, "Valori sangue: " + (int) (Math.random() * 100), microchip);
        } else if (tipo.equalsIgnoreCase("completo")) {
            EsameCompleto completo = new EsameCompleto(now, id, "Profilo completo", microchip);
            completo.aggiungiEsame(
                    new EsameUrine(now, ++ctr, "Valori urine: " + (int) (Math.random() * 100), microchip));
            completo.aggiungiEsame(
                    new EsameSangue(now, ++ctr, "Valori sangue: " + (int) (Math.random() * 100), microchip));
            esame = completo;
        }

        if (esame != null) {
            esamiMap.put(id, esame);
            return id;
        }
        return -1;
    }

    public List<Esame> risultatiEsame(int microchip) {
        List<Esame> risultati = new ArrayList<>();
        java.util.Iterator<Map.Entry<Integer, Esame>> iterator = esamiMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Esame> entry = iterator.next();
            if (entry.getValue().getMicrochip() == microchip) {
                risultati.add(entry.getValue());
                esamiPassati.put(entry.getKey(), entry.getValue());
                iterator.remove();
            }
        }
        return risultati;
    }
}

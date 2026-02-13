package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartellaClinica {
    private Map<Integer, Visita> visite;
    private Visita visitaCorrente;
    private Map<Integer, Esame> esami;

    public CartellaClinica() {
        visite = new HashMap<>();
        visitaCorrente = null;
        esami = new HashMap<>();
    }

    public void nuovaVisita(String anamnesi, String esameObiettivo, String diagnosi) {
        visitaCorrente = null;
        if (anamnesi == null || esameObiettivo == null || diagnosi == null ||
                anamnesi.isEmpty() || esameObiettivo.isEmpty() || diagnosi.isEmpty())
            return;
        visitaCorrente = new Visita(anamnesi, esameObiettivo, diagnosi);
    }

    public void confermaVisita() {
        if (visitaCorrente == null)
            return;
        visite.put(visitaCorrente.getIdVisita(), visitaCorrente);
        visitaCorrente = null;
    }

    public Visita getVisitaCorrente() {
        return visitaCorrente;
    }

    public Visita ricercaVisita(int idVisita) {
        return visite.get(idVisita);
    }

    public java.util.Collection<Visita> getVisite() {
        return java.util.Collections.unmodifiableCollection(visite.values());
    }

    public void confermaEsami(List<Esame> nuoviEsami) {
        for (Esame esame : nuoviEsami) {
            this.esami.put(esame.getId(), esame);
        }
    }

    public boolean haVisitaInCorso() {
        return visitaCorrente != null;
    }

    public void aggiungiTerapia(Farmaco f, int posologia, String frequenza, java.time.LocalDate inizio,
            java.time.LocalDate fine) {
        if (visitaCorrente != null) {
            visitaCorrente.creaTerapia(f, posologia, frequenza, inizio, fine);
        }
    }

    public void setIdEsame(int idEsame) {
        if (visitaCorrente != null) {
            visitaCorrente.setIdEsame(idEsame);
        }
    }

}

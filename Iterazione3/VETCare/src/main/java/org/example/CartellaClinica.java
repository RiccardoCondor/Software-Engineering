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

    public List<Esame> risultatiEsami(int microchip) {
        List<Esame> nuoviEsami = Laboratorio.getInstance().risultatiEsame(microchip);
        confermaEsami(nuoviEsami);
        return nuoviEsami;
    }

    public void confermaEsami(List<Esame> nuoviEsami) {
        for (Esame esame : nuoviEsami) {
            this.esami.put(esame.getId(), esame);
        }
    }

    public boolean haVisitaInCorso() {
        return visitaCorrente != null;
    }

    public int richiediEsame(String tipoEsame, int microchip) {
        if (visitaCorrente == null)
            return -1;
        return visitaCorrente.richiediEsame(tipoEsame, microchip);
    }

    public Terapia creaTerapia(Farmaco f, int posologia, String frequenza, java.time.LocalDate data_i,
            java.time.LocalDate data_f) {
        if (visitaCorrente == null)
            return null;
        return visitaCorrente.creaTerapia(f, posologia, frequenza, data_i, data_f);
    }

    public boolean ricercaFarmaco(String nome) {
        if (visitaCorrente == null)
            return false;
        return visitaCorrente.ricercaFarmaco(nome);
    }

    public Farmaco getFarmacoById(int id) {
        if (visitaCorrente == null)
            return null;
        return visitaCorrente.getFarmacoById(id);
    }

    public Farmaco selezionaFarmacoByid(int id) {
        if (visitaCorrente == null)
            return null;
        return visitaCorrente.selezionaFarmacoByid(id);
    }
}

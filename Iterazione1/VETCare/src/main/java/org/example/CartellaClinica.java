package org.example;

import java.util.HashMap;
import java.util.Map;

public class CartellaClinica {
    private Map<Integer, Visita> visite;
    private Visita visitaCorrente;

    public CartellaClinica() {
        visite = new HashMap<Integer, Visita>();
        visitaCorrente = null;
    }

    public void nuovaVisita(String anamnesi, String esameObiettivo, String diagnosi) {
        visitaCorrente = new Visita(anamnesi, esameObiettivo, diagnosi);
    }

    public void confermaVisita() {
        visite.put(visitaCorrente.getIdVisita(), visitaCorrente);
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
}

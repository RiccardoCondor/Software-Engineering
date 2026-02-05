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

    public void NuovaVisita(String anamnesi, String esameObbiettivo, String diagnosi, int idvisit) {
        visitaCorrente = new Visita(anamnesi, esameObbiettivo, diagnosi, idvisit);
    }

    public void ConfermaVisit() {
        visite.put(Integer.valueOf(visitaCorrente.getIdvisit()), visitaCorrente);
    }

    public Visita getVisitaCorrente() {
        return visitaCorrente;
    }

    public Visita RicercaVisita(int idvisit) {
        return visite.get(Integer.valueOf(idvisit));
    }

    public java.util.Collection<Visita> getVisite() {
        return visite.values();
    }
}

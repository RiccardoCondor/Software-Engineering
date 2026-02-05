package org.example;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class VetCare {
    private Map<String, Propietario> proprietari;
    private Map<Integer, Animale> animali;
    private Propietario proprietarioCorrente;
    private Animale animaleCorrente;

    public VetCare() {
        proprietari = new HashMap<>();
        animali = new HashMap<>();
        proprietarioCorrente = null;
        animaleCorrente = null;
    }

    public java.util.Collection<Propietario> getProprietari() {
        return proprietari.values();
    }

    public java.util.Collection<Animale> getAnimali() {
        return animali.values();
    }

    public Animale getAnimaleCorrente() {
        return animaleCorrente;
    }

    public Propietario getProprietarioCorrente() {
        return proprietarioCorrente;
    }

    public Animale RicercaAnimale(int microchip) {
        return animali.get(Integer.valueOf(microchip));
    }

    public Propietario RicercaProprietari(String CF) {
        return proprietari.get(CF);
    }

    public Propietario InserisciNuovaAnagrafica(String nome, String cf, String contatto) {
        proprietarioCorrente=null;
        if(proprietari.get(cf) != null)
        {
            System.out.println("Proprietario già esistente");
            return proprietari.get(cf);
        }
        if(nome==null || cf==null || contatto==null)
        {
            System.out.println("Riepire tutti i campi");
            return null;
        }
        proprietarioCorrente = new Propietario(nome, cf, contatto);
        return proprietarioCorrente;
    }

    public Animale InserisciNuovoAnimale(String nome, String specie, String razza, int microchip, LocalDate dataNascita,
            Propietario proprietario) {
        animaleCorrente= null;
        if(animali.get(microchip) != null)
        {
            System.out.println("Animale gia esistente");
            return animali.get(microchip);
        }
        animaleCorrente = new Animale(nome, specie, razza, microchip, dataNascita, proprietario);
        return animaleCorrente;
    }

    public void confermaRegistrazione() {
        if(proprietarioCorrente!= null)
        proprietari.put(proprietarioCorrente.getCf(), proprietarioCorrente);

        if(animaleCorrente!=null)
        animali.put(Integer.valueOf(animaleCorrente.getMicrochip()), animaleCorrente);
    }

    public void NuovaVisita(int microchip, String anamnesi, String esameObbiettivo, String diagnosi, int idvist) {
        animaleCorrente = animali.get(Integer.valueOf(microchip));
        animaleCorrente.getCartella().NuovaVisita(anamnesi, esameObbiettivo, diagnosi, idvist);
    }

    public void confermaVisit() {
        animaleCorrente.getCartella().ConfermaVisit();
    }
}

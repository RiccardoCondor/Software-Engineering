package org.example;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class VetCare {
    private Map<String, Proprietario> proprietari;
    private Map<Integer, Animale> animali;
    private Proprietario proprietarioCorrente;
    private Animale animaleCorrente;
    private Calendario calendario;
    private static VetCare instance;

    public static VetCare getInstance() {
        if (instance == null) {
            instance = new VetCare();
        }
        return instance;
    }

    private VetCare() {
        proprietari = new HashMap<>();
        animali = new HashMap<>();
        proprietarioCorrente = null;
        animaleCorrente = null;
        this.calendario = new Calendario();
    }

    public java.util.Collection<Proprietario> getProprietari() {
        return java.util.Collections.unmodifiableCollection(proprietari.values());
    }

    public java.util.Collection<Animale> getAnimali() {
        return java.util.Collections.unmodifiableCollection(animali.values());
    }

    public Animale getAnimaleCorrente() {
        return animaleCorrente;
    }

    public Proprietario getProprietarioCorrente() {
        return proprietarioCorrente;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public Animale ricercaAnimale(int microchip) {
        return animali.get(microchip);
    }

    public Proprietario ricercaProprietari(String CF) {
        return proprietari.get(CF);
    }

    public Proprietario inserisciNuovaAnagrafica(String nome, String cf, String contatto) {
        proprietarioCorrente = null;
        if (proprietari.get(cf) != null) {
            System.out.println("Proprietario già esistente");
            proprietarioCorrente = proprietari.get(cf);
            return proprietarioCorrente;
        }
        if (nome == null || cf == null || contatto == null || nome.isEmpty() || cf.isEmpty() || contatto.isEmpty()) {
            System.out.println("Riepire tutti i campi");
            return null;
        }
        proprietarioCorrente = new Proprietario(nome, cf, contatto);
        return proprietarioCorrente;
    }

    public Animale inserisciNuovoAnimale(String nome, String specie, String razza, int microchip, LocalDate dataNascita,
            Proprietario proprietario) {
        animaleCorrente = null;
        if (animali.get(microchip) != null) {
            System.out.println("Animale gia esistente");
            animaleCorrente = animali.get(microchip);
            return animaleCorrente;
        }
        if (nome == null || microchip <= 0 || razza == null || specie == null || dataNascita == null
                || proprietario == null || nome.isEmpty() || razza.isEmpty() || specie.isEmpty()) {
            System.out.println("Riepire tutti i campi correttamente");
            return null;
        }
        animaleCorrente = new Animale(nome, specie, razza, microchip, dataNascita, proprietario);
        return animaleCorrente;
    }

    public void confermaRegistrazione() {
        if (proprietarioCorrente != null)
            proprietari.put(proprietarioCorrente.getCf(), proprietarioCorrente);

        if (animaleCorrente != null)
            animali.put(animaleCorrente.getMicrochip(), animaleCorrente);
    }

    public void nuovaVisita(int microchip, String anamnesi, String esameObiettivo, String diagnosi) {
        animaleCorrente = null;
        if (microchip <= 0) {
            System.out.println("Codice microchip errato");
            return;
        }
        animaleCorrente = animali.get(microchip);
        if (animaleCorrente == null) {
            System.out.println("Codice microchip non trovato");
            return;
        }
        animaleCorrente.getCartella().nuovaVisita(anamnesi, esameObiettivo, diagnosi);
    }

    public void confermaVisita() {
        if (animaleCorrente == null)
            return;

        animaleCorrente.getCartella().confermaVisita();
    }
}

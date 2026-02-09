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
        if (nome == null || cf == null || contatto == null || nome.trim().isEmpty() || cf.trim().isEmpty()
                || contatto.trim().isEmpty()) {
            throw new IllegalArgumentException("Dati proprietario incompleti.");
        }
        if (proprietari.containsKey(cf)) {
            System.out.println("Proprietario esistente.");
            proprietarioCorrente = proprietari.get(cf);
            return proprietarioCorrente; // Ritorna esistente se trovato
        }
        proprietarioCorrente = new Proprietario(nome, cf, contatto);
        proprietari.put(cf, proprietarioCorrente);
        return proprietarioCorrente;
    }

    public Animale inserisciNuovoAnimale(String nome, String specie, String razza, int microchip, LocalDate dataNascita,
            String cfProprietario) {
        if (microchip <= 0) {
            throw new IllegalArgumentException("Microchip non valido");
        }
        if (animali.containsKey(microchip)) {
            throw new IllegalArgumentException("Animale già presente con questo microchip");
        }
        Proprietario prop = proprietarioCorrente;
        if (prop == null) {
            throw new IllegalArgumentException("Proprietario non trovato per CF: " + cfProprietario);
        }
        if (nome == null || specie == null || razza == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Dati animale incompleti");
        }

        Animale nuovo = new Animale(nome, specie, razza, microchip, dataNascita, prop);
        animaleCorrente = nuovo;
        return nuovo;
    }

    public void confermaRegistrazione() {
        if (proprietarioCorrente != null && !proprietari.containsKey(proprietarioCorrente.getCf())) {
            proprietari.put(proprietarioCorrente.getCf(), proprietarioCorrente);
        }
        if (animaleCorrente != null && !animali.containsKey(animaleCorrente.getMicrochip())) {
            animali.put(animaleCorrente.getMicrochip(), animaleCorrente);
        }
    }

    public void nuovaVisita(int microchip, String anamnesi, String esameObiettivo, String diagnosi) {
        if (!animali.containsKey(microchip)) {
            throw new IllegalArgumentException("Animale non trovato per microchip: " + microchip);
        }
        animaleCorrente = animali.get(microchip);
        if (anamnesi == null || esameObiettivo == null || diagnosi == null || anamnesi.isEmpty()
                || esameObiettivo.isEmpty() || diagnosi.isEmpty()) {
            throw new IllegalArgumentException("Dati visita incompleti");
        }
        animaleCorrente.getCartella().nuovaVisita(anamnesi, esameObiettivo, diagnosi);
    }

    public void aggiungiTerapia(String nomeFarmaco, int idFarmaco, int posologia, String frequenza, LocalDate inizio,
            LocalDate fine) {
        if (animaleCorrente == null || animaleCorrente.getCartella().getVisitaCorrente() == null) {
            throw new IllegalStateException("Nessuna visita in corso per aggiungere terapia");
        }
        Visita visita = animaleCorrente.getCartella().getVisitaCorrente();

        Farmaco f = visita.getMagazzino().selezionaFarmacoById(idFarmaco);
        if (f == null || !f.getNome().equalsIgnoreCase(nomeFarmaco)) {
            throw new IllegalArgumentException("Farmaco non trovato o non corrispondente");
        }

        visita.creaTerapia(f, posologia, frequenza, inizio, fine);
    }

    // Metodo helper per la ricerca farmaci (per il menu)
    public boolean cercaFarmaco(String nome) {
        if (animaleCorrente == null || animaleCorrente.getCartella().getVisitaCorrente() == null)
            return false;
        return animaleCorrente.getCartella().getVisitaCorrente().ricercaFarmaco(nome);
    }

    public Farmaco getFarmacoById(int id) {
        if (animaleCorrente == null || animaleCorrente.getCartella().getVisitaCorrente() == null)
            return null;
        return animaleCorrente.getCartella().getVisitaCorrente().getMagazzino().getFarmacoByid(id);
    }

    public int aggiungiEsame(String tipoEsame, int microchip) {

        Animale a = animali.get(microchip);
        if (a == null)
            throw new IllegalArgumentException("Animale non trovato");

        Visita v = a.getCartella().getVisitaCorrente();
        if (v == null)
            throw new IllegalStateException("Nessuna visita attiva per questo animale");

        return v.richiediEsame(tipoEsame, microchip);
    }

    public void confermaVisita() {
        if (animaleCorrente != null && animaleCorrente.getCartella().getVisitaCorrente() != null) {
            animaleCorrente.getCartella().confermaVisita();
        } else {
            throw new IllegalStateException("Nessuna visita da confermare");
        }
    }

    public void aggiungiAppuntamento(int microchip, String titolo, String descrizione, java.time.LocalDateTime inizio,
            java.time.LocalDateTime fine) {
        Animale a = animali.get(microchip);
        if (a == null)
            throw new IllegalArgumentException("Animale non trovato");

        calendario.aggiungiAppuntamento(a, titolo, descrizione, inizio, fine);
    }
}

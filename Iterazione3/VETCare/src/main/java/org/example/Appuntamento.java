package org.example;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Rappresenta un appuntamento immutabile nel calendario.
 */
public final class Appuntamento implements Comparable<Appuntamento> {
    private final String titolo;
    private final String descrizione;
    private final LocalDateTime inizio;
    private final LocalDateTime fine;
    private Animale animale;

    public Appuntamento(Animale animale,String titolo, String descrizione, LocalDateTime inizio, LocalDateTime fine) {
        this.titolo = Objects.requireNonNull(titolo, "Il titolo non può essere null");
        this.descrizione = Objects.requireNonNull(descrizione, "La descrizione non può essere null");
        this.inizio = Objects.requireNonNull(inizio, "L'orario di inizio non può essere null");
        this.fine = Objects.requireNonNull(fine, "L'orario di fine non può essere null");
        this.animale = animale;
        if (fine.isBefore(inizio)) {
            throw new IllegalArgumentException("L'orario di fine deve essere dopo l'orario di inizio");
        }
    }
    public Animale getAnimale(){
        return  animale;
    }
    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDateTime getInizio() {
        return inizio;
    }

    public LocalDateTime getFine() {
        return fine;
    }

    @Override
    public int compareTo(Appuntamento altro) {
        return this.inizio.compareTo(altro.inizio);
    }

    @Override
    public String toString() {
        return "Appuntamento{" +
                "Animale= "+ animale.getMicrochip()+
                ", titolo='" + titolo + '\'' +
                ", inizio=" + inizio +
                ", fine=" + fine +
                '}';
    }
}

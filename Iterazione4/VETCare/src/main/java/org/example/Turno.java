package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Rappresenta un turno di lavoro per un membro dell'equipe.
 */
public class Turno {
    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private MembroEquipe membro;

    public Turno(LocalDate data, LocalTime oraInizio, LocalTime oraFine, MembroEquipe membro) {
        this.data = Objects.requireNonNull(data, "La data non può essere null");
        this.oraInizio = Objects.requireNonNull(oraInizio, "L'ora di inizio non può essere null");
        this.oraFine = Objects.requireNonNull(oraFine, "L'ora di fine non può essere null");
        this.membro = Objects.requireNonNull(membro, "Il membro non può essere null");

        if (oraFine.isBefore(oraInizio)) {
            throw new IllegalArgumentException("L'ora di fine deve essere dopo l'ora di inizio");
        }
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public MembroEquipe getMembro() {
        return membro;
    }

    @Override
    public String toString() {
        return "Turno{" +
                "data=" + data +
                ", oraInizio=" + oraInizio +
                ", oraFine=" + oraFine +
                ", membro=" + membro.getNome() +
                '}';
    }
}

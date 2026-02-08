package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EsameCompleto extends Esame {
    private List<Esame> componenti;

    public EsameCompleto(LocalDate data, int id, String valori, int microchip) {
        super(data, id, valori, microchip);
        this.componenti = new ArrayList<>();
    }

    public void aggiungiEsame(Esame esame) {
        componenti.add(esame);
    }

    public void rimuoviEsame(Esame esame) {
        componenti.remove(esame);
    }

    public List<Esame> getComponenti() {
        return componenti;
    }

    @Override
    public String getValori() {
        StringBuilder sb = new StringBuilder(super.getValori());
        sb.append(" [");
        for (Esame esame : componenti) {
            sb.append("\n  ").append(esame.getClass().getSimpleName()).append(": ").append(esame.getValori());
        }
        sb.append("\n]");
        return sb.toString();
    }
}

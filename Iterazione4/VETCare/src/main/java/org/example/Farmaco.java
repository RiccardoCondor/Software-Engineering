package org.example;

import java.time.LocalDate;

public class Farmaco {
    private String nome;
    private int id;
    private int quantita;
    private LocalDate scadenza;

    private boolean arrivato;
    private String mailFornitore;

    public Farmaco(String nome, int id, int q, LocalDate d, boolean arrivato, String mailFornitore) {
        this.id = id;
        this.nome = nome;
        this.quantita = q;
        this.scadenza = d;
        this.arrivato = arrivato;
        this.mailFornitore = mailFornitore;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getScadenza() {
        return scadenza;
    }

    public int getQuantita() {
        return quantita;
    }

    public boolean isArrivato() {
        return arrivato;
    }

    public void setArrivato(boolean arrivato) {
        this.arrivato = arrivato;
    }

    public String getMailFornitore() {
        return mailFornitore;
    }

    public void setMailFornitore(String mailFornitore) {
        this.mailFornitore = mailFornitore;
    }
}

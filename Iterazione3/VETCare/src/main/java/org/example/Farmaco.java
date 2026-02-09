package org.example;

import java.time.LocalDate;

public class Farmaco {
    private String nome;
    private int id;
    private int quantita;
    private LocalDate scadenza;

    public Farmaco(String nome, int id, int q, LocalDate d){
        this.id = id;
        this.nome = nome;
        this.quantita = q;
        this.scadenza = d;
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
}

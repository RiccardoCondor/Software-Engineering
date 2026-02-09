package org.example;

import java.time.LocalDate;

public abstract class Esame {
    protected LocalDate data;
    protected int id;
    protected String valori;
    protected int microchip;

    public Esame(LocalDate data, int id, String valori, int microchip) {
        this.data = data;
        this.id = id;
        this.valori = valori;
        this.microchip = microchip;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValori() {
        return valori;
    }

    public void setValori(String valori) {
        this.valori = valori;
    }

    public int getMicrochip() {
        return microchip;
    }

    public void setMicrochip(int microchip) {
        this.microchip = microchip;
    }
}

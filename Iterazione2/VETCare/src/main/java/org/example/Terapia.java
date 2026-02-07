package org.example;

import java.time.LocalDate;

public class Terapia {
    private static int ctr = 0;
    private int posologia = 0;
    private String frequenza = "";
    private LocalDate data_inizio = null;
    private LocalDate data_fine = null;
    private int idTer;
    private Farmaco farmaco;

    public Terapia() {
        this.idTer = ++ctr;
    }

    public void setData_fine(LocalDate data_fine) {
        this.data_fine = data_fine;
    }

    public void setData_inizio(LocalDate data_inizio) {
        this.data_inizio = data_inizio;
    }

    public void setFrequenza(String frequenza) {
        this.frequenza = frequenza;
    }

    public void setPosologia(int posologia) {
        this.posologia = posologia;
    }

    public void setFarmaco(Farmaco farmaco) {
        this.farmaco = farmaco;
    }

    public Farmaco getFarmaco() {
        return farmaco;
    }

    public String getFrequenza() {
        return frequenza;
    }

    public int getPosologia() {
        return posologia;
    }

    public int getIdTer() {
        return idTer;
    }

    public LocalDate getData_fine() {
        return data_fine;
    }

    public LocalDate getData_inizio() {
        return data_inizio;
    }
}

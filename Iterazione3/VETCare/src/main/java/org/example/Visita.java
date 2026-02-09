package org.example;

import java.time.LocalDate;

public class Visita {
    private static int ctr = 0;
    private String anamnesi;
    private String esameObiettivo;
    private String diagnosi;
    private int idVisita;
    private Terapia terapia = null;
    private Magazzino magazzino;
    private Laboratorio laboratorio;
    private int idEsame = -1;

    public Visita(String anamnesi, String esameObiettivo, String diagnosi) {
        this.anamnesi = anamnesi;
        this.esameObiettivo = esameObiettivo;
        this.diagnosi = diagnosi;
        this.idVisita = ++ctr;
        this.magazzino = Magazzino.getInstance();
        this.laboratorio = Laboratorio.getInstance();
    }

    public int getIdVisita() {
        return idVisita;
    }

    public String getAnamnesi() {
        return anamnesi;
    }

    public String getDiagnosi() {
        return diagnosi;
    }

    public String getEsameObiettivo() {
        return esameObiettivo;
    }

    public Terapia getTerapia() {
        return terapia;
    }

    public Magazzino getMagazzino() {
        return magazzino;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public Terapia creaTerapia(Farmaco f, int posologia, String frequenza, LocalDate data_i, LocalDate data_f) {
        if (f == null || posologia <= 0 || frequenza == null || frequenza.isEmpty() || data_i == null || data_f == null
                || data_i.isAfter(data_f)) {
            return null;
        }
        this.terapia = new Terapia();
        terapia.setFarmaco(f);
        terapia.setPosologia(posologia);
        terapia.setFrequenza(frequenza);
        terapia.setData_inizio(data_i);
        terapia.setData_fine(data_f);
        return terapia;
    }

    public boolean ricercaFarmaco(String nome) {
        return this.magazzino.ricercaFarmaci(nome);
    }

    public Farmaco getFarmacoById(int id) {
        return this.magazzino.getFarmacoByid(id);
    }
    public Farmaco selezionaFarmacoByid(int id){
        return this.magazzino.selezionaFarmacoById(id);
    }

    public String stampaTerapia() {
        if (terapia == null) {
            return "Nessuna terapia associata";
        }
        return "Terapia: " + terapia.getFarmaco().getNome()
                + ", Posologia: " + terapia.getPosologia()
                + ", Frequenza: " + terapia.getFrequenza()
                + ", Data Inizio: " + terapia.getData_inizio()
                + ", Data Fine: " + terapia.getData_fine();
    }

    public int richiediEsame(String tipoEsame, int microchip) {
        this.idEsame = this.laboratorio.produciesame(tipoEsame, microchip);
        return this.idEsame;
    }
}

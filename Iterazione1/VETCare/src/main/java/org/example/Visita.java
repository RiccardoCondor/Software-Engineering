package org.example;

public class Visita {
    private static int ctr = 0;
    private String anamnesi;
    private String esameObiettivo;
    private String diagnosi;
    private int idVisita;

    public Visita(String anamnesi, String esameObiettivo, String diagnosi) {
        this.anamnesi = anamnesi;
        this.esameObiettivo = esameObiettivo;
        this.diagnosi = diagnosi;
        this.idVisita = ++ctr;
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
}

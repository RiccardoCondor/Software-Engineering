package org.example;

public class Visita {
    private String anamnesi;
    private String esameObbiettivo;
    private String diagnosi;
    private int idvisit;

    public Visita(String anamnesi,String esameObbiettivo,String diagnosi,int idvisit){
        this.anamnesi = anamnesi;
        this.esameObbiettivo = esameObbiettivo;
        this.diagnosi = diagnosi;
        this.idvisit = idvisit;
    }

    public int getIdvisit() {
        return idvisit;
    }

    public String getAnamnesi() {
        return anamnesi;
    }

    public String getDiagnosi() {
        return diagnosi;
    }

    public String getEsameObbiettivo() {
        return esameObbiettivo;
    }
}

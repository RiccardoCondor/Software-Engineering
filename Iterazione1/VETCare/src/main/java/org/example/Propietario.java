package org.example;

public class Propietario {
    private String nome;
    private String cf;
    private String contatto;

    public Propietario(String nome, String cf, String contatto) {
        this.nome = nome;
        this.cf = cf;
        this.contatto = contatto;
    }

    public String getNome() {
        return nome;
    }

    public String getCf() {
        return cf;
    }

    public String getContatto() {
        return contatto;
    }
}

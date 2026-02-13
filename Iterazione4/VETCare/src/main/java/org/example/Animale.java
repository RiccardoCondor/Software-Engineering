package org.example;

import java.time.LocalDate;

public class Animale {
    private String nome;
    private String specie;
    private String razza;
    private int microchip;
    private LocalDate dataNascita;
    private Proprietario proprietario;
    private CartellaClinica cartella;

    public Animale(String nome, String specie, String razza, int microchip, LocalDate dataNascita,
            Proprietario proprietario) {
        this.nome = nome;
        this.specie = specie;
        this.razza = razza;
        this.microchip = microchip;
        this.dataNascita = dataNascita;
        this.proprietario = proprietario;
        this.cartella = new CartellaClinica();
    }

    public void nuovaVisita(String anamnesi, String esameObiettivo, String diagnosi) {
        cartella.nuovaVisita(anamnesi, esameObiettivo, diagnosi);
    }

    public void confermaVisita() {
        cartella.confermaVisita();
    }

    public boolean haVisitaInCorso() {
        return cartella.haVisitaInCorso();
    }

    public void aggiungiTerapia(Farmaco f, int posologia, String frequenza, java.time.LocalDate inizio,
            java.time.LocalDate fine) {
        cartella.aggiungiTerapia(f, posologia, frequenza, inizio, fine);
    }

    public void setIdEsame(int idEsame) {
        cartella.setIdEsame(idEsame);
    }

    public void confermaEsami(java.util.List<Esame> nuoviEsami) {
        cartella.confermaEsami(nuoviEsami);
    }

    public String getNome() {
        return nome;
    }

    public String getSpecie() {
        return specie;
    }

    public String getRazza() {
        return razza;
    }

    public int getMicrochip() {
        return microchip;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public CartellaClinica getCartella() {
        return cartella;
    }

}

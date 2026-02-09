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


    public void nuovaVisita(String anamnesi, String esameObiettivo, String diagnosi) {
        cartella.nuovaVisita(anamnesi, esameObiettivo, diagnosi);
    }

    public void confermaVisita() {
        cartella.confermaVisita();
    }

    public boolean haVisitaInCorso() {
        return cartella.haVisitaInCorso();
    }

    public int richiediEsame(String tipoEsame, int microchip) {
        return cartella.richiediEsame(tipoEsame, microchip);
    }

    public Terapia creaTerapia(Farmaco f, int posologia, String frequenza, java.time.LocalDate data_i,
            java.time.LocalDate data_f) {
        return cartella.creaTerapia(f, posologia, frequenza, data_i, data_f);
    }

    public boolean ricercaFarmaco(String nome) {
        return cartella.ricercaFarmaco(nome);
    }

    public Farmaco getFarmacoById(int id) {
        return cartella.getFarmacoById(id);
    }
    public Farmaco selezionaFarmacoByid(int id){
        return cartella.selezionaFarmacoByid(id);
    }
}

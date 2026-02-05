package org.example;

import java.time.LocalDate;

public class Animale {
    private String nome;
    private String specie;
    private String razza;
    private int microchip;
    private LocalDate dataNascita;
    private Propietario proprietario;
    private CartellaClinica cartella;

    public Animale(String nome, String specie, String razza, int microchip, LocalDate dataNascita,
            Propietario proprietario) {
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

    public Propietario getProprietario() {
        return proprietario;
    }

    public CartellaClinica getCartella() {
        return cartella;
    }
}

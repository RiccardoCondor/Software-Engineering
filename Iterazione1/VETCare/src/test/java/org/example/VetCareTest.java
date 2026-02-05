package org.example;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class VetCareTest {

    @org.junit.jupiter.api.Test
    void inserisciNuovaAnagrafica() {
        VetCare v = new VetCare();
        String Nome = "Rick";
        String CF = "rcr123";
        String Contatto = "334742";
        Propietario p = v.InserisciNuovaAnagrafica(Nome, CF, Contatto);
        String exp = "Rick";
        assertEquals(exp, v.getProprietarioCorrente().getNome());
    }

    @org.junit.jupiter.api.Test
    void inserisciNuovoAnimale() {
        VetCare v = new VetCare();
        String Nome = "Rick";
        String specie = "rcr123";
        String Contatto = "334742";
        Propietario p = v.InserisciNuovaAnagrafica(Nome, specie, Contatto);
        Animale a = v.InserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        String exp = "nika";
        assertEquals(exp, v.getAnimaleCorrente().getNome());
    }

    @org.junit.jupiter.api.Test
    void confermaRegistrazione() {
        VetCare v = new VetCare();
        String Nome = "Rick";
        String specie = "rcr123";
        String Contatto = "334742";
        Propietario p = v.InserisciNuovaAnagrafica(Nome, specie, Contatto);
        Animale a = v.InserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();
        assertEquals(a, v.RicercaAnimale(1234));
    }

}
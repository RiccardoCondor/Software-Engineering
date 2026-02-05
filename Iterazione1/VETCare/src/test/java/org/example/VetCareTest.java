package org.example;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class VetCareTest {

    @org.junit.jupiter.api.Test
    void inserisciNuovaAnagrafica() {
        VetCare v = new VetCare();

        //Inserimento codice fiscale non esistente
        String Nome = "Rick";
        String CF = "rcr123";
        String Contatto = "334742";
        Propietario p = v.InserisciNuovaAnagrafica(Nome, CF, Contatto);
        String exp = "Rick";
        assertEquals(exp, v.getProprietarioCorrente().getNome());
        v.confermaRegistrazione();

        // Inserimento codice fiscale esistente
        Propietario p1 = v.InserisciNuovaAnagrafica("Giulia", CF, "334789");
        assertEquals(p,p1);

        // Inserimento di nome,cf o contatto null
        Propietario p2 = v.InserisciNuovaAnagrafica(null, "MC43R5", "334789");
        assertNull(p2);
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

    @org.junit.jupiter.api.Test
    void testNuovaVisita() {
        VetCare v = new VetCare();
        Propietario p = v.InserisciNuovaAnagrafica("Rick", "rcr123", "334742");
        v.InserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();

        v.NuovaVisita(1234, "anamnesi", "esame", "diagnosi", 1);

        assertEquals("anamnesi", v.getAnimaleCorrente().getCartella().getVisitaCorrente().getAnamnesi());
    }

    @org.junit.jupiter.api.Test
    void testConfermaVisit() {
        VetCare v = new VetCare();
        Propietario p = v.InserisciNuovaAnagrafica("Rick", "rcr123", "334742");
        v.InserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();

        v.NuovaVisita(1234, "anamnesi", "esame", "diagnosi", 1);
        v.confermaVisit();

        assertNotNull(v.getAnimaleCorrente().getCartella().RicercaVisita(1));
        assertEquals("diagnosi", v.getAnimaleCorrente().getCartella().RicercaVisita(1).getDiagnosi());
    }
}
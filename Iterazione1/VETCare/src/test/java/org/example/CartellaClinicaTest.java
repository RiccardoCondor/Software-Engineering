package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartellaClinicaTest {

    @Test
    void confermaVisit() {
        CartellaClinica c = new CartellaClinica();
        c.NuovaVisita("anamnesi", "esameObbiettivo", "diagnosi", 1);
        c.ConfermaVisit();
        String exp = "anamnesi";
        assertEquals(exp, c.RicercaVisita(1).getAnamnesi());
    }

    @Test
    void inserisciVisita(){
        CartellaClinica c = new CartellaClinica();
        c.NuovaVisita("a","e","d",1);
        String exp = "a";
        assertEquals(exp, c.getVisitaCorrente().getAnamnesi());
    }

    @Test
    void RicercaVisite(){
        int idexp = 1;
        CartellaClinica c = new CartellaClinica();
        c.NuovaVisita("anamnesi", "esameObbiettivo", "diagnosi", idexp);
        c.ConfermaVisit();
        assertEquals(idexp, c.RicercaVisita(1).getIdvisit());
    }
}
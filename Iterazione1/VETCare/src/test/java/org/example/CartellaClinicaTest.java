package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartellaClinicaTest {

    @Test
    void confermaVisit() {
        CartellaClinica c = new CartellaClinica();
        c.nuovaVisita("anamnesi", "esameObbiettivo", "diagnosi");
        c.confermaVisita();
        int id = c.getVisitaCorrente().getIdVisita();
        String exp = "anamnesi";
        assertEquals(exp, c.ricercaVisita(id).getAnamnesi());
    }

    @Test
    void inserisciVisita() {
        CartellaClinica c = new CartellaClinica();
        c.nuovaVisita("a", "e", "d");
        String exp = "a";
        assertEquals(exp, c.getVisitaCorrente().getAnamnesi());
    }

    @Test
    void ricercaVisite() {
        CartellaClinica c = new CartellaClinica();
        c.nuovaVisita("anamnesi", "esameObbiettivo", "diagnosi");
        c.confermaVisita();
        int id = c.getVisitaCorrente().getIdVisita();
        assertEquals(id, c.ricercaVisita(id).getIdVisita());
    }

    @Test
    void testGetVisite() {
        CartellaClinica c = new CartellaClinica();
        c.nuovaVisita("anamnesi1", "esameObbiettivo1", "diagnosi1");
        c.confermaVisita();
        c.nuovaVisita("anamnesi2", "esameObbiettivo2", "diagnosi2");
        c.confermaVisita();

        assertEquals(2, c.getVisite().size());
    }
}
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
}
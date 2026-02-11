package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartellaClinicaTest {

    @Test
    void inserisciVisita() {
        // caso di inserimento corretto
        CartellaClinica c = new CartellaClinica();
        c.nuovaVisita("a", "e", "d");
        assertNotNull(c.getVisitaCorrente());
        // caso limite: inserimento nullo
        c.nuovaVisita(null, "e", null);
        assertNull(c.getVisitaCorrente());
        // caso limite: inserimento stringa vuota
        c.nuovaVisita("", "e", "d");
        assertNull(c.getVisitaCorrente());
    }

    @Test
    void confermaVisit() {
        // caso di funzionamento corretto
        CartellaClinica c = new CartellaClinica();
        c.nuovaVisita("anamnesi", "esameObbiettivo", "diagnosi");
        c.confermaVisita();
        c.nuovaVisita("anamnesi", "esameObbiettivo", "diagnosi");
        c.confermaVisita();
        int exp = 2;
        assertEquals(exp, c.getVisite().size());

        // caso limite: inserimenti nulli o stringhe vuote
        c.nuovaVisita("", "esameObbiettivo", "diagnosi");
        c.confermaVisita();
        c.nuovaVisita("anamnesi", null, "diagnosi");
        c.confermaVisita();
        assertEquals(exp, c.getVisite().size()); // non fa inserimenti

    }

    @Test
    void ricercaVisite() {
        CartellaClinica c = new CartellaClinica();
        c.nuovaVisita("anamnesi", "esameObbiettivo", "diagnosi");
        Visita v = c.getVisitaCorrente();
        c.confermaVisita();
        int id = v.getIdVisita();
        assertEquals(v, c.ricercaVisita(id));
    }

    @Test
    void testGetVisite() {
        CartellaClinica c = new CartellaClinica();
        c.nuovaVisita("anamnesi1", "esameObbiettivo1", "diagnosi1");
        c.confermaVisita();
        c.nuovaVisita("anamnesi2", "esameObbiettivo2", "diagnosi2");
        c.confermaVisita();
        // caso inserimento invalido
        c.nuovaVisita("", "esameObbiettivo2", "diagnosi2");
        c.confermaVisita();

        assertEquals(2, c.getVisite().size());
    }

}
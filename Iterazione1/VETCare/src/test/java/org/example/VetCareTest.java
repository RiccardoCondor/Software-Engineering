package org.example;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class VetCareTest {

    // Classi di equivalenza individuate in base al comportamento del metodo:
    // 1) Codice fiscale non presente: viene creato un nuovo proprietario
    // 2) Codice fiscale già presente: viene restituito il proprietario esistente
    // 3) Caso limite: parametri null, accettati dall'implementazione
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

    // Classi di equivalenza individuate in base al comportamento del metodo:
    // 1) Microchip non presente: viene creato un nuovo animale
    // 2) Microchip già presente: viene restituito l'animale esistente
    // 3) Caso limite: parametri null o microchip <= 0, ritorna null
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
        v.confermaRegistrazione();

        // Inserimento microchip esistente
        Animale a1 = v.InserisciNuovoAnimale("Whisky", "cane", "Labrodor", 1234, LocalDate.of(2020, 12, 2), p);
        assertEquals(a,a1);

        // Inserimento di nome,microchip(<=0),specie,data,razza o proprietario null
        Animale a2= v.InserisciNuovoAnimale(null, "cane", "Labrodor", -2442, LocalDate.of(2020, 12, 2), p);
        assertNull(a2);
    }


    // Classi di equivalenza individuate in base al comportamento del metodo confermaRegistrazione:
    // 1) Inserimento valido: proprietario e animale vengono salvati correttamente e sono ricercabili
    // 2) Parametri non validi (nome null, microchip <= 0): confermaRegistrazione non salva nulla, ritorna null
    @org.junit.jupiter.api.Test
    void confermaRegistrazione() {

        //Test funzionamento normale
        VetCare v = new VetCare();
        String Nome = "Rick";
        String specie = "rcr123";
        String Contatto = "334742";
        Propietario p = v.InserisciNuovaAnagrafica(Nome, specie, Contatto);
        Animale a = v.InserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();
        assertEquals(a, v.RicercaAnimale(1234));

        //Inserimento campi non validi
        Propietario p1 = v.InserisciNuovaAnagrafica(null, "K421E", "433434");
        Animale a1 = v.InserisciNuovoAnimale("Usagna", "Uccello", "usignolo", -1234, LocalDate.of(2020, 12, 2), p1);
        v.confermaRegistrazione();

        assertNull(p1);
        assertNull(a1);
    }

    @org.junit.jupiter.api.Test
    void testNuovaVisita() {

        //Caso principale di successo
        VetCare v = new VetCare();
        Propietario p = v.InserisciNuovaAnagrafica("Rick", "rcr123", "334742");
        v.InserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();

        v.NuovaVisita(1234, "anamnesi", "esame", "diagnosi", 1);

        assertEquals(1, v.getAnimaleCorrente().getCartella().getVisitaCorrente().getIdvisit());

        //Inserimento codice microchip o idvist <=0
        v.NuovaVisita(-1244, "anamnesi", "esame", "diagnosi", 2);
        assertNull(v.getAnimaleCorrente());

        //Inserimento di un codice microchip non esistente
        v.NuovaVisita(1123, "anamnesi", "esame", "diagnosi", 3);
        assertNull((v.getAnimaleCorrente()));
    }

    @org.junit.jupiter.api.Test
    void testConfermaVisit() {

        //Caso in cui i dati sono corretti
        VetCare v = new VetCare();
        Propietario p = v.InserisciNuovaAnagrafica("Rick", "rcr123", "334742");
        v.InserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();

        v.NuovaVisita(1234, "anamnesi", "esame", "diagnosi", 1);
        v.confermaVisit();

        assertNotNull(v.getAnimaleCorrente().getCartella().RicercaVisita(1));
        assertEquals("diagnosi", v.getAnimaleCorrente().getCartella().RicercaVisita(1).getDiagnosi());


        //Caso in cui microchip non viene trovato
        v.NuovaVisita(1256, "anamnesi", "esame", "diagnosi", 5);
        v.confermaVisit();
        assertNull(v.getAnimaleCorrente());

    }
}
package org.example;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class VetCareTest {

    // Classi di equivalenza individuate in base al comportamento del metodo:
    // 1) Codice fiscale non presente: viene creato un nuovo proprietario
    // 2) Codice fiscale già presente: viene restituito il proprietario esistente
    // 3) Caso limite: parametri null, accettati dall'implementazione
    // 4) caso limite: Ricezione di una stringa vuota
    @org.junit.jupiter.api.Test
    void inserisciNuovaAnagrafica() {
        VetCare v = VetCare.getInstance();

        //Inserimento codice fiscale non esistente
        String Nome = "Rick";
        String CF = "rcr123";
        String Contatto = "334742";
        Proprietario p = v.inserisciNuovaAnagrafica(Nome, CF, Contatto);
        String exp = "Rick";
        assertEquals(exp, v.getProprietarioCorrente().getNome());
        v.confermaRegistrazione();

        // Inserimento codice fiscale esistente
        Proprietario p1 = v.inserisciNuovaAnagrafica("Giulia", CF, "334789");
        assertEquals(p,p1);

        // Inserimento di nome,cf o contatto null
        Proprietario p2 = v.inserisciNuovaAnagrafica(null, "MC43R5", "334789");
        assertNull(p2);

        // Inserimento di una stringa vuota
        Proprietario p3 = v.inserisciNuovaAnagrafica("", "MC43R5", "334789");
        assertNull(p3);


    }

    // Classi di equivalenza individuate in base al comportamento del metodo:
    // 1) Microchip non presente: viene creato un nuovo animale
    // 2) Microchip già presente: viene restituito l'animale esistente
    // 3) Caso limite: parametri null o microchip <= 0, ritorna null
    // 4) cado limite: inserimento di una stringa vuota
    @org.junit.jupiter.api.Test
    void inserisciNuovoAnimale() {
        VetCare v = VetCare.getInstance();
        String Nome = "Rick";
        String specie = "rcr123";
        String Contatto = "334742";
        Proprietario p = v.inserisciNuovaAnagrafica(Nome, specie, Contatto);
        Animale a = v.inserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        String exp = "nika";
        assertEquals(exp, v.getAnimaleCorrente().getNome());
        v.confermaRegistrazione();

        // Inserimento microchip esistente
        Animale a1 = v.inserisciNuovoAnimale("Whisky", "cane", "Labrodor", 1234, LocalDate.of(2020, 12, 2), p);
        assertEquals(a,a1);

        // Inserimento di nome,microchip(<=0),specie,data,razza o proprietario null
        Animale a2= v.inserisciNuovoAnimale(null, "cane", "Labrodor", -2442, LocalDate.of(2020, 12, 2), p);
        assertNull(a2);

        //inserimento stringa vuota
        Animale a3= v.inserisciNuovoAnimale("", "cane", "Labrodor", 2442, LocalDate.of(2020, 12, 2), p);
        assertNull(a3);
    }


    // Classi di equivalenza individuate in base al comportamento del metodo confermaRegistrazione:
    // 1) Inserimento valido: proprietario e animale vengono salvati correttamente e sono ricercabili
    // 2) Parametri non validi (nome null, microchip <= 0): confermaRegistrazione non salva nulla, ritorna null
    @org.junit.jupiter.api.Test
    void confermaRegistrazione() {

        //Test funzionamento normale
        VetCare v = VetCare.getInstance();
        String Nome = "Rick";
        String specie = "rcr123";
        String Contatto = "334742";
        Proprietario p = v.inserisciNuovaAnagrafica(Nome, specie, Contatto);
        Animale a = v.inserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();
        assertEquals(a, v.ricercaAnimale(1234));

        //Inserimento campi non validi
        Proprietario p1 = v.inserisciNuovaAnagrafica(null, "K421E", "433434");
        Animale a1 = v.inserisciNuovoAnimale("Usagna", "Uccello", "usignolo", -1234, LocalDate.of(2020, 12, 2), p1);
        v.confermaRegistrazione();

        assertNull(p1);
        assertNull(a1);
    }

    @org.junit.jupiter.api.Test
    void testNuovaVisita() {

        //Caso principale di successo
        VetCare v = VetCare.getInstance();
        Proprietario p = v.inserisciNuovaAnagrafica("Rick", "rcr123", "334742");
        v.inserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();

        v.nuovaVisita(1234, "anamnesi", "esame", "diagnosi");

        assertNotNull(v.getAnimaleCorrente().getCartella().getVisitaCorrente());

        //Inserimento codice microchip <=0
        v.nuovaVisita(-1244, "anamnesi", "esame", "diagnosi");
        assertNull(v.getAnimaleCorrente());

        //Inserimento di un codice microchip non esistente
        v.nuovaVisita(1123, "anamnesi", "esame", "diagnosi");
        assertNull((v.getAnimaleCorrente()));
    }

    @org.junit.jupiter.api.Test
    void testConfermaVisit() {

        //Caso in cui i dati sono corretti
        VetCare v = VetCare.getInstance();
        Proprietario p = v.inserisciNuovaAnagrafica("Rick", "rcr123", "334742");
        v.inserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), p);
        v.confermaRegistrazione();

        v.nuovaVisita(1234, "anamnesi", "esame", "diagnosi");
        v.confermaVisita();


        assertNotNull(v.getAnimaleCorrente().getCartella().ricercaVisita(1));
        assertEquals("diagnosi", v.getAnimaleCorrente().getCartella().ricercaVisita(1).getDiagnosi());


        //Caso in cui microchip non viene trovato
        v.nuovaVisita(1256, "anamnesi", "esame", "diagnosi");
        v.confermaVisita();
        assertNull(v.getAnimaleCorrente());

    }
}
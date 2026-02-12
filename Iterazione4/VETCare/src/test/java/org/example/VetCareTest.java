package org.example;

import org.example.exceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class VetCareTest {

    @org.junit.jupiter.api.Test
    void testGetInstance() {
        VetCare v1 = VetCare.getInstance();
        VetCare v2 = VetCare.getInstance();
        assertNotNull(v1);
        assertSame(v1, v2);
    }

    // Classi di equivalenza individuate in base al comportamento del metodo:
    // 1) Codice fiscale non presente: viene creato un nuovo proprietario
    // 2) Codice fiscale già presente: viene restituito il proprietario esistente
    // 3) Caso limite: parametri null, accettati dall'implementazione
    // 4) caso limite: Ricezione di una stringa vuota
    @org.junit.jupiter.api.Test
    void inserisciNuovaAnagrafica() {
        VetCare v = VetCare.getInstance();
        // Inserimento codice fiscale non esistente
        String Nome = "Rick";
        String CF = "rcr123";
        String Contatto = "334742";
        Proprietario p = v.inserisciNuovaAnagrafica(Nome, CF, Contatto);
        String exp = "Rick";
        assertEquals(exp, v.getProprietarioCorrente().getNome());
        v.confermaRegistrazione();

        // Inserimento codice fiscale esistente
        Proprietario p1 = v.inserisciNuovaAnagrafica("Giulia", CF, "334789");
        assertEquals(p, p1);

        // Inserimento di nome,cf o contatto null o vuoti -> Deve lanciare
        // IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            v.inserisciNuovaAnagrafica(null, "MC43R5", "334789");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            v.inserisciNuovaAnagrafica("", "MC43R5", "334789");
        });
    }

    @org.junit.jupiter.api.Test
    void inserisciNuovoAnimale() {
        VetCare v = VetCare.getInstance();
        String Nome = "Rick";
        String CF = "rcr123";
        String Contatto = "334742";
        Proprietario p = v.inserisciNuovaAnagrafica(Nome, CF, Contatto);
        v.confermaRegistrazione(); // Importante confermare proprietario prima di usarlo

        Animale a = v.inserisciNuovoAnimale("nika", "cane", "maltese", 1234, LocalDate.of(2020, 12, 2), CF);
        String exp = "nika";
        assertEquals(exp, v.getAnimaleCorrente().getNome());
        v.confermaRegistrazione();

        // Inserimento microchip esistente -> Lancia eccezione
        assertThrows(IllegalArgumentException.class, () -> {
            v.inserisciNuovoAnimale("Whisky", "cane", "Labrodor", 1234, LocalDate.of(2020, 12, 2), CF);
        });

        // Inserimento dati invalidi
        assertThrows(IllegalArgumentException.class, () -> {
            v.inserisciNuovoAnimale(null, "cane", "Labrodor", -2442, LocalDate.of(2020, 12, 2), CF);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            v.inserisciNuovoAnimale("", "cane", "Labrodor", 2442, LocalDate.of(2020, 12, 2), CF);
        });
    }

    @org.junit.jupiter.api.Test
    void confirmRegistration() {
        // Test funzionamento normale
        VetCare v = VetCare.getInstance();
        String CF = "rcrNew";
        v.inserisciNuovaAnagrafica("Rick", CF, "334742");
        v.confermaRegistrazione(); // Conferma prop

        Animale a = v.inserisciNuovoAnimale("nika", "cane", "maltese", 1235, LocalDate.of(2020, 12, 2), CF);
        v.confermaRegistrazione(); // Conferma animale
        assertEquals(a, v.ricercaAnimale(1235));

        // Inserimento campi non validi che lanciano eccezione non arrivano a conferma
        assertThrows(IllegalArgumentException.class, () -> {
            v.inserisciNuovaAnagrafica(null, "K421E", "433434");
        });
    }

    @org.junit.jupiter.api.Test
    void testNuovaVisita() {
        // Caso principale di successo
        VetCare v = VetCare.getInstance();
        String CF = "rcrVisita";
        v.inserisciNuovaAnagrafica("Rick", CF, "334742");
        v.confermaRegistrazione();
        v.inserisciNuovoAnimale("nika", "cane", "maltese", 1236, LocalDate.of(2020, 12, 2), CF);
        v.confermaRegistrazione();

        v.nuovaVisita(1236, "anamnesi", "esame", "diagnosi");

        assertNotNull(v.getAnimaleCorrente().getCartella().getVisitaCorrente());

        // Inserimento codice microchip errato -> Eccezione
        assertThrows(IllegalArgumentException.class, () -> {
            v.nuovaVisita(-1244, "anamnesi", "esame", "diagnosi");
        });

        // Inserimento microchip non trovato -> Eccezione
        assertThrows(IllegalArgumentException.class, () -> {
            v.nuovaVisita(1123, "anamnesi", "esame", "diagnosi");
        });
    }

    @org.junit.jupiter.api.Test
    void testConfermaVisit() {
        VetCare v = VetCare.getInstance();
        String CF = "rcrConfVis";
        v.inserisciNuovaAnagrafica("Rick", CF, "334742");
        v.confermaRegistrazione();
        v.inserisciNuovoAnimale("nika", "cane", "maltese", 1237, LocalDate.of(2020, 12, 2), CF);
        v.confermaRegistrazione();

        v.nuovaVisita(1237, "anamnesi", "esame", "diagnosi");
        v.confermaVisita();

        // Nota: Id visita è incrementale statico, difficile predire l'ID esatto se
        // altri test girano prima.
        // Meglio verificare che la visita corrente sia null o che l'ultima visita
        // dell'animale sia quella.
        assertNull(v.getAnimaleCorrente().getCartella().getVisitaCorrente());
        assertFalse(v.getAnimaleCorrente().getCartella().getVisite().isEmpty());

        // Test conferma visita senza visita in corso (dovrebbe lanciare eccezione)
        assertThrows(IllegalStateException.class, () -> {
            v.confermaVisita();
        });

        // Test conferma visita su animale diverso senza visita
        v.inserisciNuovoAnimale("FidoNew", "Cane", "Labrador", 9999, LocalDate.of(2021, 1, 1), CF);
        v.confermaRegistrazione();
        // Ora animale corrente è a2, che non ha visite in corso
        assertThrows(IllegalStateException.class, () -> {
            v.confermaVisita();
        });
    }
}
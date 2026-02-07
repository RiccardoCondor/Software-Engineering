package org.example;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class VisitaTest {

    // Classi di equivalenza per creaTerapia:
    // 1) Input validi: Farmaco non null, posologia > 0, frequenza > 0, date valide
    // e coerenti -> Terapia creata
    // 2) Farmaco null -> return null
    // 3) Posologia o Frequenza <= 0 -> return null
    // 4) Date null o data_inizio > data_fine -> return null
    @Test
    void testCreaTerapia() {
        Visita visita = new Visita("Anamnesi", "Esame", "Diagnosi");
        Farmaco f = new Farmaco("Aspirina", 1, 10, LocalDate.now().plusYears(1));

        // Caso 1: Input validi
        Terapia t = visita.creaTerapia(f, 1, 2, LocalDate.now(), LocalDate.now().plusDays(5));
        assertNotNull(t);
        assertEquals(f, t.getFarmaco());
        assertEquals(1, t.getPosologia());
        assertEquals(2, t.getFrequenza());

        // Caso 2: Farmaco null
        assertNull(visita.creaTerapia(null, 1, 2, LocalDate.now(), LocalDate.now().plusDays(5)));

        // Caso 3: Posologia <= 0
        assertNull(visita.creaTerapia(f, 0, 2, LocalDate.now(), LocalDate.now().plusDays(5)));
        assertNull(visita.creaTerapia(f, -5, 2, LocalDate.now(), LocalDate.now().plusDays(5)));

        // Caso 3: Frequenza <= 0
        assertNull(visita.creaTerapia(f, 1, 0, LocalDate.now(), LocalDate.now().plusDays(5)));
        assertNull(visita.creaTerapia(f, 1, -1, LocalDate.now(), LocalDate.now().plusDays(5)));

        // Caso 4: Date non valide
        assertNull(visita.creaTerapia(f, 1, 2, null, LocalDate.now().plusDays(5)));
        assertNull(visita.creaTerapia(f, 1, 2, LocalDate.now(), null));
        assertNull(visita.creaTerapia(f, 1, 2, LocalDate.now().plusDays(5), LocalDate.now())); // Inizio dopo fine
    }

    // Classi di equivalenza per ricercaFarmaco (delega a Magazzino):
    // 1) Nome valido ed esistente -> true
    // 2) Nome non esistente -> false
    // 3) Nome null o vuoto -> false
    @Test
    void testRicercaFarmaco() {
        Visita visita = new Visita("Anamnesi", "Esame", "Diagnosi");

        // Assumiamo che Magazzino sia inizializzato con i dati di default (es.
        // "Tachipirina")
        // Caso 1: Nome esistente
        assertTrue(visita.ricercaFarmaco("Tachipirina"));

        // Caso 2: Nome non esistente
        assertFalse(visita.ricercaFarmaco("FarmacoInesistente"));

        // Caso 3: Nome null o vuoto (Gestito da Magazzino)
        assertFalse(visita.ricercaFarmaco(null));
        assertFalse(visita.ricercaFarmaco(""));
        assertFalse(visita.ricercaFarmaco("   "));
    }

    // Classi di equivalenza per selezionaFarmacoById (delega a Magazzino):
    // 1) ID valido ed esistente -> Farmaco
    // 2) ID non esistente -> null
    // 3) ID <= 0 -> null
    @Test
    void testSelezionaFarmacoById() {
        Visita visita = new Visita("Anamnesi", "Esame", "Diagnosi");

        // Assumiamo che ID 1 esista (Tachipirina)
        // Caso 1: ID valido
        Farmaco f = visita.selezionaFarmacoById(1);
        assertNotNull(f);
        assertEquals(1, f.getId());

        // Caso 2: ID non esistente
        assertNull(visita.selezionaFarmacoById(9999));

        // Caso 3: ID <= 0
        assertNull(visita.selezionaFarmacoById(0));
        assertNull(visita.selezionaFarmacoById(-1));
    }

    // Classi di equivalenza per stampaTerapia:
    // 1) Terapia null -> Messaggio "Nessuna terapia associata"
    // 2) Terapia presente -> Stringa con dettagli
    @Test
    void testStampaTerapia() {
        Visita visita = new Visita("Anamnesi", "Esame", "Diagnosi");

        // Caso 1: Nessuna terapia
        assertEquals("Nessuna terapia associata", visita.stampaTerapia());

        // Caso 2: Terapia presente
        Farmaco f = new Farmaco("Aspirina", 123, 10, LocalDate.of(2025, 1, 1));
        visita.creaTerapia(f, 2, 3, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5));

        String output = visita.stampaTerapia();
        assertTrue(output.contains("Terapia: Aspirina"));
        assertTrue(output.contains("Posologia: 2"));
        assertTrue(output.contains("Frequenza: 3"));
    }
}

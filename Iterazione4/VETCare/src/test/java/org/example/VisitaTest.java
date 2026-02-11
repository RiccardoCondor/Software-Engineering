package org.example;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class VisitaTest {

    // Classi di equivalenza per creaTerapia:
    // 1) Input validi: Farmaco non null, posologia > 0, frequenza valida (String
    // non vuota), date valide
    // e coerenti -> Terapia creata
    // 2) Farmaco null -> return null
    // 3) Posologia <= 0 -> return null
    // 4) Frequenza null o vuota -> return null
    // 5) Date null o data_inizio > data_fine -> return null
    @Test
    void testCreaTerapia() {
        Visita visita = new Visita("Anamnesi", "Esame", "Diagnosi");
        Farmaco f = new Farmaco("Oki", 4, 100, LocalDate.now().plusYears(1), true, "fornitore@example.com");

        // Caso 1: Input validi
        Terapia t = visita.creaTerapia(f, 1, "ogni 8 ore", LocalDate.now(), LocalDate.now().plusDays(5));
        assertNotNull(t);
        assertEquals(f, t.getFarmaco());
        assertEquals(1, t.getPosologia());
        assertEquals("ogni 8 ore", t.getFrequenza());

        // Caso 2: Farmaco null
        assertNull(visita.creaTerapia(null, 1, "ogni 8 ore", LocalDate.now(), LocalDate.now().plusDays(5)));

        // Caso 3: Posologia <= 0
        assertNull(visita.creaTerapia(f, 0, "ogni 8 ore", LocalDate.now(), LocalDate.now().plusDays(5)));
        assertNull(visita.creaTerapia(f, -5, "ogni 8 ore", LocalDate.now(), LocalDate.now().plusDays(5)));

        // Caso 4: Frequenza null o vuota
        assertNull(visita.creaTerapia(f, 1, null, LocalDate.now(), LocalDate.now().plusDays(5)));
        assertNull(visita.creaTerapia(f, 1, "", LocalDate.now(), LocalDate.now().plusDays(5)));
        // assertNull(visita.creaTerapia(f, 1, " ", LocalDate.now(),
        // LocalDate.now().plusDays(5))); // Opzionale se gestito trim()

        // Caso 5: Date non valide
        assertNull(visita.creaTerapia(f, 1, "ogni 8 ore", null, LocalDate.now().plusDays(5)));
        assertNull(visita.creaTerapia(f, 1, "ogni 8 ore", LocalDate.now(), null));
        assertNull(visita.creaTerapia(f, 1, "ogni 8 ore", LocalDate.now().plusDays(5), LocalDate.now())); // Inizio dopo
                                                                                                          // fine
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
        Farmaco f = new Farmaco("Oki", 4, 100, LocalDate.of(2025, 1, 1), true, "fornitore@example.com");
        visita.creaTerapia(f, 2, "3 volte al di", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5));

        String output = visita.stampaTerapia();
        assertTrue(output.contains("Terapia: Oki"));
        assertTrue(output.contains("Posologia: 2"));
        assertTrue(output.contains("Frequenza: 3 volte al di"));
    }

}

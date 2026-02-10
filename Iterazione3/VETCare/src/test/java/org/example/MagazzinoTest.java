package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.exceptions.*;

class MagazzinoTest {

    // Test Singleton
    @Test
    void testGetInstance() {
        Magazzino m1 = Magazzino.getInstance();
        Magazzino m2 = Magazzino.getInstance();
        assertNotNull(m1);
        assertSame(m1, m2);
    }

    // Classi di equivalenza per ricercaFarmaci:
    // 1) Nome presente (case insensitive) -> true
    // 2) Nome assente -> false
    // 3) Nome null o vuoto -> false
    @Test
    void testRicercaFarmaci() {
        Magazzino m = Magazzino.getInstance();

        // Caso 1: Nome presente
        assertTrue(m.ricercaFarmaci("Tachipirina")); // Esatto
        assertTrue(m.ricercaFarmaci("tachipirina")); // Case insensitive
        assertTrue(m.ricercaFarmaci("TACHIPIRINA")); // Case insensitive

        // Caso 2: Nome assente
        assertFalse(m.ricercaFarmaci("NonEsiste"));

        // Caso 3: Nome null o vuoto
        assertFalse(m.ricercaFarmaci(null));
        assertFalse(m.ricercaFarmaci(""));
        assertFalse(m.ricercaFarmaci("   "));
    }

    // Classi di equivalenza per selezionaFarmacoById:
    // 1) ID presente -> Farmaco
    // 2) ID assente -> null
    // 3) ID <= 0 -> null
    @Test
    void testSelezionaFarmacoById() {
        Magazzino m = Magazzino.getInstance();

        // Caso 1: ID presente (es. 1 è Tachipirina)
        Farmaco f = m.selezionaFarmacoById(2);
        assertNotNull(f);
        assertEquals(2, f.getId());
        assertEquals("Tachipirina", f.getNome());

        // Caso 2: ID assente
        assertThrows(EntitaNonTrovataException.class, () -> m.selezionaFarmacoById(9999));

        // Caso 3: ID <= 0
        assertThrows(EntitaNonTrovataException.class, () -> m.selezionaFarmacoById(0));
        assertThrows(EntitaNonTrovataException.class, () -> m.selezionaFarmacoById(-5));

        // Caso 4: Farmaco Scaduto (ID 3: scaduto 2024-10-10)
        assertThrows(FarmacoScadutoException.class, () -> m.selezionaFarmacoById(3));
    }
}

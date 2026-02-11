package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LaboratorioTest {

    @Test
    void testGetInstance() {
        Laboratorio l1 = Laboratorio.getInstance();
        Laboratorio l2 = Laboratorio.getInstance();
        assertNotNull(l1);
        assertSame(l1, l2);
    }

    // Classi di equivalenza per produciesame:
    // 1) Tipo valido ("urine", "sangue", "completo") -> Ritorna ID > 0
    // 2) Tipo invalido ("xyz", null, "") -> Ritorna -1
    @Test
    void testProduciesame() {
        Laboratorio lab = Laboratorio.getInstance();
        int microchip = 12345;

        // Caso 1: Tipi validi
        assertTrue(lab.produciesame("urine", microchip) > 0);
        assertTrue(lab.produciesame("sangue", microchip) > 0);
        assertTrue(lab.produciesame("completo", microchip) > 0);

        // Caso 2: Tipi invalidi
        assertEquals(-1, lab.produciesame("xyz", microchip));
        assertEquals(-1, lab.produciesame(null, microchip)); // Gestione eccezione
        // non presente, assumiamo stringa vuota o non null
        assertEquals(-1, lab.produciesame("", microchip));
    }

    // Classi di equivalenza per risultatiEsame:
    // 1) Microchip con esami pendenti -> Ritorna lista non vuota, rimuove da map
    // 2) Microchip senza esami pendenti -> Ritorna lista vuota
    @Test
    void testRisultatiEsame() {
        Laboratorio lab = Laboratorio.getInstance();
        int microchip = 67890;

        // Preparazione: aggiungi esami
        lab.produciesame("urine", microchip);
        lab.produciesame("sangue", microchip);

        // Caso 1: Recupero esami pendenti
        var risultati = lab.risultatiEsame(microchip);
        assertEquals(2, risultati.size());

        // Verifica rimozione da mappa pendenti (e spostamento in passati, se
        // implementato)
        var risultatiDopo = lab.risultatiEsame(microchip);
        assertEquals(0, risultatiDopo.size());

        // Verifica presenza in esami passati (se accessibile o verificabile
        // indirettamente)
        assertTrue(lab.getEsamiPassati().containsKey(1));
        assertTrue(lab.getEsamiPassati().containsKey(2));
    }
}

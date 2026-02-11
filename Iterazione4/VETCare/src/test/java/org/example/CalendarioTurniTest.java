package org.example;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.junit.Assert.*;

public class CalendarioTurniTest {

    private Calendario calendario;
    private MembroEquipe membro1;
    private MembroEquipe membro2;

    @Before
    public void setUp() {
        calendario = new Calendario();
        membro1 = new MembroEquipe(1, "Mario");
        membro2 = new MembroEquipe(2, "Luigi");
    }

    @Test
    public void testAggiungiTurnoValido() {
        LocalDateTime inizio = LocalDateTime.of(2023, 10, 27, 9, 0);
        LocalDateTime fine = LocalDateTime.of(2023, 10, 27, 12, 0);
        calendario.aggiungiTurno(membro1, inizio, fine);
        // No exception means success
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAggiungiTurnoSovrappostoStessoMembro() {
        LocalDateTime inizio1 = LocalDateTime.of(2023, 10, 27, 9, 0);
        LocalDateTime fine1 = LocalDateTime.of(2023, 10, 27, 12, 0);
        calendario.aggiungiTurno(membro1, inizio1, fine1);

        LocalDateTime inizio2 = LocalDateTime.of(2023, 10, 27, 10, 0); // Overlaps
        LocalDateTime fine2 = LocalDateTime.of(2023, 10, 27, 13, 0);
        calendario.aggiungiTurno(membro1, inizio2, fine2);
    }

    @Test
    public void testAggiungiTurnoSovrappostoDiversoMembro() {
        LocalDateTime inizio1 = LocalDateTime.of(2023, 10, 27, 9, 0);
        LocalDateTime fine1 = LocalDateTime.of(2023, 10, 27, 12, 0);
        calendario.aggiungiTurno(membro1, inizio1, fine1);

        LocalDateTime inizio2 = LocalDateTime.of(2023, 10, 27, 9, 0); // Same time
        LocalDateTime fine2 = LocalDateTime.of(2023, 10, 27, 12, 0);
        calendario.aggiungiTurno(membro2, inizio2, fine2); // Should succeed
    }
}

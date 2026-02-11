package org.example;

import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.Assert.*;

public class TurnoTest {

    @Test
    public void testTurnoCreation() {
        MembroEquipe membro = new MembroEquipe(1, "Mario Rossi");
        LocalDate data = LocalDate.of(2023, 10, 27);
        LocalTime inizio = LocalTime.of(9, 0);
        LocalTime fine = LocalTime.of(17, 0);

        Turno turno = new Turno(data, inizio, fine, membro);

        assertEquals(data, turno.getData());
        assertEquals(inizio, turno.getOraInizio());
        assertEquals(fine, turno.getOraFine());
        assertEquals(membro, turno.getMembro());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTurnoInvalidTime() {
        MembroEquipe membro = new MembroEquipe(1, "Mario Rossi");
        LocalDate data = LocalDate.of(2023, 10, 27);
        LocalTime inizio = LocalTime.of(17, 0);
        LocalTime fine = LocalTime.of(9, 0);

        new Turno(data, inizio, fine, membro);
    }
}

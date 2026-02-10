package org.example;

import org.example.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class CalendarioTest {

        private Calendario calendario;
        private Animale animale;
        private Proprietario proprietario;

        @BeforeEach
        void setUp() {
                calendario = new Calendario();
                proprietario = new Proprietario("Mario Rossi", "MRARSS80A01H501U", "3331234567");
                animale = new Animale("Fido", "Cane", "Labrador", 12345, LocalDate.of(2020, 1, 1), proprietario);
        }

        @Test
        void testAggiungiAppuntamentoValido() {
                LocalDateTime inizio = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 0));
                LocalDateTime fine = inizio.plusHours(1);

                assertDoesNotThrow(
                                () -> calendario.aggiungiAppuntamento(animale, "Vaccino", "Vaccinazione annuale",
                                                inizio, fine));
        }

        @Test
        void testAggiungiAppuntamentoNonValido() {
                LocalDateTime baseDate = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 0));

                // 1. Fine prima di Inizio
                assertThrows(IllegalArgumentException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Test", "Desc", baseDate,
                                                baseDate.minusHours(1)));

                // 2. Durata < 1 ora
                assertThrows(IllegalArgumentException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Test", "Desc", baseDate,
                                                baseDate.plusMinutes(30)));

                // 3. Orario non intero (es. 9:15)
                assertThrows(IllegalArgumentException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Test", "Desc",
                                                baseDate.plusMinutes(15), baseDate.plusHours(1).plusMinutes(15)));

                // 4. Fuori orario lavorativo (prima delle 8)
                LocalDateTime earlyStart = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(7, 0));
                assertThrows(IllegalArgumentException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Test", "Desc", earlyStart,
                                                earlyStart.plusHours(1)));

                // 5. Fuori orario lavorativo (dopo le 18)
                LocalDateTime lateEnd = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(19, 0));
                assertThrows(IllegalArgumentException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Test", "Desc", lateEnd.minusHours(1),
                                                lateEnd));

                // 6. Su giorni diversi
                assertThrows(IllegalArgumentException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Test", "Desc",
                                                baseDate, baseDate.plusDays(1).plusHours(1)));
        }

        @Test
        void testSovrapposizione() {
                LocalDateTime inizio = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0));
                LocalDateTime fine = inizio.plusHours(2); // 10:00 - 12:00

                calendario.aggiungiAppuntamento(animale, "Primo Appuntamento", "Desc", inizio, fine);

                // 1. Esattamente sovrapposto
                assertThrows(SovrapposizioneAppuntamentoException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Overlap", "Desc", inizio, fine));

                // 2. Inizia durante
                assertThrows(SovrapposizioneAppuntamentoException.class, () -> calendario.aggiungiAppuntamento(animale,
                                "Overlap Start", "Desc", inizio.plusHours(1), fine.plusHours(1)));

                // 3. Finisce durante
                assertThrows(SovrapposizioneAppuntamentoException.class, () -> calendario.aggiungiAppuntamento(animale,
                                "Overlap End", "Desc", inizio.minusHours(1), fine.minusHours(1)));

                // 4. Contiene l'esistente
                assertThrows(SovrapposizioneAppuntamentoException.class, () -> calendario.aggiungiAppuntamento(animale,
                                "Overlap Enclosing", "Desc", inizio.minusHours(1), fine.plusHours(1)));

        }

        @Test
        void testSovrapposizioneParziale() {
                LocalDateTime inizio = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0));
                LocalDateTime fine = inizio.plusHours(2); // 10:00 - 12:00
                calendario.aggiungiAppuntamento(animale, "Primo", "Desc", inizio, fine);

                // 10:00 - 11:00 (Sovrapposto)
                assertThrows(SovrapposizioneAppuntamentoException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Secondo", "Desc", inizio,
                                                inizio.plusHours(1)));

                // 11:00 - 12:00 (Sovrapposto)
                assertThrows(SovrapposizioneAppuntamentoException.class,
                                () -> calendario.aggiungiAppuntamento(animale, "Terzo", "Desc", inizio.plusHours(1),
                                                fine));

                // 09:00 - 10:00 (Adiacente Prima - OK)
                assertDoesNotThrow(() -> calendario.aggiungiAppuntamento(animale, "Adiacente Prima", "Desc",
                                inizio.minusHours(1), inizio));

                // 12:00 - 13:00 (Adiacente Dopo - OK)
                assertDoesNotThrow(
                                () -> calendario.aggiungiAppuntamento(animale, "Adiacente Dopo", "Desc", fine,
                                                fine.plusHours(1)));
        }
}

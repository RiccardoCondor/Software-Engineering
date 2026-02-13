package org.example;

import org.example.exceptions.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Calendario {
    // Mappa ottimizzata per la ricerca rapida per data
    private final Map<LocalDate, List<Appuntamento>> mappaAppuntamenti;
    private final Map<LocalDate, List<Turno>> mappaTurni;

    private static final LocalTime INIZIO_LAVORO = LocalTime.of(8, 0);
    private static final LocalTime FINE_LAVORO = LocalTime.of(18, 0);
    private static final DateTimeFormatter FMT_INTESTAZIONE_GRIGLIA = DateTimeFormatter.ofPattern("EEE dd");

    public Calendario() {
        this.mappaAppuntamenti = new HashMap<>();
        this.mappaTurni = new HashMap<>();
    }

    /**
     * Aggiunge un appuntamento con validazione rigorosa.
     * Regole:
     * 1. Deve essere entro l'orario di lavoro (08:00 - 18:00).
     * 2. Deve iniziare e finire all'ora esatta (MM=00).
     * 3. Non deve estendersi su più giorni.
     * 4. Deve durare un numero intero di ore.
     */
    public void aggiungiAppuntamento(Animale animale, String titolo, String descrizione, LocalDateTime inizio,
            LocalDateTime fine) {
        Appuntamento appuntamento = new Appuntamento(animale, titolo, descrizione, inizio, fine);
        validaTutto(appuntamento);

        LocalDate data = appuntamento.getInizio().toLocalDate();
        mappaAppuntamenti.computeIfAbsent(data, k -> new ArrayList<>()).add(appuntamento);
        Collections.sort(mappaAppuntamenti.get(data));
    }

    public Operazione aggiungiAppuntamento(Animale animale, String titolo, String descrizione, LocalDateTime inizio,
            LocalDateTime fine, String tipo, Map<Integer, MembroEquipe> membri) {
        Operazione op = new Operazione(animale, titolo, descrizione, inizio, fine, tipo, membri);
        aggiungiAppuntamento(op);
        return op;
    }

    public void aggiungiAppuntamento(Appuntamento app) {
        validaTutto(app);

        LocalDate data = app.getInizio().toLocalDate();
        mappaAppuntamenti.computeIfAbsent(data, k -> new ArrayList<>()).add(app);
        Collections.sort(mappaAppuntamenti.get(data));
    }

    public void checkDisponibilita(LocalDateTime inizio, LocalDateTime fine) {
        // Creiamo un appuntamento temporaneo solo per sfruttare la logica di controllo
        // esistente
        // o replichiamo la logica. Replichiamo la logica per pulizia.

        LocalDate data = inizio.toLocalDate();
        List<Appuntamento> appuntamentiEsistenti = mappaAppuntamenti.getOrDefault(data, Collections.emptyList());

        for (Appuntamento esistente : appuntamentiEsistenti) {
            LocalDateTime inizio1 = inizio;
            LocalDateTime fine1 = fine;
            LocalDateTime inizio2 = esistente.getInizio();
            LocalDateTime fine2 = esistente.getFine();

            if (inizio1.isBefore(fine2) && fine1.isAfter(inizio2)) {
                throw new SovrapposizioneAppuntamentoException(
                        "Slot orario non disponibile. L'orario richiesto si sovrappone con [" + esistente.getTitolo()
                                + "]. Inserire una fascia oraria valida.");
            }
        }
    }

    private void validaTutto(Appuntamento app) {
        validaAppuntamento(app);
        // Check atomicamente la disponibilità
        checkDisponibilita(app.getInizio(), app.getFine());
    }

    public void aggiungiTurno(MembroEquipe m, LocalDateTime inizio, LocalDateTime fine) {
        // Validation logic reused from validateAppuntamento
        if (!inizio.toLocalDate().equals(fine.toLocalDate())) {
            throw new IllegalArgumentException("Il turno deve iniziare e finire lo stesso giorno.");
        }
        if (inizio.getMinute() != 0 || fine.getMinute() != 0) {
            throw new IllegalArgumentException("I turni devono iniziare e finire esattamente all'ora.");
        }
        if (Duration.between(inizio, fine).toHours() < 1) {
            throw new IllegalArgumentException("La durata minima è di 1 ora.");
        }
        if (inizio.toLocalTime().isBefore(INIZIO_LAVORO) || fine.toLocalTime().isAfter(FINE_LAVORO)) {
            throw new IllegalArgumentException("Il turno deve essere entro l'orario di lavoro.");
        }

        // Check overlap for the same member
        LocalDate data = inizio.toLocalDate();
        List<Turno> turniGiorno = mappaTurni.getOrDefault(data, Collections.emptyList());
        for (Turno t : turniGiorno) {
            if (t.getMembro().getIdmembro() == m.getIdmembro()) { // Check same member
                // Check time overlap
                LocalDateTime tInizio = LocalDateTime.of(t.getData(), t.getOraInizio());
                LocalDateTime tFine = LocalDateTime.of(t.getData(), t.getOraFine());
                if (inizio.isBefore(tFine) && fine.isAfter(tInizio)) {
                    throw new IllegalArgumentException(
                            "Il membro " + m.getNome() + " ha già un turno in questo orario.");
                }
            }
        }

        Turno nuovoTurno = new Turno(data, inizio.toLocalTime(), fine.toLocalTime(), m);
        mappaTurni.computeIfAbsent(data, k -> new ArrayList<>()).add(nuovoTurno);
    }

    private boolean siSovrappone(Appuntamento app1, Appuntamento app2) {
        LocalDateTime inizio1 = app1.getInizio();
        LocalDateTime fine1 = app1.getFine();
        LocalDateTime inizio2 = app2.getInizio();
        LocalDateTime fine2 = app2.getFine();

        return inizio1.isBefore(fine2) && fine1.isAfter(inizio2);
    }

    private void validaAppuntamento(Appuntamento app) {
        LocalDateTime inizio = app.getInizio();
        LocalDateTime fine = app.getFine();

        // Regola: Stesso Giorno
        if (!inizio.toLocalDate().equals(fine.toLocalDate())) {
            throw new IllegalArgumentException("L'appuntamento deve iniziare e finire lo stesso giorno.");
        }

        // Regola: Ore Intere (Minuti di inizio/fine puliti)
        if (inizio.getMinute() != 0 || fine.getMinute() != 0) {
            throw new IllegalArgumentException(
                    "Gli appuntamenti devono iniziare e finire esattamente all'ora (es. 09:00, non 09:15).");
        }

        // Regola: Durata Intera
        long ore = Duration.between(inizio, fine).toHours();
        if (ore < 1) {
            throw new IllegalArgumentException("La durata minima è di 1 ora.");
        }

        // Regola: Orario di Lavoro
        LocalTime oraInizio = inizio.toLocalTime();
        LocalTime oraFine = fine.toLocalTime();
        if (oraInizio.isBefore(INIZIO_LAVORO) || oraFine.isAfter(FINE_LAVORO)) {
            throw new IllegalArgumentException(
                    "L'appuntamento deve essere entro l'orario di lavoro (" + INIZIO_LAVORO + " - " + FINE_LAVORO
                            + ").");
        }
    }

    public void stampaCalendarioGriglia() {
        LocalDate inizioSettimana = LocalDate.now();
        // Mostra i prossimi 7 giorni in griglia
        int giorniDaMostrare = 7;

        System.out.println("\n=== Vista Griglia Settimanale ===");

        // Riga di Intestazione
        System.out.print("      |");
        for (int i = 0; i < giorniDaMostrare; i++) {
            LocalDate data = inizioSettimana.plusDays(i);
            System.out.printf(" %-6s |", data.format(FMT_INTESTAZIONE_GRIGLIA));
        }
        System.out.println("\n------+--------+--------+--------+--------+--------+--------+--------+");

        // Righe Orario (08:00 to 17:00, poiché 18:00 è l'orario di chiusura)
        for (int ora = INIZIO_LAVORO.getHour(); ora < FINE_LAVORO.getHour(); ora++) {
            LocalTime oraCorrente = LocalTime.of(ora, 0);
            System.out.printf("%02d:00 |", ora);

            for (int i = 0; i < giorniDaMostrare; i++) {
                LocalDate data = inizioSettimana.plusDays(i);
                List<Appuntamento> appuntamentiGiornalieri = mappaAppuntamenti.getOrDefault(data,
                        Collections.emptyList());
                List<Turno> turniGiornalieri = mappaTurni.getOrDefault(data, Collections.emptyList());

                String contenutoCella = "        "; // 8 spazi vuoti

                boolean appuntamentoTrovato = false;

                // 1. Priorità agli Appuntamenti
                for (Appuntamento app : appuntamentiGiornalieri) {
                    LocalTime appInizio = app.getInizio().toLocalTime();
                    LocalTime appFine = app.getFine().toLocalTime();

                    if (!appInizio.isAfter(oraCorrente) && appFine.isAfter(oraCorrente)) {
                        String marcatore = app.getTitolo();
                        if (marcatore.length() > 6)
                            marcatore = marcatore.substring(0, 6);
                        contenutoCella = String.format(" %-6s ", marcatore);
                        appuntamentoTrovato = true;
                        break;
                    }
                }

                System.out.printf("%s|", contenutoCella);
            }
            System.out.println();
        }
        System.out.println("------+--------+--------+--------+--------+--------+--------+--------+");
    }

    public void stampaTurniGriglia() {
        LocalDate inizioSettimana = LocalDate.now();
        int giorniDaMostrare = 7;

        System.out.println("\n=== Vista Turni Settimanali ===");

        // Intestazione
        System.out.print("      |");
        for (int i = 0; i < giorniDaMostrare; i++) {
            LocalDate data = inizioSettimana.plusDays(i);
            System.out.printf(" %-8s |", data.format(FMT_INTESTAZIONE_GRIGLIA));
        }
        System.out.println("\n------+----------+----------+----------+----------+----------+----------+----------+");

        // Righe Orario
        for (int ora = INIZIO_LAVORO.getHour(); ora < FINE_LAVORO.getHour(); ora++) {
            LocalTime oraCorrente = LocalTime.of(ora, 0);

            // 1. Calcola quante righe servono per questa ora (max numero di turni
            // sovrapposti in un giorno)
            int maxRighe = 1;
            List<List<Turno>> turniPerGiorno = new ArrayList<>();

            for (int i = 0; i < giorniDaMostrare; i++) {
                LocalDate data = inizioSettimana.plusDays(i);
                List<Turno> tuttiTurni = mappaTurni.getOrDefault(data, Collections.emptyList());
                List<Turno> turniAttivi = new ArrayList<>();
                for (Turno t : tuttiTurni) {
                    if (!t.getOraInizio().isAfter(oraCorrente) && t.getOraFine().isAfter(oraCorrente)) {
                        turniAttivi.add(t);
                    }
                }
                turniPerGiorno.add(turniAttivi);
                if (turniAttivi.size() > maxRighe) {
                    maxRighe = turniAttivi.size();
                }
            }

            // 2. Stampa le N righe per questa fascia oraria
            for (int riga = 0; riga < maxRighe; riga++) {
                // Colonna Orario
                if (riga == 0) {
                    System.out.printf("%02d:00 |", ora);
                } else {
                    System.out.print("      |");
                }

                for (int i = 0; i < giorniDaMostrare; i++) {
                    List<Turno> turni = turniPerGiorno.get(i);

                    if (riga < turni.size()) {
                        Turno t = turni.get(riga);
                        String nome = t.getMembro().getNome();
                        // Troncamento se necessario: Surname or Nome truncated to 8 chars
                        String[] parts = nome.split(" ");
                        String shortName = (parts.length > 1) ? parts[1] : nome;
                        if (shortName.length() > 8)
                            shortName = shortName.substring(0, 8);

                        System.out.printf(" %-8s |", shortName);
                    } else {
                        // Empty cell if no shift for this row index (but hour block exists)
                        System.out.print("          |");
                    }
                }
                System.out.println();
            }
            System.out.println("------+----------+----------+----------+----------+----------+----------+----------+");
        }
    }

    public void stampaAgenda() {
        LocalDate oggi = LocalDate.now();
        LocalDate dataFine = oggi.plusDays(30);

        System.out.println("\n=== Agenda 30 Giorni ===");
        List<LocalDate> date = new ArrayList<>(mappaAppuntamenti.keySet());
        Collections.sort(date);

        for (LocalDate data : date) {
            if (data.isBefore(oggi) || data.isAfter(dataFine))
                continue;

            System.out.println("\n" + data.format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy")));
            for (Appuntamento app : mappaAppuntamenti.get(data)) {
                System.out.printf("  %02d:00 - %02d:00 | %s%n",
                        app.getInizio().getHour(),
                        app.getFine().getHour(),
                        app.toString());
            }
        }
    }
}

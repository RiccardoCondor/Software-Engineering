package org.example;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Calendario {
    // Mappa ottimizzata per la ricerca rapida per data
    private final Map<LocalDate, List<Appuntamento>> mappaAppuntamenti;

    private static final LocalTime INIZIO_LAVORO = LocalTime.of(8, 0);
    private static final LocalTime FINE_LAVORO = LocalTime.of(18, 0);
    private static final DateTimeFormatter FMT_INTESTAZIONE_GRIGLIA = DateTimeFormatter.ofPattern("EEE dd");

    public Calendario() {
        this.mappaAppuntamenti = new HashMap<>();
    }

    /**
     * Aggiunge un appuntamento con validazione rigorosa.
     * Regole:
     * 1. Deve essere entro l'orario di lavoro (08:00 - 18:00).
     * 2. Deve iniziare e finire all'ora esatta (MM=00).
     * 3. Non deve estendersi su più giorni.
     * 4. Deve durare un numero intero di ore.
     */
    public void aggiungiAppuntamento(Animale animale, String titolo, String descrizione, LocalDateTime inizio, LocalDateTime fine) {
        Appuntamento appuntamento = new Appuntamento(animale,titolo, descrizione, inizio, fine);
        validaAppuntamento(appuntamento);
        verificaSovrapposizione(appuntamento);

        LocalDate data = appuntamento.getInizio().toLocalDate();
        mappaAppuntamenti.computeIfAbsent(data, k -> new ArrayList<>()).add(appuntamento);
        // se necessario
        Collections.sort(mappaAppuntamenti.get(data));
    }

    public void aggiungiAppuntamento(Appuntamento app) {
        validaAppuntamento(app);
        verificaSovrapposizione(app);

        LocalDate data = app.getInizio().toLocalDate();
        mappaAppuntamenti.computeIfAbsent(data, k -> new ArrayList<>()).add(app);
        Collections.sort(mappaAppuntamenti.get(data));
    }

    private void verificaSovrapposizione(Appuntamento nuovoApp) {
        LocalDate data = nuovoApp.getInizio().toLocalDate();
        List<Appuntamento> appuntamentiEsistenti = mappaAppuntamenti.getOrDefault(data, Collections.emptyList());

        for (Appuntamento esistente : appuntamentiEsistenti) {
            if (siSovrappone(nuovoApp, esistente)) {
                throw new SovrapposizioneAppuntamentoException(
                        "Slot orario non disponibile. L'orario richiesto si sovrappone con [" + esistente.getTitolo()
                                + "]. Inserire una fascia oraria valida.");
            }
        }
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

                String contenutoCella = "        "; // 8 spazi vuoti

                // Controlla se qualche appuntamento copre questo slot orario
                // Un appuntamento copre lo slot se inizio <= oraCorrente E fine > oraCorrente
                for (Appuntamento app : appuntamentiGiornalieri) {
                    LocalTime appInizio = app.getInizio().toLocalTime();
                    LocalTime appFine = app.getFine().toLocalTime();

                    if (!appInizio.isAfter(oraCorrente) && appFine.isAfter(oraCorrente)) {
                        // Trovato un appuntamento!
                        // Mostra titolo troncato o marcatore simbolico
                        String marcatore = app.getTitolo();
                        if (marcatore.length() > 6)
                            marcatore = marcatore.substring(0, 6);
                        contenutoCella = String.format(" %-6s ", marcatore);
                        break; // Visualizza solo il primo trovato (gestione conflitti semplice)
                    }
                }

                System.out.printf("%s|", contenutoCella);
            }
            System.out.println();
        }
        System.out.println("------+--------+--------+--------+--------+--------+--------+--------+");
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
                        app.getTitolo());
            }
        }
    }
}

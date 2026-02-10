package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public String leggiStringa(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int leggiIntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Errore: Inserisci un numero intero valido.");
            }
        }
    }

    public LocalDate leggiData(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Errore: Formato data non valido. Usa YYYY-MM-DD.");
            }
        }
    }

    public LocalDateTime ottieniDataOraValida(String etichetta) {
        LocalDate oggi = LocalDate.now();
        while (true) {
            try {
                System.out.printf("Data %s (giorno del mese): ", etichetta);
                String inputGiorno = scanner.nextLine();
                if (inputGiorno.trim().isEmpty())
                    continue;

                int giorno = Integer.parseInt(inputGiorno);
                LocalDate data;
                if (giorno < oggi.getDayOfMonth()) {
                    data = oggi.plusMonths(1).withDayOfMonth(giorno);
                } else {
                    data = oggi.withDayOfMonth(giorno);
                }

                System.out.printf("Ora %s (08-17): ", etichetta);
                int ora = Integer.parseInt(scanner.nextLine());
                return LocalDateTime.of(data, LocalTime.of(ora, 0));

            } catch (Exception e) {
                System.out.println("Input non valido: " + e.getMessage());
            }
        }
    }

    public LocalDateTime ottieniDataOraFineValida(LocalDateTime inizio) {
        while (true) {
            try {
                System.out.print("Durata (ore): ");
                int durata = Integer.parseInt(scanner.nextLine());
                if (durata <= 0) {
                    System.out.println("Durata positiva richiesta.");
                    continue;
                }
                return inizio.plusHours(durata);
            } catch (NumberFormatException e) {
                System.out.println("Numero non valido.");
            }
        }
    }

    public void mostraMessaggio(String msg) {
        System.out.println(msg);
    }

    public void mostraErrore(String msg) {
        System.out.println("ERRORE: " + msg);
    }
}

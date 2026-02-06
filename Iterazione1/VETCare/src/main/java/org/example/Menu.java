package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Menu {
    private VetCare controller;
    private Scanner scanner;

    public Menu(VetCare controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- VETCare Menu ---");
            System.out.println("1. Aggiungi Paziente e Proprietario");
            System.out.println("2. Aggiungi Visita");
            System.out.println("3. Visualizza Animali e Proprietari");
            System.out.println("4. Visualizza Visite di un Animale");
            System.out.println("0. Esci");
            System.out.print("Seleziona un'opzione: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    gestisciNuovaAnagrafica();
                    break;
                case "2":
                    gestisciNuovaVisita();
                    break;
                case "3":
                    visualizzaAnimali();
                    break;
                case "4":
                    visualizzaVisiteAnimale();
                    break;
                case "0":
                    running = false;
                    System.out.println("Uscita...");
                    break;
                default:
                    System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    private void gestisciNuovaAnagrafica() {
        System.out.println("\n--- Inserimento Nuova Anagrafica ---");

        // Dati Proprietario
        Proprietario prop = null;
        do {
            System.out.println("Inserisci dati Proprietario:");
            String nomeProp = leggiStringa("Nome: ");
            String cfProp = leggiStringa("Codice Fiscale: ");
            String contattoProp = leggiStringa("Contatto: ");

            controller.inserisciNuovaAnagrafica(nomeProp, cfProp, contattoProp);
            prop = controller.getProprietarioCorrente();
            if (prop == null) {
                System.out.println("dati errati, riprova");
            }
        } while (prop == null);

        // Dati Animale
        Animale animale = null;
        do {
            System.out.println("Inserisci dati Animale:");
            String nomeAnimale = leggiStringa("Nome: ");
            String specie = leggiStringa("Specie: ");
            String razza = leggiStringa("Razza: ");
            int microchip = -111;
            do {
                microchip= leggiIntero("Microchip (numero intero): ");
                if(microchip == -1)return;
                animale = controller.ricercaAnimale(microchip);
                if(animale != null){
                    System.out.println("animale già presente nel sistema");
                    System.out.println("digita -1 per uscire dall'inserimento anagrafica");
                }
            }while(animale != null);
            LocalDate dataNascita = leggiData("Data di Nascita (YYYY-MM-DD): ");

            animale = controller.inserisciNuovoAnimale(nomeAnimale, specie, razza, microchip, dataNascita, prop);
            if (animale == null) {
                System.out.println("dati errati, riprova");
            }
        } while (animale == null);
        System.out.println("Premi 1 per confermare la registrazione, qualsiasi altro tasto per annullare:");
        String conferma = scanner.nextLine();
        if ("1".equals(conferma)) {
            controller.confermaRegistrazione();
            System.out.println("Registrazione completata con successo!");
        } else {
            System.out.println("Registrazione annullata.");
        }
    }

    private void gestisciNuovaVisita() {
        System.out.println("\n--- Inserimento Nuova Visita ---");

        int microchip = leggiIntero("Inserisci Microchip dell'animale: ");
        Animale animale = controller.ricercaAnimale(microchip);

        if (animale == null) {
            System.out.println("Errore: Animale con microchip " + microchip + " non trovato.");
            return;
        }

        System.out.println("Animale trovato: " + animale.getNome() + " (" + animale.getSpecie() + ")");
        do {
            String anamnesi = leggiStringa("Anamnesi: ");
            String esame = leggiStringa("Esame Obbiettivo: ");
            String diagnosi = leggiStringa("Diagnosi: ");

            controller.nuovaVisita(microchip, anamnesi, esame, diagnosi);
            if(controller.getAnimaleCorrente().getCartella().getVisitaCorrente() == null){
                System.out.println("riempi tutti i campi");
            }
        }while(controller.getAnimaleCorrente().getCartella().getVisitaCorrente() == null);
        System.out.println("Premi 1 per confermare la Visita, qualsiasi altro tasto per annullare:");
        String conferma = scanner.nextLine();
        if ("1".equals(conferma)) {
            controller.confermaVisita();
            System.out.println("Visita completata con successo!");
        } else {
            System.out.println("Visita annullata.");
        }
    }

    private void visualizzaAnimali() {
        System.out.println("\n--- Elenco Animali e Proprietari ---");
        for (Animale a : controller.getAnimali()) {
            Proprietario p = a.getProprietario();
            String nomeProp = (p != null) ? p.getNome() : "N/D";
            System.out.println("microchip: " + a.getMicrochip() + ", Nome: " + a.getNome() +
                    ", Specie: " + a.getSpecie() + ", Razza: " + a.getRazza() +
                    " | Proprietario: " + nomeProp);
        }
    }

    private void visualizzaVisiteAnimale() {
        System.out.println("\n--- Visualizza Visite Animale ---");
        int microchip = leggiIntero("Inserisci Microchip dell'animale: ");
        Animale animale = controller.ricercaAnimale(microchip);

        if (animale == null) {
            System.out.println("Errore: Animale non trovato.");
            return;
        }

        System.out.println("Visite per " + animale.getNome() + ":");
        CartellaClinica cartella = animale.getCartella();
        if (cartella != null) {
            java.util.Collection<Visita> visite = cartella.getVisite();
            if (visite.isEmpty()) {
                System.out.println("Nessuna visita registrata.");
            } else {
                for (Visita v : visite) {
                    System.out.println("ID: " + v.getIdVisita() +
                            " | Esame obiettivo: " + v.getEsameObiettivo() +
                            " | Diagnosi: " + v.getDiagnosi() +
                            " | Anamnesi: " + v.getAnamnesi());
                }
            }
        } else {
            System.out.println("Cartella clinica non presente.");
        }
    }

    private String leggiStringa(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int leggiIntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                // Leggiamo la linea e parsiamo per evitare problemi col buffer
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Errore: Inserisci un numero intero valido.");
            }
        }
    }

    private LocalDate leggiData(String prompt) {
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
}

package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            System.out.println("5. Richiedi esami per salvarli nella cartella clinica di un animale");
            System.out.println("6. Visualizza calendario settimanale");
            System.out.println("7. Visualizza agenda 30 giorni");
            System.out.println("8. Aggiungi appuntamento");
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
                case "5":
                    richiediEsami();
                    break;
                case "6":
                    controller.getCalendario().stampaCalendarioGriglia();
                    break;
                case "7":
                    controller.getCalendario().stampaAgenda();
                    break;
                case "8":
                    flussoAggiungiAppuntamento();
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
            String cfProp = leggiStringa("Codice Fiscale: ");
            prop = controller.ricercaProprietari(cfProp);
            if (prop != null) {
                System.out.println("Seleziono Propietario esistente");
                break;
            }
            String nomeProp = leggiStringa("Nome: ");
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
                microchip = leggiIntero("Microchip (numero intero): ");
                if (microchip == -1)
                    return;
                animale = controller.ricercaAnimale(microchip);
                if (animale != null) {
                    System.out.println("animale già presente nel sistema");
                    System.out.println("digita -1 per USCIRE dall'inserimento anagrafica O CORREGGI");
                }
            } while (animale != null);
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
        int microchip = -111;
        Animale animale = null;
        do {
            microchip = leggiIntero("Inserisci Microchip dell'animale: ");
            animale = controller.ricercaAnimale(microchip);

            if (animale == null) {
                System.out.println("Errore: Animale con microchip " + microchip + " non trovato.");
                int input = leggiIntero("voui registrare un nuovo animale? 1 si, altro no: ");
                if (input == 1)
                    gestisciNuovaAnagrafica();
                else
                    return;
            }
        } while (animale == null);

        System.out.println("Animale trovato: " + animale.getNome() + " (" + animale.getSpecie() + ")");
        Visita visita = null;
        do {
            String anamnesi = leggiStringa("Anamnesi: ");
            String esame = leggiStringa("Esame Obbiettivo: ");
            String diagnosi = leggiStringa("Diagnosi: ");

            controller.nuovaVisita(microchip, anamnesi, esame, diagnosi);
            visita = controller.getAnimaleCorrente().getCartella().getVisitaCorrente();
            if (visita == null) {
                System.out.println("riempi tutti i campi");
            }
            // accedi al magazzino per aggiungere farmaci
            aggiungiTerapia(visita);
            // accedi al laboratorio per aggiungere esami
            aggiungiEsame(visita, microchip);
        } while (visita == null);
        System.out.println("Premi 1 per confermare la Visita, qualsiasi altro tasto per annullare:");
        String conferma = scanner.nextLine();
        if ("1".equals(conferma)) {
            controller.confermaVisita();
            System.out.println("Visita completata con successo!");
        } else {
            System.out.println("Visita annullata.");
        }
    }

    private void aggiungiTerapia(Visita visita) {
        int scelta = leggiIntero("vuoi aggiungere una Terapia? (1 per sì, altro per no): ");
        if (scelta != 1) {
            System.out.println("Terapia non aggiunta");
            return;
        }

        boolean find = false;
        Farmaco target = null;
        while (!find) {
            String nomeFarmaco = leggiStringa("Nome del Farmaco (o 'esci' per annullare): ");
            if (nomeFarmaco.equalsIgnoreCase("esci")) {
                System.out.println("Operazione annullata.");
                return;
            }

            if (visita.ricercaFarmaco(nomeFarmaco)) {
                boolean selectionDone = false;
                while (!selectionDone) {

                    int idFarmaco = leggiIntero("Id del Farmaco (-1 per cercare altro nome): ");
                    if (idFarmaco == -1) {
                        selectionDone = true;
                    } else {
                        Farmaco farmaco = null;
                        farmaco = visita.getMagazzino().getFarmacoByid(idFarmaco);
                        if (farmaco == null || !farmaco.getNome().equalsIgnoreCase(nomeFarmaco)) {
                            System.out.println("Farmaco trovato non coincide con il nome inserito.");
                        } else {
                            selectionDone = true;
                            find = true;
                            target = visita.selezionaFarmacoById(idFarmaco);
                        }

                    }
                }
            }
        }

        int posologia = 0;
        do {
            posologia = leggiIntero("Posologia (mg) [>0]: ");
            if (posologia <= 0)
                System.out.println("Inserire un valore positivo.");
        } while (posologia <= 0);

        String frequenza = "";
        do {
            frequenza = leggiStringa("Frequenza (es: ogni 8 ore): ");
            if (frequenza == null || frequenza.trim().isEmpty())
                System.out.println("Inserire una frequenza valida.");
        } while (frequenza == null || frequenza.trim().isEmpty());

        LocalDate dataInizio = null;
        LocalDate dataFine = null;
        boolean dateValide = false;
        while (!dateValide) {
            dataInizio = leggiData("Data Inizio (YYYY-MM-DD): ");
            dataFine = leggiData("Data Fine (YYYY-MM-DD): ");
            if (!dataFine.isBefore(dataInizio)) {
                dateValide = true;
            } else {
                System.out.println("Errore: La data di fine non puo' essere precedente alla data di inizio.");
            }
        }

        visita.creaTerapia(target, posologia, frequenza, dataInizio, dataFine);
        System.out.println("Terapia aggiunta con successo!");
    }

    private void aggiungiEsame(Visita visita, int microchip) {
        int scelta = leggiIntero("vuoi aggiungere un Esame? (1 per sì, altro per no): ");
        if (scelta != 1) {
            System.out.println("Esame non aggiunto");
            return;
        }

        boolean done = false;
        while (!done) {
            String tipoEsame = leggiStringa("Tipo Esame (urine, sangue, completo) o 'esci' per annullare: ");
            if (tipoEsame.equalsIgnoreCase("esci")) {
                System.out.println("Operazione annullata.");
                return;
            }

            int idEsame = visita.richiediEsame(tipoEsame, microchip);
            if (idEsame > 0) {
                System.out.println("Esame aggiunto con successo! ID Esame: " + idEsame);
                done = true;
            } else {
                System.out.println("Errore nell'aggiunta dell'esame. Riprova (forse tipo non valido?).");
            }
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
                    System.out.println(v.stampaTerapia());
                }
            }
        } else {
            System.out.println("Cartella clinica non presente.");
        }
    }

    private void richiediEsami() {
        System.out.println("\n--- Richiedi e Salva Esami ---");
        int microchip = leggiIntero("Inserisci Microchip dell'animale: ");
        Animale animale = controller.ricercaAnimale(microchip);

        if (animale == null) {
            System.out.println("Errore: Animale non trovato.");
            return;
        }

        System.out.println("Richiedi esami per: " + animale.getNome());
        CartellaClinica cartella = animale.getCartella();
        if (cartella != null) {
            java.util.List<Esame> esami = cartella.risultatiEsami(microchip);
            if (esami.isEmpty()) {
                System.out.println("Nessun nuovo esame trovato per questo animale.");
            } else {
                System.out.println("Trovati e salvati " + esami.size() + " esami:");
                for (Esame e : esami) {
                    System.out.println("- Esame ID: " + e.getId() + " | Data: " + e.getData() + " | " + e.getValori());
                }
            }
        } else {
            System.out.println("Errore: Cartella clinica non trovata per questo animale.");
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

    private void flussoAggiungiAppuntamento() {
        try {
            System.out.println("\n--- Aggiungi Appuntamento ---");
            Animale animale = null;
            do{
            int microchip = leggiIntero("Inserisci Microchip dell'animale: ");

            animale = controller.ricercaAnimale(microchip);

            if (animale == null) {
                System.out.println("Errore: Animale non trovato. Registrare prima l'animale.");
                return;
            }
            }while(animale == null);

            String titolo;
            do {
                System.out.print("Titolo: ");
                titolo = scanner.nextLine();
                if (titolo.trim().isEmpty()) {
                    System.out.println("Il titolo non può essere vuoto. Riprova.");
                }
            } while (titolo.trim().isEmpty());
            System.out.print("Descrizione: ");
            String descrizione = scanner.nextLine();

            // Ciclo finché non viene fornito uno slot orario valido
            while (true) {
                // Input semplificato: Richiedi Ora (08-17) solo per imporre vincoli interi
                LocalDateTime inizio = ottieniDataOraValida("Inizio");

                // Per l'orario di fine, richiedi durata in ore
                LocalDateTime fine = ottieniDataOraFineValida(inizio);

                try {
                    controller.getCalendario().aggiungiAppuntamento(animale, titolo, descrizione, inizio, fine);
                    System.out.println("Appuntamento aggiunto con successo!");
                    break; // Esci dal ciclo in caso di successo
                } catch (IllegalArgumentException | SovrapposizioneAppuntamentoException e) {
                    System.out.println("Errore nell'aggiunta dell'appuntamento: " + e.getMessage());
                    System.out.println("Per favore, riprova a inserire l'orario.");
                }
            }

        } catch (Exception e) {
            System.out.println("Errore imprevisto: " + e.getMessage());
        }
    }

    private LocalDateTime ottieniDataOraValida(String etichetta) {
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

                // Imponi minuti = 00
                return LocalDateTime.of(data, LocalTime.of(ora, 0));

            } catch (NumberFormatException | DateTimeParseException e) {
                System.out.println("Formato numero non valido. Riprova.");
            } catch (Exception e) {
                System.out.println("Input non valido: " + e.getMessage());
            }
        }
    }

    private LocalDateTime ottieniDataOraFineValida(LocalDateTime inizio) {
        while (true) {
            try {
                System.out.print("Durata (ore): ");
                int durata = Integer.parseInt(scanner.nextLine());

                if (durata <= 0) {
                    System.out.println("La durata deve essere positiva.");
                    continue;
                }

                return inizio.plusHours(durata);
            } catch (NumberFormatException e) {
                System.out.println("Numero non valido. Inserisci ore intere.");
            }
        }
    }
}

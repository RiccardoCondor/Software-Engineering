package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

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
            System.out.println("9. Aggiungi Operazione");
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
                case "9":
                    flussoAggiungiOperazione();
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
            try {
                System.out.println("Inserisci dati Proprietario:");
                String cfProp = leggiStringa("Codice Fiscale: ");

                prop = controller.ricercaProprietari(cfProp);
                if (prop != null) {
                    System.out.println("Seleziono Propietario esistente");
                    // Impostiamo comunque il corrente nel controller per l'animale successivo
                    controller.inserisciNuovaAnagrafica(prop.getNome(), prop.getCf(), prop.getContatto());
                } else {
                    String nomeProp = leggiStringa("Nome: ");
                    String contattoProp = leggiStringa("Contatto: ");
                    prop = controller.inserisciNuovaAnagrafica(nomeProp, cfProp, contattoProp);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: " + e.getMessage() + ". Riprova.");
            }
        } while (prop == null);

        // Dati Animale
        Animale animale = null;
        do {
            try {
                System.out.println("Inserisci dati Animale:");
                int microchip = leggiIntero("Microchip (numero intero): ");

                animale = controller.ricercaAnimale(microchip);
                if (animale != null) {
                    System.out.println("animale già presente nel sistema");
                    String choice = leggiStringa("digita -1 per USCIRE o invio per riprovare: ");
                    if ("-1".equals(choice))
                        return;
                    animale = null; // forzo loop
                    continue;
                }

                String nomeAnimale = leggiStringa("Nome: ");
                String specie = leggiStringa("Specie: ");
                String razza = leggiStringa("Razza: ");
                LocalDate dataNascita = leggiData("Data di Nascita (YYYY-MM-DD): ");

                animale = controller.inserisciNuovoAnimale(nomeAnimale, specie, razza, microchip, dataNascita,
                        prop.getCf());
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: " + e.getMessage() + ". Riprova.");
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

        Animale animale = null;
        do {
            int microchip = leggiIntero("Inserisci Microchip dell'animale: ");
            animale = controller.ricercaAnimale(microchip);

            if (animale == null) {
                System.out.println("Errore: Animale con microchip " + microchip + " non trovato.");
                int input = leggiIntero("vuoi registrare un nuovo animale? 1 si, altro no: ");
                if (input == 1) {
                    gestisciNuovaAnagrafica();

                } else {
                    return;
                }
            }
        } while (animale == null);

        System.out.println("Animale trovato: " + animale.getNome() + " (" + animale.getSpecie() + ")");

        boolean visitaCreata = false;
        do {
            try {
                String anamnesi = leggiStringa("Anamnesi: ");
                String esame = leggiStringa("Esame Obbiettivo: ");
                String diagnosi = leggiStringa("Diagnosi: ");

                controller.nuovaVisita(animale.getMicrochip(), anamnesi, esame, diagnosi);
                visitaCreata = true;

                aggiungiTerapia();
                aggiungiEsame(animale.getMicrochip());

            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Errore: " + e.getMessage() + ". Riprova.");
            }
        } while (!visitaCreata);

        System.out.println("Premi 1 per confermare la Visita, qualsiasi altro tasto per annullare:");
        String conferma = scanner.nextLine();
        if ("1".equals(conferma)) {
            controller.confermaVisita();
            System.out.println("Visita completata con successo!");
        } else {
            System.out.println("Visita annullata.");
        }
    }

    private void aggiungiTerapia() {
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

            if (controller.cercaFarmaco(nomeFarmaco)) {
                boolean selectionDone = false;
                while (!selectionDone) {

                    int idFarmaco = leggiIntero("Id del Farmaco (-1 per cercare altro nome): ");
                    if (idFarmaco == -1) {
                        selectionDone = true;
                    } else {
                        Farmaco farmaco = controller.getFarmacoById(idFarmaco);
                        if (farmaco == null || !farmaco.getNome().equalsIgnoreCase(nomeFarmaco)) {
                            System.out.println("Farmaco trovato non coincide con il nome inserito.");
                        } else {
                            // Trovato e valido
                            target = farmaco;
                            selectionDone = true;
                            find = true;
                        }

                    }
                }
            } else {
                System.out.println("Farmaco non trovato.");
            }
        }

        // Input parametri terapia
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

        try {
            controller.aggiungiTerapia(target.getNome(), target.getId(), posologia, frequenza, dataInizio, dataFine);
            System.out.println("Terapia aggiunta con successo!");
        } catch (Exception e) {
            System.out.println("Errore creazione terapia: " + e.getMessage());
        }
    }

    private void aggiungiEsame(int microchip) {
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

            try {
                int id = controller.aggiungiEsame(tipoEsame, microchip);
                if (id > 0) {
                    System.out.println("Esame aggiunto con successo! ID Esame: " + id);
                    done = true;
                } else {
                    System.out.println("Errore nell'aggiunta dell'esame. Riprova (forse tipo non valido?).");
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage() + ". Riprova.");
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
            int microchip = leggiIntero("Inserisci Microchip dell'animale: ");
            // Check preventivo
            if (controller.ricercaAnimale(microchip) == null) {
                System.out.println("Errore: Animale non trovato. Registrare prima l'animale.");
                return;
            }

            String titolo = leggiStringa("Titolo: ");
            String descrizione = leggiStringa("Descrizione: ");

            // Ciclo orario
            while (true) {
                LocalDateTime inizio = ottieniDataOraValida("Inizio");
                LocalDateTime fine = ottieniDataOraFineValida(inizio);

                try {
                    controller.aggiungiAppuntamento(microchip, titolo, descrizione, inizio, fine);
                    System.out.println("Appuntamento aggiunto con successo!");
                    break;
                } catch (IllegalArgumentException | SovrapposizioneAppuntamentoException e) {
                    System.out.println("Errore: " + e.getMessage());
                    System.out.println("Riprova inserimento orario.");
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
                return LocalDateTime.of(data, LocalTime.of(ora, 0));

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
                    System.out.println("Durata positiva richiesta.");
                    continue;
                }
                return inizio.plusHours(durata);
            } catch (NumberFormatException e) {
                System.out.println("Numero non valido.");
            }
        }
    }

    private void flussoAggiungiOperazione() {
        try {
            System.out.println("\n--- Aggiungi Operazione ---");
            int microchip = leggiIntero("Inserisci Microchip dell'animale: ");
            if (controller.ricercaAnimale(microchip) == null) {
                System.out.println("Errore: Animale non trovato. Registrare prima l'animale.");
                return;
            }

            String titolo = leggiStringa("Titolo Operazione: ");
            String descrizione = leggiStringa("Descrizione: ");
            String tipo = leggiStringa("Tipo Operazione: ");

            // Ciclo orario
            Operazione op = null;
            while (true) {
                LocalDateTime inizio = ottieniDataOraValida("Inizio");
                LocalDateTime fine = ottieniDataOraFineValida(inizio);
                try {
                    op = controller.creaOperazione(microchip, titolo, descrizione, inizio, fine, tipo);
                    System.out.println("Operazione creata con successo!");
                    break;
                } catch (IllegalArgumentException | SovrapposizioneAppuntamentoException e) {
                   System.out.println("Errore: " + e.getMessage());
                   System.out.println("Riprova inserimento orario.");
                }
            }

            // Aggiungi Membri
            Set<Integer> membriAggiunti = new HashSet<>();
            
            while (true) {
                System.out.println("\nGestione Equipe per l'operazione:");
                System.out.println("Membri disponibili:");
                
                boolean disponibili = false;
                for (MembroEquipe m : controller.getMembri()) {
                    if (!membriAggiunti.contains(m.getIdmembro())) {
                        System.out.println(m);
                        disponibili = true;
                    }
                }
                
                if (!disponibili) {
                    System.out.println("Tutti i membri sono stati aggiunti.");
                    break;
                }

                String scelta = leggiStringa("Vuoi aggiungere un membro? (s/n): ");
                if (!scelta.equalsIgnoreCase("s")) break;

                int idMembro = leggiIntero("ID Membro: ");
                
                if (membriAggiunti.contains(idMembro)) {
                    System.out.println("Membro già aggiunto.");
                    continue;
                }
                
                try {
                    controller.aggiungiMembroAOperazione(op, idMembro);
                    membriAggiunti.add(idMembro);
                    System.out.println("Membro aggiunto.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Errore: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Errore imprevisto: " + e.getMessage());
        }
    }
}

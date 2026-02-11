package org.example;

import org.example.exceptions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VetCare {
    private Map<String, Proprietario> proprietari;
    private Map<Integer, Animale> animali;
    private Proprietario proprietarioCorrente;
    private Animale animaleCorrente;
    private Map<Integer, MembroEquipe> membri;
    private Calendario calendario;
    private static VetCare instance;
    private ConsoleUI ui;
    private Magazzino magazzino;
    private Laboratorio laboratorio;

    public static VetCare getInstance() {
        if (instance == null) {
            instance = new VetCare();
        }
        return instance;
    }

    private VetCare() {
        proprietari = new HashMap<>();
        animali = new HashMap<>();
        membri = new HashMap<>();
        proprietarioCorrente = null;
        animaleCorrente = null;
        this.calendario = new Calendario();
        this.ui = new ConsoleUI();
        this.magazzino = Magazzino.getInstance();
        this.laboratorio = Laboratorio.getInstance();

        membri.put(1, new Anestetista(1, "Dr. Rossi"));
        membri.put(2, new Infermiere(2, "Inf. Bianchi"));
    }

    public java.util.Collection<Proprietario> getProprietari() {
        return java.util.Collections.unmodifiableCollection(proprietari.values());
    }

    public java.util.Collection<Animale> getAnimali() {
        return java.util.Collections.unmodifiableCollection(animali.values());
    }

    public Animale getAnimaleCorrente() {
        return animaleCorrente;
    }

    public Proprietario getProprietarioCorrente() {
        return proprietarioCorrente;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public Animale ricercaAnimale(int microchip) {
        return animali.get(microchip);
    }

    public Proprietario ricercaProprietari(String CF) {
        return proprietari.get(CF);
    }

    public Proprietario inserisciNuovaAnagrafica(String nome, String cf, String contatto) {
        if (nome == null || cf == null || contatto == null || nome.trim().isEmpty() || cf.trim().isEmpty()
                || contatto.trim().isEmpty()) {
            throw new IllegalArgumentException("Dati proprietario incompleti.");
        }
        if (proprietari.containsKey(cf)) {
            ui.mostraMessaggio("Proprietario esistente.");
            proprietarioCorrente = proprietari.get(cf);
            return proprietarioCorrente; // Ritorna esistente se trovato
        }
        proprietarioCorrente = new Proprietario(nome, cf, contatto);
        return proprietarioCorrente;
    }

    public Animale inserisciNuovoAnimale(String nome, String specie, String razza, int microchip, LocalDate dataNascita,
            String cfProprietario) {
        if (microchip <= 0) {
            throw new IllegalArgumentException("Microchip non valido");
        }
        if (animali.containsKey(microchip)) {
            throw new IllegalArgumentException("Animale già presente con questo microchip");
        }
        Proprietario prop = proprietarioCorrente;
        if (prop == null) {
            throw new IllegalArgumentException("Proprietario non trovato per CF: " + cfProprietario);
        }
        if (nome == null || specie == null || razza == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Dati animale incompleti");
        }

        Animale nuovo = new Animale(nome, specie, razza, microchip, dataNascita, prop);
        animaleCorrente = nuovo;
        return nuovo;
    }

    public void confermaRegistrazione() {
        if (proprietarioCorrente != null && !proprietari.containsKey(proprietarioCorrente.getCf())) {
            proprietari.put(proprietarioCorrente.getCf(), proprietarioCorrente);
        }
        if (animaleCorrente != null && !animali.containsKey(animaleCorrente.getMicrochip())) {
            animali.put(animaleCorrente.getMicrochip(), animaleCorrente);
        }
    }

    public void nuovaVisita(int microchip, String anamnesi, String esameObiettivo, String diagnosi) {
        if (!animali.containsKey(microchip)) {
            throw new IllegalArgumentException("Animale non trovato per microchip: " + microchip);
        }
        animaleCorrente = animali.get(microchip);
        if (anamnesi == null || esameObiettivo == null || diagnosi == null || anamnesi.isEmpty()
                || esameObiettivo.isEmpty() || diagnosi.isEmpty()) {
            throw new IllegalArgumentException("Dati visita incompleti");
        }
        animaleCorrente.getCartella().nuovaVisita(anamnesi, esameObiettivo, diagnosi);
    }

    public void aggiungiTerapia(String nomeFarmaco, int idFarmaco, int posologia, String frequenza, LocalDate inizio,
            LocalDate fine) {
        if (animaleCorrente == null || !animaleCorrente.getCartella().haVisitaInCorso()) {
            throw new IllegalStateException("Nessuna visita in corso per aggiungere terapia");
        }

        // Refactoring: Access Magazzino directly
        Farmaco f = this.magazzino.selezionaFarmacoById(idFarmaco);
        if (f == null || !f.getNome().equalsIgnoreCase(nomeFarmaco)) {
            throw new IllegalArgumentException("Farmaco non trovato o non corrispondente");
        }

        // Refactoring: Access Visita directly
        Visita visita = animaleCorrente.getCartella().getVisitaCorrente();
        if (visita != null) {
            visita.creaTerapia(f, posologia, frequenza, inizio, fine);
        }
    }

    public java.util.List<Esame> sincronizzaEsami(Animale animale) {
        if (animale == null || animale.getCartella() == null)
            return new java.util.ArrayList<>();

        java.util.List<Esame> nuoviEsami = laboratorio.risultatiEsame(animale.getMicrochip());
        if (nuoviEsami != null && !nuoviEsami.isEmpty()) {
            animale.getCartella().confermaEsami(nuoviEsami);
        }
        return nuoviEsami;
    }

    private void gestisciRichiestaEsame() {

        int scelta = ui.leggiIntero("vuoi aggiungere un Esame? (1 per sì, altro per no): ");
        if (scelta != 1) {
            System.out.println("Esame non aggiunto");
            return;
        }

        if (animaleCorrente == null || !animaleCorrente.getCartella().haVisitaInCorso()) {
            System.out.println("Nessuna visita in corso.");
            return;
        }
        int idEsame = -1;
        do {
            String tipoEsame = ui.leggiStringa("Tipo Esame (urine, sangue, completo) o 'esci' per annullare: ");
            if (tipoEsame.equalsIgnoreCase("esci")) {
                System.out.println("Operazione annullata.");
                return;
            }

            idEsame = laboratorio.produciesame(tipoEsame, animaleCorrente.getMicrochip());
            if (idEsame > 0) {
                animaleCorrente.getCartella().getVisitaCorrente().setIdEsame(idEsame);
                System.out.println("Esame richiesto con successo. ID: " + idEsame);
            } else {
                System.out.println("Errore nella richiesta esame. Tipo non valido?");
            }
        } while (idEsame == -1);
    }

    public void confermaVisita() {
        if (animaleCorrente != null && animaleCorrente.getCartella().haVisitaInCorso()) {
            animaleCorrente.getCartella().confermaVisita();
        } else {
            throw new IllegalStateException("Nessuna visita da confermare");
        }
    }

    public void aggiungiAppuntamento(int microchip, String titolo, String descrizione, java.time.LocalDateTime inizio,
            java.time.LocalDateTime fine) {
        Animale a = animali.get(microchip);
        if (a == null)
            throw new IllegalArgumentException("Animale non trovato");

        calendario.aggiungiAppuntamento(a, titolo, descrizione, inizio, fine);
    }

    public void checkDisponibilita(LocalDateTime inizio, LocalDateTime fine) {
        calendario.checkDisponibilita(inizio, fine);
    }

    // Gestione Membri Equipe
    public void aggiungiMembroEquipe(MembroEquipe m) {
        if (m != null && !membri.containsKey(m.getIdmembro())) {
            membri.put(m.getIdmembro(), m);
        }
    }

    public MembroEquipe getMembro(int id) {
        return membri.get(id);
    }

    public java.util.Collection<MembroEquipe> getMembri() {
        return java.util.Collections.unmodifiableCollection(membri.values());
    }

    // Creazione Operazione
    public Operazione creaOperazione(int microchip, String titolo, String descrizione, LocalDateTime inizio,
            LocalDateTime fine, String tipo, Map<Integer, MembroEquipe> membri) {
        Animale a = animali.get(microchip);
        if (a == null) {
            throw new IllegalArgumentException("Animale non trovato per microchip: " + microchip);
        }

        Operazione op = new Operazione(a, titolo, descrizione, inizio, fine, tipo, membri);

        calendario.aggiungiAppuntamento(op);
        return op;
    }

    // --- MENU INTEGRATION START ---

    public void start() {
        boolean running = true;
        while (running) {
            ui.mostraMessaggio("\n--- VETCare Menu ---");
            ui.mostraMessaggio("1. Aggiungi Paziente e Proprietario");
            ui.mostraMessaggio("2. Aggiungi Visita");
            ui.mostraMessaggio("3. Visualizza Animali e Proprietari");
            ui.mostraMessaggio("4. Visualizza Visite di un Animale");
            ui.mostraMessaggio("5. Richiedi esami per salvarli nella cartella clinica di un animale");
            ui.mostraMessaggio("6. Visualizza calendario settimanale");
            ui.mostraMessaggio("7. Visualizza agenda 30 giorni");
            ui.mostraMessaggio("8. Aggiungi appuntamento");
            ui.mostraMessaggio("9. Aggiungi Operazione");
            ui.mostraMessaggio("10. Gestisci Inventario");
            ui.mostraMessaggio("0. Esci");

            String input = ui.leggiStringa("Seleziona un'opzione: ");

            try {
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
                        risultatiEsami();
                        break;
                    case "6":
                        this.getCalendario().stampaCalendarioGriglia();
                        break;
                    case "7":
                        this.getCalendario().stampaAgenda();
                        break;
                    case "8":
                        flussoAggiungiAppuntamento();
                        break;
                    case "9":
                        flussoAggiungiOperazione();
                        break;
                    case "10":
                        gestisciInventario();
                        break;
                    case "0":
                        running = false;
                        ui.mostraMessaggio("Uscita...");
                        break;
                    default:
                        ui.mostraErrore("Opzione non valida. Riprova.");
                }
            } catch (Exception e) {
                ui.mostraErrore("Si è verificato un errore imprevisto: " + e.getMessage());
                e.printStackTrace();
                ui.mostraMessaggio("Premi Invio per continuare...");
                ui.leggiStringa("");
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
                String cfProp = ui.leggiStringa("Codice Fiscale: ");

                prop = this.ricercaProprietari(cfProp);
                if (prop != null) {
                    System.out.println("Seleziono Propietario esistente");
                    // Impostiamo comunque il corrente nel controller per l'animale successivo
                    this.inserisciNuovaAnagrafica(prop.getNome(), prop.getCf(), prop.getContatto());
                } else {
                    String nomeProp = ui.leggiStringa("Nome: ");
                    String contattoProp = ui.leggiStringa("Contatto: ");
                    prop = this.inserisciNuovaAnagrafica(nomeProp, cfProp, contattoProp);
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
                int microchip = ui.leggiIntero("Microchip (numero intero): ");

                animale = this.ricercaAnimale(microchip);
                if (animale != null) {
                    System.out.println("animale già presente nel sistema");
                    String choice = ui.leggiStringa("digita -1 per USCIRE o invio per riprovare: ");
                    if ("-1".equals(choice))
                        return;
                    animale = null; // forzo loop
                    continue;
                }

                String nomeAnimale = ui.leggiStringa("Nome: ");
                String specie = ui.leggiStringa("Specie: ");
                String razza = ui.leggiStringa("Razza: ");
                LocalDate dataNascita = ui.leggiData("Data di Nascita (YYYY-MM-DD): ");

                animale = this.inserisciNuovoAnimale(nomeAnimale, specie, razza, microchip, dataNascita,
                        prop.getCf());
            } catch (IllegalArgumentException e) {
                System.out.println("Errore: " + e.getMessage() + ". Riprova.");
            }
        } while (animale == null);

        System.out.println("Premi 1 per confermare la registrazione, qualsiasi altro tasto per annullare:");
        String conferma = ui.leggiStringa("");
        if ("1".equals(conferma)) {
            this.confermaRegistrazione();
            System.out.println("Registrazione completata con successo!");
        } else {
            System.out.println("Registrazione annullata.");
        }
    }

    private void gestisciNuovaVisita() {
        System.out.println("\n--- Inserimento Nuova Visita ---");

        Animale animale = null;
        do {
            int microchip = ui.leggiIntero("Inserisci Microchip dell'animale: ");
            animale = this.ricercaAnimale(microchip);

            if (animale == null) {
                System.out.println("Errore: Animale con microchip " + microchip + " non trovato.");
                int input = ui.leggiIntero("vuoi registrare un nuovo animale? 1 si, altro no: ");
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
                String anamnesi = ui.leggiStringa("Anamnesi: ");
                String esame = ui.leggiStringa("Esame Obbiettivo: ");
                String diagnosi = ui.leggiStringa("Diagnosi: ");

                this.nuovaVisita(animale.getMicrochip(), anamnesi, esame, diagnosi);
                visitaCreata = true;

                gestisciInputTerapia();
                gestisciRichiestaEsame();

            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Errore: " + e.getMessage() + ". Riprova.");
            }
        } while (!visitaCreata);

        System.out.println("Premi 1 per confermare la Visita, qualsiasi altro tasto per annullare:");
        String conferma = ui.leggiStringa("");
        if ("1".equals(conferma)) {
            this.confermaVisita();
            System.out.println("Visita completata con successo!");
        } else {
            System.out.println("Visita annullata.");
        }
    }

    private void gestisciInputTerapia() {
        int scelta = ui.leggiIntero("vuoi aggiungere una Terapia? (1 per sì, altro per no): ");
        if (scelta != 1) {
            System.out.println("Terapia non aggiunta");
            return;
        }

        boolean find = false;
        Farmaco target = null;
        while (!find) {
            String nomeFarmaco = ui.leggiStringa("Nome del Farmaco (o 'esci' per annullare): ");
            if (nomeFarmaco.equalsIgnoreCase("esci")) {
                System.out.println("Operazione annullata.");
                return;
            }

            if (this.magazzino.ricercaFarmaci(nomeFarmaco)) {
                boolean selectionDone = false;
                while (!selectionDone) {
                    int idFarmaco = ui.leggiIntero("Id del Farmaco (-1 per cercare altro nome): ");
                    if (idFarmaco == -1) {
                        selectionDone = true;
                    } else {
                        try {
                            Farmaco farmaco = this.magazzino.getFarmacoByid(idFarmaco);
                            if (farmaco != null && farmaco.getNome().equalsIgnoreCase(nomeFarmaco)) {
                                target = farmaco;
                                selectionDone = true;
                                find = true;
                            } else {
                                System.out.println("Farmaco non valido o nome non corrispondente.");
                            }
                        } catch (EntitaNonTrovataException | FarmacoScadutoException
                                | FarmacoNonDisponibileException e) {
                            System.out.println("Errore: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Errore imprevisto nella selezione del farmaco: " + e.getMessage());
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
            posologia = ui.leggiIntero("Posologia (mg) [>0]: ");
            if (posologia <= 0)
                System.out.println("Inserire un valore positivo.");
        } while (posologia <= 0);

        String frequenza = "";
        do {
            frequenza = ui.leggiStringa("Frequenza (es: ogni 8 ore): ");
            if (frequenza == null || frequenza.trim().isEmpty())
                System.out.println("Inserire una frequenza valida.");
        } while (frequenza == null || frequenza.trim().isEmpty());

        LocalDate dataInizio = null;
        LocalDate dataFine = null;

        boolean dateValide = false;
        while (!dateValide) {
            dataInizio = ui.leggiData("Data Inizio (YYYY-MM-DD): ");
            dataFine = ui.leggiData("Data Fine (YYYY-MM-DD): ");
            if (!dataFine.isBefore(dataInizio)) {
                dateValide = true;
            } else {
                System.out.println("Errore: La data di fine non puo' essere precedente alla data di inizio.");
            }
        }

        try {
            this.aggiungiTerapia(target.getNome(), target.getId(), posologia, frequenza, dataInizio, dataFine);
            System.out.println("Terapia aggiunta con successo!");
        } catch (Exception e) {
            System.out.println("Errore creazione terapia: " + e.getMessage());
        }
    }

    private void visualizzaAnimali() {
        System.out.println("\n--- Elenco Animali e Proprietari ---");
        for (Animale a : this.getAnimali()) {
            Proprietario p = a.getProprietario();
            String nomeProp = (p != null) ? p.getNome() : "N/D";
            System.out.println("microchip: " + a.getMicrochip() + ", Nome: " + a.getNome() +
                    ", Specie: " + a.getSpecie() + ", Razza: " + a.getRazza() +
                    " | Proprietario: " + nomeProp);
        }
    }

    private void visualizzaVisiteAnimale() {
        System.out.println("\n--- Visualizza Visite Animale ---");
        int microchip = ui.leggiIntero("Inserisci Microchip dell'animale: ");
        Animale animale = this.ricercaAnimale(microchip);

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

    private void risultatiEsami() {
        System.out.println("\n--- Richiedi e Salva Esami ---");
        int microchip = ui.leggiIntero("Inserisci Microchip dell'animale: ");

        Animale animale = this.ricercaAnimale(microchip);
        if (animale == null) {
            System.out.println("Errore: Animale non trovato.");
            return;
        }

        System.out.println("Richiedi esami per: " + animale.getNome());
        CartellaClinica cartella = animale.getCartella();
        if (cartella != null) {
            java.util.List<Esame> esami = this.sincronizzaEsami(animale);
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

    private void flussoAggiungiAppuntamento() {
        try {
            System.out.println("\n--- Aggiungi Appuntamento ---");
            int microchip = ui.leggiIntero("Inserisci Microchip dell'animale: ");
            // Check preventivo
            if (this.ricercaAnimale(microchip) == null) {
                System.out.println("Errore: Animale non trovato. Registrare prima l'animale.");
                return;
            }

            String titolo = ui.leggiStringa("Titolo: ");
            String descrizione = ui.leggiStringa("Descrizione: ");

            // Ciclo orario
            while (true) {
                LocalDateTime inizio = ui.ottieniDataOraValida("Inizio");
                LocalDateTime fine = ui.ottieniDataOraFineValida(inizio);

                try {
                    this.aggiungiAppuntamento(microchip, titolo, descrizione, inizio, fine);
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

    private void flussoAggiungiOperazione() {
        try {
            System.out.println("\n--- Aggiungi Operazione ---");
            int microchip = ui.leggiIntero("Inserisci Microchip dell'animale: ");
            if (this.ricercaAnimale(microchip) == null) {
                System.out.println("Errore: Animale non trovato. Registrate prima l'animale.");
                return;
            }

            String titolo = ui.leggiStringa("Titolo Operazione: ");
            String descrizione = ui.leggiStringa("Descrizione: ");
            String tipo = ui.leggiStringa("Tipo Operazione: ");

            LocalDateTime inizio = null;
            LocalDateTime fine = null;

            // Ciclo orario (PRIMA dei membri)
            while (true) {
                inizio = ui.ottieniDataOraValida("Inizio");
                fine = ui.ottieniDataOraFineValida(inizio);
                try {
                    // Controlla disponibilità
                    this.checkDisponibilita(inizio, fine);
                    break; // Se non lancia eccezione, orario è valido
                } catch (IllegalArgumentException | SovrapposizioneAppuntamentoException e) {
                    System.out.println("Errore: " + e.getMessage());
                    System.out.println("Riprova inserimento orario.");
                }
            }

            // Aggiungi Membri (DOPO aver validato tutto il resto)
            Map<Integer, MembroEquipe> membriSelezionati = gestisciSelezioneMembri();
            if (membriSelezionati == null) {
                System.out.println("Operazione annullata durante la selezione dei membri.");
                return;
            }

            // Creazione Operazione (Alla fine)
            try {
                Operazione op = this.creaOperazione(microchip, titolo, descrizione, inizio, fine, tipo,
                        membriSelezionati);
                System.out.println("Operazione creata con successo!");
                System.out.println(op);
            } catch (Exception e) {
                System.out.println("Errore finale creazione: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Errore imprevisto: " + e.getMessage());
        }
    }

    private Map<Integer, MembroEquipe> gestisciSelezioneMembri() {
        Map<Integer, MembroEquipe> membriSelezionati = new java.util.HashMap<>();
        Set<Integer> membriAggiuntiIds = new HashSet<>();

        while (true) {
            System.out.println("\nGestione Equipe per l'operazione:");
            System.out.println("Membri disponibili:");

            boolean disponibili = false;
            for (MembroEquipe m : this.getMembri()) {
                if (!membriAggiuntiIds.contains(m.getIdmembro())) {
                    System.out.println(m);
                    disponibili = true;
                }
            }

            if (!disponibili && membriSelezionati.isEmpty()) {
                System.out.println("Nessun membro disponibile da aggiungere.");
                return null;
            } else if (!disponibili) {
                System.out.println("Tutti i membri disponibili sono stati aggiunti.");
                break;
            }

            // Loop convalida risposta s/n
            String scelta = "";
            while (true) {
                scelta = ui.leggiStringa("Vuoi aggiungere un membro? (s/n): ");
                if (scelta.equalsIgnoreCase("s") || scelta.equalsIgnoreCase("n")) {
                    break;
                }
                System.out.println("Input non valido, per favore inserisci 's' o 'n'.");
            }

            if (scelta.equalsIgnoreCase("n")) {
                if (membriSelezionati.isEmpty()) {
                    System.out.println("Attenzione: Nessun membro selezionato per l'operazione.");
                    String conf = ui.leggiStringa("Vuoi davvero procedere senza membri? (s/n): ");
                    if (conf.equalsIgnoreCase("s"))
                        break;
                    else
                        return null; // Annulla tutto? O continua loop? Facciamo continue loop.
                    // Anzi, se annulla selezione membri, annulla operazione.
                } else {
                    break;
                }
            }

            if (scelta.equalsIgnoreCase("n"))
                break; // Double check break

            int idMembro = ui.leggiIntero("ID Membro: ");

            if (membriAggiuntiIds.contains(idMembro)) {
                System.out.println("Membro già aggiunto.");
                continue;
            }

            MembroEquipe m = this.getMembro(idMembro);
            if (m != null) {
                membriSelezionati.put(idMembro, m);
                membriAggiuntiIds.add(idMembro);
                System.out.println("Membro aggiunto alla lista provvisoria.");
            } else {
                System.out.println("Membro non trovato.");
            }
        }
        return membriSelezionati;
    }

    private void gestisciInventario() {
        this.magazzino.controllaScorte();
        System.out.println("\n--- Inserimento Ordini ---");
        while (true) {
            System.out.println("Inserisci ordini (Quantità -1 per terminare)");
            String nome = ui.leggiStringa("Nome Farmaco: ");
            int quantita = ui.leggiIntero("Quantità desiderata: ");

            if (quantita == -1) {
                break;
            }

            this.magazzino.inserisciOrdine(nome, quantita);
        }
        System.out.println("Gestione inventario completata.");
    }
}

package org.example;

import java.time.LocalDateTime;

public class VerifyOperation {
    public static void main(String[] args) {
        System.out.println("Verifica Iniziata");
        VetCare v = VetCare.getInstance();
        
        // 1. Verifica Membri Iniziali
        System.out.println("Membri presenti: " + v.getMembri().size());
        if (v.getMembri().size() != 2) {
            System.err.println("ERRORE: Membri non inizializzati correttamente");
            System.exit(1);
        }

        // 2. Setup Animale
        v.inserisciNuovaAnagrafica("TestProp", "CFTEST", "123");
        v.confermaRegistrazione();
        v.inserisciNuovoAnimale("Fido", "Cane", "Misto", 999, java.time.LocalDate.now(), "CFTEST");
        v.confermaRegistrazione();
        
        // 3. Crea Operazione
        LocalDateTime inizio = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime fine = inizio.plusHours(2);
        
        try {
            Operazione op = v.creaOperazione(999, "Op Test", "Descrizione Test", inizio, fine, "Chirurgia");
            System.out.println("Operazione creata: " + op);
            
            // 4. Aggiungi membri
            v.aggiungiMembroAOperazione(op, 1); // Dr. Rossi
            v.aggiungiMembroAOperazione(op, 2); // Inf. Bianchi
            
            System.out.println("Membri aggiunti all'operazione.");
            System.out.println(op);
            
            if (!op.toString().contains("Dr. Rossi") || !op.toString().contains("Inf. Bianchi")) {
                 System.err.println("ERRORE: Membri non presenti nella stringa dell'operazione");
                 System.exit(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("Verifica Completata con Successo");
    }
}

package org.example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Operazione extends Appuntamento {
    private String tipoOperazione;
    private Map<Integer, MembroEquipe> membri;

    public Operazione(Animale animale, String titolo, String descrizione, LocalDateTime inizio, LocalDateTime fine,
            String tipo) {
        super(animale, titolo, descrizione, inizio, fine);
        this.tipoOperazione = tipo;
        membri = new HashMap<>();
    }

    public String getTipoOperazione() {
        return tipoOperazione;
    }

    @Override
    public String toString() {
        String vet = "{ \n";
        for (MembroEquipe m : membri.values()) {
            vet = vet + m.toString() + ",\n";
        }
        return "Operazione{" +
                "tipo operazione: " + tipoOperazione +
                vet
                + '}';
    }
}

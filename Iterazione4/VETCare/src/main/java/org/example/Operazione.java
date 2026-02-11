package org.example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Operazione extends Appuntamento {
    private String tipoOperazione;
    private Map<Integer, MembroEquipe> membri;

    public Operazione(Animale animale, String titolo, String descrizione, LocalDateTime inizio, LocalDateTime fine,
            String tipo, Map<Integer, MembroEquipe> uomini) {
        super(animale, titolo, descrizione, inizio, fine);
        this.tipoOperazione = tipo;
        this.membri = uomini;
    }

    public void addMembro(MembroEquipe m) {
        if (m != null) {
            membri.put(m.getIdmembro(), m);
        }
    }

    public String getTipoOperazione() {
        return tipoOperazione;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Operazione{tipo operazione: ").append(tipoOperazione).append(", membri: [");

        java.util.Iterator<MembroEquipe> it = membri.values().iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}

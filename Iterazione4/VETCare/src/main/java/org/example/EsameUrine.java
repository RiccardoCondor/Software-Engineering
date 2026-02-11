package org.example;

import java.time.LocalDate;

public class EsameUrine extends Esame {

    public EsameUrine(LocalDate data, int id, String valori, int microchip) {
        super(data, id, valori, microchip);
    }

    @Override
    public String toString() {
        return "EsameUrine{" +
                "id=" + id +
                ", data=" + data +
                ", valori='" + valori + '\'' +
                ", microchip=" + microchip +
                '}';
    }
}

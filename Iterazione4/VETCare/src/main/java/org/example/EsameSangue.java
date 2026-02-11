package org.example;

import java.time.LocalDate;

public class EsameSangue extends Esame {

    public EsameSangue(LocalDate data, int id, String valori, int microchip) {
        super(data, id, valori, microchip);
    }

    @Override
    public String toString() {
        return "EsameSangue{" +
                "id=" + id +
                ", data=" + data +
                ", valori='" + valori + '\'' +
                ", microchip='" + microchip + '\'' +
                '}';
    }
}

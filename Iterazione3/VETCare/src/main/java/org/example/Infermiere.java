package org.example;

public class Infermiere extends MembroEquipe {
    public Infermiere(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String toString() {
        return "Infermiere: " + getNome() + " | ID: " + getIdmembro();
    }
}

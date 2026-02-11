package org.example;

public class Anestetista extends MembroEquipe {
    public Anestetista(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String toString() {
        return "Anestetista: " + getNome() + " | ID: " + getIdmembro();
    }
}

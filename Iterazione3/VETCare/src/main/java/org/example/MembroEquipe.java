package org.example;

public class MembroEquipe {
    private int id;
    private String nome;
    public MembroEquipe(int id, String nome){
        this.id = id;
        this.nome = nome;

    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Membro equipe: "+nome+" | ID: "+id;
    }
}

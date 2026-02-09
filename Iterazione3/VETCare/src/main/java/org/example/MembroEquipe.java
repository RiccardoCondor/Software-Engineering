package org.example;

public class MembroEquipe {
    private int idmembro;
    private String nome;
    public MembroEquipe(int idmembro, String nome){
        this.idmembro = idmembro;
        this.nome = nome;

    }

    public int getIdmembro() {
        return idmembro;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Membro equipe: "+nome+" | ID: "+idmembro;
    }
}

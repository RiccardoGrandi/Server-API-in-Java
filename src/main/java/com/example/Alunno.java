package com.example;
import java.util.Date;


public class Alunno {
    public String nome;
    public String cognome;
    public Date dataDiNascita;

    public Alunno() {
    }

    public Alunno(String nome, String cognome, Date dataDiNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataDiNascita() {
        return this.dataDiNascita;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }
    
    public String toString(){
        return "nome: " + nome + "\tcognome: "+ cognome + "\tdata: " + dataDiNascita;
    }
}
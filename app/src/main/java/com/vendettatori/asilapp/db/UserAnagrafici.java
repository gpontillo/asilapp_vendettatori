package com.vendettatori.asilapp.db;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserAnagrafici {
    private String nome;
    private String cognome;
    private Timestamp dataNascita;
    private String luogoNascita;
    private String indirizzo;

    public UserAnagrafici(String nome, String cognome, Timestamp dataNascita, String luogoNascita, String indirizzo) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.indirizzo = indirizzo;
    }

    // Default user for anonymous users
    public UserAnagrafici() {
        this.nome = "Mario";
        this.cognome = "Rossi";
        this.dataNascita = new Timestamp(new Date());
        this.luogoNascita = "Roma";
        this.indirizzo = "Via Appia 1, Roma";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Timestamp getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Timestamp dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
}

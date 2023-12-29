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
    private String telefono;
    private Timestamp dataNascita;
    private String luogoNascita;
    private String indirizzo;

    public UserAnagrafici(String nome, String cognome, String telefono, Timestamp dataNascita, String luogoNascita, String indirizzo) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.indirizzo = indirizzo;
    }

    // Default user for anonymous users
    public UserAnagrafici() {
        this.nome = "Mario";
        this.cognome = "Rossi";
        this.dataNascita = new Timestamp(new Date());
        this.telefono = "1234567890";
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
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

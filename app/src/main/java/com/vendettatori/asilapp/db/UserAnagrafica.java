package com.vendettatori.asilapp.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

public class UserAnagrafica implements Serializable, Parcelable, Cloneable {
    private String nome;
    private String cognome;
    private String telefono;
    private Timestamp dataNascita;
    private String luogoNascita;
    private String indirizzo;
    private float peso;
    private float altezza;

    public UserAnagrafica(String nome, String cognome, String telefono, Timestamp dataNascita, String luogoNascita, String indirizzo, float peso, float altezza) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.indirizzo = indirizzo;
        this.peso = peso;
        this.altezza = altezza;
    }

    // Default user for anonymous users
    public UserAnagrafica() {
        this.nome = "Mario";
        this.cognome = "Rossi";
        this.dataNascita = new Timestamp(new Date());
        this.telefono = "1234567890";
        this.luogoNascita = "Roma";
        this.indirizzo = "Via Appia 1, Roma";
        this.peso = 70;
        this.altezza = 170;
    }

    protected UserAnagrafica(Parcel in) {
        nome = in.readString();
        cognome = in.readString();
        telefono = in.readString();
        dataNascita = in.readParcelable(Timestamp.class.getClassLoader());
        luogoNascita = in.readString();
        indirizzo = in.readString();
        peso = in.readFloat();
        altezza = in.readFloat();
    }

    public static final Creator<UserAnagrafica> CREATOR = new Creator<UserAnagrafica>() {
        @Override
        public UserAnagrafica createFromParcel(Parcel in) {
            return new UserAnagrafica(in);
        }

        @Override
        public UserAnagrafica[] newArray(int size) {
            return new UserAnagrafica[size];
        }
    };

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

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltezza() {
        return altezza;
    }

    public void setAltezza(float altezza) {
        this.altezza = altezza;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(this.cognome);
        out.writeObject(this.nome);
        out.writeObject(this.telefono);
        out.writeObject(this.dataNascita.toDate());
        out.writeObject(this.luogoNascita);
        out.writeObject(this.indirizzo);
        out.writeFloat(this.peso);
        out.writeFloat(this.altezza);
        out.flush();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.cognome = (String) in.readObject();
        this.nome = (String) in.readObject();
        this.telefono = (String) in.readObject();
        this.dataNascita = new Timestamp((Date) in.readObject());
        this.luogoNascita = (String) in.readObject();
        this.indirizzo = (String) in.readObject();
        this.peso = in.readFloat();
        this.altezza = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeString(telefono);
        dest.writeParcelable(dataNascita, flags);
        dest.writeString(luogoNascita);
        dest.writeString(indirizzo);
        dest.writeFloat(peso);
        dest.writeFloat(altezza);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public UserAnagrafica cloneUser() {
        try {
            return (UserAnagrafica) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

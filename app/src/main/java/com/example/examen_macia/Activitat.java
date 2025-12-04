package com.example.examen_macia;

import java.io.Serializable;

public class Activitat implements Serializable {
    private String nom;
    private String descripcio;
    private int iconResId;
    private String durada;
    private String intensitat;

    public Activitat(String nom, String descripcio, int iconResId, String durada, String intensitat) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.iconResId = iconResId;
        this.durada = durada;
        this.intensitat = intensitat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getDurada() {
        return durada;
    }

    public void setDurada(String durada) {
        this.durada = durada;
    }

    public String getIntensitat() {
        return intensitat;
    }

    public void setIntensitat(String intensitat) {
        this.intensitat = intensitat;
    }
}

package fr.uga.iut2.genevent.modele;

import java.io.Serializable;

public class Oeuvre implements Serializable {
    private static final long serialVersionUID = 5927361556815975571L;  // nécessaire pour la sérialisation

    private String nom;
    private String nomArtiste;

    public Oeuvre(String nom, String nomArtiste) {
        this.nom = nom;
        this.nomArtiste = nomArtiste;
    }

    public String getNom() {
        return nom;
    }

    public String getNomArtiste() {
        return nomArtiste;
    }
}

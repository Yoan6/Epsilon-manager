package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.util.Date;


public class Projet implements Serializable {

    private static final long serialVersionUID = 985698546984564589L;  // nécessaire pour la sérialisation
    private final Inventaire genevent;
    private String nom;
    private Date dateDebut;
    private Date dateFin;

    private String lieu;
    private int capacite;
    private String theme;
    private int budget;
    // Invariant de classe : !dateDebut.isAfter(dateFin)
    //     On utilise la négation ici pour exprimer (dateDebut <= dateFin), ce
    //     qui est équivalent à !(dateDebut > dateFin).


    public Projet(Inventaire genevent, String nom, Date dateDebut, Date dateFin, String lieu, int capacite, String theme, int budget) {
        assert !dateDebut.after(dateFin);
        this.genevent = genevent;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.capacite = capacite;
        this.theme = theme;
        this.budget = budget;
    }

    public static Projet initialiseEvenement(Inventaire genevent, String nom, Date dateDebut, Date dateFin, String lieu, int capacite, String theme, int budget) {
        return new Projet(genevent, nom, dateDebut, dateFin, lieu, capacite, theme, budget);
    }

    public String getNom() {
        return this.nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        assert !dateDebut.after(this.dateFin);
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        assert !this.dateDebut.after(dateFin);
        this.dateFin = dateFin;
    }
}
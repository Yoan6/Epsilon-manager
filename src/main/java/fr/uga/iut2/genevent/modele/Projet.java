package fr.uga.iut2.genevent.modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Projet implements Serializable {

    private static final long serialVersionUID = 985698546984564589L;  // nécessaire pour la sérialisation
    private final Inventaire genevent;

    private final Map<String, Location> locations;

    private final Set<Oeuvre> oeuvres;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    private String lieu;
    private int capacite;
    private String theme;
    private int budget;
    // Invariant de classe : !dateDebut.isAfter(dateFin)
    //     On utilise la négation ici pour exprimer (dateDebut <= dateFin), ce
    //     qui est équivalent à !(dateDebut > dateFin).


    public Projet(Inventaire genevent, String nom, LocalDate dateDebut, LocalDate dateFin, String lieu, int capacite, String theme, int budget) {
        assert !dateDebut.isAfter(dateFin);
        this.genevent = genevent;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.capacite = capacite;
        this.theme = theme;
        this.budget = budget;
        this.locations = new HashMap<>();
        this.oeuvres = new HashSet<>();
    }

    public static Projet initialiseEvenement(Inventaire genevent, String nom, LocalDate dateDebut, LocalDate dateFin, String lieu, int capacite, String theme, int budget) {
        return new Projet(genevent, nom, dateDebut, dateFin, lieu, capacite, theme, budget);
    }

    public String getNom() {
        return this.nom;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        assert !dateDebut.isAfter(this.dateFin);
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        assert !this.dateDebut.isAfter(dateFin);
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public int getCapacite() {
        return capacite;
    }

    public String getTheme() {
        return theme;
    }

    public int getBudget() {
        return budget;
    }

    public void ajoutLocation(Location l) {
        this.locations.put(l.getId(), l);
    }

    public void removeLocation(String id) {
        this.locations.remove(id);
    }

    public Location getLocation(String id) {
        return this.locations.get(id);
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Set<Oeuvre> getOeuvres() {
        return oeuvres;
    }
}

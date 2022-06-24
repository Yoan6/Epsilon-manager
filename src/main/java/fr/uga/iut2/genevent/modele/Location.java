package fr.uga.iut2.genevent.modele;

import java.io.Serializable;

public class Location implements Serializable {

    private static final long serialVersionUID = 9130914518504536482L;  // nécessaire pour la sérialisation

    private final String id;
    private int quantite;
    private int temps;

    public Location(String id, int quantite, int temps) {
        this.id = id;
        this.quantite = quantite;
        this.temps = temps;
    }

    public String getId() {
        return id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", quantite=" + quantite +
                ", temps=" + temps +
                '}';
    }
}

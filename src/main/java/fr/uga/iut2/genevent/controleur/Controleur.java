package fr.uga.iut2.genevent.controleur;

import fr.uga.iut2.genevent.modele.Inventaire;
import fr.uga.iut2.genevent.modele.Location;
import fr.uga.iut2.genevent.modele.Oeuvre;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.JavaFXGUI;


public class Controleur {

    private final Inventaire genevent;
    private final IHM ihm;
    private String current = null;

    public Controleur(Inventaire genevent) {
        this.genevent = genevent;

        // choisir la classe CLI ou JavaFXGUI
//        this.ihm = new CLI(this);
        this.ihm = new JavaFXGUI(this);
    }

    public void demarrer() {
        this.ihm.demarrerInteraction();
    }

    public void creerProjet() {
        this.current = null;
        this.ihm.creerProjet();
    }

    public void creerProjet(IHM.InfosProjet infos) {
        this.genevent.nouvelEvenement(infos, current);
        this.current = infos.nom;
    }

    public void ouvrirProjets() {
        this.ihm.ouvrirProjets(genevent.getEvenements().values());
    }

    public void supprimeProjet(String nom) {
        genevent.getEvenements().remove(nom);
    }

    public void choixMateriaux() {
        this.ihm.choixMateriaux();
    }

    public void modifierProjet(String nom) {
        this.current = nom;
        this.ihm.modifierProjet(genevent.getEvenements().get(nom));
    }

    public void modifierMateriaux(String nom) {
        this.current = nom;
        this.ihm.choixMateriaux();
    }

    public void addLocation(Location l) {
        genevent.getEvenements().get(current).ajoutLocation(l);
    }

    public void removeLocation(String id) {
        genevent.getEvenements().get(current).removeLocation(id);
    }

    public int[] getLocation(String id) {
        Location l = genevent.getEvenements().get(current).getLocation(id);
        if (l == null) {
            return null;
        } else {
            return new int[]{l.getQuantite(), l.getTemps()};
        }
    }

    public void choixPersonnel() {
        this.ihm.choixPersonnel();
    }

    public void modifierPersonnel(String nom) {
        this.current = nom;
        this.ihm.choixPersonnel();
    }

    public void choixArtiste() {
        this.ihm.choixArtiste();
    }

    public void modificationArtiste(String nom) {
        this.current = nom;
        this.ihm.modifierArtiste(genevent.getEvenements().get(nom));
    }

    public void addOeuvre(Oeuvre o) {
        this.genevent.getEvenements().get(current).getOeuvres().add(o);
    }

    public void removeOeuvre(Oeuvre o) {
        this.genevent.getEvenements().get(current).getOeuvres().remove(o);
    }
}

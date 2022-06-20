package fr.uga.iut2.genevent.controleur;

import fr.uga.iut2.genevent.modele.Inventaire;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.JavaFXGUI;


public class Controleur {

    private final Inventaire genevent;
    private final IHM ihm;

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
        this.ihm.creerProjet();
    }

    public void creerProjet(IHM.InfosProjet infos) {
        this.genevent.nouvelEvenement(infos);
    }

}

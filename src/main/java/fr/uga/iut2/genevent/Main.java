package fr.uga.iut2.genevent;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.Inventaire;
import fr.uga.iut2.genevent.util.Persisteur;

import java.io.IOException;


public class Main {

    public static final int EXIT_ERR_LOAD = 2;
    public static final int EXIT_ERR_SAVE = 3;

    public static void main(String[] args) {
        Inventaire genevent = null;

        try {
            genevent = Persisteur.lireEtat();
        }
        catch (ClassNotFoundException | IOException e) {
            System.err.println("Erreur irrécupérable pendant le chargement de l'état : fin d'exécution !");
            System.err.flush();
            e.printStackTrace();
            System.exit(Main.EXIT_ERR_LOAD);
        }

        Controleur controleur = new Controleur(genevent);
        // `Controleur.demarrer` garde le contrôle de l'exécution tant que
        // l'utilisa·teur/trice n'a pas saisi la commande QUITTER.
        controleur.demarrer();

        try {
            Persisteur.sauverEtat(genevent);
        } catch (IOException e) {
            System.err.println("Erreur irrécupérable pendant la sauvegarde de l'état : fin d'exécution !");
            e.printStackTrace();
            System.err.flush();
            System.exit(Main.EXIT_ERR_SAVE);
        }
    }
}

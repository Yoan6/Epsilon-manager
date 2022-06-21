package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.modele.Projet;

import java.util.Collection;
import java.util.Date;


public abstract class IHM {
    /**
     * Récupère les informations à propos d'un
     * {@link fr.uga.iut2.genevent.modele.Projet}.
     */
    public abstract void creerProjet();

    /**
     * Rend actif l'interface Humain-machine.
     * <p>
     * L'appel est bloquant : le contrôle est rendu à l'appelant une fois que
     * l'IHM est fermée.
     */
    public abstract void demarrerInteraction();

    public abstract void ouvrirProjets(Collection<Projet> projets);

    public abstract void choixMateriaux();

    public abstract void modifierProjet(Projet projet);

    public abstract void choixMateriaux(String nom);

    public static class InfosProjet {
        public final String nom;
        public final Date dateDebut;
        public final Date dateFin;
        public final String lieu;
        public final int capacite;
        public final String theme;
        public final int budget;

        public InfosProjet(String nom, Date dateDebut, Date dateFin, String lieu, int capacite, String theme, int budget) {
            this.nom = nom;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            this.lieu = lieu;
            this.capacite = capacite;
            this.theme = theme;
            this.budget = budget;
        }
    }

}

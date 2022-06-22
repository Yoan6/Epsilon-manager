package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.modele.Projet;

import java.time.LocalDate;
import java.util.Collection;


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

    public abstract void choixPersonnel();

    public abstract void choixArtiste();

    public abstract void modifierArtiste(Projet projet);

    public static class InfosProjet {
        public final String nom;
        public final LocalDate dateDebut;
        public final LocalDate dateFin;
        public final String lieu;
        public final int capacite;
        public final String theme;
        public final int budget;

        public InfosProjet(String nom, LocalDate dateDebut, LocalDate dateFin, String lieu, int capacite, String theme, int budget) {
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

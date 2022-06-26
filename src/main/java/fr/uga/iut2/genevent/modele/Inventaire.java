package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.vue.IHM;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Inventaire implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation
    private final Map<String, Projet> evenements;  // association qualifiée par le nom

    public Inventaire() {
        this.evenements = new HashMap<>();
    }

    public Map<String, Projet> getEvenements() {
        return this.evenements;
    }

    /**
     * Cette méthode crée un nouvel événement et s'il existe déjà alors elle met les attributs du projet p avec les valeurs du projet donné en paramètre
     * @param infos
     * @param oldname
     */
    public void nouvelEvenement(IHM.InfosProjet infos, String oldname) {
        if (this.evenements.containsKey(infos.nom)) {
            Projet p = this.evenements.get(infos.nom);
            p.setDateDebut(infos.dateDebut);
            p.setDateFin(infos.dateFin);
            p.setLieu(infos.lieu);
            p.setCapacite(infos.capacite);
            p.setTheme(infos.theme);
            p.setBudget(infos.budget);
        } else {
            this.evenements.put(infos.nom, Projet.initialiseEvenement(this, infos.nom, infos.dateDebut, infos.dateFin, infos.lieu, infos.capacite, infos.theme, infos.budget));
            this.evenements.remove(oldname);
        }
    }

}

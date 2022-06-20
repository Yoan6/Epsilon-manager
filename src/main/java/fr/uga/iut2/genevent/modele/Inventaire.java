package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.vue.IHM;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Inventaire implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation
    private final Map<String, IHM.InfosProjet> evenements;  // association qualifiée par le nom

    public Inventaire() {
        this.evenements = new HashMap<>();
    }

    public Map<String, IHM.InfosProjet> getEvenements() {
        return this.evenements;
    }

    public void nouvelEvenement(IHM.InfosProjet infos) {
        this.evenements.put(infos.nom, infos);
    }

}

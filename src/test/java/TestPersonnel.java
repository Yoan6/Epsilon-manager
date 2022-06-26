import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.Inventaire;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.JavaFXGUI;
import javafx.application.Platform;
import javafx.scene.Parent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPersonnel {

    @Test
    public void testID() {
        //ouvrir a la page agent securité automatiquement
        Controleur c = new Controleur(new Inventaire());
        c.creerProjet(new IHM.InfosProjet(
                "Projet 1",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                "Grenoble",
                102,
                "Fête",
                100000
        ));
        JavaFXGUI gui = new JavaFXGUI(c);
        Thread t = new Thread(gui::demarrerInteraction);
        t.start();
        //attendre que l'app se démarre
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> {
                    gui.onAgentSecuriteAction();
                    assertEquals("PERSONNELSociete N°1", JavaFXGUI.getId(
                            (Parent) ((Parent) ((Parent) gui.sceneStack.peek().getRoot().lookup("#matContainer"))
                                    .getChildrenUnmodifiable().get(0)).getChildrenUnmodifiable().get(0)
                    ));
                }
        );
        //attendre que le runLater s'éxécute
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

import fr.uga.iut2.genevent.modele.Inventaire;
import fr.uga.iut2.genevent.modele.Projet;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestProjet {
    @Test
    public void dateTest() {
        Inventaire i = new Inventaire();
        assertDoesNotThrow(() -> {
            new Projet(i, "Projet 1", LocalDate.of(2022, 11, 20), LocalDate.of(2022, 11, 21), "Grenoble", 12, "Dessin", 9000);
        });
        assertThrows(AssertionError.class, () -> {
            new Projet(i, "Projet 1", LocalDate.of(2022, 11, 22), LocalDate.of(2022, 11, 20), "Grenoble", 12, "Dessin", 9000);
        });
        assertDoesNotThrow(() -> {
            new Projet(i, "Projet 1", LocalDate.of(2022, 11, 21), LocalDate.of(2022, 11, 21), "Grenoble", 12, "Dessin", 9000);
        });
    }


}

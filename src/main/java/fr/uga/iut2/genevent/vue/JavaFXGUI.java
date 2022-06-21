package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.Projet;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;


/**
 * La classe JavaFXGUI est responsable des interactions avec
 * l'utilisa·teur/trice en mode graphique.
 * <p>
 * Attention, pour pouvoir faire le lien avec le
 * {@link fr.uga.iut2.genevent.controleur.Controleur}, JavaFXGUI n'est pas une
 * sous-classe de {@link javafx.application.Application} !
 * <p>
 * Le démarrage de l'application diffère des exemples classiques trouvés dans
 * la documentation de JavaFX : l'interface est démarrée à l'initiative du
 * {@link fr.uga.iut2.genevent.controleur.Controleur} via l'appel de la méthode
 * {@link #demarrerInteraction()}.
 */
public class JavaFXGUI extends IHM {

    public static final SimpleDateFormat FORMAT_DATETIME = new SimpleDateFormat("dd/MM/yyyy/HH-mm");
    private final Controleur controleur;
    private final CountDownLatch eolBarrier;  // /!\ ne pas supprimer /!\ : suivi de la durée de vie de l'interface

    private final Stack<Scene> sceneStack = new Stack<>();

    // Par vue non spéciale
    @FXML
    private Label labelTitre;

    @FXML
    private Label filAriane;

    // éléments vue nouveau projet
    @FXML
    private TextField projetNomTextField;
    @FXML
    private TextField projetDateDebut;
    @FXML
    private TextField projetDateFin;
    @FXML
    private TextField projetLieu;
    @FXML
    private TextField projetCapacite;
    @FXML
    private TextField projetTheme;
    @FXML
    private TextField projetBudget;
    @FXML
    private Button projetSuivantButton;
    @FXML
    private ImageView backImage;

    // Vue modifier projets
    @FXML
    private GridPane projetsGridPane;

    // Vue matériaux

    public JavaFXGUI(Controleur controleur) {
        this.controleur = controleur;
        this.eolBarrier = new CountDownLatch(1);  // /!\ ne pas supprimer /!\
    }

    private static boolean validateDateCoherenceTextField(TextField dateDebut, TextField dateFin) {
        boolean isValid = false;
        try {
            Date debut = FORMAT_DATETIME.parse(dateDebut.getText());
            Date fin = FORMAT_DATETIME.parse(dateFin.getText());
            isValid = debut.compareTo(fin) <= 0;
        } catch (ParseException e) {

        }
        markTextFieldErrorStatus(dateDebut, isValid);
        return isValid;
    }

//-----  Éléments du dialogue  -------------------------------------------------

    private void exitAction() {
        // fermeture de l'interface JavaFX : on notifie sa fin de vie
        this.eolBarrier.countDown();
    }

    // menu principal  -----

    private static boolean validateDateTextField(TextField textField) {
        boolean isValid = false;
        try {
            FORMAT_DATETIME.parse(textField.getText());
            isValid = true;
        } catch (ParseException e) {
        }
        markTextFieldErrorStatus(textField, isValid);
        return isValid;
    }

    @FXML
    private void exitMenuItemAction() {
        Platform.exit();
        this.exitAction();
    }

    // vue nouveau projet  -----

    private static boolean validateNumberTextField(TextField textField) {
        boolean isValid = true;
        try {
            Integer.parseInt(textField.getText().strip());
        } catch (NumberFormatException e) {
            isValid = false;
        }

        markTextFieldErrorStatus(textField, isValid);

        return isValid;
    }

    private static void markTextFieldErrorStatus(TextField textField, boolean isValid) {
        ObservableList<Node> list = textField.getParent().getChildrenUnmodifiable();
        Node errorCheck = list.get(list.size() - 1);
        if (isValid) {
            errorCheck.setVisible(false);
        } else {
            errorCheck.setVisible(true);
        }
    }

    /**
     * Point d'entrée principal pour le code de l'interface JavaFX.
     *
     * @param primaryStage stage principale de l'interface JavaFX, sur laquelle
     *     définir des scenes.
     *
     * @throws IOException si le chargement de la vue FXML échoue.
     *
     * @see javafx.application.Application#start(Stage)
     */
    private void start(Stage primaryStage) throws IOException {
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        mainViewLoader.setController(this);
        Scene mainScene = new Scene(mainViewLoader.load());
        sceneStack.push(mainScene);
        primaryStage.setTitle("GenEvent");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    @FXML
    private void newProjetAction() {
        this.controleur.creerProjet();
    }

    @FXML
    private void openProjetAction() {
        this.controleur.ouvrirProjets();
    }

    @FXML
    private void createNewProjetAction() {
        if (validateTextFields()) {
            InfosProjet data;
            try {
                data = new InfosProjet(
                        this.projetNomTextField.getText().strip(),
                        FORMAT_DATETIME.parse(this.projetDateDebut.getText().strip()),
                        FORMAT_DATETIME.parse(this.projetDateFin.getText().strip()),
                        this.projetLieu.getText().strip(),
                        Integer.parseInt(this.projetCapacite.getText().strip()),
                        this.projetTheme.getText().strip(),
                        Integer.parseInt(this.projetBudget.getText().strip())
                );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            this.controleur.creerProjet(data);
            this.controleur.choixMateriaux();
        }
    }

    @FXML
    private void onBack() {
        Scene old = sceneStack.pop();
        Scene current = sceneStack.peek();
        ((Stage) old.getWindow()).setScene(current);
    }

    //Vue matériaux

    @FXML
    private void onMatSub(ActionEvent e) {
        changeLabel(e, false);
    }

    @FXML
    private void onMatAdd(ActionEvent e) {
        changeLabel(e, true);
    }

    private void changeLabel(ActionEvent e, boolean increment) {
        Button b = (Button) e.getSource();
        Parent p1 = b.getParent();
        boolean isTop = p1.getChildrenUnmodifiable().get(0) == b;
        VBox labels = (VBox) p1.getParent().getChildrenUnmodifiable().get(1);
        Label l = (Label) labels.getChildren().get(isTop ? 0 : 1);
        if (isTop) {
            l.setText((Integer.parseInt(l.getText()) + (increment ? 1 : -1)) + "");
        } else {
            l.setText((Integer.parseInt(l.getText()) + (increment ? 12 : -12)) + "");
        }
    }

    @FXML
    private void onChaiseAction() {

    }

    @FXML
    private void onTableAction() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("table.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);

            current.setTitle("Ajout de tables");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private boolean validateTextFields() {
        boolean isValid;

        isValid = validateNonEmptyTextField(this.projetNomTextField);

        isValid &= validateDateTextField(this.projetDateDebut);
        isValid &= validateNonEmptyTextField(this.projetDateDebut);

        isValid &= validateDateTextField(this.projetDateFin);
        isValid &= validateNonEmptyTextField(this.projetDateFin);

        isValid &= validateDateCoherenceTextField(this.projetDateDebut, this.projetDateFin);

        isValid &= validateNonEmptyTextField(this.projetLieu);

        isValid &= validateNumberTextField(this.projetCapacite);
        isValid &= validateNonEmptyTextField(this.projetCapacite);

        isValid &= validateNonEmptyTextField(this.projetTheme);

        isValid &= validateNumberTextField(this.projetBudget);

        return isValid;
    }

    private static boolean validateNonEmptyTextField(TextField textField) {
        boolean isValid = textField.getText().strip().length() > 0;

        markTextFieldErrorStatus(textField, isValid);

        return isValid;
    }

    private String calculFilAriane() {
        StringBuilder sb = new StringBuilder();
        for (Scene s : sceneStack) {
            Node n = s.lookup("#labelTitre");
            if (n instanceof Label) {
                String text = ((Label) n).getText();
                sb.append(text, 0, text.length() - 2).append(" > ");
            }
        }
        return sb.toString();
    }

//-----  Implémentation des méthodes abstraites  -------------------------------

    @Override
    public void demarrerInteraction() {
        // démarrage de l'interface JavaFX
        Platform.startup(() -> {
            Stage primaryStage = new Stage();
            primaryStage.setOnCloseRequest((WindowEvent t) -> this.exitAction());
            try {
                this.start(primaryStage);
            }
            catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        });

        // attente de la fin de vie de l'interface JavaFX
        try {
            this.eolBarrier.await();
        } catch (InterruptedException exc) {
            System.err.println("Erreur d'exécution de l'interface.");
            System.err.flush();
        }
    }

    @Override
    public void ouvrirProjets(Collection<Projet> projets) {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("modifier-projet-view.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            int i = 0;
            for (Projet p : projets) {
                projetsGridPane.getRowConstraints().add(new RowConstraints());
                Label l = new Label(p.getNom());
                GridPane.setRowIndex(l, i);
                Button b = new Button("OuvrirFicheTec");
                GridPane.setRowIndex(b, i);
                GridPane.setColumnIndex(b, 1);

                MenuItem modProj = new MenuItem("Modifier projet");
                modProj.setOnAction((e) -> {
                    controleur.modifierProjet(p.getNom());
                });
                MenuButton mb = new MenuButton("Choisir page", null, modProj, new MenuItem("Matériaux"), new MenuItem("Personnel"), new MenuItem("Artiste / Oeuvre"));
                GridPane.setRowIndex(mb, i);
                GridPane.setColumnIndex(mb, 2);


                ImageView iv = new ImageView(JavaFXGUI.class.getResource("Supprimer.png").toString());
                iv.setFitHeight(30);
                iv.setFitWidth(30);
                iv.setPreserveRatio(true);
                iv.setPickOnBounds(true);
                GridPane.setRowIndex(iv, i);
                GridPane.setColumnIndex(iv, 3);
                int finalI = i;
                iv.setOnMouseClicked((e) -> {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Êtes vous sur de supprimer ce projet ?");
                    a.showAndWait().ifPresent((button) -> {
                        if (button == ButtonType.OK) {
                            controleur.supprimeProjet(p.getNom());
                            projetsGridPane.getChildren().removeIf(n -> GridPane.getRowIndex(n) == finalI);
                        }
                    });
                });
                projetsGridPane.getChildren().addAll(l, b, mb, iv);
                i++;
            }
            current.setTitle("Modifier un Projet");
            current.setScene(newUserScene);


            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void choixMateriaux() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("ajout-materiaux-view.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            filAriane.setText(calculFilAriane());

            current.setTitle("Choisir les matériaux");
            current.setScene(newUserScene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifierProjet(Projet projet) {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("nouveau-projet-view.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            projetNomTextField.setText(projet.getNom());
            projetDateDebut.setText(FORMAT_DATETIME.format(projet.getDateDebut()));
            projetDateFin.setText(FORMAT_DATETIME.format(projet.getDateFin()));
            projetLieu.setText(projet.getLieu());
            projetCapacite.setText(projet.getCapacite() + "");
            projetTheme.setText(projet.getTheme());
            projetBudget.setText(projet.getBudget() + "");

            labelTitre.setText("Modifier projet :");
            filAriane.setText(calculFilAriane());

            current.setTitle("Modifier un Projet");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void creerProjet() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("nouveau-projet-view.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            filAriane.setText(calculFilAriane());

            current.setTitle("Créer un Projet");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

}

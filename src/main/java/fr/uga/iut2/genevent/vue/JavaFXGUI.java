package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.Location;
import fr.uga.iut2.genevent.modele.Oeuvre;
import fr.uga.iut2.genevent.modele.Projet;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Map;
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
    private DatePicker projetDateDebut;
    @FXML
    private DatePicker projetDateFin;
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


    @FXML
    private GridPane artistreGridPane;

    @FXML
    private TextField oeuvreTextField;
    @FXML
    private TextField artisteTextField;
    // Vue matériaux
    @FXML
    private Label budgetLabel;
    @FXML
    private Label coutLabel;

    public JavaFXGUI(Controleur controleur) {
        this.controleur = controleur;
        this.eolBarrier = new CountDownLatch(1);  // /!\ ne pas supprimer /!\
    }

    private static boolean validateDateCoherenceTextField(DatePicker dateDebut, DatePicker dateFin) {
        boolean isValid = dateDebut.getValue().isBefore(dateFin.getValue());
        markTextFieldErrorStatus(dateDebut.getEditor(), isValid);
        return isValid;
    }

//-----  Éléments du dialogue  -------------------------------------------------

    private void exitAction() {
        // fermeture de l'interface JavaFX : on notifie sa fin de vie
        this.eolBarrier.countDown();
    }

    // menu principal  -----

    private static boolean validateDateTextField(DatePicker picker) {
        boolean isValid = picker.getValue() != null;
        markTextFieldErrorStatus(picker.getEditor(), isValid);
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
        errorCheck.setVisible(!isValid);
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
            data = new InfosProjet(
                    this.projetNomTextField.getText().strip(),
                    this.projetDateDebut.getValue(),
                    this.projetDateFin.getValue(),
                    this.projetLieu.getText().strip(),
                    Integer.parseInt(this.projetCapacite.getText().strip()),
                    this.projetTheme.getText().strip(),
                    Integer.parseInt(this.projetBudget.getText().strip())
            );
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
    private void materiauxSuivant() {
        this.controleur.choixPersonnel();
    }

    @FXML
    private void personnelSuivant() {
        this.controleur.choixArtiste();
    }

    @FXML
    private void onMatSub(ActionEvent e) {
        changeLabel((Node) e.getSource(), false);
    }

    @FXML
    private void onMatAdd(ActionEvent e) {
        changeLabel((Node) e.getSource(), true);
    }

    private static Node getNodeAt(GridPane gp, Integer col, Integer row) {
        col = col == null ? 0 : col;
        row = row == null ? 0 : row;
        for (Node n : gp.getChildrenUnmodifiable()) {
            int rowI = GridPane.getRowIndex(n) == null ? 0 : GridPane.getRowIndex(n);
            int colI = GridPane.getColumnIndex(n) == null ? 0 : GridPane.getColumnIndex(n);
            if (rowI == row && colI == col) {
                return n;
            }
        }
        return null;
    }

    //Table des prix de tous les prix des matériaux (ou autres)
    private final Map<String, Float> prixs = Map.ofEntries(Map.entry("table1.JPG", 8.20f), Map.entry("table2.JPG", 10.50f),
            Map.entry("table3.JPG", 5.70f), Map.entry("1.1.JPG", 3.01f), Map.entry("1.2.JPG", 2.50f), Map.entry("1.3.JPG", 2.55f), Map.entry("support1.JPG", 12.0f), Map.entry("support2.JPG", 1.12f), Map.entry("support3.JPG", 3.99f), Map.entry("tablette.JPG", 7.80f), Map.entry("tele.JPG", 33.50f), Map.entry("projo.JPG", 23.90f), Map.entry("lumiere1.JPG", 7.20f), Map.entry("lumiere2.JPG", 3.80f), Map.entry("lumiere3.JPG", 1.95f), Map.entry("ecouteur.JPG", 63.20f), Map.entry("camera.JPG", 39.99f), Map.entry("enceinte.JPG", 14.90f));

    @FXML
    private void onMatValider(ActionEvent e) {
        ObservableList<Node> childrenUnmodifiable = ((Parent) e.getSource()).getScene().getRoot().getChildrenUnmodifiable();
        for (int i = 0, size = childrenUnmodifiable.size(); i < size - 1; i += 2) {
            Parent p = (Parent) childrenUnmodifiable.get(i);
            ImageView iv = (ImageView) p.getChildrenUnmodifiable().get(0);
            String[] parts = iv.getImage().getUrl().split("/");
            String id = parts[parts.length - 1];
            GridPane fbox = (GridPane) p.getChildrenUnmodifiable().get(2);
            int qt = Integer.parseInt(((Label) getNodeAt(fbox, 2, 0)).getText());
            int temps = Integer.parseInt(((Label) getNodeAt(fbox, 2, 1)).getText());
            if (qt != 0 && temps != 0) {
                Location l = new Location(id, qt, temps);
                this.controleur.addLocation(l);
            } else {
                this.controleur.removeLocation(id);
            }
        }
        onBack();
    }

    private void changeLabel(Node b, boolean increment) {
        GridPane p1 = (GridPane) b.getParent();
        boolean isTop = GridPane.getRowIndex(b) == null || GridPane.getRowIndex(b) == 0;
        Label l = (Label) getNodeAt(p1, 2, GridPane.getRowIndex(b));
        int p;
        if (isTop) {
            p = Integer.parseInt(l.getText()) + (increment ? 1 : -1);
        } else {
            p = Integer.parseInt(l.getText()) + (increment ? 12 : -12);
        }
        if (p < 0) {
            return;
        }
        l.setText(p + "");
        //Calcul du prix
        Label qt = (Label) getNodeAt(p1, 2, 0);
        Label tps = (Label) getNodeAt(p1, 2, 1);

        ImageView iv = (ImageView) p1.getParent().getChildrenUnmodifiable().get(0);
        String[] parts = iv.getImage().getUrl().split("/");
        String id = parts[parts.length - 1];
        int quantite = Integer.parseInt(qt.getText());
        int temps = Integer.parseInt(tps.getText());
        Float prix = prixs.get(id);
        if (prix == null) {
            System.err.println("Prix non trouvé !!!");
            return;
        }
        float total = quantite * (temps / 12.0f) * prix;
        total = Math.round(total * 100.0f) / 100.0f;
        Label lprix = (Label) getNodeAt(p1, 4, 0);
        Label ltemps = (Label) getNodeAt(p1, 4, 1);
        lprix.setText("Prix : " + total + "€");
        ltemps.setText("Temps : " + tps.getText() + "H");

        // Chargement budget
        budgetLabel.setText("Budget : " + this.controleur.getBudget());
        coutLabel.setText("Coût total : " + getCoutTotal(b.getScene().getRoot()));
    }

    @FXML
    private void onChaiseAction() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("chaises.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            // Chargement des materiaux
            chargeMateriaux(newUserScene.getRoot());

            current.setTitle("Ajout de chaises");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void onEclairageAction() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("lumiere.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            chargeMateriaux(newUserScene.getRoot());

            current.setTitle("Ajout de Éclairages");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void onMultimediaAction() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("multimedia.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            // Chargement des materiaux
            chargeMateriaux(newUserScene.getRoot());

            current.setTitle("Ajout de multimedia");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void onSupportAction() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("support.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            // Chargement des materiaux
            chargeMateriaux(newUserScene.getRoot());

            current.setTitle("Ajout de supports");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void onLocauxAction() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("local.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);

            current.setTitle("Ajout de locaux");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void onTableAction() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("table.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            // Chargement des materiaux
            chargeMateriaux(newUserScene.getRoot());

            current.setTitle("Ajout de tables");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void chargeMateriaux(Parent root) {
        ObservableList<Node> childrenUnmodifiable = root.getChildrenUnmodifiable();
        for (int i = 0, size = childrenUnmodifiable.size(); i < size - 1; i += 2) {
            Parent p = (Parent) childrenUnmodifiable.get(i);
            ImageView iv = (ImageView) p.getChildrenUnmodifiable().get(0);
            String[] parts = iv.getImage().getUrl().split("/");
            String id = parts[parts.length - 1];
            int[] infos = this.controleur.getLocation(id);
            if (infos == null) {
                continue;
            }

            GridPane fbox = (GridPane) p.getChildrenUnmodifiable().get(2);
            Label qt = ((Label) getNodeAt(fbox, 2, 0));
            Label temps = ((Label) getNodeAt(fbox, 2, 1));
            qt.setText(infos[0] + "");
            temps.setText(infos[1] + "");
            Float prix = prixs.get(id);
            if (prix == null) {
                System.err.println("Prix non trouvé !!!");
                return;
            }
            float total = infos[0] * (infos[1] / 12.0f) * prix;
            total = Math.round(total * 100.0f) / 100.0f;
            Label lprix = (Label) getNodeAt(fbox, 4, 0);
            Label ltemps = (Label) getNodeAt(fbox, 4, 1);
            lprix.setText("Prix : " + total + "€");
            ltemps.setText("Temps : " + infos[1] + "H");
        }
        // Chargement budget
        budgetLabel.setText("Budget : " + this.controleur.getBudget());
        coutLabel.setText("Coût total : " + getCoutTotal(root));
    }

    private String getCoutTotal(Parent root) {
        double total = 0;
        for (int i = 0, size = root.getChildrenUnmodifiable().size(); i < size - 1; i += 2) {
            Parent p = (Parent) root.getChildrenUnmodifiable().get(i);

            GridPane fbox = (GridPane) p.getChildrenUnmodifiable().get(2);
            Label lprix = (Label) getNodeAt(fbox, 4, 0);
            total += Double.parseDouble(lprix.getText().substring(7, lprix.getText().length() - 1));
        }
        return String.valueOf(total);
    }

    // Vue personnel
    @FXML
    private void onAgentSecuriteAction() {
        openPage("agentsecurite.fxml", "Ajout d'agent de securité");
    }

    @FXML
    private void onGuideAction() {
        openPage("guide.fxml", "Ajout de guide");
    }

    @FXML
    private void onRegisseurAction() {
        openPage("regisseur.fxml", "Ajout de régisseur");
    }

    @FXML
    private void onAccueilAction() {
        openPage("acceuil.fxml", "Ajout de personnel d'accueil");
    }

    @FXML
    private void onCuisinierAction() {
        openPage("cuisinier.fxml", "Ajout de cuisinier");
    }

    private void openPage(String fileName, String title) {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource(fileName));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);

            current.setTitle(title);
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
        isValid &= validateNonEmptyTextField(this.projetDateDebut.getEditor());

        isValid &= validateDateTextField(this.projetDateFin);
        isValid &= validateNonEmptyTextField(this.projetDateFin.getEditor());

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
                modProj.setOnAction((e) -> controleur.modifierProjet(p.getNom()));
                MenuItem modMate = new MenuItem("Matériaux");
                modMate.setOnAction((e) -> controleur.modifierMateriaux(p.getNom()));
                MenuItem modPers = new MenuItem("Personnel");
                modPers.setOnAction((e) -> controleur.modifierPersonnel(p.getNom()));
                MenuItem modArtiste = new MenuItem("Artiste / Oeuvre");
                modArtiste.setOnAction((e) -> controleur.modificationArtiste(p.getNom()));
                MenuButton mb = new MenuButton("Choisir page", null, modProj, modMate, modPers, modArtiste);
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
            projetDateDebut.setValue(projet.getDateDebut());
            projetDateFin.setValue(projet.getDateFin());
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
    public void choixPersonnel() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("ajout-personnel-view.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            filAriane.setText(calculFilAriane());

            current.setTitle("Choisir les personnels");
            current.setScene(newUserScene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void choixArtiste() {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("ajouter-artiste-oeuvre.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            filAriane.setText(calculFilAriane());

            current.setTitle("Choisir les Artistes et oeuvres");
            current.setScene(newUserScene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifierArtiste(Projet projet) {
        try {
            FXMLLoader newUserViewLoader = new FXMLLoader(getClass().getResource("ajouter-artiste-oeuvre.fxml"));
            newUserViewLoader.setController(this);
            Scene newUserScene = new Scene(newUserViewLoader.load());
            for (Oeuvre o : projet.getOeuvres()) {
                ajouteOeuvre(o);
            }
            Stage current = (Stage) sceneStack.peek().getWindow();
            sceneStack.push(newUserScene);
            filAriane.setText(calculFilAriane());


            current.setTitle("Choisir les Artistes et oeuvres");
            current.setScene(newUserScene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onAddOeuvre() {
        Oeuvre o = new Oeuvre(oeuvreTextField.getText(), artisteTextField.getText());
        oeuvreTextField.setText("");
        artisteTextField.setText("");
        this.controleur.addOeuvre(o);
        ajouteOeuvre(o);
    }

    private void ajouteOeuvre(Oeuvre o) {
        int cols = artistreGridPane.getColumnCount() - 3;
        //moving down all
        for (ListIterator<Node> it = artistreGridPane.getChildren().listIterator(); it.hasNext(); ) {
            Node n = it.next();
            if (GridPane.getRowIndex(n) >= cols) {
                it.remove();
                GridPane.setConstraints(n, GridPane.getColumnIndex(n), GridPane.getRowIndex(n) + 1);
                it.add(n);
            }
        }
        Label lab = new Label(o.getNomArtiste() + " - " + o.getNom());
        GridPane.setHgrow(lab, Priority.ALWAYS);
        ImageView mod = new ImageView(JavaFXGUI.class.getResource("Modifier.png").toString());
        mod.setFitHeight(23);
        mod.setFitWidth(25);
        GridPane.setHalignment(mod, HPos.CENTER);
        GridPane.setValignment(mod, VPos.CENTER);
        mod.setPreserveRatio(true);
        mod.setPickOnBounds(true);
        ImageView suppr = new ImageView(JavaFXGUI.class.getResource("Supprimer.png").toString());
        suppr.setFitHeight(23);
        suppr.setFitWidth(25);
        GridPane.setHalignment(suppr, HPos.CENTER);
        GridPane.setValignment(suppr, VPos.CENTER);
        suppr.setPreserveRatio(true);
        suppr.setPickOnBounds(true);
        suppr.setOnMouseClicked((e) -> removeOeuvre(o, lab, mod, suppr));
        mod.setOnMouseClicked((e) -> {
            artisteTextField.setText(o.getNomArtiste());
            oeuvreTextField.setText(o.getNom());
            removeOeuvre(o, lab, mod, suppr);
        });
        artistreGridPane.getRowConstraints().add(new RowConstraints(15, 40, 40));
        artistreGridPane.addRow(cols, lab, mod, suppr);
    }

    private void removeOeuvre(Oeuvre o, Label lab, ImageView mod, ImageView suppr) {
        int col = GridPane.getRowIndex(lab);
        //BUMP
        for (ListIterator<Node> it = artistreGridPane.getChildren().listIterator(); it.hasNext(); ) {
            Node n = it.next();
            if (GridPane.getRowIndex(n) > col) {
                it.remove();
                GridPane.setConstraints(n, GridPane.getColumnIndex(n), GridPane.getRowIndex(n) - 1);
                it.add(n);
            }
        }
        artistreGridPane.getChildren().remove(lab);
        artistreGridPane.getChildren().remove(mod);
        artistreGridPane.getChildren().remove(suppr);
        artistreGridPane.getRowConstraints().remove(artistreGridPane.getRowConstraints().size() - 1);
        this.controleur.removeOeuvre(o);
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
            projetDateDebut.setDayCellFactory((cell) -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (projetDateFin.getValue() != null && item.isAfter(projetDateFin.getValue())) {
                        setDisable(true);
                    }
                }
            });
            projetDateFin.setDayCellFactory((cell) -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (projetDateDebut.getValue() != null && item.isBefore(projetDateDebut.getValue())) {
                        setDisable(true);
                    }
                }
            });
            current.setTitle("Créer un Projet");
            current.setScene(newUserScene);
            //current.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

}

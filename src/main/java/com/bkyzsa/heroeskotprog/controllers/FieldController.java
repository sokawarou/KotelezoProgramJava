package com.bkyzsa.heroeskotprog.controllers;

import com.bkyzsa.heroeskotprog.Main;
import com.bkyzsa.heroeskotprog.units.Egyseg;
import com.bkyzsa.heroeskotprog.spells.Varazslat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * A field.fxml controller osztálya. Tartalmazza a játékbeli csata taktikai felállításának megvalósítását.
 * Ezekben a controllerekben találhatók a grafikus kinézethez tartozó backend funkciók.
 * @author Isztin Martin
 * @version 1.0
 */

public class FieldController implements Initializable {

    @FXML
    GridPane field;

    @FXML
    ImageView player1;
    @FXML
    ImageView player2;
    @FXML
    ImageView foldmuvesImg;
    @FXML
    ImageView ijaszImg;
    @FXML
    ImageView griffImg;
    @FXML
    ImageView magusImg;
    @FXML
    ImageView saiyanImg;
    @FXML
    Label info;

    @FXML
    Label lpley;
    @FXML
    Label rpley;

    //eltunos

    @FXML
    Button harcSetter;


    boolean felkeszules;


    static Image hosimg;
    static Image foldmuves;
    static Image ijasz;
    static Image griff;
    static Image magus;
    static Image szupercsillagharcos;

    static Egyseg kattintott;

    static Image chooseable = new Image("file:img/chooseable.png");
    static Image notnull = new Image("file:img/notnull.png");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        felkeszules = true;
        hosimg = new Image("file:img/hos.jpg");
        foldmuves = new Image("file:img/foldmuves.png");
        ijasz = new Image("file:img/ijasz.png");
        griff = new Image("file:img/griff.png");
        magus = new Image("file:img/magus.png");
        szupercsillagharcos = new Image("file:img/szupercsillagharcos.png");

        player1.setImage(hosimg);
        player2.setImage(hosimg);

        foldmuvesImg.setImage(foldmuves);
        ijaszImg.setImage(ijasz);
        griffImg.setImage(griff);
        magusImg.setImage(magus);
        saiyanImg.setImage(szupercsillagharcos);
        info.setText("Válassz taktikát és tetszőlegesen helyezd el az egységeid!");

        Image notnull = new Image("file:img/notnull.png");
        for (Node node: field.getChildren()) {
            if(node instanceof ImageView) {
                ((ImageView)node).setImage(notnull);
            }

        }

        lpley.setText("1. játékos");
        rpley.setText(Main.gameData.multiplayer ? "2. játékos" : "BOT játékos");

        ImageView[] units = new ImageView[]{foldmuvesImg, ijaszImg, griffImg, magusImg, saiyanImg};

        for(int i = 0; i<5; i++) {
            if(Main.gameData.pakol.egysegek[i].getDb() == 0) {
                units[i].setOpacity(0.3);
                units[i].setDisable(true);
            }
        }

    }

    @FXML
    public void hoverUnit(MouseEvent event) {
        String output = "";

        switch(((ImageView)event.getSource()).getId()) {
            case "foldmuvesImg" -> output = Main.gameData.pakol.egysegek[0].getDb() + " db Földműves";
            case "ijaszImg" -> output = Main.gameData.pakol.egysegek[1].getDb() + " db Íjász";
            case "griffImg" -> output = Main.gameData.pakol.egysegek[2].getDb() + " db Griff";
            case "magusImg" -> output = Main.gameData.pakol.egysegek[3].getDb() + " db Mágus";
            case "saiyanImg" -> output = Main.gameData.pakol.egysegek[4].getDb() + " db Szupercsillagharcos";
        }

        info.setText(output);

    }

    @FXML
    public void hoverOff(MouseEvent event) {
        info.setText("Válassz taktikát és tetszőlegesen helyezd el az egységeid!");
    }


    @FXML
    public void chooseUnit(MouseEvent event) {

        int index;
        //melyik unitra kattintottunk?
        switch(((ImageView)event.getSource()).getId()) {
            case "foldmuvesImg" -> index = 0;
            case "ijaszImg" -> index = 1;
            case "griffImg" -> index = 2;
            case "magusImg" -> index = 3;
            case "saiyanImg" -> index = 4;
            default -> index = 5;
        }

        if(index == 5) {
            return;
        }

        info.setText("Helyezd el az egységet a rendelkezésre álló területen.");

        kattintott = Main.gameData.pakol.egysegek[index];

        //hova lehet rakni kijelolese
        for (Node node: field.getChildren()) {
            if(node instanceof ImageView) {
                if(((ImageView)node).getImage().getUrl().contains("notnull") && GridPane.getColumnIndex(node) != null && Main.gameData.pakol == Main.gameData.lplayer && GridPane.getColumnIndex(node) < 2) {
                    ((ImageView)node).setImage(chooseable);
                }
                if(((ImageView)node).getImage().getUrl().contains("notnull") && GridPane.getColumnIndex(node) != null && Main.gameData.pakol == Main.gameData.rplayer && GridPane.getColumnIndex(node) > 9) {
                    ((ImageView)node).setImage(chooseable);
                }
            }
        }

    }

    @FXML
    public void placeUnit(MouseEvent event) {
        boolean rakott = false;
        if((event.getSource()) instanceof ImageView) {
            for(Node node: field.getChildren()) {
                if(felkeszules && GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null && !((ImageView)event.getSource()).getImage().getUrl().contains("notnull") && ((ImageView)node).getImage().getUrl().contains(kattintott.getNev())) {
                    ((ImageView)node).setImage(chooseable);
                }
                if(felkeszules && GridPane.getRowIndex(node) == GridPane.getRowIndex((Node)event.getSource()) && GridPane.getColumnIndex(node) == GridPane.getColumnIndex((Node)event.getSource())
                && ((ImageView)node).getImage().getUrl().contains("chooseable")) {
                    ((ImageView)event.getSource()).setImage(new Image("file:img/" + kattintott.getNev() + ".png"));

                    //ha van ki kene torolni az elozo helyen
                    for(int i = 0; i < 10; i++) {
                        for (int j = 0; j < 12; j++) {
                            if(Main.gameData.map[i][j] == kattintott) {
                                Main.gameData.map[i][j] = null;
                                break;
                            }
                        }
                    }

                    Main.gameData.map[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = kattintott;



                    rakott = true;
                    System.out.println(kattintott.getNev() + " " + kattintott.getDb());
                }
            }
            if(rakott) {
                for(Node node: field.getChildren()) {
                    if(GridPane.getRowIndex(node) != null && ((ImageView)node).getImage().getUrl().contains("chooseable")) {
                        ((ImageView)node).setImage(notnull);
                        info.setText("Válassz taktikát és tetszőlegesen helyezd el az egységeid!");
                    }
                }
            }
        }
    }

    private void aiInitizalize() {
        Random rnd = new Random();

        //2-3 a támadás, többi 1-3, a védekezés pedig a Maradék, ami lehet 0 Squish :D.
        int[] skills = new int[] {2 + rnd.nextInt(3), 0, 1 + rnd.nextInt(3), 1 + rnd.nextInt(3),1 + rnd.nextInt(3), 1 + rnd.nextInt(3)};
        int sum = 0;

        for (int skill : skills) {
            sum += skill;
        }

        int vedekezes = 15 - sum;

        Main.gameData.rplayer.setTamadas(skills[0]);
        Main.gameData.rplayer.setVedekezes(vedekezes);
        Main.gameData.rplayer.setVarazsero(skills[2]);
        Main.gameData.rplayer.setTudas(skills[3]);
        Main.gameData.rplayer.setMoral(skills[4]);
        Main.gameData.rplayer.setSzerencse(skills[5]);


        //15 skill = 222 gold
        Main.gameData.rplayer.setArany(778);

        System.out.println("kepessegre koltott: " + (sum + vedekezes));


        //VARÁZSLAT egy RANDOM
        boolean[] varazslat = new boolean[]{false, false, false, false, false};
        varazslat[rnd.nextInt(varazslat.length)] = true;

        Main.gameData.rplayer.setElerhetoVarazslatok(varazslat[0], varazslat[1], varazslat[2], varazslat[3], varazslat[4]);

        int ar = 0;
        for(Varazslat v : Main.gameData.rplayer.elerhetoVarazslatok)
            if(v.isVan())
            {
                ar = v.getAr();
                System.out.println("skille: " + v.getLeiras());
            }

        Main.gameData.rplayer.setArany(778 - ar);


        //units
        int[] units = new int[] {0, 8 + rnd.nextInt(20), 10, 4 + rnd.nextInt(8), 1 + rnd.nextInt(5)};

        int reszar;
        sum = 0;

        for (int i=1; i < units.length; i++) {
            reszar = units[i] * Main.gameData.rplayer.egysegek[i].getAr();
            System.out.println("unit: " + Main.gameData.rplayer.egysegek[i].getNev() + " " + units[i]);
            sum += reszar;
        }

        int fold = (Main.gameData.rplayer.getArany() - sum) / 2;
        System.out.println("foldmuves: " +fold);
        System.out.println(Main.gameData.rplayer.getArany() + "-" + sum );

        Main.gameData.rplayer.setArany(Main.gameData.rplayer.getArany() - sum);


        Main.gameData.rplayer.setEgysegek(Main.gameData.rplayer, fold, units[1], units[2], units[3], units[4]);

        Main.gameData.rplayer.egysegek[0].setOsszHp(Main.gameData.rplayer.egysegek[0].getHp() * Main.gameData.rplayer.egysegek[0].getDb());
        Main.gameData.rplayer.egysegek[1].setOsszHp(Main.gameData.rplayer.egysegek[1].getHp() * Main.gameData.rplayer.egysegek[1].getDb());
        Main.gameData.rplayer.egysegek[2].setOsszHp(Main.gameData.rplayer.egysegek[2].getHp() * Main.gameData.rplayer.egysegek[2].getDb());
        Main.gameData.rplayer.egysegek[3].setOsszHp(Main.gameData.rplayer.egysegek[3].getHp() * Main.gameData.rplayer.egysegek[3].getDb());
        Main.gameData.rplayer.egysegek[4].setOsszHp(Main.gameData.rplayer.egysegek[4].getHp() * Main.gameData.rplayer.egysegek[4].getDb());

        Main.gameData.rplayer.setMana(Main.gameData.rplayer.getTudas() * 10);

        //we got 0 gold
        System.out.println(Main.gameData.rplayer.getArany() - (fold * 2));


        //pakolássza le a dolgait
        for(int i = 0; i < Main.gameData.rplayer.egysegek.length; i++) {
            boolean sikerult = false;

            while(!sikerult) {
                int sor = rnd.nextInt(10);
                int oszlop = 10 + rnd.nextInt(2);

                if(Main.gameData.map[sor][oszlop] == null) {
                    Main.gameData.map[sor][oszlop] = Main.gameData.rplayer.egysegek[i];
                    sikerult = true;
                }
            }
        }

    }

    @FXML
    public void startGame(ActionEvent event) throws IOException {

        int vartLerakottDb = 0, lerakottDb = 0;
        for(Egyseg e: Main.gameData.pakol.egysegek) {
            if(e.getDb() > 0) {
                vartLerakottDb++;
            }
        }
        for(Node node: field.getChildren()) {
            if(node instanceof ImageView) {
                for(Egyseg e: Main.gameData.pakol.egysegek) {
                    if(((ImageView)node).getImage().getUrl().contains(e.getNev())) {
                        lerakottDb++;
                    }
                }
            }
        }

        if(vartLerakottDb != lerakottDb) {
            info.setText("Nem hagyhatsz hátra az egységedből senkit!");
        }
        else {
            //ha multi akkor ez kicsit más
            if(!Main.gameData.multiplayer) {
                felkeszules = false;
                info.setText("Az ellenfél AI építi a stratégiáját...");
                aiInitizalize();
                Main.gameData.print();

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("statsummary.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setResizable(false);
                stage.setScene(scene);

            }
            else {

                if(Main.gameData.pakol != Main.gameData.rplayer) {
                    Main.gameData.pakol = Main.gameData.rplayer;

                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("shop.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setResizable(false);
                    stage.setScene(scene);
                }
                else {
                    felkeszules = false;

                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("statsummary.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setResizable(false);
                    stage.setScene(scene);
                }


            }

            info.setText("Induljon a csata!");
        }


    }





}

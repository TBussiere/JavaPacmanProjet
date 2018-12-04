
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;


import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.WindowEvent;

/**
 *
 * @author freder
 */
public class VueControleur extends Application {
    
    // modèle : ce qui réalise le calcule de l'expression
    Jeu m;
    Text txt = new Text();
    ImageView pacmanView = new ImageView(new Image("./ressources/pacman.gif"));
    
    Media pacmanSound = new Media(new File("src/ressources/pacman_beginning.mp3").toURI().toString());
    MediaPlayer player = new MediaPlayer(pacmanSound);
    
    Pane mainPane = new Pane();
    Button play = new Button();
    Text endResultTxt = new Text();
    Button returnMenu = new Button();
    
    int column = 21;
    int row = 21;

    @Override
    public void start(Stage primaryStage) {
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
        // initialisation du modèle que l'on souhaite utiliser
        m = new Jeu(column,row,4);
        
        // gestion du placement (permet de palcer le champ Text affichage en haut, et GridPane gPane au centre)
        GridPane grid = new GridPane();
        FlowPane MainGamePane = new FlowPane();        
        MainGamePane.getChildren().add(grid);
        
        
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < row; j++) {
                Rectangle rec = new Rectangle(30,30);
                rec.setFill(Color.BLACK);
                grid.add(rec, i, j);
            }
        }
        
        Observer obs = new Observer() {
            
            @Override
            public void update(Observable o, Object arg) {
                grid.getChildren().clear();
                Case [][] temp = m.getPlateau();
                for (int i = 0; i < temp.length; i++) {
                    for (int j = 0; j < temp[i].length; j++) {
                        Rectangle r = new Rectangle(30,30);
                        
                        if (temp[j][i] instanceof Mur) {
                            r.setFill(Color.rgb(52, 93, 169));
                            grid.add(r,i,j);
                        }
                        else if (temp[j][i] instanceof Couloir) {
                            Couloir c = (Couloir)temp[j][i];
                            if (c.asPacman) {
                                r.setFill(Color.BLACK);
                                grid.add(r,i,j);
                                pacmanView.setFitWidth(25);
                                pacmanView.setFitHeight(25);
                                grid.add(pacmanView,i,j);
                            }
                            else if (c.asGhost) {
                                r.setFill(Color.BLACK);
                                grid.add(r,i,j);
                                ImageView ghostView = new ImageView();
                                ghostView.setFitWidth(25);
                                ghostView.setFitHeight(25);
                                if(c.eatableGhost == true){
                                    ghostView.setImage(new Image("./ressources/ghost_eatable.png"));
                                }else{
                                    if(c.idGhost == 1){
                                        ghostView.setImage(new Image("./ressources/ghost_red.png"));
                                    }
                                    if(c.idGhost == 2){
                                        ghostView.setImage(new Image("./ressources/ghost_yellow.png"));
                                    }
                                    if(c.idGhost == 3){
                                        ghostView.setImage(new Image("./ressources/ghost_pink.png"));
                                    }
                                    if(c.idGhost == 4){
                                        ghostView.setImage(new Image("./ressources/ghost_blue.png"));
                                    }
                                }
                                grid.add(ghostView,i,j);
                            }
                            else if (c.pac_Gomme) {
                                r.setFill(Color.BLACK);
                                grid.add(r,i,j);
                                
                                Circle circle = new Circle(5);
                                BorderPane bp = new BorderPane();
                                bp.setCenter(circle);
                                circle.setFill(Color.BEIGE);
                                grid.add(bp,i,j);
                            }
                            else if (c.super_Pac_Gomme) {
                                r.setFill(Color.BLACK);
                                grid.add(r,i,j);
                              
                                Circle circle = new Circle(8);
                                BorderPane bp = new BorderPane();
                                bp.setCenter(circle);
                                circle.setFill(Color.BEIGE);
                                grid.add(bp,i,j);
                            }
                            else{
                                r.setFill(Color.BLACK);
                                grid.add(r,i,j);
                            }
                        }
                    }
                }
                
                txt.setText("Score :" + ((Pacman)m.getTabEntites()[0]).score);
                
                if (!m.finPartie()) {
                    endResultTxt.setText("GAME OVER");
                    returnMenu.setText("Retour menu");
                    endResultTxt.setVisible(true);
                    returnMenu.setVisible(true);
                }
            }
        };
        
        // la vue observe les "update" du modèle, et réalise les mises à jour graphiques
        m.addObserver(obs);
        //obs.update(m, obs);
        txt.setText(".");
        txt.setFont(new Font(20));
        txt.setFill(Color.WHITE);
        MainGamePane.getChildren().add(txt);
        MainGamePane.getStyleClass().add("bg-black-style");
        MainGamePane.setVisible(false);
        endResultTxt.setTranslateX(203);
        endResultTxt.setTranslateY(330);
        endResultTxt.setTextAlignment(TextAlignment.CENTER);
        endResultTxt.setText("");
        endResultTxt.setFont(new Font(40));
        endResultTxt.setFill(Color.WHITE);
        endResultTxt.setStroke(Color.YELLOW);
        endResultTxt.setStrokeWidth(1);
        endResultTxt.setVisible(false);
        returnMenu.setTranslateX(265);
        returnMenu.setTranslateY(350);
        returnMenu.setText("");
        returnMenu.setMinHeight(30);
        returnMenu.setMinWidth(100);
        returnMenu.setVisible(false);
        
        BorderPane mainMenu = new BorderPane();
        play.setText("Jouer !");
        play.setMinHeight(30);
        play.setMinWidth(100);
        mainMenu.setCenter(play);
        //mainMenu.setTranslateX(265);
        //mainMenu.setTranslateY(300);
        Image logoPacman = new Image("./ressources/pacman_logo.jpg");
        ImageView IvLogo = new ImageView(logoPacman);
        IvLogo.setFitWidth(630);
        IvLogo.setPreserveRatio(true);
        IvLogo.fitWidthProperty();
        mainMenu.setTop(IvLogo);
        mainPane.prefHeight(630);
        mainPane.prefWidth(630);
        mainPane.getChildren().addAll(MainGamePane,mainMenu,endResultTxt,returnMenu);
        mainPane.setMaxHeight(655);
        mainPane.setMaxWidth(630);
        mainPane.getStyleClass().add("bg-black-style");
        
        Scene scene = new Scene(mainPane, Color.BLACK);
        scene.getStylesheets().add("ressources/css-file.css");
       
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent t) {
                switch (t.getCode()) {
                    case UP:
                        //System.out.println("UP");
                        m.deplacer(Direction.HAUT);
                        pacmanView.setScaleY(1.0);
                        pacmanView.setRotate(270);
                        break;
                    case DOWN:
                        //System.out.println("Down");
                        m.deplacer(Direction.BAS);
                        pacmanView.setScaleY(1.0);
                        pacmanView.setRotate(90);
                        break;
                    case LEFT:
                        //System.out.println("Left");
                        m.deplacer(Direction.GAUCHE);
                        pacmanView.setScaleY(-1.0);
                        pacmanView.setRotate(180);
                        break;
                    case RIGHT:
                        //System.out.println("Right");
                        m.deplacer(Direction.DROITE);
                        pacmanView.setScaleY(1.0);
                        pacmanView.setRotate(360);
                        break;
                }
            }
            
        });
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent t) {
                try{
                    m.stopAllThread();
                }
                catch(Exception ex){
                }
            }
            
        });
        
        play.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                m.init(m.xLength, m.yLength, m.nbenemis);
                mainMenu.setVisible(false);
                MainGamePane.setVisible(true);
                player.pause();
            }
        });
        
        returnMenu.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                m.stopAllThread();
                grid.getChildren().clear();
                endResultTxt.setVisible(false);
                returnMenu.setVisible(false);
                MainGamePane.setVisible(false);
                m = new Jeu(column,row,4);
                m.addObserver(obs);
                mainMenu.setVisible(true);
                player.play();
            }
        });
              
        
        primaryStage.setTitle("Pacman");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

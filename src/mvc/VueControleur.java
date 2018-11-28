
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;


import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Blend;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Shadow;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.WindowEvent;

/**
 *
 * @author freder
 */
public class VueControleur extends Application {
    
    // modèle : ce qui réalise le calcule de l'expression
    Jeu m;
    ImageView pacmanView = new ImageView(new Image("./ressources/pacman.gif"));
    
    int column = 21;
    int row = 21;

    @Override
    public void start(Stage primaryStage) {

        // initialisation du modèle que l'on souhaite utiliser
        m = new Jeu(21,21,4);
        
        // gestion du placement (permet de palcer le champ Text affichage en haut, et GridPane gPane au centre)
        GridPane grid = new GridPane();        
        
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
            }
        };
        
        // la vue observe les "update" du modèle, et réalise les mises à jour graphiques
        m.addObserver(obs);
        obs.update(m, obs);
        
        Scene scene = new Scene(grid, Color.WHITESMOKE);
        
       
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent t) {
                switch (t.getCode()) {
                    case UP:
                        System.out.println("UP");
                        m.deplacer(Direction.HAUT);
                        pacmanView.setScaleY(1.0);
                        pacmanView.setRotate(270);
                        break;
                    case DOWN:
                        System.out.println("Down");
                        m.deplacer(Direction.BAS);
                        pacmanView.setScaleY(1.0);
                        pacmanView.setRotate(90);
                        break;
                    case LEFT:
                        System.out.println("Left");
                        m.deplacer(Direction.GAUCHE);
                        pacmanView.setScaleY(-1.0);
                        pacmanView.setRotate(180);
                        break;
                    case RIGHT:
                        System.out.println("Right");
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
                m.stopAllThread();
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

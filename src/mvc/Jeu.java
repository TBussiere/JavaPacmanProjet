/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.io.BufferedReader;
import java.util.Observable;
import java.util.Observer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Lewho
 */
public class Jeu extends Observable {

    private Case[][] plateau;
    private Entite[] tabEntites;

    public Jeu(int x, int y, int nbenemis) {
        init(x, y, nbenemis + 1);
        Thread t = new Thread(tabEntites[0]);
        t.setName("PacmanThread");
        t.start();
    }

    public void init(int nbx, int nby, int nbent) {

        plateau = new Case[nbx][nby];
        tabEntites = new Entite[nbent];

        tabEntites[0] = new Pacman(this);

        for (int i = 1; i < nbent; i++) {
            tabEntites[i] = new Ghost();
        }

        int curNb = 0;

        ////PLATEAU faire le truc sur fichier externe
        File file = new File("./src/ressources/plan.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String text = "";
            StringBuilder sb = new StringBuilder();

            while ((text = reader.readLine()) != null) {
                sb.append(text);
            }
            text = sb.toString();

            String[] split = text.split(";");

            for (int i = 0; i < split.length; i++) {
                System.out.println(split[i]);
                String[] split2 = split[i].split(",");
                for (int j = 0; j < 21; j++) {
                    switch (split2[j]) {
                        case "m":
                            plateau[i][j] = new Mur();
                            break;
                        case "c":
                            plateau[i][j] = new Couloir();
                            break;
                        case "p":
                            plateau[i][j] = new Couloir();
                            ((Couloir) plateau[i][j]).spawn(tabEntites[0]);
                            break;
                        case "f":
                            plateau[i][j] = new Couloir();
                            ((Couloir) plateau[i][j]).spawn(tabEntites[1]);
                            break;
                        case "g":
                            plateau[i][j] = new Couloir();
                            ((Couloir) plateau[i][j]).spawn(TypeGomme.Petite);
                            break;
                        case "b":
                            plateau[i][j] = new Couloir();
                            ((Couloir) plateau[i][j]).spawn(TypeGomme.Grosse);
                            break;
                        default:
                            plateau[i][j] = new Couloir();
                            break;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public synchronized boolean finPartie() {
        for (int i = 0; i < this.plateau.length; i++) {
            for (int j = 0; j < this.plateau[i].length; j++) {
                if (plateau[i][j] instanceof Couloir) {
                    if (((Couloir) plateau[i][j]).asGhost && ((Couloir) plateau[i][j]).asPacman) {
                        return true;
                    } else if (((Couloir) plateau[i][j]).pac_Gomme) {
                        return true;
                    } else if (((Couloir) plateau[i][j]).super_Pac_Gomme) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void deplacer(Direction d) {
        tabEntites[0].currentDirection = d;
    }

    public void newChange() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setChanged(); // notification de la vue
                notifyObservers();
            }
        });
    }

    public Case[][] getPlateau() {
        return plateau;
    }

    public Entite[] getTabEntites() {
        return tabEntites;
    }
}

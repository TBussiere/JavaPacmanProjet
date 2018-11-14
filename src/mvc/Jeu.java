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

/**
 *
 * @author Lewho
 */
public class Jeu extends Observable {

    private Case[][] plateau;
    private Entite[] tabEntites;

    public Jeu() {

        plateau = new Case[21][21];

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
                            ((Couloir) plateau[i][j]).asPacman = true;
                            break;
                        case "f":
                            plateau[i][j] = new Couloir();
                            ((Couloir) plateau[i][j]).asGhost = true;
                            break;
                        case "g":
                            plateau[i][j] = new Couloir();
                            ((Couloir) plateau[i][j]).pac_Gomme = true;
                            break;
                        case "b":
                            plateau[i][j] = new Couloir();
                            ((Couloir) plateau[i][j]).super_Pac_Gomme = true;
                            break;
                        default:
                            plateau[i][j] = new Couloir();
                            break;
                    }
                }
            }
            // notification de la vue, suite à la mise à jour du champ lastValue
            setChanged();
            notifyObservers();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ////Entitées =>4 fantome =>1 pacman
        /*
            tabEntites[0] = new Pacman();
            tabEntites[1] = new Ghost();
            tabEntites[2] = new Ghost();
            tabEntites[3] = new Ghost();
            tabEntites[4] = new Ghost();
         */
    }

    public Case[][] getPlateau() {
        return plateau;
    }

    public Entite[] getTabEntites() {
        return tabEntites;
    }
}

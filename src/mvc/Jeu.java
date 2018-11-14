/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Lewho
 */
public class Jeu extends Observable{

    private Case [][] plateau;
    private Entite [] tabEntites;

    public Jeu() {
        ////PLATEAU faire le truc sur fichier externe
        
        
        ////Entitées =>4 fantome =>1 pacman
        
        tabEntites[0] = new Pacman();
        tabEntites[1] = new Ghost();
        tabEntites[2] = new Ghost();
        tabEntites[3] = new Ghost();
        tabEntites[4] = new Ghost();
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers(); //To change body of generated methods, choose Tools | Templates.
    }
    
}

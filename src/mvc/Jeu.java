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

    @Override
    public void notifyObservers() {
        super.notifyObservers(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o); //To change body of generated methods, choose Tools | Templates.
    }
    
}

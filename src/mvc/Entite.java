/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jean-Baptiste
 */
public abstract class Entite implements Runnable{
    protected Direction currentDirection;
    protected int tempsEntreActions = 500;
    protected Jeu j;
    
    public abstract void realiserAction();
    
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    @Override
    public void run() {
        System.out.println("START");
        while((j.finPartie())) {
            System.out.println("TICK");
            realiserAction();
            j.newChange();
            try {
                sleep(tempsEntreActions); /* par exemple, Pac-Man est plus rapide durant quelques secondes
                après avoir mangé une super-pac-gomme, tempsEntreActions peut varier */
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("END");
    }
}

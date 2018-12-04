/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import static java.lang.Thread.sleep;

/**
 *
 * @author Jean-Baptiste
 */
public abstract class Entite implements Runnable{
    protected Direction currentDirection;
    protected int tempsEntreActions = 250;
    protected Jeu j;
    protected boolean running = true;
    public String threadName = "";
    
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
        while((j.finPartie()) && running) {
            //System.out.println("TICK");
            realiserAction();
            j.newChange();
            try {
                sleep(tempsEntreActions); /* par exemple, Pac-Man est plus rapide durant quelques secondes
                après avoir mangé une super-pac-gomme, tempsEntreActions peut varier */
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        if (!j.finPartie()) {
            System.out.println("GAME OVER");
        }
        else if (!running) {
            System.out.println("END");
        }
        
        if (this instanceof Pacman) {
            System.out.println("Score : " + ((Pacman)this).score);
        }
        
    }
}

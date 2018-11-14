/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

/**
 *
 * @author Jean-Baptiste
 */
public class Entite implements Runnable{
    protected String currentDirection;

    public String getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(String currentDirection) {
        this.currentDirection = currentDirection;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

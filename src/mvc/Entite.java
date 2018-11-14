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
public abstract class Entite implements Runnable{
    protected String currentDirection;
    
    public abstract void realiserAction();
    
    public String getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(String currentDirection) {
        this.currentDirection = currentDirection;
    }

    @Override
    public void run() {
//        while(actif) {
//            realiserAction() ;
//            setChanged(); // notification de la vue, (4) sur le schéma MVC ci-dessous
//            notifyObservers();
//            sleep(tempsEntreActions) ; /* par exemple, Pac-Man est plus rapide durant quelques secondes
//            après avoir mangé une super-pac-gomme, tempsEntreActions peut varier */
//        }
//        grille.retirerDeLEnvironnement(this) ;
//        setChanged(); // notification de la vue
//        notifyObservers();
    }
}

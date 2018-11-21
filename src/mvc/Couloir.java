/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

/**
 *
 * @author Lewho
 */
public class Couloir extends Case{
    public boolean pac_Gomme;
    public boolean super_Pac_Gomme;
    public boolean asGhost;
    public boolean asPacman;
    public boolean eatableGhost = false;
    public int idGhost = -1;

    public Couloir() {
    }
    
    public void spawn(Entite e){
        if (e instanceof Ghost) {
            asGhost = true;
            this.idGhost = ((Ghost) e).ID;
        }
        else if (e instanceof Pacman) {
            asPacman = true;
            
        }

    }
    public void spawn(TypeGomme t){
        if (t == TypeGomme.Petite) {
            pac_Gomme = true;
        }
        else if (t == TypeGomme.Grosse) {
            super_Pac_Gomme = true;
        }
    }
    
}

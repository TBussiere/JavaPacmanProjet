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
public class Pacman extends Entite {

    public int score;
    public boolean superPacman;
    public int curDurationSuperPacmen;
    public int baseDurationSuperPacmen = 32;
    //public int baseDurationSuperPacmen = 1000;
    

    public Pacman(Jeu j) {
        this.score = 0;
        currentDirection = Direction.DROITE;
        superPacman = false;
        this.j = j;
    }

    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    @Override
    public void realiserAction() {
        Case c[][] = j.getPlateau();
        int curX = -1, curY = -1;
        int nextX = -1, nextY = -1;

        for (int i = 0; i < c.length; i++) {
            for (int k = 0; k < c[i].length; k++) {
                if (c[i][k] instanceof Couloir) {
                    if (((Couloir) c[i][k]).asPacman) {
                        curX = i;
                        curY = k;
                        break;
                    }
                }
            }
            if (curX != -1) {
                break;
            }
        }

        switch (currentDirection) {
            case HAUT:
                nextX = curX - 1;
                nextY = curY;
                deplacement(c, curX, curY, nextX, nextY);
                break;
            case BAS:
                nextX = curX + 1;
                nextY = curY;
                deplacement(c, curX, curY, nextX, nextY);
                break;
            case GAUCHE:
                nextX = curX;
                nextY = curY - 1;
                deplacement(c, curX, curY, nextX, nextY);
                break;
            case DROITE:
                nextX = curX;
                nextY = curY + 1;
                deplacement(c, curX, curY, nextX, nextY);
                break;
        }
        
        if (this.superPacman && this.curDurationSuperPacmen == this.baseDurationSuperPacmen) {
            for (int i = 0; i < c.length; i++) {
                for (int k = 0; k < c[i].length; k++) {
                    if (c[i][k] instanceof Couloir) {
                        if (((Couloir)c[i][k]).asGhost) {
                            ((Ghost)j.getTabEntites()[((Couloir)c[i][k]).idGhost]).eatable = true;
                        }
                    }
                }
            }
            this.curDurationSuperPacmen -= 1;
            System.out.println(curDurationSuperPacmen);
        }
        else if (this.superPacman && this.curDurationSuperPacmen<=0) {
            this.superPacman = false;
            this.tempsEntreActions = 250;
            System.out.println(curDurationSuperPacmen);
            
            for (int i = 0; i < c.length; i++) {
                for (int k = 0; k < c[i].length; k++) {
                    if (c[i][k] instanceof Couloir) {
                        if (((Couloir)c[i][k]).asGhost) {
                            ((Ghost)j.getTabEntites()[((Couloir)c[i][k]).idGhost]).eatable = false;
                        }
                    }
                }
            }
        }else if (this.superPacman) {
            this.curDurationSuperPacmen -= 1;
            System.out.println(curDurationSuperPacmen);
        }
    }

    private void deplacement(Case[][] c, int curX, int curY, int nextX, int nextY) {
        synchronized (this) {
            if (nextX == c.length) {
                nextX = 0;
            }
            if (nextX == -1) {
                nextX = c.length-1;
            }
            
            if (nextY == c[0].length) {
                nextY = 0;
            }
            if (nextY == -1) {
                nextY = c[0].length-1;
            }
            
            
            if (c[nextX][nextY] instanceof Couloir) {
                ((Couloir) c[curX][curY]).asPacman = false;
                ((Couloir) c[nextX][nextY]).asPacman = true;
                if (((Couloir) c[nextX][nextY]).pac_Gomme) {
                    j.eating =true;
                    score += 100;
                    ((Couloir) c[nextX][nextY]).pac_Gomme = false;
                } else if (((Couloir) c[nextX][nextY]).super_Pac_Gomme) {
                    j.eating = true;
                    score += 500;
                    superPacman = true;
                    this.tempsEntreActions = 150;
                    curDurationSuperPacmen = baseDurationSuperPacmen;
                    ((Couloir) c[nextX][nextY]).super_Pac_Gomme = false;
                }
            }
        }        
        
        if (c[nextX][nextY] instanceof Couloir && ((Couloir) c[nextX][nextY]).asGhost && this.superPacman) {
            j.entityGetEated(((Couloir) c[nextX][nextY]).idGhost, 0, nextX, nextY);
        }
        
    }
}

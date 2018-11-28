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
    public int baseDurationSuperPacmen = 200;
    
    

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
        }
        else if (this.superPacman) {
            this.curDurationSuperPacmen -= 1;
        }
        else if (this.superPacman && this.curDurationSuperPacmen<=0) {
            this.superPacman = false;
            
            for (int i = 0; i < c.length; i++) {
                for (int k = 0; k < c[i].length; k++) {
                    if (c[i][k] instanceof Couloir) {
                        if (((Couloir)c[i][k]).asGhost) {
                            ((Ghost)j.getTabEntites()[((Couloir)c[i][k]).idGhost]).eatable = false;
                        }
                    }
                }
            }
        }
    }

    private void deplacement(Case[][] c, int curX, int curY, int nextX, int nextY) {
        synchronized (this) {
            if (c[nextX][nextY] instanceof Couloir) {
                ((Couloir) c[curX][curY]).asPacman = false;
                ((Couloir) c[nextX][nextY]).asPacman = true;
                if (((Couloir) c[nextX][nextY]).pac_Gomme) {
                    score += 100;
                    ((Couloir) c[nextX][nextY]).pac_Gomme = false;
                } else if (((Couloir) c[nextX][nextY]).super_Pac_Gomme) {
                    score += 500;
                    superPacman = true;
                    curDurationSuperPacmen = baseDurationSuperPacmen;
                    ((Couloir) c[nextX][nextY]).super_Pac_Gomme = false;
                }
                else if (((Couloir) c[nextX][nextY]).asGhost && this.superPacman) {
                    j.entityGetEated(((Couloir) c[nextX][nextY]).idGhost, nextX, nextY);
                }
            }
        }
    }
}

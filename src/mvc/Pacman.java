/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Jean-Baptiste
 */
public class Pacman extends Entite {

    private int score;
    public boolean superPacman;
    private Image img = new Image("./ressources/pacman.png");
    public ImageView pacmanView = new ImageView(img);

    public Pacman(Jeu j) {
        this.score = 0;
        this.pacmanView.setFitWidth(25);
        this.pacmanView.setFitHeight(25);
        currentDirection = Direction.DROITE;
        superPacman = false;
        this.j = j;
    }

    public ImageView getPacmanView() {
        return pacmanView;
    }

    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    @Override
    public void realiserAction() {
        Case c[][] = j.getPlateau();
        int curX = -1, curY = -1;
        int nextX, nextY;

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
                    ((Couloir) c[nextX][nextY]).super_Pac_Gomme = false;
                }
            }
        }
    }
}

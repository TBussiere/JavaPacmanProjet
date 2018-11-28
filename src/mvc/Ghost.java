/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Jean-Baptiste
 */
public class Ghost extends Entite {
    public static int Xpop = 7;
    public static int Ypop = 10;
    private Color color;
    protected int ID;
    private boolean pop = false;
    private int popIn;

    public Ghost(Jeu j, int id, int popIn) {
        this.j = j;
        this.ID = id;
        this.popIn = popIn;
    }

    @Override
    public void realiserAction() {
        System.out.println("ID :" + this.ID);
        try {
            sleep(popIn);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        Case c[][] = j.getPlateau();
        int curX = -1, curY = -1;
        int nextX, nextY;

        for (int i = 0; i < c.length; i++) {
            for (int k = 0; k < c[i].length; k++) {
                if (c[i][k] instanceof Couloir) {
                    if (((Couloir) c[i][k]).asGhost && ((Couloir) c[i][k]).idGhost == this.ID) {
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
        if (!pop) {
            pop=true;
            popIn = 0;
            deplacement(c,curX,curY,Xpop,Ypop);
            return;
        }
        currentDirection = IA(c, curX, curY);

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
            case NOTFOUND:
                break;
        }
    }

    private void deplacement(Case[][] c, int curX, int curY, int nextX, int nextY) {
        synchronized (this) {
            ((Couloir) c[curX][curY]).asGhost = false;
            ((Couloir) c[curX][curY]).idGhost = -1;
            ((Couloir) c[nextX][nextY]).asGhost = true;
            ((Couloir) c[nextX][nextY]).idGhost = this.ID;
        }
    }

    private Direction IA(Case[][] c, int curX, int curY) {
        Map<Direction, Float> MapDir;
        MapDir = new TreeMap<>();
        int nbNA = 0;
        if (!(c[curX - 1][curY] instanceof Couloir)) {
            MapDir.put(Direction.HAUT, 0f);
            if (Direction.BAS == this.currentDirection) {
                nbNA++;
            }
            else{
                nbNA+=3;
            }
        } else if (((Couloir) c[curX - 1][curY]).asGhost) {
            MapDir.put(Direction.HAUT, 0f);
            if (Direction.BAS == this.currentDirection) {
                nbNA++;
            }
            else{
                nbNA+=3;
            }
        }

        if (!(c[curX + 1][curY] instanceof Couloir)) {
            MapDir.put(Direction.BAS, 0f);
            if (Direction.HAUT == this.currentDirection) {
                nbNA++;
            }
            else{
                nbNA+=3;
            }
        } else if (((Couloir) c[curX + 1][curY]).asGhost) {
            MapDir.put(Direction.BAS, 0f);
            if (Direction.HAUT == this.currentDirection) {
                nbNA++;
            }
            else{
                nbNA+=3;
            }
        }

        if (!(c[curX][curY - 1] instanceof Couloir)) {
            MapDir.put(Direction.GAUCHE, 0f);
            if (Direction.DROITE == this.currentDirection) {
                nbNA++;
            }
            else{
                nbNA+=3;
            }
        } else if (((Couloir) c[curX][curY - 1]).asGhost) {
            MapDir.put(Direction.GAUCHE, 0f);
            if (Direction.DROITE == this.currentDirection) {
                nbNA++;
            }
            else{
                nbNA+=3;
            }
        }

        if (!(c[curX][curY + 1] instanceof Couloir)) {
            MapDir.put(Direction.DROITE, 0f);
            if (Direction.GAUCHE == this.currentDirection) {
                nbNA++;
            }
            else{
                nbNA+=3;
            }
        } else if (((Couloir) c[curX][curY + 1]).asGhost) {
            MapDir.put(Direction.DROITE, 0f);
            if (Direction.GAUCHE == this.currentDirection) {
                nbNA++;
            }
            else{
                nbNA+=3;
            }
        }

        if (!(MapDir.containsKey(Direction.HAUT))) {
            if (Direction.BAS == this.currentDirection) {
                MapDir.put(Direction.HAUT, 1f / (10 - nbNA));
            }else{
                MapDir.put(Direction.HAUT, 3f / (10 - nbNA));
            } 
        }
        if (!(MapDir.containsKey(Direction.BAS))) {
            if (Direction.HAUT == this.currentDirection) {
                MapDir.put(Direction.BAS, 1f / (10 - nbNA));
            }else{
                MapDir.put(Direction.BAS, 3f / (10 - nbNA));
            } 
        }
        if (!(MapDir.containsKey(Direction.GAUCHE))) {
            if (Direction.DROITE == this.currentDirection) {
                MapDir.put(Direction.GAUCHE, 1f / (10 - nbNA));
            }else{
                MapDir.put(Direction.GAUCHE, 3f / (10 - nbNA));
            } 
        }
        if (!(MapDir.containsKey(Direction.DROITE))) {
            if (Direction.GAUCHE == this.currentDirection) {
                MapDir.put(Direction.DROITE, 1f / (10 - nbNA));
            }else{
                MapDir.put(Direction.DROITE, 3f / (10 - nbNA));
            } 
        }

        Direction res = Direction.NOTFOUND;
        
        int randomNumint = ThreadLocalRandom.current().nextInt(1, 100+1);  //rand.nextInt((100 - 0) + 1) + 0;
        float randomNum = randomNumint/100f;

        for (Map.Entry<Direction, Float> entry : MapDir.entrySet()) {
            randomNum -= entry.getValue();
            if (randomNum <= 0) {
                res = entry.getKey();
                break;
            }
        }
        return res;
    }
}

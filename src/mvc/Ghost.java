/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import static java.lang.Thread.sleep;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Jean-Baptiste
 */
public class Ghost extends Entite {

    public static int Xpop = 7;
    public static int Ypop = 10;
    //private Color color;
    protected int ID;
    public boolean pop = false;
    public int popIn;
    public boolean eatable = false;
    public boolean dead = false;

    public Ghost(Jeu j, int id, int popIn) {
        this.j = j;
        this.ID = id;
        this.popIn = popIn;
    }

    @Override
    public void realiserAction() {
        //System.out.println("ID :" + this.ID);
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
            pop = true;
            popIn = 0;
            deplacement(c, curX, curY, Xpop, Ypop);
            return;
        }
        
        if (dead) {
            dead = false;
            j.entityGetEated(this.ID,this.ID, curX, curY);
            return;
        }
        
        currentDirection = IAaStar(curX, curY);

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
            if (nextX == c.length) {
                nextX = 0;
            }
            if (nextX == -1) {
                nextX = c.length - 1;
            }

            if (nextY == c[0].length) {
                nextY = 0;
            }
            if (nextY == -1) {
                nextY = c[0].length - 1;
            }

            ((Couloir) c[curX][curY]).asGhost = false;
            ((Couloir) c[curX][curY]).eatableGhost = false;
            ((Couloir) c[curX][curY]).idGhost = -1;
            ((Couloir) c[nextX][nextY]).asGhost = true;
            ((Couloir) c[nextX][nextY]).idGhost = this.ID;
        }
        if (((Couloir) c[nextX][nextY]).asPacman) {
            if (((Pacman) j.getTabEntites()[0]).superPacman) {
                j.entityGetEated(this.ID,this.ID, nextX, nextY);
            }
        }
        if (this.eatable) {
            ((Couloir) c[nextX][nextY]).eatableGhost = true;
        }
    }
    
    //IA RANDOM 
    private Direction IARNG(Case[][] c, int curX, int curY) {
        Map<Direction, Float> MapDir;
        MapDir = new TreeMap<>();
        int nbNA = 0;
        if (!(curX - 1 == -1)) {
            if (!(c[curX - 1][curY] instanceof Couloir)) {
                MapDir.put(Direction.HAUT, 0f);
                if (Direction.BAS == this.currentDirection) {
                    nbNA++;
                } else {
                    nbNA += 3;
                }
            } else if (((Couloir) c[curX - 1][curY]).asGhost) {
                MapDir.put(Direction.HAUT, 0f);
                if (Direction.BAS == this.currentDirection) {
                    nbNA++;
                } else {
                    nbNA += 3;
                }
            }
        }

        if (!(curX + 1 == c.length)) {
            if (!(c[curX + 1][curY] instanceof Couloir)) {
                MapDir.put(Direction.BAS, 0f);
                if (Direction.HAUT == this.currentDirection) {
                    nbNA++;
                } else {
                    nbNA += 3;
                }
            } else if (((Couloir) c[curX + 1][curY]).asGhost) {
                MapDir.put(Direction.BAS, 0f);
                if (Direction.HAUT == this.currentDirection) {
                    nbNA++;
                } else {
                    nbNA += 3;
                }
            }
        }

        if (!(curY - 1 == -1)) {
            if (!(c[curX][curY - 1] instanceof Couloir)) {
                MapDir.put(Direction.GAUCHE, 0f);
                if (Direction.DROITE == this.currentDirection) {
                    nbNA++;
                } else {
                    nbNA += 3;
                }
            } else if (((Couloir) c[curX][curY - 1]).asGhost) {
                MapDir.put(Direction.GAUCHE, 0f);
                if (Direction.DROITE == this.currentDirection) {
                    nbNA++;
                } else {
                    nbNA += 3;
                }
            }
        }

        if (!(curY + 1 == c[0].length)) {
            if (!(c[curX][curY + 1] instanceof Couloir)) {
                MapDir.put(Direction.DROITE, 0f);
                if (Direction.GAUCHE == this.currentDirection) {
                    nbNA++;
                } else {
                    nbNA += 3;
                }
            } else if (((Couloir) c[curX][curY + 1]).asGhost) {
                MapDir.put(Direction.DROITE, 0f);
                if (Direction.GAUCHE == this.currentDirection) {
                    nbNA++;
                } else {
                    nbNA += 3;
                }
            }
        }

        if (!(MapDir.containsKey(Direction.HAUT))) {
            if (Direction.BAS == this.currentDirection) {
                MapDir.put(Direction.HAUT, 1f / (10 - nbNA));
            } else {
                MapDir.put(Direction.HAUT, 3f / (10 - nbNA));
            }
        }
        if (!(MapDir.containsKey(Direction.BAS))) {
            if (Direction.HAUT == this.currentDirection) {
                MapDir.put(Direction.BAS, 1f / (10 - nbNA));
            } else {
                MapDir.put(Direction.BAS, 3f / (10 - nbNA));
            }
        }
        if (!(MapDir.containsKey(Direction.GAUCHE))) {
            if (Direction.DROITE == this.currentDirection) {
                MapDir.put(Direction.GAUCHE, 1f / (10 - nbNA));
            } else {
                MapDir.put(Direction.GAUCHE, 3f / (10 - nbNA));
            }
        }
        if (!(MapDir.containsKey(Direction.DROITE))) {
            if (Direction.GAUCHE == this.currentDirection) {
                MapDir.put(Direction.DROITE, 1f / (10 - nbNA));
            } else {
                MapDir.put(Direction.DROITE, 3f / (10 - nbNA));
            }
        }

        Direction res = Direction.NOTFOUND;

        int randomNumint = ThreadLocalRandom.current().nextInt(1, 100 + 1);  //rand.nextInt((100 - 0) + 1) + 0;
        float randomNum = randomNumint / 100f;

        for (Map.Entry<Direction, Float> entry : MapDir.entrySet()) {
            randomNum -= entry.getValue();
            if (randomNum <= 0) {
                res = entry.getKey();
                break;
            }
        }
        return res;
    }
    
    
    private Direction IAaStar(int curX, int curY){

        BFS findPosToGo = new BFS(j,ID);
        
        Position[] path  = findPosToGo.getPath();
        if (path.length < 2) {
            return Direction.NOTFOUND;
        }
        Position nextPos = path[path.length-2];
        
        if (nextPos.x == curX - 1 && !eatable) {
            return Direction.HAUT;
        }else if (nextPos.x == curX - 1 && eatable) {
            if (j.getPlateau()[curX + 1][curY] instanceof Couloir && !((Couloir)j.getPlateau()[curX + 1][curY]).asGhost) {
               return Direction.BAS; 
            }
            if (j.getPlateau()[curX][curY - 1] instanceof Couloir && !((Couloir)j.getPlateau()[curX][curY -1]).asGhost) {
                return Direction.GAUCHE;
            }
            if (j.getPlateau()[curX][curY + 1] instanceof Couloir && !((Couloir)j.getPlateau()[curX][curY +1]).asGhost) {
                return Direction.DROITE;
            }
            if (j.getPlateau()[curX - 1][curY] instanceof Couloir && !((Couloir)j.getPlateau()[curX - 1][curY]).asGhost) {
                return Direction.HAUT;
            }
        }

        if (nextPos.x == curX + 1 && !eatable) {
            return Direction.BAS;
        }else if (nextPos.x == curX + 1 && eatable) {
            if (j.getPlateau()[curX - 1][curY] instanceof Couloir && !((Couloir)j.getPlateau()[curX - 1][curY]).asGhost) {
                return Direction.HAUT;
            }
            if (j.getPlateau()[curX][curY - 1] instanceof Couloir && !((Couloir)j.getPlateau()[curX][curY-1]).asGhost) {
                return Direction.GAUCHE;
            }
            if (j.getPlateau()[curX][curY + 1] instanceof Couloir && !((Couloir)j.getPlateau()[curX][curY + 1]).asGhost) {
                return Direction.DROITE;
            }
            if (j.getPlateau()[curX + 1][curY] instanceof Couloir && !((Couloir)j.getPlateau()[curX + 1][curY]).asGhost) {
               return Direction.BAS; 
            } 
        }
        if (nextPos.y == curY - 1 && !eatable) {
            return Direction.GAUCHE;
        }else if (nextPos.y == curY - 1 && eatable) {
            if (j.getPlateau()[curX][curY + 1] instanceof Couloir && !((Couloir)j.getPlateau()[curX][curY + 1]).asGhost) {
                return Direction.DROITE;
            }
            if (j.getPlateau()[curX - 1][curY] instanceof Couloir && !((Couloir)j.getPlateau()[curX - 1][curY]).asGhost) {
                return Direction.HAUT;
            }
            if (j.getPlateau()[curX + 1][curY] instanceof Couloir && !((Couloir)j.getPlateau()[curX + 1][curY]).asGhost) {
               return Direction.BAS; 
            }
            if (j.getPlateau()[curX][curY - 1] instanceof Couloir && !((Couloir)j.getPlateau()[curX][curY - 1]).asGhost) {
                return Direction.GAUCHE;
            }
        }
        if (nextPos.y == curY + 1 && !eatable) {
            return Direction.DROITE;
        }else if (nextPos.y == curY + 1 && eatable) {
            if (j.getPlateau()[curX][curY - 1] instanceof Couloir && !((Couloir)j.getPlateau()[curX][curY - 1]).asGhost) {
                return Direction.GAUCHE;
            }
            if (j.getPlateau()[curX - 1][curY] instanceof Couloir && !((Couloir)j.getPlateau()[curX - 1][curY]).asGhost) {
                return Direction.HAUT;
            }
            if (j.getPlateau()[curX + 1][curY] instanceof Couloir && !((Couloir)j.getPlateau()[curX + 1][curY]).asGhost) {
               return Direction.BAS; 
            }
            if (j.getPlateau()[curX][curY + 1] instanceof Couloir && !((Couloir)j.getPlateau()[curX][curY + 1]).asGhost) {
                return Direction.DROITE;
            }
        }
        return Direction.NOTFOUND;
    }
}

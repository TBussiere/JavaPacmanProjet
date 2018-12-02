package mvc;



public class AbstractSearch {
    public AbstractSearch(Jeu j,int idGhost) {
        jeu = j;
        this.idGhost = idGhost;
        initSearch();
    }
    public AbstractSearch(Jeu j,int idGhost,Position p) {
        jeu = j;
        this.idGhost = idGhost;
        pasedGoal = p;
        initSearch();
    }
    //patern singleton
    public Jeu getJeu() { return jeu; }
    protected Jeu jeu;
    protected int idGhost;
    protected Position pasedGoal = null;
    /*
     * Java type Position (with width and height encoding x and y directions) is used for path finding
     */
    protected Position [] searchPath = null;
    protected int pathCount;
    protected int maxDepth;
    protected Position startPos, goalPos, currentPos;
    protected boolean isSearching = true;

    protected void initSearch() {
        if (searchPath == null) {
            searchPath = new Position[1000];
            for (int i=0; i<1000; i++) {
                searchPath[i] = new Position();
            }
        }
        pathCount = 0;
        startPos = jeu.getPosbyID(this.idGhost);
        currentPos = startPos;
        if (pasedGoal == null) {
            //0 = pacman
            goalPos = jeu.getPosbyID(0);
        }else{
            goalPos = pasedGoal;
        }
        
        searchPath[pathCount++] = currentPos;
    }

    protected boolean equals(Position d1, Position d2) {
        return d1.x == d2.x && d1.y == d2.y;
    }

    public Position [] getPath() {
      Position [] ret = new Position[maxDepth];
      for (int i=0; i<maxDepth; i++) {
        ret[i] = searchPath[i];
      }
      return ret;
    }
    protected Position [] getPossibleMoves(Position Pos) {
        Position tempMoves [] = new Position[4];
        tempMoves[0] = tempMoves[1] = tempMoves[2] = tempMoves[3] = null;
        int x = Pos.x;
        //System.out.println(x);
        int y = Pos.y;
        //System.out.println(y);
        int num = 0;
        Case[][] plat = jeu.getPlateau();
        
        if (x>0 && x<jeu.xLength-1 && plat[x - 1][y] instanceof Couloir) {
            tempMoves[num++] = new Position(x - 1, y);
        }
        if (x>0 && x<jeu.xLength-1 && plat[x + 1][y] instanceof Couloir) {
            tempMoves[num++] = new Position(x + 1, y);
        }
        if (y>0 && y<jeu.yLength-1 && plat[x][y - 1] instanceof Couloir) {
            tempMoves[num++] = new Position(x, y - 1);
        }
        if (y>0 && y<jeu.yLength-1 && plat[x][y + 1] instanceof Couloir) {
            tempMoves[num++] = new Position(x, y + 1);
        }
        return tempMoves;
    }
}

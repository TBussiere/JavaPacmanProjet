package mvc;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BFS extends AbstractSearch {

    public BFS(Jeu j,int idGhost,Position goal) {
        super(j,idGhost,goal);
        performBFS();
    }
    public BFS(Jeu j,int idGhost) {
        super(j,idGhost);
        performBFS();
    }

    private void performBFS() {
        // method to complete
        QueuePath queue = new QueuePath(this.jeu.xLength,this.jeu.yLength);
        queue.addQueue(startPos);
        queue.setVisited(startPos);
        boolean findGoal = false;
        
        while(!queue.isEmpty() || !findGoal){
           Position p = queue.DeQueue();
            if (p == null) {
                break;
            }
           Position[] pos = this.getPossibleMoves(p);
            for (int i = 0; i < pos.length; i++) {
                if (pos[i] != null && !queue.isVisited(pos[i])) {
                    queue.addQueue(pos[i]);
                    queue.setVisited(pos[i]);
                    if(this.equals(pos[i], goalPos)){
                        findGoal = true;
                    }
                }
            }
            queue.addPredMain(p, pos);
            if(findGoal == true){
                break;
            }
        }
        
        if (findGoal) {
            //System.out.println("chemin trouvé");
            int temp = 0;
            int temp2 = 0;
            Position[][] realPredecessor;
            for (int i = 0; i < queue.predecessor.length; i++) {
                if (queue.predecessor[i][0] == null) {
                    temp2 = i;
                    break;
                }

            }

            realPredecessor = new Position[temp2][4];
            for (int i = 0; i < realPredecessor.length; i++) {
                for (int j = 0; j < realPredecessor[i].length; j++) {
                    realPredecessor[i][j] = queue.predecessor[i][j];
                }
            }
            boolean ok = false;
            int i = queue.NeedHim-1;
            //for (int i = realPredecessor.length - 1; i > 0; i--) {
            Position TOFIND = this.goalPos;
            boolean reset = false;
            while(!ok){
                //int next = 0;
                for (int j = 0; j < queue.NeedHim; j++) {
                    for (int k = 0; k < queue.predecessor[j].length; k++) {
                        //System.out.println(j+"et "+k);
                        
                        //System.out.println(i);
                        
                        if(queue.predecessor[j][k] !=null){
                        //System.out.println(realPredecessor[j][k].x + "," + realPredecessor[j][k].y);
                        //System.out.println("    et "+ TOFIND.x+","+TOFIND.y);
                            if (this.equals(queue.predecessor[j][k], TOFIND) && !this.equals(queue.main[i])) {
                                this.searchPath[temp++] = queue.predecessor[j][k];
                                TOFIND= queue.main[j];
                                this.maxDepth=this.maxDepth+1;
                                if (this.equals(TOFIND,this.startPos)) {
                                    ok = true;
                                }
                                reset = true;
                                break;
                            }
                        }
                    }
                    if (reset) {
                        reset = false;
                        break;
                    }
                }                
            }
            this.maxDepth++;
        }
        else{
            System.out.println("Path imposible");
        }
        
        //System.out.println("end");
    }

    protected class QueuePath {

        //Map<Position,Position[]> predecessor;
        Position[][] predecessor;
        Position[] main;
        List<Position> qList;
        int NeedHim = 0;
        boolean[][] alreadyVisited;

        public QueuePath(int h,int w) {
            //predecessor = new Position[h*w][4];
            alreadyVisited = new boolean[w][h];
            qList = new ArrayList<>();
            predecessor = new Position[1000][4];
            main = new Position[1000];
        }
        
        public void addQueue(Position p) {
            qList.add(p);
            
        }
        public void addPredMain(Position main,Position[] child){
            //System.out.println("on appel add pred" + NeedHim);
            if(main != null){
                this.main[NeedHim] = main;
            }
            int temp = 0;
            for (int i = 0; i < child.length; i++) {
                if(child[i] != null){
                    predecessor[NeedHim][i] = child[temp];
                    temp++;
                }
            }
            NeedHim++;
        }
        public Position DeQueue() {
            if (!this.isEmpty()) {
                Position p = this.getFront();
                qList.remove(0);
                return p;
            }
            else{
                return null;
            }
        }
        public Position getFront() {
            if (!this.isEmpty()) {
               return qList.get(0); 
            }
            else{
                return new Position(0,0);
            }
        }
        public void Shift() {
            this.addQueue(this.DeQueue());
        }
        public void setVisited(Position p) {
            alreadyVisited[p.x][p.y] = true;
        }
        public boolean isVisited(Position p){
            return alreadyVisited[p.x][p.y];
        }
        public boolean isEmpty(){
            return qList.size() == 0;
        }
    }
}

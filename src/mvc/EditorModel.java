/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thibu_000
 */
public class EditorModel {
    String[][] grid;
    String[] order = {"m","g","c","b","f","p"};

    public EditorModel(int x,int y) {
        this.grid = new String[x][y];
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = "d";
            }
        }
        
    }
    
    public String whatsNext(String s){
        if (s.equals("d")) {
            return order[0];
        }
        for (int i = 0; i < order.length; i++) {
            if (order[i].equals(s)) {
                if (i == order.length-1) {
                    return order[0];
                }
                else{
                    return order[i+1];
                }
            }
        }
        return "c";
    }
    
    public String changeNode(long x, long y){
        int newx = (int)x;
        int newy = (int)y;
        grid[newx][newy] = whatsNext(grid[newx][newy]);
        return grid[newx][newy];
    }

    String[][] getStrGrid() {
        return grid;
    }
    
    void exportToString(File file){
        try {
            PrintWriter writer = new PrintWriter(file);//new PrintWriter("./src/ressources/customMap1.txt", "UTF-8");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[j][i].equals("d")) {
                        sb.append("c,");
                    }
                    else{
                        sb.append(grid[j][i]);
                        sb.append(",");
                    }
                }
                sb.append(";");
                writer.println(sb.toString());
                sb = new StringBuilder();
            }
            writer.close();
            
        } catch (FileNotFoundException ex) {
            
        }
    }
    
}

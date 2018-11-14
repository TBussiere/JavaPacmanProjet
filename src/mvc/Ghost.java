/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import javafx.scene.paint.Color;

/**
 *
 * @author Jean-Baptiste
 */
public class Ghost extends Entite{
    private Color color;
    
    public Ghost(){
        this.color=Color.BLUE;
    }
    
    @Override
    public void realiserAction(){
        
    }
}

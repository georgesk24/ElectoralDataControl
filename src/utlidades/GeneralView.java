/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utlidades;

import javafx.scene.Node;

/**
 *
 * @author georgep24
 */
public interface GeneralView {
    
    public void initComponents(Object obj);
    
    public void controlVisibilidad(Node node, boolean condicion);
    
}

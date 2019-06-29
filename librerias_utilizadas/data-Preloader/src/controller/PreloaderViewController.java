/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.gluonhq.charm.glisten.control.ProgressIndicator;
import com.gluonhq.charm.glisten.mvc.SplashView;
import data.preloader.data_Preloader;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author root
 */
public class PreloaderViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    ProgressIndicator progress;

    @FXML
    GridPane layout;
    
    @FXML
    private FontAwesomeIcon btnClose;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    
        final Delta dragDelta = new Delta();
      
        layout.setOnMousePressed(new EventHandler<MouseEvent>() {
           @Override public void handle(MouseEvent mouseEvent) {
             dragDelta.x = data_Preloader.stage.getX() - mouseEvent.getScreenX();
             dragDelta.y = data_Preloader.stage.getY() - mouseEvent.getScreenY();
           }
         });
        
        layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            data_Preloader.stage.setX(mouseEvent.getScreenX() + dragDelta.x);
            data_Preloader.stage.setY(mouseEvent.getScreenY() + dragDelta.y);
          }
        });        
    
    }    
    
    @FXML
    public void eventsMouseClick(MouseEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnClose)){
            int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea salir?", "Advertencia", JOptionPane.WARNING_MESSAGE);
            if(confirmar==JOptionPane.YES_OPTION){
                System.exit(0);
            }
        }
        
    }    
    
    
}


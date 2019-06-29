/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.preloader;

import java.io.IOException;
import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author root
 */
public class data_Preloader extends Preloader {
    
    //ProgressBar bar;
    public static Stage stage;
    
    private Scene createPreloaderScene() {
        /*bar = new ProgressBar();
        BorderPane p = new BorderPane();
        p.setCenter(bar);
        return new Scene(p, 300, 150);        */
        return null;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        try {

            stage.initStyle(StageStyle.UNDECORATED);
                        
            Parent root = FXMLLoader.load(getClass().getResource("Preloader.fxml"));
            
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            
            stage.show();
            
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);               

            data_Preloader.stage = stage;

            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al mostrar modulo \n"+ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void handleStateChangeNotification(StateChangeNotification scn) {
        if (scn.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }
    
    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        //bar.setProgress(pn.getProgress());
    }    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author root
 */
public class ElectoralDataControl extends Application {
    
    public static Parent root;
    public static FXMLLoader loader;
    public static Stage stage;
    
    public static Stage stageLogin;
            
    @Override
    public void init(){
        /*Cargamos ventana inicial 5 segundos*/
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ElectoralDataControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        stage.initStyle(StageStyle.UNDECORATED);

        loader = new FXMLLoader(getClass().getResource("test.fxml"));        
        root = loader.load();
                
        Parent root1 = FXMLLoader.load(getClass().getResource("LoginView.fxml"));

        Scene scene = new Scene(root1);

        stage.setResizable(false);
        stage.setScene(scene);

        stage.show();
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);               

        ElectoralDataControl.stageLogin=stage;
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");        
        launch(args);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;

/**
 *
 * @author lenov0o
 */
public class RecomendacionController implements Initializable{

    @FXML
    private Hyperlink linkCorreoConfig;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    
    }
    
    
    @FXML
    public void eventsOnAction(ActionEvent event){
        
        Object evt = event.getSource();
        
        if(evt.equals(linkCorreoConfig)){
        
            String url = "https://myaccount.google.com/lesssecureapps";

            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                }
            }else{
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("xdg-open " + url);
                } catch (IOException e) {
                }
            }            
                        
        }
        
    }
    
    
    
    
}

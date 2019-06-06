/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.webkit.WebConsoleListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelLugarDeVotacionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private WebView webview;
    
    @FXML 
    private JFXButton btnRecargar;
    
    private WebEngine engine;
    
    @FXML
    Hyperlink hiper;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        

        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(new WebConsoleListener() {

            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);           
            
            }
        });        
        
        
        
        //String html = "<html><body><iframe width='700' height='700' src='https://www.google.com/' frameborder='0' allowfullscreen></iframe></body></html>";
        
        engine = webview.getEngine();
        //engine.setUserAgent("use required / intended UA string");        
        
        engine.load("https://wsp.registraduria.gov.co/censo/consultar");
        
        //engine.loadContent(html);
        
    }


    @FXML
    public void eventsOnAction(ActionEvent event) throws URISyntaxException, IOException {
        
        Object evt = event.getSource();

        if(evt.equals(btnRecargar)){
            engine.reload();
        }
        
    }
    
}

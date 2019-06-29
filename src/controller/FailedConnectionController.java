
package controller;

import com.jfoenix.controls.JFXButton;
import data.preloader.data_Preloader;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
<<<<<<< HEAD
import java.awt.HeadlessException;
=======
>>>>>>> 36a48334ae93ca73a7ea5761fec022f9b025982d
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import view.ElectoralDataControl;

/**
 * FXML Controller class
 *
 * @author lenov0o
 */
public class FailedConnectionController implements Initializable {

    @FXML
    private VBox containerVideo;
    
    @FXML
    private FontAwesomeIcon btnClose, btnMinimize;
<<<<<<< HEAD
        
    @FXML
    private JFXButton btnSalir, btnReintentar;
    
    private MediaPlayer player;
    private MediaView mediaView;
=======
    
    @FXML
    private VBox layout;
    
    @FXML
    private JFXButton btnSalir, btnReintentar;
>>>>>>> 36a48334ae93ca73a7ea5761fec022f9b025982d
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

<<<<<<< HEAD
        try{

            player = new MediaPlayer( new Media(getClass().getResource("/resources/video/video-error-final.mp4").toExternalForm()));
            mediaView = new MediaView(player);

            containerVideo.getChildren().add( mediaView);
            player.setAutoPlay(true);
            player.setCycleCount(MediaPlayer.INDEFINITE);        
        
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
        /*
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);        
        */
=======
        MediaPlayer player = new MediaPlayer( new Media(getClass().getResource("/resources/video/video-error-final.mp4").toExternalForm()));
        MediaView mediaView = new MediaView(player);
        
        containerVideo.getChildren().add( mediaView);
        
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);        
        
>>>>>>> 36a48334ae93ca73a7ea5761fec022f9b025982d
        
        
        final Delta dragDelta = new Delta();
        /*
        layout.setOnMousePressed((MouseEvent mouseEvent) -> {
            dragDelta.x = ElectoralDataControl.stageLogin.getX() - mouseEvent.getScreenX();
            dragDelta.y = ElectoralDataControl.stageLogin.getY() - mouseEvent.getScreenY();
        });
        
        layout.setOnMouseDragged((MouseEvent mouseEvent) -> {
            ElectoralDataControl.stageLogin.setX(mouseEvent.getScreenX() + dragDelta.x);
            ElectoralDataControl.stageLogin.setY(mouseEvent.getScreenY() + dragDelta.y);
        });            
        */
        
    }    
    
    
    
    
    @FXML
    public void eventsOnAction(ActionEvent event){
    
        Object evt = event.getSource();
        
<<<<<<< HEAD
        try{
            
            if(evt.equals(btnSalir)){

                if(evt.equals(btnSalir)){
                    int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea salir?");
                    if(confirmar==JOptionPane.YES_OPTION){
                        System.exit(0);
                    }
                }            

            }else if(evt.equals(btnReintentar)){

                ElectoralDataControl.stageFailedConnection.close();
                ElectoralDataControl principal = new ElectoralDataControl();

                try{
                    principal.init();
                    principal.start( new Stage() );                        
                }catch(Exception ex){
                    System.out.println(ex.getMessage());
                }

              /*  
                Platform.runLater( () -> {
                    try {

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } );
            */    
            }
            
        
        }catch(HeadlessException ex){
            System.out.println(ex.getMessage());
        }
        
        
=======
        if(evt.equals(btnSalir)){

            if(evt.equals(btnSalir)){
                int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea salir?");
                if(confirmar==JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }            
            
        }else if(evt.equals(btnReintentar)){
            
            ElectoralDataControl.stageFailedConnection.close();
            ElectoralDataControl principal = new ElectoralDataControl();
            
            Platform.runLater( () -> {
                try {
                    principal.init();
                    principal.start( new Stage() );
                } catch (Exception ex) {
                    Logger.getLogger(FailedConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } );
            
        }
        
>>>>>>> 36a48334ae93ca73a7ea5761fec022f9b025982d
    }
    
    
    @FXML
    public void eventsMouseClick(MouseEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnClose)){
            int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea salir?");
            if(confirmar==JOptionPane.YES_OPTION){
                System.exit(0);
            }
        }else if(evt.equals(btnMinimize)){
        
            ElectoralDataControl.stageFailedConnection.setIconified(true);
        
        }
        
    }
    
    
    
}

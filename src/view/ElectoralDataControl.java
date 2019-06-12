
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
import model.Conexion;

/**
 *
 * @author root
 */
public class ElectoralDataControl extends Application {
    
    public static Parent root;
    public static FXMLLoader loader;
    public static Stage stage;
    
    public static Stage stageLogin;
    public static Stage stageFailedConnection;
    
    private boolean validarConexion=true;
    
    @Override
    public void init(){
        /*Cargamos ventana inicial 5 segundos*/
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ElectoralDataControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Conexion conexion = new Conexion();
        this.validarConexion = conexion.conectar()!=null;
        
        System.out.println("hola mundo");
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        if(this.validarConexion){

            stage.initStyle(StageStyle.UNDECORATED);

            loader = new FXMLLoader(getClass().getResource("Principal.fxml"));        
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
                    
        }else{

            stage.initStyle(StageStyle.UNDECORATED);            
            
            Parent root1 = FXMLLoader.load(getClass().getResource("FailedConnection.fxml"));

            Scene scene = new Scene(root1);

            stage.setResizable(false);
            stage.setScene(scene);

            stage.show();

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);    
            
            ElectoralDataControl.stageFailedConnection=stage;
                        
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

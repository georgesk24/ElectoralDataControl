
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Usuario;
import modelDAO.UsuarioDAO;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;
import view.ElectoralDataControl;
import static view.ElectoralDataControl.root;

/**
 * FXML Controller class
 *
 * @author root
 */



public class LoginViewController implements Initializable, GeneralView{

    @FXML
    private GridPane layout;    
    
    @FXML
    private FontAwesomeIcon btnClose, btnMinimize;
    
    @FXML
    private JFXCheckBox checkVerPassI, checkVerPassC;
    
    @FXML
    private JFXPasswordField textFieldPass, textFieldNuevoPass;
    
    @FXML
    private JFXTextField textFieldPassUnmask, textFieldPassUnmask2, 
                         textFieldUsuario, textFieldNuevoUsuario;
    
    @FXML
    private JFXButton btnIngresar, btnActualizar;
    
    private UsuarioDAO model;
    

    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initComponents(null);
        
    }    

    public void iniciarNuevaVentana(){
        
        ElectoralDataControl.stageLogin.close();
        ElectoralDataControl.stage = new Stage();
            
        URL resource = getClass().getResource("/resources/images/elections.png");        
        ElectoralDataControl.stage.getIcons().add(new Image(resource.toString()));
        
        Scene scene = new Scene(root);
        ElectoralDataControl.stage.setMaximized(true);
        ElectoralDataControl.stage.setScene(scene);
        ElectoralDataControl.stage.show();    
                
        PanelAjustesController config = PrincipalController.loaderAjustes.getController();
        config.mostrarDatosUsuario();        
        ControladorGeneral.resetSesion();
        
    }
    
    
    public void maskPassword(JFXPasswordField pass, JFXTextField text, CheckBox check){

        text.setManaged(false);
        text.setVisible(false);

        // Bind properties. Toggle textField and passwordField
        // visibility and managability properties mutually when checkbox's state is changed.
        // Because we want to display only one component (textField or passwordField)
        // on the scene at a time.
        text.managedProperty().bind(check.selectedProperty());
        text.visibleProperty().bind(check.selectedProperty());

        pass.managedProperty().bind(check.selectedProperty().not());
        pass.visibleProperty().bind(check.selectedProperty().not());

        // Bind the textField and passwordField text values bidirectionally.
        text.textProperty().bindBidirectional(pass.textProperty());
    
    }
    
    @FXML
    public void eventsOnAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnIngresar)){
            
            if(!textFieldUsuario.getText().isEmpty() && !textFieldPass.getText().isEmpty()){
                
                model = new UsuarioDAO();
                ControladorGeneral.CONTROLSESION = model.consultarUsuario(textFieldUsuario.getText(), textFieldPassUnmask.getText());

                if(ControladorGeneral.CONTROLSESION != null){                    
                    iniciarNuevaVentana();
                }else{
                    /*validamos si hubo alguna excepción u error*/
                    if(!ControladorValidaciones.EXCEPCIONES.equals("")){

                        JOptionPane.showMessageDialog(null, "Error en la consulta, Posibles errores : \n"+
                                                            ControladorValidaciones.EXCEPCIONES, 
                                                            "ERROR", JOptionPane.ERROR_MESSAGE);           
                        ControladorValidaciones.EXCEPCIONES="";                                

                    }else{
                        JOptionPane.showMessageDialog(null, "El usuario o contraseña no son validos", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }                        
                    
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos", "ERROR", JOptionPane.ERROR_MESSAGE);            
            }
            
        }else if(evt.equals(btnActualizar)){
        
            if(!textFieldNuevoUsuario.getText().isEmpty() && !textFieldNuevoPass.getText().isEmpty()){

                model = new UsuarioDAO();                
                String usuarioActual = JOptionPane.showInputDialog("Ingrese El nombre de usuario actual");

                if(model.validateUsuario(usuarioActual)){  
                    
                    boolean resultado = model.actualizarDatosUsuario(textFieldNuevoUsuario.getText(), textFieldPassUnmask2.getText());
                    
                    if(resultado){
                    
                        JOptionPane.showMessageDialog(null, "la información se actualizo de manera exitosa", "Exito", JOptionPane.INFORMATION_MESSAGE);
                        textFieldNuevoUsuario.setText("");
                        textFieldPassUnmask2.setText("");
                        textFieldNuevoPass.setText("");
                    
                    }else{
                        
                        if(!ControladorValidaciones.EXCEPCIONES.equals("")){

                            JOptionPane.showMessageDialog(null, "Error en la operación, Posibles errores : \n"+
                                                                ControladorValidaciones.EXCEPCIONES, 
                                                                "ERROR", JOptionPane.ERROR_MESSAGE);           
                            ControladorValidaciones.EXCEPCIONES="";                                

                        }else{
                            JOptionPane.showMessageDialog(null, "No se pudo actualizar la informacion", "ERROR", JOptionPane.ERROR_MESSAGE);                        
                        }                        

                    
                    }
                
                }else{                    
                    JOptionPane.showMessageDialog(null, "No se pudo autenticar que sea un usuario valido", "Error", JOptionPane.ERROR_MESSAGE);
                }
                                
            }else{
                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos", "ERROR", JOptionPane.ERROR_MESSAGE);            
            }
            
            
        }
        
    }

    @FXML
    public void eventsMouseClick(MouseEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnClose)){
            int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea salir?", "Advertencia", JOptionPane.WARNING_MESSAGE);
            if(confirmar==JOptionPane.YES_OPTION){
                System.exit(0);
            }
        }else if(evt.equals(btnMinimize)){
            ElectoralDataControl.stageLogin.setIconified(true);
        }
        
    }

    @Override
    public void initComponents(Object obj) {
        
        maskPassword(textFieldPass, textFieldPassUnmask, checkVerPassI);
        maskPassword(textFieldNuevoPass, textFieldPassUnmask2, checkVerPassC);

        final Delta dragDelta = new Delta();
        
        layout.setOnMousePressed((MouseEvent mouseEvent) -> {
            dragDelta.x = ElectoralDataControl.stageLogin.getX() - mouseEvent.getScreenX();
            dragDelta.y = ElectoralDataControl.stageLogin.getY() - mouseEvent.getScreenY();
        });
        
        layout.setOnMouseDragged((MouseEvent mouseEvent) -> {
            ElectoralDataControl.stageLogin.setX(mouseEvent.getScreenX() + dragDelta.x);
            ElectoralDataControl.stageLogin.setY(mouseEvent.getScreenY() + dragDelta.y);
        });        
        
    
    }

    @Override
    public void controlVisibilidad(Node node, boolean condicion) {
    }

    
}


class Delta { double x, y; } 
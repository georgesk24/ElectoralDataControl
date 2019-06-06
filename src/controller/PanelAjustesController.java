package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javax.mail.Address;
import javax.swing.JOptionPane;
import modelDAO.UsuarioDAO;
import modelDAO.VotantesDAO;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;
import utlidades.Mail;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelAjustesController implements Initializable, GeneralView {

    @FXML
    private Accordion accordionConfig;
        
    @FXML
    private TitledPane titledPaneUsuario;
    
    @FXML
    private JFXPasswordField textFieldPass, textFieldPassEmail;
    
    @FXML
    private JFXTextField textFieldPassUnmask,textFieldUsuario, 
                         textFieldPara, textFieldDe, textFieldAsunto, 
                         textFieldPassEmailUnmask, textFieldEmail;
    
    @FXML
    private JFXButton btnCargarImagen, btnActualizarImagen, btnRestaurarImagen, 
                      btnActualizarUsuario, btnRestaurarUsuario, 
                      btnActualizarPass, btnRestaurarPass,
                      btnLimpiarUser, btnLimpiarPass,
                      btnLimpiarMail, btnEnviarMail, 
                      btnGuardarDatosEmail, btnLimpiarEmail,
                      btnAdjuntar;
    
    @FXML
    private JFXCheckBox checkVerPass, checkBoxTodo, checkVerPassEmail;
    
    @FXML
    private ImageView containerImageProfile;
    
    @FXML
    private HTMLEditor htmlEditorMensaje;
    
    @FXML
    private Hyperlink linkAdjunto;
    
    @FXML
    private Label labelInfo;
    
    @FXML
    private JFXProgressBar barraDeProgreso;

    @FXML
    private Label labelBarra;
    
    private FileChooser filechooser, filechooser2;
    
    private File fileImage, fileAdjuntar;
    
    private ArrayList<String> listEmail=null;
        
    private UsuarioDAO model;

    
    public void mostrarImagen(File file){

        if(file!=null){

            FileInputStream input = null;
            try {
                input = new FileInputStream(file.getAbsolutePath());                    
                Image img = new Image(input);
                containerImageProfile.setImage(img);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PanelAjustesController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if(input!=null){
                        input.close();                        
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PanelAjustesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    public void mostrarDatosUsuario(){
        
        if(ControladorGeneral.CONTROLSESION!=null){

            try{
                textFieldUsuario.setText(ControladorGeneral.CONTROLSESION.getUser());
                textFieldPassUnmask.setText(ControladorGeneral.CONTROLSESION.getPassword());
                textFieldPass.setText(ControladorGeneral.CONTROLSESION.getPassword());
                containerImageProfile.setImage(ControladorGeneral.toImage(ControladorGeneral.CONTROLSESION.getImage()));
                textFieldEmail.setText(ControladorGeneral.CONTROLSESION.getEmail());
                textFieldPassEmail.setText(ControladorGeneral.CONTROLSESION.getPasswordEmail());
                textFieldPassEmailUnmask.setText(ControladorGeneral.CONTROLSESION.getPasswordEmail());
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
            
        }else{
            textFieldUsuario.setText("");
            textFieldPassUnmask.setText("");
            textFieldPass.setText("");        
            textFieldEmail.setText("");
            textFieldPassEmail.setText("");
            textFieldPassEmailUnmask.setText("");
        }

        
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
    
    private void addTooltipText(JFXButton button, String text){
        
        Tooltip tooltipText = new Tooltip(text);
        tooltipText.getStyleClass().add("toolTipTextAjustes");
        Tooltip.install(button, tooltipText);        
    
    }
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initComponents(null);
    }    
    
    @FXML
    public void eventsOnAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnCargarImagen)){
            
            ArrayList<String> extensiones = new ArrayList<>();
            extensiones.add("*.jpg");
            extensiones.add("*.png");
            FileChooser.ExtensionFilter filtro = new FileChooser.ExtensionFilter("Solo imagenes JPG, PNG", extensiones);
            
            filechooser.getExtensionFilters().add(filtro);
            
            boolean condicion = true;
            
            while(condicion){

                File file = filechooser.showOpenDialog(null);

                if(file!=null){

                    long fileSizeInBytes = file.length();
                    long fileSizeInKB = fileSizeInBytes / 1024; 

                    if(fileSizeInKB < 1024){
                        fileImage = file;
                        condicion=false;
                    }

                }else{
                    condicion=false;
                }
            
            }
            
            mostrarImagen(fileImage);
        
        }else if(evt.equals(btnActualizarImagen)){

            if(fileImage!=null){
                
                int confirm = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea actualizar la información?");
                
                if(confirm==JOptionPane.YES_OPTION){
                    
                    model = new UsuarioDAO();
                    
                    if(ControladorGeneral.CONTROLSESION!=null){

                        if(model.actualizarImagenUsuario(ControladorGeneral.CONTROLSESION.getUser(), fileImage)==true){

                            //actualizamos la imagen, asignando la nueva imagen al objeto tipo Usuario(sesion)
                            ControladorGeneral.CONTROLSESION.setImage(ControladorGeneral.toBlob(fileImage));
                            JOptionPane.showMessageDialog(null, "La imagen ha sido actualizada de manera exitosa", "Éxito", JOptionPane.INFORMATION_MESSAGE);                    
                            fileImage=null; 
                            ControladorGeneral.resetSesion();
                            
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
                        JOptionPane.showMessageDialog(null, "No puede realizar la operación la sesión no esta disponible", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "Operación invalida, posibles causas:\n"+
                                                    "1- La imagen ha sido actualizada\n"+
                                                    "2- El formato no es valido", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        
        }else if(evt.equals(btnRestaurarImagen)){

            int confirm = JOptionPane.showConfirmDialog(null, "¿Esta seguro?\nAl realizar este proceso la imagen por defecto será restaurada");

            if(confirm==JOptionPane.YES_OPTION){
            
                model = new UsuarioDAO();
                fileImage = new File("src/resources/images/user-profile.png");

                if(ControladorGeneral.CONTROLSESION!=null){

                    if(model.actualizarImagenUsuario(ControladorGeneral.CONTROLSESION.getUser(), fileImage)==true){

                        //actualizamos la imagen, asignando la nueva imagen al objeto tipo Usuario(sesion)
                        ControladorGeneral.CONTROLSESION.setImage(ControladorGeneral.toBlob(fileImage));
                        mostrarImagen(fileImage);            
                        JOptionPane.showMessageDialog(null, "La imagen ha sido restaurada de manera exitosa", "Éxito", JOptionPane.INFORMATION_MESSAGE);                    
                        fileImage=null;
                        ControladorGeneral.resetSesion();
                        
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
                    JOptionPane.showMessageDialog(null, "No puede realizar la operación la sesión no esta disponible", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                
            }

        }else if(evt.equals(btnActualizarUsuario)){
        
            if(!textFieldUsuario.getText().isEmpty()){
                
                if(textFieldUsuario.getText().length()>=4 && textFieldUsuario.getText().length()<=10){

                    if(ControladorGeneral.CONTROLSESION!=null){

                        if(!ControladorGeneral.CONTROLSESION.getUser().equals(textFieldUsuario.getText())){

                            int confirm = JOptionPane.showConfirmDialog(null, "¿Esta seguro?\nAl realizar este proceso se actualizará el nombre de usuario en la base de datos");

                            if(confirm==JOptionPane.YES_OPTION){

                                model = new UsuarioDAO();

                                if(model.actualizarNombreUsuario(textFieldUsuario.getText(), ControladorGeneral.CONTROLSESION.getId())){

                                    //actualizamos la imagen, asignando la nueva imagen al objeto tipo Usuario(sesion)
                                    ControladorGeneral.CONTROLSESION.setUser(textFieldUsuario.getText());
                                    JOptionPane.showMessageDialog(null, "El nombre de usuario a sido actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);                    
                                    ControladorGeneral.resetSesion();

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

                            }

                        }else{
                            JOptionPane.showMessageDialog(null, "El nombre de usuario ya esta actualizado", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }

                    }else{
                        JOptionPane.showMessageDialog(null, "No puede realizar la operación la sesión no esta disponible", "ERROR", JOptionPane.ERROR_MESSAGE);                
                    }
                
                }else{
                    JOptionPane.showMessageDialog(null, "Error en la operación, el rango de caracteres debe estar entre 4 y 10", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                
                
            }else{
                JOptionPane.showMessageDialog(null, "Ingrese un valor valido", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(evt.equals(btnRestaurarUsuario)){
        
            int confirm = JOptionPane.showConfirmDialog(null, "¿Esta seguro?\nAl realizar este proceso el nombre de usuario por defecto será restaurado");

            if(confirm==JOptionPane.YES_OPTION){
            
                model = new UsuarioDAO();
                String usuarioDefault="Admin";
                
                if(ControladorGeneral.CONTROLSESION!=null){

                    if(model.actualizarNombreUsuario(usuarioDefault, ControladorGeneral.CONTROLSESION.getId())){

                        //actualizamos la imagen, asignando la nueva imagen al objeto tipo Usuario(sesion)
                        ControladorGeneral.CONTROLSESION.setUser(usuarioDefault);
                        textFieldUsuario.setText(usuarioDefault);
                        JOptionPane.showMessageDialog(null, "El nombre de usuario ha sido actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);                    
                        ControladorGeneral.resetSesion();
                        
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
                    JOptionPane.showMessageDialog(null, "No puede realizar la operación la sesión no esta disponible", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                
            }
            
        }else if(evt.equals(btnActualizarPass)){
        
            if(!textFieldPass.getText().isEmpty()){
                
                if(textFieldPassUnmask.getText().length()>=4 && textFieldPassUnmask.getText().length()<=10){

                    if(ControladorGeneral.CONTROLSESION!=null){

                        if(!ControladorGeneral.CONTROLSESION.getPassword().equals(textFieldPassUnmask.getText())){

                            int confirm = JOptionPane.showConfirmDialog(null, "¿Esta seguro?\nAl realizar este proceso se actualizará el nombre de usuario en la base de datos");

                            if(confirm==JOptionPane.YES_OPTION){

                                model = new UsuarioDAO();

                                if(model.actualizarPasswordUsuario(textFieldPassUnmask.getText(), ControladorGeneral.CONTROLSESION.getId())){

                                    //actualizamos la imagen, asignando la nueva imagen al objeto tipo Usuario(sesion)
                                    ControladorGeneral.CONTROLSESION.setPassword(textFieldPassUnmask.getText());
                                    JOptionPane.showMessageDialog(null, "La contraseña ha sido actualizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);                    

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

                            }

                        }else{
                            JOptionPane.showMessageDialog(null, "La contraseña ya esta actualizada", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }

                    }else{
                        JOptionPane.showMessageDialog(null, "No puede realizar la operación la sesión no esta disponible", "ERROR", JOptionPane.ERROR_MESSAGE);                
                    }
                
                }else{
                    JOptionPane.showMessageDialog(null, "Error en la operación, el rango de caracteres debe estar entre 4 y 10", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                
                
            }else{
                JOptionPane.showMessageDialog(null, "Ingrese un valor valido", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            
        }else if(evt.equals(btnRestaurarPass)){
        
            int confirm = JOptionPane.showConfirmDialog(null, "¿Esta seguro?\nAl realizar este proceso la contraseña del usuario será restaurada");

            if(confirm==JOptionPane.YES_OPTION){
            
                model = new UsuarioDAO();
                String passwordDefault="1234";
                
                if(ControladorGeneral.CONTROLSESION!=null){

                    if(model.actualizarPasswordUsuario(passwordDefault, ControladorGeneral.CONTROLSESION.getId())){

                        //actualizamos la imagen, asignando la nueva imagen al objeto tipo Usuario(sesion)
                        ControladorGeneral.CONTROLSESION.setPassword(passwordDefault);
                        textFieldPass.setText(passwordDefault);
                        textFieldPassUnmask.setText(passwordDefault);
                        JOptionPane.showMessageDialog(null, "La contraseña del usuario a sido actualizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);                    
                        
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
                    JOptionPane.showMessageDialog(null, "No puede realizar la operación la sesión no esta disponible", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                
            }
            
            
        }else if(evt.equals(btnLimpiarUser)){
            
            if(ControladorGeneral.CONTROLSESION!=null){
                textFieldUsuario.setText(ControladorGeneral.CONTROLSESION.getUser());
            }
            
        }else if(evt.equals(btnLimpiarPass)){

            if(ControladorGeneral.CONTROLSESION!=null){
                textFieldPass.setText(ControladorGeneral.CONTROLSESION.getPassword());
                textFieldPassUnmask .setText(ControladorGeneral.CONTROLSESION.getPassword());
            }
        
        }else if(evt.equals(btnGuardarDatosEmail)){
            
            if(!textFieldEmail.getText().isEmpty() && !textFieldPassEmailUnmask.getText().isEmpty()){
                
                if(ControladorGeneral.CONTROLSESION!=null){

                    int confirm = JOptionPane.showConfirmDialog(null, "¿Esta seguro?\nAl realizar este proceso el E-mail por defecto sera actualizado en la base de datos");

                    if(confirm==JOptionPane.YES_OPTION){
                        
                        model = new UsuarioDAO();
                        
                        if(model.actualizarEmailUsuario(textFieldEmail.getText(), textFieldPassEmailUnmask.getText(), ControladorGeneral.CONTROLSESION.getId())){

                            ControladorGeneral.CONTROLSESION.setEmail(textFieldEmail.getText());
                            ControladorGeneral.CONTROLSESION.setPasswordEmail(textFieldPassEmailUnmask.getText());
                            JOptionPane.showMessageDialog(null, "El E-mail y Contraseña ha sido actualizada", "Éxito", JOptionPane.INFORMATION_MESSAGE);                    

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

                    }

                }else{
                    JOptionPane.showMessageDialog(null, "No puede realizar la operación la sesión no esta disponible", "ERROR", JOptionPane.ERROR_MESSAGE);                
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "Debe llenar los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(evt.equals(btnLimpiarEmail)){

            if(ControladorGeneral.CONTROLSESION!=null){
                textFieldEmail.setText(ControladorGeneral.CONTROLSESION.getEmail());
                textFieldPassEmail.setText(ControladorGeneral.CONTROLSESION.getPasswordEmail());
                textFieldPassEmailUnmask.setText(ControladorGeneral.CONTROLSESION.getPasswordEmail());
            }        

        }else if(evt.equals(btnEnviarMail)){
            
            if(ControladorGeneral.CONTROLSESION!=null){
                
                if(!ControladorGeneral.CONTROLSESION.getEmail().equals("") && 
                   !ControladorGeneral.CONTROLSESION.getPasswordEmail().equals("")){

                    String pass= ControladorGeneral.CONTROLSESION.getPasswordEmail();
                    String de =  ControladorGeneral.CONTROLSESION.getEmail();
                    String asunto = textFieldAsunto.getText();
                    String mensaje = htmlEditorMensaje.getHtmlText();
                    String para="";
                    
                    boolean condicion=false;
                    
                    if(checkBoxTodo.isSelected()){

                        VotantesDAO votantes = new VotantesDAO();
                        listEmail = votantes.consultarEmails();
                        if(listEmail!=null){
                            condicion=true;                        
                        }

                    }else{

                        if(!textFieldPara.getText().isEmpty()){                            
                            listEmail = ControladorGeneral.toArrayString(textFieldPara.getText());                            
                            condicion=true;
                        }else{
                            JOptionPane.showMessageDialog(null, "Debe ingresar un E-mail valido o marcar la opción todos", "ERROR", JOptionPane.ERROR_MESSAGE);    
                        }

                    }
                    
                    if(condicion){
                                                
                        //creamos un hilo para generar el documento
                        Task task = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {

                                labelBarra.setVisible(true);
                                barraDeProgreso.setVisible(true);

                                Address emails[]= null;
                                if(listEmail!=null){
                                    emails = ControladorGeneral.toArrayAddress(listEmail);
                                }

                                if(fileAdjuntar!=null){

                                    Mail mail = new Mail(de, pass, emails, asunto, mensaje, fileAdjuntar);
                                    if(mail.sendEmailWithFile()){
                                        labelBarra.setVisible(false);
                                        barraDeProgreso.setVisible(false);                                        
                                        JOptionPane.showMessageDialog(null, "Operación Éxitosa, El E-mail se ha enviado de manera correcta", "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
                                    }else{
                                        JOptionPane.showMessageDialog(null, "Operación Invalida, Error al enviar E-mail posibles errores:\n"+
                                                "1- El correo electronico no existe\n"+
                                                "2- No hay una conexión a internet\n"+
                                                "3- No tiene autorización para realizar la operación", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    }

                                }else{

                                    Mail mail = new Mail(de, pass, emails, asunto, mensaje, null);
                                    if(mail.sendEmail()){
                                        labelBarra.setVisible(false);
                                        barraDeProgreso.setVisible(false);                                        
                                        JOptionPane.showMessageDialog(null, "Operación Éxitosa, El E-mail se ha enviado de manera correcta", "ÉXITO", JOptionPane.INFORMATION_MESSAGE);
                                    }else{
                                        JOptionPane.showMessageDialog(null, "Operación Invalida, Error al enviar E-mail posibles errores:\n"+
                                                "1- El correo electronico no existe\n"+
                                                "2- No hay una conexión a internet\n"+
                                                "3- No tiene autorización para realizar la operación", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    }

                                }
                                return null;
                            }

                        };

                        task.setOnSucceeded((Event event1) -> {
                            labelBarra.setVisible(false);
                            barraDeProgreso.setVisible(false);
                        });

                        new Thread(task).start();
                    
                    }else{
                        JOptionPane.showMessageDialog(null, "Operacion fallida, sin destinatarios\n"+
                                                            "1- No hay correos registrados en la base de datos\n"+
                                                            "2- No ha ingresado valores validos en el campo destinatario", 
                                                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    
                                        
                }else{
                    JOptionPane.showMessageDialog(null, "Aun no ha registrado el E-mail, \n"+
                                                        "por favor vaya ajustes de usuario y realice este proceso", 
                                                        "ERROR", JOptionPane.ERROR_MESSAGE);            
                }
            
            }else{
                JOptionPane.showMessageDialog(null, "El usuario no esta disponible por favor intente mas tarde", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            

        }else if(evt.equals(btnLimpiarMail)){
        
            htmlEditorMensaje.setHtmlText("");
            textFieldAsunto.setText("");
            checkBoxTodo.setSelected(true);
            fileAdjuntar=null;
            linkAdjunto.setText("");
            
        }else if(evt.equals(checkBoxTodo)){
            
            if(!checkBoxTodo.isSelected()){
                textFieldPara.setEditable(true);
                labelInfo.setVisible(true);            
            }else{
                textFieldPara.setEditable(false);
                labelInfo.setVisible(false);                        
            }
            
            
        }else if(evt.equals(btnAdjuntar)){
            
            boolean condicion = true;
            
            while(condicion){

                File file = filechooser2.showOpenDialog(null);

                if(file!=null){

                    long fileSizeInBytes = file.length();
                    long fileSizeInKB = fileSizeInBytes / 1024; 

                    if(fileSizeInKB < 11000){
                        fileAdjuntar = file;
                        condicion=false;
                    }

                }else{
                    condicion=false;
                }
            
            }
            
            if(fileAdjuntar!=null){
                linkAdjunto.setText(fileAdjuntar.getName());            
            }
            
        }
        
    }

    @FXML
    public void eventsKeyTyped(KeyEvent event){
        
        Object evt = event.getSource();
        
        if(evt.equals(textFieldUsuario)){

            if(!ControladorValidaciones.validateLegth(textFieldUsuario.getText())){                
                event.consume();                
            }
            
        }else if(evt.equals(textFieldPass)){

            if(!ControladorValidaciones.validateLegth(textFieldPass.getText())){
                event.consume();
            }
        
        }else if(evt.equals(textFieldPassUnmask)){

            if(!ControladorValidaciones.validateLegth(textFieldPassUnmask.getText())){
                event.consume();
            }
        
        }else if(evt.equals(htmlEditorMensaje)){
                

        }


    }
    
    @FXML
    public void eventsKeyPressed(KeyEvent event){
        
        Object evt = event.getSource();
        
        if(evt.equals(htmlEditorMensaje)){
            
            if(event.getCode()==KeyCode.ENTER){
            
                
                
            }
            
        }
    }
    
    @Override
    public void initComponents(Object obj) {

        this.fileImage=null;
        this.filechooser = new FileChooser();
        this.filechooser2 = new FileChooser();
        accordionConfig.setExpandedPane(titledPaneUsuario);
        maskPassword(textFieldPass, textFieldPassUnmask, checkVerPass);
        maskPassword(textFieldPassEmail, textFieldPassEmailUnmask, checkVerPassEmail);

        mostrarDatosUsuario();
        
        /*agregamos los tooltip text informativo para los respectivos botones*/
        addTooltipText(btnCargarImagen, "Cargar una nueva imagen");
        addTooltipText(btnActualizarImagen, "Actualizar imagen en base de datos");
        addTooltipText(btnRestaurarImagen, "Restaurar los valores por defecto");
        addTooltipText(btnActualizarUsuario, "Actualizar nombre de usuario en base de datos");
        addTooltipText(btnLimpiarUser, "Deshacer cambios");
        addTooltipText(btnLimpiarPass, "Deshacer cambios");
        
        textFieldDe.setText("jorgepotes1994@gmail.com");
        textFieldDe.setEditable(false);
        textFieldPara.setEditable(false);
        checkBoxTodo.setSelected(true);
        labelInfo.setVisible(false);
        
        labelBarra.setVisible(false);
        barraDeProgreso.setVisible(false);
        
    }

    @Override
    public void controlVisibilidad(Node node, boolean condicion) {
        
    }
    
    
}

package controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import model.Persona;
import model.Votantes;
import utlidades.ControladorGeneral;
import utlidades.UtilidadesView;

/**
 *
 * @author jorge
 */
public class PrincipalController implements Initializable, UtilidadesView {
    
    @FXML
    public BorderPane borderP;
        
    public GridPane panelInicio, panelRegistro, panelRegistroLider, panelConsulta,
             panelLugarDeVotacion, panelReporte, panelAjuste, panelGuia;
    
    @FXML
    private JFXButton btnInicio, btnRegistroPersona, btnRegistroLider, 
                      btnConsulta, btnReportes, btnLugarDeVotacion,
                      btnAjustes, btnSalir, btnAcercaDe, btnRecomendacion, 
                      btnGuia;

    @FXML
    private FontAwesomeIcon fontIconInicio, fontIconSalir, 
                            fontIconAjustes, fontIconLugar,
                            fontIconReporte, fontIconConsulta,
                            fontIconRegistro, fontIconLider, 
                            fontIconGuia;
    
    @FXML
    public Circle imageProfile;

    @FXML
    private JFXButton btnAjustesSubMenu, btnSalirSubMenu;

    @FXML
    public Label lbNombreUsuario;
    
    FXMLLoader loader = new FXMLLoader();
    public static FXMLLoader loaderAjustes = new FXMLLoader();
    public static FXMLLoader loaderReportes = new FXMLLoader();

    
    Stop[] stopsActive = new Stop[]{new Stop(0, Color.DARKCYAN), new Stop(1, Color.CYAN)};
    LinearGradient linearActive = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stopsActive);
    
    ScrollPane pane = new ScrollPane();
    
    @FXML
    public void validatorMenu(ActionEvent event){

        Object evt = event.getSource();
        
        if(evt.equals(btnInicio)){

            selectView("Inicio", null, "");
                        
        }else if(evt.equals(btnRegistroPersona) ){

            selectView("Registro", null, "");
            
        }else if(evt.equals(btnRegistroLider) ){

            selectView("Registro_Lider", null, "");
            
        }else if(evt.equals(btnConsulta)){

            selectView("Consulta", null, "");
            
        }else if(evt.equals(btnReportes)){

            selectView("Reporte", null, "");
            
        }else if(evt.equals(btnLugarDeVotacion)){

            selectView("Lugar", null, "");
            
        }else if(evt.equals(btnAjustes) || evt.equals(btnAjustesSubMenu)){

            selectView("Ajuste", null, "");
        
        }else if(evt.equals(btnGuia)){

            selectView("Guia", null, "");
            
        }else if(evt.equals(btnSalir) || evt.equals(btnSalirSubMenu)){
            
            int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea salir del sistema?");
            
            if(confirmar==JOptionPane.YES_OPTION){
                System.exit(0);
            }
            
        }else if(evt.equals(btnAcercaDe)){
            
            loadModal("/view/AcercaDe.fxml", "VBox");
        
        }else if(evt.equals(btnRecomendacion)){

            loadModal("/view/Recomendacion.fxml", "ScrollPane");            
        
        }
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnInicio.setTextFill(linearActive);
        fontIconInicio.setFill(linearActive);        
        
        Image image = new Image("/resources/images/user-profile.png", false);
        imageProfile.setFill(new ImagePattern(image));

        String tip1 = "Módulo para registrar \npersonal de votación";
        ControladorGeneral.addTooltipText(btnRegistroPersona, tip1, "toolTipText");
        
        tip1 = "Realiza consultas del personal \nregistrado en base de datos";
        ControladorGeneral.addTooltipText(btnConsulta, tip1, "toolTipText");

        tip1 = "Genera reportes en excel";
        ControladorGeneral.addTooltipText(btnReportes, tip1, "toolTipText");

        tip1 = "Consulta el lugar y \nmesa de votación";
        ControladorGeneral.addTooltipText(btnLugarDeVotacion, tip1, "toolTipText");

        ControladorGeneral.addTooltipText(btnSalirSubMenu, "Salir del sistema", "toolTipTextAjustes");
        ControladorGeneral.addTooltipText(btnAjustesSubMenu, "Ir a preferencias del sistema", "toolTipTextAjustes");
        ControladorGeneral.addTooltipText(btnAcercaDe, "Información del software", "toolTipTextAjustes");
        ControladorGeneral.addTooltipText(btnRecomendacion, "Información general del sistema", "toolTipTextAjustes");

        try {
            
            panelInicio =  (GridPane) FXMLLoader.load(getClass().getResource("/view/PanelInicio.fxml"));
            panelRegistro =  (GridPane) FXMLLoader.load(getClass().getResource("/view/PanelRegister.fxml"));
            panelRegistroLider= (GridPane) FXMLLoader.load(getClass().getResource("/view/PanelLider.fxml"));
            //panelConsulta =  (GridPane) FXMLLoader.load(getClass().getResource("/view/PanelConsulta.fxml"));
            
            loader = new FXMLLoader(getClass().getResource("/view/PanelConsulta.fxml"));        
            panelConsulta = (GridPane)loader.load();
            
            loaderReportes= new FXMLLoader(getClass().getResource("/view/PanelReporte.fxml"));
            panelReporte =  (GridPane) loaderReportes.load();
            
            panelLugarDeVotacion =  (GridPane) FXMLLoader.load(getClass().getResource("/view/PanelLugarDeVotacion.fxml"));

            panelGuia =  (GridPane) FXMLLoader.load(getClass().getResource("/view/PanelGuia.fxml"));            
            
            loaderAjustes = new FXMLLoader(getClass().getResource("/view/PanelAjustes.fxml"));
            panelAjuste =  (GridPane) loaderAjustes.load();
            
            addOpacity(panelRegistro, 0.0);
            addOpacity(panelConsulta, 0.0);
            addOpacity(panelReporte, 0.0);
            addOpacity(panelLugarDeVotacion, 0.0);
            addOpacity(panelAjuste, 0.0);

            borderP.setCenter(panelInicio);
            BorderPane.setMargin(panelInicio, new Insets(0, 30, 0, 0));
            redimensionarHeightPanel(borderP.getPrefHeight(), panelInicio.getPrefHeight());
            
                    
        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
       
    private void makeFadeOut(GridPane container){
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(700));
        fade.setNode(container);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void addOpacity(Node node, double value){
        node.setOpacity(value);
    }    

    
    public void selectView(String name, Object object, String type){

        Votantes v=null;
        Persona p=null;
        
        switch(type){
            
            case "Votantes":
                v = (Votantes) object;
            break;
            
            case "Persona":           
                p = (Persona) object;
            break;
            
        }
        
        switch(name){
            
            case "Inicio":

                fillActive("Inicio");
                fillDefault("Registro");
                fillDefault("Registro_Lider");
                fillDefault("Consulta");
                fillDefault("Reporte");
                fillDefault("Lugar");
                fillDefault("Ajuste");
                fillDefault("Guia");

                addOpacity(panelInicio, 0.0);
                borderP.setCenter(panelInicio);
                BorderPane.setMargin(panelInicio, new Insets(0, 30, 0, 0));                
                makeFadeOut(panelInicio);
                redimensionarHeightPanel(borderP.getPrefHeight(), panelInicio.getPrefHeight());
    
            break;
                
            case "Registro":

                if(ControladorGeneral.CONTROLVIEWMODIFICAR==1){
                    
                    loader = new FXMLLoader(getClass().getResource("/view/PanelRegister.fxml"));        
                    {
                        try {
                            panelRegistro = (GridPane)loader.load();     
                            PanelRegistroController registroController = loader.getController();
                            registroController.ActualizarDatos(v);
                        } catch (IOException ex) {
                            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }                
                
                fillActive("Registro");
                fillDefault("Registro_Lider");                
                fillDefault("Inicio");
                fillDefault("Consulta");
                fillDefault("Reporte");
                fillDefault("Lugar");
                fillDefault("Ajuste");
                fillDefault("Guia");

                borderP.setCenter(panelRegistro);
                BorderPane.setMargin(panelRegistro, new Insets(0, 30, 0, 0));                        
                makeFadeOut(panelRegistro);
                redimensionarHeightPanel(borderP.getPrefHeight(), panelRegistro.getPrefHeight());
    
            break;

            case "Registro_Lider":
                
                if(ControladorGeneral.CONTROLVIEWMODIFICARLIDER==1){
                    
                    loader = new FXMLLoader(getClass().getResource("/view/PanelLider.fxml"));        
                    {
                        try {
                            panelRegistroLider = (GridPane)loader.load();     
                            PanelLiderController registroController = loader.getController();
                            registroController.ActualizarDatos(p);
                        } catch (IOException ex) {
                            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }              
                
                fillActive("Registro_Lider");
                fillDefault("Registro");
                fillDefault("Inicio");
                fillDefault("Consulta");
                fillDefault("Reporte");
                fillDefault("Lugar");
                fillDefault("Ajuste");
                fillDefault("Guia");

                borderP.setCenter(panelRegistroLider);
                BorderPane.setMargin(panelRegistroLider, new Insets(0, 30, 0, 0));                        
                makeFadeOut(panelRegistroLider);
                redimensionarHeightPanel(borderP.getPrefHeight(), panelRegistroLider.getPrefHeight());
    
            break;
                
            case "Consulta":
                                                
                fillActive("Consulta");
                fillDefault("Inicio");
                fillDefault("Registro");
                fillDefault("Registro_Lider");                
                fillDefault("Reporte");
                fillDefault("Lugar");
                fillDefault("Ajuste");
                fillDefault("Guia");

                borderP.setCenter(panelConsulta);
                BorderPane.setMargin(panelConsulta, new Insets(0, 30, 0, 0));                
                makeFadeOut(panelConsulta);
                redimensionarHeightPanel(borderP.getPrefHeight(), panelConsulta.getPrefHeight());
                
            break;
            
            case "Lugar":

                fillActive("Lugar");
                fillDefault("Inicio");
                fillDefault("Registro");
                fillDefault("Registro_Lider");                
                fillDefault("Consulta");
                fillDefault("Reporte");
                fillDefault("Ajuste");
                fillDefault("Guia");

                borderP.setCenter(panelLugarDeVotacion);
                BorderPane.setMargin(panelLugarDeVotacion, new Insets(0, 30, 0, 0));                
                makeFadeOut(panelLugarDeVotacion);
                redimensionarHeightPanel(borderP.getPrefHeight(), panelLugarDeVotacion.getPrefHeight());
                
            break;            

            case "Reporte":

                fillActive("Reporte");
                fillDefault("Inicio");
                fillDefault("Registro");
                fillDefault("Registro_Lider");                
                fillDefault("Consulta");
                fillDefault("Lugar");
                fillDefault("Ajuste");
                fillDefault("Guia");

                borderP.setCenter(panelReporte);
                BorderPane.setMargin(panelReporte, new Insets(0, 30, 0, 0));                                
                makeFadeOut(panelReporte);
                redimensionarHeightPanel(borderP.getPrefHeight(), 900);                
                                
            break;

            case "Ajuste":

                fillActive("Ajuste");
                fillDefault("Inicio");
                fillDefault("Registro");
                fillDefault("Registro_Lider");                
                fillDefault("Consulta");
                fillDefault("Reporte");
                fillDefault("Lugar");
                fillDefault("Guia");

                borderP.setCenter(panelAjuste);
                BorderPane.setMargin(panelAjuste, new Insets(0, 30, 0, 0));                                
                makeFadeOut(panelAjuste);
                redimensionarHeightPanel(borderP.getPrefHeight(), panelAjuste.getPrefHeight());                
                                
            break;

            case "Guia":
                
                fillActive("Guia");
                fillDefault("Ajuste");
                fillDefault("Inicio");
                fillDefault("Registro");
                fillDefault("Registro_Lider");                
                fillDefault("Consulta");
                fillDefault("Reporte");
                fillDefault("Lugar");
                
                borderP.setCenter(panelGuia);
                BorderPane.setMargin(panelGuia, new Insets(0, 30, 0, 0));                                
                makeFadeOut(panelGuia);
                redimensionarHeightPanel(borderP.getPrefHeight(), panelGuia.getPrefHeight());                
                                
            break;            
            
        
        }
    }
    
    public void redimensionarHeightPanel(double dimension1, double dimension2){

        //if(dimension1<dimension2){
            borderP.setPrefHeight(dimension2+70);
        //}else{
          //  borderP.setPrefHeight(dimension2);            
        //}
        
    }
    
    
    /*Metodo interfaz UtilidadesView controlara que item del menu esta activo*/

    @Override
    public void fillActive(String navigate) {
        
        switch(navigate){
            
            case "Inicio":
                btnInicio.setTextFill(linearActive);
                fontIconInicio.setFill(linearActive);                                        
            break;
                
            case "Registro":
                btnRegistroPersona.setTextFill(linearActive);
                fontIconRegistro.setFill(linearActive);                                        
            break;

            case "Registro_Lider":
                btnRegistroLider.setTextFill(linearActive);
                fontIconLider.setFill(linearActive);                                        
            break;                
                
            case "Consulta":
                btnConsulta.setTextFill(linearActive);
                fontIconConsulta.setFill(linearActive);                                        
            break;
            
            case "Lugar":
                btnLugarDeVotacion.setTextFill(linearActive);
                fontIconLugar.setFill(linearActive);                                        
            break;            

            case "Reporte":
                btnReportes.setTextFill(linearActive);
                fontIconReporte.setFill(linearActive);                        
            break;

            case "Ajuste":
                btnAjustes.setTextFill(linearActive);
                fontIconAjustes.setFill(linearActive);                                        
            break;

            case "Guia":
                btnGuia.setTextFill(linearActive);
                fontIconGuia.setFill(linearActive);                                        
            break;            
        }

    }

    @Override
    public void fillDefault(String navigate) {
        switch(navigate){
            
            case "Inicio":
                btnInicio.setTextFill(Color.web("#999999"));
                fontIconInicio.setFill(Color.web("#999999"));                                        
            break;
                
            case "Registro":
                btnRegistroPersona.setTextFill(Color.web("#999999"));
                fontIconRegistro.setFill(Color.web("#999999"));                                        
            break;

            case "Registro_Lider":
                btnRegistroLider.setTextFill(Color.web("#999999"));
                fontIconLider.setFill(Color.web("#999999"));                                        
            break;                     
                
            case "Consulta":
                btnConsulta.setTextFill(Color.web("#999999"));
                fontIconConsulta.setFill(Color.web("#999999"));                                        
            break;
            
            case "Lugar":
                btnLugarDeVotacion.setTextFill(Color.web("#999999"));
                fontIconLugar.setFill(Color.web("#999999"));                                        
            break;            

            case "Reporte":
                btnReportes.setTextFill(Color.web("#999999"));
                fontIconReporte.setFill(Color.web("#999999"));                        
            break;

            case "Ajuste":
                btnAjustes.setTextFill(Color.web("#999999"));
                fontIconAjustes.setFill(Color.web("#999999"));                                        
            break;

            case "Guia":
                btnGuia.setTextFill(Color.web("#999999"));
                fontIconGuia.setFill(Color.web("#999999"));                                        
            break;            
        }
    }

    private void loadModal(String url, String type) {

        try {

            Stage stage = new Stage();
            
            URL resource = getClass().getResource("/resources/images/elections.png");        
            stage.getIcons().add(new Image(resource.toString()));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            Parent parent = null;
            switch(type){
                case "VBox":
                    parent =  (VBox) FXMLLoader.load(getClass().getResource(url));                                     
                 break;
                 
                case "ScrollPane": 
                    parent =  (ScrollPane) FXMLLoader.load(getClass().getResource(url));                                     
                break;
            }
            Scene scene = new Scene(parent);                
            stage.setScene(scene);
            stage.show();

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);               
                        
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar modulo\n"+
                                                 ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    
}

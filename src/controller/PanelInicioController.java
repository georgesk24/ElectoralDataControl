package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import view.ElectoralDataControl;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelInicioController implements Initializable {

    @FXML
    private JFXButton btnCardRegistrar, btnCardConsulta, btnCardReporte, 
                      btnCardLugarDeVotacion, btnCardAjustes, btnCardRegistrarLider;
    
 
        
    @FXML
    public void validatorMenu(ActionEvent event){
        
        Object evt = event.getSource();
        
        PrincipalController principalController = ElectoralDataControl.loader.getController();
        
        if(evt.equals(btnCardRegistrar)){
           principalController.selectView("Registro", null, "");        
        }else if(evt.equals(btnCardRegistrarLider)){
           principalController.selectView("Registro_Lider", null, "");                
        }else if(evt.equals(btnCardConsulta)){
           principalController.selectView("Consulta", null, "");                
        }else if(evt.equals(btnCardReporte)){
           principalController.selectView("Reporte", null, "");                
        }else if(evt.equals(btnCardLugarDeVotacion)){
           principalController.selectView("Lugar", null, "");                
        }else if(evt.equals(btnCardAjustes)){
           principalController.selectView("Ajuste", null, "");                
        }

        
    }
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        
        
    }    




    
}

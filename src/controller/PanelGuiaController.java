package controller;

import com.jfoenix.controls.JFXButton;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import org.icepdf.ri.util.FontPropertiesManager;
import org.icepdf.ri.util.PropertiesManager;
import utlidades.ControladorGeneral;
import utlidades.GeneralView;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelGuiaController implements Initializable, GeneralView {

    @FXML
    private Accordion accordionConfig;
        
    @FXML
    private TitledPane titledPaneInicio;
    
    @FXML
    private VBox vBoxSeccion, vBoxError;
    
    @FXML
    private JFXButton btnSeccionesDeSoftware, btnListadoDeErrores;
    
    
    @FXML
    public void eventsOnAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnSeccionesDeSoftware)){
            
            try {
                Desktop.getDesktop().open(new File("src/resources/pdf-files/secciones-de-software.pdf"));                        
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
        }else if(evt.equals(btnListadoDeErrores)){
            
            try {
                Desktop.getDesktop().open(new File("src/resources/pdf-files/control-errores-electoral-data-control.pdf"));                        
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }            
            
        }
    
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
        
    public void openFilePdf(String url, VBox vbox){
        
        try{

            SwingUtilities.invokeAndWait(() -> {
                SwingController control = new SwingController();
                
                Properties properties = new Properties();
                properties.put("application.showLocalStorageDialogs", "false");
                
                
                PropertiesManager props = new PropertiesManager(System.getProperties(), properties,
                        ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));
                
                props.setInt(PropertiesManager.PROPERTY_DEFAULT_PAGEFIT, DocumentViewController.PAGE_FIT_WINDOW_WIDTH);
                props.setInt("document.viewtype", DocumentViewControllerImpl.ONE_COLUMN_VIEW);
                
                
                control.setPropertiesManager(props);
                new FontPropertiesManager(props, System.getProperties(), ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));
                
                SwingViewBuilder factry = new SwingViewBuilder(control);
                JPanel panel = factry.buildViewerPanel();
                
                ComponentKeyBinding.install(control, panel);
                
                control.getDocumentViewController().setAnnotationCallback(
                        new org.icepdf.ri.common.MyAnnotationCallback(
                                control.getDocumentViewController()));
                
                control.openDocument(url);
                
                SwingNode node = new SwingNode();
                node.setContent(panel);
                vbox.getChildren().add(node);
            });
                        
        }catch(InterruptedException | InvocationTargetException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void initComponents(Object obj) {

        accordionConfig.setExpandedPane(titledPaneInicio);
        openFilePdf("src/resources/pdf-files/control-errores-electoral-data-control.pdf", vBoxError);
        openFilePdf("src/resources/pdf-files/secciones-de-software.pdf", vBoxSeccion);
        
    }

    @Override
    public void controlVisibilidad(Node node, boolean condicion) {
        
    }
    
    
}

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
    private VBox vBoxSeccion;
    
    @FXML
    private JFXButton btnSeccionesDeSoftware, btnListadoDeErrores;
    
    
    @FXML
    public void eventsOnAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnSeccionesDeSoftware)){
            
            try {

                switch(ControladorGeneral.getOs()){
                    
                    case "Linux":

                        Desktop.getDesktop().open(new File("src/resources/pdf-files/secciones-de-software.pdf"));
                        break;
                        
                    case "Windows":

                        Desktop.getDesktop().open(new File("src/resources/pdf-files/secciones-de-software.pdf"));
                        break;
                        
                }
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
        }else if(evt.equals(btnListadoDeErrores)){
            
            
            
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
        
    
    @Override
    public void initComponents(Object obj) {

        try {
           
            accordionConfig.setExpandedPane(titledPaneInicio);
           
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {

                    /*
                    SwingController control = new SwingController();
                    control.setIsEmbeddedComponent(true);
                    PropertiesManager properties = new PropertiesManager(System.getProperties(),
                            ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));

                    
                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT, "false");
                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ROTATE, "false");
                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_TOOL, "false");
                    properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "false");
                    properties.setBoolean(PropertiesManager.PROPERTY_SHOW_STATUSBAR_VIEWMODE, Boolean.FALSE);
                    properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_PAGENAV, "false");

                    ResourceBundle messageBundle = ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);
                    
                    new FontPropertiesManager(properties, System.getProperties(), messageBundle);
                    
                    control.getDocumentViewController().setAnnotationCallback(
                            new org.icepdf.ri.common.MyAnnotationCallback(
                                    control.getDocumentViewController()));
                    
                    SwingViewBuilder factry = new SwingViewBuilder(control, properties);
                    JPanel panel = factry.buildViewerPanel();
                    panel.revalidate();
                    
                    SwingNode swingNode = new SwingNode();
                    swingNode.setContent(panel);

                    BorderPane pane = new BorderPane();
                    pane.setCenter(swingNode);
                    */

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
            
            control.openDocument("src/resources/pdf-files/secciones-de-software.pdf");
                    
            SwingNode node = new SwingNode();
            node.setContent(panel);
            vBoxSeccion.getChildren().add(node);
                    

                    
                }
            });
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(PanelGuiaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(PanelGuiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @Override
    public void controlVisibilidad(Node node, boolean condicion) {
        
    }
    
    
}

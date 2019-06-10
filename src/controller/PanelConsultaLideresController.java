
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.Lider;
import model.Persona;
import modelDAO.LiderDAO;
import org.controlsfx.control.RangeSlider;
import utlidades.ComponentesTabla;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;
import view.ElectoralDataControl;

/**
 * FXML Controller class
 *
 * @author lenov0o
 */
public class PanelConsultaLideresController implements Initializable, ComponentesTabla, GeneralView {

    @FXML
    private JFXComboBox<String> fxCombotipoBusquedaLider, tipoDocumentoLider; 
    
    @FXML
    private JFXTextField numeroDocumentoLider, busquedaLider;

    @FXML
    private JFXRadioButton radioMaleLider, radioFemaleLider;
    
    @FXML 
    private Label rangoEdadValorLider;
    
    @FXML
    private JFXButton btnBuscarLider, btnNuevoLider, btnModificarLider, 
                      btnEliminarLider, btnActualizarTablaLider;
    
    @FXML
    private HBox hBoxSexoLider, hBoxEdadLider, hBoxBusquedaLider, 
                 hBoxNumeroDocumentoLider, hBoxBtnLider;
    
    @FXML
    private JFXTreeTableView<ControlTableLideres> tableViewLider;
        
    @FXML
    private RangeSlider rangeSliderEdadLider;
    
    private JFXTreeTableColumn<ControlTableLideres, String> columnNames, columnNum, columnNumDoc, columnSexo,
                                                columnTelefono, columnCorreo;

    private ObservableList<ControlTableLideres> datos;
    private TreeItem<ControlTableLideres> itemRow;
    
    
    private final LiderDAO model = new LiderDAO();
    
    
    /*Controlador de componentes visuales*/
    private final ToggleGroup groupRadio = new ToggleGroup();
    private final RequiredFieldValidator validatorFieldText = new RequiredFieldValidator();
    private final NumberValidator validatorNumber = new NumberValidator();
    
    
    
    
    @FXML
    public void eventosOnAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(fxCombotipoBusquedaLider)){
            
            String value =fxCombotipoBusquedaLider.getValue();
            
            switch(value){
                
                case "Número de Documento":
                    hBoxNumeroDocumentoLider.setVisible(true);
                    hBoxBtnLider.setVisible(true);
                    hBoxBusquedaLider.setVisible(false);
                    hBoxEdadLider.setVisible(false);
                    hBoxSexoLider.setVisible(false);
                break;

                case "Nombre/Apellido":
                case "Barrio":
                    hBoxBusquedaLider.setVisible(true);
                    hBoxBtnLider.setVisible(true);
                    hBoxNumeroDocumentoLider.setVisible(false);
                    hBoxEdadLider.setVisible(false);
                    hBoxSexoLider.setVisible(false);
                break;                
 
                case "Edad":
                    hBoxEdadLider.setVisible(true);                
                    hBoxBtnLider.setVisible(true);                   
                    hBoxBusquedaLider.setVisible(false);                    
                    hBoxNumeroDocumentoLider.setVisible(false);
                    hBoxSexoLider.setVisible(false);
                break;
                
                case "Sexo":
                    hBoxSexoLider.setVisible(true);                
                    hBoxBtnLider.setVisible(true);                    
                    hBoxEdadLider.setVisible(false);
                    hBoxBusquedaLider.setVisible(false);                    
                    hBoxNumeroDocumentoLider.setVisible(false);
                break;
                
                default:
                    hBoxSexoLider.setVisible(false);                
                    hBoxBtnLider.setVisible(true);
                    hBoxEdadLider.setVisible(false);                                    
                    hBoxBusquedaLider.setVisible(false);                    
                    hBoxNumeroDocumentoLider.setVisible(false);
                break;                                
            }
        
        }else if(evt.equals(btnBuscarLider)){

            buscarDatos();
            
        }else if(evt.equals(btnNuevoLider)){            

            PrincipalController principalController = ElectoralDataControl.loader.getController();
            principalController.selectView("Registro_Lider", null, "");
            
        }else if(evt.equals(btnModificarLider)){
            
            TreeTableView.TreeTableViewSelectionModel<ControlTableLideres> modelTable = tableViewLider.getSelectionModel();
            if(modelTable.getSelectedIndex() > -1){
                
                TreeItem<ControlTableLideres> d = modelTable.getSelectedItem();
                ControlTableLideres c = d.getValue();
                Lider v = c.getLider();
                                
                ControladorGeneral.CONTROLVIEWMODIFICARLIDER=1;
                PrincipalController principalController = ElectoralDataControl.loader.getController();
                principalController.selectView("Registro_Lider", v, "Persona");                                   
            
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una celda valida", "Operación fallida", JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(evt.equals(btnEliminarLider)){
            
            TreeTableView.TreeTableViewSelectionModel<ControlTableLideres> modelTable = tableViewLider.getSelectionModel();
            if(modelTable.getSelectedIndex() > -1){
                
                TreeItem<ControlTableLideres> d = modelTable.getSelectedItem();
                ControlTableLideres c = d.getValue();
                Lider v = c.getLider();
                int id = v.getId();
                
                int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro?\nsi realiza esta operación el registro será eliminado definitivamente de la base de datos");
                
                if(confirmar==JOptionPane.YES_OPTION){
                 
                    if(model.eliminarLider(id)==true){
                        
                        JOptionPane.showMessageDialog(null, "La información ha sido eliminada exitosamente de la base de datos", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);

                        ArrayList<Lider> list;
                        
                        list = model.consultaLider("Todo", null);
                        addRow(list);                        

                    }else{
                         JOptionPane.showMessageDialog(null, "Operación invalida, Posibles errores : \n"+
                                                             ControladorValidaciones.EXCEPCIONES, 
                                                             "ERROR", JOptionPane.ERROR_MESSAGE);           
                         ControladorValidaciones.EXCEPCIONES="";
                    }
                
                }
            
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una celda valida", "Operación fallida", JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(evt.equals(btnActualizarTablaLider)){
            
            ArrayList <Lider> list = model.consultaLider("Todo", null);

            if(list.size()>0){
                addRow(list);                        
            }else{

                /*validamos si hubo alguna excepción u error*/
                if(!ControladorValidaciones.EXCEPCIONES.equals("")){

                    JOptionPane.showMessageDialog(null, "Error en la consulta, Posibles errores : \n"+
                                                        ControladorValidaciones.EXCEPCIONES, 
                                                        "ERROR", JOptionPane.ERROR_MESSAGE);           
                    ControladorValidaciones.EXCEPCIONES="";                                

                }else{
                    JOptionPane.showMessageDialog(null, "No se obtuvo resultado de la consulta", "WARNING", JOptionPane.WARNING_MESSAGE);
                    addRow(list);
                }                        

            } 
            
        }
        
    }    
    
    public void validatorNumber() {
        ControladorValidaciones.validator(numeroDocumentoLider);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addColumn();        
        initComponents(null);
        validatorNumber();
        
        rangeSliderEdadLider.lowValueChangingProperty().addListener(new ChangeListener(){

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                int valueMin = ControladorGeneral.toInt(rangeSliderEdadLider.getLowValue());
                int valueMax = ControladorGeneral.toInt(rangeSliderEdadLider.getHighValue());
                
                String min = String.valueOf(valueMin);
                String max = String.valueOf(valueMax);
                
                rangoEdadValorLider.setText("Entre "+min+" y "+max+ " Años");            
            }
        });

        rangeSliderEdadLider.highValueChangingProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                int valueMin = ControladorGeneral.toInt(rangeSliderEdadLider.getLowValue());
                int valueMax = ControladorGeneral.toInt(rangeSliderEdadLider.getHighValue());
                
                String min = String.valueOf(valueMin);
                String max = String.valueOf(valueMax);
                
                rangoEdadValorLider.setText("Entre "+min+" y "+max+ " Años");            
            }

        });     
        
    }

    @Override
    public void addColumn() {

        columnNum = new JFXTreeTableColumn<>("Nº");
        columnNum.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableLideres, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableLideres, String> param) {
                StringProperty num = param.getValue().getValue().num;
                return num;
            }
        });


        columnNumDoc = new JFXTreeTableColumn<>("Nº Documento");
        columnNumDoc.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableLideres, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableLideres, String> param) {
                StringProperty numDoc = param.getValue().getValue().numDoc;
                return numDoc;
            }
        });

        
        columnNames = new JFXTreeTableColumn<>("Nombre");
        columnNames.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableLideres, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableLideres, String> param) {
                StringProperty name = param.getValue().getValue().nombre;
                return name;
            }
        });

        columnSexo = new JFXTreeTableColumn<>("Sexo");
        columnSexo.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableLideres, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableLideres, String> param) {
                StringProperty sexo = param.getValue().getValue().sexo;
                return sexo;
            }
        });
                

        columnTelefono = new JFXTreeTableColumn<>("Telefono");
        columnTelefono.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableLideres, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableLideres, String> param) {
                StringProperty telefono = param.getValue().getValue().telefono;
                return telefono;
            }
        });        

        columnCorreo = new JFXTreeTableColumn<>("E-mail");
        columnCorreo.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableLideres, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableLideres, String> param) {
                StringProperty correo = param.getValue().getValue().correo;
                return correo;
            }
        });        
        
        
    }

    @Override
    public void addRow(ArrayList  list) {

        ArrayList<Lider> listadoLideres = list;
        datos = FXCollections.observableArrayList();
        
        TreeItem <ControlTableLideres> root = new RecursiveTreeItem<ControlTableLideres>(datos, RecursiveTreeObject::getChildren);
        tableViewLider.setRoot(root);
        tableViewLider.setShowRoot(false);
        
        if(list.size()>0){
            for(int i=0; i<list.size(); i++){
                ControlTableLideres table = new ControlTableLideres(listadoLideres.get(i));
                datos.add(table);
            }        
        }else{
            
        }
        

    }

    @Override
    public void initComponents(Object obj) {

        //agregamos combobox
        fxCombotipoBusquedaLider.getItems().add("Todos");
        fxCombotipoBusquedaLider.getItems().add("Número de Documento");
        fxCombotipoBusquedaLider.getItems().add("Nombre/Apellido");
        fxCombotipoBusquedaLider.getItems().add("Edad");
        fxCombotipoBusquedaLider.getItems().add("Sexo");
        fxCombotipoBusquedaLider.getItems().add("Barrio");        
        fxCombotipoBusquedaLider.setValue("Todos");
        
        tipoDocumentoLider.getItems().add("Cedula de Ciudadanía");
        tipoDocumentoLider.getItems().add("Cedula de Extranjería");
        tipoDocumentoLider.setValue("Cedula de Ciudadanía");
        
        
        controlVisibilidad(hBoxSexoLider, false);
        controlVisibilidad(hBoxBusquedaLider, false);
        controlVisibilidad(hBoxEdadLider, false);
        controlVisibilidad(hBoxNumeroDocumentoLider, false);
        controlVisibilidad(hBoxBtnLider, true);
    
        rangeSliderEdadLider.adjustHighValue(80.0);
        rangeSliderEdadLider.adjustLowValue(20.0);
    
        tableViewLider.getColumns().setAll(columnNum, columnNumDoc, columnNames, columnSexo, columnTelefono, columnCorreo);

        tableViewLider.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);        
        columnNum.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        columnNumDoc.setMaxWidth(1f * Integer.MAX_VALUE * 18);
        columnNames.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        columnSexo.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        columnTelefono.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        columnCorreo.setMaxWidth(1f * Integer.MAX_VALUE * 27);
        
        addRow(model.consultaLider("Todo", null));    
        
        /*agregamos validadores para los campos de filtro de busqueda*/
        numeroDocumentoLider.getValidators().addAll(validatorFieldText, validatorNumber);                
        busquedaLider.getValidators().add(validatorFieldText);
        
        /*mensaje de validaciones*/
        validatorFieldText.setMessage("Este campo es obligatorio");
        validatorNumber.setMessage("Solo se permite el ingreso de números");

        radioFemaleLider.setToggleGroup(groupRadio);
        radioFemaleLider.setUserData("Femenino");
        radioMaleLider.setToggleGroup(groupRadio);
        radioMaleLider.setUserData("Masculino");
        radioMaleLider.setSelected(true);
        
        int valueMin = ControladorGeneral.toInt(rangeSliderEdadLider.getLowValue());
        int valueMax = ControladorGeneral.toInt(rangeSliderEdadLider.getHighValue());

        String min = String.valueOf(valueMin);
        String max = String.valueOf(valueMax);

        rangoEdadValorLider.setText("Entre "+min+" y "+max+ " Años");            
        
        
        
    }

    @Override
    public void controlVisibilidad(Node node, boolean condicion) {
        node.setVisible(condicion);
        node.setManaged(condicion);
        node.managedProperty().bind(node.visibleProperty());
    }

    
    private void buscarDatos() {

        ArrayList<String> data = new ArrayList<>();            
        ArrayList<Lider> list;
        /*validamos el tipo de busqueda*/
        if(hBoxNumeroDocumentoLider.isVisible()){

            if(!numeroDocumentoLider.getText().isEmpty()){

                if(ControladorValidaciones.onlyNumber(numeroDocumentoLider.getText())){

                    /*obtenemos datos*/
                    data.add(tipoDocumentoLider.getValue());
                    data.add(numeroDocumentoLider.getText());

                    list = model.consultaLider("Número de Documento", data);

                    if(list.size()>0){
                        addRow(list);                        
                    }else{

                        /*validamos si hubo alguna excepción u error*/
                        if(!ControladorValidaciones.EXCEPCIONES.equals("")){

                            JOptionPane.showMessageDialog(null, "Error en la consulta, Posibles errores : \n"+
                                                                ControladorValidaciones.EXCEPCIONES, 
                                                                "ERROR", JOptionPane.ERROR_MESSAGE);           
                            ControladorValidaciones.EXCEPCIONES="";                                

                        }else{
                            JOptionPane.showMessageDialog(null, "No se obtuvo resultado de la consulta", "WARNING", JOptionPane.WARNING_MESSAGE);
                            addRow(list);
                        }                        
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "Solo de aceptan valores numericos", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            }else{
                JOptionPane.showMessageDialog(null, "Debe ingresar un valor valido", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            }

        }else if(hBoxBusquedaLider.isVisible()){

            if(!busquedaLider.getText().isEmpty()){

                /*obtenemos datos*/
                data.add(busquedaLider.getText());

                list = model.consultaLider(fxCombotipoBusquedaLider.getValue(), data);

                if(list.size()>0){
                    addRow(list);                        
                }else{

                    /*validamos si hubo alguna excepción u error*/
                    if(!ControladorValidaciones.EXCEPCIONES.equals("")){

                        JOptionPane.showMessageDialog(null, "Error en la consulta, Posibles errores : \n"+
                                                            ControladorValidaciones.EXCEPCIONES, 
                                                            "ERROR", JOptionPane.ERROR_MESSAGE);           
                        ControladorValidaciones.EXCEPCIONES="";                                

                    }else{
                        JOptionPane.showMessageDialog(null, "No se obtuvo resultado de la consulta", "WARNING", JOptionPane.WARNING_MESSAGE);
                        addRow(list);
                    }                        
                }

            }else{
                JOptionPane.showMessageDialog(null, "Debe ingresar un valor valido", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            }

        }else if(hBoxEdadLider.isVisible()){

            int valueMin = ControladorGeneral.toInt(rangeSliderEdadLider.getLowValue());
            int valueMax = ControladorGeneral.toInt(rangeSliderEdadLider.getHighValue());

            data.add(String.valueOf(valueMin));
            data.add(String.valueOf(valueMax));

            list = model.consultaLider("Edad", data);

            if(list.size()>0){
                addRow(list);                        
            }else{

                /*validamos si hubo alguna excepción u error*/
                if(!ControladorValidaciones.EXCEPCIONES.equals("")){

                    JOptionPane.showMessageDialog(null, "Error en la consulta, Posibles errores : \n"+
                                                        ControladorValidaciones.EXCEPCIONES, 
                                                        "ERROR", JOptionPane.ERROR_MESSAGE);           
                    ControladorValidaciones.EXCEPCIONES="";                                

                }else{
                    JOptionPane.showMessageDialog(null, "No se obtuvo resultado de la consulta", "WARNING", JOptionPane.WARNING_MESSAGE);
                    addRow(list);
                }                        
            }



        }else if(hBoxSexoLider.isVisible()){

            /*obtenemos datos*/
            if(radioFemaleLider.isSelected()){
                data.add("Femenino");                
            }else{
                data.add("Masculino");            
            }

            list = model.consultaLider("Sexo", data);

            if(list.size()>0){
                addRow(list);                        
            }else{

                /*validamos si hubo alguna excepción u error*/
                if(!ControladorValidaciones.EXCEPCIONES.equals("")){

                    JOptionPane.showMessageDialog(null, "Error en la consulta, Posibles errores : \n"+
                                                        ControladorValidaciones.EXCEPCIONES, 
                                                        "ERROR", JOptionPane.ERROR_MESSAGE);           
                    ControladorValidaciones.EXCEPCIONES="";                                

                }else{
                    JOptionPane.showMessageDialog(null, "No se obtuvo resultado de la consulta", "WARNING", JOptionPane.WARNING_MESSAGE);
                    addRow(list);
                }                        
            }

        }else{

            list = model.consultaLider("Todo", null);

            if(list.size()>0){
                addRow(list);                        
            }else{

                /*validamos si hubo alguna excepción u error*/
                if(!ControladorValidaciones.EXCEPCIONES.equals("")){

                    JOptionPane.showMessageDialog(null, "Error en la consulta, Posibles errores : \n"+
                                                        ControladorValidaciones.EXCEPCIONES, 
                                                        "ERROR", JOptionPane.ERROR_MESSAGE);           
                    ControladorValidaciones.EXCEPCIONES="";                                

                }else{
                    JOptionPane.showMessageDialog(null, "No se obtuvo resultado de la consulta", "WARNING", JOptionPane.WARNING_MESSAGE);
                    addRow(list);
                }                        

            }

        }            
        
        
    }
    
    
}



class ControlTableLideres extends RecursiveTreeObject<ControlTableLideres> {

    StringProperty num;
    StringProperty numDoc;
    StringProperty nombre;
    StringProperty sexo;
    StringProperty telefono;
    StringProperty correo;
    Lider lider;

    public ControlTableLideres(Lider v) {
        
        this.num= new SimpleStringProperty(String.valueOf(v.getIndice()));
        this.numDoc = new SimpleStringProperty(ControladorGeneral.abreviarTipoDocumento(v.getTipoDocumento())+" "+String.valueOf(v.getNumeroDocumento()));
        this.nombre = new SimpleStringProperty(v.getNombre()+" "+v.getApellido());
        this.sexo = new SimpleStringProperty(ControladorGeneral.abreviarSexo(v.getSexo()));
        this.telefono = new SimpleStringProperty(v.getTelefono());
        this.correo = new SimpleStringProperty(v.getCorreoElectronico());
        this.lider = v;
        
    }
        
    public Lider getLider(){
        return this.lider;
    }


}
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
import java.awt.HeadlessException;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView.TreeTableViewSelectionModel;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.Votantes;
import modelDAO.VotantesDAO;
import org.controlsfx.control.RangeSlider;
import utlidades.ComponentesTabla;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;
import view.ElectoralDataControl;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelConsultaController implements Initializable, ComponentesTabla, GeneralView{

    @FXML
    private JFXComboBox<String> fxCombotipoBusqueda, tipoDocumento, 
                                fxComboBoxLugar, fxComboBoxMesa;
    
    @FXML
    private JFXTextField search, numeroDocumento, busqueda;
        
    @FXML
    private JFXRadioButton radioMale, radioFemale;
    
    @FXML 
    private Label rangoEdadValor;
    
    @FXML
    private JFXButton btnBuscar, btnNuevo, btnModificar, btnEliminar, 
                      btnReporte, btnActualizarTabla;

    @FXML
    private HBox hTipoBusqueda, hBoxSexo, hBoxEdad, hBoxBusqueda, 
                 hBoxNumeroDocumento, hBoxBtn;
    
    @FXML
    private GridPane hlugar;

    @FXML
    private JFXTreeTableView<ControlTable> tableView;
        
    @FXML
    private RangeSlider rangeSliderEdad;
    
    @FXML
    private GridPane panelConsulta;
     
    private JFXTreeTableColumn<ControlTable, String> columnNames, columnNum, columnNumDoc, columnSexo,
                                                columnLugar, columnMesa;

    private ObservableList<ControlTable> datos;
    private TreeItem<ControlTable> itemRow;
    
    
    private final VotantesDAO model = new VotantesDAO();
    
    
    /*Controlador de componentes visuales*/
    private final ToggleGroup groupRadio = new ToggleGroup();
    private final RequiredFieldValidator validatorFieldText = new RequiredFieldValidator();
    private final NumberValidator validatorNumber = new NumberValidator();
    
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        addColumn();
        initComponents(false);        
        validatorText();
        validatorNumber();
        
        rangeSliderEdad.lowValueChangingProperty().addListener(new ChangeListener(){

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                int valueMin = ControladorGeneral.toInt(rangeSliderEdad.getLowValue());
                int valueMax = ControladorGeneral.toInt(rangeSliderEdad.getHighValue());
                
                String min = String.valueOf(valueMin);
                String max = String.valueOf(valueMax);
                
                rangoEdadValor.setText("Entre "+min+" y "+max+ " Años");            
            }
        });

        rangeSliderEdad.highValueChangingProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                int valueMin = ControladorGeneral.toInt(rangeSliderEdad.getLowValue());
                int valueMax = ControladorGeneral.toInt(rangeSliderEdad.getHighValue());
                
                String min = String.valueOf(valueMin);
                String max = String.valueOf(valueMax);
                
                rangoEdadValor.setText("Entre "+min+" y "+max+ " Años");            
            }

        });     
        
        


        
        
    }
    
    public void validatorText() {
        ControladorValidaciones.validator(busqueda);
    }

    public void validatorNumber() {
        ControladorValidaciones.validator(numeroDocumento);
    }
  
    
    
    @FXML
    public void eventosOnAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(fxCombotipoBusqueda)){
            
            String value =fxCombotipoBusqueda.getValue();
            
            switch(value){
                
                case "Número de Documento":
                    hBoxNumeroDocumento.setVisible(true);
                    hBoxBtn.setVisible(true);
                    hlugar.setVisible(false);                    
                    hBoxBusqueda.setVisible(false);
                    hBoxEdad.setVisible(false);
                    hBoxSexo.setVisible(false);
                break;

                case "Nombre/Apellido":
                case "Barrio":
                    hBoxBusqueda.setVisible(true);
                    hBoxBtn.setVisible(true);
                    hlugar.setVisible(false);
                    hBoxNumeroDocumento.setVisible(false);
                    hBoxEdad.setVisible(false);
                    hBoxSexo.setVisible(false);
                break;                
 
                case "Edad":
                    hBoxEdad.setVisible(true);                
                    hBoxBtn.setVisible(true);
                    hlugar.setVisible(false);                    
                    hBoxBusqueda.setVisible(false);                    
                    hBoxNumeroDocumento.setVisible(false);
                    hBoxSexo.setVisible(false);
                break;
                
                case "Sexo":
                    hBoxSexo.setVisible(true);                
                    hBoxBtn.setVisible(true);
                    hlugar.setVisible(false);                    
                    hBoxEdad.setVisible(false);
                    hlugar.setVisible(false);                    
                    hBoxBusqueda.setVisible(false);                    
                    hBoxNumeroDocumento.setVisible(false);
                break;
                
                case "Lugar De Votación":
                    hBoxBtn.setVisible(true);
                    hlugar.setVisible(true);
                    hBoxSexo.setVisible(false);                
                    hBoxEdad.setVisible(false);                                    
                    hBoxBusqueda.setVisible(false);                    
                    hBoxNumeroDocumento.setVisible(false);
                break;                
                default:
                    hBoxSexo.setVisible(false);                
                    hBoxBtn.setVisible(true);
                    hlugar.setVisible(false);                    
                    hBoxEdad.setVisible(false);                                    
                    hBoxBusqueda.setVisible(false);                    
                    hBoxNumeroDocumento.setVisible(false);
                break;                                
            }
        
        }else if(evt.equals(btnBuscar)){

            buscarDatos();
            
        }else if(evt.equals(btnNuevo)){            
            PrincipalController principalController = ElectoralDataControl.loader.getController();
            principalController.selectView("Registro", null);            
        }else if(evt.equals(btnModificar)){
            
            TreeTableViewSelectionModel<ControlTable> modelTable = tableView.getSelectionModel();
            if(modelTable.getSelectedIndex() > 0){
                
                TreeItem<ControlTable> d = modelTable.getSelectedItem();
                ControlTable c = d.getValue();
                Votantes v = c.getVotante();
                                
                ControladorGeneral.CONTROLVIEWMODIFICAR=1;
                PrincipalController principalController = ElectoralDataControl.loader.getController();
                principalController.selectView("Registro", v);                                   
            
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una celda valida", "Operación fallida", JOptionPane.ERROR_MESSAGE);
            }
            
        }else if(evt.equals(btnEliminar)){
        
            TreeTableViewSelectionModel<ControlTable> modelTable = tableView.getSelectionModel();
            if(modelTable.getSelectedIndex() > 0){
                
                TreeItem<ControlTable> d = modelTable.getSelectedItem();
                ControlTable c = d.getValue();
                Votantes v = c.getVotante();
                int id = v.getId();
                
                int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro?\nsi realiza esta operación el registro será eliminado definitivamente de la base de datos");
                
                if(confirmar==JOptionPane.YES_OPTION){
                 
                    if(model.eliminarVotantes(id)==true){
                        
                        JOptionPane.showMessageDialog(null, "La información ha sido eliminada exitosamente de la base de datos", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);

                        ArrayList<Votantes> list;
                        
                        list = model.consultarVotantes();

                        if(list.size()>0){
                            addRow(list);                        
                        }

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
            
        }else if(evt.equals(btnReporte)){
            
            PrincipalController config = ElectoralDataControl.loader.getController();
            config.selectView("Reporte", null);
        
        }else if(evt.equals(btnActualizarTabla)){
            
            ArrayList <Votantes> list = model.consultarVotantes();

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

    /*eventos de teclado*/
    @FXML
    public void eventsKeyTyped(KeyEvent event){
        
        Object evt = event.getSource();

        char c = event.getCharacter().charAt(0);
        
        if(evt.equals(numeroDocumento)){
        
            if(!Character.isDigit(c)){
                event.consume();
            }
            
        }
        
    }
    
    
    
    
    @Override
    public void initComponents(Object obj) {
                
        //agregamos combobox
        fxCombotipoBusqueda.getItems().add("Todos");
        fxCombotipoBusqueda.getItems().add("Número de Documento");
        fxCombotipoBusqueda.getItems().add("Nombre/Apellido");
        fxCombotipoBusqueda.getItems().add("Edad");
        fxCombotipoBusqueda.getItems().add("Sexo");
        fxCombotipoBusqueda.getItems().add("Barrio");        
        fxCombotipoBusqueda.getItems().add("Lugar De Votación");        
        fxCombotipoBusqueda.setValue("Todos");
        
        tipoDocumento.getItems().add("Cedula de Ciudadanía");
        tipoDocumento.getItems().add("Cedula de Extranjería");
        tipoDocumento.setValue("Cedula de Ciudadanía");
        
        
        controlVisibilidad(hBoxSexo, false);
        controlVisibilidad(hBoxBusqueda, false);
        controlVisibilidad(hBoxEdad, false);
        controlVisibilidad(panelConsulta, true);
        controlVisibilidad(hlugar, false);
        
        hBoxNumeroDocumento.setVisible(false);
        hBoxNumeroDocumento.setManaged(false);
        hBoxNumeroDocumento.managedProperty().bind(hBoxNumeroDocumento.visibleProperty());
        
        rangeSliderEdad.adjustHighValue(80.0);
        rangeSliderEdad.adjustLowValue(20.0);

        tableView.getColumns().setAll(columnNum, columnNumDoc, columnNames, columnSexo, columnLugar, columnMesa);

        tableView.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);        
        columnNum.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        columnNumDoc.setMaxWidth(1f * Integer.MAX_VALUE * 22);
        columnNames.setMaxWidth(1f * Integer.MAX_VALUE * 22);
        columnSexo.setMaxWidth(1f * Integer.MAX_VALUE * 8);
        columnLugar.setMaxWidth(1f * Integer.MAX_VALUE * 31);
        columnMesa.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        
        addRow(model.consultarVotantes());
        
        /*agregamos validadores para los campos de filtro de busqueda*/
        numeroDocumento.getValidators().addAll(validatorFieldText, validatorNumber);                
        busqueda.getValidators().add(validatorFieldText);
        
        /*mensaje de validaciones*/
        validatorFieldText.setMessage("Este campo es obligatorio");
        validatorNumber.setMessage("Solo se permite el ingreso de números");

        radioFemale.setToggleGroup(groupRadio);
        radioFemale.setUserData("Femenino");
        radioMale.setToggleGroup(groupRadio);
        radioMale.setUserData("Masculino");
        radioMale.setSelected(true);
        
        int valueMin = ControladorGeneral.toInt(rangeSliderEdad.getLowValue());
        int valueMax = ControladorGeneral.toInt(rangeSliderEdad.getHighValue());

        String min = String.valueOf(valueMin);
        String max = String.valueOf(valueMax);

        rangoEdadValor.setText("Entre "+min+" y "+max+ " Años");            
        
        ControladorGeneral.addTooltipText(btnActualizarTabla, "Actualizar listado", "toolTipTextAjustes");
        
        ArrayList<Votantes>list1 = model.consultarVotantes("listLugar", null);
        
        if(list1.size()>0){            
            
            ControladorGeneral.llenarListaDesplegable(list1, fxComboBoxLugar);            
            
            fxComboBoxMesa.getItems().add("Todo");
            for(int i=1; i<=100; i++){
                fxComboBoxMesa.getItems().add(String.valueOf(i));            
            }
            fxComboBoxMesa.getItems().add("Otro");            
            
        }else{
            fxComboBoxLugar.getItems().add("Sin resultados");
            fxComboBoxLugar.setValue("Sin resultados");
            fxComboBoxMesa.getItems().add("Sin resultados");
            fxComboBoxMesa.setValue("Sin resultados");
        }
        
        
        
        
    }

    @Override
    public void controlVisibilidad(Node node, boolean state){
        node.setVisible(state);
        node.setManaged(state);
        node.managedProperty().bind(node.visibleProperty());
    }

    
    @Override
    public void addColumn() {

        columnNum = new JFXTreeTableColumn<>("Nº");
        //deptName.setPrefWidth(150);
        columnNum.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTable, String> param) {
                StringProperty num = param.getValue().getValue().num;
                return num;
            }
        });


        columnNumDoc = new JFXTreeTableColumn<>("Nº Documento");
        //deptName.setPrefWidth(150);
        columnNumDoc.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTable, String> param) {
                StringProperty numDoc = param.getValue().getValue().numDoc;
                return numDoc;
            }
        });

        
        columnNames = new JFXTreeTableColumn<>("Nombre");
        //deptName.setPrefWidth(150);
        columnNames.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTable, String> param) {
                StringProperty name = param.getValue().getValue().nombre;
                return name;
            }
        });

        columnSexo = new JFXTreeTableColumn<>("Sexo");
        //deptName.setPrefWidth(150);
        columnSexo.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTable, String> param) {
                StringProperty sexo = param.getValue().getValue().sexo;
                return sexo;
            }
        });
                

        columnLugar = new JFXTreeTableColumn<>("Lugar V.");
        columnLugar.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTable, String> param) {
                StringProperty lugar = param.getValue().getValue().lugarV;
                return lugar;
            }
        });        

        columnMesa = new JFXTreeTableColumn<>("Mesa V.");
        columnMesa.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTable, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTable, String> param) {
                StringProperty mesa = param.getValue().getValue().mesa;
                return mesa;
            }
        });        
        
    }

    
    @Override
    public void addRow(ArrayList<Votantes> list) {
        
        datos = FXCollections.observableArrayList();
        
        TreeItem <ControlTable> root = new RecursiveTreeItem<ControlTable>(datos, RecursiveTreeObject::getChildren);
        tableView.setRoot(root);
        tableView.setShowRoot(false);
        
        if(list.size()>0){
            for(int i=0; i<list.size(); i++){
                ControlTable table = new ControlTable(list.get(i));
                datos.add(table);
            }        
        }else{
            
        }
        
        
    }

    private void buscarDatos() {

        ArrayList<String> data = new ArrayList<>();            
        ArrayList<Votantes> list;
        /*validamos el tipo de busqueda*/
        if(hBoxNumeroDocumento.isVisible()){

            if(!numeroDocumento.getText().isEmpty()){

                if(ControladorValidaciones.onlyNumber(numeroDocumento.getText())){

                    /*obtenemos datos*/
                    data.add(tipoDocumento.getValue());
                    data.add(numeroDocumento.getText());

                    list = model.consultarVotantes("Número de Documento", data);

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

        }else if(hBoxBusqueda.isVisible()){

            if(!busqueda.getText().isEmpty()){

                /*obtenemos datos*/
                data.add(busqueda.getText());

                list = model.consultarVotantes(fxCombotipoBusqueda.getValue(), data);

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

        }else if(hBoxEdad.isVisible()){

            int valueMin = ControladorGeneral.toInt(rangeSliderEdad.getLowValue());
            int valueMax = ControladorGeneral.toInt(rangeSliderEdad.getHighValue());

            data.add(String.valueOf(valueMin));
            data.add(String.valueOf(valueMax));

            list = model.consultarVotantes("Edad", data);

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



        }else if(hBoxSexo.isVisible()){

            /*obtenemos datos*/
            if(radioFemale.isSelected()){
                data.add("Femenino");                
            }else{
                data.add("Masculino");            
            }

            list = model.consultarVotantes("Sexo", data);

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

        }else if(hlugar.isVisible()){
        
            if(fxComboBoxLugar.getValue()!=null && fxComboBoxMesa.getValue() != null){
                
                String lugar = fxComboBoxLugar.getValue();
                String mesa="";
                
                if(fxComboBoxMesa.getValue().equals("Otro")){
                    boolean condicion=true;
                    while(condicion){
                        try{
                            mesa = JOptionPane.showInputDialog("Ingrese el número de mesa a buscar");
                            if(mesa!=null){
                                condicion=false;                            
                            }else{
                                JOptionPane.showMessageDialog(null, "Debe ingresar un valor valido por favor intente nuevamente");
                            }
                        }catch(HeadlessException ex){
                            JOptionPane.showMessageDialog(null, "Debe ingresar un valor valido por favor intente nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }else{
                    mesa=fxComboBoxMesa.getValue();
                }

                if(mesa==null){
                    mesa="Todo";
                }

                data.add(lugar);
                data.add(mesa);
                list = model.consultarVotantes("Lugar De Votación", data);
                
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
                JOptionPane.showMessageDialog(null, "Debe seleccionar un valor valido para realizar la busqueda", "Error", JOptionPane.ERROR_MESSAGE);                        
            }
        
        }else{

            list = model.consultarVotantes();

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


class ControlTable extends RecursiveTreeObject<ControlTable> {

    StringProperty num;
    StringProperty numDoc;
    StringProperty nombre;
    StringProperty sexo;
    StringProperty lugarV;
    StringProperty mesa;
    Votantes votante;

    public ControlTable(Votantes v) {
        
        this.num= new SimpleStringProperty(String.valueOf(v.getIndice()));
        this.numDoc = new SimpleStringProperty(ControladorGeneral.abreviarTipoDocumento(v.getTipoDocumento())+" "+String.valueOf(v.getNumeroDocumento()));
        this.nombre = new SimpleStringProperty(v.getNombre()+" "+v.getApellido());
        this.sexo = new SimpleStringProperty(ControladorGeneral.abreviarSexo(v.getSexo()));
        this.lugarV = new SimpleStringProperty(v.getLugar());
        this.mesa = new SimpleStringProperty(v.getMesa());
        this.votante = v;
        
    }
    
    
    public Votantes getVotante(){
        return this.votante;
    }


}    
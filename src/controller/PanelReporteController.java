package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.Lider;
import model.Votantes;
import modelDAO.LiderDAO;
import modelDAO.VotantesDAO;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.RangeSlider;
import utlidades.ComponentesTabla;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;
import utlidades.Item;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelReporteController implements Initializable, ComponentesTabla, GeneralView {

    // Creamos objetos y relacionamos componentes de la interfaz grafica
    
    @FXML
    private HBox hBoxSexo, hBoxEdad, 
                 hBoxBusqueda;
    
    
    @FXML
    private VBox vBoxLider;
    
    @FXML
    private GridPane hlugar;
    
    @FXML
    private JFXComboBox<String> fxCombotipoBusqueda, fxComboBoxMesa, fxComboBoxLugar;
    
    @FXML
    private JFXComboBox<Item> fxComboBoxLider;
    private Item item;

    @FXML
    private JFXTextField fxTextFieldBusqueda;

    @FXML
    private RangeSlider rangeSliderEdad;
    
    @FXML 
    private Label rangoEdadValor, labelTotalRegistro;
    
    
    @FXML
    private JFXRadioButton radioMale, radioFemale;
    
    @FXML
    private JFXButton btnBuscar, btnReporte, btnActualizarLugarDeVotacion, btnActualizarLiderAsignado;;

    @FXML
    private VBox contenedorBarra;
    
    @FXML
    private JFXProgressBar barraDeProgreso;
    
    @FXML
    private JFXTreeTableView<ControlTableReport> tableView;

    private JFXTreeTableColumn<ControlTableReport, String> columnNames, columnNumDoc,
                                                columnLugar, columnMesa;

    private ObservableList<ControlTableReport> datos;

    private final VotantesDAO model = new VotantesDAO();
    private final LiderDAO modelLider = new LiderDAO();
        
    private final ToggleGroup groupRadio = new ToggleGroup();

    private ArrayList<Votantes> list=null;
    private ArrayList<String> dataFilter=null;
    private String filter;
    
    
    private Workbook book = null;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        addColumn();
        initComponents(null);

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

    public void listarLideres(int update){
        
        
        ArrayList<Lider> listLider =  modelLider.consultaLider("id/value", null);
        
        if(update!=1){
        
            if(listLider.size()>0){
                item = new Item(-1, "Ninguno");
                fxComboBoxLider.getItems().add(item);
                for(int i=0; i<listLider.size(); i++){
                    item = new Item(listLider.get(i).getId(), listLider.get(i).getNombre() + " "+listLider.get(i).getApellido());
                    fxComboBoxLider.getItems().add(item);
                }
            }        
        
        }else{

            if(listLider.size()>0){

                int length = fxComboBoxLider.getItems().size();
                fxComboBoxLider.getItems().remove(0, length);
                
                item = new Item(-1, "Ninguno");
                fxComboBoxLider.getItems().add(item);
                for(int i=0; i<listLider.size(); i++){
                    item = new Item(listLider.get(i).getId(), listLider.get(i).getNombre() + " "+listLider.get(i).getApellido());
                    fxComboBoxLider.getItems().add(item);
                }
            
            }        
        
        }
        
    }
    
    
    
    public String generarNombreReporte(){
        
        String nombreArchivo="Reporte_";
        boolean condicion=true;
        
        
        while(condicion){
            
            int key=ThreadLocalRandom.current().nextInt(1000000000, 1999999999);
            String homeUsuario = System.getProperty("user.home"); 
            File directorio=new File(homeUsuario+"/Reportes-Electoral-Data-Control/"+nombreArchivo+""+key+".xlsx");

            if (!directorio.exists()) {
                nombreArchivo=nombreArchivo+""+key+".xlsx";
                condicion=false;
            }

            
        }
        
        
        return nombreArchivo;
        
    }
    
    public final File crearDirectorioPrincipal(String nombreCarpeta){
        String homeUsuario = System.getProperty("user.home"); 
        File directorio=new File(homeUsuario+"/"+nombreCarpeta);
        directorio.mkdir();
 
        return directorio;
    }
    
    
    public void saveFile(ArrayList<String> extensiones, Workbook book){

        //COPIA DE SEGURIDAD
        File directorio = crearDirectorioPrincipal("Reportes-Electoral-Data-Control");
                
        try{
            
            JOptionPane.showMessageDialog(null, "¡Falta poco para terminar!\n"
                                              + "Para guardar el reporte debe seleccionar el directorio donde será alojado\n"
                                              + "Igualmente se almacenará un copia de segridad en la siguiente dirección:\n"
                                              + "<html><body><span style='color:red;'>"+directorio+"</span></body></html>", 
                                                "INFORMACIÓN", 
                                                JOptionPane.INFORMATION_MESSAGE);
            
            FileChooser fileSave = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Solo archivos EXCEL", extensiones);                
            fileSave.getExtensionFilters().add(extFilter); //Show save file dialog             
            
            File file = fileSave.showSaveDialog(null); 

            if (file != null) { 

                String name = file.getPath();

                if(file.getName()!=null){

                    if(name.contains(".")){
                        file = new File(name.substring(0, name.lastIndexOf("."))+".xlsx");                    
                    }else{
                        file = new File(name+".xlsx");                                    
                    }                

                }else{
                    JOptionPane.showMessageDialog(null, "Debe ingresar un nombre valido para el archivo", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    book.write(fileOut);
                }

                JOptionPane.showMessageDialog(null, "Operación Exitosa, El reporte ha sido generado de manera correcta", "INFO", JOptionPane.INFORMATION_MESSAGE);

            }
                                    
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Operación fallida, hubo un error al generar el reporte.\n"
                                               +ex.getMessage());
        }finally{
            
            try{
                try (FileOutputStream fileOut = new FileOutputStream(directorio.getAbsolutePath()+"/"+generarNombreReporte())) {
                    book.write(fileOut);
                }            
            }catch(IOException ex){}
            
        }

        
    }
        
    @Override
    public void addColumn() {
    
        columnNumDoc = new JFXTreeTableColumn<>("Nº Documento");
        //deptName.setPrefWidth(150);
        columnNumDoc.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableReport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableReport, String> param) {
                StringProperty numDoc = param.getValue().getValue().numDoc;
                return numDoc;
            }
        });

        
        columnNames = new JFXTreeTableColumn<>("Nombre");
        //deptName.setPrefWidth(150);
        columnNames.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableReport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableReport, String> param) {
                StringProperty name = param.getValue().getValue().nombre;
                return name;
            }
        });

        columnLugar = new JFXTreeTableColumn<>("Lugar V.");
        columnLugar.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableReport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableReport, String> param) {
                StringProperty lugar = param.getValue().getValue().lugarV;
                return lugar;
            }
        });        

        columnMesa = new JFXTreeTableColumn<>("Mesa V.");
        columnMesa.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ControlTableReport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ControlTableReport, String> param) {
                StringProperty mesa = param.getValue().getValue().mesa;
                return mesa;
            }
        });        
    
    
    }

    @Override
    public void addRow(ArrayList list) {
        
        ArrayList<Votantes> listadoReporte  = list;
        datos = FXCollections.observableArrayList();
        
        TreeItem <ControlTableReport> root = new RecursiveTreeItem<ControlTableReport>(datos, RecursiveTreeObject::getChildren);
        tableView.setRoot(root);
        tableView.setShowRoot(false);
        
        if(list.size()>0){
            for(int i=0; i<list.size(); i++){
                ControlTableReport table = new ControlTableReport(listadoReporte.get(i));
                datos.add(table);
            }        
            labelTotalRegistro.setText(list.size() + " registros");
        }else{
            labelTotalRegistro.setText("");
        }        
        
    }

    @Override
    public void initComponents(Object obj) {

        //agregamos combobox
        fxCombotipoBusqueda.getItems().add("Todo");
        fxCombotipoBusqueda.getItems().add("Nombre/Apellido");
        fxCombotipoBusqueda.getItems().add("Edad");
        fxCombotipoBusqueda.getItems().add("Sexo");        
        fxCombotipoBusqueda.getItems().add("Lugar De Votación");
        fxCombotipoBusqueda.getItems().add("Lider Asignado");
        fxCombotipoBusqueda.setValue("Todo");
        
        controlVisibilidad(hBoxSexo, false);
        controlVisibilidad(hBoxEdad, false);
        controlVisibilidad(hlugar, false);
        controlVisibilidad(vBoxLider, false);
        controlVisibilidad(hBoxBusqueda, false);
                
        rangeSliderEdad.adjustHighValue(80.0);
        rangeSliderEdad.adjustLowValue(20.0);
        
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
        
        tableView.getColumns().setAll(columnNumDoc, columnNames, columnLugar, columnMesa);

        tableView.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);        
        columnNumDoc.setMaxWidth(1f * Integer.MAX_VALUE * 21);
        columnNames.setMaxWidth(1f * Integer.MAX_VALUE * 26);
        columnLugar.setMaxWidth(1f * Integer.MAX_VALUE * 40);
        columnMesa.setMaxWidth(1f * Integer.MAX_VALUE * 13);
        
        list = model.consultarVotantes();
        addRow(list);
        
        ArrayList<Votantes>list1 = model.consultarVotantes("ListCombo", null);
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

        contenedorBarra.setVisible(false);
        contenedorBarra.setManaged(false);
        contenedorBarra.managedProperty().bind(contenedorBarra.visibleProperty());
        
        listarLideres(0);
        fxComboBoxLider.setVisibleRowCount(10);

        filter = "Todo";
        
    }

    @Override
    public void controlVisibilidad(Node node, boolean condicion) {
        node.setVisible(condicion);
        node.setManaged(condicion);
        node.managedProperty().bind(node.visibleProperty());        
    }
    
    @FXML
    public void eventosOnAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(fxCombotipoBusqueda)){
            
            String value =fxCombotipoBusqueda.getValue();
            
            switch(value){
                
                case "Nombre/Apellido":
                    hBoxBusqueda.setVisible(true);
                    hBoxEdad.setVisible(false);
                    hBoxSexo.setVisible(false);
                    hlugar.setVisible(false);
                    vBoxLider.setVisible(false);
                break;                
 
                case "Edad":
                    hBoxEdad.setVisible(true);                
                    hBoxBusqueda.setVisible(false);                    
                    hBoxSexo.setVisible(false);
                    hlugar.setVisible(false);
                    vBoxLider.setVisible(false);
                break;
                case "Sexo":
                    hBoxSexo.setVisible(true);                
                    hBoxEdad.setVisible(false);                                    
                    hBoxBusqueda.setVisible(false);                    
                    hlugar.setVisible(false);
                    vBoxLider.setVisible(false);
                break;
                        
                case "Lugar De Votación":
                    hlugar.setVisible(true);
                    hBoxSexo.setVisible(false);                
                    hBoxEdad.setVisible(false);                                    
                    hBoxBusqueda.setVisible(false);                    
                    vBoxLider.setVisible(false);
                break;    
                
                case "Lider Asignado":
                    vBoxLider.setVisible(true);
                    hlugar.setVisible(false);
                    hBoxSexo.setVisible(false);                
                    hBoxEdad.setVisible(false);                                    
                    hBoxBusqueda.setVisible(false);                    
                break;
                
                
                default:
                    hBoxBusqueda.setVisible(false);
                    hBoxEdad.setVisible(false);
                    hBoxSexo.setVisible(false);
                    hlugar.setVisible(false);
                    vBoxLider.setVisible(false);
                break;                                
            }
        
        }else if(evt.equals(btnBuscar)){

            ArrayList<String> data = new ArrayList<>();            
            /*validamos el tipo de busqueda*/            
            
            if(hBoxBusqueda.isVisible()){
            
                if(!fxTextFieldBusqueda.getText().isEmpty()){

                    /*obtenemos datos*/
                    data.add(fxTextFieldBusqueda.getText());

                    dataFilter = new ArrayList<>();
                    dataFilter.add(fxTextFieldBusqueda.getText());
                    filter = fxCombotipoBusqueda.getValue();
                    
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

                dataFilter = new ArrayList<>();
                dataFilter.add(String.valueOf(valueMin));
                dataFilter.add(String.valueOf(valueMax));
                filter = fxCombotipoBusqueda.getValue();
                
                
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

                dataFilter = new ArrayList<>();
                dataFilter.add(data.get(0));
                filter = fxCombotipoBusqueda.getValue();
                
                
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
        
                if(fxComboBoxLugar.getValue()!=null ){

                    String lugar = fxComboBoxLugar.getValue();
                    String mesa=fxComboBoxMesa.getValue();
                    
                    if(mesa!=null){

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
                    
                    }else{
                        mesa="Todo";
                    }
   
                    data.add(lugar);
                    data.add(mesa);

                    dataFilter = new ArrayList<>();
                    dataFilter.add(data.get(0));
                    dataFilter.add(data.get(1));                    
                    filter = fxCombotipoBusqueda.getValue();
                    
                    
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
        
            }else if(vBoxLider.isVisible()){
                
                filter = fxCombotipoBusqueda.getValue();
                
                Item itemLider = fxComboBoxLider.getValue();

                if(itemLider!=null){                
                    itemLider = fxComboBoxLider.getValue();            
                }else{
                    itemLider = new Item(-1, "Ninguno");
                }

                data.add(String.valueOf(itemLider.getId()));                                
                
                list = model.consultarVotantes("Lider", data);

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

                filter = fxCombotipoBusqueda.getValue();
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
            
        }else if(evt.equals(btnReporte)){
            
            if(list!=null){
            
                if(list.size()>0){

                    //creamos un hilo para generar el documento
                    Task task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            contenedorBarra.setVisible(true);
                            GenerateReport report = new GenerateReport(list, filter, dataFilter);
                            book = report.generarReporte();
                            contenedorBarra.setVisible(false);
                            return null;
                        }

                    };
                    
                    task.setOnSucceeded((Event event1) -> {
                        if(book!=null){
                            ArrayList<String> extensiones = new ArrayList<>();
                            extensiones.add("*.xlsx");                            
                            saveFile(extensiones, book);
                        }else{
                            JOptionPane.showMessageDialog(null, ControladorValidaciones.EXCEPCIONES);
                            ControladorValidaciones.EXCEPCIONES="";
                        }
                    });

                    Thread t = new Thread(task);
                    t.start();
                    
                }else{
                   JOptionPane.showMessageDialog(null, "No hay datos para generar el reporte", "Error De Operación", JOptionPane.ERROR_MESSAGE);
                }
            
            }else{
                JOptionPane.showMessageDialog(null, "No hay datos para generar el reporte", "Error De Operación", JOptionPane.ERROR_MESSAGE);
            }
        
        }else if(evt.equals(btnActualizarLugarDeVotacion)){
        
            ArrayList<Votantes>listVotantes = model.consultarVotantes("ListCombo", null);            
            
            int length = fxComboBoxLugar.getItems().size();
            fxComboBoxLugar.getItems().remove(0, length);
            
            if(listVotantes.size()>0){

                ControladorGeneral.llenarListaDesplegable(listVotantes, fxComboBoxLugar);

            }else{
                fxComboBoxLugar.getItems().add("Sin resultados");
            }        
        
        }else if(evt.equals(btnActualizarLiderAsignado)){
            listarLideres(1);            
        }
        
    }    
    
    
}

class ControlTableReport extends RecursiveTreeObject<ControlTableReport> {

    StringProperty tipoDoc;
    StringProperty numDoc;
    StringProperty nombre;
    StringProperty sexo;
    StringProperty lugarV;
    StringProperty mesa;
    Votantes votante;

    public ControlTableReport(Votantes v) {

        this.numDoc = new SimpleStringProperty(ControladorGeneral.abreviarTipoDocumento(v.getTipoDocumento())+" "+String.valueOf(v.getNumeroDocumento()));
        this.nombre = new SimpleStringProperty(v.getNombre()+" "+v.getApellido());
        this.lugarV = new SimpleStringProperty(v.getLugar());
        this.mesa = new SimpleStringProperty(v.getMesa());
        this.votante = v;
        
    }
    
    
    public Votantes getVotante(){
        return this.votante;
    }


}

class GenerateReport{
    
    private final ArrayList<Votantes> lista;
    private final ArrayList<String> dataFilter;
    private final String filtro;
    
    public final void agregarTitulo(Workbook libro, Sheet sheet, CellStyle style, String fuente, 
                             int longitudFuente, Row filaT, int columna, String text, int state){
        
        style = libro.createCellStyle();
        if(state==1){
            style.setAlignment(HorizontalAlignment.CENTER);
        }else{
            style.setAlignment(HorizontalAlignment.LEFT);        
        }
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font fuenteT = libro.createFont();
        fuenteT.setFontName(fuente);
        fuenteT.setBold(true);
        fuenteT.setFontHeightInPoints((short) longitudFuente);
        
        style.setFont(fuenteT);

        Cell celdaT = filaT.createCell(columna);
        celdaT.setCellStyle(style);
        celdaT.setCellValue(text);
        
    }
    
    
    public final void agregarDatos(Row fila, int pos, CellStyle style, CreationHelper h1, String datos){

        Cell celdaEnzabezado = fila.createCell(pos);
        celdaEnzabezado.setCellStyle(style);
        celdaEnzabezado.setCellValue(h1.createRichTextString(datos));
                
    }
    
    public GenerateReport(ArrayList<Votantes> list, String filtro, ArrayList<String> dataFilter){    
        this.lista=list;  
        this.filtro=filtro;
        this.dataFilter=dataFilter;
    }
    
    
    public Workbook generarReporte(){

        /*creamos carpeta principal*/
                
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Votantes");
        Footer footer = sheet.getFooter();  
        footer.setCenter("Pagina " + HeaderFooter.page() + " de " + HeaderFooter.numPages() ); 
        
        try {

            CellStyle style1=null;
            Row filaT = sheet.createRow(0);

            agregarTitulo(book, sheet, style1, "Arial", 31, filaT, 0, "Listado Sufragantes", 1);            
            sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 7));

            Row filaT1;            
            
            switch(filtro){
            
                case "Lider Asignado":

                    String nameLider;

                    if(lista.size()>0){

                        if(lista.get(0).getLider().getId()!=-1){
                            nameLider = lista.get(0).getLider().getNombre() + " "+ lista.get(0).getLider().getApellido();
                        }else{
                            nameLider="Ninguno";
                        }
                        filaT1 = sheet.createRow(5);            
                        filaT1.setHeight((short) (2*sheet.getDefaultRowHeight()));            
                        agregarTitulo(book, sheet, style1, "Arial", 18, filaT1, 0, "Lider Asignado: "+nameLider, 0);            
                        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));                            

                    }
                                        
                break;
            
                case "Nombre/Apellido":

                    filaT1 = sheet.createRow(4);            
                    filaT1.setHeight((short) (2*sheet.getDefaultRowHeight()));            
                    agregarTitulo(book, sheet, style1, "Arial", 18, filaT1, 0, "Buscar Por: "+filtro, 0);            
                    sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 7));                            
                    
                    if(dataFilter.size()>0){
                        Row filaT2 = sheet.createRow(5);            
                        agregarTitulo(book, sheet, style1, "Arial", 14, filaT2, 0, "Filtro: "+dataFilter.get(0), 0);            
                        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));                            
                    }
                    
                break;
                
                
                case "Edad":

                    filaT1 = sheet.createRow(4);            
                    filaT1.setHeight((short) (2*sheet.getDefaultRowHeight()));            
                    agregarTitulo(book, sheet, style1, "Arial", 18, filaT1, 0, "Buscar Por: "+filtro, 0);            
                    sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 7));                            
                    
                    if(dataFilter.size()>0){
                        Row filaT2 = sheet.createRow(5);            
                        agregarTitulo(book, sheet, style1, "Arial", 14, filaT2, 0, "Filtro: "+dataFilter.get(0)+ " a "+dataFilter.get(1) + " Años", 0);            
                        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));                            
                    }
                    
                break;
                
                case "Sexo":

                    filaT1 = sheet.createRow(4);            
                    filaT1.setHeight((short) (2*sheet.getDefaultRowHeight()));            
                    agregarTitulo(book, sheet, style1, "Arial", 18, filaT1, 0, "Buscar Por: "+filtro, 0);            
                    sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 7));                            
                    
                    if(dataFilter.size()>0){
                        Row filaT2 = sheet.createRow(5);            
                        agregarTitulo(book, sheet, style1, "Arial", 14, filaT2, 0, "Filtro: "+dataFilter.get(0), 0);            
                        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));                            
                    }
                    
                break;
                
                case "Lugar De Votación":

                    filaT1 = sheet.createRow(4);            
                    filaT1.setHeight((short) (2*sheet.getDefaultRowHeight()));            
                    agregarTitulo(book, sheet, style1, "Arial", 18, filaT1, 0, "Buscar Por: "+filtro, 0);            
                    sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 7));                            
                    
                    if(dataFilter.size()>0){
                        Row filaT2 = sheet.createRow(5);            
                        agregarTitulo(book, sheet, style1, "Arial", 14, filaT2, 0, "Filtro: "+dataFilter.get(0) +  " Mesa "+dataFilter.get(1), 0);            
                        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));                            
                    }
                    
                    
                break;
                
                default:
                    
                    filaT1 = sheet.createRow(5);            
                    filaT1.setHeight((short) (2*sheet.getDefaultRowHeight()));            
                    agregarTitulo(book, sheet, style1, "Arial", 18, filaT1, 0, "Buscar Por: "+filtro, 0);            
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));                            
                    
                break;
                
                
            }
                                                
            String[] cabecera = new String[]{"Nº", "Nº Documento", "Nombres", "Apellidos", "Telefono", "Lugar de Votación", "Dirección", "Mesa"};
                        
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setWrapText(true);
            headerStyle.setAlignment(HorizontalAlignment.LEFT);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            Font font = book.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 15);
            headerStyle.setFont(font);

            CreationHelper h1 = book.getCreationHelper();
            
            Row filaEncabezados = sheet.createRow(6);
            filaEncabezados.setHeight((short) (2*sheet.getDefaultRowHeight()));

            for (int i = 0; i < cabecera.length; i++) {
                agregarDatos(filaEncabezados, i, headerStyle, h1, cabecera[i]);
            }
            
            int numFilaDatos = 7;

            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setWrapText(true);
            datosEstilo.setAlignment(HorizontalAlignment.LEFT);
            datosEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
                        
            CreationHelper h = book.getCreationHelper();
            
            for(int i=0; i<lista.size(); i++){
                
                Row filaDatos = sheet.createRow(numFilaDatos);
                filaDatos.setHeight((short) (2*sheet.getDefaultRowHeight()));
                
                agregarDatos(filaDatos, 0, datosEstilo, h, String.valueOf(i+1));
                agregarDatos(filaDatos, 1, datosEstilo, h, String.valueOf(lista.get(i).getNumeroDocumento()));
                agregarDatos(filaDatos, 2, datosEstilo, h, lista.get(i).getNombre());
                agregarDatos(filaDatos, 3, datosEstilo, h, lista.get(i).getApellido());
                agregarDatos(filaDatos, 4, datosEstilo, h, lista.get(i).getTelefono());
                agregarDatos(filaDatos, 5, datosEstilo, h, lista.get(i).getLugar());
                agregarDatos(filaDatos, 6, datosEstilo, h, lista.get(i).getDireccionLugar());
                agregarDatos(filaDatos, 7, datosEstilo, h, String.valueOf(lista.get(i).getMesa()));
                
                numFilaDatos++;

            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            
            sheet.setRepeatingRows(CellRangeAddress.valueOf("1:7"));
                        
        }catch (Exception ex) {
            ControladorValidaciones.EXCEPCIONES= "* Error al generar reporte, posibles errores : \n*"+ex.getMessage();
            book=null;
        }

        return book;
        
    }

}
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.Votantes;
import modelDAO.VotantesDAO;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.RangeSlider;
import utlidades.ComponentesTabla;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelReporteController implements Initializable, ComponentesTabla, GeneralView {

    // Creamos objetos y relacionamos componentes de la interfaz grafica
    
    @FXML
    private HBox hBoxMesaVotacion, hBoxLugarVotacion, hBoxSexo, hBoxEdad, 
                 hBoxBusqueda;
    
    @FXML
    private JFXComboBox<String> fxCombotipoBusqueda, fxComboBoxMesa, fxComboBoxLugar;

    @FXML
    private JFXTextField fxTextFieldBusqueda;

    @FXML
    private RangeSlider rangeSliderEdad;
    
    @FXML 
    private Label rangoEdadValor, labelTotalRegistro;
    
    
    @FXML
    private JFXRadioButton radioMale, radioFemale;
    
    @FXML
    private JFXButton btnBuscar, btnReporte;

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
        
    private final ToggleGroup groupRadio = new ToggleGroup();

    private ArrayList<Votantes> list=null;
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

    
    private void llenarLista(ArrayList<Votantes> list, int value){
        
        for (int i=0; i<list.size(); i++){
            if(value==1){
                fxComboBoxLugar.getItems().add(list.get(i).getLugar());
                if(i==0){
                    fxComboBoxLugar.setValue(list.get(i).getLugar());
                }
            }else if(value==2){
                fxComboBoxMesa.getItems().add(list.get(i).getMesa());
                if(i==0){
                    fxComboBoxMesa.setValue(list.get(i).getMesa());                
                }            
            }
        }
        
        fxComboBoxLugar.setVisibleRowCount(10);
        fxComboBoxMesa.setVisibleRowCount(10);
        
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
    public void addRow(ArrayList<Votantes> list) {

        datos = FXCollections.observableArrayList();
        
        TreeItem <ControlTableReport> root = new RecursiveTreeItem<ControlTableReport>(datos, RecursiveTreeObject::getChildren);
        tableView.setRoot(root);
        tableView.setShowRoot(false);
        
        if(list.size()>0){
            for(int i=0; i<list.size(); i++){
                ControlTableReport table = new ControlTableReport(list.get(i));
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
        fxCombotipoBusqueda.getItems().add("Mesa De Votación");
        fxCombotipoBusqueda.getItems().add("Lugar De Votación");
        fxCombotipoBusqueda.getItems().add("Edad");
        fxCombotipoBusqueda.getItems().add("Sexo");        
        fxCombotipoBusqueda.setValue("Todo");
        
        controlVisibilidad(hBoxSexo, false);
        controlVisibilidad(hBoxEdad, false);
        controlVisibilidad(hBoxMesaVotacion, false);
        controlVisibilidad(hBoxLugarVotacion, false);
        
        hBoxBusqueda.setVisible(false);
        hBoxBusqueda.setManaged(false);
        hBoxBusqueda.managedProperty().bind(hBoxBusqueda.visibleProperty());
        
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
            if(fxComboBoxMesa.getItems().size()>0){
                fxComboBoxMesa.getItems().remove(0);
            }
            if(fxComboBoxLugar.getItems().size()>0){
                fxComboBoxLugar.getItems().remove(0);
            }            
            llenarLista(list1, 1);
            llenarLista(list1, 2);
        }else{
            fxComboBoxMesa.getItems().add("Sin resultados");
            fxComboBoxLugar.getItems().add("Sin resultados");
            fxComboBoxMesa.setValue("Sin resultados");
            fxComboBoxLugar.setValue("Sin resultados");
        }

        contenedorBarra.setVisible(false);
        contenedorBarra.setManaged(false);
        contenedorBarra.managedProperty().bind(contenedorBarra.visibleProperty());
        
        
        
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
                    hBoxLugarVotacion.setVisible(false);
                    hBoxMesaVotacion.setVisible(false);
                break;                
 
                case "Edad":
                    hBoxEdad.setVisible(true);                
                    hBoxBusqueda.setVisible(false);                    
                    hBoxSexo.setVisible(false);
                    hBoxLugarVotacion.setVisible(false);
                    hBoxMesaVotacion.setVisible(false);
                break;
                case "Sexo":
                    hBoxSexo.setVisible(true);                
                    hBoxEdad.setVisible(false);                                    
                    hBoxBusqueda.setVisible(false);                    
                    hBoxLugarVotacion.setVisible(false);
                    hBoxMesaVotacion.setVisible(false);                    
                break;
                case "Mesa De Votación":
                    hBoxMesaVotacion.setVisible(true);                                        
                    hBoxSexo.setVisible(false);                
                    hBoxEdad.setVisible(false);                                    
                    hBoxBusqueda.setVisible(false);
                    hBoxLugarVotacion.setVisible(false);
                break;
                case "Lugar De Votación":
                    hBoxLugarVotacion.setVisible(true);                    
                    hBoxSexo.setVisible(false);                
                    hBoxEdad.setVisible(false);                                    
                    hBoxBusqueda.setVisible(false); 
                    hBoxMesaVotacion.setVisible(false);                                        
                break;                
                default:
                    hBoxBusqueda.setVisible(false);
                    hBoxEdad.setVisible(false);
                    hBoxSexo.setVisible(false);
                    hBoxLugarVotacion.setVisible(false);
                    hBoxMesaVotacion.setVisible(false);
                break;                                
            }
        
        }else if(evt.equals(btnBuscar)){

            ArrayList<String> data = new ArrayList<>();            
            /*validamos el tipo de busqueda*/            
            
            if(hBoxBusqueda.isVisible()){
            
                if(!fxTextFieldBusqueda.getText().isEmpty()){

                    /*obtenemos datos*/
                    data.add(fxTextFieldBusqueda.getText());
                    
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
                
                
            }else if(hBoxLugarVotacion.isVisible()){
            
                String lugarDeVotacion = fxComboBoxLugar.getValue();
                
                if(!lugarDeVotacion.equals("Sin resultados")){
                    
                    data.add(lugarDeVotacion);
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
                    JOptionPane.showMessageDialog(null, "Seleccione un ítem valido", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }else if(hBoxMesaVotacion.isVisible()){

                String lugarDeVotacion = fxComboBoxMesa.getValue();
                
                if(!lugarDeVotacion.equals("Sin resultados")){
                    
                    data.add(lugarDeVotacion);
                    list = model.consultarVotantes("Mesa De Votación", data);
                    
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
                    JOptionPane.showMessageDialog(null, "Seleccione un ítem valido", "Error", JOptionPane.ERROR_MESSAGE);
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
            
        }else if(evt.equals(btnReporte)){
            
            if(list!=null){
            
                if(list.size()>0){

                    //creamos un hilo para generar el documento
                    Task task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            contenedorBarra.setVisible(true);
                            GenerateReport report = new GenerateReport(list, fxCombotipoBusqueda.getValue());
                            boolean condicion = report.generarReporte();
                            if(!condicion){
                                JOptionPane.showMessageDialog(null, "Hubo un error en el proceso, intente nuevamente", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            contenedorBarra.setVisible(false);
                            return null;
                        }

                    };
                    


                    new Thread(task).start();
                    
                }else{
                   JOptionPane.showMessageDialog(null, "No hay datos para generar el reporte", "Error De Operación", JOptionPane.ERROR_MESSAGE);
                }
            
            }else{
                JOptionPane.showMessageDialog(null, "No hay datos para generar el reporte", "Error De Operación", JOptionPane.ERROR_MESSAGE);
            }
        
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
    private final String filtro;
    
    public String generarNombre(){
        
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
    
    public GenerateReport(ArrayList<Votantes> list, String filtro){    
        this.lista=list;  
        this.filtro=filtro;
    }
    
    
    public boolean generarReporte(){
        boolean condicion=false;
        /*creamos carpeta principal*/
        File directorio = crearDirectorioPrincipal("Reportes-Electoral-Data-Control");
                
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Votantes");
        Footer footer = sheet.getFooter();  
        footer.setCenter("Pagina " + HeaderFooter.page() + " de " + HeaderFooter.numPages() ); 
        
        try {

            CellStyle style1=null, style2=null ;
            Row filaT = sheet.createRow(0);

            agregarTitulo(book, sheet, style1, "Arial", 31, filaT, 0, "Listado Sufragantes", 1);            
            sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 7));

            Row filaT1 = sheet.createRow(5);            
            filaT1.setHeight((short) (2*sheet.getDefaultRowHeight()));            
            agregarTitulo(book, sheet, style1, "Arial", 18, filaT1, 0, "Buscar Por: "+filtro, 0);            
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));

            //agregarTitulo(book, sheet, style2, "Arial", 21, filaT, 5, "Filtro: ");            
            //sheet.addMergedRegion(new CellRangeAddress(0, 4, 5, 7));
                        
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
            
            /*
            //System.out.println(sheet.getColumnWidthInPixels(5) + " (5 + 3) "+sheet.getColumnWidthInPixels(3));
                        
            InputStream is = new FileInputStream("src/resources/images/registraduria_logo.png");
            byte[] bytes = IOUtils.toByteArray(is);
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();

            CreationHelper help = sheet.getWorkbook().getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();
            
            //Create an anchor that is attached to the worksheet
            ClientAnchor anchor = help.createClientAnchor();
            //set top-left corner for the image
            anchor.setCol1(3);
            anchor.setRow1(10);

            //Creates a picture
            Picture pict = draw.createPicture(anchor, imgIndex);
            //Reset the image to the original size
            pict.resize();            
            
            
            /*
            CreationHelper help = sheet.getWorkbook().getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();

            ClientAnchor anchor = help.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);            
                     
            int width1=Math.round(((sheet.getColumnWidthInPixels(3))*50)/100);
            int width2=Math.round(((sheet.getColumnWidthInPixels(4))*50)/100);
            
            int widthFinal = width1>=width2 ? width1 : width2;
            
            anchor.setCol1(3); //first anchor determines upper left position
            anchor.setRow1(0);
            anchor.setDx1(Units.pixelToEMU(widthFinal)); //dx = left in px
            anchor.setDy1(Units.toEMU(10)); //dy = top in pt

            anchor.setCol2(4); //second anchor determines bottom right position
            anchor.setRow2(4); 
            anchor.setDx2(Units.pixelToEMU(widthFinal)); //dx = left + wanted width in px
            anchor.setDy2(Units.toEMU(10)); //dy= top + wanted height in pt
                        
            draw.createPicture(anchor, imgIndex);
            sheet.addMergedRegion(new CellRangeAddress(0, 4, 3, 4));
            */

          
            sheet.setRepeatingRows(CellRangeAddress.valueOf("1:7"));
            FileOutputStream fileOut = new FileOutputStream(directorio+"/"+generarNombre());
            book.write(fileOut);
            fileOut.close();
            
            condicion=true;
            
        }catch(IOException ex){
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return condicion;
        
    }

}
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javax.swing.JOptionPane;
import model.Lider;
import model.Persona;
import model.Votantes;
import modelDAO.LiderDAO;
import modelDAO.VotantesDAO;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;
import utlidades.Item;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelRegistroController implements Initializable, GeneralView {

    @FXML
    private JFXTextField apellidos, barrio, lugarDeVotacion, direccion,
                         mesaDeVotacion, correo, numeroDocumento, 
                         direccionVotacion, telefono, nombreCompleto;

    @FXML
    private JFXDatePicker fechaNacimiento;
    
    
    @FXML
    private JFXRadioButton radioMale, radioFemale;
    
    @FXML
    private JFXComboBox<String> tipoDocumento, estadoCivil;
    
    @FXML 
    private JFXComboBox<Item> fxComboLider;
    
    @FXML
    private GridPane panelRegistro;

    @FXML
    private JFXButton btnGuardar, btnLimpiar, btnActualizarLiderEncargado;
    
    private final VotantesDAO modelo = new VotantesDAO();
    private final LiderDAO modeloLider = new LiderDAO();
    
    
    /*Controlador de componentes visuales*/
    private final ToggleGroup groupRadioG = new ToggleGroup();
    private final RequiredFieldValidator validatorFieldText = new RequiredFieldValidator();
    private final NumberValidator validatorNumber = new NumberValidator();
    private ValidatorBase validatorEmail = new ValidatorBase() {
        @Override
        protected void eval() {}            
    };
    private ValidatorBase validatorAge = new ValidatorBase() {
        @Override
        protected void eval() {}            
    };
   
    private int idVotante=-1;
    
    private Item item;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initComponents(null);
        validatorText();
        validatorNumber();
        validatorEmail();
        validatorEdad();
        
    }    
    
    public void ActualizarDatos(Votantes votante){

        if(votante!=null){
            
            ControladorGeneral.CONTROLVIEWMODIFICAR=0;                                    

            idVotante = votante.getId();

            this.tipoDocumento.setValue(votante.getTipoDocumento());
            this.numeroDocumento.setText(String.valueOf(votante.getNumeroDocumento()));
            this.nombreCompleto.setText(votante.getNombre());
            this.apellidos.setText(votante.getApellido());
            //verificamos el tipo de genero que se va a seleccionar
            if(votante.getSexo().equals("Femenino")){
                this.radioFemale.setSelected(true);            
            }else{
                this.radioMale.setSelected(true);            
            }              

            if(votante.getFehaNacimiento()!=null){
                this.fechaNacimiento.setValue(LocalDate.parse(votante.getFehaNacimiento()));        
            }

            if(votante.getEstadoCivil()!=null){
                this.estadoCivil.setValue(votante.getEstadoCivil());            
            }

            this.direccion.setText(votante.getDireccion());
            this.barrio.setText(votante.getBarrio());
            this.telefono.setText(votante.getTelefono());
            this.correo.setText(votante.getCorreoElectronico());
            this.lugarDeVotacion.setText(votante.getLugar());
            this.mesaDeVotacion.setText(String.valueOf(votante.getMesa()));
            this.direccionVotacion.setText(votante.getDireccionLugar());
            
            listarLideres(1);
            
            Item it;
            if(votante.getLider().getId()==-1){
                it = new Item(-1, "Ninguno");
            }else{
                it = new Item(votante.getLider().getId(), votante.getLider().getNombre()+ " "+ votante.getApellido());            
            }
            
            ObservableList<Item> ob = fxComboLider.getItems();

            boolean condicion = true;
            
            int i=0;
            while(condicion){
                Item it1  = ob.get(i);
                if(it.getId()==it1.getId()){
                    condicion=false;
                    fxComboLider.setValue(ob.get(i));
                }
                i++;
                if(i==ob.size()){
                    condicion=false;
                }
            }
                        
            btnGuardar.setText("Modificar");

        }else{
            limpiarCampos();
        }
        
    
    
    }
    
    private boolean agregarVotantes(){
        
        //Creamos un objeto de tipo Votantes
        Votantes datosVotantes = new Votantes();
        
        JFXRadioButton rb = (JFXRadioButton)groupRadioG.getSelectedToggle(); 
        
        String sex="";
        if (rb != null) { 
            sex = rb.getText(); 
        }        
        
        //Obtenemos los valores de los campos de texto
        datosVotantes.setTipoDocumento(tipoDocumento.getValue());        
        datosVotantes.setNumeroDocumento(Integer.parseInt(numeroDocumento.getText()));        
        datosVotantes.setNombre(nombreCompleto.getText());
        datosVotantes.setApellido(apellidos.getText());        
        datosVotantes.setDireccion(direccion.getText());
        datosVotantes.setBarrio(barrio.getText());        
        datosVotantes.setTelefono(telefono.getText());
        datosVotantes.setCorreoElectronico(correo.getText());     
        datosVotantes.setSexo(sex);        
        datosVotantes.setLugar(lugarDeVotacion.getText());        
        datosVotantes.setMesa(mesaDeVotacion.getText());
        datosVotantes.setDireccionLugar(direccionVotacion.getText());   
        datosVotantes.setEstadoCivil(estadoCivil.getValue());

        Item itemLider;        
        if(fxComboLider==null){
            itemLider = new Item(-1, "Ninguno");
        }else{
            itemLider = fxComboLider.getValue();
        }     
        
        try{
            datosVotantes.getLider().setId(itemLider.getId());
        }catch(Exception ex){
            datosVotantes.getLider().setId(-1);            
        }   
        
        String fecha=null;

        try{

            if(fechaNacimiento.getValue()!=null){
                LocalDate date = fechaNacimiento.getValue();
                fecha = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();            
            }

        }catch(Exception ex){
            fecha=null;
        }
        
        
        datosVotantes.setFehaNacimiento(fecha);
        
        return modelo.agregarVotantes(datosVotantes);
            
    }
    
    private boolean modificarVotantes(){
        
        //Creamos un objeto de tipo Votantes
        Votantes datosVotantes = new Votantes();
        
        JFXRadioButton rb = (JFXRadioButton)groupRadioG.getSelectedToggle(); 
        
        String sex="";
        if (rb != null) { 
            sex = rb.getText(); 
        }        
        
        //Obtenemos los valores de los campos de texto
        datosVotantes.setId(idVotante);
        datosVotantes.setTipoDocumento(tipoDocumento.getValue());        
        datosVotantes.setNumeroDocumento(Integer.parseInt(numeroDocumento.getText()));        
        datosVotantes.setNombre(nombreCompleto.getText());
        datosVotantes.setApellido(apellidos.getText());        
        datosVotantes.setDireccion(direccion.getText());
        datosVotantes.setBarrio(barrio.getText());        
        datosVotantes.setTelefono(telefono.getText());
        datosVotantes.setCorreoElectronico(correo.getText());     
        datosVotantes.setSexo(sex);        
        datosVotantes.setLugar(lugarDeVotacion.getText());        
        datosVotantes.setMesa(mesaDeVotacion.getText());
        datosVotantes.setDireccionLugar(direccionVotacion.getText());   
        datosVotantes.setEstadoCivil(estadoCivil.getValue());

        Item itemLider;        
        if(fxComboLider==null){
            itemLider = new Item(-1, "Ninguno");
        }else{
            itemLider = fxComboLider.getValue();
        }     
<<<<<<< HEAD
        
        try{
            datosVotantes.getLider().setId(itemLider.getId());
        }catch(Exception ex){
            datosVotantes.getLider().setId(-1);            
        }   
        
        String fecha=null;

        try{
=======
        
        try{
            datosVotantes.getLider().setId(itemLider.getId());
        }catch(Exception ex){
            datosVotantes.getLider().setId(-1);            
        }   
        
        String fecha=null;

        try{
>>>>>>> 36a48334ae93ca73a7ea5761fec022f9b025982d

            if(fechaNacimiento.getValue()!=null){
                LocalDate date = fechaNacimiento.getValue();
                fecha = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();            
            }

        }catch(Exception ex){
            fecha=null;
        }
        
        
        datosVotantes.setFehaNacimiento(fecha);
        
        return modelo.modificarVotantes(datosVotantes);
            
    }

    private void limpiarCampos(){
        tipoDocumento.setValue("Cedula de Ciudadanía");
        numeroDocumento.setText("");
        nombreCompleto.setText("");
        apellidos.setText("");
        estadoCivil.setValue("Soltero(a)");
        radioMale.setSelected(true);
        direccion.setText("");
        fechaNacimiento.setValue(null);
        direccionVotacion.setText("");
        barrio.setText("");
        telefono.setText("");
        correo.setText("");
        lugarDeVotacion.setText("");
        mesaDeVotacion.setText("");
        idVotante = -1;
        btnGuardar.setText("Guardar");
        fxComboLider.setValue(null);
    }

    public void validatorText() {

        ControladorValidaciones.validator(nombreCompleto);
        ControladorValidaciones.validator(apellidos);
        ControladorValidaciones.validator(lugarDeVotacion);
        ControladorValidaciones.validator(direccionVotacion);
                       
    }

    public void validatorNumber() {

        ControladorValidaciones.validator(numeroDocumento);
        ControladorValidaciones.validator(mesaDeVotacion);
                
    }
    
    public void validatorEmail() {

        ControladorValidaciones.validator(correo);
                        
    }

    public void validatorEdad() {
        
        fechaNacimiento.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                fechaNacimiento.validate();
            }
        });

    }
    
    public void listarLideres(int update){
        
        
        ArrayList<Lider> list =  modeloLider.consultaLider("id/value", null);
        
        if(update!=1){
        
            if(list.size()>0){
                for(int i=0; i<list.size(); i++){
                    item = new Item(list.get(i).getId(), list.get(i).getNombre() + " "+list.get(i).getApellido());
                    fxComboLider.getItems().add(item);
                }
            }        
        
        }else{
<<<<<<< HEAD

            if(list.size()>0){

=======

            if(list.size()>0){

>>>>>>> 36a48334ae93ca73a7ea5761fec022f9b025982d
                int length = fxComboLider.getItems().size();
                fxComboLider.getItems().remove(0, length);
                
                item = new Item(-1, "Ninguno");
                fxComboLider.getItems().add(item);
                for(int i=0; i<list.size(); i++){
                    item = new Item(list.get(i).getId(), list.get(i).getNombre() + " "+list.get(i).getApellido());
                    fxComboLider.getItems().add(item);
                }
            
            }        
        
        }
        
    }
    
    @FXML
    public void eventsAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnGuardar) ){
            
            if(!numeroDocumento.getText().isEmpty() && !nombreCompleto.getText().isEmpty() &&
               !apellidos.getText().isEmpty() && !mesaDeVotacion.getText().isEmpty() &&
               !lugarDeVotacion.getText().isEmpty() && !direccionVotacion.getText().isEmpty()){

                if(ControladorValidaciones.onlyNumber(numeroDocumento.getText()) == true && 
                   ControladorValidaciones.onlyNumber(mesaDeVotacion.getText()) == true && 
                   ControladorValidaciones.isMayorDeEdad(fechaNacimiento.getValue()) == true){

                    if(btnGuardar.getText().equals("Guardar")){
                        
                        if(modelo.validarNumeroIdentificacion(tipoDocumento.getValue(), numeroDocumento.getText())){

                            JOptionPane.showMessageDialog(null, "El número de identificación ya ha sido registrado en la base de datos", "ERROR", JOptionPane.WARNING_MESSAGE);
                        
                        }else{

                            if(agregarVotantes()==true){
                                JOptionPane.showMessageDialog(null, "Los Datos se almacenaron exitosamente", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                                limpiarCampos();
                            }else{
                                 JOptionPane.showMessageDialog(null, "Operación invalida, Posibles errores : \n"+
                                                                     ControladorValidaciones.EXCEPCIONES, 
                                                                     "ERROR", JOptionPane.ERROR_MESSAGE);           
                                 ControladorValidaciones.EXCEPCIONES="";
                            }
                            
                        }

                    }else if(btnGuardar.getText().equals("Modificar")){

                        if(modificarVotantes()==true){
                            JOptionPane.showMessageDialog(null, "Los Datos han sido actualizados exitosamente", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                            idVotante=-1;
                            limpiarCampos();
                        }else{
                             JOptionPane.showMessageDialog(null, "Operación invalida, Posibles errores : \n"+
                                                                 ControladorValidaciones.EXCEPCIONES, 
                                                                 "ERROR", JOptionPane.ERROR_MESSAGE);           
                             ControladorValidaciones.EXCEPCIONES="";
                        }

                    }

                }else{
                    
                    JOptionPane.showMessageDialog(null, "Operación fallida, Posibles errores : \n"+
                                                        "1- Errores de formato( valores numericos fuera de rango )\n"+
                                                        "2- El campo número documento y mesa de votación solo acepta valores numericos\n"+
                                                        "3- La fecha de nacimiento ingresada no es valida, debe ser mayor de 18 años \n"+
                                                        "<html><p> <span style='color:red;'>Nota:</span> Verifique la información e intente nuevamente.</p></html>" , "ERROR", JOptionPane.ERROR_MESSAGE);            
                
                }

            }else{
                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos obligatorios", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
            }


        
        }else if(evt.equals(btnLimpiar)){
            
            limpiarCampos();
        
        }else if(evt.equals(btnActualizarLiderEncargado)){
            listarLideres(1);
        }
        
    }

    
    /*eventos de teclado*/
    @FXML
    public void eventsKeyTyped(KeyEvent event){
        
        Object evt = event.getSource();

        char c = event.getCharacter().charAt(0);
        
        if(evt.equals(numeroDocumento) || evt.equals(mesaDeVotacion)){
        
            if(!Character.isDigit(c) || !ControladorValidaciones.validateWhiteSpaces(c+"")){
                event.consume();
            }
            
            if(evt.equals(numeroDocumento)){
                
                if(!ControladorValidaciones.validateLegth(numeroDocumento.getText(), 10)){
                    event.consume();
                }
                            
            }        
            
        }else if(evt.equals(telefono) || evt.equals(correo)){
            
            if(!ControladorValidaciones.validateWhiteSpaces(String.valueOf(c))){
                event.consume();
            }
            
            if(evt.equals(telefono)){
                if(!Character.isDigit(c)){
                    event.consume();
                }                
                
                if(!ControladorValidaciones.validateLegth(telefono.getText(), 30)){
                    event.consume();
                }                

            }else if(evt.equals(correo)){

                if(!ControladorValidaciones.validateLegth(correo.getText(), 255)){
                    event.consume();
                }                                
            }
        
        }else if(evt.equals(nombreCompleto)){

            if(!ControladorValidaciones.validateLegth(nombreCompleto.getText(), 100)){
                event.consume();
            }
            
        }else if(evt.equals(apellidos)){

            if(!ControladorValidaciones.validateLegth(apellidos.getText(), 100)){
                event.consume();
            }
            
        }else if(evt.equals(direccion)){

            if(!ControladorValidaciones.validateLegth(direccion.getText(), 255)){
                event.consume();
            }
                        
        }else if(evt.equals(barrio)){
        
            if(!ControladorValidaciones.validateLegth(barrio.getText(), 255)){
                event.consume();
            }
            
        }
        
    }

    @Override
    public void initComponents(Object obj) {
        
        String tipoDocumentoSelected="Cedula de Ciudadanía";
        String estadoCivilSelected="Soltero(a)";
            
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter.format(object);
                } else {
                    return "";
                }                    
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }                    
            }

        };           
            
        fechaNacimiento.setConverter(converter);
        radioMale.setSelected(true);            
        radioMale.setToggleGroup(groupRadioG);
        radioFemale.setToggleGroup(groupRadioG);

        tipoDocumento.getItems().add("Cedula de Ciudadanía");
        tipoDocumento.getItems().add("Cedula de Extranjería");
        tipoDocumento.setValue(tipoDocumentoSelected);

        estadoCivil.getItems().add("Soltero(a)");
        estadoCivil.getItems().add("Casado(a)");
        estadoCivil.setValue(estadoCivilSelected);


        /*Agregamos validadores a los campos de texto y similares*/        
        numeroDocumento.getValidators().addAll(validatorFieldText, validatorNumber);        
        nombreCompleto.getValidators().add(validatorFieldText);
        apellidos.getValidators().add(validatorFieldText);
        lugarDeVotacion.getValidators().add(validatorFieldText);
        mesaDeVotacion.getValidators().addAll(validatorFieldText, validatorNumber);
        direccionVotacion.getValidators().add(validatorFieldText); 
        telefono.getValidators().add(validatorNumber);

        /*validacion de email*/
        validatorEmail = new ValidatorBase() {
            @Override
            protected void eval() {
                setMessage("Email invalido");
                if (correo.getText().length() < 2 || !isValidEmailAddress(correo.getText())) {
                    hasErrors.set(true);
                } else hasErrors.set(false);
            }

            private boolean isValidEmailAddress(String text) {
                String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
                return text.matches(regex);
            }

        };

        /*validacion de email*/
        validatorAge = new ValidatorBase() {
            @Override
            protected void eval() {
                setMessage("Debe ser mayor de 18 años");
                try{
                    LocalDate value = fechaNacimiento.getValue();
                    if (!isMAyorDeEdad(value.getYear())) {
                        hasErrors.set(true);
                    } else hasErrors.set(false);                
                }catch(Exception ex){
                    hasErrors.set(false);
                }
            }

            private boolean isMAyorDeEdad(int year) {
                int yearA = Calendar.getInstance().get(Calendar.YEAR);
                return (yearA-year)>=18;
            }

        };

        correo.getValidators().addAll(validatorFieldText, validatorEmail);
        fechaNacimiento.getValidators().add(validatorAge);

        /*mensaje de validaciones*/
        validatorFieldText.setMessage("Este campo es obligatorio");
        validatorNumber.setMessage("Solo se permite el ingreso de números");
        
        item = new Item(-1, "Ninguno");
        fxComboLider.getItems().add(item);
        listarLideres(0);
        fxComboLider.setVisibleRowCount(10);
        
    }

    @Override
    public void controlVisibilidad(Node node, boolean condicion) {

        
    }




    
    
    
    
    
    
}

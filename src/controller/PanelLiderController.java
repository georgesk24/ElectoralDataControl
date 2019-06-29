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
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import javax.swing.JOptionPane;
import model.Lider;
import model.Persona;
import modelDAO.LiderDAO;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;

/**
 * FXML Controller class
 *
 * @author jorge
 */
public class PanelLiderController implements Initializable, GeneralView {

    @FXML
    private JFXTextField apellidos, barrio, direccion,
                         correo, numeroDocumento, 
                         telefono, nombreCompleto;

    @FXML
    private JFXDatePicker fechaNacimiento;
    
    
    @FXML
    private JFXRadioButton radioMale, radioFemale;
    
    
    @FXML
    private JFXComboBox<String> tipoDocumento, estadoCivil;
        

    @FXML
    private JFXButton btnGuardar, btnLimpiar;
    
    private final LiderDAO modelo = new LiderDAO();

    
    /*Controlador de componentes visuales*/
    private final ToggleGroup groupRadio = new ToggleGroup();
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
   
    private int idLider=-1;
    
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
    
    public void ActualizarDatos(Persona persona){
    
        if(persona!=null){
            
            ControladorGeneral.CONTROLVIEWMODIFICARLIDER=0;                                    

            idLider = persona.getId();

            this.tipoDocumento.setValue(persona.getTipoDocumento());
            this.numeroDocumento.setText(String.valueOf(persona.getNumeroDocumento()));
            this.nombreCompleto.setText(persona.getNombre());
            this.apellidos.setText(persona.getApellido());
            //verificamos el tipo de genero que se va a seleccionar
            if(persona.getSexo().equals("Femenino")){
                this.radioFemale.setSelected(true);            
            }else{
                this.radioMale.setSelected(true);            
            }              

            if(persona.getFehaNacimiento()!=null){
                this.fechaNacimiento.setValue(LocalDate.parse(persona.getFehaNacimiento()));        
            }

            if(persona.getEstadoCivil()!=null){
                this.estadoCivil.setValue(persona.getEstadoCivil());            
            }

            this.direccion.setText(persona.getDireccion());
            this.barrio.setText(persona.getBarrio());
            this.telefono.setText(persona.getTelefono());
            this.correo.setText(persona.getCorreoElectronico());
            
            btnGuardar.setText("Modificar");

        }else{
            limpiarCampos();
        }
        
        
        
    }
    
    private boolean agregarLider(){
        
        //Creamos un objeto de tipo Votantes
        Lider datosLider = new Lider();
        
        JFXRadioButton rb = (JFXRadioButton)groupRadio.getSelectedToggle(); 
        
        String sex="";
        if (rb != null) { 
            sex = rb.getText(); 
        }         
        
        //Obtenemos los valores de los campos de texto
        datosLider.setTipoDocumento(tipoDocumento.getValue());        
        datosLider.setNumeroDocumento(Integer.parseInt(numeroDocumento.getText()));        
        datosLider.setNombre(nombreCompleto.getText());
        datosLider.setApellido(apellidos.getText());        
        datosLider.setDireccion(direccion.getText());
        datosLider.setBarrio(barrio.getText());        
        datosLider.setTelefono(telefono.getText());
        datosLider.setCorreoElectronico(correo.getText());     
        datosLider.setSexo(sex);        
        datosLider.setEstadoCivil(estadoCivil.getValue());
        
        String fecha=null;

        try{

            if(fechaNacimiento.getValue()!=null){
                LocalDate date = fechaNacimiento.getValue();
                fecha = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();            
            }

        }catch(Exception ex){
            fecha=null;
        }
        
        
        datosLider.setFehaNacimiento(fecha);
        
        return modelo.agregarLider(datosLider);

    }
    
    private boolean modificarLider(){
        
        //Creamos un objeto de tipo Votantes
        Lider datoLider = new Lider();
        
        JFXRadioButton rb = (JFXRadioButton)groupRadio.getSelectedToggle(); 
        
        String sex="";
        if (rb != null) { 
            sex = rb.getText(); 
        }        
                
        //Obtenemos los valores de los campos de texto
        datoLider.setId(idLider);
        datoLider.setTipoDocumento(tipoDocumento.getValue());        
        datoLider.setNumeroDocumento(Integer.parseInt(numeroDocumento.getText()));        
        datoLider.setNombre(nombreCompleto.getText());
        datoLider.setApellido(apellidos.getText());        
        datoLider.setDireccion(direccion.getText());
        datoLider.setBarrio(barrio.getText());        
        datoLider.setTelefono(telefono.getText());
        datoLider.setCorreoElectronico(correo.getText());     
        datoLider.setSexo(sex);        
        datoLider.setEstadoCivil(estadoCivil.getValue());
        
        String fecha=null;

        try{

            if(fechaNacimiento.getValue()!=null){
                LocalDate date = fechaNacimiento.getValue();
                fecha = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();            
            }

        }catch(Exception ex){
            fecha=null;
        }        
        
        datoLider.setFehaNacimiento(fecha);
        
        return modelo.modificarLider(datoLider);
            
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
        barrio.setText("");
        telefono.setText("");
        correo.setText("");
        btnGuardar.setText("Guardar");
    }

    public void validatorText() {

        ControladorValidaciones.validator(nombreCompleto);
        ControladorValidaciones.validator(apellidos);
                       
    }

    public void validatorNumber() {

        ControladorValidaciones.validator(numeroDocumento);
                
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
    

    
    @FXML
    public void eventsAction(ActionEvent event) {
        
        Object evt = event.getSource();
        
        if(evt.equals(btnGuardar) ){
            
        
            if(!numeroDocumento.getText().isEmpty() && !nombreCompleto.getText().isEmpty() &&
               !apellidos.getText().isEmpty() ){

                if(ControladorValidaciones.onlyNumber(numeroDocumento.getText()) == true && 
                   ControladorValidaciones.isMayorDeEdad(fechaNacimiento.getValue()) == true){

                    switch (btnGuardar.getText()) {
                        
                        case "Guardar":
                            if(modelo.validarNumeroIdentificacion(tipoDocumento.getValue(), numeroDocumento.getText())){
                                
                                JOptionPane.showMessageDialog(null, "El número de identificación ya ha sido registrado en la base de datos", "ERROR", JOptionPane.WARNING_MESSAGE);
                                
                            }else{
                                
                                if(agregarLider()==true){
                                    JOptionPane.showMessageDialog(null, "La información ha sido almacenada de manera exitosa", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                                    limpiarCampos();
                                }else{
                                    JOptionPane.showMessageDialog(null, "Operación invalida, Posibles errores : \n"+
                                            ControladorValidaciones.EXCEPCIONES,
                                            "ERROR", JOptionPane.ERROR_MESSAGE);
                                    ControladorValidaciones.EXCEPCIONES="";
                                }
                                
                            }   break;
                        
                        case "Modificar":
                            if(modificarLider()==true){
                                JOptionPane.showMessageDialog(null, "Los Datos han sido actualizados exitosamente", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                                idLider=-1;
                            }else{
                                JOptionPane.showMessageDialog(null, "Operación invalida, Posibles errores : \n"+
                                        ControladorValidaciones.EXCEPCIONES,
                                        "ERROR", JOptionPane.ERROR_MESSAGE);
                                ControladorValidaciones.EXCEPCIONES="";
                            }  
                        break;
                    
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
        }
        
    }

    
    /*eventos de teclado*/
    @FXML
    public void eventsKeyTyped(KeyEvent event){
        
        Object evt = event.getSource();

        char c = event.getCharacter().charAt(0);
        
        if(evt.equals(numeroDocumento)){
        
            if(!Character.isDigit(c) || !ControladorValidaciones.validateLegth(numeroDocumento.getText(), 10)){
                event.consume();
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
        
        }else if(evt.equals(telefono) || evt.equals(correo)){
            
            if(!ControladorValidaciones.validateWhiteSpaces(String.valueOf(c))){
                event.consume();
            }
            
            if(evt.equals(telefono)){
                
                if(!Character.isDigit(c) || !ControladorValidaciones.validateLegth(telefono.getText(), 30)){
                    event.consume();
                }                
                
            }else if(evt.equals(correo)){

                if(!ControladorValidaciones.validateLegth(correo.getText(), 255)){
                    event.consume();
                }                                
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
        radioMale.setToggleGroup(groupRadio);
        radioFemale.setToggleGroup(groupRadio);


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
        
                
    }

    @Override
    public void controlVisibilidad(Node node, boolean condicion) {

        
    }




    
    
    
    
    
    
}

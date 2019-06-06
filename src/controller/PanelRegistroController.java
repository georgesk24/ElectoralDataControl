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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javax.swing.JOptionPane;
import model.Votantes;
import modelDAO.VotantesDAO;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;
import utlidades.GeneralView;

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
    
    /*
    @FXML
    private DatePicker fechaNacimiento;*/

    @FXML
    private JFXComboBox<String> tipoDocumento, estadoCivil;
        
    @FXML
    private GridPane panelRegistro;

    @FXML
    private JFXButton btnGuardar, btnLimpiar;
    
    private final VotantesDAO modelo = new VotantesDAO();

    
    
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
   
    private int idVotante=-1;
    
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
    
    private boolean agregarVotantes(){
        
        //Creamos un objeto de tipo Votantes
        Votantes datosVotantes = new Votantes();
        
        //Obtenemos los valores de los campos de texto
        datosVotantes.setTipoDocumento(tipoDocumento.getValue());        
        datosVotantes.setNumeroDocumento(Integer.parseInt(numeroDocumento.getText()));        
        datosVotantes.setNombre(nombreCompleto.getText());
        datosVotantes.setApellido(apellidos.getText());        
        datosVotantes.setDireccion(direccion.getText());
        datosVotantes.setBarrio(barrio.getText());        
        datosVotantes.setTelefono(telefono.getText());
        datosVotantes.setCorreoElectronico(correo.getText());     
        datosVotantes.setSexo(groupRadio.getSelectedToggle().getUserData().toString());        
        datosVotantes.setLugar(lugarDeVotacion.getText());        
        datosVotantes.setMesa(mesaDeVotacion.getText());
        datosVotantes.setDireccionLugar(direccionVotacion.getText());   
        datosVotantes.setEstadoCivil(estadoCivil.getValue());
        
        String fecha="";
        try{
            LocalDate date = fechaNacimiento.getValue();
            fecha = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
        }catch(Exception ex){}
        
        
        datosVotantes.setFehaNacimiento(fecha);
        
        return modelo.agregarVotantes(datosVotantes);
            
    }
    
    private boolean modificarVotantes(){
        
        //Creamos un objeto de tipo Votantes
        Votantes datosVotantes = new Votantes();
        
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
        datosVotantes.setSexo(groupRadio.getSelectedToggle().getUserData().toString());        
        datosVotantes.setLugar(lugarDeVotacion.getText());        
        datosVotantes.setMesa(mesaDeVotacion.getText());
        datosVotantes.setDireccionLugar(direccionVotacion.getText());   
        datosVotantes.setEstadoCivil(estadoCivil.getValue());
        
        String fecha="";
        try{
            LocalDate date = fechaNacimiento.getValue();
            fecha = date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
        }catch(Exception ex){}
        
        
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

                        if(agregarVotantes()==true){
                            JOptionPane.showMessageDialog(null, "Los Datos se almacenaron exitosamente", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                             JOptionPane.showMessageDialog(null, "Operación invalida, Posibles errores : \n"+
                                                                 ControladorValidaciones.EXCEPCIONES, 
                                                                 "ERROR", JOptionPane.ERROR_MESSAGE);           
                             ControladorValidaciones.EXCEPCIONES="";
                        }

                    }else if(btnGuardar.getText().equals("Modificar")){

                        if(modificarVotantes()==true){
                            JOptionPane.showMessageDialog(null, "Los Datos han sido actualizados exitosamente", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                             JOptionPane.showMessageDialog(null, "Operación invalida, Posibles errores : \n"+
                                                                 ControladorValidaciones.EXCEPCIONES, 
                                                                 "ERROR", JOptionPane.ERROR_MESSAGE);           
                             ControladorValidaciones.EXCEPCIONES="";
                        }

                    }

                }else{
                    JOptionPane.showMessageDialog(null, "Operación fallida, Posibles errores : \n"+
                                                        "1- El campo número documento y mesa de votación solo acepta valores numericos\n"+
                                                        "2- La fecha de nacimiento ingresada no es valida, debe ser mayor de 18 años \n"+
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
        
        if(evt.equals(numeroDocumento) || evt.equals(mesaDeVotacion)){
        
            if(!Character.isDigit(c)){
                event.consume();
            }
            
        }
        
    }

    @Override
    public void initComponents(Object obj) {
        
            String sexo = "Masculino";
            String tipoDocumentoSelected="Cedula de Ciudadanía";
            String estadoCivilSelected="Soltero(a)";
            String fecha = "";
            
            if(obj!=null){
                
                ControladorGeneral.CONTROLVIEWMODIFICAR=0;                                    
                Votantes votante = (Votantes) obj;

                idVotante = votante.getId();
                
                sexo = votante.getSexo();
                tipoDocumentoSelected = votante.getTipoDocumento();
                
                tipoDocumento.getItems().remove(0, 2);
                estadoCivil.getItems().remove(0, 2);
                
                numeroDocumento.setText(String.valueOf(votante.getNumeroDocumento()));
                nombreCompleto.setText(votante.getNombre());
                apellidos.setText(votante.getApellido());
                
                estadoCivilSelected = votante.getEstadoCivil();
                
                direccion.setText(votante.getDireccion());
                barrio.setText(votante.getBarrio());
                telefono.setText(votante.getTelefono());
                correo.setText(votante.getCorreoElectronico());
                
                lugarDeVotacion.setText(votante.getLugar());
                direccionVotacion.setText(votante.getDireccionLugar());
                mesaDeVotacion.setText(votante.getMesa());
            
                fecha = votante.getFehaNacimiento();
                
                btnGuardar.setText("Modificar");
                                
            }else{
                limpiarCampos();
            }

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
            if(!fecha.equals("")){
                fechaNacimiento.setValue(LocalDate.parse(fecha));        
            }
            
            radioFemale.setToggleGroup(groupRadio);
            radioFemale.setUserData("Femenino");
            radioMale.setToggleGroup(groupRadio);
            radioMale.setUserData("Masculino");            
            //verificamos el tipo de genero que se va a seleccionar
            if(sexo.equals("Femenino")){
                radioFemale.setSelected(true);            
            }else{
                radioMale.setSelected(true);            
            }  
            
            
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

package utlidades;

import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import java.util.Calendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author georgep24
 * 
 *<b Clase ControladorValidaciones, todos los métodos y funciones aqui creados serán de acceso public 
 * con el fin de acceder a estos desde otras clases igualmente serán de tipo static
 * para evitar crear una instancia de la clase y que dichos metodos o funciones se puedan invocar sin la necesidad de la creación de objetos
 * 
 * Esta clase tendra un atributo de tipo string para capturar los respectivos errores u excepciones que surgan en la ejecución del programa />
 * 
 */
public class ControladorValidaciones {
    
    public static String EXCEPCIONES;
    
    /*función de tipo boolean para validar que el dato ingresado por teclado sea un valor numerico*/
    public static boolean onlyNumber(String text){
        
        boolean condicion = false;
        
        try{
            int value = Integer.parseInt(text);
            condicion=true;
        }catch(NumberFormatException ex){
            System.out.println(ex.getMessage());
        }
        
        return condicion;
    
    }
    
    /*Función de tipo boolean donde validamos si la fecha de nacimiento corresponde a una persona mayor de edad*/
    public static boolean isMayorDeEdad(LocalDate date){
        
        try{
            int year = date.getYear();
            int yearA = Calendar.getInstance().get(Calendar.YEAR);
            return (yearA-year)>=18;      
        }catch(Exception ex){
            return true;
        }
        
    }

    /*función de tipo boolean para validar que el dato ingresado por teclado sea un valor numerico*/
    public static boolean validateWhiteSpaces(String text){
                
        boolean condicion = !text.equals(" ");           
        return condicion;
    
    }

    
    /*Método donde agregamos los validadores a los respectivos textfield que lo requieran*/
    public static void validator(JFXTextField node){
        
        node.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                node.validate();
            }
    
        });

    }
    
    /*Función donde validamos la longitud de una cadena, esta debe ser menor(<) o igual(=) a 10 */
    public static boolean validateLegth(String text){
        return (text.length()+1) <= 10;
    }

    public static boolean validateLegth(String text, int limite){
        return (text.length()+1) <= limite;
    }
    
    /*Función de tipo boolean en la cual validamos a partir de una cadena de texto recibida si este corresponde a una dirección E-mail*/
    public static boolean isValidEmailAddress(String text) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return text.matches(regex);
    }

    
    
}

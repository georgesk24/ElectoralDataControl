package utlidades;

import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import java.util.Calendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

/**
 *
 * @author georgep24
 */
public class ControladorValidaciones {
    
    public static String EXCEPCIONES;
    
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
    
    public static boolean isMayorDeEdad(LocalDate date){
        
        try{
            int year = date.getYear();
            int yearA = Calendar.getInstance().get(Calendar.YEAR);
            return (yearA-year)>=18;      
        }catch(Exception ex){
            return true;
        }
        
    }
    
    public static void validator(JFXTextField node){
        
        node.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                node.validate();
            }
    
        });

    }
    
    public static boolean validateLegth(String text){
        return (text.length()+1) <= 10;
    }

    public static boolean isValidEmailAddress(String text) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return text.matches(regex);
    }

    
    
}

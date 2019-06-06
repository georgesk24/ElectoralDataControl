/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utlidades;

import controller.PanelAjustesController;
import controller.PrincipalController;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JOptionPane;
import model.Usuario;
import view.ElectoralDataControl;

/**
 *
 * @author georgep24
 */
public class ControladorGeneral {
 
    public static int CONTROLVIEWMODIFICAR=0;
    public static int CONTROLVIEWSTAGE=0;
    public static Usuario CONTROLSESION=null;
        
    public static String abreviarTipoDocumento(String text){

        String res="";
        
        switch(text){
            
            case "Cedula de Ciudadanía":
                res="CC";
            break;

            case "Cedula de Extranjería":
                res="CE";
            break;
            
            
        }
        
        return res;
        
    }


    public static String abreviarSexo(String text){

        String res="";
        
        switch(text){
            
            case "Femenino":
                res="F";
            break;

            case "Masculino":
                res="M";
            break;
            
            
        }
        
        return res;
        
        
    }
    
    public static int toInt(double value){
        try{
            return (int) value;
        }catch(Exception ex){
            return 0;
        }
    }
    
    public static byte [] toBlob(File file){
        
        //creamos un array de bytes
        byte [] fileContent = new byte[(int)file.length()];
        
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);                    
            input.read(fileContent);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PanelAjustesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControladorGeneral.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(input!=null){
                    input.close();                
                }
            } catch (IOException ex) {
                Logger.getLogger(PanelAjustesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return fileContent;
        
    }

    public static Image toImage(byte [] arrayImage){
        
        return new Image(new ByteArrayInputStream(arrayImage));
        
    }

    public static void resetSesion(){

        PrincipalController configPrincipal = ElectoralDataControl.loader.getController();
        configPrincipal.lbNombreUsuario.setText(ControladorGeneral.CONTROLSESION.getUser());
        configPrincipal.imageProfile.setFill(new ImagePattern(ControladorGeneral.toImage(ControladorGeneral.CONTROLSESION.getImage())));
    
    }
    
    public static String decryptPassword(String pass){
        
        int i=12;
        String newPass="";
        
        if(i<pass.length()){
            while(pass.charAt(i) != ','){
                newPass= newPass + pass.charAt(i);
                i++;
                if(i==pass.length()){
                    break;
                }
            }            
        }
        
        return newPass;
    }
    
    public static Address [] toArrayAddress(ArrayList<String> array ){
        
        Address emails[] = new Address[array.size()];
        
        for (int i = 0; i < emails.length; i++) {
            try {
                emails[i] = new InternetAddress(array.get(i));
            } catch (AddressException ex) {
                Logger.getLogger(ControladorGeneral.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return emails;
    }
    
    public static ArrayList<String> toArrayString(String text){
        
        int num=0, i=0, init=0;
        boolean condicion = true;
        
        ArrayList<String> listaMails = new ArrayList<>();
        while(condicion){
            
            while(text.charAt(i)!=','){
                i++;
                if(i==text.length()){
                    condicion=false;
                    break;                    
                }
            }
            
            if(i!=init){

                String preMail=deleteWhiteSpaces(text.substring(init, i));                
                if(ControladorValidaciones.isValidEmailAddress(preMail)){
                    listaMails.add(preMail);            
                }
            
            }

            i++;
            init=i;
            
            if(i==text.length()){
                condicion=false;                
            }
            
        }
        
        return listaMails;
    
    }
    
    private static String deleteWhiteSpaces(String text){

        String newString="";

        for (int i = 0; i < text.length(); i++) {

            if(text.charAt(i)!=' '){
                newString=newString+text.charAt(i);
            }

        }
        
        return newString;
    }
  
    
    
}

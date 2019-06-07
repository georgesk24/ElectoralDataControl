/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Conexion;
import model.Persona;
import utlidades.ControladorValidaciones;

/**
 *
 * @author lenov0o
 */
public class LiderDAO {
    
    private final Conexion conexion;
    private Connection conectar;
    private PreparedStatement pst;
    private ResultSet rs;
    
    public LiderDAO(){
        conexion=new Conexion();
    }

    public boolean agregarLider(Persona lider){
        
        boolean estado = false;
    
        try{
            
            conectar = conexion.conectar();
            
            if(conectar!=null){
            
                String sql = "INSERT INTO "
                            + "lider(tipo_documento, numero_documento, nombre, apellido, sexo, fecha_nacimiento, direccion, barrio, estado_civil, telefono, correo_electronico) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                pst = conectar.prepareStatement(sql);
                pst.setString(1, lider.getTipoDocumento());
                pst.setInt(2, lider.getNumeroDocumento());
                pst.setString(3, lider.getNombre());
                pst.setString(4, lider.getApellido());
                pst.setString(5, lider.getSexo());
                pst.setString(6, lider.getFehaNacimiento());
                pst.setString(7, lider.getDireccion());
                pst.setString(8, lider.getBarrio());
                pst.setString(9, lider.getEstadoCivil());
                pst.setString(10, lider.getTelefono());
                pst.setString(11, lider.getCorreoElectronico());
                
                int res = pst.executeUpdate();
                
                estado = res > 0 ;

                ControladorValidaciones.EXCEPCIONES="";
                
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";
            }
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();
        }finally{
            try{
                if(conexion!=null){
                    conexion.cerrar(conectar);               
                }
            }catch(Exception ex){}
        }
        
        
        return estado;
    
    
    }
    
    public ArrayList<Persona> consultaLider(String filtro, ArrayList<String> data){return null;}
    
    public boolean modificarLider(Persona lider){return false;}
    
    public boolean eliminarLider(int id){return false;}
    
    /*MOTODOS DE VALIDACION*/
    
    public boolean validarNumeroIdentificacion(String tipo_documento, String numero_documento){
        
        boolean res = false;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){
                
                String sql ="SELECT tipo_documento, numero_documento FROM lider WHERE tipo_documento=? AND numero_documento=?";
                
                pst = conectar.prepareStatement(sql);
                pst.setString(1, tipo_documento);
                pst.setString(2, numero_documento);
                
                rs = pst.executeQuery();
                                
                if(rs.next()){
                    res=true;
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                if(conexion!=null){
                    conexion.cerrar(conectar);                
                }
            }catch(Exception ex){
                
            }
        }
        
        return res;
        
    }
    
    
    
    
}

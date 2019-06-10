
package modelDAO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Conexion;
import model.Usuario;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;

/**
 *
 * @author root
 */
public class UsuarioDAO {

    private final Conexion conexion;
    private Connection conectar;
    private PreparedStatement pst;
    private ResultSet rs;
    
    public UsuarioDAO(){
        conexion=new Conexion();
    }    
    
    public Usuario consultarUsuario(String user, String pass){
        
        Usuario usuario = null;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "SELECT id, usuario, foto, email, password_email FROM usuario WHERE usuario=? AND pass=?";
                //CAST(AES_DECRYPT(AES_ENCRYPT(1234,key), key)AS char(50)) as pass
                pst = conectar.prepareStatement(sql);
                pst.setString(1, user);
                pst.setString(2, "AES_ENCRYPT("+pass+", key)");

                rs = pst.executeQuery();

                while(rs.next()){

                    usuario = new Usuario();
                    
                    usuario.setId(rs.getInt("id"));
                    usuario.setUser(rs.getString("usuario"));
                    usuario.setPassword(pass);
                    usuario.setImage(rs.getBytes("foto"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPasswordEmail(ControladorGeneral.decryptPassword(rs.getString("password_email")));
                    
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                conexion.cerrar(conectar);
            }catch(Exception ex){
                
            }
        }
        
        return usuario;
        
    }
        
    public String decryptPassword(String usuario){
    
        String pass="";
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "SELECT CAST(AES_DECRYPT(AES_ENCRYPT(pass, 'key'), 'key') AS char(50))  as pass FROM usuario WHERE usuario=?";

                pst = conectar.prepareStatement(sql);
                pst.setString(1, usuario);

                rs = pst.executeQuery();

                if(rs.next()){
                    pass = rs.getString("pass");
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                conexion.cerrar(conectar);
            }catch(Exception ex){
                
            }
        }
        
        return pass;
        
    }
    
    public boolean validateUsuario(String usuario){
        
        boolean condicion=false;

        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "SELECT usuario FROM usuario WHERE usuario=?";

                pst = conectar.prepareStatement(sql);
                pst.setString(1, usuario);

                rs = pst.executeQuery();

                if(rs.next()){
                    condicion=true;
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                conexion.cerrar(conectar);
            }catch(Exception ex){
                
            }
        }
        
        return condicion;
    }
 
    public boolean actualizarDatosUsuario(String usuario, String pass){

        boolean condicion=false;

        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "UPDATE usuario SET usuario=?, pass=? WHERE usuario=?";

                pst = conectar.prepareStatement(sql);
                pst.setString(1, usuario);
                pst.setString(2, "AES_ENCRYPT("+pass+", key)");
                pst.setString(3, usuario);

                int res = pst.executeUpdate();

                if(res>0){
                    condicion=true;
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                conexion.cerrar(conectar);
            }catch(Exception ex){
                
            }
        }
        
        return condicion;
                
    }

    public boolean actualizarImagenUsuario(String usuario, File imagen){

        boolean condicion=false;

        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "UPDATE usuario SET foto=? WHERE usuario=?";

                pst = conectar.prepareStatement(sql);
                pst.setBytes(1, ControladorGeneral.toBlob(imagen));
                pst.setString(2, usuario);

                int res = pst.executeUpdate();

                if(res>0){
                    condicion=true;
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                conexion.cerrar(conectar);
            }catch(Exception ex){
                
            }
        }
        
        return condicion;
                
    }

    public boolean actualizarNombreUsuario(String usuarioNuevo, int id){

        boolean condicion=false;

        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "UPDATE usuario SET usuario=? WHERE id=?";

                pst = conectar.prepareStatement(sql);
                pst.setString(1, usuarioNuevo);
                pst.setInt(2, id);

                int res = pst.executeUpdate();

                if(res>0){
                    condicion=true;
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                conexion.cerrar(conectar);
            }catch(Exception ex){
                
            }
        }
        
        return condicion;
                
    }

    public boolean actualizarPasswordUsuario(String newPassword, int id){

        boolean condicion=false;

        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "UPDATE usuario SET pass=? WHERE id=?";

                pst = conectar.prepareStatement(sql);
                pst.setString(1, "AES_ENCRYPT("+newPassword+", key)");
                pst.setInt(2, id);

                int res = pst.executeUpdate();

                if(res>0){
                    condicion=true;
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                conexion.cerrar(conectar);
            }catch(Exception ex){
                
            }
        }
        
        return condicion;
                
    }

    public boolean actualizarEmailUsuario(String emailNuevo, String pass,  int id){

        boolean condicion=false;

        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "UPDATE usuario SET email=?, password_email=? WHERE id=?";

                pst = conectar.prepareStatement(sql);
                pst.setString(1, emailNuevo);
                pst.setString(2, "AES_ENCRYPT("+pass+", key)");
                pst.setInt(3, id);

                int res = pst.executeUpdate();

                if(res>0){
                    condicion=true;
                }
                
                ControladorValidaciones.EXCEPCIONES="";
                
            }else{
                ControladorValidaciones.EXCEPCIONES="* Error al conectar con la base de datos\n";            
            }
            
            
        }catch(SQLException ex){
            ControladorValidaciones.EXCEPCIONES= "* Error de ejecución : "+ex.getMessage();        
        }finally{
            try{
                conexion.cerrar(conectar);
            }catch(Exception ex){
                
            }
        }
        
        return condicion;
                
    }


    
}


package modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Conexion;
import model.Lider;
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

    public boolean agregarLider(Lider lider){
        
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
    
    public ArrayList<Lider> consultaLider(String filtro, ArrayList<String> data){
        
        ArrayList list = new ArrayList();
        Lider lider;

        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql;
                
                switch(filtro){
                    
                    case "Todo":
                        sql = "SELECT * FROM lider WHERE id != -1 ORDER BY nombre, apellido ASC";
                        pst = conectar.prepareStatement(sql);
                    break;

                    case "Número de Documento":
                        sql = "SELECT * FROM lider WHERE id != -1 AND tipo_documento=? And numero_documento=?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));
                        pst.setString(2, data.get(1));
                    break;
                    
                    case "Nombre/Apellido":
                        sql = "SELECT * FROM lider WHERE id != -1 AND nombre LIKE ? OR apellido LIKE ? ORDER BY nombre, apellido ASC";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                        pst.setString(2, "%"+data.get(0)+"%");                        
                    break;                    
                    
                    case "Barrio":
                        sql = "SELECT * FROM lider WHERE id != -1 AND barrio LIKE ? ORDER BY nombre ASC";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                    break;                
                    
                    case "Edad":
                        sql = "SELECT * FROM lider WHERE id != -1 AND (YEAR(NOW()) - YEAR(fecha_nacimiento)) BETWEEN ? AND ? ORDER BY nombre, apellido ASC";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0)); 
                        pst.setString(2, data.get(1));                                                
                    break;

                    case "Sexo":
                        sql = "SELECT * FROM lider WHERE id != -1 AND sexo = ? ORDER BY nombre, apellido ASC";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));                        
                    break;
                    
                    case "id/value":
                        sql = "SELECT id, nombre, apellido FROM lider WHERE id != -1 ORDER BY nombre, apellido ASC";
                        pst= conectar.prepareStatement(sql);
                    break;
                    
                }
                                
                rs = pst.executeQuery();
                
                int count=1;                
                
                while(rs.next()){
                    
                    lider = new Lider();
                                            
                    if(!filtro.equals("id/value")){
                    
                        lider.setId(rs.getInt("id"));
                        lider.setTipoDocumento(rs.getString("tipo_documento"));
                        lider.setNumeroDocumento(rs.getInt("numero_documento"));    
                        lider.setNombre(rs.getString("nombre"));
                        lider.setApellido(rs.getString("apellido"));
                        lider.setSexo(rs.getString("sexo"));
                        lider.setFehaNacimiento(rs.getString("fecha_nacimiento"));
                        lider.setDireccion(rs.getString("direccion"));
                        lider.setBarrio(rs.getString("barrio"));
                        lider.setEstadoCivil(rs.getString("estado_civil"));
                        lider.setTelefono(rs.getString("telefono"));
                        lider.setCorreoElectronico(rs.getString("correo_electronico"));
                        lider.setIndice(count);                   
                    
                    }else{
                        lider.setId(rs.getInt("id"));
                        lider.setNombre(rs.getString("nombre"));
                        lider.setApellido(rs.getString("apellido"));
                    }
 
                    
                    list.add(lider);
                    count++;
                    
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

        
        return list;
    }
    
    public boolean modificarLider(Lider datosLider){
    
        boolean estado = false;
    
        try{
            
            conectar = conexion.conectar();
            
            if(conectar!=null){
                String sql = "UPDATE "
                            + "lider SET tipo_documento=?, numero_documento=?, nombre=?, apellido=?, sexo=?, fecha_nacimiento=?, direccion=?, barrio=?, estado_civil=?, telefono=?, correo_electronico=? "
                            + "WHERE id=?";
                
                pst = conectar.prepareStatement(sql);
                pst.setString(1, datosLider.getTipoDocumento());
                pst.setInt(2, datosLider.getNumeroDocumento());
                pst.setString(3, datosLider.getNombre());
                pst.setString(4, datosLider.getApellido());
                pst.setString(5, datosLider.getSexo());
                pst.setString(6, datosLider.getFehaNacimiento());
                pst.setString(7, datosLider.getDireccion());
                pst.setString(8, datosLider.getBarrio());
                pst.setString(9, datosLider.getEstadoCivil());
                pst.setString(10, datosLider.getTelefono());
                pst.setString(11, datosLider.getCorreoElectronico());
                pst.setInt(12, datosLider.getId());                
                
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
                conexion.cerrar(conectar);
            }catch(Exception ex){}
        }
        
        
        return estado;
    
    
    
    }
    
    public boolean eliminarLider(int id){
        
        boolean estado = false;
    
        try{
            
            conectar = conexion.conectar();
            
            if(conectar!=null){
                
                String sql = "DELETE FROM lider WHERE id=?";
                
                pst = conectar.prepareStatement(sql);
                pst.setInt(1, id);                
                
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
                conexion.cerrar(conectar);
            }catch(Exception ex){}
        }
        
        
        return estado;

    
    }
    
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
    
    /*LIDER DAO MODEL*/
    public boolean validateLider(){

        boolean condicion = false;

        try{

            conectar = conexion.conectar();
            if(conectar!=null){

                String sql = "SELECT id FROM lider WHERE id=-1";
                pst = conectar.prepareStatement(sql);
                rs = pst.executeQuery();

                if(rs.next()){
                    condicion = true;
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

    public boolean agregarLider(){

        boolean condicion=false;

        try{    
            conectar = conexion.conectar();            

            if(conectar!=null){

                String sql = "INSERT INTO lider(id, tipo_documento, numero_documento, nombre, apellido) VALUES (?, ?, ?, ?, ?)";

                pst = conectar.prepareStatement(sql);
                pst.setString(1, "-1");
                pst.setString(2, "default");
                pst.setString(3, "-1");
                pst.setString(4, "default");
                pst.setString(5, "default");

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

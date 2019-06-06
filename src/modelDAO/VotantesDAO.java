package modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Conexion;
import model.Usuario;
import model.Votantes;
import utlidades.ControladorGeneral;
import utlidades.ControladorValidaciones;

/**
 *
 * @author georgep24
 */
public class VotantesDAO {
    
    private final Conexion conexion;
    private Connection conectar;
    private PreparedStatement pst;
    private ResultSet rs;
    
    public VotantesDAO(){
        conexion=new Conexion();
    }
    
    public boolean agregarVotantes(Votantes datosVotante){
        
        boolean estado = false;
    
        try{
            
            conectar = conexion.conectar();
            
            if(conectar!=null){
            
                String sql = "INSERT INTO "
                            + "Votantes(tipo_documento, numero_documento, nombre, apellido, sexo, fecha_nacimiento, direccion, barrio, estado_civil, telefono, correo_electronico, lugar_de_votacion, mesa_de_votacion, direccion_votacion) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                pst = conectar.prepareStatement(sql);
                pst.setString(1, datosVotante.getTipoDocumento());
                pst.setInt(2, datosVotante.getNumeroDocumento());
                pst.setString(3, datosVotante.getNombre());
                pst.setString(4, datosVotante.getApellido());
                pst.setString(5, datosVotante.getSexo());
                pst.setString(6, datosVotante.getFehaNacimiento());
                pst.setString(7, datosVotante.getDireccion());
                pst.setString(8, datosVotante.getBarrio());
                pst.setString(9, datosVotante.getEstadoCivil());
                pst.setString(10, datosVotante.getTelefono());
                pst.setString(11, datosVotante.getCorreoElectronico());
                pst.setString(12, datosVotante.getLugar());
                pst.setString(13, datosVotante.getMesa());
                pst.setString(14, datosVotante.getDireccionLugar());
                
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
    
    public ArrayList<Votantes> consultarVotantes(){
        
        ArrayList list = new ArrayList();
        Votantes votante;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){
                
                String sql ="SELECT * FROM Votantes WHERE 1";
                
                pst = conectar.prepareStatement(sql);
                
                rs = pst.executeQuery();
                                
                while(rs.next()){
                    
                    votante = new Votantes();
                    
                    votante.setId(rs.getInt("id"));
                    votante.setTipoDocumento(rs.getString("tipo_documento"));
                    votante.setNumeroDocumento(rs.getInt("numero_documento"));    
                    votante.setNombre(rs.getString("nombre"));
                    votante.setApellido(rs.getString("apellido"));
                    votante.setSexo(rs.getString("sexo"));
                    votante.setFehaNacimiento(rs.getString("fecha_nacimiento"));
                    votante.setDireccion(rs.getString("direccion"));
                    votante.setBarrio(rs.getString("barrio"));
                    votante.setEstadoCivil(rs.getString("estado_civil"));
                    votante.setTelefono(rs.getString("telefono"));
                    votante.setCorreoElectronico(rs.getString("correo_electronico"));
                    votante.setLugar(rs.getString("lugar_de_votacion"));
                    votante.setMesa(rs.getString("mesa_de_votacion"));
                    votante.setDireccionLugar(rs.getString("direccion_votacion"));
                                        
                    list.add(votante);

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
    
    public ArrayList<Votantes> consultarVotantes(String tipoBusqueda, ArrayList<String> data){
        
        ArrayList list = new ArrayList();
        Votantes votante;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql;

                
                switch(tipoBusqueda){
                    case "Número de Documento":
                        sql = "SELECT * FROM Votantes WHERE tipo_documento=? And numero_documento=?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));
                        pst.setString(2, data.get(1));
                    break;

                    case "Nombre":
                        sql = "SELECT * FROM Votantes WHERE nombre LIKE ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                    break;
                    
                    case "Apellido":
                        sql = "SELECT * FROM Votantes WHERE apellido LIKE ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                    break;
                    
                    case "Nombre/Apellido":
                        sql = "SELECT * FROM Votantes WHERE nombre LIKE ? OR apellido LIKE ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                        pst.setString(2, "%"+data.get(0)+"%");                        
                    break;                    
                    
                    case "Barrio":
                        sql = "SELECT * FROM Votantes WHERE barrio LIKE ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                    break;                

                    case "Edad":
                        sql = "SELECT * FROM `Votantes` WHERE (YEAR(NOW()) - YEAR(fecha_nacimiento)) BETWEEN ? AND ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0)); 
                        pst.setString(2, data.get(1));                                                
                    break;

                    case "Sexo":
                        sql = "SELECT * FROM Votantes WHERE sexo = ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));                        
                    break;

                    case "Mesa De Votación":
                        sql = "SELECT * FROM Votantes WHERE mesa_de_votacion = ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));                        
                    break;

                     case "Lugar De Votación":
                        sql = "SELECT * FROM Votantes WHERE lugar_de_votacion = ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));                        
                    break;                   

                     case "ListCombo":
                        sql = "SELECT DISTINCT mesa_de_votacion, lugar_de_votacion FROM Votantes WHERE 1";
                        pst = conectar.prepareStatement(sql);
                    break;    
                    
                    default:
                        sql = "SELECT * FROM Votantes WHERE 1";
                        pst = conectar.prepareStatement(sql);
                    break;                                

                }
                
                                
                rs = pst.executeQuery();
                                
                while(rs.next()){
                    
                    votante = new Votantes();
                    
                    if(!tipoBusqueda.equals("ListCombo")){
                        
                        votante.setId(rs.getInt("id"));
                        votante.setTipoDocumento(rs.getString("tipo_documento"));
                        votante.setNumeroDocumento(rs.getInt("numero_documento"));    
                        votante.setNombre(rs.getString("nombre"));
                        votante.setApellido(rs.getString("apellido"));
                        votante.setSexo(rs.getString("sexo"));
                        votante.setFehaNacimiento(rs.getString("fecha_nacimiento"));
                        votante.setDireccion(rs.getString("direccion"));
                        votante.setBarrio(rs.getString("barrio"));
                        votante.setEstadoCivil(rs.getString("estado_civil"));
                        votante.setTelefono(rs.getString("telefono"));
                        votante.setCorreoElectronico(rs.getString("correo_electronico"));
                        votante.setLugar(rs.getString("lugar_de_votacion"));
                        votante.setMesa(rs.getString("mesa_de_votacion"));
                        votante.setDireccionLugar(rs.getString("direccion_votacion"));

                    }else{
                        votante.setLugar(rs.getString("lugar_de_votacion"));
                        votante.setMesa(rs.getString("mesa_de_votacion"));
                    }
                    
                    list.add(votante);

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

    public ArrayList<String> consultarEmails(){
        
        ArrayList<String> list=null;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "SELECT DISTINCT correo_electronico FROM Votantes WHERE 1 ";
 
                pst = conectar.prepareStatement(sql);
                rs = pst.executeQuery();
                
                list = new ArrayList<>();
                while(rs.next()){
                    
                    String preEmail=rs.getString("correo_electronico");
                    
                    if(ControladorValidaciones.isValidEmailAddress(preEmail)){
                        list.add(preEmail);                    
                    }
                    
                    
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

    
    
    public boolean modificarVotantes(Votantes datosVotante){
        
        boolean estado = false;
    
        try{
            
            conectar = conexion.conectar();
            
            if(conectar!=null){
                String sql = "UPDATE "
                            + "Votantes SET tipo_documento=?, numero_documento=?, nombre=?, apellido=?, sexo=?, fecha_nacimiento=?, direccion=?, barrio=?, estado_civil=?, telefono=?, correo_electronico=?, lugar_de_votacion=?, mesa_de_votacion=?, direccion_votacion=? "
                            + "WHERE id=?";
                
                pst = conectar.prepareStatement(sql);
                pst.setString(1, datosVotante.getTipoDocumento());
                pst.setInt(2, datosVotante.getNumeroDocumento());
                pst.setString(3, datosVotante.getNombre());
                pst.setString(4, datosVotante.getApellido());
                pst.setString(5, datosVotante.getSexo());
                pst.setString(6, datosVotante.getFehaNacimiento());
                pst.setString(7, datosVotante.getDireccion());
                pst.setString(8, datosVotante.getBarrio());
                pst.setString(9, datosVotante.getEstadoCivil());
                pst.setString(10, datosVotante.getTelefono());
                pst.setString(11, datosVotante.getCorreoElectronico());
                pst.setString(12, datosVotante.getLugar());
                pst.setString(13, datosVotante.getMesa());
                pst.setString(14, datosVotante.getDireccionLugar());
                pst.setInt(15, datosVotante.getId());                
                
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
    
    public boolean eliminarVotantes(int id){
        
        boolean estado = false;
    
        try{
            
            conectar = conexion.conectar();
            
            if(conectar!=null){
                String sql = "DELETE FROM Votantes WHERE id=?";
                
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
    
    
}

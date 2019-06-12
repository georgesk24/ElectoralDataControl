package modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Conexion;
import model.Votantes;
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
                            + "votantes(id_lider, tipo_documento, numero_documento, nombre, apellido, sexo, fecha_nacimiento, direccion, barrio, estado_civil, telefono, correo_electronico, lugar_de_votacion, mesa_de_votacion, direccion_votacion) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                pst = conectar.prepareStatement(sql);
            
                pst.setInt(1, datosVotante.getLider().getId());
                pst.setString(2, datosVotante.getTipoDocumento());
                pst.setInt(3, datosVotante.getNumeroDocumento());
                pst.setString(4, datosVotante.getNombre());
                pst.setString(5, datosVotante.getApellido());
                pst.setString(6, datosVotante.getSexo());
                pst.setString(7, datosVotante.getFehaNacimiento());
                pst.setString(8, datosVotante.getDireccion());
                pst.setString(9, datosVotante.getBarrio());
                pst.setString(10, datosVotante.getEstadoCivil());
                pst.setString(11, datosVotante.getTelefono());
                pst.setString(12, datosVotante.getCorreoElectronico());
                pst.setString(13, datosVotante.getLugar());
                pst.setString(14, datosVotante.getMesa());
                pst.setString(15, datosVotante.getDireccionLugar());
                
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
                
                String sql= "SELECT votantes.id, votantes.tipo_documento, votantes.numero_documento, "
                           + "votantes.nombre, votantes.apellido, votantes.sexo, votantes.direccion, "
                           + "votantes.fecha_nacimiento, votantes.barrio, votantes.estado_civil, "
                           + "votantes.telefono, votantes.correo_electronico, "
                           + "votantes.lugar_de_votacion, votantes.mesa_de_votacion, "
                           + "votantes.direccion_votacion, lider.id AS idLider, lider.nombre AS nombreLider, lider.apellido AS apellidoLider "
                           + "FROM votantes "
                           + "INNER JOIN lider ON(votantes.id_lider=lider.id) "
                           + "WHERE 1 ORDER BY votantes.nombre, votantes.apellido ASC";                       
                
                pst = conectar.prepareStatement(sql);
                
                rs = pst.executeQuery();
                
                int count=1;
                
                while(rs.next()){
                    
                    votante = new Votantes();
                    
                    votante.setIndice(count);
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
                    votante.getLider().setId(rs.getInt("idLider"));
                    votante.getLider().setNombre(rs.getString("nombrelider"));
                    votante.getLider().setApellido(rs.getString("apellidoLider"));
                    
                    list.add(votante);
                    
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
        System.out.println(ControladorValidaciones.EXCEPCIONES);
        
        return list;
        
    }
    
    public ArrayList<Votantes> consultarVotantes(String tipoBusqueda, ArrayList<String> data){
        
        ArrayList list = new ArrayList();
        Votantes votante;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql= "SELECT votantes.id, votantes.tipo_documento, votantes.numero_documento, "
                           + "votantes.nombre, votantes.apellido, votantes.sexo, votantes.direccion, "
                           + "votantes.fecha_nacimiento, votantes.barrio, votantes.estado_civil, "
                           + "votantes.telefono, votantes.correo_electronico, "
                           + "votantes.lugar_de_votacion, votantes.mesa_de_votacion, "
                           + "votantes.direccion_votacion, lider.id AS idLider, lider.nombre AS nombreLider, lider.apellido AS apellidoLider "
                           + "FROM votantes "
                           + "INNER JOIN lider ON(votantes.id_lider=lider.id) ";

                
                switch(tipoBusqueda){
                    case "Número de Documento":

                        sql= sql+ "WHERE votantes.tipo_documento=? And votantes.numero_documento=?";                       
                        
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));
                        pst.setString(2, data.get(1));
                    break;
                    
                    case "Nombre/Apellido":

                        sql=  sql + "WHERE votantes.nombre LIKE ? OR votantes.apellido LIKE ? ORDER BY votantes.nombre, votantes.apellido ASC";                       

                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                        pst.setString(2, "%"+data.get(0)+"%");                        
                    break;                    
                    
                    case "Barrio":

                        sql= sql + "WHERE votantes.barrio LIKE ?";                       
                            
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                    break;                

                    case "Edad":

                        sql= sql + "WHERE (YEAR(NOW()) - YEAR(votantes.fecha_nacimiento)) BETWEEN ? AND ? ORDER BY votantes.nombre, votantes.apellido ASC";                       
    
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0)); 
                        pst.setString(2, data.get(1));                                                
                    break;

                    case "Sexo":

                        sql= sql + "WHERE votantes.sexo = ? ORDER BY votantes.nombre, votantes.apellido ASC";                        
                        
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));                        
                        
                    break;

                     case "Lugar De Votación":
                        if(data.get(1).equals("Todo")){

                            sql= sql + "WHERE votantes.lugar_de_votacion = ? ORDER BY votantes.nombre, votantes.apellido ASC";                            

                                pst = conectar.prepareStatement(sql);
                                pst.setString(1, data.get(0));                        

                        }else{

                            sql= sql + "WHERE votantes.lugar_de_votacion = ? AND votantes.mesa_de_votacion=? ORDER BY votantes.nombre, votantes.apellido ASC";
                            
                            pst = conectar.prepareStatement(sql);
                            pst.setString(1, data.get(0));       
                            pst.setString(2, data.get(1));                                                                            
                        }
                    break;                   

                     case "ListCombo":
                        sql = "SELECT DISTINCT mesa_de_votacion, lugar_de_votacion FROM votantes WHERE 1 ORDER BY votantes.lugar_de_votacion ASC";
                        pst = conectar.prepareStatement(sql);
                    break;    
                    
                    case "listLugar":                         
                        sql = "SELECT DISTINCT lugar_de_votacion FROM votantes ORDER BY votantes.lugar_de_votacion";
                        pst = conectar.prepareStatement(sql);
                    break;
                    
                    case "Lider":

                        sql= sql + "WHERE lider.id=? ORDER BY votantes.nombre, votantes.apellido ASC";

                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));
                        
                    break;
                    
                    default:
                        pst = conectar.prepareStatement(sql);
                    break;                                

                }
                
                                
                rs = pst.executeQuery();
                
                int count=1;                
                
                while(rs.next()){
                    
                    votante = new Votantes();
                    
                    if(!tipoBusqueda.equals("ListCombo") && !tipoBusqueda.equals("listLugar")){
                        
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
                        votante.getLider().setId(rs.getInt("idLider"));
                        votante.getLider().setNombre(rs.getString("nombrelider"));
                        votante.getLider().setApellido(rs.getString("apellidoLider"));

                    }else{
                        votante.setLugar(rs.getString("lugar_de_votacion"));
                    }
                    
                    votante.setIndice(count);
                    
                    list.add(votante);
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

    public ArrayList<String> consultarEmails(){
        
        ArrayList<String> list=null;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql = "SELECT DISTINCT correo_electronico FROM votantes WHERE 1 ";
 
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
                            + "votantes SET id_lider=?, tipo_documento=?, numero_documento=?, nombre=?, apellido=?, sexo=?, fecha_nacimiento=?, direccion=?, barrio=?, estado_civil=?, telefono=?, correo_electronico=?, lugar_de_votacion=?, mesa_de_votacion=?, direccion_votacion=? "
                            + "WHERE id=?";
                
                pst = conectar.prepareStatement(sql);
                pst.setInt(1, datosVotante.getLider().getId());
                pst.setString(2, datosVotante.getTipoDocumento());
                pst.setInt(3, datosVotante.getNumeroDocumento());
                pst.setString(4, datosVotante.getNombre());
                pst.setString(5, datosVotante.getApellido());
                pst.setString(6, datosVotante.getSexo());
                pst.setString(7, datosVotante.getFehaNacimiento());
                pst.setString(8, datosVotante.getDireccion());
                pst.setString(9, datosVotante.getBarrio());
                pst.setString(10, datosVotante.getEstadoCivil());
                pst.setString(11, datosVotante.getTelefono());
                pst.setString(12, datosVotante.getCorreoElectronico());
                pst.setString(13, datosVotante.getLugar());
                pst.setString(14, datosVotante.getMesa());
                pst.setString(15, datosVotante.getDireccionLugar());
                pst.setInt(16, datosVotante.getId());                
                
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
                String sql = "DELETE FROM votantes WHERE id=?";
                
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
    
    public boolean validarNumeroIdentificacion(String tipo_documento, String numero_documento){
        
        boolean res = false;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){
                
                String sql ="SELECT tipo_documento, numero_documento FROM votantes WHERE tipo_documento=? AND numero_documento=?";
                
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

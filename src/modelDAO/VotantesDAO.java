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
                            + "votantes(tipo_documento, numero_documento, nombre, apellido, sexo, fecha_nacimiento, direccion, barrio, estado_civil, telefono, correo_electronico, lugar_de_votacion, mesa_de_votacion, direccion_votacion) "
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
                
                String sql ="SELECT * FROM votantes WHERE 1";
                
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
    
    public ArrayList<Votantes> consultarVotantes(String tipoBusqueda, ArrayList<String> data){
        
        ArrayList list = new ArrayList();
        Votantes votante;
        
        try{

            conectar = conexion.conectar();
            
            if(conectar!=null){

                String sql;

                
                switch(tipoBusqueda){
                    case "Número de Documento":
                        sql = "SELECT * FROM votantes WHERE tipo_documento=? And numero_documento=?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));
                        pst.setString(2, data.get(1));
                    break;

                    case "Nombre":
                        sql = "SELECT * FROM votantes WHERE nombre LIKE ? AND apellido=?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                        pst.setString(2, "%"+data.get(0)+"%");
                    break;
                    
                    case "Apellido":
                        sql = "SELECT * FROM votantes WHERE apellido LIKE ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                    break;
                    
                    case "Nombre/Apellido":
                        sql = "SELECT * FROM votantes WHERE nombre LIKE ? OR apellido LIKE ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                        pst.setString(2, "%"+data.get(0)+"%");                        
                    break;                    
                    
                    case "Barrio":
                        sql = "SELECT * FROM votantes WHERE barrio LIKE ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+data.get(0)+"%");
                    break;                

                    case "Edad":
                        sql = "SELECT * FROM votantes WHERE (YEAR(NOW()) - YEAR(fecha_nacimiento)) BETWEEN ? AND ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0)); 
                        pst.setString(2, data.get(1));                                                
                    break;

                    case "Sexo":
                        sql = "SELECT * FROM votantes WHERE sexo = ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));                        
                    break;

                    case "Mesa De Votación":
                        sql = "SELECT * FROM votantes WHERE mesa_de_votacion = ?";
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, data.get(0));                        
                    break;

                     case "Lugar De Votación":
                        if(data.get(1).equals("Todo")){
                            sql = "SELECT * FROM votantes WHERE lugar_de_votacion = ?";                        
                            pst = conectar.prepareStatement(sql);
                            pst.setString(1, data.get(0));                        
                        }else{
                            sql = "SELECT * FROM votantes WHERE lugar_de_votacion = ? AND mesa_de_votacion=?";                        
                            pst = conectar.prepareStatement(sql);
                            pst.setString(1, data.get(0));       
                            pst.setString(2, data.get(1));                                                                            
                        }
                    break;                   

                     case "ListCombo":
                        sql = "SELECT DISTINCT mesa_de_votacion, lugar_de_votacion FROM votantes WHERE 1";
                        pst = conectar.prepareStatement(sql);
                    break;    
                    
                    case "listLugar":                         
                        sql = "SELECT DISTINCT lugar_de_votacion FROM votantes ORDER BY votantes.lugar_de_votacion";
                        pst = conectar.prepareStatement(sql);
                    break;
                    
                    
                    
                    default:
                        sql = "SELECT * FROM votantes WHERE 1";
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
                            + "votantes SET tipo_documento=?, numero_documento=?, nombre=?, apellido=?, sexo=?, fecha_nacimiento=?, direccion=?, barrio=?, estado_civil=?, telefono=?, correo_electronico=?, lugar_de_votacion=?, mesa_de_votacion=?, direccion_votacion=? "
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

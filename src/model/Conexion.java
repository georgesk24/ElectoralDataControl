package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author SOFTWARE GALAXIA
 */
public class Conexion {

    private final String DB="electoraldb";
    private final String URL="jdbc:mysql://localhost:3307/"+DB+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String USER="root";
    private final String PASS="";    
    
    public Connection conectar(){
        
        Connection conectar=null;
        
        try {  
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar =DriverManager.getConnection(URL, USER , PASS);  
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }

        return conectar;
    }    

    public void cerrar(Connection con){
        
        try {  
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    
    
}

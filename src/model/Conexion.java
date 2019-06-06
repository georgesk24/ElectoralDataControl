package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;


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
        
        Connection connect = null;
        
        try{

            BasicDataSource basicDataSource = new BasicDataSource();

            basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            basicDataSource.setUsername(USER);
            basicDataSource.setPassword(PASS);
            basicDataSource.setUrl(URL);
            basicDataSource.setMaxTotal(50);
            
            connect = basicDataSource.getConnection();
            
        }catch(SQLException ex){
            System.out.println(ex.getMessage());        
        }
        
        return connect;
        
    }    



    public void cerrar(Connection con){
        
        try {  
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    
    
}

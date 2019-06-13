
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author root
 */
public class test_connection {
 
    private final String DB="electoraldb";
    private final String URL="jdbc:mysql://localhost:3307/"+DB+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String USER="prueba";
    private final String PASS="*7LIBERTADYORDEN7*";    
    
    public DataSource initializeDataSource(){

        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUsername(USER);
        basicDataSource.setPassword(PASS);
        basicDataSource.setUrl(URL);
        basicDataSource.setMaxTotal(50);
                
        return basicDataSource;

    }    

    
    
    
    public test_connection(){}
    
    public static void main(String [] args){
        
        try{

            test_connection con = new test_connection();
            Connection conect =  con.initializeDataSource().getConnection();
            
            if(conect!=null){
                System.out.println("Conectado");
            }else{
                System.out.println("No Conectado");
            }
            
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }finally{
        
        }
        
        
    }
    
    
}

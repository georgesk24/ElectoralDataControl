import java.io.File; 
import java.io.FileOutputStream;
import java.io.IOException; 
import java.io.PrintWriter; 
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger; 
import javafx.application.Application; 
import static javafx.application.Application.launch; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.layout.VBox; 
import javafx.scene.text.Font; 
import javafx.scene.text.Text; 
import javafx.stage.FileChooser; 
import javafx.stage.Stage; 

public class SaveFileWithFileChooser extends Application { 
    
    @Override 
    public void start(final Stage primaryStage) {
        
        final String sampleText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut \n" 
                                + "labore et dolore magna aliqua.\n" + 
                                   "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" 
                                + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n" 
                                + "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."; 
        
        Text sample = new Text(sampleText); 
        sample.setFont(new Font(14)); 
        Button btnSave = new Button("Save");
        
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ArrayList<String> extensiones = new ArrayList<>();
                extensiones.add("*.xlsx");
                            
                FileChooser fileChooser = new FileChooser(); //Set extension
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Solo archivos EXCEL", extensiones);                
                fileChooser.getExtensionFilters().add(extFilter); //Show save file dialog             

                
                
                
                File file = fileChooser.showSaveDialog(null); 
                            
                if (file != null) { 
                   System.out.println(file.getAbsolutePath());
                   saveTextToFile(sampleText, file); 
                } 
            
            }
        });
        
        VBox vBox = new VBox(sample, btnSave); 
        vBox.setAlignment(Pos.CENTER); 
        primaryStage.setScene(new Scene(vBox, 800, 300)); 
        primaryStage.setTitle("www.genuinecoder.com"); 
        primaryStage.show();         
        
    }

    private void saveTextToFile(String content, File file) { 

        try { 
            
            String homeUsuario = System.getProperty("user.home"); 
            File directorio=new File(homeUsuario+"/Reportes-Electoral-Data-Control/Reporte_1546744017.xlsx");
            
            
            PrintWriter writer; 
            writer = new PrintWriter(file); 
            writer.println(content); 
            writer.close(); 
        } catch (IOException ex) {
            Logger.getLogger(SaveFileWithFileChooser.class.getName()).log(Level.SEVERE, null, ex); 
        } 
    }    
    
    
    public static void main(String[] args) { 
        launch(args); 
    } 

}
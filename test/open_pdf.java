

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class open_pdf {
    
    public static void main(String [] args){
        
        



            SwingController control = new SwingController();
            SwingViewBuilder factry = new SwingViewBuilder(control);
            JPanel panel = factry.buildViewerPanel();
            ComponentKeyBinding.install(control, panel);
            control.getDocumentViewController().setAnnotationCallback(
                    new org.icepdf.ri.common.MyAnnotationCallback(
                    control.getDocumentViewController()));
            
            control.openDocument("src/resources/pdf-files/secciones-de-software.pdf");

            JFrame frame = new JFrame();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setTitle("PDF VIEWER");
            frame.add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
            System.out.println("hola mundo");
        
    }
    
    
}

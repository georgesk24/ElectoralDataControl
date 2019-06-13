

package utlidades;


import modelDAO.LiderDAO;

import modelDAO.UsuarioDAO;


/**
 *
 * @author root
 */

public class ConfiguracionInicial {
    
    public boolean validarUsuario(){

        boolean condicion;
        
        UsuarioDAO modelDAO = new UsuarioDAO();

        int validate = modelDAO.validateUsuario();

        if(validate<=0){
            condicion = modelDAO.agregarUsuario();            
        }else{
            condicion=true;
        }
        
        System.out.println(ControladorValidaciones.EXCEPCIONES);
        return condicion;
    }

    public boolean validarLider(){

        boolean condicionFinal;
        LiderDAO liderDAO = new LiderDAO();
        boolean condicion = liderDAO.validateLider();
        
        if(condicion!=true && ControladorValidaciones.EXCEPCIONES.equals("")){
            condicionFinal = liderDAO.agregarLider();
        }else{
            condicionFinal = true;
        }

        return condicionFinal;
    }

}






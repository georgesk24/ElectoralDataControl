package model;

/**
 *
 * @author jorge
 */
public class Votantes extends Persona{
    
    private String lugar;
    private String direccionLugar; 
    private String mesa;
    private Lider lider;
    
    public Votantes(){
        lider = new Lider();
    }
    
    
    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDireccionLugar() {
        return direccionLugar;
    }

    public void setDireccionLugar(String Direccion) {
        this.direccionLugar = Direccion;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public Lider getLider() {
        return lider;
    }

    public void setLider(Lider lider) {
        this.lider = lider;
    }

    
    
    
    
}

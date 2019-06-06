/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author jorge
 */
public class Votantes extends Persona{
    
    private String lugar;
    private String direccionLugar; 
    private String mesa;

    
    
    
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
    
    
    
    
    
}

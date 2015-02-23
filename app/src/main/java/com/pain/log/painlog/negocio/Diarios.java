package com.pain.log.painlog.negocio;

/**
 * Created by raul.rodriguezconcep on 23/02/15.
 */
public class Diarios {

    private int clave;
    private String nombre;


    public Diarios() {

    }


    public Diarios(int clave, String nombre) {
        this.nombre = nombre;
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }
}

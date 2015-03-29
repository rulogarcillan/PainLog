package com.pain.log.painlog.negocio;

/**
 * Created by raul.rodriguezconcep on 27/03/15.
 */
public class DriveFiles {

    String id;
    String nombre;
    String nombreMuestra;

    public DriveFiles(String id, String nombre, String nombreMuestra) {
        this.id = id;
        this.nombre = nombre;
        this.nombreMuestra = nombreMuestra;
    }

    public DriveFiles() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreMuestra() {
        return nombreMuestra;
    }

    public void setNombreMuestra(String nombreMuestra) {
        this.nombreMuestra = nombreMuestra;
    }
}

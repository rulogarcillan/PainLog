package com.pain.log.painlog.negocio;

import java.util.Date;

/**
 * Created by Rulo on 28/02/2015.
 */
public class Logs {

    private int clave;
    private Date fecha;
    private int intensidad;
    private String notas;
    private int clave_d;

    public Logs() {
    }

    public Logs(int clave, Date fecha, int intensidad, String notas, int clave_d) {
        this.clave = clave;
        this.fecha = fecha;
        this.intensidad = intensidad;
        this.notas = notas;
        this.clave_d = clave_d;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(int intensidad) {
        this.intensidad = intensidad;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public int getClave_d() {
        return clave_d;
    }

    public void setClave_d(int clave_d) {
        this.clave_d = clave_d;
    }
}

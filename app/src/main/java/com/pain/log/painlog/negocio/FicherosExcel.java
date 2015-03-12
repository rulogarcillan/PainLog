package com.pain.log.painlog.negocio;

public class FicherosExcel {

	private String nombre;
    private String fecha;
	private String tamaño;
    public static final int TYPE_LARGE= 0;
    public static final int TYPE_SHORT = 1;

    public FicherosExcel(String nombre, String fecha, String tamaño) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.tamaño = tamaño;
    }

    public FicherosExcel() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }
}
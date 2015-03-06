package com.pain.log.painlog.negocio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rulo on 28/02/2015.
 */
public class Logs {

    private int clave;
    private String fecha;
    private int intensidad;
    private String notas;
    private int clave_d;
    private Date date;

    public Logs() {
    }

    public Logs(int clave, String fecha, int intensidad, String notas, int clave_d) {
        this.clave = clave;
        this.fecha = fecha;
        this.intensidad = intensidad;
        this.notas = notas;
        this.clave_d = clave_d;
        this.date = parseFecha(fecha);
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
        this.date = parseFecha(fecha);
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

    public Date getDate() {
        return date;
    }

    private Date parseFecha (String date){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateD = null;

        try {

            dateD = formatter.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateD);
        return dateD;
    }


}

package com.pain.log.painlog.negocio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by raul.rodriguezconcep on 23/02/15.
 */
public class Diarios {

    private int clave;
    private String nombre;
    private String ultPainDate;

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
    private Calendar c = Calendar.getInstance();

    public Diarios() {

    }

    public Diarios(int clave, String nombre, String ultPainDate) {
        this.nombre = nombre;
        this.clave = clave;

        // si es null = sysdate
        if (ultPainDate == null) {

            this.ultPainDate = dateFormat.format(c.getTime()).toString();

        } else {

            try {

                this.ultPainDate = dateFormat.format(dateFormat2.parse(ultPainDate)).toString();


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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

    public String getUltPainDate() {
        return ultPainDate;
    }

    public void setUltPainDate(String ultPainDate) {
        this.ultPainDate = ultPainDate;
    }


    public int fechasDiferenciaEnDias() {

        Date fechaInicial = null, fechaFinal = null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {

            fechaInicial = formatter.parse(this.ultPainDate);
            fechaFinal = formatter.parse(dateFormat.format(c.getTime()).toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicial);
        cal.setTime(fechaFinal);

        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

}

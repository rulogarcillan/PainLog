package com.pain.log.painlog.negocio;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.pain.log.painlog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ***************************
 * Escrito por Rulogarcillan
 * <p/>
 * Resultados
 */


public class MoonCalculation {

    private static final int MINYEAR = 2000;
    private static final int MAXYEAR = 2019;
    private static final int PERIOD = 19;


    public String phaseName(Context mContext, int codPhase) {

        String textPhase = "";



        if (codPhase == 29 || codPhase == 0 || codPhase == 1) {

            textPhase = mContext.getResources().getString(R.string.luna0);
        } else if (codPhase >= 2 && codPhase <= 4) {

            textPhase = mContext.getResources().getString(R.string.luna1);

        } else if (codPhase >= 5 && codPhase <= 8) {

            textPhase = mContext.getResources().getString(R.string.luna2);

        } else if (codPhase >= 9 && codPhase <= 12) {

            textPhase = mContext.getResources().getString(R.string.luna3);
        } else if (codPhase >= 13 && codPhase <= 15) {

            textPhase = mContext.getResources().getString(R.string.luna4);

        } else if (codPhase >= 16 && codPhase <= 20) {

            textPhase = mContext.getResources().getString(R.string.luna5);

        } else if (codPhase >= 21 && codPhase <= 24) {
            textPhase = mContext.getResources().getString(R.string.luna6);

        } else if (codPhase >= 25 && codPhase <= 28) {

            textPhase = mContext.getResources().getString(R.string.luna7);

        }

        return textPhase;
    }


    public Drawable phaseImage(Context mContext, int codPhase) {

        Drawable drawPhase = null;


        if (codPhase == 29 || codPhase == 0 || codPhase == 1) {

            drawPhase = mContext.getResources().getDrawable(R.drawable.luna0);
        } else if (codPhase >= 2 && codPhase <= 4) {

            drawPhase = mContext.getResources().getDrawable(R.drawable.luna1);

        } else if (codPhase >= 5 && codPhase <= 8) {

            drawPhase = mContext.getResources().getDrawable(R.drawable.luna2);

        } else if (codPhase >= 9 && codPhase <= 12) {

            drawPhase = mContext.getResources().getDrawable(R.drawable.luna3);
        } else if (codPhase >= 13 && codPhase <= 15) {

            drawPhase = mContext.getResources().getDrawable(R.drawable.luna4);

        } else if (codPhase >= 16 && codPhase <= 20) {

            drawPhase = mContext.getResources().getDrawable(R.drawable.luna5);

        } else if (codPhase >= 21 && codPhase <= 24) {

            drawPhase = mContext.getResources().getDrawable(R.drawable.luna6);

        } else if (codPhase >= 25 && codPhase <= 28) {

            drawPhase = mContext.getResources().getDrawable(R.drawable.luna7);

        }

        return drawPhase;
    }


    public int moonPhase(String date) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateD = null;

        try {

            dateD = formatter.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateD);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;


        return moonPhase(day, month, year);
    }


    /*Este método calcula la fase lunar entre los años 2000 y 2019
         como la fase lunar se repite cada 19 años solo hay que parsear
         la fecha para coger su equivalente*/
    public int moonPhase(int day, int month, int year) {


        int anioParse;
        int codPhase = -1; // = null


        //parseamos el año para buscar el equivalente entre el 2000 y 2019
        anioParse = parseAnio(1);

        // sumamos primera cifra + dos ultimas cifras ej: 2015 = 2 + 15 resultado 17 ; 2009 = 2 + 09 resultado 11
        codPhase = 2 + Integer.parseInt(Integer.toString(year).substring(2));

        //multiplicamos el resultado * 11
        codPhase = codPhase * 11;

        //le sumamos el mes y el dia
        codPhase = codPhase + day + month;

        //Al resultado se le resta 30 tantas veces como se pueda sin llegar a un numero negativo

        while (codPhase >= 30) {
            codPhase -= 30;
        }

        return codPhase; // ya tenemos nuestro código 0 lluna nueva

    }

    private int parseAnio(int year) {

        int anioParse;

        if (year > MAXYEAR) {

            int valor = (int) Math.ceil((double) (Math.abs(year - MAXYEAR)) / PERIOD) * PERIOD;
            anioParse = year - valor;


        } else if (year < MINYEAR) {

            int valor = (int) Math.ceil((double) (Math.abs(MINYEAR - year)) / PERIOD) * PERIOD;
            anioParse = year + valor;

        } else {

            anioParse = year;
        }

        return anioParse;
    }


    public String getNamePhase(int codPhase) {

        String NamePhase[] = {"Luna nueva",
                " Creciente",
                " Cuarto creciente",
                " Gibosa creciente",
                " Luna llena",
                " Gibosa menguante",
                " Cuarto menguante",
                " Menguante"};

        String name = "Error";

        if (codPhase == 29 || codPhase == 0 || codPhase == 1) {

            name = NamePhase[0];
        } else if (codPhase >= 2 && codPhase <= 4) {

            name = NamePhase[1];

        } else if (codPhase >= 5 && codPhase <= 8) {

            name = NamePhase[2];

        } else if (codPhase >= 9 && codPhase <= 12) {

            name = NamePhase[3];

        } else if (codPhase >= 13 && codPhase <= 15) {

            name = NamePhase[4];

        } else if (codPhase >= 16 && codPhase <= 20) {

            name = NamePhase[5];

        } else if (codPhase >= 21 && codPhase <= 24) {

            name = NamePhase[6];

        } else if (codPhase >= 25 && codPhase <= 28) {

            name = NamePhase[7];

        }


        return name;
    }
}

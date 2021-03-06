package com.pain.log.painlog.export;

import android.app.Activity;

import com.pain.log.painlog.R;
import com.pain.log.painlog.negocio.Logs;
import com.pain.log.painlog.negocio.MoonCalculation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



/**
 * Created by raul.rodriguezconcep on 2/03/15.
 */
public class exportLog {

    Activity activity;
    MoonCalculation luna = new MoonCalculation();

    public exportLog(Activity activity) {
        this.activity = activity;
    }

    public boolean exportToExcel(ArrayList<Logs> items, String name) {

        final String fileName = Ficheros.generaNombre(name);

        //Manejo de ficheros


        File directory = new File(Ficheros.path);

        Ficheros.CreaRuta();

        File file = new File(directory, fileName);


        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet(name, 0);

            try {
                int i = 0;
                sheet.addCell(new Label(0, i, activity.getResources().getString(R.string.col1excel)));
                sheet.addCell(new Label(1, i, activity.getResources().getString(R.string.col1exce2)));
                sheet.addCell(new Label(2, i, activity.getResources().getString(R.string.col1exce3)));
                sheet.addCell(new Label(3, i, activity.getResources().getString(R.string.col1exce4)));


                for (Logs item : items) {

                    i++;

                    String fecha = item.getFecha().toString();
                    String intensidad = getNombreIntensidad(item.getIntensidad());
                    String notas = item.getNotas();
                    int fase = luna.moonPhase(item.getFecha());
                    String lunaNombre = luna.phaseName(activity, fase);

                    sheet.addCell(new Label(0, i, fecha));
                    sheet.addCell(new Label(1, i, intensidad));
                    sheet.addCell(new Label(2, i, notas));
                    sheet.addCell(new Label(3, i, lunaNombre));

                }

            } catch (RowsExceededException e) {
                e.printStackTrace();
                return false;
            } catch (WriteException e) {
                e.printStackTrace();
                return false;
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
                return false;
            }



        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private String getNombreIntensidad(int progress) {

        String nombreDolor = "";

        if (progress <= 32) {
            nombreDolor = activity.getResources().getString(R.string.dolor1);
        } else if (progress >= 33 && progress <= 65) {
            nombreDolor = activity.getResources().getString(R.string.dolor2);
        } else if (progress >= 66) {
            nombreDolor = activity.getResources().getString(R.string.dolor3);
        }

        return nombreDolor;
    }





}

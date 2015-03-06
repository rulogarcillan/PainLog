package com.pain.log.painlog.export;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

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

    public void exportToExcel(ArrayList<Logs> items, String name, Boolean send) {

        final String fileName = remove(name) + ".xls";

        //Manejo de ficheros
        String lect = "/PainLog/" + fileName;
        File sdCard = Environment.getExternalStorageDirectory();
        String ruta = sdCard.getAbsolutePath() + "/PainLog";
        File directory = new File(ruta);

        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

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
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }

            Toast.makeText(activity, lect, Toast.LENGTH_LONG).show();

            if (send == true) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                shareIntent.setType("application/excel");
                activity.startActivity(Intent.createChooser(shareIntent, activity.getText(R.string.exportSendTittle)));

            }


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "error", Toast.LENGTH_LONG).show();
        }

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


    public static String remove(String input) {

        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ.\\/:?*\"<>|";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC----------";
        String output = input;
        for (int i = 0; i < original.length(); i++) {

            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }


}

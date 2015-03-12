package com.pain.log.painlog.Constantes;

import android.os.Environment;

import com.pain.log.painlog.negocio.FicherosExcel;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.pain.log.painlog.negocio.LogUtils.LOGI;

public final class Ficheros {

	public static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PainLog/";

	public static String generaNombre(String titu) {

		String name = parseChar(titu) + ".xls";
        return name;
	}


	/**
	 * Devuelve un string concatenada la fecha y hora -->>> _ddMMyyyy_hhmmss
	 */
	public static String HoraSistema() {

		//
		Date date = new Date();
		DateFormat dfd = new SimpleDateFormat("ddMMyyyy");
		DateFormat dfh = new SimpleDateFormat("HHmmss");

		String fecha = dfd.format(date) + "_" + dfh.format(date);

		return fecha;

	}

	/**
	 * Realiza la carga del arrayItems (recoge todos los datos necesario de los
	 * ficheros)
	 */
	public static ArrayList cargaItems() {

		File archivos[];
		File carpeta;

        ArrayList<FicherosExcel> arrayItems = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		// arrayItems.clear();
		carpeta = new File(path);
		if (carpeta.exists()) {
			archivos = carpeta.listFiles();

			for (int i = archivos.length-1 ; i >= 0; i--) {


				String name = archivos[i].getName();
				java.util.Date myDate = new java.util.Date(archivos[i].lastModified());
				String tam = Long.toString(archivos[i].length() / 1024) + " KB";

                FicherosExcel item = new FicherosExcel(name, dateFormat.format(myDate), tam);
				arrayItems.add(item);
			}
		}
		return arrayItems;

	}

	public static void removeItem(String nombre) {
		File archivo;
		archivo = new File(path + "/" + nombre);
		if (archivo.exists())
			archivo.delete();

	}


	public static void CreaRuta() {

		File file = new File(path);

        if (!file.isDirectory()) {
            file.mkdirs();
        }


    }

    public static String parseChar(String input) {

        String original = " áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ.\\/:?*\"<>|";
        String ascii = "_aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC----------";
        String output = input;
        for (int i = 0; i < original.length(); i++) {

            output = output.replace(original.charAt(i), ascii.charAt(i)).toLowerCase().trim();
        }
        return output;
    }
}
package com.pain.log.painlog.negocio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.pain.log.painlog.Constantes.PreferencesCons;
import com.pain.log.painlog.R;
import com.pain.log.painlog.export.BackUp;
import com.pain.log.painlog.export.Ficheros;

import java.io.File;

import src.chooser.ChooseFolder;

public class PreferenceFrag extends android.preference.PreferenceFragment {

    private ChooseFolder chos;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        android.preference.Preference pref1 = findPreference("opcion1");
        android.preference.Preference pref2 = findPreference("opcion2");
        android.preference.Preference pref3 = findPreference("opcion3");

        pref1.setSummary(Ficheros.path.replace(Ficheros.root, "/sdcard"));
        pref2.setSummary(BackUp.path.replace(BackUp.root, "/sdcard"));

        pref3.setSummary(Html.fromHtml( "<font color='red'>" +  getResources().getString(R.string.prefcautionRed) +"</font>" + " " + getResources().getString(R.string.prefcaution)));

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, final android.preference.Preference preference) {

        switch (preference.getKey()) {
            case "opcion1":


                final View view = getActivity().getLayoutInflater().inflate(R.layout.chooser, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                chos = (ChooseFolder) view.findViewById(R.id.chooserview);
                chos.setPath(Ficheros.path);

                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {


                        Ficheros.path = chos.getPath();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(PreferencesCons.RUTAFILE, Ficheros.path);
                        editor.commit();

                        preference.setSummary(Ficheros.path.replace(Ficheros.root, "/sdcard"));

                    }
                });

                dialog.setView(view);
                dialog.show();


                break;
            case "opcion2":


                final View view2 = getActivity().getLayoutInflater().inflate(R.layout.chooser, null);
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(getActivity());
                chos = (ChooseFolder) view2.findViewById(R.id.chooserview);
                chos.setPath(BackUp.path);

                dialog2.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {


                        BackUp.path = chos.getPath();

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(PreferencesCons.RUTABACKUP, BackUp.path);
                        editor.commit();

                        preference.setSummary(BackUp.path.replace(BackUp.root, "/sdcard"));

                    }
                });

                dialog2.setView(view2);
                dialog2.show();

                break;
            case "opcion3":


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.confirmarDelBackup)
                        .setCancelable(true)
                        .setTitle(R.string.eliminarTittle)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                File archivos[];
                                File carpeta;
                                Boolean mens = false;

                                carpeta = new File(BackUp.path);
                                if (carpeta.exists()) {
                                    archivos = carpeta.listFiles();
                                    for (File file : archivos) {
                                        String name = file.getName();
                                        if (name.contains("PL ")) {
                                            mens= true;
                                            BackUp.removeItem(name);
                                        }
                                    }
                                }

                                Toast.makeText(getActivity(), getResources().getString(R.string.prefAlldelete), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();



                break;
            default:
                break;

        }


        return super.onPreferenceTreeClick(preferenceScreen, preference);


    }
}
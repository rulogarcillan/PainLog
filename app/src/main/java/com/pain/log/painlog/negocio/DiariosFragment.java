package com.pain.log.painlog.negocio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.pain.log.painlog.BD.Consultas;
import com.pain.log.painlog.BD.MyDatabase;
import com.pain.log.painlog.Constantes.Ficheros;
import com.pain.log.painlog.R;
import com.pain.log.painlog.export.exportLog;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.cketti.library.changelog.ChangeLog;

import static com.pain.log.painlog.negocio.LogUtils.copybd;


public class DiariosFragment extends Fragment {

    // BBDD
    private MyDatabase myDB; //base de datos
    private FloatingActionButton fab; //boton añadir
    private RecyclerView recyclerView;
    private TextView mensajeVacio;
    private AdapterProyectos adapter;
    private Consultas consultas;
    private ArrayList<Diarios> items = new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();
        //carga de recyclerview
        items = consultas.getDiarios(); // llamada a query BBDD
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);

    }


    public static DiariosFragment newInstance() {
        DiariosFragment fragment = new DiariosFragment();
        return fragment;
    }

    public DiariosFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.diarios, container, false);


        ChangeLog cl = new ChangeLog(getActivity());
        if (cl.isFirstRun()) {
            new BaseActivity.LanzaChangelog(getActivity()).getLogDialog().show();
        }

        mensajeVacio = (TextView) rootView.findViewById(R.id.txtMnsVacio);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) rootView.findViewById(R.id.btn_add);

        myDB = new MyDatabase(getActivity());
        copybd();
        scroll();
        //  getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_pain));

        consultas = new Consultas(getActivity());


        adapter = new AdapterProyectos(getActivity(), items); //Agregamos los items al adapter

        //definimos el recycler y agregamos el adaptaer

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


       /* RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);*/
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);





        //add diario
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final View view = getActivity().getLayoutInflater().inflate(R.layout.edittext, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        final Calendar c = Calendar.getInstance();

                        EditText editText = (EditText) view.findViewById(R.id.edittext);

                        if (editText.getText().toString().trim().length() == 0) {
                            Toast.makeText(getActivity(), R.string.errorvacio, Toast.LENGTH_SHORT).show();
                        } else {
                            Diarios nuevo = new Diarios(consultas.genKeyIdTablaDia(), editText.getText().toString(), dateFormat.format(c.getTime()).toString());
                            consultas.addDiario(nuevo);
                            carga();
                            mensajeVacio.setVisibility(View.INVISIBLE);
                        }

                    }
                });
                dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });

        return rootView;

    }

    protected void deleteItem(int clave) {

        consultas.deleteDiario(clave);
        carga();
        if (items.isEmpty())
            mensajeVacio.setVisibility(View.VISIBLE);

    }


    protected void editItem(int clave, String titu) {
        consultas.editDiario(clave, titu);
        carga();

    }




    protected void exportItem(final int clave, final String name, View v) {

        final PopupMenu popup = new PopupMenu(getActivity(), v);


        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.export, popup.getMenu());


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                exportLog exp = new exportLog(getActivity());
                Boolean result = false;
                Toast mens;

                switch (item.getItemId()) {
                    case R.id.export:

                        result = exp.exportToExcel(consultas.getLogs(clave), name);

                        if (result)
                            mens = Toast.makeText(getActivity(), getResources().getString(R.string.exportok), Toast.LENGTH_SHORT);
                        else
                            mens = Toast.makeText(getActivity(), getResources().getString(R.string.exportnotok), Toast.LENGTH_SHORT);

                        mens.show();
                        break;

                    case R.id.exportYenviar:

                        result = exp.exportToExcel(consultas.getLogs(clave), name);


                        if (result) {
                            mens = Toast.makeText(getActivity(), getResources().getString(R.string.exportok), Toast.LENGTH_SHORT);
                            export(name);
                        } else
                            mens = Toast.makeText(getActivity(), getResources().getString(R.string.exportnotok), Toast.LENGTH_SHORT);

                        mens.show();
                        break;

                    default:

                        break;

                }
                return true;
            }
        });

        popup.show();

    }

    private void scroll() {

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (recyclerView.getY() > dy) {

                    fab.show(true);


                } else if (recyclerView.getY() <= dy) {
                    fab.hide(true);

                }

            }

        });
    }

    private void carga() {
        items = consultas.getDiarios(); // llamada a query BBDD
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        fab.show();
    }


    private void export(String name){


        File file = Ficheros.getFile(Ficheros.generaNombre(name));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("application/excel");
        getActivity().startActivity(Intent.createChooser(shareIntent, getActivity().getText(R.string.exportSendTittle)));
    }




}
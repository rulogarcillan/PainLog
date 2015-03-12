package com.pain.log.painlog.negocio;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.pain.log.painlog.BD.Consultas;
import com.pain.log.painlog.Constantes.Ficheros;

import com.pain.log.painlog.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.pain.log.painlog.negocio.LogUtils.LOGI;

/**
 * Created by rulo on 12/03/15.
 */
public class ExplorerActivity  extends BaseActivity {


        private RecyclerView recyclerView;
        private TextView mensajeVacio;
        private AdapterFicheros adapter;

        private ArrayList<FicherosExcel> items = new ArrayList<>();


        @Override
        protected void onResume() {
            super.onResume();

            carga();
            if (!items.isEmpty())
                mensajeVacio.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.explorer);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mensajeVacio = (TextView) findViewById(R.id.txtMnsVacio);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


            getSupportActionBar().setTitle(R.string.explorar);


            adapter = new AdapterFicheros(this, items, 0); //Agregamos los items al adapter
            carga();
            //definimos el recycler y agregamos el adaptaer
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
                RecyclerView.ItemDecoration itemDecoration =
                        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
                recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            if (!items.isEmpty())
                mensajeVacio.setVisibility(View.INVISIBLE);


        }


        private void carga(){


            items = Ficheros.cargaItems();
            adapter.setItems(items);
            adapter.notifyDataSetChanged();


        }




    }



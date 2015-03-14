package com.pain.log.painlog.negocio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pain.log.painlog.Constantes.Ficheros;
import com.pain.log.painlog.R;

import java.util.ArrayList;

/**
 * Created by rulo on 12/03/15.
 */
public class ExplorerFragment  extends Fragment {


        private RecyclerView recyclerView;
        private TextView mensajeVacio;
        private AdapterFicheros adapter;

        private ArrayList<FicherosExcel> items = new ArrayList<>();


    public static ExplorerFragment newInstance() {
        ExplorerFragment fragment = new ExplorerFragment();
        return fragment;
    }

    public ExplorerFragment() {
    }


        @Override
        public void onResume() {
            super.onResume();

            carga();
            if (!items.isEmpty())
                mensajeVacio.setVisibility(View.INVISIBLE);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.explorer, container, false);


            mensajeVacio = (TextView) rootView.findViewById(R.id.txtMnsVacio);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);



            adapter = new AdapterFicheros(getActivity(), items, 0); //Agregamos los items al adapter
            carga();
            //definimos el recycler y agregamos el adaptaer
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
                RecyclerView.ItemDecoration itemDecoration =
                        new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
                recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            if (!items.isEmpty())
                mensajeVacio.setVisibility(View.INVISIBLE);



            return  rootView;
        }


        private void carga(){

            items = Ficheros.cargaItems();
            adapter.setItems(items);
            adapter.notifyDataSetChanged();

        }

   }



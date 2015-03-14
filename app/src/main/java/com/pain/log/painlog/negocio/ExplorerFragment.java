package com.pain.log.painlog.negocio;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.pain.log.painlog.Constantes.Ficheros;
import com.pain.log.painlog.R;
import com.pain.log.painlog.SwipeRecycler.SwipeableRecyclerViewTouchListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rulo on 12/03/15.
 */
public class ExplorerFragment extends Fragment {


    private RecyclerView recyclerView;
    private TextView mensajeVacio, path;
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


        path = (TextView) rootView.findViewById(R.id.path);
        mensajeVacio = (TextView) rootView.findViewById(R.id.txtMnsVacio);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);


        path.setText(Ficheros.customPath);

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
        clickItem();


        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    undo(items.get(position), position);
                                    items.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                                if (!items.isEmpty())
                                    mensajeVacio.setVisibility(View.INVISIBLE);
                                else
                                    mensajeVacio.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    export(items.get(position));

                                }

                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);


        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);


        return rootView;
    }


    public void carga() {

        items = Ficheros.cargaItems();
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);
        else
            mensajeVacio.setVisibility(View.VISIBLE);


    }

    private void export(FicherosExcel item) {

        File file = Ficheros.getFile(item.getNombre());

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("application/excel");
        getActivity().startActivity(Intent.createChooser(shareIntent, getActivity().getText(R.string.exportSendTittle)));
    }


    private void clickItem() {


        adapter.SetOnItemClickListener(new AdapterFicheros.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int i) {




                File file = Ficheros.getFile(items.get(i).getNombre());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                try {

                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                    startActivity(intent);

                } catch (Exception e) {

                    SnackbarManager.show(
                            Snackbar.with(getActivity()).text(getResources().getString(R.string.noappxls)).actionLabel(R.string.descargar).actionLabelTypeface(Typeface.DEFAULT_BOLD).actionColorResource(R.color.color_h).actionListener(new ActionClickListener() {
                                @Override
                                public void onActionClicked(Snackbar snackbar) {

                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("market://search?q=excel"));
                                    startActivity(intent);

                                }
                            }),getActivity());

                }

            }
        });
    }


    private void undo(final FicherosExcel item, final int pos) {

        SnackbarManager.show(
                Snackbar.with(getActivity()).text(item.getNombre() + " " + getResources().getString(R.string.eliminado)).actionLabel(R.string.deshacer).actionLabelTypeface(Typeface.DEFAULT_BOLD).actionColorResource(R.color.color_h).actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {

                        items.add(pos, item);
                        adapter.notifyItemInserted(pos);
                        adapter.notifyDataSetChanged();
                        if (!items.isEmpty())
                            mensajeVacio.setVisibility(View.INVISIBLE);
                        else
                            mensajeVacio.setVisibility(View.VISIBLE);


                    }
                }).eventListener(new EventListener() {
                    @Override
                    public void onShow(Snackbar snackbar) {

                    }

                    @Override
                    public void onShowByReplace(Snackbar snackbar) {

                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }

                    @Override
                    public void onDismiss(Snackbar snackbar) {

                        Ficheros.removeItem(item.getNombre());


                    }

                    @Override
                    public void onDismissByReplace(Snackbar snackbar) {
                        Ficheros.removeItem(item.getNombre());
                    }

                    @Override
                    public void onDismissed(Snackbar snackbar) {
                        Ficheros.removeItem(item.getNombre());

                    }
                }), getActivity());


    }


}



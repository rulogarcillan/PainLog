package com.pain.log.painlog.negocio;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.pain.log.painlog.R;
import com.pain.log.painlog.SwipeRecycler.SwipeableRecyclerViewTouchListener;
import com.pain.log.painlog.export.Ficheros;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rulo on 12/03/15.
 */
public class ExplorerFragment extends Fragment {


    private RecyclerView recyclerView;
    private TextView mensajeVacio, path;
    private AdapterFicheros adapter;
    private Boolean eliminar = true;

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

        path.setText(Ficheros.path.replace(Ficheros.root, "/sdcard"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.explorer, container, false);


        path = (TextView) rootView.findViewById(R.id.path);
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
                                    eliminar = true;
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

        setHasOptionsMenu(true);

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

                        eliminar = false;
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

                        if (eliminar)Ficheros.removeItem(item.getNombre());


                    }

                    @Override
                    public void onDismissByReplace(Snackbar snackbar) {
                        if (eliminar)Ficheros.removeItem(item.getNombre());

                    }

                    @Override
                    public void onDismissed(Snackbar snackbar) {
                        if (eliminar)Ficheros.removeItem(item.getNombre());


                    }
                }), getActivity());


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.help_menu, menu);
        MenuItem item = menu.findItem(R.id.action_help);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                dialogAyuda();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }




    private void dialogAyuda(){


        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LinearLayout contentView = (LinearLayout) ((getActivity()))
                .getLayoutInflater().inflate(R.layout.help, null);
        dialog.setContentView(contentView);

        ImageView image = (ImageView) contentView.findViewById(R.id.helpDelete);
        final AnimationDrawable animation = (AnimationDrawable) image.getDrawable();

        ImageView image2 = (ImageView) contentView.findViewById(R.id.helpSend);
        final AnimationDrawable animation2 = (AnimationDrawable) image2.getDrawable();
        dialog.setCancelable(true);




        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                animation.start();animation2.start();
            }
        });
        dialog.show();

       /* final View view = getActivity().getLayoutInflater().inflate(R.layout.help, null);



        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {




                ImageView image2 = (ImageView) view.findViewById(R.id.helpSend);
                AnimationDrawable animation2 = (AnimationDrawable) image2.getDrawable();
                animation2.start();

                ImageView image = (ImageView) view.findViewById(R.id.helpDelete);
                AnimationDrawable animation = (AnimationDrawable) image.getDrawable();
                animation.start();



            }
        });


        dialog.setView(view);
        dialog.show();*/

    }




}



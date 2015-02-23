package com.pain.log.painlog.negocio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.pain.log.painlog.BD.MyDatabase;
import com.pain.log.painlog.R;

import java.util.ArrayList;

import static com.pain.log.painlog.negocio.LogUtils.copybd;


public class DiariosActivity extends BaseActivity {

    // BBDD
    private MyDatabase myDB; //base de datos
    private FloatingActionButton fab; //boton añadir
    private RecyclerView recyclerView;
    private TextView mensajeVacio;
    private AdapterProyectos adapter;
    private MoonCalculation luna = new MoonCalculation();
    private ArrayList<Diarios> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diarios);

        mensajeVacio = (TextView) findViewById(R.id.txtMnsVacio);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.btn_add);

        myDB = new MyDatabase(this);
        copybd();
        scroll();

        for(int x = 1; x < 31; x++) {
            items.add(new Diarios(x, luna.phaseName(luna.moonPhase(2015,3,x+1))));

        }

        adapter = new AdapterProyectos(this, items);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View view = getLayoutInflater().inflate(R.layout.edittext, null );
                AlertDialog.Builder dialog = new AlertDialog.Builder(DiariosActivity.this);
                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });



    }

    private void scroll(){

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


}

package com.pain.log.painlog.negocio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.pain.log.painlog.BD.Consultas;
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
    private Consultas consultas;
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
        consultas = new Consultas(this);

        //carga de recyclerview
        items = consultas.getDiarios(); // llamada a query BBDD
        adapter = new AdapterProyectos(this, items); //Agregamos los items al adapter

        //definimos el recycler y agregamos el adaptaer
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       /* RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);


        //add diario
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final View view = getLayoutInflater().inflate(R.layout.edittext, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(DiariosActivity.this);

                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        EditText editText = (EditText) view.findViewById(R.id.edittext);

                        Diarios nuevo = new Diarios(consultas.genKeyIdTabla("diarios"),editText.getText().toString()) ;
                        consultas.addDiario(nuevo);
                        adapter.add(nuevo, adapter.LAST_POSITION);

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

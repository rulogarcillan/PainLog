package com.pain.log.painlog.negocio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.pain.log.painlog.BD.Consultas;
import com.pain.log.painlog.BD.MyDatabase;
import com.pain.log.painlog.R;

import java.util.ArrayList;

import static com.pain.log.painlog.negocio.LogUtils.LOGI;

public class LogActivity extends BaseActivity {


    // BBDD
    private MyDatabase myDB; //base de datos
    private FloatingActionButton fab; //boton añadir
    private RecyclerView recyclerView;
    private TextView mensajeVacio;
    private AdapterLogs adapter;
    private Consultas consultas;
    private MoonCalculation luna = new MoonCalculation();
    private ArrayList<Logs> items = new ArrayList<>();
    private int clave;
    private String titulo;


    @Override
    protected void onResume() {
        super.onResume();
        //carga de recyclerview
        carga();
        if (!items.isEmpty())
            mensajeVacio.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mensajeVacio = (TextView) findViewById(R.id.txtMnsVacio);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.btn_add);


        myDB = new MyDatabase(this);
        scroll();
        cargaDatos();
        getSupportActionBar().setTitle(titulo);


        consultas = new Consultas(this);





        adapter = new AdapterLogs(this, items); //Agregamos los items al adapter

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


        //add log
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogActivity.this, DolActivity.class);
                intent.putExtra("CLAVE",clave);
                LOGI("VALOR CLAVE", Integer.toString(clave));
                intent.putExtra("SERVICIO", "INS");
                LogActivity.this.startActivity(intent);
            }
        });

    }

    protected void deleteItem(int clave, int clave_d) {

        consultas.deleteLog(clave, clave_d);
        carga();
        if (items.isEmpty())
            mensajeVacio.setVisibility(View.VISIBLE);

    }


    private void cargaDatos() {

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

        }
        clave = extras.getInt("CLAVE");
        titulo = extras.getString("NOMBRE");
        LOGI("VALOR CLAVE", Integer.toString(clave));
        LOGI("VALOR NOMBRE", titulo);


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

    private void carga(){
        items = consultas.getLogs(clave); // llamada a query BBDD
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

}

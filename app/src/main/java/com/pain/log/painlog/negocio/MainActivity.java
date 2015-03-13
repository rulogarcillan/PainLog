package com.pain.log.painlog.negocio;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.aboutlibraries.Libs;
import com.pain.log.painlog.BD.Consultas;
import com.pain.log.painlog.Constantes.Constantes;
import com.pain.log.painlog.R;
import com.pain.log.painlog.export.exportLog;

import java.util.ArrayList;

import de.cketti.library.changelog.ChangeLog;

public class MainActivity extends ActionBarActivity {


    DiariosFragment fragment = new DiariosFragment();

    RecyclerView mRecyclerView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    AdapterDrawer adapter;
    FragmentManager fragmentManager;
    Boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerDrawer);


        adapter = new AdapterDrawer(this, new Constantes().genItemsDrawerMenu()); //Agregamos los items al adapter
        //definimos el recycler y agregamos el adaptaer
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
       /* RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);*/
        mRecyclerView.setAdapter(adapter);
        optionDrawer();


        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.changelog,
                R.string.app_name) {
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                invalidateOptionsMenu();
                syncState();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

       // mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerToggle.syncState();


        LanzarMisDiarios();

        // Sombra del panel Navigation Drawer
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                    mDrawerLayout.closeDrawer(mRecyclerView);
                } else {
                    mDrawerLayout.openDrawer(mRecyclerView);
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void optionDrawer(){


        adapter.SetOnItemClickListener(new AdapterDrawer.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {


                if (adapter.getItems().get(i).getTitulo() == R.string.puntuar) {
                    LanzaRate();
                    mDrawerLayout.closeDrawers();
                } else if (adapter.getItems().get(i).getTitulo() == R.string.license) {
                    lanzaLicense();
                    mDrawerLayout.closeDrawers();
                } else if (adapter.getItems().get(i).getTitulo() == R.string.more) {
                    LanzaMore();
                   mDrawerLayout.closeDrawers();
                } else if (adapter.getItems().get(i).getTitulo() == R.string.changelog) {
                    new LanzaChangelog(MainActivity.this).getFullLogDialog().show();
                  mDrawerLayout.closeDrawers();
                } else if (adapter.getItems().get(i).getTitulo() == R.string.explorar){
                    lanzaExplorer();
                    mDrawerLayout.closeDrawers();
                }  else if (adapter.getItems().get(i).getTitulo() == R.string.exportall){

                    exportAllItem();
                    mDrawerLayout.closeDrawers();
                }else if (adapter.getItems().get(i).getTitulo() == R.string.miscalendarios) {
                    LanzarMisDiarios();
                    mDrawerLayout.closeDrawers();
                }

            }
        });
    }

    private void LanzarMisDiarios(){

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        getSupportActionBar().setTitle(R.string.miscalendarios);

    }

    private void LanzaRate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.pain.log.painlog"));
        startActivity(intent);
    }


    private void LanzaMore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Raúl R."));
        startActivity(intent);
    }

    public void  lanzaExplorer(){

        Intent intent = new Intent(this, ExplorerActivity.class);
        startActivity(intent);
    }

    public static class LanzaChangelog extends ChangeLog {

        public static final String DEFAULT_CSS =

                "body {                                                           " + "	font-family: Verdana, Helvetica, Arial, sans-serif;   " + "	font-size: 11px;                                      " + "	color: #000000;                                       " + "	background-color: #ffffff;                            " + "	margin: 0px;                                          " + "	padding: 0px;                                         " + "}                                                        "
                        + "h1 {                                                     " + "	font-size: 14px;                                      " + "	font-weight: bold;                                    " + "	text-transform: uppercase;                            " + "	color: #000000;                                       " + "	margin: 0px;                                          " + "	padding: 10px 0px 0px 8px;                            " + "}                                                        "
                        + "h2 {                                                     " + "	font-size: 10px;                                      " + "	color: #999999;                                       " + "	font-weight: normal;                                  " + "	margin: 0px 0px 0px 8px;                              " + "	padding: 0px;                                         " + "}                                                        " + "ul {                                                     "
                        + "	margin: 0px 0px 10px 15px;                            " + "	padding-left: 15px;                                " + "	padding-top: 8px;                                     " + "	list-style-type: square;                              " + "	color: #999999;                                       " + "}";

        public LanzaChangelog(Context context) {
            super(new ContextThemeWrapper(context, R.style.AppTheme), DEFAULT_CSS);
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.salir), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    public void lanzaLicense() {


        Fragment fragment =  new Libs.Builder()
                //Pass the fields of your application to the lib so it can find all external lib information
                .withFields(R.string.class.getFields())
                .withVersionShown(true)
                .withLicenseShown(true)
                .withAutoDetect(true)
                .withLibraries("sqliteassethelper")
                .withAboutDescription(getResources().getString(R.string.escrita) + "<br/><br/><b>License GNU GPL V3.0</b><br/><br/><a href=\"https://github.com/rulogarcillan/PainLog\">Project in Github</a>")
                .withActivityTheme(R.style.AppTheme)
                        //start the activity
                .fragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        getSupportActionBar().setTitle(R.string.license);

    }

    public void exportAllItem() {

        Consultas consultas = new Consultas(this);
        exportLog exp = new exportLog(this);
        Boolean result = false;
        Toast mens;
        ArrayList<Diarios> items;
        items = consultas.getDiarios();


        for (Diarios item : items) {

            result = exp.exportToExcel(consultas.getLogs(item.getClave()), item.getNombre(), false);

        }

        if (result)
            mens = Toast.makeText(this, getResources().getString(R.string.exportok), Toast.LENGTH_SHORT);
        else
            mens = Toast.makeText(this, getResources().getString(R.string.exportnotok), Toast.LENGTH_SHORT);

        mens.show();

    }


}



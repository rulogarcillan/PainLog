package com.pain.log.painlog.negocio;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.pain.log.painlog.BD.Consultas;
import com.pain.log.painlog.Constantes.Constantes;
import com.pain.log.painlog.ContextIconMenu.IconContextMenu;
import com.pain.log.painlog.R;
import com.pain.log.painlog.export.BackUp;
import com.pain.log.painlog.export.Ficheros;
import com.pain.log.painlog.export.exportLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;

import static com.google.android.gms.drive.DriveApi.DriveContentsResult;
import static com.pain.log.painlog.negocio.LogUtils.LOGI;


public class MainActivity extends DriveActivity {


    DiariosFragment fragmentD = new DiariosFragment();
    ExplorerFragment fragmentE = new ExplorerFragment();

    RecyclerView mRecyclerView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    AdapterDrawer adapter;
    FragmentManager fragmentManager;
    Boolean doubleBackToExitPressedOnce = false;
    ArrayList<String> resItem = new ArrayList<>();
    ArrayList<DriveFiles> drivefiles = new ArrayList<>();


    @Override
    protected void onPause() {
        if (mGoogleApiClient != null && !flag_ini) {

            mGoogleApiClient.unregisterConnectionCallbacks(llamadaBackup);
            mGoogleApiClient.unregisterConnectionCallbacks(llamadaRestore);
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

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

        dialog = new ProgressDialog(MainActivity.this);

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


    private void optionDrawer() {


        adapter.SetOnItemClickListener(new AdapterDrawer.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

                if (adapter.getItems().get(i).getTitulo() == R.string.miscalendarios) {
                    mDrawerLayout.closeDrawers();
                    LanzarMisDiarios();
                } else if (adapter.getItems().get(i).getTitulo() == R.string.explorar) {
                    mDrawerLayout.closeDrawers();
                    LanzaExplorer();
                } else if (adapter.getItems().get(i).getTitulo() == R.string.exportall) {
                    mDrawerLayout.closeDrawers();
                    exportAllItem();
                } else if (adapter.getItems().get(i).getTitulo() == R.string.backupRES) {

                    lanzarContextMenuBackup(BackUp.OPRESTORE);
                    mDrawerLayout.closeDrawers();

                } else if (adapter.getItems().get(i).getTitulo() == R.string.backupUP) {

                    lanzarContextMenuBackup(BackUp.OPBACKUP);
                    mDrawerLayout.closeDrawers();


                } else if (adapter.getItems().get(i).getTitulo() == R.string.settings) {
                    mDrawerLayout.closeDrawers();
                    LanzarSetting();
                    //  mDrawerLayout.closeDrawers();
                }

            }
        });
    }


    private void lanzarContextMenuBackup(final int op) {
        IconContextMenu cm = new IconContextMenu(MainActivity.this, R.menu.backup);

        if (op == BackUp.OPBACKUP)
            cm.setTitle(getResources().getString(R.string.backupUP));

        else if (op == BackUp.OPRESTORE)
            cm.setTitle(getResources().getString(R.string.backupRES));
        cm.show();

        cm.setOnIconContextItemSelectedListener(new IconContextMenu.IconContextItemSelectedListener() {
            @Override
            public void onIconContextItemSelected(MenuItem item, Object info) {
                if (op == BackUp.OPBACKUP)
                    LanzarBackupTo(item.getItemId());


                else if (op == BackUp.OPRESTORE) {

                    LanzarRestorefrom(item.getItemId());
                }
            }
        });
    }


    private void LanzarSetting() {

        Intent intent = new Intent(this, PreferencesAct.class);
        startActivity(intent);
    }


    private void restoreLocal() {

        ArrayList<String> items = new ArrayList<>();
        ArrayAdapter<String> filesXML = new ArrayAdapter<String>(this, R.layout.list_item_backup, items);

        File[] files;
        java.io.File folder;
        resItem.clear();

        folder = new File(BackUp.path);
        if (folder.exists()) {
            files = folder.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
                }
            });


            for (File file : files) {

                String name = file.getName();

                if (name.contains("PL ")) {
                    resItem.add(name);
                    items.add(name.replace("-", "/").replace(".xml", "").replace(".", ":").replace("PL", "Backup"));

                }
            }
        }

        if (!items.isEmpty()) {


            new AlertDialog.Builder(MainActivity.this)
                    .setAdapter(filesXML, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            BackUp.ReadXMLFile(new File(BackUp.path + "/" + resItem.get(which)), MainActivity.this, false);
                            android.support.v4.app.Fragment fragment = fragmentManager.findFragmentById(R.id.container);
                            String tag = (String) fragment.getTag();
                            if (tag == "DIARIOS") {
                                fragmentD.carga();

                            }
                        }
                    })
                    .setCancelable(true).setTitle(R.string.RestoreTitle)
                    .show();
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.noBackups), Toast.LENGTH_SHORT).show();
        }

    }

    private void LanzarRestorefrom(int id) {


        switch (id) {
            case R.id.backup_st:
                restoreLocal();
                break;
            case R.id.backup_dr:
                flag_ini = false;
                driveRestore();
                break;
        /*    case R.id.backup_db:
                break;*/

            default:
                break;
        }


    }

    private void LanzarBackupTo(int id) {

        switch (id) {
            case R.id.backup_st:
                BackUp.dump(this);
                break;
            case R.id.backup_dr:
                flag_ini = false;
                driveBackup();
                break;
           /* case R.id.backup_db:
                break;*/
            default:
                break;
        }

    }


    private void LanzarMisDiarios() {

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragmentD, "DIARIOS")
                .commit();
        getSupportActionBar().setTitle(R.string.miscalendarios);

    }

    public void LanzaExplorer() {


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragmentE, "EXPLORER")
                .commit();
        getSupportActionBar().setTitle(R.string.explorar);
    }

    public void exportAllItem() {

        android.support.v4.app.Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        String tag = (String) fragment.getTag();


        Consultas consultas = new Consultas(this);
        exportLog exp = new exportLog(this);
        Boolean result = false;
        Toast mens;
        ArrayList<Diarios> items;
        items = consultas.getDiarios();
        String url = "<table>";
        String si = "<b><font color='#45ab2d'>OK</font></b>", no = "<b><font color='#e71a03'>ERROR</font></b>";
        String row = "<tr>\n" +
                "<td><b><font color='#B8B8B8' face=\"sans-serif\">%name</font><b></td>\n" +
                "<td>%result</td>\n" +
                "</tr>";

        for (Diarios item : items) {

            result = exp.exportToExcel(consultas.getLogs(item.getClave()), item.getNombre());

            if (result)
                url = url + row.replace("%name", padLeft(Ficheros.generaNombre(item.getNombre()), 10)).replace("%result", si);
            else
                url = url + row.replace("%name", padLeft(Ficheros.generaNombre(item.getNombre()), 10)).replace("%result", no);

        }
        url = url + "</table>";

        if (result)
            mens = Toast.makeText(this, getResources().getString(R.string.exportok), Toast.LENGTH_SHORT);
        else
            mens = Toast.makeText(this, getResources().getString(R.string.exportnotok), Toast.LENGTH_SHORT);

        mens.show();

        if (!url.equalsIgnoreCase("<table></table>")) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setCancelable(false)
                    .setTitle(R.string.exportedTitleOk)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }

                    });

            WebView wv = new WebView(this);

            LOGI("URL", "6" + url);
            wv.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);

                    return true;
                }
            });

            alert.setView(wv);
            alert.show();

            if (tag == "EXPLORER") {
                fragmentE.carga();
            }

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
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public String padLeft(String value, int length) {
        StringBuilder result = new StringBuilder(length);
        result.append(value);

        while (result.length() < length) {
            result.insert(0, " ");
        }

        return result.toString();
    }


    /**
     * *****DRIVE********
     */

    private void driveBackup() {

        mGoogleApiClient.registerConnectionCallbacks(llamadaBackup);
        dialog.setMessage(getResources().getString(R.string.dialogBackup1));
        dialog.show();
        mGoogleApiClient.connect();

    }

    private void driveRestore() {

        mGoogleApiClient.registerConnectionCallbacks(llamadaRestore);
        dialog.setMessage(getResources().getString(R.string.dialogBackup1));
        dialog.show();
        mGoogleApiClient.connect();

    }

    final GoogleApiClient.ConnectionCallbacks llamadaBackup = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            dialog.setMessage(getResources().getString(R.string.dialogBackup2));
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(driveContentsCallback);
            dialog.dismiss();

        }

        @Override
        public void onConnectionSuspended(int i) {
            dialog.dismiss();
        }
    };

    final private ResultCallback<DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        dialog.dismiss();
                        return;
                    }
                    final DriveContents driveContents = result.getDriveContents();

                    // Perform I/O off the UI thread.
                    new Thread() {
                        @Override
                        public void run() {
                            // write content to DriveContents
                            OutputStream outputStream = driveContents.getOutputStream();
                            Writer writer = new OutputStreamWriter(outputStream);
                            try {
                                writer.write(BackUp.getTextdumpTemp(MainActivity.this));
                                writer.close();
                            } catch (IOException e) {

                            }

                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(BackUp.genName())
                                    .setMimeType("text/xml")
                                    .setStarred(true).build();

                            // create a file on root folder
                            Drive.DriveApi.getRootFolder(getGoogleApiClient())
                                    .createFile(getGoogleApiClient(), changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }
                    }.start();
                }
            };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        dialog.dismiss();
                        return;
                    }
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.BackupoK), Toast.LENGTH_SHORT).show();
                }
            };


    private final GoogleApiClient.ConnectionCallbacks llamadaRestore = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {


            final Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/xml"))
                    .addFilter(Filters.eq(SearchableField.TRASHED, false))
                    .build();

            dialog.setMessage(getResources().getString(R.string.dialogBackup2));

            Drive.DriveApi.requestSync(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status result) {

                            if (!result.isSuccess()) {
                                showMessage(getResources().getString(R.string.errBackup1));
                                dialog.dismiss();
                                return;
                            }
                            Drive.DriveApi.query(getGoogleApiClient(), query).setResultCallback(genListFiles);


                        }
                    });


        }

        @Override
        public void onConnectionSuspended(int i) {
            dialog.dismiss();
        }
    };

    final private ResultCallback<DriveApi.MetadataBufferResult> genListFiles = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {

                    if (!result.getStatus().isSuccess()) {

                        return;
                    }


                    itemsName.clear();
                    itemsId.clear();

                    MetadataBuffer files = result.getMetadataBuffer();
                    if (files.getCount() > 0) {
                        for (int i = files.getCount() - 1; i >= 0; i--) {
                            itemsName.add(files.get(i).getTitle().replace("-", "/").replace(".xml", "").replace(".", ":").replace("PL", "Backup"));
                            itemsId.add(files.get(i).getDriveId().getResourceId());
                        }

                        ArrayAdapter<String> filesXML = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item_backup, itemsName);


                        new AlertDialog.Builder(MainActivity.this)
                                .setAdapter(filesXML, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogList, int which) {

                                        dialog.setMessage(getResources().getString(R.string.dialogBackup4));
                                        dialog.show();
                                        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), itemsId.get(which))
                                                .setResultCallback(idCallback);
                                    }
                                })
                                .setCancelable(true).setTitle(R.string.RestoreTitle)
                                .show();


                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.noBackups), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();


                }
            };

    ArrayList<String> itemsName = new ArrayList<>();
    ArrayList<String> itemsId = new ArrayList<>();



    final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
        @Override
        public void onResult(DriveApi.DriveIdResult result) {
            new RetrieveDriveFileContentsAsyncTask(
                    MainActivity.this).execute(result.getDriveId());
        }
    };

    final private class RetrieveDriveFileContentsAsyncTask
            extends ApiClientAsyncTask<DriveId, Boolean, String> {

        public RetrieveDriveFileContentsAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected String doInBackgroundConnected(DriveId... params) {
            String contents = null;
            DriveFile file = Drive.DriveApi.getFile(getGoogleApiClient(), params[0]);
            DriveContentsResult driveContentsResult =
                    file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return null;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(driveContents.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                contents = builder.toString();
            } catch (IOException e) {

            }

            driveContents.discard(getGoogleApiClient());
            return contents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                showMessage("Error while reading from the file");
                dialog.dismiss();
                return;
            }


            try {
                File file = new File(BackUp.path + "/" + "drive.xml");
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(result);
                output.close();

                BackUp.ReadXMLFile(file, MainActivity.this, false);
                file.delete();
                android.support.v4.app.Fragment fragment = fragmentManager.findFragmentById(R.id.container);
                String tag = (String) fragment.getTag();
                if (tag == "DIARIOS") {
                    fragmentD.carga();

                }


            } catch ( IOException e ) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }

    public abstract class ApiClientAsyncTask<Params, Progress, Result>
            extends AsyncTask<Params, Progress, Result> {

        private GoogleApiClient mClient;

        public ApiClientAsyncTask(Context context) {
            GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE);
            mClient = builder.build();
        }

        @Override
        protected final Result doInBackground(Params... params) {
            Log.d("TAG", "in background");
            final CountDownLatch latch = new CountDownLatch(1);
            mClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnectionSuspended(int cause) {
                }

                @Override
                public void onConnected(Bundle arg0) {
                    latch.countDown();
                }
            });
            mClient.registerConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult arg0) {
                    latch.countDown();
                }
            });
            mClient.connect();
            try {
                latch.await();
            } catch (InterruptedException e) {
                return null;
            }
            if (!mClient.isConnected()) {
                return null;
            }
            try {
                return doInBackgroundConnected(params);
            } finally {
                mClient.disconnect();
            }
        }

        /**
         * Override this method to perform a computation on a background thread, while the client is
         * connected.
         */
        protected abstract Result doInBackgroundConnected(Params... params);

        /**
         * Gets the GoogleApliClient owned by this async task.
         */
        protected GoogleApiClient getGoogleApiClient() {
            return mClient;
        }
    }
}




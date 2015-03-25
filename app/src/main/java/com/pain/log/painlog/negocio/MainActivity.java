package com.pain.log.painlog.negocio;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
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
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import static com.pain.log.painlog.negocio.LogUtils.LOGE;
import static com.pain.log.painlog.negocio.LogUtils.LOGI;


public class MainActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    DiariosFragment fragmentD = new DiariosFragment();
    ExplorerFragment fragmentE = new ExplorerFragment();

    RecyclerView mRecyclerView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    AdapterDrawer adapter;
    FragmentManager fragmentManager;
    Boolean doubleBackToExitPressedOnce = false;
    ArrayList<String> resItem = new ArrayList<>();
    ProgressDialog dialog;


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

                mGoogleApiClient.registerConnectionCallbacks(llamadaRestore);
                mGoogleApiClient.connect();
                dialog.setMessage(getResources().getString(R.string.dialogBackup1));
                dialog.show();

                break;
            case R.id.backup_db:
                break;

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
                driveBackup();
                break;
            case R.id.backup_db:
                break;
            // case R.id.backup_em:
            //   break;
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
     * ********************************************
     */


    private static final String TAG = "BaseDriveActivity";

    private DriveId sFolderId;
    private DriveId mFolderDriveId;
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private static final String FOLDER = "PainLog Backup";

    private GoogleApiClient mGoogleApiClient;
    private Thread t;


    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {

            mGoogleApiClient.unregisterConnectionCallbacks(llamadaBackup);
            mGoogleApiClient.unregisterConnectionCallbacks(llamadaRestore);
            mGoogleApiClient.disconnect();

        }
        super.onPause();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        LOGI(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();

            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            LOGE(TAG, "Exception while starting resolution activity");
        }
    }

    /**
     * Shows a toast message.
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    private void driveBackup() {

        dialog.setMessage(getResources().getString(R.string.dialogBackup1));
        dialog.show();
        mGoogleApiClient.registerConnectionCallbacks(llamadaBackup);
        mGoogleApiClient.connect();
        LOGI("sii","Conecta");


    }

    final private ResultCallback<DriveApi.MetadataBufferResult> getIdFolder = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {

                    if (!result.getStatus().isSuccess()) {

                        return;
                    }

                    MetadataBuffer files = result.getMetadataBuffer();
                    if (files.getCount() > 0) {

                        sFolderId = files.get(0).getDriveId();

                        /*Drive.DriveApi.fetchDriveId(getGoogleApiClient(), files.get(0).getDriveId().getResourceId())
                                .setResultCallback(idCallback);*/

                        return;

                    }
                }
            };


    final ResultCallback<DriveFolder.DriveFolderResult> creaCarpeta = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {

            if (!result.getStatus().isSuccess()) {
                showMessage(getResources().getString(R.string.errBackup2));
                return;
            }
            //ok
        }
    };


    /**
     * ***********CALLS*****************
     */

    final GoogleApiClient.ConnectionCallbacks llamadaBackup = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            boolean flag;
            LOGI("sii","empieza");
            procesoDriveBackup();

        }

        @Override
        public void onConnectionSuspended(int i) {
            dialog.dismiss();
        }
    };


    private void procesoDriveBackup() {

        sFolderId = null;

        final Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, FOLDER))
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

                        t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //sincroniza ok
                                MainActivity.this.dialog.setMessage(getResources().getString(R.string.dialogBackup3));

                                Drive.DriveApi.query(getGoogleApiClient(), query).setResultCallback(getIdFolder);
                                LOGI("sii","tenemos id?");
                                //id nulo tenemos que ccrear carpeta porque no existe
                                if (sFolderId  == null) {

                                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(FOLDER).build();
                                    Drive.DriveApi.getRootFolder(getGoogleApiClient()).createFolder(
                                            getGoogleApiClient(), changeSet).setResultCallback(creaCarpeta);

                                    //Dormimos para que se cree la id y la buscamos
                                    try {
                                        t.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    //sincronizamos
                                    Drive.DriveApi.requestSync(mGoogleApiClient)
                                            .setResultCallback(new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(Status result) {

                                                    if (!result.isSuccess()) {

                                                        showMessage(getResources().getString(R.string.errBackup1));
                                                        dialog.dismiss();
                                                        return;
                                                    }

                                                    //backup
                                                    dialog.dismiss();

                                                }
                                            });


                                } else{

                                    //backup

                                    dialog.dismiss();

                                }

                            }
                        });
                        t.start();

                    }
                });

    }


    private final GoogleApiClient.ConnectionCallbacks llamadaRestore = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            dialog.setMessage(getResources().getString(R.string.dialogBackup2));
            Drive.DriveApi.requestSync(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status result) {
                            if (!result.isSuccess()) {
                                // Sync not ok
                                showMessage(getResources().getString(R.string.errBackup1));
                                dialog.dismiss();
                                return;
                            }

                            showMessage("Noooo");
                            dialog.dismiss();
                        }
                    }, 10, TimeUnit.SECONDS);
        }

        @Override
        public void onConnectionSuspended(int i) {
            dialog.dismiss();
        }
    };


    final private ResultCallback<DriveApi.DriveIdResult> idCallback = new ResultCallback<DriveApi.DriveIdResult>() {
        @Override
        public void onResult(DriveApi.DriveIdResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Cannot find DriveId. Are you authorized to view this file?");
                return;
            }
            mFolderDriveId = result.getDriveId();
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(driveContentsCallback);
        }
    };

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create new file contents");
                        return;
                    }
                    DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), mFolderDriveId);
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("New file")
                            .setMimeType("text/plain")
                            .setStarred(true).build();
                    folder.createFile(getGoogleApiClient(), changeSet, result.getDriveContents())
                            .setResultCallback(fileCallback);
                }
            };


    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback =
            new ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create the file");
                        return;
                    }
                    showMessage("Created a file: " + result.getDriveFile().getDriveId());
                }
            };

}



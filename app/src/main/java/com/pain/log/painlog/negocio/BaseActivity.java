package com.pain.log.painlog.negocio;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.mikepenz.aboutlibraries.Libs;
import com.pain.log.painlog.R;

import de.cketti.library.changelog.ChangeLog;

import static com.pain.log.painlog.negocio.LogUtils.LOGE;
import static com.pain.log.painlog.negocio.LogUtils.LOGI;


/**
 * Created by raul.rodriguezconcep on 20/02/15.
 */
public class BaseActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_settings:
                break;
            case R.id.puntuar:

                LanzaRate();

                break;

            case R.id.more:

                LanzaMore();

                break;
            case R.id.changelog:

                new LanzaChangelog(BaseActivity.this).getFullLogDialog().show();
                break;
            case R.id.license:
                //Create an intent with context and the Activity class

                new Libs.Builder()
                        //Pass the fields of your application to the lib so it can find all external lib information
                        .withFields(R.string.class.getFields())
                        .withVersionShown(true)
                        .withLicenseShown(true)
                        .withAutoDetect(true)
                        .withLibraries("sqliteassethelper")
                        .withActivityTitle(getResources().getString(R.string.license))
                        .withAboutDescription(getResources().getString(R.string.escrita) + "<br/><br/><b>License GNU GPL V3.0</b><br/><br/><a href=\"https://github.com/rulogarcillan/PainLog\">Project in Github</a>")
                        .withActivityTheme(R.style.AppTheme)
                                //start the activity
                        .start(this);
                break;


            default:
                onBackPressed();
        }

        return true;
    }

    private void LanzaRate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.pain.log.painlog"));
        startActivity(intent);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }


    private void LanzaMore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Raúl R."));

        startActivity(intent);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
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




    private static final String TAG = "BaseDriveActivity";

    /**
     * DriveId of an existing folder to be used as a parent folder in
     * folder operations samples.
     */
    public static final String EXISTING_FOLDER_ID = "0B2EEtIjPUdX6MERsWlYxN3J6RU0";

    /**
     * DriveId of an existing file to be used in file operation samples..
     */
    public static final String EXISTING_FILE_ID = "0ByfSjdPVs9MZTHBmMVdSeWxaNTg";

    /**
     * Extra for account name.
     */
    protected static final String EXTRA_ACCOUNT_NAME = "account_name";

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Next available request code.
     */
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 2;
    protected GoogleApiClient mGoogleApiClient;



    public void clienteDrive() {


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(com.google.android.gms.drive.Drive.API)
                    .addScope(com.google.android.gms.drive.Drive.SCOPE_FILE)
                    .addScope(com.google.android.gms.drive.Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }



    }

    public void ConenectDrive(){

        mGoogleApiClient.connect();
    }

    public  void DisconectDrive(){
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();

        }
    }


    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        LOGI(TAG, "GoogleApiClient connected");

        DriveId folderId = DriveId.decodeFromString(EXISTING_FOLDER_ID);
        DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient, folderId);
        folder.getMetadata(mGoogleApiClient).setResultCallback(metadataRetrievedCallback);


        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("PainLog Backup").build();
        Drive.DriveApi.getRootFolder(getGoogleApiClient()).createFolder(getGoogleApiClient(), changeSet).setResultCallback(callback);

    }

    /**
     * Called when {@code mGoogleApiClient} is disconnected.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        LOGI(TAG, "GoogleApiClient connection suspended");
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution is
     * available.
     */
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
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Getter for the {@code GoogleApiClient}.
     */
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }


    final ResultCallback<DriveFolder.DriveFolderResult> callback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Error while trying to create the folder");
                return;
            }
            showMessage("Created a folder: " + result.getDriveFolder().getDriveId());
        }
    };

    final private ResultCallback<DriveResource.MetadataResult> metadataRetrievedCallback = new
            ResultCallback<DriveResource.MetadataResult>() {
                @Override
                public void onResult(DriveResource.MetadataResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.v(TAG, "Problem while trying to fetch metadata.");
                        return;
                    }

                    Metadata metadata = result.getMetadata();
                    if(metadata.isTrashed()){
                        Log.v(TAG, "Folder is trashed");
                    }else{
                        Log.v(TAG, "Folder is not trashed");
                    }

                }
            };

}




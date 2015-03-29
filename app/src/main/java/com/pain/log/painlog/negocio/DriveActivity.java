package com.pain.log.painlog.negocio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import static com.pain.log.painlog.negocio.LogUtils.LOGE;
import static com.pain.log.painlog.negocio.LogUtils.LOGI;

/**
 * Created by raul.rodriguezconcep on 27/03/15.
 */
public class DriveActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "BaseDriveActivity";
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    protected GoogleApiClient mGoogleApiClient;
    protected Boolean flag_ini = false;
    protected ProgressDialog dialog;

    @Override
    protected void onResume() {
        super.onResume();
        CreaConexion();
    }


    protected void CreaConexion() {
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



    /**
     * Shows a toast message.
     */
    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LOGI(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());
        flag_ini = true;
        if (!connectionResult.hasResolution()) {
            // show the localized error dialog.

            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();

            return;
        }
        try {
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            LOGE(TAG, "Exception while starting resolution activity");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        } else
            dialog.dismiss();
    }
}



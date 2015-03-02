package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pain.log.painlog.R;
import com.pain.log.painlog.log.ChangeLog;

/**
 * Created by raul.rodriguezconcep on 20/02/15.
 */
public class BaseActivity extends Activity{

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
            case R.id.changelog:
                LanzaChangelog();
                break;
            default:
                onBackPressed();
        }

        return true;
    }

    private void LanzaRate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.signaturemaker.app"));
        startActivity(intent);
    }

    private void LanzaChangelog() {
        ChangeLog changelog = new ChangeLog(this);

        changelog.getFullLogDialog().show();

    }

    public void onRetoreInstanceState(Bundle inState) {
        // TODO Auto-generated method stub

    }
    }




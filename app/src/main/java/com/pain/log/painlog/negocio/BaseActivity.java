package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.pain.log.painlog.R;

/**
 * Created by raul.rodriguezconcep on 20/02/15.
 */
public class BaseActivity extends Activity{


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_diarios, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }




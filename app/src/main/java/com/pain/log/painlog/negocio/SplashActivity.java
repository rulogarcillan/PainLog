package com.pain.log.painlog.negocio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.pain.log.painlog.Constantes.PreferencesCons;
import com.pain.log.painlog.R;
import com.pain.log.painlog.export.BackUp;
import com.pain.log.painlog.export.Ficheros;

import me.relex.circleindicator.CircleIndicator;

public class SplashActivity extends FragmentActivity {
 

    ViewPager pager = null;
    PagerAdapter pagerAdapter;
 
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.splash);

        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Ficheros.path =  prefs.getString(PreferencesCons.RUTAFILE, Ficheros.path);
        BackUp.path =  prefs.getString(PreferencesCons.RUTABACKUP, BackUp.path);


        if (!prefs.getBoolean("first_time", true)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            finish();
        } else{
           SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_time", false);
            editor.commit();
        }

        Ficheros.CreaRuta();
        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.viewpager);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SplashFragment());
        pagerAdapter.addFragment(new SplashFragment());
        pagerAdapter.addFragment(new SplashFragment());
        pagerAdapter.addFragment(new SplashFragment());

        pagerAdapter.getItem(0).setPos(1);

        int i=0;
        for (SplashFragment foo : pagerAdapter.getAllFragment())
        {
            i++;
            foo.setPos(i);
        }



        this.pager.setAdapter(pagerAdapter);
        defaultIndicator.setViewPager(pager);
 
    }
 
    @Override
    public void onBackPressed() {
 

        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
 
    }
 
}
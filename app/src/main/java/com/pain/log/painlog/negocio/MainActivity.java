package com.pain.log.painlog.negocio;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pain.log.painlog.R;

public class MainActivity extends ActionBarActivity {


    DiariosFragment fragment = new DiariosFragment();



        DrawerLayout mDrawerLayout;
        ListView mDrawerList;
        ActionBarDrawerToggle mDrawerToggle;
        String[] mDrawerListItems;

        String NamePhase[] = {"Luna nueva",
                " Creciente",
                " Cuarto creciente",
                " Gibosa creciente",
                " Luna llena",
                " Gibosa menguante",
                " Cuarto menguante",
                " Menguante"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);



            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            mDrawerList = (ListView)findViewById(R.id.navdrawer);
            mDrawerListItems  = NamePhase;
            mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerListItems));
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int editedPosition = position+1;
                    Toast.makeText(MainActivity.this, "You selected item " + editedPosition, Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            });
            mDrawerToggle = new ActionBarDrawerToggle(this,
                    mDrawerLayout,
                    toolbar,
                    R.string.changelog,
                    R.string.app_name){
                public void onDrawerClosed(View v){
                    super.onDrawerClosed(v);
                    invalidateOptionsMenu();
                    syncState();
                }
                public void onDrawerOpened(View v){
                    super.onDrawerOpened(v);
                    invalidateOptionsMenu();
                    syncState();
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            mDrawerToggle.syncState();


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

        @Override
        protected void onPostCreate(Bundle savedInstanceState){
            super.onPostCreate(savedInstanceState);
            mDrawerToggle.syncState();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig){
            super.onConfigurationChanged(newConfig);
            mDrawerToggle.onConfigurationChanged(newConfig);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()){
                case android.R.id.home: {
                    if (mDrawerLayout.isDrawerOpen(mDrawerList)){
                        mDrawerLayout.closeDrawer(mDrawerList);
                    } else {
                        mDrawerLayout.openDrawer(mDrawerList);
                    }
                    return true;
                }
                default: return super.onOptionsItemSelected(item);
            }
        }
    }



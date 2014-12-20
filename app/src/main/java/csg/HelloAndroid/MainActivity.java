package csg.HelloAndroid;

import android.app.Activity;
import android.app.ActionBar;

import android.content.res.Configuration;
import android.os.Bundle;

import android.view.*;
import android.widget.*;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity
{
    private ActionBarDrawerToggle myDrawerToggle;
    private DrawerLayout myDrawerLayout;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerToggle = new ActionBarDrawerToggle(
                    this,                              /* host Activity */
                    myDrawerLayout,                    /* DrawerLayout object */
                    R.drawable.ic_drawer,              /* nav drawer image to replace 'Up' caret */
                    R.string.tempStr1,                 /* "open drawer" description for accessibility */
                    R.string.tempStr2                  /* "close drawer" description for accessibility */
                    ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.tempStr1);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
             public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.tempStr2);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        myDrawerLayout.setDrawerListener(myDrawerToggle);
        /**
         * When using the ActionBarDrawerToggle, you must call it during
         * onPostCreate() and onConfigurationChanged()...
         */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (myDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        myDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        myDrawerToggle.onConfigurationChanged(newConfig);
    }
}


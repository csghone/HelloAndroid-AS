package csg.HelloAndroid;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.view.*;
import android.widget.Toast;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;

public class MainActivity
        extends Activity
        implements MyAdapter.OnItemClickListener,
                   BlankFragment_1.OnFragmentInteractionListener
{
    private ActionBarDrawerToggle myDrawerToggle;
    private DrawerLayout myDrawerLayout;
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private BlankFragment_1 myFragment1;
    private Fragment curFragment;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // update the main content by replacing fragments
        myFragment1 = BlankFragment_1.newInstance(this, "World");
        curFragment = null;


        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);

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

        myRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);

        // specify an adapter (see also next example)
        myAdapter = new MyAdapter(getResources().getStringArray(R.array.drawer_menu), this);
        myRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
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

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
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

    /* The click listener for RecyclerView in the navigation drawer */
    @Override
    public void onClick(View view, int position) {
        CharSequence text = "Hello";
        Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
        getActionBar().setTitle(getResources().getStringArray(R.array.drawer_menu)[position]);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (position)
        {
            case 0:
                if(curFragment != null)
                ft.remove(curFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_activity_content, myFragment1);
                ft.commit();
                curFragment = myFragment1;
                break;
            default:
                break;
        }
        myDrawerLayout.closeDrawer(myRecyclerView);
    }

    /* The OnFragmentInteractionListener for BlankFragment in the navigation drawer */
    @Override
    public void onFragmentInteraction_BlankFragment_1()
    {

    }

}


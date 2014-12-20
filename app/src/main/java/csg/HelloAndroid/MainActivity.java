package csg.HelloAndroid;

import android.app.Activity;
import android.app.ActionBar;

import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.view.*;
import android.widget.*;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends Activity
{
    private ActionBarDrawerToggle myDrawerToggle;
    private DrawerLayout myDrawerLayout;
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
        myAdapter = new MyAdapter(getResources().getStringArray(R.array.drawer_menu));
        myRecyclerView.setAdapter(myAdapter);
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

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            //...
            ViewHolder vh = new ViewHolder((TextView)v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset[position]);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }

    }
}


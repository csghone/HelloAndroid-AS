package csg.HelloAndroid;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import android.widget.Toast;
import android.widget.CheckBox;

import android.app.ActionBar;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("HELLO");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast;

        // Check which checkbox was clicked
        switch (item.getItemId()) {
            case R.id.ActionItem1:
            case R.id.ActionItem2:
                    toast = Toast.makeText(this, "Check", Toast.LENGTH_SHORT);
                    toast.show();
                break;
        }
        return true;
    }    
    
    
}


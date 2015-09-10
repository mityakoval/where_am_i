package com.mityakoval.whereami;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mityakoval.whereami.containers.Location;
import com.mityakoval.whereami.containers.Settings;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Location location;
    Settings settings;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        settings = new Settings();
        loadSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_settings: {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            if(data.hasExtra("latitude")){
                location = new Location();
                location.setLatitude(String.valueOf(data.getExtras().getDouble("latitude")));
                location.setLongitude(String.valueOf(data.getExtras().getDouble("longitude")));
                location.setAltitude(String.valueOf(data.getExtras().getDouble("altitude")));
                location.setCity(data.getExtras().getString("city"));
                location.setCountry(data.getExtras().getString("country"));

                textView.setText("You are here:");
                textView.append("\nCity: " + location.getCity() + ", " + location.getCountry());
                if(settings.isShowLatLong())
                    textView.append("\nYour coordinates: " + location.getLatitude() + ", " + location.getLongitude());
                textView.append("\nYou are " + location.getAltitude() + "m above sea level");
            }
        }
    }

    public void launchMapsActivity(View v){
        Intent i = new Intent(this, MapsActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
    }

    public void loadSettings(){
        settings.loadSettings(this);
    }
}

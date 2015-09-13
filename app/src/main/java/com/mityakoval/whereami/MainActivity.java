package com.mityakoval.whereami;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mityakoval.whereami.containers.Constants;
import com.mityakoval.whereami.containers.Location;
import com.mityakoval.whereami.containers.Settings;
import com.mityakoval.whereami.containers.Weather;
import com.mityakoval.whereami.db.DBHelper;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Location location;
    Settings settings;
    RelativeLayout layout;
    Weather weather;

    private static final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.main_layout);
        layout.setBackgroundColor(getResources().getColor(R.color.clr_pending));
        textView = (TextView) findViewById(R.id.textView);
        settings = new Settings();
        loadSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
        if(location != null)
            updateView();
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

            case R.id.action_history:{
                Intent i = new Intent(this, LocationHistoryActivity.class);
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
                if(data.getExtras().getDouble("altitude") == -1000)
                    location.setAltitude(null);
                else
                    location.setAltitude(String.valueOf(data.getExtras().getDouble("altitude")));
                location.setCity(data.getExtras().getString("city"));
                location.setCountry(data.getExtras().getString("country"));

                if(data.hasExtra("tempC")){
                    weather = new Weather();
                    weather.setTemperatureC(data.getExtras().getInt("tempC"));
                    weather.setTemperatureF(data.getExtras().getInt("tempF"));
                    weather.setWeatherSummary(data.getExtras().getString("weatherSum"));
                    weather.setWindStrength(data.getExtras().getString("wind"));
                }

                saveData(location, weather);

                updateView();
                ColorDrawable [] colors = {new ColorDrawable(ContextCompat.getColor(this, R.color.clr_pending)),
                                    new ColorDrawable(ContextCompat.getColor(this, R.color.clr_location_found))};
                TransitionDrawable transition = new TransitionDrawable(colors);
                layout.setBackground(transition);
                transition.startTransition(2000);
            }
        }
    }

    public void launchMapsActivity(View v){
        Intent i = new Intent(this, MapsActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void updateView(){
        textView.setText("You are here:");
        textView.append("\nCity: " + location.getCity() + ", " + location.getCountry());
        if(settings.isShowDetails()) {
            textView.append("\nCoordinates: " + location.getLatitude() + ", " + location.getLongitude());
            if (location.getAltitude() != null)
                textView.append("\n" + location.getAltitude() + "m above sea level");
            else
                textView.append("\nAltitude is not available due to GPS problems");
        }

        if(weather != null) {
            textView.append("\n\nCurrently it is " + weather.getWeatherSummary() + " in " + location.getCity());
            if(settings.isShowCelsius())
                textView.append("\nTemperature is " + weather.getTemperatureC() + "C");
            else
                textView.append("\nTemperature is " + weather.getTemperatureF() + "F");
            textView.append("\nWind speed is about " + weather.getWindStrength() + "kmph");
        }

    }

    public void loadSettings(){
        settings.loadSettings(this);
    }

    public void saveData(Location location, Weather weather){
        DBHelper dbHelper = new DBHelper(this);

        if(location != null){
            dbHelper.addLocation(location);
        }

        if (weather != null){
            dbHelper.addWeather(weather);
        }
    }
}

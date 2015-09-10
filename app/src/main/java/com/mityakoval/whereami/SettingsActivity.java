package com.mityakoval.whereami;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mityakoval.whereami.containers.Location;
import com.mityakoval.whereami.containers.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton celsiusRadio;
    private RadioButton fahrenheitRadio;
    private ToggleButton latLongToggle;
    private Button saveButton;
    private Button cancelButton;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupTemp);
        celsiusRadio = (RadioButton) findViewById(R.id.radioButtonCelsius);
        fahrenheitRadio = (RadioButton) findViewById(R.id.radioButtonFahrenheit);
        latLongToggle = (ToggleButton) findViewById(R.id.toggleButtonLatLong);
        saveButton = (Button) findViewById(R.id.buttonSave);
        cancelButton = (Button) findViewById(R.id.buttonCancel);
        settings = new Settings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void saveSettings(View v) {
        if(latLongToggle.isChecked())
            settings.setShowLatLong(true);
        else
            settings.setShowLatLong(false);

        if(radioGroup.getCheckedRadioButtonId() == celsiusRadio.getId()){
            settings.setShowCelsius(true);
            settings.setShowFahrenheit(false);
        } else {
            settings.setShowCelsius(false);
            settings.setShowFahrenheit(true);
        }

        try {
            settings.saveSettings(this);
        } catch (IOException e) {
            Log.e("SETTINGS", e.getLocalizedMessage());
        }
        finish();
    }

    public void dismiss(View v){
        finish();
    }

}

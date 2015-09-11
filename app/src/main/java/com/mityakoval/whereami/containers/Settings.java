package com.mityakoval.whereami.containers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mityakoval on 10/09/15.
 */
public class Settings {

    private boolean showLatLong;
    private boolean showCelsius;
    private boolean showFahrenheit;
    public static final String FILE_NAME = "settings.json";
    public static final String SHOW_LAT_LONG_KEY = "showLatLong";
    public static final String SHOW_CELSIUS_KEY = "showCelsius";
    public static final String SHOW_FAHRENHEIT_KEY = "showFahrenheit";

    public Settings() {
        this.showLatLong = true;
        this.showCelsius = true;
        this.showFahrenheit = false;
    }

    public boolean isShowDetails() {
        return showLatLong;
    }

    public void setShowDetails(boolean showLatLong) {
        this.showLatLong = showLatLong;
    }

    public boolean isShowCelsius() {
        return showCelsius;
    }

    public void setShowCelsius(boolean showCelsius) {
        this.showCelsius = showCelsius;
    }

    public boolean isShowFahrenheit() {
        return showFahrenheit;
    }

    public void setShowFahrenheit(boolean showFahrenheit) {
        this.showFahrenheit = showFahrenheit;
    }

    public void saveSettings(Context context) throws IOException{
        FileOutputStream fout = null;
        JSONObject settingsJSON = new JSONObject();

        try{
            settingsJSON.put(SHOW_LAT_LONG_KEY, showLatLong);
            settingsJSON.put(SHOW_CELSIUS_KEY, showCelsius);
            settingsJSON.put(SHOW_FAHRENHEIT_KEY, showFahrenheit);
        } catch (JSONException e) {
            Toast.makeText(context, "Unable to add setting! " + e.getLocalizedMessage(), Toast.LENGTH_LONG)
                    .show();
            Log.e("SETTINGS/JSON", e.getLocalizedMessage());
        }

        try{
            fout = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fout.write(settingsJSON.toString().getBytes());
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Unable to save settings! No such file!", Toast.LENGTH_LONG)
                    .show();
            Log.e("SETTINGS/FILE", e.getLocalizedMessage());
        } catch (IOException e) {
            Toast.makeText(context, "Unable to save settings!", Toast.LENGTH_LONG)
                    .show();
            Log.e("SETTINGS/FILE", e.getLocalizedMessage());
        } finally {
            if(fout != null)
                fout.close();
        }
    }

    public void loadSettings(Context context){
        File settingsFile = new File(context.getFilesDir(), FILE_NAME);
        BufferedReader br = null;
        JSONObject settingsJSON;
        StringBuilder text = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(settingsFile));
            String line;

            while((line = br.readLine()) != null){
                text.append(line);
                text.append('\n');
            }
            br.close();

            settingsJSON = new JSONObject(text.toString());
            this.showLatLong = settingsJSON.getBoolean(SHOW_LAT_LONG_KEY);
            this.showCelsius = settingsJSON.getBoolean(SHOW_CELSIUS_KEY);
            this.showFahrenheit = settingsJSON.getBoolean(SHOW_FAHRENHEIT_KEY);
        } catch (FileNotFoundException e) {
            Log.i("SETTINGS", "No settings file was found. Default settings were loaded.");
        } catch (IOException e) {
            Toast.makeText(context, "Unable to load custom settings! Loading defaults...", Toast.LENGTH_LONG)
                    .show();
            Log.e("SETTINGS/FILE", "Unable to load settings from a file! " + e.getLocalizedMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteSettingsFile(Context context){
        context.deleteFile(FILE_NAME);
    }
}

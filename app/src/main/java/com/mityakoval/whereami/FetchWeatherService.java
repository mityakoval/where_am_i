package com.mityakoval.whereami;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.mityakoval.whereami.containers.Constants;
import com.mityakoval.whereami.containers.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mityakoval on 11/09/15.
 */
public class FetchWeatherService extends IntentService {

    public static final String ERROR_TAG = "WEATHER SERVICE";
    public static final String WEATHER_API_KEY = "cb518c933d78f2d36b2d9053b54cc";

    private ResultReceiver resultReceiver;

    public FetchWeatherService(String name) {
        super(name);
    }

    public FetchWeatherService(){
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        JSONObject jsonObject;
        Weather weatherInfo = null;

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        String weatherURI = "http://api.worldweatheronline.com/free/v2/weather.ashx?q=" +
                location.getLatitude() + "%2C" + location.getLongitude() + "&format=json&key=" + WEATHER_API_KEY;
        try {
            URL url = new URL(weatherURI);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            jsonObject = new JSONObject(readStream(urlConnection.getInputStream()));

            weatherInfo = new Weather();
            JSONObject currentCondition = jsonObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0);

            weatherInfo.setTemperatureC(Integer.parseInt(currentCondition.getString("temp_C")));
            weatherInfo.setTemperatureF(Integer.parseInt(currentCondition.getString("temp_F")));
            weatherInfo.setWeatherSummary(currentCondition.getJSONArray("weatherDesc").getJSONObject(0).getString("value"));
            weatherInfo.setWindStrength(currentCondition.getString("windspeedKmph"));

        } catch (IOException | JSONException e) {
            Log.e(ERROR_TAG, e.getLocalizedMessage());
        }

        if(weatherInfo != null)
            deliverResults(Constants.SUCCESS_CODE, "Weather was fetched successfully", weatherInfo);
        else
            deliverResults(Constants.FAILURE_CODE, "Weather was not fetched", null);
    }

    public void deliverResults(int resultCode, String message, Weather weatherInfo){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        bundle.putInt("tempC", weatherInfo.getTemperatureC());
        bundle.putInt("tempF", weatherInfo.getTemperatureF());
        bundle.putString("weatherSum", weatherInfo.getWeatherSummary());
        bundle.putString("wind", weatherInfo.getWindStrength());
        resultReceiver.send(resultCode, bundle);
    }

    public String readStream(InputStream is){
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String nextLine = "";

            while((nextLine = br.readLine()) != null){
                stringBuilder.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}

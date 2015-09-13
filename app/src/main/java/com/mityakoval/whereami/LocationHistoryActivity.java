package com.mityakoval.whereami;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mityakoval.whereami.containers.Location;
import com.mityakoval.whereami.containers.Weather;
import com.mityakoval.whereami.db.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LocationHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);

        HistoryViewAdapter adapter = new HistoryViewAdapter();

        ListView listView = (ListView) findViewById(R.id.history_list);

        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_hictory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class HistoryViewAdapter extends BaseAdapter {

        private ArrayList<Location> locationData;
        private ArrayList<Weather> weatherData;
        private ArrayList<HistoryData> historyData;

        public HistoryViewAdapter() {
            super();
            DBHelper dbHelper = new DBHelper(LocationHistoryActivity.this);
            try {
                locationData = dbHelper.getAllLocations();
                weatherData = dbHelper.getAllWeather();
                historyData = getHistoryData();
            } catch (Exception e) {
                Toast.makeText(LocationHistoryActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public int getCount() {
            if(historyData != null)
                return historyData.size();
            else
                return 0;
        }

        @Override
        public HistoryData getItem(int position) {
            if(historyData != null)
                return historyData.get(position);
            else
                return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) LocationHistoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
            }

            TextView date = (TextView) convertView.findViewById(R.id.dateTextView);
            TextView city = (TextView) convertView.findViewById(R.id.citycountryTextView);
            TextView weather = (TextView) convertView.findViewById(R.id.weatherTextView);

            HistoryData historyItem = historyData.get(position);
            date.setText(historyItem.getTodayDate());
            city.setText(historyItem.getCity() + ", " + historyItem.getCountry());
            weather.setText(historyItem.getWeatherSummary() + " - " + historyItem.getTempC() + "C (" +
                historyItem.getTempF() + "F)");

            return convertView;
        }

        public ArrayList<HistoryData> getHistoryData(){
            ArrayList<HistoryData> historyData = null;

            if(locationData != null){
                historyData = new ArrayList<>();
                for (int i=0; i < locationData.size(); i++) {
                    HistoryData historyDataItem = new HistoryData();
                    historyDataItem.setCity(locationData.get(i).getCity());
                    historyDataItem.setCountry(locationData.get(i).getCountry());

                    if (weatherData != null) {
                        historyDataItem.setWeatherSummary(weatherData.get(i).getWeatherSummary());
                        historyDataItem.setTempC(String.valueOf(weatherData.get(i).getTemperatureC()));
                        historyDataItem.setTempF(String.valueOf(weatherData.get(i).getTemperatureF()));
                    }


                    historyDataItem.setTodayDate(locationData.get(i).getDate());
                    historyData.add(historyDataItem);
                }
            }

            return historyData;
        }

        private class HistoryData{
            String todayDate;
            String city;
            String country;
            String weatherSummary;
            String tempC;
            String tempF;

            public String getTodayDate() {
                return todayDate;
            }

            public void setTodayDate(String todayDate) {
                this.todayDate = todayDate;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getWeatherSummary() {
                return weatherSummary;
            }

            public void setWeatherSummary(String weatherSummary) {
                this.weatherSummary = weatherSummary;
            }

            public String getTempC() {
                return tempC;
            }

            public void setTempC(String tempC) {
                this.tempC = tempC;
            }

            public String getTempF() {
                return tempF;
            }

            public void setTempF(String tempF) {
                this.tempF = tempF;
            }
        }
    }
}

package com.mityakoval.whereami.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mityakoval.whereami.containers.Location;
import com.mityakoval.whereami.containers.Weather;

import java.util.ArrayList;

/**
 * Created by mityakoval on 05/09/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "where_am_i.db";
    SQLiteDatabase writableDb = getWritableDatabase();
    SQLiteDatabase readableDb = getReadableDatabase();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataContract.Location.SQL_CREATE);
        db.execSQL(DataContract.Weather.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataContract.Location.SQL_DROP);
        db.execSQL(DataContract.Weather.SQL_DROP);
        onCreate(db);
    }

    public void drop(SQLiteDatabase db){
        db.execSQL(DataContract.Location.SQL_DROP);
        db.execSQL(DataContract.Weather.SQL_DROP);
    }

    public long addLocation(Location location){
        ContentValues values = new ContentValues();

        values.put(DataContract.Location.COLUMN_NAME_LATITUDE, location.getLatitude());
        values.put(DataContract.Location.COLUMN_NAME_LONGITUDE, location.getLongitude());
        values.put(DataContract.Location.COLUMN_NAME_ALTITUDE, location.getAltitude());
        values.put(DataContract.Location.COLUMN_NAME_CITY, location.getCity());
        values.put(DataContract.Location.COLUMN_NAME_COUNTRY, location.getCountry());

        return writableDb.insert(DataContract.Location.TABLE_NAME, null, values);
    }

    public ArrayList<Location> getAllLocations(){
        ArrayList<Location> locations = null;
        Location location;

        String [] projection = {DataContract.Location._ID,
                                 DataContract.Location.COLUMN_NAME_LATITUDE,
                                 DataContract.Location.COLUMN_NAME_LONGITUDE,
                                 DataContract.Location.COLUMN_NAME_ALTITUDE,
                                 DataContract.Location.COLUMN_NAME_CITY,
                                 DataContract.Location.COLUMN_NAME_COUNTRY};

        Cursor c = readableDb.query(DataContract.Location.TABLE_NAME, projection, null, null, null, null, null);

        if(c == null || c.getCount() <= 0)
            return null;

        c.moveToFirst();

        locations = new ArrayList<>();

        do {
            location = new Location();

            location.setLatitude(c.getString(c.getColumnIndexOrThrow(DataContract.Location.COLUMN_NAME_LATITUDE)));
            location.setLongitude(c.getString(c.getColumnIndexOrThrow(DataContract.Location.COLUMN_NAME_LONGITUDE)));
            location.setAltitude(c.getString(c.getColumnIndexOrThrow(DataContract.Location.COLUMN_NAME_ALTITUDE)));
            location.setCity(c.getString(c.getColumnIndexOrThrow(DataContract.Location.COLUMN_NAME_CITY)));
            location.setCountry(c.getString(c.getColumnIndexOrThrow(DataContract.Location.COLUMN_NAME_COUNTRY)));

            locations.add(location);
        } while (c.moveToNext());

        c.close();

        return locations;
    }

    public long addWeather(Weather weather){
        ContentValues values = new ContentValues();

        values.put(DataContract.Weather.COLUMN_NAME_TEMP_F, weather.getTemperatureF());
        values.put(DataContract.Weather.COLUMN_NAME_TEMP_C, weather.getTemperatureC());
        values.put(DataContract.Weather.COLUMN_NAME_WEATHER_SUMMARY, weather.getWeatherSummary());
        values.put(DataContract.Weather.COLUMN_NAME_WIND, weather.getWindStrength());

        return writableDb.insert(DataContract.Weather.TABLE_NAME, null, values);
    }

    public ArrayList<Weather> getAllWeather(){
        ArrayList<Weather> allWeather = null;
        Weather weatherItem;

        String [] projection = {DataContract.Location._ID,
                DataContract.Weather.COLUMN_NAME_TEMP_F,
                DataContract.Weather.COLUMN_NAME_TEMP_C,
                DataContract.Weather.COLUMN_NAME_WEATHER_SUMMARY,
                DataContract.Weather.COLUMN_NAME_WIND};

        Cursor c = readableDb.query(DataContract.Weather.TABLE_NAME, projection, null, null, null, null, null);

        if(c == null || c.getCount() <= 0)
            return null;

        c.moveToFirst();

        allWeather = new ArrayList<>();

        do {
            weatherItem = new Weather();

            weatherItem.setTemperatureF(c.getInt(c.getColumnIndexOrThrow(DataContract.Weather.COLUMN_NAME_TEMP_F)));
            weatherItem.setTemperatureC(c.getInt(c.getColumnIndexOrThrow(DataContract.Weather.COLUMN_NAME_TEMP_C)));
            weatherItem.setWeatherSummary(c.getString(c.getColumnIndexOrThrow(DataContract.Weather.COLUMN_NAME_WEATHER_SUMMARY)));
            weatherItem.setWindStrength(c.getString(c.getColumnIndexOrThrow(DataContract.Weather.COLUMN_NAME_WIND)));

            allWeather.add(weatherItem);

        } while (c.moveToNext());

        c.close();

        return allWeather;
    }
}

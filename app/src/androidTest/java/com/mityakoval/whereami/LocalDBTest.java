package com.mityakoval.whereami;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;
import android.util.Log;

import com.mityakoval.whereami.containers.Location;
import com.mityakoval.whereami.containers.Weather;
import com.mityakoval.whereami.db.*;

import java.util.ArrayList;

/**
 * Created by mityakoval on 06/09/15.
 */
public class LocalDBTest extends AndroidTestCase{

    private Location location1 = null;
    private Location location2 = null;
    private Weather weather1 = null;
    private Weather weather2 = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        DBHelper dbHelper = new DBHelper(mContext);

        location1 = new Location();
        location1.setLatitude("60.8789");
        location1.setLongitude("10.5219");
        location1.setAltitude("127");
        location1.setCity("Gjøvik");
        location1.setCountry("Norway");

        location2 = new Location();
        location2.setLatitude("50.4500");
        location2.setLongitude("30.5233");
        location2.setAltitude("179");
        location2.setCity("Kiev");
        location2.setCountry("Ukraine");

        weather1 = new Weather();
        weather1.setTemperatureC(13);
        weather1.setTemperatureF(56);
        weather1.setWeatherSummary("Sunny");
        weather1.setWindStrength("4");

        weather2 = new Weather();
        weather2.setTemperatureC(15);
        weather2.setTemperatureF(59);
        weather2.setWeatherSummary("Light rain showers");
        weather2.setWindStrength("6");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

    }

    public void testCreateDB(){
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
        Log.d("TEST", "DB was opened successfully");
    }

    public void testAddLocation() throws Exception{
        DBHelper dbHelper = new DBHelper(mContext);

        long id = dbHelper.addLocation(location1);
        assertTrue(id != -1);
        Log.d("TEST", "First location record added successfully");

        id = dbHelper.addLocation(location2);
        assertTrue(id != -1);
        Log.d("TEST", "Second location record added successfully");
    }

    public void testAddWeather() throws Exception {
        DBHelper dbHelper = new DBHelper(mContext);

        long id = dbHelper.addWeather(weather1);
        assertTrue(id != -1);
        Log.d("TEST", "First weather record added successfully");

        id = dbHelper.addWeather(weather2);
        assertTrue(id != -1);
        Log.d("TEST", "Second weather record added successfully");
    }

    public void testGetAllLocations() throws Exception {
        DBHelper dbHelper = new DBHelper(mContext);

        ArrayList<Location> retrievedLocations = dbHelper.getAllLocations();

        assertTrue(location1.equals(retrievedLocations.get(0)));
        Log.d("TEST", "First location retrieved successfully");

        assertEquals(location2, retrievedLocations.get(1));
        Log.d("TEST", "Second location retrieved successfully");
    }

    public void testGetAllWeather() throws Exception {
        DBHelper dbHelper = new DBHelper(mContext);

        ArrayList<Weather> retrievedLocations = dbHelper.getAllWeather();

        assertEquals(weather1, retrievedLocations.get(0));
        Log.d("TEST", "First weather record retrieved successfully");

        assertEquals(weather2, retrievedLocations.get(1));
        Log.d("TEST", "Second weather record retrieved successfully");
    }
}

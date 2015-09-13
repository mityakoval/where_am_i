package com.mityakoval.whereami.db;

import android.provider.BaseColumns;

/**
 * Created by mityakoval on 05/09/15.
 */
public class DataContract {
    public static String TEXT_TYPE = " TEXT ";
    public static String INT_TYPE = " INTEGER ";
    public static String COMMA_SEP = ", ";

    public static abstract class Location implements BaseColumns{
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_ALTITUDE = "altitude";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_DATE = "date";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY NOT NULL," +
                COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_ALTITUDE + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP+
                COLUMN_NAME_DATE + TEXT_TYPE + ");";

        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Weather implements BaseColumns{
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_NAME_TEMP_F = "temp_Fahr";
        public static final String COLUMN_NAME_TEMP_C = "temp_Cels";
        public static final String COLUMN_NAME_WEATHER_SUMMARY = "summary";
        public static final String COLUMN_NAME_WIND = "wind";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY NOT NULL," +
                COLUMN_NAME_TEMP_F + INT_TYPE + COMMA_SEP +
                COLUMN_NAME_TEMP_C + INT_TYPE + COMMA_SEP +
                COLUMN_NAME_WEATHER_SUMMARY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_WIND + TEXT_TYPE + ");";

        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

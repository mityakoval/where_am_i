package com.mityakoval.whereami.containers;

/**
 * Created by mityakoval on 05/09/15.
 */
public class Location {
    private String longitude;
    private String latitude;
    private String altitude;
    private String city;
    private String country;
    private String date;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;

        Location compared = (Location) o;

        return (longitude.equals(compared.getLongitude())) && (latitude.equals(compared.getLatitude())) &&
                (altitude.equals(compared.getAltitude())) && (city.equals(compared.getCity()))
                && (country.equals(compared.getCountry()));
    }
}

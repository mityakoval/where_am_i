package com.mityakoval.whereami.containers;

/**
 * Created by mityakoval on 05/09/15.
 */
public class Weather {
    private int temperatureF;
    private int temperatureC;
    private String weatherSummary;
    private String windStrength;

    public int getTemperatureF() {
        return temperatureF;
    }

    public void setTemperatureF(int temperatureF) {
        this.temperatureF = temperatureF;
    }

    public int getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(int temperatureC) {
        this.temperatureC = temperatureC;
    }

    public String getWeatherSummary() {
        return weatherSummary;
    }

    public void setWeatherSummary(String weatherSummary) {
        this.weatherSummary = weatherSummary;
    }

    public String getWindStrength() {
        return windStrength;
    }

    public void setWindStrength(String windStrength) {
        this.windStrength = windStrength;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;

        Weather compared = (Weather) o;

        return (temperatureC == ((Weather) o).getTemperatureC()) && (temperatureF == ((Weather) o).getTemperatureF()) &&
                (weatherSummary.equals(((Weather) o).getWeatherSummary())) && (windStrength.equals(((Weather) o).getWindStrength()));
    }
}

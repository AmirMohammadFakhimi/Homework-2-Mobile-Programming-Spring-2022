package edu.sharif.weather;

import androidx.room.Entity;

import java.util.ArrayList;

@Entity(primaryKeys = {"longitude", "latitude"})
public class Weather {
    private static ArrayList<Weather> allWeather = new ArrayList<>();

    private final double longitude;
    private final double latitude;
    private String city = null;
    private String lastUpdatedTime;
//    private double temperature;
//    private double feelsLike;
//    private double minTemperature;
//    private double maxTemperature;
//    private double pressure;
//    private double humidity;
    private String temp;

    public Weather(double longitude, double latitude, String lastUpdatedTime) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.lastUpdatedTime = lastUpdatedTime;
        allWeather.add(this);
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return temp;
    }
}

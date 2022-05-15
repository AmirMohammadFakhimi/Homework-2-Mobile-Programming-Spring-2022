package edu.sharif.weather;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity(primaryKeys = {"longitude", "latitude"})
public class City {
    private String name;
    private final double latitude;
    private final double longitude;
    private LocalDateTime lastUpdatedTime;
    private ArrayList<Weather> weatherForecasts;

    {
        weatherForecasts = new ArrayList<>();
    }

    public City(String name, double latitude, double longitude, LocalDateTime lastUpdatedTime) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public ArrayList<Weather> getWeatherForecasts() {
        return weatherForecasts;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setWeatherForecasts(ArrayList<Weather> weatherForecasts) {
        this.weatherForecasts = weatherForecasts;
        lastUpdatedTime = LocalDateTime.now();
    }

    public void addWeatherForecast(Weather weather, int day) {
        weatherForecasts.add(day, weather);
    }

    @NonNull
    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", lastUpdatedTime=" + lastUpdatedTime +
                ", weatherForecasts=" + weatherForecasts +
                '}';

    }
}

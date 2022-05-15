package edu.sharif.weather;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"longitude", "latitude"})
public class Weather {
    private double currentTemperature;
    private double minTemperature;
    private double maxTemperature;
    private double feelsLike;
    private double pressure;
    private double humidity;

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    @NonNull
    @Override
    public String toString() {
        return "Weather{" +
                "currentTemperature=" + currentTemperature +
                ", feelsLike=" + feelsLike +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}

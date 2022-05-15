package edu.sharif.weather;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"longitude", "latitude"})
public class Weather {
    private double temperature;
    private double minTemperature;
    private double maxTemperature;
    private double feelsLike;
    private double pressure;
    private double humidity;
    private String icon;
    private String description;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Weather{" +
                "temperature=" + temperature +
                ", feelsLike=" + feelsLike +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}

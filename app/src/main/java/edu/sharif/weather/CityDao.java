package edu.sharif.weather;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Dao
public interface CityDao {
    @Insert
    void insert(City city);

    @Query("UPDATE city SET name = :name WHERE latitude = :latitude AND longitude = :longitude")
    void updateName(double latitude, double longitude, String name);

    @Query("UPDATE city SET weatherForecasts = :weatherForecasts, lastUpdatedTime = :lastUpdatedTime " +
            "WHERE latitude = :latitude AND longitude = :longitude")
    void updateWeatherForecasts(double latitude, double longitude,
                                ArrayList<Weather> weatherForecasts, LocalDateTime lastUpdatedTime);

    @Query("SELECT * FROM city WHERE latitude = :latitude AND longitude = :longitude")
    LiveData<City> getCity(double latitude, double longitude);

    @Query("SELECT * FROM city WHERE name = :name")
    LiveData<City> getCity(String name);

    @Query("DELETE FROM city")
    void deleteTable();

    @Query("SELECT COUNT() FROM city WHERE latitude = :latitude AND longitude = :longitude")
    int count(double latitude, double longitude);
}

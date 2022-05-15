package edu.sharif.weather;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface CityDao {
    @Insert
    void insert(City city);

    @Query("UPDATE city SET name=:name WHERE latitude = :latitude AND longitude = :longitude")
    void updateName(double latitude, double longitude, String name);

    @Query("UPDATE city SET weatherForecasts=:weatherForecasts, lastUpdatedTime=:lastUpdatedTime " +
            "WHERE latitude = :latitude AND longitude = :longitude")
    void updateWeatherForecasts(double latitude, double longitude,
                                ArrayList<Weather> weatherForecasts, LocalDateTime lastUpdatedTime);

    @Query("SELECT * FROM city WHERE latitude = :latitude AND longitude = :longitude")
    LiveData<City> getCity(double latitude, double longitude);

    @Query("DELETE FROM city")
    public void deleteTable();

    @Query("SELECT COUNT() FROM city WHERE latitude = :latitude AND longitude = :longitude")
    public int count(double latitude, double longitude);
}

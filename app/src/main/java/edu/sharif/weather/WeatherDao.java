package edu.sharif.weather;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WeatherDao {
    @Insert
    void insert(Weather weather);

    @Update
    void updateWeather(Weather weather);

    @Delete
    void delete(Weather weather);

    @Query("SELECT * FROM weather WHERE longitude = :longitude AND latitude = :latitude")
    LiveData<Weather> getWeather(double longitude, double latitude);

    @Query("DELETE FROM weather")
    public void nukeTable();

//    @Query("SELECT * FROM weather")
//    List<Weather> getAllWeather();
}

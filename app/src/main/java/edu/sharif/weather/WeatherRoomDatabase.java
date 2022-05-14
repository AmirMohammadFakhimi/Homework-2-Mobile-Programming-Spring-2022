package edu.sharif.weather;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Weather.class}, version = 1)
public abstract class WeatherRoomDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();

    private static WeatherRoomDatabase instance;

    public static WeatherRoomDatabase getInstance(Context context) {
        if (instance != null)
            return instance;

        synchronized (WeatherRoomDatabase.class) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, WeatherRoomDatabase.class, "weather_database")
                        .fallbackToDestructiveMigration().build();
            }
            return instance;
        }
    }
}

package edu.sharif.weather;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {City.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class CityRoomDatabase extends RoomDatabase {
    public abstract CityDao weatherDao();

    private static CityRoomDatabase instance;

    public static CityRoomDatabase getInstance(Context context) {
        if (instance != null)
            return instance;

        synchronized (CityRoomDatabase.class) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, CityRoomDatabase.class, "city_database")
                        .fallbackToDestructiveMigration().build();
            }
            return instance;
        }
    }
}

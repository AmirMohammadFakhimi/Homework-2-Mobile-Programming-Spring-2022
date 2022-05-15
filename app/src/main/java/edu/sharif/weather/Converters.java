package edu.sharif.weather;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Converters {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime fromTimestamp(String dateTime) {
        return dateTime == null ? null : LocalDateTime.parse(dateTime);
    }

    @TypeConverter
    public static String dateToTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toString();
    }

    @TypeConverter
    public static ArrayList<Weather> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Weather>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Weather> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}

package edu.sharif.weather;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;

public class WeatherRepository {
    private static final String apiKey = "ee4afd8eedb319ceec2ef7e168cc79aa";

    private List<Weather> allWeathers;
    private WeatherDao weatherDao;
    private LiveData<Weather> currentWeather;

    public WeatherRepository(@NonNull Application application) {
        WeatherRoomDatabase database =
                WeatherRoomDatabase.getInstance(application);

        weatherDao = database.weatherDao();
        new Thread(() -> {
            weatherDao.nukeTable();
        }).start();
//        allWeathers = weatherDao.getAllWeather();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    public void getCityWeather(Context context, double longitude, double latitude) throws Exception {
        try {
//            InetAddress inetAddress = InetAddress.getByName("google.com");

            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://api.openweathermap.org/data/2.5/weather?lat="
                    + latitude + "&lon=" + longitude + "&appid=" + apiKey;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {

                try {
                    JSONObject weatherString = new JSONObject(response);
                    Weather weather = new Weather(longitude, latitude, LocalDateTime.now().toString());
                    weather.setTemp(weatherString.get("main").toString());
                    new Thread(() -> {
                        weatherDao.insert(weather);
                    }).start();
//                    setCurrentWeather(longitude, latitude);
//                    weatherDao.updateWeather(weather);
//                    setCurrentWeather(longitude, latitude);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                }, error -> Log.d("asd", "That didn't work!"));

// Add the request to the RequestQueue.
            queue.add(stringRequest);
            queue.start();
        } catch (Exception e) {
            throw new Exception("No Internet Connection");
        }
    }

    public void setCurrentWeather(double longitude, double latitude) {
        this.currentWeather = weatherDao.getWeather(longitude, latitude);
    }

    public LiveData<Weather> getCurrentWeather() {
        return currentWeather;
    }
}

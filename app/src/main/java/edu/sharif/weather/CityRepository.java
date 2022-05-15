package edu.sharif.weather;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CityRepository {
    private CityDao cityDao;
    private LiveData<City> city;
    private City tempCity;

    public CityRepository(@NonNull Application application) {
        CityRoomDatabase database =
                edu.sharif.weather.CityRoomDatabase.getInstance(application);

        cityDao = database.weatherDao();
        Thread thread = new Thread(() -> cityDao.deleteTable());
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    synchronized public LiveData<City> getCityInfo(Context context, double latitude, double longitude) {
        tempCity = new City(null, latitude, longitude, LocalDateTime.now());
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest cityNameRequest = getCityName(context, latitude, longitude);
        StringRequest cityWeatherForecastsRequest = getCityWeatherForecasts(context, latitude, longitude);

        queue.add(cityNameRequest);
        queue.add(cityWeatherForecastsRequest);

        city = cityDao.getCity(latitude, longitude);
        return city;
    }

    public StringRequest getCityName(Context context, double latitude, double longitude) {
        String cityApiUrl = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + longitude +
                "," + latitude + ".json?access_token=" + BuildConfig.MAP_BOX_API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, cityApiUrl, response -> {
            try {
                JSONObject cityInfoString = new JSONObject(response);
                JSONArray cityJsonFeatures = cityInfoString.getJSONArray("features");
                if (cityJsonFeatures.length() == 0)
                    return;

                JSONArray cityJsonDetails = cityJsonFeatures.getJSONObject(0).getJSONArray("context");
                String cityName = null;
                for (int i = 0; i < cityJsonDetails.length(); i++) {
                    JSONObject cityJsonDetail = cityJsonDetails.getJSONObject(i);
                    if (cityJsonDetail.getString("id").startsWith("place")) {
                        cityName = cityJsonDetail.getString("text");
                    }
                }

                if (cityName == null)
                    return;

                tempCity.setName(cityName);
                String finalCityName = cityName;
                Thread thread = new Thread(() -> {
                    boolean isCacheAvailable = cityDao.count(latitude, longitude) > 0;

                    if (isCacheAvailable)
                        cityDao.updateName(latitude, longitude, finalCityName);
                    else
                        cityDao.insert(tempCity);
                });
                thread.join();
                thread.start();
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(context, "Unexpected Error!\n" +
                        response, Toast.LENGTH_LONG).show();
            }

        },
                error -> Toast.makeText(context, "There was a problem with the connection." + error.toString(),
                        Toast.LENGTH_LONG).show());

        return stringRequest;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private StringRequest getCityWeatherForecasts(Context context, double latitude, double longitude) {
        String weatherApiUrl = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude +
                "&lon=" + longitude + "&appid=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;

        StringRequest stringRequest =  new StringRequest(Request.Method.GET, weatherApiUrl, response -> {
            try {
                JSONObject weatherString = new JSONObject(response);
                JSONArray forecasts = weatherString.getJSONArray("daily");

                ArrayList<Weather> weathers = new ArrayList<>();
                for (int i = 0; i < forecasts.length(); i++) {
                    Weather weather = new Weather();
                    JSONObject forecast = forecasts.getJSONObject(i);
                    weather.setCurrentTemperature(forecast.getJSONObject("temp").getDouble("day"));
                    weather.setMinTemperature(forecast.getJSONObject("temp").getDouble("min"));
                    weather.setMaxTemperature(forecast.getJSONObject("temp").getDouble("max"));
                    weather.setFeelsLike(forecast.getJSONObject("feels_like").getDouble("day"));
                    weather.setPressure(forecast.getDouble("pressure"));
                    weather.setHumidity(forecast.getDouble("humidity"));

                    weathers.add(weather);
                }

                tempCity.setWeatherForecasts(weathers);
                Thread thread = new Thread(() -> {
                    boolean isCacheAvailable = cityDao.count(latitude, longitude) > 0;

                    if (isCacheAvailable)
                        cityDao.updateWeatherForecasts(latitude, longitude, weathers, LocalDateTime.now());
                    else
                        cityDao.insert(tempCity);
                });
                thread.join();
                thread.start();
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(context, "Unexpected Error!\n" +
                        response, Toast.LENGTH_LONG).show();
            }

        },
                error -> Toast.makeText(context, "There was a problem with the connection." + error.toString(),
                        Toast.LENGTH_LONG).show());

        return stringRequest;
    }

    public LiveData<City> getCity() {
        return city;
    }
}

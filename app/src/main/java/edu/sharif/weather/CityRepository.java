package edu.sharif.weather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CityRepository {
    private final CityDao cityDao;
    private LiveData<City> city;
    private City tempCity;

    public CityRepository(@NonNull Application application) {
        CityRoomDatabase database =
                edu.sharif.weather.CityRoomDatabase.getInstance(application);

        cityDao = database.weatherDao();

//        UNCOMMENT TO CLEAR DATA IN DATABASE
//        Thread thread = new Thread(cityDao::deleteTable);
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    synchronized public LiveData<City> getCityInfo(View view, double latitude, double longitude) {
        tempCity = new City(null, latitude, longitude, LocalDateTime.now());
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringRequest cityNameRequest = getCityName(view, latitude, longitude);
        StringRequest cityWeatherForecastsRequest = getCityWeatherForecasts(view, latitude, longitude);

        queue.add(cityNameRequest);
        queue.add(cityWeatherForecastsRequest);

        city = cityDao.getCity(latitude, longitude);
        return city;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    synchronized public LiveData<City> getCityInfo(View view, String cityName) {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringRequest getLatitudeAndLongitude = getCityLatAndLong(view, cityName);
        queue.add(getLatitudeAndLongitude);
        queue.start();

        StringRequest cityWeatherForecastsRequest = getCityWeatherForecasts(view,
                tempCity.getLatitude(), tempCity.getLongitude());
        queue.add(cityWeatherForecastsRequest);
        queue.start();

        city = cityDao.getCity(tempCity.getLatitude(), tempCity.getLongitude());
        return city;
    }

    public StringRequest getCityName(View view, double latitude, double longitude) {
        String cityApiUrl = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + longitude +
                "," + latitude + ".json?access_token=" + BuildConfig.MAP_BOX_API_KEY;

        return new StringRequest(Request.Method.GET, cityApiUrl, response -> {
            try {
                JSONObject cityInfoString = new JSONObject(response);
                JSONArray cityJsonFeatures = cityInfoString.getJSONArray("features");
                if (cityJsonFeatures.length() == 0)
                    return;

                JSONArray cityJsonDetails = cityJsonFeatures.getJSONObject(0)
                        .getJSONArray("context");
                String cityName = null;
                for (int i = 0; i < cityJsonDetails.length(); i++) {
                    JSONObject cityJsonDetail = cityJsonDetails.getJSONObject(i);
                    if (cityJsonDetail.getString("id").startsWith("place")) {
                        cityName = cityJsonDetail.getString("text");
                        break;
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
                createToast(view, "Unexpected Error!\n" + response);
            }

        },
                error -> createToast(view, "There was a problem with the connection."));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private StringRequest getCityWeatherForecasts(View view, double latitude, double longitude) {
        String weatherApiUrl = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude +
                "&lon=" + longitude + "&appid=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;

        return new StringRequest(Request.Method.GET, weatherApiUrl, response -> {
            try {
                JSONObject weatherString = new JSONObject(response);
                ArrayList<Weather> weathers = new ArrayList<>();
                Weather weather = new Weather();

//                add today weather
                JSONObject todayWeather = weatherString.getJSONObject("current");
                weather.setTemperature(todayWeather.getDouble("temp"));
                weather.setFeelsLike(todayWeather.getDouble("feels_like"));
                weather.setPressure(todayWeather.getDouble("pressure"));
                weather.setHumidity(todayWeather.getDouble("humidity"));
                weather.setIcon(todayWeather.getJSONArray("weather").getJSONObject(0)
                        .getString("icon"));
                weather.setDescription(todayWeather.getJSONArray("weather").getJSONObject(0)
                        .getString("description"));

                weathers.add(weather);

//                add forecasts
                JSONArray forecasts = weatherString.getJSONArray("daily");
                for (int i = 0; i < forecasts.length(); i++) {
                    weather = new Weather();
                    JSONObject forecast = forecasts.getJSONObject(i);
                    weather.setTemperature(forecast.getJSONObject("temp").getDouble("day"));
                    weather.setMinTemperature(forecast.getJSONObject("temp").getDouble("min"));
                    weather.setMaxTemperature(forecast.getJSONObject("temp").getDouble("max"));
                    weather.setFeelsLike(forecast.getJSONObject("feels_like").getDouble("day"));
                    weather.setPressure(forecast.getDouble("pressure"));
                    weather.setHumidity(forecast.getDouble("humidity"));
                    weather.setIcon(forecast.getJSONArray("weather").getJSONObject(0)
                            .getString("icon"));
                    weather.setDescription(forecast.getJSONArray("weather").getJSONObject(0)
                            .getString("description"));

                    weathers.add(weather);
                }

                tempCity.setWeatherForecasts(weathers);
                @SuppressLint("ResourceType") Thread thread = new Thread(() -> {
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
                createToast(view, "Unexpected Error!\n" + response);
            }

        },
                error -> createToast(view, "There was a problem with the connection."));
    }

    public StringRequest getCityLatAndLong(View view, String cityName) {
        String cityApiUrl = "https://api.mapbox.com/geocoding/v5/mapbox.places/" +
                cityName + ".json?access_token=" + BuildConfig.MAP_BOX_API_KEY;

        return new StringRequest(Request.Method.GET, cityApiUrl, response -> {
            try {
                JSONObject cityInfoString = new JSONObject(response);
                JSONArray cityJsonFeatures = cityInfoString.getJSONArray("features");
                if (cityJsonFeatures.length() == 0)
                    return;

                JSONArray cityJsonDetails = cityJsonFeatures.getJSONObject(0)
                        .getJSONArray("center");

                double latitude = cityJsonDetails.getDouble(1);
                double longitude = cityJsonDetails.getDouble(0);
                tempCity = new City(cityName, latitude, longitude, LocalDateTime.now());
                Thread thread = new Thread(() -> {
                    boolean isCacheAvailable = cityDao.count(latitude, longitude) > 0;

                    if (isCacheAvailable)
                        cityDao.updateName(latitude, longitude, cityName);
                    else
                        cityDao.insert(tempCity);

                });
                thread.join();
                thread.start();
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
                createToast(view, "Unexpected Error!\n" + response);
            }

        },
                error -> createToast(view, "There was a problem with the connection."));
    }

    public LiveData<City> getCity() {
        return city;
    }

    private void createToast(View view, String message) {
        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
    }
}

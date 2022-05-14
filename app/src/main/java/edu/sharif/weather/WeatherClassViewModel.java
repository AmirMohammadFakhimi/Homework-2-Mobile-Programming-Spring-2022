package edu.sharif.weather;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class WeatherClassViewModel extends AndroidViewModel {
    private WeatherRepository weatherRepository;
    private LiveData<Weather> weather;

    public WeatherClassViewModel(@NonNull Application application) {
        super(application);

        weatherRepository = new WeatherRepository(application);
    }

    public LiveData<Weather> getWeather() {
        return weather;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setWeather() {
        try {
            weatherRepository.getCityWeather(getApplication(), 0, 0);
            Log.d("asd", "Done");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("asd", e.getMessage());
        }

        weatherRepository.setCurrentWeather(0, 0);
        this.weather = weatherRepository.getCurrentWeather();
    }
}

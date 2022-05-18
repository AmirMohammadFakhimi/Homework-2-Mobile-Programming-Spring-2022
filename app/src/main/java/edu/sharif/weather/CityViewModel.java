package edu.sharif.weather;

import android.app.Application;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CityViewModel extends AndroidViewModel {
    private final CityRepository cityRepository;
    private LiveData<City> city;

    public CityViewModel(@NonNull Application application) {
        super(application);
        cityRepository = new CityRepository(application);
        city = cityRepository.getCity();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<City> getCityInfo(View view, double latitude, double longitude) {
        city = cityRepository.getCityInfo(view, latitude, longitude);
        return city;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<City> getCityInfo(View view, String cityName) {
        city = cityRepository.getCityInfo(view, cityName);
        return city;
    }
}

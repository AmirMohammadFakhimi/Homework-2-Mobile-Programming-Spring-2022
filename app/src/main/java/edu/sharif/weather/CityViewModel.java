package edu.sharif.weather;

import android.app.Application;
import android.os.Build;
import android.util.Log;

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
//        city = cityRepository.getCity();
    }

    public LiveData<City> getCity() {
        return city;
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void setCity() {
//        cityRepository.getCityInfo(getApplication(), 0, 0);
//        this.city = cityRepository.getCity();
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<City> getCityInfo(double latitude, double longitude) {
        city = cityRepository.getCityInfo(getApplication(), latitude, longitude);
        return city;
    }
}

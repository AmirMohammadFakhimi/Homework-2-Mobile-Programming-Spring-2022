package edu.sharif.weather.ui.weather;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.sharif.weather.City;
import edu.sharif.weather.CityViewModel;
import edu.sharif.weather.EditTextValidator;
import edu.sharif.weather.ForecastRecyclerViewAdaptor;
import edu.sharif.weather.R;
import edu.sharif.weather.Weather;
import edu.sharif.weather.databinding.FragmentWeatherBinding;

public class WeatherFragment extends Fragment implements ForecastRecyclerViewAdaptor.ItemClickListener {

    private FragmentWeatherBinding binding;
    private EditText latitude;
    private EditText longitude;
    private EditText inputCity;
    private Button submitButton;
    private CityViewModel viewModel;
    private ForecastRecyclerViewAdaptor adapter;
    private ArrayList<Weather> weathers;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WeatherTabViewModel weatherTabViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(WeatherTabViewModel.class);

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        latitude = binding.latitudeEditText;
        longitude = binding.longitudeEditText;
        inputCity = binding.cityEditText;
        submitButton = binding.submitButton;

        binding.latitudeLongitudeRadio.setOnCheckedChangeListener((group, isChecked) -> {
            if (isChecked) {
                latitude.setVisibility(View.VISIBLE);
                longitude.setVisibility(View.VISIBLE);
                binding.cityEditText.setVisibility(View.INVISIBLE);
                submitButton.setEnabled(true);
            }
        });

        binding.cityRadio.setOnCheckedChangeListener((group, isChecked) -> {
            if (isChecked) {
                binding.cityEditText.setVisibility(View.VISIBLE);
                latitude.setVisibility(View.INVISIBLE);
                longitude.setVisibility(View.INVISIBLE);
                submitButton.setEnabled(true);
            }
        });

        addDelayForEditText(latitude, longitude);
        addDelayForEditText(longitude, latitude);
        addDelayForEditText(inputCity, null);

        addValidatorForEditText(latitude, longitude, 90);
        addValidatorForEditText(longitude, latitude, 180);

        viewModel = ViewModelProviders.of(this).get(CityViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addDelayForEditText(EditText main, EditText toValidate) {
        main.addTextChangedListener(
                new TextWatcher() {
                    private Timer timer = new Timer();

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(final Editable s) {
                        timer.cancel();
                        timer = new Timer();
                        //Milliseconds
                        long DELAY = 5000;
                        timer.schedule(
                                new TimerTask() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void run() {
//                                        TODO: bug -> it runs twice
                                        if (main.getError() == null && !main.getText().toString().isEmpty() &&
                                                (toValidate == null || (toValidate.getError() == null && !toValidate.getText().toString().isEmpty()))) {
                                            new Handler(Looper.getMainLooper()).post(() -> {
                                                if (adapter != null) adapter.clear();
                                                binding.progressBar.setVisibility(View.VISIBLE);
                                                submitButton.setEnabled(false);
                                                getCityInfo();
                                            });
                                        }
                                    }
                                },
                                DELAY
                        );
                    }
                });
    }

    private void addValidatorForEditText(EditText main, EditText toValidate, int limit) {
        main.addTextChangedListener(new EditTextValidator(main) {
            @Override
            public void validate(EditText editText, String text) {
                if (text.isEmpty()) {
                    submitButton.setEnabled(false);
                    return;
                }

                double longitude = Double.parseDouble(text);
                if (longitude < -limit || longitude > limit) {
                    editText.setError("Must be between -" + limit + " and " + limit);
                    submitButton.setEnabled(false);
                } else
                    submitButton.setEnabled(toValidate.getError() == null &&
                            !toValidate.getText().toString().isEmpty());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getCityInfo() {
        if (binding.latitudeLongitudeRadio.isChecked()) {
            double latitude = Double.parseDouble(this.latitude.getText().toString());
            double longitude = Double.parseDouble(this.longitude.getText().toString());

            viewModel.getCityInfo(this.getView(), latitude, longitude)
                    .observe(getViewLifecycleOwner(), this::doOnObserve);
        } else {
            String cityName = inputCity.getText().toString();

            viewModel.getCityInfo(this.getView(), cityName).observe(
                    getViewLifecycleOwner(), this::doOnObserve);
        }

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doOnObserve(City city) {
            if (city == null) {
                binding.cityInfo.setText(R.string.no_city_selected);
                if (adapter != null) adapter.clear();
                return;
            }

            String cityName;
            if (city.getName() == null)
                cityName = "City name not found";
            else
                cityName = city.getName();

//            TODO: lastUpdatedTime updates same as liveData!!!!
            binding.cityInfo.setText(cityName + "\n" +
                    city.getLatitude() + "°, " + city.getLongitude() + "°\n" +
                    "Last Update: " + city.getLastUpdatedTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            if (city.getWeatherForecasts() != null) {
                binding.progressBar.setVisibility(View.INVISIBLE);

                RecyclerView forecastRecyclerView = binding.forecastRecyclerView;
                forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                weathers = city.getWeatherForecasts();
                adapter = new ForecastRecyclerViewAdaptor(getContext(), weathers);
                adapter.setClickListener(this);
                forecastRecyclerView.setAdapter(adapter);
            }
    }

    @Override
    public void onItemClick(View view, int position) {
        Weather weather = weathers.get(position);
        NavHostFragment.findNavController(WeatherFragment.this)
                .navigate(WeatherFragmentDirections
                        .actionNavigationWeatherToDetailedWeatherFragment(position,
                                weather.getDescription(),
                                (float) weather.getTemperature(),
                                (float) weather.getFeelsLike(),
                                (float) weather.getHumidity(),
                                (float) weather.getPressure(),
                                (float) weather.getMinTemperature(),
                                (float) weather.getMaxTemperature(),
                                weather.getIcon()));
    }
}
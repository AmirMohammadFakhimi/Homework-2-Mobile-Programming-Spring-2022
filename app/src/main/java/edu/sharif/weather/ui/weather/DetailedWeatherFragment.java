package edu.sharif.weather.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.sharif.weather.R;
import edu.sharif.weather.databinding.FragmentDetailedWeatherBinding;
import edu.sharif.weather.databinding.FragmentWeatherBinding;

public class DetailedWeatherFragment extends Fragment {
    FragmentDetailedWeatherBinding binding;
    TextView descriptionTextView;
    ImageView iconImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WeatherTabViewModel weatherTabViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(WeatherTabViewModel.class);

        binding = FragmentDetailedWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descriptionTextView = binding.descriptionTextView;
        iconImageView = binding.iconImageView;

        int day = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getDay();
        String description = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getDescription();
        float temperature = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getTemperature();
        float feelsLike = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getFeelsLike();
        float humidity = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getHumidity();
        float pressure = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getPressure();
        float minTemp = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getMinTemp();
        float maxTemp = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getMaxTemp();

        String result = "";
        result = result + "Day: " + day + "\n";
        result = result + "Description: " + description + "\n";
        result = result + "temperature: " + temperature + "\n";
        result = result + "feels like: " + feelsLike + "\n";
        result = result + "humidity: " + humidity + "\n";
        result = result + "pressure: " + pressure + "\n";
        result = result + "min temperature: " + minTemp + "\n";
        result = result + "max temperature: " + maxTemp + "\n";

        descriptionTextView.setText(result);

        String icon = DetailedWeatherFragmentArgs.fromBundle(getArguments()).getIcon();
        Picasso.get().load(getContext().getResources().getIdentifier("ic_" + icon, "drawable", getContext().getPackageName()))
                .into(iconImageView);
    }
}
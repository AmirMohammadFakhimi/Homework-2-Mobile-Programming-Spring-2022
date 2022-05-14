package edu.sharif.weather.ui.weather;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import edu.sharif.weather.WeatherClassViewModel;
import edu.sharif.weather.databinding.FragmentWeatherBinding;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WeatherTabViewModel weatherTabViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(WeatherTabViewModel.class);

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        weatherTabViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WeatherClassViewModel viewModel = ViewModelProviders.of(this).get(WeatherClassViewModel.class);
        viewModel.setWeather();

        viewModel.getWeather().observe(getViewLifecycleOwner(), weather -> {
            if (weather != null)
                binding.textHome.setText(weather.getTemp());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
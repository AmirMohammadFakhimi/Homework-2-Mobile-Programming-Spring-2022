package edu.sharif.weather.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.sharif.weather.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Switch darkModeSwitch;
    private Boolean nightMode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        darkModeSwitch =
                binding.switch1;

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                }
            }
        });

//        nightMode = false;
//
//        darkModeSwitch.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view)
//                    {
//                        if (!nightMode) {
//                            AppCompatDelegate
//                                    .setDefaultNightMode(
//                                            AppCompatDelegate
//                                                    .MODE_NIGHT_YES);
//                            nightMode = true;
//                        }
//                        else {
//                            AppCompatDelegate
//                                    .setDefaultNightMode(
//                                            AppCompatDelegate
//                                                    .MODE_NIGHT_NO);
//                            nightMode = false;
//                        }
//                    }
//                });

        final TextView textView = binding.textDashboard;
        settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
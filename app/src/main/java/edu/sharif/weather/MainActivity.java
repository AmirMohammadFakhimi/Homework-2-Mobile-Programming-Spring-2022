package edu.sharif.weather;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.sharif.weather.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    int NightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_weather, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        NightMode = sharedPreferences.getInt("NightModeInt", 1);
        AppCompatDelegate.setDefaultNightMode(NightMode);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        NightMode = AppCompatDelegate.getDefaultNightMode();

        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt("NightModeInt", NightMode);
        editor.apply();
    }
}

//    public class MainActivity
//            extends AppCompatActivity {
//
//        private Button btnToggleDark;
//
//        @SuppressLint("SetTextI18n")
//        @Override
//        protected void onCreate(
//                Bundle savedInstanceState)
//        {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//            btnToggleDark
//                    = findViewById(R.id.btnToggleDark);
//
//            // Saving state of our app
//            // using SharedPreferences
//            SharedPreferences sharedPreferences
//                    = getSharedPreferences(
//                    "sharedPrefs", MODE_PRIVATE);
//            final SharedPreferences.Editor editor
//                    = sharedPreferences.edit();
//            final boolean isDarkModeOn
//                    = sharedPreferences
//                    .getBoolean(
//                            "isDarkModeOn", false);
//
//            // When user reopens the app
//            // after applying dark/light mode
//            if (isDarkModeOn) {
//                AppCompatDelegate
//                        .setDefaultNightMode(
//                                AppCompatDelegate
//                                        .MODE_NIGHT_YES);
//                btnToggleDark.setText(
//                        "Disable Dark Mode");
//            }
//            else {
//                AppCompatDelegate
//                        .setDefaultNightMode(
//                                AppCompatDelegate
//                                        .MODE_NIGHT_NO);
//                btnToggleDark
//                        .setText(
//                                "Enable Dark Mode");
//            }
//
//            btnToggleDark.setOnClickListener(
//                    new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View view)
//                        {
//                            // When user taps the enable/disable
//                            // dark mode button
//                            if (isDarkModeOn) {
//
//                                // if dark mode is on it
//                                // will turn it off
//                                AppCompatDelegate
//                                        .setDefaultNightMode(
//                                                AppCompatDelegate
//                                                        .MODE_NIGHT_NO);
//                                // it will set isDarkModeOn
//                                // boolean to false
//                                editor.putBoolean(
//                                        "isDarkModeOn", false);
//                                editor.apply();
//
//                                // change text of Button
//                                btnToggleDark.setText(
//                                        "Enable Dark Mode");
//                            }
//                            else {
//
//                                // if dark mode is off
//                                // it will turn it on
//                                AppCompatDelegate
//                                        .setDefaultNightMode(
//                                                AppCompatDelegate
//                                                        .MODE_NIGHT_YES);
//
//                                // it will set isDarkModeOn
//                                // boolean to true
//                                editor.putBoolean(
//                                        "isDarkModeOn", true);
//                                editor.apply();
//
//                                // change text of Button
//                                btnToggleDark.setText(
//                                        "Disable Dark Mode");
//                            }
//                        }
//                    });
//        }
//    }


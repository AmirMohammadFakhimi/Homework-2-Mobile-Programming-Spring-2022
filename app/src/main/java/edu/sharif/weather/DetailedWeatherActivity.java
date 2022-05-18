package edu.sharif.weather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailedWeatherActivity extends AppCompatActivity {
    private TextView descriptionTextView;
    private ImageView iconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);

        descriptionTextView = findViewById(R.id.descriptionTextView);
        iconImageView = findViewById(R.id.iconImageView);

        Intent intent = getIntent();

        int day_number = intent.getIntExtra("day_number", 0);
        String description = intent.getStringExtra("description");
        double temperature = intent.getDoubleExtra("temperature", 0);
        double feels_like = intent.getDoubleExtra("feels_like", 0);
        double humidity = intent.getDoubleExtra("humidity", 0);
        double pressure = intent.getDoubleExtra("pressure", 0);
        double min_temp = intent.getDoubleExtra("min_temp", 0);
        double max_temp = intent.getDoubleExtra("max_temp", 0);
        String icon_code = intent.getStringExtra("icon_code");

        String result = "";
        {
            if (day_number == 0)
                result = result + "Right now" + "\n";
            else if (day_number == 1)
                result = result + "Tomorrow" + "\n";
            else
                result = result + day_number + " days from today" + "\n";
        }
        result = result + "Description: " + description + "\n";
        result = result + "temperature: " + temperature + "\n";
        result = result + "feels like: " + feels_like + "\n";
        result = result + "humidity: " + humidity + "\n";
        result = result + "pressure: " + pressure + "\n";

        if (day_number > 0) {
            result = result + "min temperature: " + min_temp + "\n";
            result = result + "max temperature: " + max_temp + "\n";
        }

        descriptionTextView.setText(result);

        Picasso.get().load(this.getResources().getIdentifier("ic_" + icon_code, "drawable", this.getPackageName()))
                .into(iconImageView);
    }
}
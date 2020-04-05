package com.example.weatherforecast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.R;
import com.example.weatherforecast.adapters.DarkSkyViewAdapter;
import com.example.weatherforecast.model.WeatherCondition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    private List<WeatherCondition> lstWeathers;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private TextView cityNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        recyclerView = findViewById(R.id.thirdPage_recyclerView);
        lstWeathers = new ArrayList<>();
        sharedPreferences = getSharedPreferences(getString(R.string.SharedPreferencesInstance), MODE_PRIVATE);
        cityNameTextView = findViewById(R.id.weather_city_name);

        if (getIntent().getBooleanExtra("ISCONNECTED", true)) {
            initialize();
        } else {
            Toast.makeText(getApplicationContext(), R.string.NoNetworkConnection,
                    Toast.LENGTH_SHORT).show();
            lastSeen();
        }
        setupRecyclerView(lstWeathers);
    }

    @Override
    protected void onStop() {
        super.onStop();
        save();
    }

    private void initialize() {
        String[] summaries = getIntent().getStringArrayExtra("SUMMARIES");
        String[] icons = getIntent().getStringArrayExtra("ICONS");
        String[] humidities = getIntent().getStringArrayExtra("HUMIDITIES");
        String[] pressures = getIntent().getStringArrayExtra("PRESSURES");
        String[] temperaturesMax = getIntent().getStringArrayExtra("TEMPERATURESMAX");
        String[] temperaturesMin = getIntent().getStringArrayExtra("TEMPERATURESMIN");
        String cityName = getIntent().getStringExtra("CITYNAME");
        cityNameTextView.setText(cityName);
        for (int i = 0; i < summaries.length; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.dayFormat));
            String day = simpleDateFormat.format(date);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(getString(R.string.dateFormat));
            String ourDate = simpleDateFormat1.format(date);

            WeatherCondition weatherCondition = new WeatherCondition();
            weatherCondition.setDate(ourDate);
            weatherCondition.setDay(day);
            weatherCondition.setSummary(summaries[i]);
            weatherCondition.setIcon(icons[i]);
            weatherCondition.setHumidity(humidities[i]);
            weatherCondition.setPressure(pressures[i]);
            weatherCondition.setTemperatureMax(temperaturesMax[i]);
            weatherCondition.setTemperatureMin(temperaturesMin[i]);

            lstWeathers.add(weatherCondition);
        }
    }

    private void lastSeen() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lstWeathers", null);
        Type type = new TypeToken<ArrayList<WeatherCondition>>() {
        }.getType();
        lstWeathers = gson.fromJson(json, type);
    }

    private void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lstWeathers", new Gson().toJson(lstWeathers));
        editor.apply();
    }

    private void setupRecyclerView(List<WeatherCondition> lstWeathers) {
        DarkSkyViewAdapter myAdapter = new DarkSkyViewAdapter(this, lstWeathers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);
    }

}

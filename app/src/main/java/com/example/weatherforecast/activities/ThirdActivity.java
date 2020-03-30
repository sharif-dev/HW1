package com.example.weatherforecast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.weatherforecast.R;
import com.example.weatherforecast.adapters.DarkSkyViewAdapter;
import com.example.weatherforecast.model.WeatherCondition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    private List<WeatherCondition> lstWeathers;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        recyclerView = (RecyclerView) findViewById(R.id.thirdPage_recyclerView);
        lstWeathers = new ArrayList<>();
        sharedPreferences = getSharedPreferences(getString(R.string.SharedPreferencesInstance), MODE_PRIVATE);

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
        String[] times = getIntent().getStringArrayExtra("TIMES");  //TODO
        String[] summaries = getIntent().getStringArrayExtra("SUMMARIES");
        String[] icons = getIntent().getStringArrayExtra("ICONS");
        String[] humidities = getIntent().getStringArrayExtra("HUMIDITIES");
        String[] pressures = getIntent().getStringArrayExtra("PRESSURES");
        String[] temperaturesMax = getIntent().getStringArrayExtra("TEMPERATURESMAX");
        String[] temperaturesMin = getIntent().getStringArrayExtra("TEMPERATURESMIN");
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
        for (int i = 0; i < 7; i++) {
            String[] weather = sharedPreferences.getString("weather" + Integer.toString(i),
                    null).split(getString(R.string.split_In_Filing));
            WeatherCondition weatherCondition = new WeatherCondition();
            weatherCondition.setDate(weather[0]);
            weatherCondition.setDay(weather[1]);
            weatherCondition.setSummary(weather[2]);
            weatherCondition.setIcon(weather[3]);
            weatherCondition.setHumidity(weather[4]);
            weatherCondition.setPressure(weather[5]);
            weatherCondition.setTemperatureMax(weather[6]);
            weatherCondition.setTemperatureMin(weather[7]);

            lstWeathers.add(weatherCondition);
        }
    }

    private void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit().clear();

        for (int i = 0; i < lstWeathers.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(lstWeathers.get(i).getDate() + getString(R.string.split_In_Filing));
            stringBuilder.append(lstWeathers.get(i).getDay() + getString(R.string.split_In_Filing));
            stringBuilder.append(lstWeathers.get(i).getSummary() + getString(R.string.split_In_Filing));
            stringBuilder.append(lstWeathers.get(i).getIcon() + getString(R.string.split_In_Filing));
            stringBuilder.append(lstWeathers.get(i).getHumidity() + getString(R.string.split_In_Filing));
            stringBuilder.append(lstWeathers.get(i).getPressure() + getString(R.string.split_In_Filing));
            stringBuilder.append(lstWeathers.get(i).getTemperatureMax() + getString(R.string.split_In_Filing));
            stringBuilder.append(lstWeathers.get(i).getTemperatureMin());

            editor.putString("weather" + Integer.toString(i), stringBuilder.toString());
        }
        editor.apply();
    }

    private void setupRecyclerView(List<WeatherCondition> lstWeathers) {
        DarkSkyViewAdapter myAdapter = new DarkSkyViewAdapter(this, lstWeathers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);
    }
}

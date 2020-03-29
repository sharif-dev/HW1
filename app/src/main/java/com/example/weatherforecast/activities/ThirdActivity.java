package com.example.weatherforecast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.weatherforecast.R;
import com.example.weatherforecast.adapters.DarkSkyViewAdapter;
import com.example.weatherforecast.adapters.RecyclerViewAdapter;
import com.example.weatherforecast.model.City;
import com.example.weatherforecast.model.WeatherCondition;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    private List<WeatherCondition> lstWeathers;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        recyclerView = (RecyclerView) findViewById(R.id.thirdPage_recyclerView);
        lstWeathers = new ArrayList<>();
        boolean isConnected = getIntent().getBooleanExtra("ISCONNECTED", true);

        if(isConnected){
            initialize();
        }
        else{
            //TODO last Information
        }
        setupRecyclerView(lstWeathers);
    }

    private void initialize(){
        String[] times = getIntent().getStringArrayExtra("TIMES");  //TODO
        String[] summaries = getIntent().getStringArrayExtra("SUMMARIES");
        String[] icons = getIntent().getStringArrayExtra("ICONS");
        String[] humidities = getIntent().getStringArrayExtra("HUMIDITIES");
        String[] pressures = getIntent().getStringArrayExtra("PRESSURES");
        String[] temperaturesMax = getIntent().getStringArrayExtra("TEMPERATURESMAX");
        String[] temperaturesMin = getIntent().getStringArrayExtra("TEMPERATURESMIN");
        for (int i = 0; i < summaries.length; i++) {
            WeatherCondition weatherCondition = new WeatherCondition();
            weatherCondition.setSummary(summaries[i]);
            weatherCondition.setIcon(icons[i]);
            weatherCondition.setHumidity(humidities[i]);
            weatherCondition.setPressure(pressures[i]);
            weatherCondition.setTemperatureMax(temperaturesMax[i]);
            weatherCondition.setTemperatureMin(temperaturesMin[i]);

            weatherCondition.setDate("TODO");//TODO
            weatherCondition.setDay("TODO");//TODO

            lstWeathers.add(weatherCondition);
        }
    }

    private void setupRecyclerView(List<WeatherCondition> lstWeathers) {
        DarkSkyViewAdapter myAdapter = new DarkSkyViewAdapter(this, lstWeathers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);
    }
}

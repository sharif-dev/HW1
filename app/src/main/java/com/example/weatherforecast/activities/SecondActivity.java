package com.example.weatherforecast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.weatherforecast.R;
import com.example.weatherforecast.adapters.RecyclerViewAdapter;
import com.example.weatherforecast.model.City;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private List<City> lstCity;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        lstCity = new ArrayList<>();

        String city_name = getIntent().getStringExtra("CITY_NAME");
        String[] longitudes = getIntent().getStringArrayExtra("LONGITUDES");
        String[] latitudes = getIntent().getStringArrayExtra("LATITUDES");
        for (int i = 0; i < longitudes.length; i++) {
            City city = new City();
            city.setName(city_name);
            city.setLongitude(longitudes[i]);
            city.setLatitude(latitudes[i]);
            lstCity.add(city);
        }
        setupRecyclerView(lstCity);
    }

    private void setupRecyclerView(List<City> lstCity) {
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstCity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);
    }
}

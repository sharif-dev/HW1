package com.example.weatherforecast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherforecast.Listeners.RecyclerItemClickListener;
import com.example.weatherforecast.R;
import com.example.weatherforecast.adapters.RecyclerViewAdapter;
import com.example.weatherforecast.model.City;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        String[] city_names = getIntent().getStringArrayExtra("CITY_NAMES");
        String[] longitudes = getIntent().getStringArrayExtra("LONGITUDES");
        String[] latitudes = getIntent().getStringArrayExtra("LATITUDES");
        for (int i = 0; i < longitudes.length; i++) {
            City city = new City();
            city.setName(city_names[i]);
            city.setLongitude(longitudes[i]);
            city.setLatitude(latitudes[i]);
            lstCity.add(city);
        }
        setupRecyclerView(lstCity);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        DarkSkyCall(position);
                    }
                })
        );
    }

    private void setupRecyclerView(List<City> lstCity) {
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstCity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);
    }

    private void DarkSkyCall(int position){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getString(R.string.DarkSkyURL) + getString(R.string.DarkSkySecretKey)
                + "/" + lstCity.get(position).getLatitude()  + "," + lstCity.get(position).getLongitude();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO
                Toast.makeText(getApplicationContext(), response.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO
                Toast.makeText(getApplicationContext(), error.getCause().toString(),
                    Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }
}

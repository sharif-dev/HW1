package com.example.weatherforecast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;
import com.example.weatherforecast.Listeners.RecyclerItemClickListener;
import com.example.weatherforecast.R;
import com.example.weatherforecast.adapters.RecyclerViewAdapter;
import com.example.weatherforecast.model.City;
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

    private void DarkSkyCall(int position){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getString(R.string.DarkSkyURL) +
                getString(R.string.DarkSkySecretKey) + "/" +
                lstCity.get(position).getLatitude() + "," +
                lstCity.get(position).getLongitude();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String[] summaries = new String[7];
                            String[] humidities = new String[7];
                            String[] pressures = new String[7];
                            String[] temperaturesMax = new String[7];
                            String[] temperaturesMin = new String[7];
                            String[] icons = new String[7];
                            String[] times = new String[7];

                            JSONArray data = response.getJSONObject("daily").getJSONArray("data");
                            for(int i = 0; i < 7; i++){
                                times[i] = data.getJSONObject(i).get("time").toString();
                                summaries[i] = data.getJSONObject(i).getString("summary");
                                icons[i] = data.getJSONObject(i).getString("icon");
                                humidities[i] = data.getJSONObject(i).get("humidity").toString();
                                pressures[i] = data.getJSONObject(i).get("pressure").toString();
                                temperaturesMax[i] = fahrenheitToCelsius(data.getJSONObject(i).getDouble("temperatureMax"));
                                temperaturesMin[i] = fahrenheitToCelsius(data.getJSONObject(i).getDouble("temperatureMin"));
                            }

                            goFinalPage(times, summaries, icons, humidities, pressures, temperaturesMax, temperaturesMin);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ( error instanceof NoConnectionError ){
                    Toast.makeText(getApplicationContext(), R.string.NoNetworkConnection,
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError ) {
                    Toast.makeText(getApplicationContext(), R.string.error_network_timeout,
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), R.string.error_server,
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), R.string.error_network,
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), R.string.error_parse,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        queue.add(request);
    }

    private void goFinalPage(String[] times, String[] summaries, String[] icons, String[] humidities,
                         String[] pressures, String[] temperaturesMax, String[] temperaturesMin){
        Intent intent = new Intent(this, ThirdActivity.class);
        intent.putExtra("TIMES", times);
        intent.putExtra("SUMMARIES", summaries);
        intent.putExtra("ICONS", icons);
        intent.putExtra("HUMIDITIES", humidities);
        intent.putExtra("PRESSURES", pressures);
        intent.putExtra("TEMPERATURESMAX", temperaturesMax);
        intent.putExtra("TEMPERATURESMIN", temperaturesMin);

        intent.putExtra("ISCONNECTED", true);
        startActivity(intent);
    }

    private void setupRecyclerView(List<City> lstCity) {
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstCity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);
    }

    public String fahrenheitToCelsius(Double temperature){
        return Integer.toString( (int) Math.round((temperature - 32) * 5 / 9));
    }
}

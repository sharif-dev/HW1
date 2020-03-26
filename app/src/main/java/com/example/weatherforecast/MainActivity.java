package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private EditText city_name;
    private Button search_button;
    private ProgressBar progressBar;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city_name = (EditText) findViewById(R.id.city_edit_text);
        search_button = (Button) findViewById(R.id.search);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city_name.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.nullCityName,
                            Toast.LENGTH_SHORT).show();
                } else {
                    mapBoxCall();
                }
            }
        });
    }

    public void mapBoxCall() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.mapBoxURL) + city_name.getText().toString()
                + ".json?access_token=" + getString(R.string.accessToken);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("features");
                    String[] longitudes = new String[jsonArray.length()];
                    String[] latitudes = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject feature = jsonArray.getJSONObject(i);
                        JSONObject geometry = feature.getJSONObject("geometry");
                        JSONArray coordinates = geometry.getJSONArray("coordinates");
                        longitudes[i] = coordinates.getString(0);
                        latitudes[i] = coordinates.getString(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), R.string.error_network_timeout,
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), R.string.error_server,
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), R.string.error_network,
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), R.string.error_parse,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        queue.add(request);
    }
}

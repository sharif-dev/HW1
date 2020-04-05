package com.example.weatherforecast.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.example.weatherforecast.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private EditText city_name;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city_name = findViewById(R.id.city_edit_text);
        Button search_button = findViewById(R.id.search);
        progressBar = findViewById(R.id.progressBar);
        final Handler handler = new Handler(getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkInternetConnection(handler);
            }
        }).start();

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (city_name.getText().toString().matches("")) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), R.string.nullCityName,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                            });
                            city_name.onEditorAction(EditorInfo.IME_ACTION_DONE);
                            mapBoxCall();
                        }
                    }
                }).start();
            }
        });
    }

    public void checkInternetConnection(Handler handler) {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            checkHistory(handler);
        }
    }

    public void checkHistory(Handler handler) {
        if (getSharedPreferences(getString(R.string.SharedPreferencesInstance), MODE_PRIVATE).getAll().size() != 0) {
            Intent intent = new Intent(this, ThirdActivity.class);
            intent.putExtra("ISCONNECTED", false);
            startActivity(intent);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), R.string.NoNetworkConnection,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void mapBoxCall() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.mapBoxURL) + city_name.getText().toString()
                + ".json?access_token=" + getString(R.string.mapBoxAccessToken);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mapBoxParse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandler(error);
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        queue.add(request);
    }

    private void mapBoxParse(JSONObject response) {
        try {
            JSONArray jsonArray = response.getJSONArray("features");
            if (jsonArray.length() == 0) {
                Toast.makeText(getApplicationContext(), R.string.ValidCity,
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                city_name.setText("");
                city_name.setText("");
                return;
            }

            String[] place_names = new String[jsonArray.length()];
            String[] longitudes = new String[jsonArray.length()];
            String[] latitudes = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject feature = jsonArray.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                place_names[i] = feature.getString("place_name");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                longitudes[i] = coordinates.getString(0);
                latitudes[i] = coordinates.getString(1);
            }

            goSecondPage(place_names, longitudes, latitudes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void errorHandler(VolleyError error) {
        if (error instanceof NoConnectionError) {
            Toast.makeText(getApplicationContext(), R.string.NoNetworkConnection,
                    Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
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

    private void goSecondPage(String[] place_names, String[] longitudes, String[] latitudes) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("CITY_NAMES", place_names);
        intent.putExtra("LONGITUDES", longitudes);
        intent.putExtra("LATITUDES", latitudes);

        startActivity(intent);
    }

}

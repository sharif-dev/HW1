package com.example.weatherforecast.model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherforecast.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DarkSky {
    private String url = "https://api.darksky.net/forecast/" +
            "c0363c434af5d6b11a443146e9bc0337";
    private boolean currentlycheck = true;
    private boolean minutelycheck = false;
    private boolean hourlycheck = false;
    private boolean dailycheck = true;

    private String[] summaries;
    private String[] humidities;
    private String[] pressures;
    private String[] temperaturesHigh;
    private String[] temperaturesLow;


    public DarkSky(){
        summaries = new String[8];
        humidities = new String[8];
        pressures = new String[8];
        temperaturesHigh = new String[8];
        temperaturesLow = new String[8];
    }

    public void setUrl(String latitude, String longitude){
        url += "/" + latitude + "," + longitude;
    }

    public String getUrl(){
        return url;
    }

    public boolean isCurrentlycheck() {
        return currentlycheck;
    }

    public boolean isMinutelycheck() {
        return minutelycheck;
    }

    public boolean isHourlycheck() {
        return hourlycheck;
    }

    public boolean isDailycheck() {
        return dailycheck;
    }

    public String[] getSummaries() {
        return summaries;
    }

    public String[] getHumidities() {
        return humidities;
    }

    public String[] getPressures() {
        return pressures;
    }

    public String[] getTemperaturesHigh() {
        return temperaturesHigh;
    }

    public String[] getTemperaturesLow() {
        return temperaturesLow;
    }

    public void call(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO
            }
        });
        queue.add(request);
    }

    private void parse(JSONObject response){
        if(isCurrentlycheck()){
            currentSituation(response);
        }
        if(isDailycheck()){
            dailySituation(response);
        }
    }

    public void currentSituation(JSONObject response){
        try{
            JSONObject current = response.getJSONObject("currently");
            summaries[0] = current.getString("summary");
            humidities[0] = current.get("humidity").toString();
            pressures[0] = current.get("pressure").toString();
            this.temperaturesHigh[0] = current.get("temperature").toString();
            temperaturesLow[0] = current.get("temperature").toString();

            Log.d("CUCUCUCUCUCUCU", temperaturesHigh[0]);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void dailySituation(JSONObject response){
        try{
            JSONArray data = response.getJSONObject("daily").getJSONArray("data");
            for(int i = 0; i < 7; i++){
                summaries[i + 1] = data.getJSONObject(i).getString("summary");
                humidities[i + 1] = data.getJSONObject(i).get("humidity").toString();
                pressures[i + 1] = data.getJSONObject(i).get("pressure").toString();
                this.temperaturesHigh[i + 1] = data.getJSONObject(i).get("temperatureHigh").toString();
                temperaturesLow[i + 1] = data.getJSONObject(i).get("temperatureLow").toString();

                Log.d("DADADADADADA", temperaturesHigh[1]);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

package com.example.weatherforecast.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
    private String[] icons;
    private String[] humidities;
    private String[] pressures;
    private String[] temperaturesMax;
    private String[] temperaturesMin;


    public DarkSky(){
        summaries = new String[8];
        icons = new String[8];
        humidities = new String[8];
        pressures = new String[8];
        temperaturesMax = new String[8];
        temperaturesMin = new String[8];
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

    public String[] getIcons() {
        return icons;
    }

    public String[] getHumidities() {
        return humidities;
    }

    public String[] getPressures() {
        return pressures;
    }

    public String[] getTemperaturesMax() {
        return temperaturesMax;
    }

    public String[] getTemperaturesMin() {
        return temperaturesMin;
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
            this.temperaturesMax[0] = current.get("temperature").toString();
            temperaturesMin[0] = current.get("temperature").toString();
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
                this.temperaturesMax[i + 1] = data.getJSONObject(i).get("temperatureHigh").toString();
                temperaturesMin[i + 1] = data.getJSONObject(i).get("temperatureLow").toString();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

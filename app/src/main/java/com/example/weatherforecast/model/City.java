package com.example.weatherforecast.model;

public class City {
    private String latitude;
    private String longitude;
    private String name;
    private Boolean txt_and_progressBar_visibility;

    public Boolean getTxt_and_progressBar_visibility() {
        return txt_and_progressBar_visibility;
    }

    public void setTxt_and_progressBar_visibility(Boolean txt_and_progressBar_visibility) {
        this.txt_and_progressBar_visibility = txt_and_progressBar_visibility;
    }

    public City() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

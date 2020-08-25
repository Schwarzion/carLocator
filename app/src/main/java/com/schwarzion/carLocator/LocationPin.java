package com.schwarzion.carLocator;

public class LocationPin {
    private String time;
    private String titre;
    private float lat;
    private float lon;

    public LocationPin(String time, String titre, float lat, float lon) {
        this.time = time;
        this.titre = titre;
        this.lat = lat;
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }
}

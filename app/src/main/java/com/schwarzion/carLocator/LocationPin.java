package com.schwarzion.carLocator;

public class LocationPin {
    private String time;
    private String titre;
    private double lat;
    private double lon;

    public LocationPin(String time, String titre, double lat, double lon) {
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

    public double getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }
}

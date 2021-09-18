package com.example.bloodonrequest.model;

public class LatLng {

    private double lat;
    private double lon;
    private String marker;

    private double v;
    private double v2;

    public LatLng(double lat, double lon, String marker) {
        this.lat = lat;
        this.lon = lon;
        this.marker = marker;
    }

    public LatLng(double v, double v1) {

        this.v = v;
        this.v2 = v2;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getV2() {
        return v2;
    }

    public void setV2(double v2) {
        this.v2 = v2;
    }
}

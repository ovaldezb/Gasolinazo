package com.survey.innovation;

/**
 * Created by omar.valdez on 10/01/2017.
 */

public class GasStation {
    private double lat;
    private double lon;
    private String price;
    private String date;

    public GasStation(double lat, double lon, String price, String date) {
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.date = date;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}

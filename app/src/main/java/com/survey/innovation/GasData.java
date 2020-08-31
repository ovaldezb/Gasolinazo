package com.survey.innovation;

/**
 * Created by omar.valdez on 03/01/2017.
 */

public class GasData {

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBarRate() {
        return barRate;
    }

    public void setBarRate(String barRate) {
        this.barRate = barRate;
    }

    private String lat;
    private String lon;
    private String price;
    private String barRate;

    public GasData(String lat, String lon, String price, String barRate) {
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.barRate = barRate;
    }

    public GasData() {
    }
}

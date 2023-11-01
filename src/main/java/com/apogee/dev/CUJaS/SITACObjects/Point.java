package com.apogee.dev.CUJaS.SITACObjects;

public class Point extends Figure {
    public String name = "Point";
    public double latitude, longitude;

    public Point(double latitude, double longitude, String... name) {
        super(name);
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

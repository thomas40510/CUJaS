package com.apogee.dev.CUJaS.SITACObjects;

public class Circle extends Ellipse {
    public Point center;
    public double radius;

    // accepts either Point or double[] for center
    public Circle(Object center, double radius, String... name) {
        super(center, radius, radius, 0, name);
        this.center = as_point(center);
        this.radius = radius;
    }

    @Override
    public String toString() {
        return this.name + " " + this.center + " " + this.radius;
    }

    @Override
    public String export_kml() {
        return super.export_kml();
    }
}

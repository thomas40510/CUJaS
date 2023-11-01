package com.apogee.dev.CUJaS.SITACObjects;

public class Corridor extends Figure {
    public String name = "Corridor";
    public Point start_point, end_point;
    public double width;

    public Corridor(Object start_point, Object end_point, double width, String... name) {
        super(name);
        this.start_point = as_point(start_point);
        this.end_point = as_point(end_point);
        this.width = width;
    }
}

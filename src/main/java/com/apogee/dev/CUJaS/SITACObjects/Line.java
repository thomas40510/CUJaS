package com.apogee.dev.CUJaS.SITACObjects;

import java.util.ArrayList;

public class Line extends Figure {
    public ArrayList<Point> points = new ArrayList<Point>();
    public String name = "Line";

    public Line(ArrayList<Object> points, String... name) {
        super(name);
        this.points.addAll(as_points(points));
    }
}

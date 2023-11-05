package com.apogee.dev.CUJaS.SITACObjects;

import java.util.ArrayList;

public class Line extends Figure {
    public ArrayList<Point> points = new ArrayList<>();

    public Line(ArrayList<Point> points, String... name) {
        super(name);
        this.points.addAll(points);
    }

    public Line() {
        super();
        this.points = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(this.name + " {");
        for(Point p : this.points) {
            res.append(p).append(";");
        }
        return res + "}";
    }

    public Polygon asPolygon() {
        this.points.add(this.points.get(0));
        return new Polygon(this.points);
    }
}

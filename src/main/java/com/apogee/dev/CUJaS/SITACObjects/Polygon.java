package com.apogee.dev.CUJaS.SITACObjects;

import java.util.ArrayList;


public class Polygon extends Line {
    public String name = "Polygon";

    /**
     * A Polygon Object, define by its points
     * @param points Points of the polygon
     * @param name Optional name of the Polygon
     */
    public Polygon(ArrayList<Point> points, String... name) {
        super(points, name);
    }

    public Polygon() {
        super();
        this.points = new ArrayList<>();
    }

    public Polygon fromLine(Line l) {
        return new Polygon(l.points, l.name);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(this.name + " {");
        for(Point p : this.points) {
            res.append(p).append(" ");
        }
        return res + "}";
    }
}

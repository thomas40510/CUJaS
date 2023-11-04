package com.apogee.dev.CUJaS.SITACObjects;

import java.util.ArrayList;


public class Polygon extends Line {
    public String name = "Polygon";

    /**
     * A Polygon Object, define by its points
     * @param points Points of the polygon
     * @param name Optional name of the Polygon
     */
    public Polygon(ArrayList<Object> points, String... name) {
        super(points, name);
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

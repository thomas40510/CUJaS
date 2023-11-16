package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.KMLUtils;

import java.util.ArrayList;


public class Polygon extends Line {
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

    @Override
    public String export_kml() {
        StringBuilder coords = new StringBuilder();
        for (Point p : this.points) {
            coords.append(p.longitude).append(",").append(p.latitude).append(",0 \n");
        }
        return KMLUtils.kmlPolygon(this.name, "#style_shape", coords.toString());
    }
}

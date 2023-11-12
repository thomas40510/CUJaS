package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.GeomUtils;
import com.apogee.dev.CUJaS.SITACObjects.utils.KMLUtils;

public class Rectangle extends Figure {
    public Point start;
    public double horizontal, vertical;

    public Rectangle(Object start, double horizontal, double vertical, String... name) {
        super(name);
        this.start = as_point(start);
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    @Override
    public String toString() {
        return this.name + " ("
                + this.start + " "
                + this.horizontal + " "
                + this.vertical + ")";
    }

    @Override
    public String export_kml() {
        StringBuilder res = new StringBuilder();
        for (Point p : new GeomUtils().makeRectangle(this)) {
            res.append(p.latitude).append(",").append(p.longitude).append(",0\n");
        }
        return new KMLUtils().kmlPolygon(this.name, "#style_shape", res.toString());
    }
}

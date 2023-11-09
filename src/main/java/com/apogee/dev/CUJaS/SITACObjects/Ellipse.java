package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.GeomUtils;

import java.util.ArrayList;

public class Ellipse extends Figure {
    public Point center;
    public double hradius, vradius, angle;

    // accepts either Point or double[] for center
    public Ellipse(Object center, double hradius, double vradius, double angle, String... name) {
        super(name);
        this.center = as_point(center);
        this.hradius = hradius / 2;
        this.vradius = vradius / 2;
        this.angle = angle;
    }

    @Override
    public String toString() {
        return this.name + " {"
                + this.center + " / "
                + this.hradius + " ; "
                + this.vradius + "}";
    }

    @Override
    public String export_kml() {
        StringBuilder res = new StringBuilder();
        ArrayList<Point> points = new GeomUtils().makeEllipse(this);
        for (Point p : points) {
            res.append(p.export_kml());
        }
        return res.toString();
    }
}


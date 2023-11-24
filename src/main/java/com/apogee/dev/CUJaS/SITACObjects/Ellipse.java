package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.GeomUtils;
import com.apogee.dev.CUJaS.SITACObjects.utils.KMLUtils;

import java.util.ArrayList;

/**
 * Implémentation de l'Ellipse
 * @see Figure
 * @implNote Une ellipse est une figure géométrique définie par un centre, deux rayons et un angle de rotation.
 */
public class Ellipse extends Figure {
    public Point center;
    public double hradius, vradius, angle;

    /**
     * Constructeur de l'{@code Ellipse}
     * @param center centre de l'ellipse
     * @param hradius rayon horizontal de l'ellipse
     * @param vradius rayon vertical de l'ellipse
     * @param angle inclinaison de l'ellipse par rapport à l'horizontale (en degrés)
     * @param name nom de l'ellipse
     * @implNote Les rayons extraits sont en réalité les diamètres de l'ellipse, donc on divise par 2.
     */
    public Ellipse(Point center, double hradius, double vradius, double angle, String... name) {
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
        ArrayList<Point> points = GeomUtils.makeEllipse(this);
        for (Point p : points) {
            res.append(p.longitude).append(",").append(p.latitude).append(",0\n");
        }
        return KMLUtils.kmlPolygon(this.name, "#style_circle", res.toString());
    }
}


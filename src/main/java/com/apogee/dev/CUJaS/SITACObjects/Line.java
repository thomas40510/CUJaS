package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.KMLUtils;

import java.util.ArrayList;

/**
 * Implémentation de la {@code Line}
 * @see Figure
 */
public class Line extends Figure {
    public ArrayList<Point> points = new ArrayList<>();

    /**
     * Constructeur de la {@code Line}
     * @param points liste de points constituant la {@code Line}
     * @param name nom de la {@code Line}
     */
    public Line(ArrayList<Point> points, String... name) {
        super(name);
        this.points.addAll(points);
    }

    /**
     * Constructeur par défaut, crée une {@code Line} vide.
     */
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

    /**
     * Convertit la {@code Line} en {@link Polygon}
     * @return un {@link Polygon} constitué des mêmes points que la {@link Line}
     */
    @SuppressWarnings("unused")
    public Polygon asPolygon() {
        this.points.add(this.points.get(0));
        return new Polygon(this.points);
    }

    @Override
    public String export_kml() {
        StringBuilder coords = new StringBuilder();
        this.points.add(this.points.get(0));
        for (Point p : this.points) {
            coords.append(p.longitude).append(",").append(p.latitude).append(",0 \n");
        }
        return KMLUtils.kmlPolygon(this.name, "#style_line", coords.toString());
    }

}

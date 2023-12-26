package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.KMLUtils;

import java.util.ArrayList;


/**
 * Implémentation du Polygone
 * @see Line
 */
public class Polygon extends Line {

    /**
     * Instanciation d'un Polygone
     * @param points points du polygone
     * @param name nom du polygone
     */
    public Polygon(ArrayList<Point> points, String... name) {
        super(points, name);
    }

    /**
     * Constructeur par défaut du Polygone, crée un Polygone vide.
     */
    public Polygon() {
        super();
        this.points = new ArrayList<>();
    }

    /**
     * Crée un {@code Polygon} à partir d'une {@code Line}
     * @param line ligne à convertir
     * @return un {@code Polygon} constitué des mêmes points que la {@link Line}
     * @see Line#asPolygon()
     */
    public Polygon fromLine(Line line) {
        return new Polygon(line.points, line.name);
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
        // on s'assure que le polygone est fermé
        if (this.points.get(0) != this.points.get(this.points.size() - 1)) this.points.add(this.points.get(0));
        for (Point p : this.points) {
            coords.append(p.longitude).append(",").append(p.latitude).append(",0 \n");
        }
        return KMLUtils.kmlPolygon(this.name, "#style_shape", coords.toString());
    }
}

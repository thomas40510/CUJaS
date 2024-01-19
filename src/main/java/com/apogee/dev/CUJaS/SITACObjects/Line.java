package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.KMLUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

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

    public Line (@Nullable String name, Point... points) {
        super(name == null ? "" : name);
        if (points.length == 0) this.points = new ArrayList<>();
        this.points.addAll(Arrays.asList(points));
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
        String style = (this.name.contains("corr")) ? "#style_porte" : "#style_line";
        return export_kml(style);
    }

    public String export_kml(String style) {
        StringBuilder coords = new StringBuilder();
        for (Point p : this.points) {
            coords.append(p.longitude).append(",").append(p.latitude).append(",0 \n");
        }
        return KMLUtils.kmlLine(this.name, style, coords.toString());
    }

}

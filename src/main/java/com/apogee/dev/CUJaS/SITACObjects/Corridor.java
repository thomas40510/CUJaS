package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.GeomUtils;
import org.jetbrains.annotations.Nullable;

/**
 * Implémentation du Couloir.
 * <br>
 * Un couloir est une figure géométrique définie par une position de départ, une position d'arrivée et une largeur.
 * @see Figure
 * TODO: implémenter l'export en KML
 */
public class Corridor extends Figure {
    public Point start_point, end_point;
    private @Nullable Point center;
    public double width;

    /**
     * Constructeur d'un {@code Corridor}
     * @param start_point position de départ
     * @param end_point position d'arrivée
     * @param width largeur du {@code Corridor}
     * @param name nom du {@code Corridor}
     */
    public Corridor(Point start_point, Point end_point, double width, String... name) {
        super(name);
        this.start_point = as_point(start_point);
        this.end_point = as_point(end_point);
        this.width = width;
    }

    /**
     * Constructeur d'un {@code Corridor} à partir d'un centre, d'une largeur et d'un angle (pour implémentation Melissa).
     * @param center centre du {@code Corridor}
     * @param width largeur du {@code Corridor}
     * @param angle angle du {@code Corridor} par rapport à l'axe horizontal
     */
    public Corridor(Point center, double width, double angle) {
        Point[] points = GeomUtils.calcCorridorPoints(center, width, angle);
        this.start_point = points[0];
        this.end_point = points[1];
        this.width = width;
        this.center = center;
    }

    /**
     * <i>Getter</i> du centre du {@code Corridor}.
     * <br>
     * Pour alléger le calcul, on ne le calcule que s'il n'est pas explicitement spécifié par la SITAC.
     * @return le {@code Point} central du {@code Corridor}
     * @see GeomUtils#calcCenterPoint(Point, Point)
     */
    public Point center() {
        return (this.center == null) ? GeomUtils.calcCenterPoint(this.start_point, this.end_point) : this.center;
    }

    /**
     * <i>Getter</i> du delta horizontal de l'ouverture du {@code Corridor}.
     * @return la différence numérique de latitude entre le centre une extrémité du {@code Corridor}
     */
    public double dx() {
        return this.end_point.latitude - this.center().latitude;
    }

    /**
     * <i>Getter</i> du delta vertical de l'ouverture du {@code Corridor}.
     * @return la différence numérique de longitude entre le centre une extrémité du {@code Corridor}
     */
    public double dy() {
        return this.end_point.longitude - this.center().longitude;
    }

    @Override
    public String toString() {
        return this.name + " " + this.start_point + " " + this.end_point + " " + this.width;
    }

}

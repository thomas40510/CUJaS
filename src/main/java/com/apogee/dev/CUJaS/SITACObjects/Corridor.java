package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.GeomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implémentation du Couloir.
 * <br>
 * Un couloir est une figure géométrique définie par une position de départ, une position d'arrivée et une largeur.
 * @see Figure
 */
public class Corridor extends Figure {
    public Point start_point, end_point;
    private @Nullable Point center;
    public double width;
    /** Taille des portes du corridor en mètre */
    private static final double DELIMITER_SIZE = 2500;

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
    public Corridor(@NotNull Point center, double width, double angle) {
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

    /**
     * Export en KML du {@code Corridor}.
     * <br>
     * On génère deux lignes parallèles, orthogonales à la ligne reliant les deux extrémités du {@code Corridor}.
     * @return le code KML du {@code Corridor}
     */
    @Override
    public String export_kml() {
        String style = "#style_porte";
        StringBuilder res = new StringBuilder();

        // on s'assure que les délimiteurs ont une taille fixe
        final double angular_size = GeomUtils.meter2degree(DELIMITER_SIZE);
        final double size_coeff = Math.max(angular_size/this.dx(), angular_size/this.dy());
        // on calcule les deltas de position
        final double delta_y = size_coeff * this.dy();
        final double delta_x = size_coeff * this.dx();

        // premier côté
        Point ps1 = new Point(this.start_point.latitude - delta_y,
                this.start_point.longitude + delta_x);
        Point ps2 = new Point(this.start_point.latitude + delta_y,
                this.start_point.longitude - delta_x);
        res.append((new Line("corr1", ps1, start_point, ps2)).export_kml(style));

        // second côté
        Point pe1 = new Point(this.end_point.latitude - delta_y,
                this.end_point.longitude + delta_x);
        Point pe2 = new Point(this.end_point.latitude + delta_y,
                this.end_point.longitude - delta_x);
        res.append((new Line("corr2", pe1, end_point, pe2)).export_kml(style));

        return res.toString();
    }

}

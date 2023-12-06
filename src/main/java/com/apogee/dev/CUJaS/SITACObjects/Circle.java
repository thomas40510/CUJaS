package com.apogee.dev.CUJaS.SITACObjects;

/**
 * Implémentation du Cercle.
 * <br>
 * Un cercle est une ellipse dont les deux rayons sont égaux, et l'inclinaison nulle.
 * @see Ellipse
 * @see Figure
 */
public class Circle extends Ellipse {
    public Point center;
    public double radius;

    /**
     * Constructeur d'un cercle
     * @param center centre du cercle
     * @param radius rayon du cercle
     * @param name nom du cercle
     */
    public Circle(Point center, double radius, String... name) {
        super(center, radius, radius, 0, name);
        this.center = as_point(center);
        this.radius = radius;
    }

    @Override
    public String toString() {
        return this.name + " " + this.center + " " + this.radius;
    }

    @Override
    public String export_kml() {
        return super.export_kml();
    }
}

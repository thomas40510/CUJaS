package com.apogee.dev.CUJaS.SITACObjects;

/**
 * Implémentation du Couloir
 * @see Figure
 * @implNote Un couloir est une figure géométrique définie par une position de départ, une position d'arrivée et une largeur
 * TODO: implémenter l'export en KML
 */
public class Corridor extends Figure {
    public Point start_point, end_point;
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

    @Override
    public String toString() {
        return this.name + " " + this.start_point + " " + this.end_point + " " + this.width;
    }

}

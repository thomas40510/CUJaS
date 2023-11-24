package com.apogee.dev.CUJaS.SITACObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Classe abstraite pour les figures géométriques d'une SITAC, et méthodes utilitaires associées.
 * @author PRV
 * @version 1.0
 */
public abstract class Figure implements SITACObject {
    protected static final Logger logger = LogManager.getLogger(Figure.class);

    public String name;

    /**
     * Constructeur par défaut.
     * @param name nom de la figure
     */
    public Figure(String... name) {
        this.name = this.getClass().getSimpleName();
        try {
            this.name = (name[0] != null) ? name[0] : this.name;
        } catch (Exception e) {
            logger.debug("No name provided. Using default (" + this.name + ").");
        }
    }

    /**
     * Création d'un {@code Point} à partir d'un tableau de doubles.
     * @param coords tableau de doubles
     * @return le {@code Point} créé
     */
    public static Point as_point(double[] coords) {
        return new Point(coords[0], coords[1]);
    }

    /**
     * Conversion d'un objet en {@code Point}.
     * @param obj objet à convertir
     * @return le {@code Point} créé. Par défaut, retourne le point (0, 0).
     */
    public static Point as_point(Object obj) {
        if (obj instanceof Point) {
            return (Point) obj;
        } else if (obj instanceof double[]) {
            return as_point((double[]) obj);
        } else if (obj instanceof ArrayList) {
            double lat = (double) ((ArrayList<?>) obj).get(0);
            double lon = (double) ((ArrayList<?>) obj).get(1);
            return new Point(lat, lon);
        } else {
            return new Point(0, 0);
        }
    }

    /**
     * Conversion d'une liste d'objets en liste de {@code Point}.
     * @param points liste d'objets à convertir
     * @return la liste de {@code Point} créée
     * @see #as_point(Object)
     */
    @SuppressWarnings("unused")
    public static ArrayList<Point> as_points(ArrayList<Object> points) {
        ArrayList<Point> res = new ArrayList<>();
        for(Object o : points) {
            res.add(as_point(o));
        }
        return res;
    }

    /**
     * Lecture du nom de la figure.
     * @return le nom de la figure
     */
    public String getName() {
        return this.name;
    }

    /**
     * Conversion de la figure en objets KML.
     * @return la construction de la figure en KML
     * @implNote Par défaut, on renvoie une chaîne vide (on ignore la figure).
     * @implSpec La coordonnée d'altitude, requise par le format KML, est fixée à 0.
     * Elle pourra être extraite de la SITAC et prise en compte.
     */
    public String export_kml() {
        logger.warn("KML export not implemented for " + this.getClass().getSimpleName() + ". I'm ignoring it.");
        return "";
    }

}

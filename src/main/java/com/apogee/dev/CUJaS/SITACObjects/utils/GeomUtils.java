package com.apogee.dev.CUJaS.SITACObjects.utils;

import com.apogee.dev.CUJaS.SITACObjects.Circle;
import com.apogee.dev.CUJaS.SITACObjects.Ellipse;
import com.apogee.dev.CUJaS.SITACObjects.Point;
import com.apogee.dev.CUJaS.SITACObjects.Rectangle;

import java.util.ArrayList;

/**
 * Méthodes utilitaires pour la géométrie
 */
public class GeomUtils {
    /**
     * Conversion d'un angle en radians
     * @param angle angle en degrés
     * @return angle en radians
     */
    private static double rad(double angle) {
        angle %= 360;
        return angle * Math.PI / 180;
    }

    /**
     * Conversion d'une distance métrique en distance angulaire
     * @param meter distance métrique
     * @return distance angulaire
     */
    public static double meter2degree(double meter) {
        return meter / 111111;
    }

    /**
     * Nombre de points à générer pour une ellipse ou un cercle
     */
    private static final int NB_POINTS = 60;


    /**
     * Génération des points de contour d'une {@code Ellipse}.
     * @param ellipse une Ellipse quelconque
     * @return une liste de {@value NB_POINTS} {@code Points} décrivant le contour de l'ellipse
     * @implNote À partir du centre et des rayons de l'ellipse,
     * on génère {@value NB_POINTS} points uniformément répartis sur son contour
     * en utilisant sa représentation paramétrique et prenant en compte son angle de rotation.
     */
    public static ArrayList<Point> makeEllipse(Ellipse ellipse) {
        ArrayList<Point> points = new ArrayList<>();
        double x = ellipse.center.latitude;
        double y = ellipse.center.longitude;
        double angle = rad(ellipse.angle);
        double hrad = meter2degree(ellipse.hradius);
        double vrad = meter2degree(ellipse.vradius);

        double angle_step = rad((double) 360 / NB_POINTS);

        for (int i = 0; i < NB_POINTS; i++) {
            double latitude = x + hrad * Math.cos(i * angle_step + angle);
            double longitude = y + vrad * Math.sin(i * angle_step + angle);
            points.add(new Point(latitude, longitude, ellipse.name+"_Pt"+i));
        }
        points.add(points.get(0)); // make it a closed contour

        return points;
    }

    /**
     * Génération des points de contour d'un {@code Circle}.
     * @param circle un Cercle quelconque
     * @return une liste de {@value NB_POINTS} {@code Points} décrivant le contour du cercle
     * @implNote On utilise la méthode {@link GeomUtils#makeEllipse} en passant un {@link Ellipse} dont les rayons sont égaux.
     * @see GeomUtils#makeEllipse
     */
    public static ArrayList<Point> makeCircle(Circle circle) {
        return makeEllipse(circle);
    }

    /**
     * Génération des sommets d'un rectangle.
     * @param rectangle un Rectangle quelconque
     * @return une liste de 5 {@code Points} décrivant le contour fermé du rectangle
     * @implNote À partir du {@code Point} de départ et des dimensions du rectangle,
     * on génère les 4 sommets du rectangle et on renvoie un contour fermé.
     */
    public static ArrayList<Point> makeRectangle(Rectangle rectangle) {
        double start_lat = rectangle.start.latitude;
        double start_lon = rectangle.start.longitude;
        double end_lat = start_lat + meter2degree(rectangle.horizontal);
        double end_lon = start_lon + meter2degree(rectangle.vertical);

        ArrayList<Point> rectPoints = new ArrayList<>();
        rectPoints.add(new Point(start_lat, start_lon, rectangle.name+"_Pt0"));
        rectPoints.add(new Point(start_lat, end_lon, rectangle.name+"_Pt1"));
        rectPoints.add(new Point(end_lat, end_lon, rectangle.name+"_Pt2"));
        rectPoints.add(new Point(end_lat, start_lon, rectangle.name+"_Pt3"));
        rectPoints.add(new Point(start_lat, start_lon, rectangle.name+"_Pt4"));

        return rectPoints;
    }

    /**
     * Génération d'une ligne à partir d'un centre, d'une longueur et d'un angle.
     * @param center {@code Point} central de la ligne
     * @param length longueur de la ligne
     * @param angle angle de la ligne avec l'horizontale
     * @return une liste de 3 {@code Points} décrivant la ligne
     * @implNote On génère simplement le diamètre d'un cercle de rayon {@code length} centré sur {@code center}
     */
    public static ArrayList<Point> makeLineFromRadius(Point center, double length, double angle) {
        ArrayList<Point> points = new ArrayList<>();
        double line_angle = rad(angle);
        Point start = new Point (center.latitude + length * Math.cos(line_angle),
                center.longitude + length * Math.sin(line_angle));
        Point end = new Point(center.latitude - length * Math.cos(line_angle),
                center.longitude - length * Math.sin(line_angle));
        points.add(start);
        points.add(center);
        points.add(end);

        return points;
    }

}

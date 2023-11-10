package com.apogee.dev.CUJaS.SITACObjects.utils;

import com.apogee.dev.CUJaS.SITACObjects.Circle;
import com.apogee.dev.CUJaS.SITACObjects.Ellipse;
import com.apogee.dev.CUJaS.SITACObjects.Point;

import java.util.ArrayList;


public class GeomUtils {
    /**
     * Convert degrees to radians
     * @param angle in degrees
     * @return a value in radians
     */
    private double rad(double angle) {
        return angle * Math.PI / 180;
    }

    /**
     * Convert meters to degrees, so that we can translate a distance into a coordinate
     * @param meter distance in meters
     * @return a value in degrees
     */
    private double meter2degree(double meter) {
        return meter / 111111;
    }

    // number of points to generate for an ellipse or a circle
    private static final int N = 60;


    /**
     * From an Ellipse, generate the contour points
     * @param ellipse Ellipse to generate the contour of
     * @return a list of points along the contour of the Ellipse
     */
    public ArrayList<Point> makeEllipse(Ellipse ellipse) {
        ArrayList<Point> points = new ArrayList<>();
        double x = ellipse.center.latitude;
        double y = ellipse.center.longitude;
        double angle = rad(ellipse.angle);
        double hrad = meter2degree(ellipse.hradius);
        double vrad = meter2degree(ellipse.vradius);

        double angle_step = rad((double) 360 / N);

        for (int i = 0 ; i < N ; i++) {
            double latitude = x + hrad * Math.cos(i * angle_step + angle);
            double longitude = y + vrad * Math.sin(i * angle_step + angle);
            points.add(new Point(latitude, longitude, ellipse.name+"_Pt"+i));
        }
        points.add(points.get(0)); // make it a closed contour

        return points;
    }

    /**
     * From a Circle, generate the contour points
     * @see GeomUtils#makeEllipse
     * @param circle Circle to generate the contour of
     * @return a list of points along the contour of the Circle
     */
    public ArrayList<Point> makeCircle(Circle circle) {
        return makeEllipse(circle);
    }
}
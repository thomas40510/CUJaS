package com.apogee.dev.CUJaS.SITACObjects.utils;

import com.apogee.dev.CUJaS.SITACObjects.Circle;
import com.apogee.dev.CUJaS.SITACObjects.Ellipse;
import com.apogee.dev.CUJaS.SITACObjects.Point;

import java.util.ArrayList;


public class GeomUtils {
    private static final int N = 400;

    private double rad(double angle) {
        return angle * Math.PI / 180;
    }

    public ArrayList<Point> makeEllipse(Ellipse ellipse) {
        // from center, hradius and vradius, calculate coordinates of ellipse border in N points
        ArrayList<Point> points = new ArrayList<>();
        double x = ellipse.center.latitude;
        double y = ellipse.center.longitude;
        double angle = rad(ellipse.angle);

        double angle_step = rad((double) 360 / N);

        for (int i = 0 ; i < N ; i++) {
            double latitude = x + ellipse.hradius * Math.cos(i * angle_step + angle);
            double longitude = y + ellipse.vradius * Math.sin(i * angle_step + angle);
            points.add(new Point(latitude, longitude, ellipse.name+"_Pt"+i));
        }

        return points;
    }

    public ArrayList<Point> makeCircle(Circle circle) {
        return makeEllipse(circle);
    }
}

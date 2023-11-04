package com.apogee.dev.CUJaS.SITACObjects;

import java.util.ArrayList;

public abstract class Figure implements SITACObject {
    public String name = "Figure";
    public Figure(String... name) {
        if (name.length > 0) {
            this.name = name[0];
        }
    }

    public static Point as_point(double[] point) {
        return new Point(point[0], point[1]);
    }

    public static Point as_point(double latitude, double longitude) {
        return new Point(latitude, longitude);
    }
    public static Point as_point(Point p) {
        return p;
    }
    public static Point as_point() {
        return new Point(0.0, 0.0);
    }
    public static Point as_point(Object o) {
        if (o instanceof Point) {
            return (Point) o;
        } else if (o instanceof double[]) {
            return as_point((double[]) o);
        } else if (o instanceof ArrayList) {
            return as_point(o);
        } else {
            return as_point();
        }
    }

    public static ArrayList<Point> as_points(ArrayList<Object> points) {
        ArrayList<Point> res = new ArrayList<>();
        for(Object o : points) {
            res.add(as_point(o));
        }
        return res;
    }


    public String getName() {
        return this.name;
    }
}

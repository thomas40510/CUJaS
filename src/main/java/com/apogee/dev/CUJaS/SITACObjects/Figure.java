package com.apogee.dev.CUJaS.SITACObjects;

import java.util.ArrayList;

public abstract class Figure {
    public String name = "Figure";
    public Figure(String... name) {
        if (name.length > 0) {
            this.name = name[0];
        }
    }

    public Figure() {
        this.name = "Figure";
    }

    public void accept_visitor(Visitor v) {
        v.visit(this);
    }

    public Point as_point(double[] point) {
        return new Point(point[0], point[1]);
    }
    public Point as_point(double latitude, double longitude) {
        return new Point(latitude, longitude);
    }
    public Point as_point(Point p) {
        return p;
    }
    public Point as_point() {
        return new Point(0.0, 0.0);
    }
    public Point as_point(Object o) {
        switch (o.getClass().getName()) {
            case "double[]":
                return as_point((double[]) o);
            case "Point":
                return as_point((Point) o);
            default:
                return as_point();
        }
    }

    public ArrayList<Point> as_points(ArrayList<Object> points) {
        ArrayList<Point> res = new ArrayList<>();
        for(Object o : points) {
            res.add(as_point(o));
        }
        return res;
    }
}

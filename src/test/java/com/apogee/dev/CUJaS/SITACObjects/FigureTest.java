package com.apogee.dev.CUJaS.SITACObjects;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {

    @Test
    void test_as_point_lat_long() {
        int lat = 0, lon = 0;
        Point p = new Point(0.0, 0.0);
        assertInstanceOf(Point.class, Figure.as_point(lat, lon));
        assertEquals(p, Figure.as_point(lat, lon));
    }

    @Test
    void test_as_point_double() {
        double[] point = {0.0, 0.0};
        Point p = new Point(0.0, 0.0);
        assertInstanceOf(Point.class, Figure.as_point(point));
        assertEquals(p, Figure.as_point(point));
    }

    @Test
    void test_as_point_point() {
        Point p = new Point(0.0, 0.0);
        assertInstanceOf(Point.class, Figure.as_point(p));
        assertEquals(p, Figure.as_point(p));
    }

    @Test
    void test_as_point_null() {
        Point p = new Point(0.0, 0.0);
        assertInstanceOf(Point.class, Figure.as_point());
        assertEquals(p, Figure.as_point());
    }

    @Test
    void test_as_point_object() {
        Object o = new Point(0.0, 0.0);
        Point p = new Point(0.0, 0.0);
        assertInstanceOf(Point.class, Figure.as_point(o));
        assertEquals(p, Figure.as_point(o));
    }

    @Test
    void test_as_points() {
        ArrayList<Object> points = new ArrayList<>();
        points.add(new Point(0.0, 0.0));
        points.add(new Point(1.0, 1.0));
        points.add(new double[] {2.3, 4.5});
        ArrayList<Point> res = new ArrayList<>();
        res.add(new Point(0.0, 0.0));
        res.add(new Point(1.0, 1.0));
        res.add(new Point(2.3, 4.5));
        ArrayList<Point> aspoints = Figure.as_points(points);
        for (int i = 0; i < points.size(); i++) {
            assertEquals(res.get(i), aspoints.get(i));
        }
    }
}
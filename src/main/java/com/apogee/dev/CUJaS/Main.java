package com.apogee.dev.CUJaS;

import com.apogee.dev.CUJaS.CUJaS_Core.NTKSemantics;
import com.apogee.dev.CUJaS.CUJaS_Core.XMLParser;
import com.apogee.dev.CUJaS.SITACObjects.Line;
import com.apogee.dev.CUJaS.SITACObjects.Point;
import com.apogee.dev.CUJaS.SITACObjects.Polygon;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String current_path = System.getProperty("user.dir");
        // read file in 'resources' folder
        String filename = current_path + "/src/main/resources/test.xml";

        Point p1 = new Point(0.0, 0.0);
        Point p2 = new Point(1.0, 1.0);
        Point p3 = new Point(2.0, 2.0);
        Point p4 = new Point(3.0, 3.0);

        ArrayList<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        Line l1 = new Line(points);
        System.out.println(l1);
        Polygon pol = new Polygon().fromLine(l1);
        System.out.println(pol);
    }
}

package com.apogee.dev.CUJaS;

import com.apogee.dev.CUJaS.CUJaS_Core.XMLReader;
import com.apogee.dev.CUJaS.SITACObjects.Circle;
import com.apogee.dev.CUJaS.SITACObjects.Point;
import com.apogee.dev.CUJaS.SITACObjects.Polygon;
import com.apogee.dev.CUJaS.SITACObjects.Rectangle;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String current_path = System.getProperty("user.dir");
        // read file in 'resources' folder
        String filename = current_path + "/src/main/resources/test.xml";
        XMLReader reader = new XMLReader(filename);
        Circle c = new Circle(0.0, 3.0);
        Rectangle r = new Rectangle(new Point(.3,.4), 3.0, 4.0);
        System.out.println(c);
        System.out.println(r);
        reader.extractFigures();
        reader.displayFigures();
    }
}

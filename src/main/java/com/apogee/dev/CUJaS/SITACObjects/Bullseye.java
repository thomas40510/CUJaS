package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.GeomUtils;
import com.apogee.dev.CUJaS.SITACObjects.utils.KMLUtils;

import java.util.ArrayList;

public class Bullseye extends Figure {
    public Point center;
    public double hradius, vradius;
    public int rings;
    public double ring_distance;

    public Bullseye(Object center, double hradius, double vradius, int rings, double ring_distance, String... name) {
        super(name);
        this.center = as_point(center);
        this.hradius = hradius / 2;
        this.vradius = vradius / 2;
        this.rings = rings;
        this.ring_distance = ring_distance;
    }

    @Override
    public String toString() {
        return "(BullsEye) " + this.name + " "
                + this.center + " "
                + this.hradius + " "
                + this.vradius + " "
                + this.rings + " "
                + this.ring_distance;
    }

    @Override
    public String export_kml() {
        double dist = this.ring_distance / 2;
        double radius = this.vradius;

        double smallest_radius = radius - (this.rings * dist);

        ArrayList<Circle> tmp = new ArrayList<>();

        for (int i = 0 ; i < rings; i += 2) {
            double rad = smallest_radius + i * dist;
            // TODO: chercher pourquoi on a un facteur 2 qui traîne
            tmp.add(new Circle(this.center, 2 * rad));
        }
        String res_major = KMLUtils.multiGeom(this.name + "_major", "#style_bulls", genCircles(tmp));

        tmp.clear();

        for (int i = 1 ; i < this.rings ; i += 2) {
            double rad = smallest_radius + i * dist;
            tmp.add(new Circle(this.center, 2 * rad));
        }
        String res_minor = KMLUtils.multiGeom(this.name + "_minor", "#style_bulls_thin", genCircles(tmp));

        String res_cross = KMLUtils.multiGeom(this.name + "_minor", "#style_line", genCross(this.center, radius));

        return res_major + res_minor + res_cross;
    }

    private static String genCross(Point center, double radius) {
        int alpha = 45;
        StringBuilder res = new StringBuilder();
        for (int angle = 0 ; angle <= 360 ; angle += alpha) {
            StringBuilder tmp = new StringBuilder();
            for (Point p : GeomUtils.makeLineFromRadius(center, GeomUtils.meter2degree(radius), angle)) {
                tmp.append(p.longitude).append(",").append(p.latitude).append(",0\n");
            }
            res.append(KMLUtils.rawLine(tmp.toString()));
        }
        return res.toString();
    }

    private static String genCircles(ArrayList<Circle> tmp) {
        StringBuilder tmpString = new StringBuilder();
        for (Circle c : tmp) {
            StringBuilder curr_coords = new StringBuilder();
            ArrayList<Point> cpoints = GeomUtils.makeCircle(c);
            for (Point p : cpoints) {
                curr_coords.append(p.longitude).append(",").append(p.latitude).append(",0\n");
            }
            tmpString.append(KMLUtils.rawPoly(curr_coords.toString()));
        }
        return tmpString.toString();
    }
}

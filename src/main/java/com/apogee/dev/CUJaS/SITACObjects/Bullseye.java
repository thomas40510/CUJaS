package com.apogee.dev.CUJaS.SITACObjects;

import com.apogee.dev.CUJaS.SITACObjects.utils.GeomUtils;
import com.apogee.dev.CUJaS.SITACObjects.utils.KMLUtils;

import java.util.ArrayList;

/**
 * Implémentation du Bullseye
 * @see <a href="http://www.185th.co.uk/squad_info/training/basic_n&b.htm">BullsEye</a>
 */
public class Bullseye extends Figure {
    public Point center;
    public double hradius, vradius;
    public int rings;
    public double ring_distance;

    /**
     * Constructeur du Bullseye
     * @param center centre
     * @param hradius rayon horizontal
     * @param vradius rayon vertical
     * @param rings nombre d'anneaux
     * @param ring_distance distance entre les anneaux (en mètres)
     * @param name nom du Bullseye
     */
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

    /**
     * Exportation du {@code Bullseye} en KML.
     * <br>
     * En pratique :
     * <ul>
     *     <li>on génère les cercles primaires ({@code major}) en partant du plus petit</li>
     *     <li>on génère les cercles secondaires ({@code minor}) en partant du plus petit</li>
     *     <li>on génère la croix</li>
     *     <li>on concatène le tout</li>
     * </ul>
     * @return le fragment KML correspondant au {@code Bullseye}
     */
    @Override
    public String export_kml() {
        double dist = this.ring_distance / 2;
        double radius = this.vradius;

        double smallest_radius = radius - (this.rings * dist);

        ArrayList<Circle> tmp = new ArrayList<>();

        // Cercles primaires
        for (int i = 0 ; i < rings; i += 2) {
            double rad = smallest_radius + i * dist;
            // TODO: chercher pourquoi on a un facteur 2 qui traîne
            tmp.add(new Circle(this.center, 2 * rad));
        }
        String res_major = KMLUtils.multiGeom(this.name + "_major", "#style_bulls", genCircles(tmp));

        // inutile d'instancier une nouvelle ArrayList, on réutilise la même qu'on vide
        tmp.clear();

        // Cercles secondaires
        for (int i = 1 ; i < this.rings ; i += 2) {
            double rad = smallest_radius + i * dist;
            tmp.add(new Circle(this.center, 2 * rad));
        }
        String res_minor = KMLUtils.multiGeom(this.name + "_minor", "#style_bulls_thin", genCircles(tmp));

        // Croix
        String res_cross = KMLUtils.multiGeom(this.name + "_minor", "#style_line", genCross(this.center, radius));

        return res_major + res_minor + res_cross;
    }

    private static final int CROSS_ALPHA = 30;

    /**
     * Génère la croix du Bullseye.
     * <br>
     * On trace, tous les {@value CROSS_ALPHA} degrés,
     * un diamètre du cercle de centre {@code center} et de rayon {@code radius}.
     * @param center centre de la croix
     * @param radius rayon de la croix
     * @return le fragment KML correspondant à la croix
     */
    private static String genCross(Point center, double radius) {
        StringBuilder res = new StringBuilder();
        for (int angle = 0 ; angle <= 360 ; angle += CROSS_ALPHA) {
            StringBuilder tmp = new StringBuilder();
            for (Point p : GeomUtils.makeLineFromRadius(center, GeomUtils.meter2degree(radius), angle)) {
                tmp.append(p.longitude).append(",").append(p.latitude).append(",0\n");
            }
            res.append(KMLUtils.rawLine(tmp.toString()));
        }
        return res.toString();
    }

    /**
     * Génère les cercles du Bullseye
     * @param tmp liste des cercles à générer
     * @return le fragment KML correspondant aux cercles
     */
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

package com.apogee.dev.CUJaS.SITACObjects;

/**
 * Implémentation d'un point géographique.
 * @see <a href="https://fr.wikipedia.org/wiki/Coordonn%C3%A9es_g%C3%A9ographiques">Point Géographique</a>
 */
public class Point extends Figure {
    public double latitude, longitude;

    /**
     * Constructeur d'un point géographique
     * @param latitude latitude du point
     * @param longitude longitude du point
     * @param name nom du point
     */
    public Point(double latitude, double longitude, String... name) {
        super(name);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Opération d'égalité sur les points.
     * Deux points sont égaux si leurs latitudes et longitudes sont respectivement égales.
     * @param other autre point
     * @return {@code true} si les deux points sont égaux, {@code false} sinon
     */
    @Override
    public boolean equals(Object other) {
        try {
            Point pother = (Point) other;
            return this.latitude == pother.latitude && this.longitude == pother.longitude;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.name + " (" + this.latitude + "," + this.longitude + ")";
    }

    /**
     * @see <a href="https://developers.google.com/kml/documentation/kmlreference#placemark">KML Placemark</a>
     */
    @Override
    public String export_kml() {
        String style = "style_point";
        String coordinates = latitude + "," + longitude;
        return "<Placemark>\n" +
                "    <name>" + this.name + "</name>\n" +
                "    <description> un point </description>\n" +
                "    <styleUrl>" + style + "</styleUrl>\n" +
                "    <Point>\n" +
                "        <coordinates>" + coordinates + "</coordinates>\n" +
                "    </Point>\n" +
                "</Placemark>";
    }
}

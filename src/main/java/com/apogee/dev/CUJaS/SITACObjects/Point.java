package com.apogee.dev.CUJaS.SITACObjects;

public class Point extends Figure {
    public double latitude, longitude;

    public Point(double latitude, double longitude, String... name) {
        super(name);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Point p = (Point) o;
            return this.latitude == p.latitude && this.longitude == p.longitude;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.name + " (" + this.latitude + "," + this.longitude + ")";
    }

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

package com.apogee.dev.CUJaS.CUJaS_Core.utils;

// https://stackoverflow.com/a/17364689/8361698
public class KMLDefaults {
    public KMLDefaults() {
        return;
    }
    public static class Placemark {
        public String name;
        public String description;
        public String styleUrl;
        public String coordinates;
        public String altitudeMode;
        public Placemark(String name, String description, double lat, double lon) {
            this.name = name;
            this.description = description;
            this.styleUrl = "#default";
            this.coordinates = lon + "," + lat;
            this.altitudeMode = "clampToGround";
        }
        public String export() {
            return "<Placemark>\n" +
                    "    <name>" + this.name + "</name>\n" +
                    "    <description>" + this.description + "</description>\n" +
                    "    <styleUrl>" + this.styleUrl + "</styleUrl>\n" +
                    "    <Point>\n" +
                    "        <coordinates>" + this.coordinates + "</coordinates>\n" +
                    "    </Point>\n" +
                    "</Placemark>";
        }
    }
}

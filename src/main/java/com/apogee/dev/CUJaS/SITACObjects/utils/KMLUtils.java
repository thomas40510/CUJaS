package com.apogee.dev.CUJaS.SITACObjects.utils;

public class KMLUtils {
    public static String kmlPolygon(String name, String styleUrl, String coords) {
        return """
                <Placemark>
                    <name>%s</name>
                    <styleUrl>%s</styleUrl>
                    <Polygon>
                        <outerBoundaryIs>
                            <LinearRing>
                                <coordinates>
                                    %s
                                </coordinates>
                            </LinearRing>
                        </outerBoundaryIs>
                    </Polygon>
                  </Placemark>""".formatted(name, styleUrl, coords);
    }

    public static String rawLine(String coords) {
        return """
                <LineString>
                    <coordinates>
                        %s
                    </coordinates>
                </LineString>
                """.formatted(coords);
    }

    public static String rawPoly(String coords) {
        return """
                <Polygon>
                    <outerBoundaryIs>
                        <LinearRing>
                            <coordinates>
                                %s
                            </coordinates>
                        </LinearRing>
                    </outerBoundaryIs>
                </Polygon>
                """.formatted(coords);
    }

    public static String multiGeom(String name, String styleUrl, String elem) {
        return """
                <Placemark>
                    <name>%s</name>
                    <styleUrl>%s</styleUrl>
                    <MultiGeometry>
                    %s
                    </MultiGeometry>
                  </Placemark>""".formatted(name, styleUrl, elem);

    }
}

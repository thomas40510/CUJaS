package com.apogee.dev.CUJaS.SITACObjects.utils;

public class KMLUtils {
    public String kmlPolygon(String name, String styleUrl, String coords) {
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
}

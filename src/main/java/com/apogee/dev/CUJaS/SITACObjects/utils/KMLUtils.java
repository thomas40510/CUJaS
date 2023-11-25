package com.apogee.dev.CUJaS.SITACObjects.utils;

/**
 * Méthodes utilitaires pour l'écriture d'un fichier KML.
 * @author PRV
 * @version 1.0
 * @see <a href="https://developers.google.com/kml/documentation/kmlreference">Documentation du langage KML</a>
 * @see <a href="https://developers.google.com/kml/documentation/kmlreference#style">Styles dans la doc KML</a>
 */
public class KMLUtils {
    /**
     * Génère le KML pour un {@code Polygon}.
     * @param name nom du polygone
     * @param styleUrl référence vers le style à utiliser
     * @param coords concaténation des coordonnées des points du polygone
     * @return le fragment KML correspondant
     * @see <a href="https://developers.google.com/kml/documentation/kmlreference#polygon">Polygon dans la doc KML</a>
     */
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

    /**
     * Génère le fragment KML pour une ligne, sans autre élément de contexte ou style.
     * @param coords concaténation des coordonnées des points de la ligne
     * @return le fragment KML correspondant
     * @see <a href="https://developers.google.com/kml/documentation/kmlreference#linestring">LineString dans la doc KML</a>
     */
    public static String rawLine(String coords) {
        return """
                <LineString>
                    <coordinates>
                        %s
                    </coordinates>
                </LineString>
                """.formatted(coords);
    }

    /**
     * Génère le fragment KML pour un {@code Polygon} quelconque, sans autre élément de contexte ou style.
     * @param coords concaténation des coordonnées des points du polygone
     * @return le fragment KML correspondant
     */
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

    /**
     * Génère le fragment KML pour une {@code MultiGeometry}.
     * @param name nom de la figure
     * @param styleUrl référence vers le style à utiliser
     * @param elem concaténation des éléments de la figure
     * @return le fragment KML correspondant
     * @see <a href="https://developers.google.com/kml/documentation/kmlreference#multigeometry">MultiGeometry dans la doc KML</a>
     */
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

package com.apogee.dev.CUJaS.SITACObjects;

/**
 * Interface pour les objets d'une SITAC.
 * @author PRV
 * @version 1.0
 */
public interface SITACObject {
    String name = "SITACObject";

    /**
     * Conversion en chaîne de caractères pour affichage.
     * @return la figure comme {@code String}
     */
    String toString();

    String getName();

    /**
     * Prise en charge de l'export de la figure en objets KML.
     * @return la construction de la figure en KML
     */
    String export_kml();
}

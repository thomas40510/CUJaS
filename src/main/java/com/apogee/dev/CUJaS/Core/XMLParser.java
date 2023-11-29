package com.apogee.dev.CUJaS.Core;

import com.apogee.dev.CUJaS.SITACObjects.Figure;

import java.util.ArrayList;

/**
 * Interface de Parser XML. Elle définit les méthodes de base pour parser un fichier XML de SITAC.
 */
public interface XMLParser {

    void parse_figures() throws RuntimeException;
    void build_figures();

    /**
     * Renvoie la liste des figures extraites du fichier XML.
     * @return liste des figures
     */
    ArrayList<Figure> getFigures();

}

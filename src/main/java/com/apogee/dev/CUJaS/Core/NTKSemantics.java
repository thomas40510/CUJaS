package com.apogee.dev.CUJaS.Core;

/**
 * Éléments sémantiques pour le format NTK.
 * En pratique, on fait correspondre la syntaxe spécifique NTK aux clés génériques {@link XKey} d'une SITAC.
 * @see Semantics
 * @author PRV
 * @version 1.0
 */
public class NTKSemantics implements Semantics {
    /**
     * Mots-clés spécifiques au format NTK.
     * @see XKey
     */
    public NTKSemantics() {
        this.keywords.put(XKey.BODY, "figures");
        this.keywords.put(XKey.FIGURE, "figure");
        this.keywords.put(XKey.FIG_TYPE, "figureType");
        this.keywords.put(XKey.FIG_NAME, "name");
        this.keywords.put(XKey.FIG_HORIZ, "horizontal");
        this.keywords.put(XKey.FIG_VERT, "vertical");
        this.keywords.put(XKey.FIG_POINTS, "points");
        this.keywords.put(XKey.FIG_POINT, "point");
        this.keywords.put(XKey.PT_LAT, "latitude");
        this.keywords.put(XKey.PT_LON, "longitude");
        this.keywords.put(XKey.BULLSEYE, "bullseye");
        this.keywords.put(XKey.BULLS_RINGS, "numberOfRings");
        this.keywords.put(XKey.BULLS_DIST, "distanceBetweenRing");
        this.keywords.put(XKey.ANGLE, "angle");
    }
}

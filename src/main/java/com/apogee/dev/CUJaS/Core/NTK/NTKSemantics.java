package com.apogee.dev.CUJaS.Core.NTK;

import com.apogee.dev.CUJaS.Core.Semantics;

/**
 * Éléments sémantiques pour le format NTK.
 * En pratique, on fait correspondre la syntaxe spécifique NTK aux clés génériques {@link NTKKey} d'une SITAC.
 * @see Semantics
 * @author PRV
 * @version 1.0
 */
public class NTKSemantics implements Semantics {
    /**
     * Mots-clés spécifiques au format NTK.
     * @see NTKKey
     */
    public NTKSemantics() {
        this.keywords.put(NTKKey.BODY, "figures");
        this.keywords.put(NTKKey.FIGURE, "figure");
        this.keywords.put(NTKKey.FIG_TYPE, "figureType");
        this.keywords.put(NTKKey.FIG_NAME, "name");
        this.keywords.put(NTKKey.FIG_HORIZ, "horizontal");
        this.keywords.put(NTKKey.FIG_VERT, "vertical");
        this.keywords.put(NTKKey.FIG_POINTS, "points");
        this.keywords.put(NTKKey.FIG_POINT, "point");
        this.keywords.put(NTKKey.PT_LAT, "latitude");
        this.keywords.put(NTKKey.PT_LON, "longitude");
        this.keywords.put(NTKKey.BULLSEYE, "bullseye");
        this.keywords.put(NTKKey.BULLS_RINGS, "numberOfRings");
        this.keywords.put(NTKKey.BULLS_DIST, "distanceBetweenRing");
        this.keywords.put(NTKKey.ANGLE, "angle");
    }
}

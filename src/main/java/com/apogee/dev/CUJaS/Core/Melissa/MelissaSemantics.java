package com.apogee.dev.CUJaS.Core.Melissa;

import com.apogee.dev.CUJaS.Core.Semantics;

/**
 * Éléments sémantiques pour le format Melissa.
 * En pratique, on fait correspondre la syntaxe spécifique Melissa aux clés de construction.
 * @see Semantics
 * @see MelissaKey
 * @author PRV
 * @version 1.0
 */
public class MelissaSemantics implements Semantics {
    public MelissaSemantics() {
        this.keywords.put(MelissaKey.BODY, "Sitac");
        this.keywords.put(MelissaKey.POINT, "PointMelissa");
        this.keywords.put(MelissaKey.COORDS, "coordonnees");
        this.keywords.put(MelissaKey.LINE, "Ligne");
        this.keywords.put(MelissaKey.PTS_LINE, "liste");
        this.keywords.put(MelissaKey.ELLIPSE, "Ellipse");
        this.keywords.put(MelissaKey.ELL_RADII, "rayons");
        this.keywords.put(MelissaKey.ANGLE, "angle");
        this.keywords.put(MelissaKey.POLYGON, "Zone");
        this.keywords.put(MelissaKey.CORRIDOR, "Porte");
        this.keywords.put(MelissaKey.BULLS, "Bulls");
        this.keywords.put(MelissaKey.BULLS_RINGS, "nbCercles");
        this.keywords.put(MelissaKey.BULLS_DIST, "pas");
        this.keywords.put(MelissaKey.ANGLE_CORR, "anglePorte");
        this.keywords.put(MelissaKey.SIZE_CORR, "espacementPorte");
        this.keywords.put(MelissaKey.PTS_CORR, "liste");

    }
}

package com.apogee.dev.CUJaS.Core.Melissa;

import com.apogee.dev.CUJaS.Core.XKey;

/**
 * Implémentation des clés génériques {@link XKey} pour les éléments sémantiques Melissa.
 * @see MelissaSemantics
 */
public enum MelissaKey implements XKey {
    BODY,
    POINT,
    COORDS,
    LINE,
    PTS_LINE,
    ELLIPSE,
    ELL_RADII,
    ANGLE,
    POLYGON,
    CORRIDOR,
    ANGLE_CORR,
    SIZE_CORR,
    PTS_CORR,
    BULLS,
    BULLS_RINGS,
    BULLS_DIST,
}

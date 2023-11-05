package com.apogee.dev.CUJaS.CUJaS_Core;

import java.util.HashMap;

//TODO: check if relevant given the xml parsing capabilities of the language
public class NTKSemantics extends Semantics {
    public NTKSemantics() {
        this.keywords = new HashMap<>();
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
    }
}

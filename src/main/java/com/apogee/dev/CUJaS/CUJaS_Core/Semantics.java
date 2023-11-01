package com.apogee.dev.CUJaS.CUJaS_Core;

import java.util.HashMap;


public abstract class Semantics {
    public HashMap<XKey, String> keywords;
    public HashMap<String, String> sems;

    enum XKey {
        BODY,
        FIGURE,
        FIG_TYPE,
        FIG_NAME,
        FIG_HORIZ,
        FIG_VERT,
        FIG_POINTS,
        FIG_POINT
    }

}
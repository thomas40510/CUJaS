package com.apogee.dev.CUJaS.CUJaS_Core;

import java.util.HashMap;

//TODO: check if relevant given the xml parsing capabilities of the language
public class NTKSemantics extends Semantics {
    public NTKSemantics() {
        this.keywords = new HashMap<>();
        this.keywords.put(XKey.BODY, "figures");
        this.keywords.put(XKey.FIGURE, "figure");
    }
}

package com.apogee.dev.CUJaS.Core;

import com.apogee.dev.CUJaS.SITACObjects.Figure;

import java.util.ArrayList;

public interface XMLParser {

    void parse_figures() throws RuntimeException;
    void build_figures();
    ArrayList<Figure> getFigures();

}

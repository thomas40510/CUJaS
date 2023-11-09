package com.apogee.dev.CUJaS.CUJaS_Core;

import com.apogee.dev.CUJaS.SITACObjects.Figure;
import com.apogee.dev.CUJaS.CUJaS_Core.utils.KMLDefaults.*;
import jdk.dynalink.Namespace;
import org.w3c.dom.Document;


import java.util.ArrayList;

public class KMLExporter {
    private ArrayList<Figure> figures;
    private String filepath;
    public KMLExporter(ArrayList<Figure> figures, String filepath) {
        this.figures = figures;
        this.filepath = filepath;
    }

    public void export() {
        Placemark pm = new Placemark("test", "test", 0.0, 0.0);
    }


    public ArrayList<Figure> getFigures() {
        return this.figures;
    }

    public String getFilepath() {
        return this.filepath;
    }
}

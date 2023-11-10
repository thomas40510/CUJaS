package com.apogee.dev.CUJaS;

import com.apogee.dev.CUJaS.CUJaS_Core.NTKSemantics;
import com.apogee.dev.CUJaS.CUJaS_Core.XMLParser;
import com.apogee.dev.CUJaS.SITACObjects.Figure;
import com.apogee.dev.CUJaS.CUJaS_Core.KMLExporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String current_path = System.getProperty("user.dir");
        // read file in 'resources' folder
        String filename = current_path + "/src/main/resources/test.xml";

        XMLParser parser = new XMLParser(filename, new NTKSemantics());
        parser.parse_sitac();
        ArrayList<Figure> figures = parser.getFigures();

        String kml_path = current_path + "/src/main/resources/output.kml";
        KMLExporter exporter = new KMLExporter(figures, kml_path);
        exporter.export();
    }
}

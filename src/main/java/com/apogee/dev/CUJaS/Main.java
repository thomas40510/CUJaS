package com.apogee.dev.CUJaS;

import com.apogee.dev.CUJaS.Core.KMLExporter;
import com.apogee.dev.CUJaS.Core.Melissa.MelissaParser;

public class Main {
    public static void main(String[] args) {
        // run CUJaS_UI.main
        //CUJaS_UI.main(args);

        String current_path = System.getProperty("user.dir");
        // read file in 'resources' folder
        String filename = current_path + "/src/main/resources/MELISSA.stc.xml";

        MelissaParser parser = new MelissaParser(filename);
        parser.parse_figures();
        parser.build_figures();

        String kml_filename = current_path + "/src/main/out/MELISSA.kml";
        KMLExporter exporter = new KMLExporter(parser.getFigures(), kml_filename, null);
        exporter.export();
    }
}

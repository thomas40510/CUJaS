package com.apogee.dev.CUJaS;

public class Main {
    public static void main(String[] args) {
        // run CUJaS_UI.main
        CUJaS_UI.main(args);

/*
        String current_path = System.getProperty("user.dir");
        // read file in 'resources' folder
        String filename = current_path + "/src/main/resources/test.xml";

        XMLParser parser = new XMLParser(filename, new NTKSemantics());
        parser.parse_sitac();
        ArrayList<Figure> figures = parser.getFigures();

        String kml_path = current_path + "/src/main/out/output.kml";
        KMLExporter exporter = new KMLExporter(figures, kml_path);
        exporter.export();
*/
    }
}

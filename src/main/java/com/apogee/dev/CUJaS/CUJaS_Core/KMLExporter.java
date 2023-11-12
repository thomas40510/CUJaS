package com.apogee.dev.CUJaS.CUJaS_Core;

import com.apogee.dev.CUJaS.SITACObjects.Figure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// https://developers.google.com/kml/documentation/kmlreference
public record KMLExporter(ArrayList<Figure> figures, String filepath) {
    private static final Logger logger = LogManager.getLogger(KMLExporter.class);

    public KMLExporter(ArrayList<Figure> figures, String filepath) {
        this.figures = figures;
        this.filepath = filepath;
        logger.info("Initialized exporter for file " + filepath);
    }

    private static final String header = """
            <?xml version="1.0" encoding="UTF-8"?>
                            <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
                            <Document>
            """;

    private static final String footer = "</Document></kml>";

    private String readStyles() {
        logger.debug("Reading styles for SITAC...");
        // read contents of file
        String res = "";
        try {
            Path currentRelativePath = Paths.get("");
            // read file in 'resources' folder as string
            String filename = currentRelativePath.toAbsolutePath() + "/src/main/resources/kml_styles.xml";
            File file = new File(filename);
            res = new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            logger.error(e);
        }
        return res;
    }

    public void export() {
        logger.debug("Exporting figures to KML...");
        StringBuilder figCode = new StringBuilder();
        for (Figure f : this.figures) {
            logger.debug("Exporting figure " + f.name +".");
            figCode.append(f.export_kml());
        }
        String kml = header + readStyles() + figCode + footer;
        try {
            FileOutputStream fos = new FileOutputStream(this.filepath);
            fos.write(kml.getBytes());
            fos.close();
            logger.info("Successfully exported figures to KML. File is at " + this.filepath);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}

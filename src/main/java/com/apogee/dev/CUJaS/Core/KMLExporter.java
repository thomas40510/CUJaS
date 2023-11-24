package com.apogee.dev.CUJaS.Core;

import com.apogee.dev.CUJaS.SITACObjects.Figure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Exportation des figures vers un fichier KML.
 * Voir la <a href="https://developers.google.com/kml/documentation/kmlreference">documentation KML</a> pour connaître les specs.
 * @author PRV
 * @version 1.0
 */
public record KMLExporter(ArrayList<Figure> figures, String filepath, String styles_filepath) {
    private static final Logger logger = LogManager.getLogger(KMLExporter.class);

    /**
     * Prise en charge de l'exportation d'un ensemble de figures vers un fichier KML.
     * @param figures liste des figures à exporter (voir {@link Figure})
     * @param filepath chemin vers le fichier de sortie
     * @param  styles_filepath chemin vers le fichier de styles (optionnel)
     */
    public KMLExporter(ArrayList<Figure> figures, String filepath, @Nullable String styles_filepath) {
        this.figures = figures;
        this.filepath = filepath;
        this.styles_filepath = styles_filepath;
        logger.info("Initialized exporter for file " + filepath);
    }

    /**
     * En-tête du fichier KML.
     */
    private static final String header = """
            <?xml version="1.0" encoding="UTF-8"?>
                            <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
                            <Document>
            """;

    /**
     * {@code footer} du fichier KML.
     */
    private static final String footer = "</Document></kml>";

    /**
     * Lecture du fichier de styles (voir <a href="https://developers.google.com/kml/documentation/kmlreference#style">KML Docs</a>).
     * Si aucun fichier n'a été spécifié, on utilise le fichier par défaut.
     * @return contenu du fichier de styles
     */
    private String readStyles() {
        logger.debug("Reading styles for SITAC...");
        // read contents of file
        String res = "";
        try {
            String filename;
            if (this.styles_filepath == null) {
                Path currentRelativePath = Paths.get("");
                // read file in 'resources' folder as string
                filename = currentRelativePath.toAbsolutePath() + "/src/main/resources/kml_styles.xml";
                logger.info("Using default styles");
            } else {
                filename = this.styles_filepath;
                logger.info("Using custom styles");
            }
            File file = new File(filename);
            res = new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            logger.error(e);
        }
        return res;
    }

    /**
     * Exportation des figures vers un fichier KML. Chaque figure est exportée grâce à sa méthode propre.
     * @see Figure#export_kml()
     */
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

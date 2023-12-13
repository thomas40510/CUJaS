package com.apogee.dev.CUJaS.Core;

import com.apogee.dev.CUJaS.SITACObjects.Figure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Exportation des figures vers un fichier KML.
 * @author PRV
 * @version 1.1
 * @see <a href="https://developers.google.com/kml/documentation/kmlreference">Documentation du langage KML</a>
 */
public record KMLExporter(ArrayList<Figure> figures, String filepath, String styles_filepath) {
    private static final Logger logger = LogManager.getLogger(KMLExporter.class);
    private static boolean force_default = false;

    /**
     * Prise en charge de l'exportation d'un ensemble de figures vers un fichier KML.
     * @param figures liste des figures à exporter (voir {@link Figure})
     * @param filepath chemin vers le fichier de sortie
     * @param  styles_filepath chemin vers le fichier de styles (optionnel)
     * @see <a href="https://developers.google.com/kml/documentation/kmlreference#style">Styles dans la doc KML</a>
     */
    public KMLExporter(ArrayList<Figure> figures, String filepath, @Nullable String styles_filepath) {
        this.figures = figures;
        this.filepath = filepath;
        this.styles_filepath = styles_filepath;
        logger.info("Exporter initialisé pour " + filepath);
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
     * Lecture du fichier de styles.
     * Si aucun fichier n'a été spécifié, on utilise le fichier par défaut.
     * @return contenu du fichier de styles
     * @see <a href="https://developers.google.com/kml/documentation/kmlreference#style">Styles dans la doc KML</a>
     */
    private String readStyles() {
        logger.debug("Lecture des styles de SITAC...");
        // read contents of file
        String res = "";
        try {
            String filename;
            if (this.styles_filepath == null || force_default) {
                // access file so the jar can find it
                InputStream is = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("default_styles.xml"));
                res = new String(is.readAllBytes());
                logger.info("Utilisation des styles par défaut.");
            } else {
                try {
                    // TODO: check consistency of the file
                    filename = this.styles_filepath;
                    logger.info("Utilisation de styles personnalisés.");
                    File file = new File(filename);
                    res = new String(Files.readAllBytes(file.toPath()));
                } catch (FileNotFoundException e) {
                    logger.error(e);
                    force_default = true;
                    logger.debug("Échec de lecture des styles personnalisés. Utilisation des styles par défaut.");
                    return readStyles();
                }
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("Failed to read styles file.");
        }
        return res;
    }

    /**
     * Exportation des figures vers un fichier KML. Chaque figure est exportée grâce à sa méthode propre.
     * @throws RuntimeException Si aucune figure n'est à exporter.
     * @see Figure#export_kml()
     */
    public void export() throws RuntimeException {
        logger.debug("Exportation en KML...");
        StringBuilder figCode = new StringBuilder();
        for (Figure f : this.figures) {
            logger.debug("Exportation de la figure " + f.name +".");
            figCode.append(f.export_kml());
        }
        if (figCode.isEmpty()) {
            logger.warn("Aucune figure à exporter.");
            throw new RuntimeException("No figures to export.");
        }
        String kml = header + readStyles() + figCode + footer;
        try {
            FileOutputStream fos = new FileOutputStream(this.filepath);
            fos.write(kml.getBytes());
            fos.close();
            logger.info("Succès de l'exportation vers " + this.filepath);
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("Failed to write KML file.");
        }
    }
}

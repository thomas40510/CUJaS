package com.apogee.dev.CUJaS.Core;

import com.apogee.dev.CUJaS.SITACObjects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Parser XML pour les fichiers de SITAC.
 * @author PRV
 * @version 1.0
 */
public class XMLParser {
    private final Document doc;
    private final ArrayList<Node> extracted_figures;
    private final HashMap<XKey, String> keywords;
    private final ArrayList<Figure> figures;

    private static final Logger logger = LogManager.getLogger(XMLParser.class);

    /**
     * Instanciation du parser XML.
     * @param filepath Chemin vers le fichier XML à parser
     * @param semantics Sémantique du fichier XML ({@link NTKSemantics} / {@link MelissaSemantics})
     */
    public XMLParser(String filepath, Semantics semantics) {
        this.extracted_figures = new ArrayList<>();
        this.keywords = semantics.keywords;
        this.figures = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.doc = builder.parse(new File(filepath));
            logger.info("Successfully found your file.");
        } catch (NullPointerException e) {
            throw new RuntimeException("File not found: " + filepath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse le fichier XML et construit les figures.
     * @deprecated Utiliser {@link #parse_figures()} et {@link #build_figures()} à la place
     */
    @Deprecated
    public void parse_sitac() {
        parse_figures();
        build_figures();
    }

    /**
     * Parse le fichier XML et extrait les noeuds XML correspondant aux figures.
     * @see org.w3c.dom.Node
     * @see org.w3c.dom.NodeList
     */
    public void parse_figures() throws RuntimeException {
        this.extracted_figures.clear();
        doc.getDocumentElement().normalize();
        NodeList figs = doc.getElementsByTagName(this.keywords.get(XKey.FIGURE));
        for (int i = 0; i < figs.getLength(); i++) {
            logger.debug("Extracting figure " + i);
            this.extracted_figures.add(figs.item(i));
        }
        if (this.extracted_figures.isEmpty()) {
            throw new RuntimeException("No figure found in the file.");
        }
        logger.info("Done parsing. I found " + this.extracted_figures.size() + " figures.");
    }

    /**
    * Construit les figures à partir des nœuds XML extraits, en fonction de la sémantique.
     * <br>
     * Seules les figures connues sont construites. Les autres sont ignorées.
     * En cas d'implémentation d'une nouvelle figure, il faut ajouter un cas dans le {@code switch}.
     * @see Figure
    */
    public void build_figures() {
        for (Node node : this.extracted_figures) {
            Element elem = (Element) node;
            String figureType = getVal(elem, XKey.FIG_TYPE);
            String figureName = getVal(elem, XKey.FIG_NAME);
            logger.debug("Building figure " + figureName + " of type " + figureType);
            switch (figureType) {
                case "Point":
                    this.figures.add(parse_point(elem, figureName));
                    break;
                case "Line":
                    this.figures.add(parse_line(elem, figureName));
                    break;
                case "Polygon":
                    this.figures.add(parse_polygon(elem, figureName));
                    break;
                case "Circle":
                    this.figures.add(parse_circle(elem, figureName));
                    break;
                case "Rectangle":
                    this.figures.add(parse_rectangle(elem, figureName));
                    break;
                case "Ellipse":
                    this.figures.add(parse_ellipse(elem, figureName));
                    break;
                case "Bullseye":
                    this.figures.add(parse_bullseye(elem, figureName));
                    break;
                case "Corridor":
                    this.figures.add(parse_corridor(elem, figureName));
                    break;
                default:
                    logger.warn("Unknown figure type: " + figureType + ". I'm ignoring it.");
            }
        }
        logger.info("Done building " + this.figures.size() + " figures.");
    }

    /**
     * Crée un {@link Point} à partir d'un nœud XML.
     * @param elem Nœud XML à parser
     * @param figureName Nom de la figure
     * @return Point créé
     */
    private Point parse_point(Element elem, String figureName) {
        String latitude = getVal(elem, XKey.PT_LAT);
        String longitude = getVal(elem, XKey.PT_LON);
        return new Point(Double.parseDouble(latitude), Double.parseDouble(longitude), figureName);
    }

    /**
     * Crée une {@link Line} à partir d'un nœud XML, en créant récursivement les {@link Point} correspondants.
     * @param elem Nœud XML à parser
     * @param figureName Nom de la figure
     * @return Line créée
     */
    private Line parse_line(Element elem, String figureName) {
        NodeList points = elem.getElementsByTagName(this.keywords.get(XKey.FIG_POINT));
        ArrayList<Point> pts = new ArrayList<>();
        for (int i = 0; i < points.getLength(); i++) {
            Element pt = (Element) points.item(i);
            pts.add(parse_point(pt, "Pt"+i));
        }
        return new Line(pts, figureName);
    }

    /**
     * Crée un {@link Polygon} à partir d'un nœud XML.
     * <br>
     * Un polygone est une ligne fermée, donc on utilise {@link #parse_line(Element, String)}.
     * @param element Nœud XML à parser
     * @param figureName Nom de la figure
     * @return Polygon créé
     * @see Polygon#fromLine(Line)
     */
    private Polygon parse_polygon(Element element, String figureName) {
        return new Polygon().fromLine(parse_line(element, figureName));
    }

    /**
     * Crée un {@link Ellipse} à partir d'un nœud XML.
     * @param elem Nœud XML à parser
     * @param figureName Nom de la figure
     * @return Ellipse créée
     */
    private Ellipse parse_ellipse(Element elem, String figureName) {
        Point center = parse_point(elem, "Center");
        double[] horizVert = getHorizVert(elem);
        double horizontal = horizVert[0];
        double vertical = horizVert[1];
        double angle = Double.parseDouble(getVal(elem, XKey.ANGLE));
        return new Ellipse(center, horizontal, vertical, angle, figureName);
    }

    /**
     * Crée un {@link Circle} à partir d'un nœud XML.
     * @param elem Nœud XML à parser
     * @param figureName Nom de la figure
     * @return Circle créé
     */
    private Circle parse_circle(Element elem, String figureName) {
        Point center = parse_point(elem, "Center");
        String radius = getVal(elem, XKey.FIG_HORIZ);
        return new Circle(center, Double.parseDouble(radius), figureName);
    }

    /**
     * Crée un {@link Rectangle} à partir d'un nœud XML.
     * @param element Nœud XML à parser
     * @param figureName Nom de la figure
     * @return Rectangle créé
     */
    private Rectangle parse_rectangle(Element element, String figureName) {
        Point pos = parse_point(element, "StartPt");
        double[] horizVert = getHorizVert(element);
        return new Rectangle(pos, horizVert[0], horizVert[1], figureName);
    }

    /**
     * Crée un {@link Bullseye} à partir d'un nœud XML.
     * <br>
     * Seul l'objet est créé, on ne se pose ici pas la question pratique du dessin.
     * @param element Nœud XML à parser
     * @param figureName Nom de la figure
     * @return Bullseye créé
     */
    private Bullseye parse_bullseye(Element element, String figureName) {
        Point pos = parse_point(element, "Center");
        double[] horizVert = getHorizVert(element);
        int nbRings = Integer.parseInt(getVal(element, XKey.BULLS_RINGS));
        double dist = Double.parseDouble(getVal(element, XKey.BULLS_DIST));
        return new Bullseye(pos, horizVert[0], horizVert[1], nbRings, dist, figureName);
    }

    /**
     * Crée un {@link Corridor} à partir d'un nœud XML.
     * @param element Nœud XML à parser
     * @param figureName Nom de la figure
     * @return Corridor créé
     */
    private Corridor parse_corridor(Element element, String figureName) {
        NodeList points = element.getElementsByTagName(this.keywords.get(XKey.FIG_POINT));
        Point start = parse_point((Element) points.item(0), "CorrStart");
        Point end = parse_point((Element) points.item(1), "CorrEnd");
        double width = Double.parseDouble(getVal(element, XKey.FIG_HORIZ));
        return new Corridor(start, end, width, figureName);
    }

    /**
     * Extrait les valeurs {@code horizontal} et {@code vertical} d'un nœud XML.
     * @param element Nœud XML
     * @return Tableau contenant les valeurs {@code horizontal} et {@code vertical}
     */
    private double[] getHorizVert(Element element) {
        String horizontal = getVal(element, XKey.FIG_HORIZ);
        String vertical = getVal(element, XKey.FIG_VERT);
        return new double[]{Double.parseDouble(horizontal), Double.parseDouble(vertical)};
    }

    /**
     * Extrait une valeur donnée d'un nœud XML.
     * @param elem Nœud XML
     * @param key Clé de la valeur à extraire
     * @return Valeur extraite
     * @throws RuntimeException Si la clé n'est pas connue
     * @see XKey
     */
    private String getVal(Element elem, XKey key) throws RuntimeException {
        try {
            return elem.getElementsByTagName(this.keywords.get(key)).item(0).getTextContent();
        } catch (NullPointerException e) {
            String figName = elem.getElementsByTagName(this.keywords.get(XKey.FIG_NAME)).item(0).getTextContent();
            throw new RuntimeException("Unknown key: " + key + " for " + figName);
        }
    }

    /**
     * Récupère les figures construites.
     * @return Liste des figures construites
     */
    public ArrayList<Figure> getFigures() {
        return this.figures;
    }

}

package com.apogee.dev.CUJaS.CUJaS_Core;

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

public class XMLParser {
    private final Document doc;
    private final ArrayList<Node> extracted_figures;
    private final HashMap<XKey, String> keywords;
    private final ArrayList<Figure> figures;

    private static final Logger logger = LogManager.getLogger(XMLParser.class);

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

    public void parse_sitac() {
        parse_figures();
        build_figures();
    }

    public void parse_figures() {
        this.extracted_figures.clear();
        doc.getDocumentElement().normalize();
        NodeList figs = doc.getElementsByTagName(this.keywords.get(XKey.FIGURE));
        for (int i = 0; i < figs.getLength(); i++) {
            logger.debug("Extracting figure " + i);
            this.extracted_figures.add(figs.item(i));
        }
        logger.info("Done parsing. I found " + this.extracted_figures.size() + " figures.");
    }

    /*
    From extracted xml nodes, build figures by extracting desired properties.
    Each constructed figure is added to the figures array.
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

    private Point parse_point(Element elem, String figureName) {
        String latitude = getVal(elem, XKey.PT_LAT);
        String longitude = getVal(elem, XKey.PT_LON);
        return new Point(Double.parseDouble(latitude), Double.parseDouble(longitude), figureName);
    }

    private Line parse_line(Element elem, String figureName) {
        NodeList points = elem.getElementsByTagName(this.keywords.get(XKey.FIG_POINT));
        ArrayList<Point> pts = new ArrayList<>();
        for (int i = 0; i < points.getLength(); i++) {
            Element pt = (Element) points.item(i);
            pts.add(parse_point(pt, "Pt"+i));
        }
        return new Line(pts, figureName);
    }

    private Polygon parse_polygon(Element element, String figureName) {
        return new Polygon().fromLine(parse_line(element, figureName));
    }

    private Ellipse parse_ellipse(Element elem, String figureName) {
        Point center = parse_point(elem, "Center");
        double[] horizVert = getHorizVert(elem);
        double horizontal = horizVert[0];
        double vertical = horizVert[1];
        double angle = Double.parseDouble(getVal(elem, XKey.ANGLE));
        return new Ellipse(center, horizontal, vertical, angle, figureName);
    }

    private Circle parse_circle(Element elem, String figureName) {
        Point center = parse_point(elem, "Center");
        String radius = getVal(elem, XKey.FIG_HORIZ);
        return new Circle(center, Double.parseDouble(radius), figureName);
    }

    private Rectangle parse_rectangle(Element element, String figureName) {
        Point pos = parse_point(element, "StartPt");
        double[] horizVert = getHorizVert(element);
        return new Rectangle(pos, horizVert[0], horizVert[1], figureName);
    }

    private Bullseye parse_bullseye(Element element, String figureName) {
        Point pos = parse_point(element, "Center");
        double[] horizVert = getHorizVert(element);
        int nbRings = Integer.parseInt(getVal(element, XKey.BULLS_RINGS));
        double dist = Double.parseDouble(getVal(element, XKey.BULLS_DIST));
        return new Bullseye(pos, horizVert[0], horizVert[1], nbRings, dist, figureName);
    }

    private Corridor parse_corridor(Element element, String figureName) {
        NodeList points = element.getElementsByTagName(this.keywords.get(XKey.FIG_POINT));
        Point start = parse_point((Element) points.item(0), "CorrStart");
        Point end = parse_point((Element) points.item(1), "CorrEnd");
        double width = Double.parseDouble(getVal(element, XKey.FIG_HORIZ));
        return new Corridor(start, end, width, figureName);
    }

    /**
     * Parse horizontal and vertical values from a figure
     * @param element Element to parse values from
     * @return Array of horizontal and vertical values
     */
    private double[] getHorizVert(Element element) {
        String horizontal = getVal(element, XKey.FIG_HORIZ);
        String vertical = getVal(element, XKey.FIG_VERT);
        return new double[]{Double.parseDouble(horizontal), Double.parseDouble(vertical)};
    }

    private String getVal(Element elem, XKey key) throws RuntimeException {
        try {
            return elem.getElementsByTagName(this.keywords.get(key)).item(0).getTextContent();
        } catch (NullPointerException e) {
            String figName = elem.getElementsByTagName(this.keywords.get(XKey.FIG_NAME)).item(0).getTextContent();
            throw new RuntimeException("Unknown key: " + key + " for " + figName);
        }
    }

    public ArrayList<Figure> getFigures() {
        return this.figures;
    }

}

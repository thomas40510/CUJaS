package com.apogee.dev.CUJaS.Core.Melissa;

import com.apogee.dev.CUJaS.Core.XKey;
import com.apogee.dev.CUJaS.Core.XMLParser;
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

public class MelissaParser implements XMLParser {
    private final Document doc;
    private final ArrayList<Node> extracted_figures;
    private final HashMap<XKey, String> keywords;
    private final ArrayList<Figure> figures;

    private static final Logger logger = LogManager.getLogger(MelissaParser.class);


    public MelissaParser(String filepath) {
        this.extracted_figures = new ArrayList<>();
        this.keywords = new MelissaSemantics().keywords;
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

    @Override
    public void parse_figures() throws RuntimeException {
        this.extracted_figures.clear();
        this.figures.clear();
        Node root = this.doc.getFirstChild();
        if (root.getNodeName().equals(this.keywords.get(MelissaKey.BODY))) {
            logger.info("Root node is " + root.getNodeName());
            for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (this.keywords.containsValue(child.getNodeName())) {
                    logger.info("Found a figure: " + child.getNodeName());
                    this.extracted_figures.add(child);
                }
            }
        } else {
            throw new RuntimeException("Root node is not " + this.keywords.get(MelissaKey.BODY));
        }

    }

    @Override
    public void build_figures() {
        for (Node figure : this.extracted_figures) {
            // find key matching figure.getNodeName()
            MelissaKey key = (MelissaKey) this.keywords.entrySet().stream()
                    .filter(entry -> figure.getNodeName().equals(entry.getValue()))
                    .map(HashMap.Entry::getKey)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No key found for " + figure.getNodeName()));
            Element element = (Element) figure;
            switch (key) {
                case POINT:
                    this.figures.add(parse_point(element));
                    break;
                case LINE:
                    this.figures.add(parse_line(element));
                    break;
                case ELLIPSE:
                    this.figures.add(parse_ellipse(element));
                    break;
                case POLYGON:
                    this.figures.add(parse_polygon(element));
                    break;
                case CORRIDOR:
                    logger.info("Found a corridor");
                    break;
                case BULLS:
                    this.figures.add(parse_bulls(element));
                    break;
                default:
                    logger.error("Unknown figure type: " + figure.getNodeName());
                    //throw new RuntimeException("Unknown figure type: " + figure.getNodeName());
            }
        }

    }

    protected Point parse_point(Element figure) {
        logger.info("Building a point");
        // <coordonnees latitude="xx.xxxx" longitude="yy.yyyy"/>
        // get info
        double lat, lon;
        try {
            Element coords = (Element) figure.getElementsByTagName(this.keywords.get(MelissaKey.COORDS)).item(0);
            lat = Double.parseDouble(coords.getAttribute("latitude"));
            lon = Double.parseDouble(coords.getAttribute("longitude"));
        } catch (NullPointerException e) {
            lat = Double.parseDouble(figure.getAttribute("latitude"));
            lon = Double.parseDouble(figure.getAttribute("longitude"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // build point
        Point point = new Point(lat, lon);
        logger.info("Point built: " + point);
        return point;
    }

    protected Line parse_line(Element figure) {
        ArrayList<Point> points = new ArrayList<>();

        Element center = (Element) figure.getElementsByTagName(this.keywords.get(MelissaKey.COORDS)).item(0);
        double lat = Double.parseDouble(center.getAttribute("latitude"));
        double lon = Double.parseDouble(center.getAttribute("longitude"));
        points.add(new Point(lat, lon));

        // <liste>
        // <coordonnees latitude="xx.xxxx" longitude="yy.yyyy"/>
        // <coordonnees latitude="xx.xxxx" longitude="yy.yyyy"/>
        // </liste>

        NodeList list = figure.getElementsByTagName(this.keywords.get(MelissaKey.PTS_LINE)).item(0).getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeName().equals(this.keywords.get(MelissaKey.COORDS))) {
                points.add(parse_point((Element) node));
            }
        }

        Line line = new Line(points);
        logger.info("Line built: " + line);
        return line;
    }

    protected Polygon parse_polygon(Element figure) {
        Polygon polygon = parse_line(figure).asPolygon();
        logger.info("Polygon built: " + polygon);
        return polygon;
    }

    protected Ellipse parse_ellipse(Element figure) {
        Element center = (Element) figure.getElementsByTagName(this.keywords.get(MelissaKey.COORDS)).item(0);
        logger.warn("Center: " + center.getAttribute("latitude") + ", " + center.getAttribute("longitude"));
        double lat = Double.parseDouble(center.getAttribute("latitude"));
        double lon = Double.parseDouble(center.getAttribute("longitude"));
        Point point = new Point(lat, lon);

        Element radii = (Element) figure.getElementsByTagName(this.keywords.get(MelissaKey.ELL_RADII)).item(0);
        double hrad = Double.parseDouble(radii.getAttribute("rayonHorizontal"));
        double vrad = Double.parseDouble(radii.getAttribute("rayonVertical"));

        Element angle = (Element) figure.getElementsByTagName(this.keywords.get(MelissaKey.ANGLE)).item(0);
        double alpha = Double.parseDouble(angle.getAttribute("code"));

        Ellipse ellipse = new Ellipse(point, hrad, vrad, alpha);
        logger.info("Ellipse built: " + ellipse);
        return ellipse;
    }

    protected Bullseye parse_bulls(Element element) {
        Element center = (Element) element.getElementsByTagName(this.keywords.get(MelissaKey.COORDS)).item(0);
        double lat = Double.parseDouble(center.getAttribute("latitude"));
        double lon = Double.parseDouble(center.getAttribute("longitude"));
        Point ptCenter = new Point(lat, lon);

        Element rings = (Element) element.getElementsByTagName(this.keywords.get(MelissaKey.BULLS_RINGS)).item(0);
        int nb_rings = Integer.parseInt(rings.getAttribute("code"));

        Element dist = (Element) element.getElementsByTagName(this.keywords.get(MelissaKey.BULLS_DIST)).item(0);
        double step = Double.parseDouble(dist.getAttribute("code"));

        double rad = nb_rings * step;

        Bullseye bullseye = new Bullseye(ptCenter, rad, rad, nb_rings, step);
        logger.info("Bullseye built: " + bullseye);
        return bullseye;
    }

    @Override
    public ArrayList<Figure> getFigures() {
        return this.figures;
    }
}

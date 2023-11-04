package com.apogee.dev.CUJaS.CUJaS_Core;

import com.apogee.dev.CUJaS.SITACObjects.*;
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
    private Document doc;
    private ArrayList<Node> extracted_figures;
    private HashMap<XKey, String> keywords;
    private ArrayList<Figure> figures;

    public XMLParser(String filepath, Semantics semantics) {
        this.extracted_figures = new ArrayList<>();
        this.keywords = semantics.keywords;
        this.figures = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.doc = builder.parse(new File(filepath));
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
            this.extracted_figures.add(figs.item(i));
        }
    }

    /*
    From extracted xml nodes, build figures by extracting desired properties.
    Each constructed figure is added to the figures array.
     */
    public void build_figures() {
        String figureTypeKey = this.keywords.get(XKey.FIG_TYPE);
        for (Node node : this.extracted_figures) {
            Element elem = (Element) node;
            String figureType = elem.getElementsByTagName(figureTypeKey).item(0).getTextContent();
            String figureName = elem.getElementsByTagName(this.keywords.get(XKey.FIG_NAME)).item(0).getTextContent();
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
/*
                case "Circle":
                    this.figures.add(parse_circle(elem, figureName));
                    break;
                case "Rectangle":
                    parse_rectangle(elem, figureName);
                    break;
                case "Ellipse":
                    parse_ellipse(elem, figureName);
                    break;
                case "BullsEye":
                    parse_bullseye(elem, figureName);
                    break;
*/
                default:
                    throw new RuntimeException("Unknown figure type: " + figureType);
            }
        }
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
            pts.add(parse_point(pt, figureName));
        }
        return new Line(pts, figureName);
    }

    private Polygon parse_polygon(Element element, String figureName) {
        Line l = parse_line(element, figureName);
        return (Polygon) l;
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

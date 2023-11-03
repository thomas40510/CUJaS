package com.apogee.dev.CUJaS.CUJaS_Core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * @see https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
 */
public class XMLReader {
    private Document doc;
    private ArrayList<Node> figures;
    public XMLReader(String filename)  {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.doc = builder.parse(new File(filename));
            this.figures = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractFigures() {
        // extract figures (tag "figure") and store them in an array as nodes
        NodeList nodes = this.doc.getElementsByTagName("figure");
        for (int i = 0; i < nodes.getLength(); i++) {
            this.figures.add(nodes.item(i));
        }
        System.out.println("Number of figures: " + this.figures.size());
    }

    public void displayFigures() {
        for (Node figure : this.figures) {
            System.out.print("Figure "+ this.figures.indexOf(figure) + ": ");
            this.displayFigure(figure);
        }
    }

    private void displayFigure(Node figure) {
        // get content of tag "figureType" by name
        String figType = ((Element) figure).getElementsByTagName("figureType").item(0).getTextContent();
        System.out.println(figType);
    }

    /**
     * Read xml tree and pretty-print it
     */
    public void printTree() {
        this.doc.getDocumentElement().normalize();
        Node root = this.doc.getDocumentElement();
        System.out.println("Root element: " + root.getNodeName());
        // get "figures" subtree
        NodeList figures = this.doc.getElementsByTagName("figures").item(0).getChildNodes();
        //NodeList figures = nodes.item(1).getChildNodes();
        System.out.println("Number of figures: " + figures.getLength());
        for (int i = 0; i < figures.getLength(); i++) {
            System.out.println("----------");
            System.out.println("Figure " + i);
            System.out.println("----------");
            Node node = figures.item(i);
            this.printNode(node, 0);
            //this.displayType(node);
        }

        //this.printNode(root, 0);

    }

    private void printNode(Node node, int level) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return;
        }
        System.out.println("|  ".repeat(Math.max(0, level)) + node.getNodeName());
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            this.printNode(node.getChildNodes().item(i), level + 1);
        }
    }

    private void displayText(Node node) {
        System.out.println(node.getNodeName() + ": " + node.getTextContent());
    }
}

package com.apogee.dev.CUJaS.Core.Melissa;

import com.apogee.dev.CUJaS.Core.XKey;
import com.apogee.dev.CUJaS.Core.XMLParser;
import com.apogee.dev.CUJaS.SITACObjects.Figure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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

    }

    @Override
    public void build_figures() {

    }

    @Override
    public ArrayList<Figure> getFigures() {
        return null;
    }
}

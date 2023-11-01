package com.apogee.dev.CUJaS;

import com.apogee.dev.CUJaS.CUJaS_Core.XMLReader;

public class Main {
    public static void main(String[] args) {
        String current_path = System.getProperty("user.dir");
        // read file in 'resources' folder
        String filename = current_path + "/src/main/resources/test.xml";
        XMLReader reader = new XMLReader(filename);
        reader.extractFigures();
        reader.displayFigures();
    }
}

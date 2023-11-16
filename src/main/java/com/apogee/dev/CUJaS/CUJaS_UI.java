package com.apogee.dev.CUJaS;

import com.apogee.dev.CUJaS.CUJaS_Core.NTKSemantics;
import com.apogee.dev.CUJaS.CUJaS_Core.XMLParser;
import com.apogee.dev.CUJaS.SITACObjects.Figure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CUJaS_UI {
    private final Logger logger = LogManager.getLogger(CUJaS_UI.class);

    private JPanel rootPanel;
    private JButton bFileSelect;

    public CUJaS_UI() {
        logger.info("UI initialized.");

        bFileSelect.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // open file picker
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select SITAC XML file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                // open on current directory
                fileChooser.setCurrentDirectory(new java.io.File("."));
                // set extension
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(java.io.File f) {
                        return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
                    }

                    public String getDescription() {
                        return "SITAC XML files (*.xml)";
                    }
                });
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.showOpenDialog(rootPanel);

                // get selected file
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                logger.info("Selected file: " + filename);

                XMLParser parser = new XMLParser(filename, new NTKSemantics());
                parser.parse_sitac();
                ArrayList<Figure> figures = parser.getFigures();
                for (Figure f : figures) {
                    logger.debug(f);
                }

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CUJaS_UI");
        frame.setContentPane(new CUJaS_UI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);
        // uncomment to wrap content
        // frame.pack();
        frame.setLocationRelativeTo(null); // center window
        frame.setVisible(true);
    }
}

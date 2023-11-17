package com.apogee.dev.CUJaS.UI;

import com.apogee.dev.CUJaS.Core.NTKSemantics;
import com.apogee.dev.CUJaS.Core.XMLParser;
import com.apogee.dev.CUJaS.SITACObjects.Figure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class CUJaS_UI {
    private final Logger logger = LogManager.getLogger(CUJaS_UI.class);

    private JPanel rootPanel;
    private JButton inputSelectBtn;
    private JButton nextBtn;
    private JRadioButton NTKRadioButton;
    private JRadioButton melissaRadioButton;
    private JPanel inputPane;
    private JPanel langPane;
    private JPanel outPane;
    private JButton outSelectBtn;
    private JTabbedPane tabbedPane1;
    private JScrollPane processingPane;
    private ButtonGroup langGroup;

    public CUJaS_UI() {
        logger.info("UI initialized.");

        ColoredTextPane textPane = new ColoredTextPane();

        processingPane.setViewportView(textPane);

        // redirect stdout to textPane
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                textPane.append(String.valueOf((char) b));
            }
        });
        System.setOut(printStream);
        System.setErr(printStream);


        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.info("Next button pressed.");
            }
        });

        inputSelectBtn.addActionListener(new ActionListener() {
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

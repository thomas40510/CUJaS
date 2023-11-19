package com.apogee.dev.CUJaS.UI;

import com.apogee.dev.CUJaS.Core.MelissaSemantics;
import com.apogee.dev.CUJaS.Core.NTKSemantics;
import com.apogee.dev.CUJaS.Core.Semantics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class CUJaS_UI {
    private final Logger logger = LogManager.getLogger(CUJaS_UI.class);

    private String inputFileName;

    private Semantics semantics;

    private JPanel rootPanel;
    private JButton nextBtn;
    private JRadioButton NTKRadioButton;
    private JRadioButton melissaRadioButton;
    private JPanel inputPane;
    private JPanel langPane;
    private JPanel outPane;
    private JButton outSelectBtn;
    private JTabbedPane tabbedPane1;
    private JScrollPane procPane;
    private JButton inputBtn;
    private JLabel inputLocTxt;
    private ButtonGroup langGroup;

    public CUJaS_UI() {
        redirectConsole();

        logger.info("UI initialized.");

        nextBtn.addActionListener(e -> {
            logger.info("Next button pressed.");
            logger.debug("Next button pressed.");
            logger.error("Next button pressed.");
            logger.warn("Next button pressed.");
        });

        inputBtn.addActionListener(e -> {
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
            inputFileName = fileChooser.getSelectedFile().getAbsolutePath();
            inputLocTxt.setText(inputFileName);
        });

        // listener on radio buttons group
        melissaRadioButton.addActionListener(e -> semantics = new MelissaSemantics());
        NTKRadioButton.addActionListener(e -> semantics = new NTKSemantics());

        outSelectBtn.addActionListener(e -> {
            // open directory picker
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select output directory");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            // open on current directory
            fileChooser.setCurrentDirectory(new java.io.File("."));
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.showOpenDialog(rootPanel);

            // get selected directory
            String outputDir = fileChooser.getSelectedFile().getAbsolutePath();
            logger.info("Output directory: " + outputDir);
        });
    }

    /**
     * Redirect stdout and stderr to a {@link com.apogee.dev.CUJaS.UI.ColoredTextPane ColoredTextPane}
     */
    private void redirectConsole() {
        ColoredTextPane textPane = new ColoredTextPane();

        procPane.setViewportView(textPane);

        // redirect stdout to textPane
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                textPane.append(String.valueOf((char) b));
            }
        });
        System.setOut(printStream);
        System.setErr(printStream);
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

package com.apogee.dev.CUJaS.UI;

import com.apogee.dev.CUJaS.Core.MelissaSemantics;
import com.apogee.dev.CUJaS.Core.NTKSemantics;
import com.apogee.dev.CUJaS.Core.Semantics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class CUJaS_UI {
    private final Logger logger = LogManager.getLogger(CUJaS_UI.class);

    private String inputFileName;

    private enum Lang {
        MELISSA,
        NTK
    }

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
    private JLabel txtRead;
    private JLabel txtExtract;
    private JLabel txtGenerate;
    private JLabel txtExport;
    private JProgressBar procProgress;
    private JLabel readStatus;
    private JLabel extractStatus;
    private JLabel genStatus;
    private JLabel exportStatus;
    private ButtonGroup langGroup;

    public CUJaS_UI() {
        redirectConsole();

        logger.info("UI initialized.");

        nextBtn.addActionListener(e -> logger.debug("Next button pressed."));

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
            // show next tab
            tabbedPane1.setSelectedIndex(1);
            // change tab title
            tabbedPane1.setTitleAt(0, tabbedPane1.getTitleAt(0) + " ✅");
        });

        // listener on radio buttons group
        melissaRadioButton.addActionListener(e -> selectLang(Lang.MELISSA));
        NTKRadioButton.addActionListener(e -> selectLang(Lang.NTK));

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
            // show next tab
            tabbedPane1.setSelectedIndex(3);
            // change tab title
            tabbedPane1.setTitleAt(2, tabbedPane1.getTitleAt(2) + " ✅");

            nextBtn.setEnabled(true);
        });

        preProcess();
    }

    private void preProcess() {
        nextBtn.setEnabled(false);
        ArrayList<JLabel> statusLabels = new ArrayList<>();
        statusLabels.add(readStatus);
        statusLabels.add(extractStatus);
        statusLabels.add(genStatus);
        statusLabels.add(exportStatus);

        for (JLabel label : statusLabels) {
            label.setText("");
        }

        txtExtract.setVisible(false);
        txtGenerate.setVisible(false);
        txtExport.setVisible(false);

        procProgress.setValue(0);
    }

    private void selectLang(Lang lang) {
        switch (lang) {
            case MELISSA:
                semantics = new MelissaSemantics();
                break;
            case NTK:
                semantics = new NTKSemantics();
                break;
        }
        tabbedPane1.setTitleAt(1, tabbedPane1.getTitleAt(1) + " ✅");
        tabbedPane1.setSelectedIndex(2);
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

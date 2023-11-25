package com.apogee.dev.CUJaS.UI;

import com.apogee.dev.CUJaS.Core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * UI de l'outil CUJaS
 * @author PRV
 * @version 1.0
 */
public class CUJaS_UI {
    private final Logger logger = LogManager.getLogger(CUJaS_UI.class);

    private String inputFileName = null;
    private String outputDir = null;
    private String kml_styles = null;

    /**
     * Langage de la SITAC
     */
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
    private JButton customStyleBtn;
    private JButton stylesQBtn;
    private ButtonGroup langGroup;

    /**
     * Constructeur de l'UI
     */
    public CUJaS_UI() {
        redirectConsole();

        logger.info("UI initialized.");

        preProcess();

        inputBtn.addActionListener(e -> selectInput());

        // listener on radio buttons group
        melissaRadioButton.addActionListener(e -> selectLang(Lang.MELISSA));
        NTKRadioButton.addActionListener(e -> selectLang(Lang.NTK));

        outSelectBtn.addActionListener(e -> selectOutput());

        customStyleBtn.addActionListener(e -> selectCustomStyleFile());

        stylesQBtn.addActionListener(e -> {
            // show a message dialog
            JOptionPane.showMessageDialog(rootPanel,
                    """
                            Il est possible de définir le style des objets plutôt que d'utiliser ceux par défaut.\s
                            Voir la documentation et le fichier d'exemple kml_styles.xml pour + d'infos.""",
                    "Styles KML",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        nextBtn.addActionListener(e -> exportFile());

    }

    /**
     * Sélection du fichier d'entrée.
     */
    private void selectInput() {
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
        if (fileChooser.getSelectedFile() == null) return;

        // get selected file
        inputFileName = fileChooser.getSelectedFile().getAbsolutePath();
        inputLocTxt.setText(inputFileName);
        // show next tab
        tabbedPane1.setSelectedIndex(1);
        // change tab title
        titleDone(0);
    }

    /**
     * Sélection du dossier de sortie.
     */
    private void selectOutput() {
        // open directory picker
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select output directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // open on current directory
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.showOpenDialog(rootPanel);
        // if dialog closes without selecting a directory, do nothing
        if (fileChooser.getSelectedFile() == null) return;

        // get selected directory
        outputDir = fileChooser.getSelectedFile().getAbsolutePath();
        logger.info("Output directory: " + outputDir);
        // show next tab
        //tabbedPane1.setSelectedIndex(3);
        // change tab title
        titleDone(2);

        nextBtn.setEnabled(true);
    }

    /**
     * L'étape de l'onglet {@code i} est un succès, on met à jour le titre de l'onglet.
     * @param i index de l'onglet
     */
    private void titleDone(int i) {
        String paneTitle = tabbedPane1.getTitleAt(i);
        if (!paneTitle.contains(" ✅")) tabbedPane1.setTitleAt(i, paneTitle + " ✅");
    }

    /**
     * Fenêtre de sélection d'un fichier de styles.
     */
    private void selectCustomStyleFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select styles file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // open on current directory
        fileChooser.setCurrentDirectory(new java.io.File("."));
        // set extension
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File f) {
                return f.getName().toLowerCase().endsWith(".xml")
                        || f.getName().toLowerCase().endsWith(".kml")
                        || f.isDirectory();
            }

            public String getDescription() {
                return "KML Style file (*.xml, *.kml)";
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.showOpenDialog(rootPanel);

        if (fileChooser.getSelectedFile() == null) return;

        kml_styles = fileChooser.getSelectedFile().getAbsolutePath();
        logger.info("Custom kml styles set: " + kml_styles);

        if (outputDir != null) tabbedPane1.setSelectedIndex(3);

    }

    /**
     * Préparation de l'UI avant l'exportation.
     */
    private void preProcess() {
        final ArrayList<JLabel> statusLabels = new ArrayList<>();
        inputLocTxt.setText("Aucun fichier sélectionné");
        txtRead.setText("Lecture de la SITAC");
        nextBtn.setEnabled(false);
        statusLabels.add(readStatus);
        statusLabels.add(extractStatus);
        statusLabels.add(genStatus);
        statusLabels.add(exportStatus);

        for (JLabel label : statusLabels) {
            label.setText("");
        }

        procProgress.setValue(0);
    }

    /**
     * Avancement de l'exportation (pour la {@code ProgressBar}).
     */
    private static int PROGRESS = 1;

    /**
     * <i>Cooldown</i> entre chaque étape de l'exportation, en ms.
     */
    private static final long WAIT_TIME = 500;

    /**
     * Exportation de la SITAC.
     * @see com.apogee.dev.CUJaS.Core.XMLParser
     * @see com.apogee.dev.CUJaS.Core.KMLExporter
     */
    private void exportFile() {
        SwingWorker<Void, JLabel> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // read xml
                XMLParser parser = new XMLParser(inputFileName, semantics);
                this.publish(readStatus);

                Thread.sleep(WAIT_TIME);

                // extract figures
                parser.parse_figures();
                this.publish(extractStatus);

                Thread.sleep(WAIT_TIME);

                // build figures
                parser.build_figures();
                this.publish(genStatus);

                Thread.sleep(WAIT_TIME);

                // generate kml
                String outputFile = outputDir + "/output.kml";
                KMLExporter exporter = new KMLExporter(parser.getFigures(), outputFile, kml_styles);
                exporter.export();
                this.publish(exportStatus);

                showSuccessAlert();

                return null;
            }

            @Override
            protected void process(List<JLabel> res) {
                for (JLabel label : res) {
                    complete(label);
                }
            }
        };

        try {
            worker.execute();
            logger.info("/// Done exporting! ///");
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Succès de l'exportation, on affiche une boîte de dialogue.
     */
    private void showSuccessAlert() {
        // show a success alert dialog with 2 buttons
        int res = JOptionPane.showConfirmDialog(rootPanel,
                "Exportation terminée avec succès !\nVoulez-vous ouvrir la destination ?",
                "Exportation terminée",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            // open file
            try {
                // get OS name
                String os = System.getProperty("os.name").toLowerCase();
                // open output directory
                if (os.contains("win")) {
                    Runtime.getRuntime().exec("explorer.exe /select," + outputDir + "/output.kml");
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec("open " + outputDir);
                } else {
                    // linux
                    Runtime.getRuntime().exec("xdg-open " + outputDir);
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
    }

    /**
     * Met à jour un label de status pour indiquer que l'étape est terminée, et incrémente la {@code ProgressBar}.
     * @param label label à mettre à jour
     */
    private void complete (JLabel label) {
        label.setText("✅");
        procProgress.setValue(PROGRESS++);
    }

    /**
     * Sélection du langage de la SITAC.
     * @param lang langage choisi par l'utilisateur
     * @see Lang
     * @see Semantics
     */
    private void selectLang(Lang lang) {
        switch (lang) {
            case MELISSA:
                semantics = new MelissaSemantics();
                break;
            case NTK:
                semantics = new NTKSemantics();
                break;
        }
        titleDone(1);
        tabbedPane1.setSelectedIndex(2);
    }

    /**
     * Redirection des sorties {@code stdout} et {@code stderr}
     * vers un {@link com.apogee.dev.CUJaS.UI.ColoredTextPane}
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

    /**
     * Point d'entrée de l'application.
     * @param args arguments de la ligne de commande
     */
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

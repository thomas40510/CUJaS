package com.apogee.dev.CUJaS.UI;

import com.apogee.dev.CUJaS.Core.KMLExporter;
import com.apogee.dev.CUJaS.Core.Melissa.MelissaParser;
import com.apogee.dev.CUJaS.Core.NTK.NTKParser;
import com.apogee.dev.CUJaS.Core.Semantics;
import com.apogee.dev.CUJaS.Core.XMLParser;
import mdlaf.MaterialLookAndFeel;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * UI de l'outil CUJaS
 * @author PRV
 * @version 1.2
 * @since 1.0
 */
public class CUJaS_UI {
    private static final Logger logger = LogManager.getLogger(CUJaS_UI.class);

    private String inputFileName = null;
    private String outputDir = null;
    private String kml_styles = null;
    private String outputFile;

    /**
     * Langage de la SITAC
     */
    private enum Lang {
        MELISSA,
        NTK
    }

    private XMLParser parser;

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
    private JLabel logo1;
    private JLabel logo2;
    private JPanel titlePanel;
    private JLabel titleText;
    private JButton aboutBtn;
    private ButtonGroup langGroup;

    static {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new CUJASTheme()));
        } catch (UnsupportedLookAndFeelException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Constructeur de l'UI
     */
    public CUJaS_UI() {
        redirectConsole();

        // set text size for title
        titleText.setFont(new Font("Arial", Font.BOLD, 20));

        setLogos();

        //set margins
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        logger.info("UI initialized.");

        preProcess();

        addBtns();
    }

    /**
     * Charge les insignes et les affiche dans l'UI.
     * <br>
     * On charge l'insigne du CIET et celui de l'EIE.
     */
    protected void setLogos() {
        logo1.setText("");
        logo2.setText("");
        try {
            BufferedImage img1 = ImageIO.read(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("eie.png"))
            );
            BufferedImage img2 = ImageIO.read(
                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("spabi2.png"))
            );
            int size = 60;
            logo1.setIcon(new ImageIcon(Objects.requireNonNull(img1).getScaledInstance(size, size, Image.SCALE_SMOOTH)));
            logo2.setIcon(new ImageIcon(Objects.requireNonNull(img2).getScaledInstance(size, size, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Ajout des listeners sur les boutons de l'UI.
     */
    public void addBtns() {
        inputBtn.addActionListener(e -> selectInput());

        // listener on radio buttons group
        melissaRadioButton.addActionListener(e -> selectLang(Lang.MELISSA));
        NTKRadioButton.addActionListener(e -> selectLang(Lang.NTK));

        outSelectBtn.addActionListener(e -> selectOutput());

        customStyleBtn.addActionListener(e -> selectCustomStyleFile());

        isAboutBtn(stylesQBtn);
        stylesQBtn.addActionListener(e -> {
            // show a message dialog
            JOptionPane.showMessageDialog(rootPanel,
                    """
                            Il est possible de définir le style des objets plutôt que d'utiliser ceux par défaut.\s
                            Voir la documentation et le fichier d'exemple kml_styles.xml pour + d'infos.""",
                    "Styles KML",
                    JOptionPane.INFORMATION_MESSAGE, MaterialImageFactory.getInstance().getImage(
                            MaterialIconFont.HELP_OUTLINE, new ColorUIResource(0, 0, 0)));
        });

        nextBtn.addActionListener(e -> exportFile());

        isAboutBtn(aboutBtn);
        aboutBtn.addActionListener(e -> JOptionPane.showMessageDialog(rootPanel,
                aboutMsg(),
                "À propos",
                JOptionPane.INFORMATION_MESSAGE));
        // mousehover text
        aboutBtn.setToolTipText("À propos");
    }

    /**
     * Implémentation du contenu de la fenêtre "à propos".
     * @return message de présentation
     */
    public String aboutMsg() {
        String version = getClass().getPackage().getImplementationVersion();
        return """
                CUJaS
                (Convertisseur Unifié en Java pour les SiTaC).
                ---
                Version %s
                Développé par l'IETA Prévost pour l'EIE CN-235
                Distribué sous licence GNU GPL v3
                
                ooOoo
                Les vrais avions ont des hélices.
                """.formatted(version == null ? "DEV" : version);
    }

    /**
     * Mise en forme des boutons d'à propos (icône, forme)
     * @param btn bouton à mettre en forme
     */
    private void isAboutBtn(JButton btn) {
        // set icon
        btn.setIcon(MaterialImageFactory.getInstance().getImage(
                MaterialIconFont.HELP_OUTLINE, new ColorUIResource(0, 0, 0))
        );
        btn.setText("");
        // about btn is rounded
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
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
        preProcess();
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
        tabbedPane1.setIconAt(i, MaterialImageFactory.getInstance().getImage(
                MaterialIconFont.CHECK, new ColorUIResource(6, 148, 50))
        );
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

    protected static final ArrayList<JLabel> statusLabels = new ArrayList<>();

    /**
     * Préparation de l'UI avant l'exportation.
     */
    private void preProcess() {
        // reset UI elements to default
        procProgress.setIndeterminate(false);

        inputLocTxt.setText("Aucun fichier sélectionné");
        nextBtn.setEnabled(false);
        statusLabels.add(readStatus);
        statusLabels.add(extractStatus);
        statusLabels.add(genStatus);
        statusLabels.add(exportStatus);

        checkTabs();

        for (JLabel label : statusLabels) {
            label.setText("");
            label.setIcon(null);
        }

        procProgress.setValue(0);
    }

    private void checkTabs() {
        for (int i = 0; i < tabbedPane1.getTabCount(); i++) {
            tabbedPane1.setIconAt(i, null);
        }
    }

    /**
     * Avancement de l'exportation (pour la {@code ProgressBar}).
     */
    private static int PROGRESS = 1;

    /**
     * <i>Cooldown</i> entre chaque étape de l'exportation, en ms.
     */
    private static final long WAIT_TIME = 50;

    /**
     * Exportation de la SITAC.
     * @see NTKParser
     * @see com.apogee.dev.CUJaS.Core.KMLExporter
     */
    private void exportFile() {
        tabbedPane1.setSelectedIndex(3);
        SwingWorker<Void, JLabel> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // read xml
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
                    String time_now = String.valueOf(System.currentTimeMillis());
                    outputFile = outputDir + "/output_" + time_now + ".kml";
                    KMLExporter exporter = new KMLExporter(parser.getFigures(), outputFile, kml_styles);
                    exporter.export();
                    this.publish(exportStatus);

                    showSuccessAlert();

                    return null;
                } catch (RuntimeException e) {
                    logger.warn(e.getMessage());
                    showErrorStatus();
                    showErrorAlert();
                    return null;
                }
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
                    Runtime.getRuntime().exec("explorer.exe /select," + outputFile);
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec("open " + outputFile);
                } else {
                    // linux
                    Runtime.getRuntime().exec("xdg-open " + outputFile);
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
    }

    /**
     * Erreur lors de l'exportation, on affiche une boîte de dialogue.
     */
    private void showErrorAlert () {
        JOptionPane.showMessageDialog(rootPanel,
                "Une erreur est survenue lors de l'exportation. Le fichier est sûrement incorrect," +
                        "ou le langage mal sélectionné. Voir les logs pour plus d'informations.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Erreur lors de l'exportation, on met à jour les labels de status.
     */
    private void showErrorStatus() {
        for (JLabel label : statusLabels) {
            if (label.getIcon() == null) label.setIcon(MaterialImageFactory.getInstance().getImage(
                    MaterialIconFont.CLOSE, new ColorUIResource(255, 0, 0)));
        }
        procProgress.setIndeterminate(true);
    }

    /**
     * Met à jour un label de statut pour indiquer que l'étape est terminée, et incrémente la {@code ProgressBar}.
     * @param label label à mettre à jour
     */
    private void complete (JLabel label) {
        label.setIcon(MaterialImageFactory.getInstance().getImage(
                MaterialIconFont.CHECK, new ColorUIResource(6, 148, 50)));
        procProgress.setValue(PROGRESS++);
    }

    /**
     * Sélection du langage de la SITAC, et instanciation du {@link XMLParser} correspondant.
     * @param lang langage choisi par l'utilisateur
     * @see Lang
     * @see Semantics
     */
    private void selectLang(Lang lang) {
        switch (lang) {
            case MELISSA:
                parser = new MelissaParser(inputFileName);
                break;
            case NTK:
                parser = new NTKParser(inputFileName);
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
        try {
            BufferedImage icon = ImageIO.read(
                    Objects.requireNonNull(CUJaS_UI.class.getClassLoader().getResourceAsStream("cujas_icon.png"))
            );
            frame.setIconImage(icon);
        } catch (Exception e) {
            logger.warn("Error loading icon : " + e.getMessage());
        }
        // uncomment to wrap content
        // frame.pack();
        frame.setLocationRelativeTo(null); // center window
        frame.setVisible(true);
    }
}

package pl.psnc.synat.a12.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

public class ActionsController implements ActionListener {

    private LettersBrowser browser;
    private Component browserWindow;

    private static final ActionsController instance = new ActionsController();
    private static final String EXIT_CMD = "exit";
    private static final String SAVE_CMD = "save";
    private static final String ITALICS_CMD = "italics";
    private static final String GOTHIC_CMD = "gothic";
    private static final String NOISE_CMD = "noise";
    private static final String OPEN_CMD = "open";
    private File loadDir;
    private JCheckBoxMenuItem italicsBox;
    private JCheckBoxMenuItem gothicBox;
    private JCheckBoxMenuItem noiseBox;


    private ActionsController() {
        // empty
    }


    private void openFile() {
        JFileChooser chooser = new JFileChooser(loadDir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = chooser.showOpenDialog(browserWindow);

        if (option == JFileChooser.APPROVE_OPTION) {
            loadDir = chooser.getSelectedFile();
            try {
                browser.load(loadDir);
                browser.updateFilter(italicsBox.getState(), noiseBox.getState(), gothicBox.getState());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void saveOffsets() {
        try {
            browser.saveOffsets(new File(loadDir, "baselines.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ActionsController getInstance() {
        return instance;
    }


    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (EXIT_CMD.equals(action)) {
            System.exit(0);
        }

        if (browser == null || browserWindow == null) {
            return;
        }

        if (OPEN_CMD.equals(action)) {
            openFile();
        }

        if (SAVE_CMD.equals(action)) {
            saveOffsets();
        }

        if (ITALICS_CMD.equals(action) || NOISE_CMD.equals(action)) {
            browser.updateFilter(italicsBox.getState(), noiseBox.getState(), gothicBox.getState());
        }
    }


    public void registerFileOpen(AbstractButton item) {
        item.setActionCommand(OPEN_CMD);
        item.addActionListener(this);
    }


    public void registerSaveOffsets(JMenuItem menuItem) {
        menuItem.setActionCommand(SAVE_CMD);
        menuItem.addActionListener(this);
    }


    public void registerExit(JMenuItem item) {
        item.setActionCommand(EXIT_CMD);
        item.addActionListener(this);
    }


    public void registerItalics(JCheckBoxMenuItem checkBox) {
        checkBox.setActionCommand(ITALICS_CMD);
        checkBox.addActionListener(this);
        italicsBox = checkBox;
    }


    public void registerGothic(JCheckBoxMenuItem checkBox) {
        checkBox.setActionCommand(GOTHIC_CMD);
        checkBox.addActionListener(this);
        gothicBox = checkBox;
    }


    public void registerNoise(JCheckBoxMenuItem checkBox) {
        checkBox.setActionCommand(NOISE_CMD);
        checkBox.addActionListener(this);
        noiseBox = checkBox;
    }


    public void setModel(LettersBrowser browser) {
        this.browser = browser;
    }


    public void setWindow(Component parent) {
        this.browserWindow = parent;
    }
}

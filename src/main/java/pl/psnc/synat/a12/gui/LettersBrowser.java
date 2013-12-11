package pl.psnc.synat.a12.gui;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

import pl.psnc.synat.a12.generator.BaseLinesFile;
import pl.psnc.synat.a12.generator.ImageSelector;
import pl.psnc.synat.a12.generator.custom.LettersProvider;

public class LettersBrowser implements Runnable {

    private final static Logger logger = Logger.getLogger(LettersBrowser.class);

    private LettersProvider alphabet;
    private MainForm frame;

    public void load(File storeFile)
            throws IOException {
        Map<Character, Integer> offsets;
        logger.info("Loading alphabet...");
        alphabet = new LettersProvider(storeFile);
        alphabet.load(ImageSelector.FINAL);
        logger.info("Chars loaded: " + alphabet.getAlphabet().size());

        File file = new File(storeFile, "baselines.txt");

        if (file.isFile()) {
            logger.info("Found baselines file, loading...");
            offsets = new BaseLinesFile(file).read();
            logger.info("Done");
            frame.setOffsets(offsets);
        }
    }

    public void run() {
        frame = new MainForm();
        frame.init();
    }

    public static void main(String[] args)
            throws IOException {
        final LettersBrowser browser = new LettersBrowser();
        ActionsController.getInstance().setModel(browser);

        SwingUtilities.invokeLater(browser);
    }

    public void updateFilter(boolean italics, boolean noise, boolean gothic) {
        LettersProvider filtered = new LettersProvider(alphabet, noise, italics, gothic);
        frame.setAlphabet(filtered);
    }

    public void saveOffsets(File file)
            throws IOException {
        Map<Character, Integer> offsets = frame.getOffsets();
        BaseLinesFile baseline = new BaseLinesFile(file);
        baseline.write(offsets);
    }
}

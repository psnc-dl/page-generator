package pl.psnc.synat.a12.generator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class TextGenerator {
    
    private final static Logger logger = Logger.getLogger(TextGenerator.class);

    private final static List<Character> punctuations = Arrays.asList(new Character[] { ',', '.', '?', '!', ':' });

    private double spaceEmRatio = 0.25;

    private EmEstimate emCalculator;

    private int spacesCount;

    private List<LineEstimate> lines;

    private boolean verbose;


    public TextGenerator(List<LineEstimate> lines) {
        this.lines = lines;
        emCalculator = new EmEstimate();
        emCalculator.setLetterBoxes(lines);
        emCalculator.calculateEm();
    }


    public void printText() {
        for (BoxLine line : lines) {
            logger.info(buildLine(line).toString());
        }

        if (isVerbose()) {
            emCalculator.printInfo();

            System.err.println("Added " + spacesCount + " spaces");
        }
    }


    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }


    public boolean isVerbose() {
        return verbose;
    }


    public void setSpaceEmRatio(double spaceEmRatio) {
        this.spaceEmRatio = spaceEmRatio;
    }


    public double getSpaceEmRatio() {
        return spaceEmRatio;
    }


    private StringBuilder buildLine(BoxLine line) {
        Iterator<LetterBox> letters = line.getBoxes().iterator();
        Set<Character> punctuationGlyphs = new HashSet<Character>(punctuations);
        StringBuilder sb = new StringBuilder();
        LetterBox previous = null;

        while (letters.hasNext()) {
            LetterBox box = letters.next();

            if (previous != null && (punctuationGlyphs.contains(previous.getGlyph()) || spaceDetected(previous, box))) {
                sb.append(' ');
                spacesCount++;
            }
            sb.append(box.getGlyph());
            previous = box;
        }
        return sb;
    }


    private boolean spaceDetected(LetterBox previous, LetterBox next) {
        if (emCalculator.getEmValue() == 0) {
            return false;
        }

        double space = next.getX1() - previous.getX2();
        double ratio = space / emCalculator.getEmValue();
        return ratio > getSpaceEmRatio();
    }
}

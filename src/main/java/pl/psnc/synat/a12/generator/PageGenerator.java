package pl.psnc.synat.a12.generator;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import org.apache.log4j.Logger;

public class PageGenerator extends AbstractGenerator<LineEstimate> {

    private final static Logger logger = Logger.getLogger(PageGenerator.class);

    protected File store;
    protected File[] images;
    protected int matchesCount;
    protected int multiMatched;
    protected Double spaceEmRatio;
    protected boolean overlap = false;

    public PageGenerator(String path) {
        super(new LinkedList<LineEstimate>());
        store = new File(path);
        images = store.listFiles(ImageSelector.FINAL);
    }

    @Override
    public void init() throws Exception {
        for (File file : images) {
            LetterBox box = new LetterBox(file);
            addMatch(box);
        }
        adjustLines();
    }

    public void printStats() {
        logger.debug("TOTAL: " + multiMatched + " of " + images.length);
        logger.debug("Estimated Matching Accuracy: "
                + (100 - (double) multiMatched / (double) images.length * 100) + "%");
    }

    public TextGenerator getText() {
        TextGenerator text = new TextGenerator(lines);
        text.setVerbose(verbose);

        if (spaceEmRatio != null) {
            text.setSpaceEmRatio(spaceEmRatio);
        }
        return text;
    }

    public void setEmRatio(Double emRatio) {
        spaceEmRatio = emRatio;
    }

    protected void adjustLines() {
        Collections.sort(lines, new TopDownOrder());

        for (LineEstimate line : lines) {
            line.sort();

            if (!overlap) {
                line.adjust();
            }
        }
    }

    protected void addMatch(LetterBox box) {
        matchesCount = 0;
        LineEstimate matchingLine = findMatch(box);

        if (matchingLine == null) {
            matchingLine = new LineEstimate();
            lines.add(matchingLine);
        }

        matchingLine.add(box);

        if (matchesCount > 1) {
            multiMatched++;

            if (isVerbose()) {
                logger.error(box.getGlyph() + ": " + matchesCount);
            }
        }
    }

    private LineEstimate findMatch(LetterBox box) {
        LineEstimate matchingLine = null;
        int distance = Integer.MAX_VALUE;

        for (LineEstimate line : lines) {
            if (line.match(box)) {
                int d = line.distance(box);
                matchesCount++;

                if (d < distance) {
                    matchingLine = line;
                    distance = d;
                }
            }
        }
        return matchingLine;
    }

    public void setOverlap(boolean overlap) {
        this.overlap = overlap;
    }

    public boolean isOverlap() {
        return overlap;
    }

    public File getStore() {
        return store;
    }
}

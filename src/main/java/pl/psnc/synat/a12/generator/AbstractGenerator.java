package pl.psnc.synat.a12.generator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractGenerator<T extends BoxLine> {

    protected List<T> lines;
    protected boolean verbose;
    protected boolean showFontType;
    private boolean skipItalic;
    private boolean skipNoise;
    private boolean skipGothic;


    protected AbstractGenerator(List<T> lines) {
        this.lines = lines;
    }

    public void init() throws Exception {
    }

    public Map<Character, Integer> getBaseLines() {
        BaseLine baseLine = new BaseLine();
        baseLine.setSkipItalic(skipItalic);
        baseLine.setSkipNoise(skipNoise);
        baseLine.setSkipGothic(skipGothic);

        for (BoxLine line : lines) {
            baseLine.addLine(line);
        }
        return baseLine.getOffsets();
    }
    

    public File generateBoxFile(String boxFilename, int height)
            throws IOException {
        TesseractBoxWriter writer = new TesseractBoxWriter(boxFilename, height);
        writer.setStoreFontType(showFontType);

        try {
            for (BoxLine line : lines) {
                for (LetterBox box : line.getBoxes()) {
                    writer.write(box);
                }
            }
        } finally {
            writer.close();
        }
        
        return writer.getFile();
    }


    public Page getPage() {
        return new Page(lines, skipItalic, skipNoise, skipGothic);
    }


    public List<T> getLines() {
        return lines;
    }


    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }


    public boolean isVerbose() {
        return verbose;
    }


    public void setShowFontType(boolean setFontType) {
        this.showFontType = setFontType;
    }


    public void setSkipItalics(boolean b) {
        skipItalic = b;
    }


    public void setSkipNoise(boolean b) {
        skipNoise = b;
    }


    public void setSkipGothic(boolean setGothic) {
        skipGothic = setGothic;
    }
}
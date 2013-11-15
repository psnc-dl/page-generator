package pl.psnc.synat.a12.generator;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TesseractBoxWriter extends BoxWriter {

    private final int height;

    public TesseractBoxWriter(String filename, int height)
            throws FileNotFoundException {
        super(filename);
        this.height = height;
    }

    @Override
    public void write(LetterBox box)
            throws IOException {
        
        int y1 = height - box.getY2();
        int y2 = height - box.getY1();
        writeBox(box.getGlyph(), box.getX1(), y1, box.getX2(), y2, box.isItalic(), box.isGothic());
    }
}

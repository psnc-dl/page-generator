package pl.psnc.synat.a12.model;

import pl.psnc.synat.a12.model.BoxWriter;
import pl.psnc.synat.a12.model.LetterBox;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;

public class TesseractBoxWriter extends BoxWriter {

    private final Logger logger = Logger.getLogger(TesseractBoxWriter.class);

    private final int height;

    public TesseractBoxWriter(String filename, int height)
            throws FileNotFoundException {
        super(filename);
        this.height = height;
        logger.info("Image height set to:" + height);
    }

    @Override
    public void write(LetterBox box)
            throws IOException {

        int y1 = height - box.getY2();
        if (y1 < 0) {
            logger.info("height lower than 0 for " + box.getY2());
        }

        int y2 = height - box.getY1();
        if (y2 < 0) {
            logger.info("height lower than 0 for " + box.getY1());
        }

        writeBox(box.getGlyph(), box.getX1(), y1, box.getX2(), y2, box.isItalic(), box.isGothic());
    }
}

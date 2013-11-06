package pl.psnc.synat.a12.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TesseractBoxWriter {

    private final BufferedWriter writer;
    private final int height;
    private boolean next;
    private boolean storeFontType;
    private File file;


    public TesseractBoxWriter(String filename, int height)
            throws FileNotFoundException {
        file = new File(filename);
    	OutputStream output = new FileOutputStream(file);
        this.writer = new BufferedWriter(new OutputStreamWriter(output));
        this.height = height;
    }


    public void write(LetterBox box)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        int y1 = height - box.getY2();
        int y2 = height - box.getY1();

        // this is undocumented tesseract feature?
        if (isStoreFontType() && box.isItalic()) {
            sb.append('$');
        }

        // this is undocumented tesseract feature?
        if (isStoreFontType() && box.isGothic()) {
            sb.append('*');
        }

        sb.append(box.getGlyph()).append(' ');
        sb.append(box.getX1()).append(' ');
        sb.append(y1).append(' ');
        sb.append(box.getX2()).append(' ');
        sb.append(y2).append(' ');
        sb.append(0);

        if (next) {
            writer.newLine();
        } else {
            next = true;
        }
        writer.append(sb);
    }


    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setStoreFontType(boolean storeFontType) {
        this.storeFontType = storeFontType;
    }


    public boolean isStoreFontType() {
        return storeFontType;
    }
    
    public File getFile() {
    	return file;
    }
}

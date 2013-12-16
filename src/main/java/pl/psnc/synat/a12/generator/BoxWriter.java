package pl.psnc.synat.a12.generator;

import java.io.*;
import org.apache.log4j.Logger;

public class BoxWriter {
    
    private final static Logger logger = Logger.getLogger(BoxWriter.class);

    protected final BufferedWriter writer;
    protected boolean next;
    protected boolean storeFontType;
    protected File file;


    public BoxWriter(String filename)
            throws FileNotFoundException {

        if (!filename.endsWith(".box")) {
            filename = filename + ".box";
        }
        
        file = new File(filename);
        OutputStream output = new FileOutputStream(file);
        this.writer = new BufferedWriter(new OutputStreamWriter(output));
    }


    public void write(LetterBox box)
            throws IOException {
        writeBox(box.getGlyph(), box.getX1(), box.getY1(), box.getX2(), box.getY2(), box.isItalic(), box.isGothic());
    }

    protected void writeBox(char glyph,int x1,int y1,int x2,int y2,boolean italic, boolean gothic) throws IOException {
             StringBuilder sb = new StringBuilder();

        // this is undocumented tesseract feature?
        if (isStoreFontType() && italic) {
            sb.append('$');
        }

        // this is undocumented tesseract feature?
        if (isStoreFontType() && gothic) {
            sb.append('*');
        }

        sb.append(glyph).append(' ');
        sb.append(x1).append(' ');
        sb.append(y1).append(' ');
        sb.append(x2).append(' ');
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
            logger.error("Exception while closing box file",e);
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

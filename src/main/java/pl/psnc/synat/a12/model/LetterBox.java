package pl.psnc.synat.a12.model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import pl.psnc.synat.a12.model.sort.Sortable;

public class LetterBox extends BoundingBox implements Sortable {

    private static final String PREFIX_NOISE = "noise";
    private static final String PREFIX_GOTHIC = "gothic";
    private static final String TOKEN_ITALIC = "italic";
    private static final String PREFIX_NEW = "new_";

    private final char glyph;
    private Double threshold;
    private BufferedImage image;
    private boolean italic;
    private boolean noised;
    private boolean gothic;


    public LetterBox(File file)
            throws IOException {
        String filename = file.getName();
        italic = filename.contains(TOKEN_ITALIC);
        noised = filename.contains(PREFIX_NOISE);
        gothic = filename.contains(PREFIX_GOTHIC);

        if (filename.startsWith(PREFIX_NEW)) {
            filename = filename.substring(PREFIX_NEW.length());
        }

        final int endIndex = filename.indexOf('_');
        final String[] data = filename.substring(0, endIndex).split(",");

        image = ImageIO.read(file);
        x1 = Integer.parseInt(data[1]);
        y1 = Integer.parseInt(data[2]);
        y2 = image.getHeight() + y1;
        x2 = image.getWidth() + x1;

        glyph = (char) Integer.parseInt(data[3]);
    }
 
    public LetterBox(File file, int x1, int y1, int x2, int y2, char glyph, String fontType, boolean noised)
            throws IOException {

        this.italic = fontType.equals(TOKEN_ITALIC);
        this.gothic = fontType.equals(PREFIX_GOTHIC);
        this.noised = noised;
        this.image = ImageIO.read(file);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.glyph = glyph;
        
    }
    
    public LetterBox(LetterBox box, int newX1, int newY1, int newX2, int newY2) {
        italic = box.italic;
        gothic = box.gothic;
        noised = box.noised;
        image = box.image;
        x1 = newX1;
        y1 = newY1;
        x2 = newX2;
        y2 = newY2;
        glyph = box.glyph;
    }
    
    public LetterBox(LetterBox box, int newX1, int newY2) {
        italic = box.italic;
        gothic = box.gothic;
        noised = box.noised;
        image = box.image;
        x1 = newX1;
        y2 = newY2;
        x2 = box.getWidth() + x1;
        y1 = newY2 - box.getHeight();
        glyph = box.glyph;
    }

    public char getGlyph() {
        return glyph;
    }


    public void draw(Graphics graphics) {
        graphics.drawImage(image, getX1(), getY1(), null);
    }


    public void fill(Graphics graphics) {
        graphics.fillRect(getX1(), getY1(), getX2() - getX1(), getY2() - getY1());
    }


    public BufferedImage getImage() {
        return image;
    }


    public boolean isItalic() {
        return italic;
    }


    public boolean isNoised() {
        return noised;
    }


    public boolean isGothic() {
        return gothic;
    }


    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }


    public Double getThreshold() {
        return threshold;
    }


    public boolean hasThreshold() {
        return threshold != null;
    }
}

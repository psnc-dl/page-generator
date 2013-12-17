package pl.psnc.synat.cutter.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.apache.log4j.Logger;

import pl.psnc.synat.a12.model.BoundingBox;
import pl.psnc.synat.a12.model.LetterBox;

public class ImageProcesor {

    private static final Logger LOG = Logger.getLogger(ImageProcesor.class);
    public static final String FORMAT_PNG = "png";
    private BufferedImage image;
    private BufferedImage copy;
    private double threshold;

    static {
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
    }


    public static ImageProcesor read(final InputStream input)
            throws IOException {
        final BufferedImage origin = ImageIO.read(input);

        if (origin == null) {
            throw new IllegalArgumentException("Cannot read given image stream");
        }
        return new ImageProcesor(origin);
    }


    public ImageProcesor(final BufferedImage origin) {
        this.image = origin;
    }


    public void save() {
        copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        image.copyData(copy.getRaster());
    }


    public void load() {
        if (copy != null) {
            image = copy;
        }
    }


    public void crop(final int left, final int top, final int width, final int height) {
        BufferedImage cropped = new BufferedImage(width, height, image.getType());
        Graphics2D graphics = cropped.createGraphics();
        graphics.drawImage(image, 0, 0, width, height, left, top, left + width, top + height, null);
        image = cropped;
    }


    public void crop(int... coords) {
        if (coords.length == 4) {
            crop(coords[0], coords[1], coords[2] - coords[0] + 1, coords[3] - coords[1] + 1);
        }
    }


    public void crop(final BoundingBox cropBox) {
        crop(cropBox.getX1(), cropBox.getY1(), cropBox.getWidth(), cropBox.getHeight());
    }


    // FIXME
    public void clip(int[] xcoords, int[] ycoords) {
        BufferedImage clipped = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = clipped.createGraphics();
        Shape shape = new OutlinePolygon(xcoords, ycoords, ycoords.length);
        // g.setBackground(Color.WHITE);
        // g.clearRect(0, 0, clipped.getWidth(), clipped.getHeight());
        g.setClip(shape);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        image = clipped;
    }


    public int[] trim() {
        final Raster raster = image.getData();
        final int w = raster.getWidth();
        final int h = raster.getHeight();
        final int band = 0;
        final int[] trims = new int[4];
        int[] samples = new int[Math.max(w, h)];

        if (raster.getNumBands() != 1) {
            LOG.warn("Processed image has more than one sample per pixel, refuse to trim");
            return new int[] { 0, 0, w - 1, h - 1 };
        }
        for (int y = 0; y < h; y++) {
            samples = raster.getSamples(0, y, w, 1, band, samples);

            if (zeroArray(samples, w)) {
                trims[1] = y;
                break;
            }
        }
        for (int y = h - 1; y >= 0; y--) {
            samples = raster.getSamples(0, y, w, 1, band, samples);

            if (zeroArray(samples, w)) {
                trims[3] = y;
                break;
            }
        }

        for (int x = 0; x < w; x++) {
            samples = raster.getSamples(x, 0, 1, h, band, samples);

            if (zeroArray(samples, h)) {
                trims[0] = x;
                break;
            }
        }
        for (int x = w - 1; x >= 0; x--) {
            samples = raster.getSamples(x, 0, 1, h, band, samples);

            if (zeroArray(samples, h)) {
                trims[2] = x;
                break;
            }
        }
        return trims;
    }


    public int[] trim(LetterBox thresholdedLetter) {
        int[] trims = trim();
        thresholdedLetter.trim(trims);
        return trims;
    }


    public void setGrayscale() {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        copy.getGraphics().drawImage(image, 0, 0, null);
        image = copy;
    }


    public void binarize() {
        final PlanarImage dst = JAI.create("binarize", image, new Double(threshold));
        image = dst.getAsBufferedImage();
    }


    public Double setThresholdAuto() {
        final Histogram histogram = (Histogram) JAI.create("histogram", image).getProperty("histogram");
        threshold = histogram.getMinFuzzinessThreshold()[0];
        return threshold;
    }


    public void setThreshold(final LetterBox thresholdedLetter) {
        if (thresholdedLetter.hasThreshold()) {
            threshold = thresholdedLetter.getThreshold();
        } else {
            setThresholdAuto();
            thresholdedLetter.setThreshold(threshold);
        }
    }


    public void write(final OutputStream output, final String format)
            throws IOException {
        ImageIO.write(image, format, output);
    }


    public BufferedImage getImageCopy() {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        copy.getGraphics().drawImage(image, 0, 0, null);
        return copy;
    }


    public void strokeBrushPath(final List<Integer> coordinates) {
        if (coordinates.isEmpty() || coordinates.size() % 2 != 0) {
            return;
        }

        final Graphics graph = image.getGraphics();
        final Iterator<Integer> coordin = coordinates.iterator();
        graph.setColor(Color.WHITE);

        while (coordin.hasNext()) {
            final int xCoordin = coordin.next();
            final int yCoordin = coordin.next();
            graph.fillRect(xCoordin, yCoordin, 1, 1);
        }
    }


    public int getHeight() {
        return image.getHeight();
    }


    private boolean zeroArray(int[] samples, int size) {
        for (int i = 0; i < size; i++) {
            if (samples[i] == 0) {
                return true;
            }
        }
        return false;
    }
}

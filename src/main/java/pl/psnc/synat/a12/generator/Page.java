package pl.psnc.synat.a12.generator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class Page extends BoxFilter {

    final private List<? extends BoxLine> lines;

    private BufferedImage page;

    private boolean showBaselines = false;


    public Page(List<? extends BoxLine> lines, boolean skipItalic, boolean skipNoise, boolean skipGothic) {
        this.lines = lines;
        this.setSkipItalic(skipItalic);
        this.setSkipNoise(skipNoise);
        this.setSkipGothic(skipGothic);
    }


    public Page(List<? extends BoxLine> lines2) {
        this(lines2, false, false, false);
    }


    public void draw(int width, int height) {
        page = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        BaseLine baseLine = new BaseLine();
        baseLine.setSkipItalic(isSkipItalic());
        baseLine.setSkipNoise(isSkipNoise());

        Graphics graphics = page.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        
        for (BoxLine line : lines) {

            for (LetterBox box : line.getBoxes()) {
                if (!skip(box)) {
                    box.draw(graphics);
                }
            }

            if (isShowBaselines()) {
                List<LetterBox> selected = filter(line.getBoxes());
                int baseline = baseLine.calculateBaseLine(selected);
                graphics.setColor(Color.BLUE);
                graphics.drawLine(0, baseline, width, baseline);
            }
        }
    }


    public void save(File file)
            throws IOException {
        ImageIO.write(page, "png", file);
    }


    public void setShowBaselines(boolean showBaselines) {
        this.showBaselines = showBaselines;
    }


    public boolean isShowBaselines() {
        return showBaselines;
    }
}

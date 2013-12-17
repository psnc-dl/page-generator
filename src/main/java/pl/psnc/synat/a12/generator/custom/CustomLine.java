package pl.psnc.synat.a12.generator.custom;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.psnc.synat.a12.model.BoxLine;
import pl.psnc.synat.a12.model.LetterBox;

public class CustomLine implements BoxLine {

    private List<LetterBox> boxes = new LinkedList<LetterBox>();
    private int emValue;
    private int baseLine;
    private int xcoordin;
    private int marginLeft;
    private int marginTop;
    private int lineWidth;

    private double spaceEmRatio = 0.35;
    private double interlineEmRatio = 0.25;
    private double interletterEmRatio = 0.05;
    private Map<Character, Integer> offsets;


    public CustomLine(Map<Character, Integer> offsets, int emValue, int marginLeft, int marginTop) {
        this(offsets, emValue, marginLeft, marginTop, 0);
    }


    public CustomLine(Map<Character, Integer> offsets, int emValue, int marginLeft, int marginTop, int lineWidth) {
        this.emValue = emValue;
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.lineWidth = lineWidth;
        this.offsets = offsets;

        xcoordin = marginLeft;
        baseLine = marginTop + emValue;
    }


    public void add(LetterBox box) {
        int offset = getOffset(box.getGlyph());
        LetterBox newBox = new LetterBox(box, xcoordin, baseLine);

        newBox.moveY(offset);
        xcoordin += newBox.getWidth() + interletterEmRatio * emValue;
        boxes.add(newBox);
    }


    public void addWhiteSpace() {
        xcoordin += (int) Math.round(emValue * spaceEmRatio);
    }


    public CustomLine newLine() {
        final int interlined = (int) (baseLine + Math.round(interlineEmRatio * emValue));
        return new CustomLine(offsets, emValue, marginLeft, interlined, lineWidth);
    }


    public List<LetterBox> getBoxes() {
        return boxes;
    }


    public int getY1() {
        return marginTop;
    }


    public int getX1() {
        return marginLeft;
    }


    public boolean enoughSpace(List<LetterBox> wordBox) {
        int width = 0;

        for (LetterBox letterBox : wordBox) {
            width += letterBox.getWidth();
        }
        return (width + xcoordin < lineWidth + marginLeft);
    }


    private int getOffset(char c) {
        if (offsets != null && offsets.containsKey(c))
            return offsets.get(c);
        return 0;
    }
}

package pl.psnc.synat.a12.model;

import pl.psnc.synat.a12.common.StatUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pl.psnc.synat.a12.common.MultiMap;

public class BaseLine extends BoxFilter {

    private static char[] baselineChars = new char[] { 'M', 'm', 'w', 'W', 'n', 'k', 'H' };

    private MultiMap<Character, Integer> offsets = new MultiMap<Character, Integer>();


    public void addLine(BoxLine line) {
        List<LetterBox> boxes = filter(line.getBoxes());
        int baseline = calculateBaseLine(boxes);

        for (LetterBox box : boxes) {
            offsets.putElement(box.getGlyph(), box.getY2() - baseline);
        }
    }


    public Map<Character, Integer> getOffsets() {
        Map<Character, Integer> offsetAvg = new HashMap<Character, Integer>();

        for (Entry<Character, List<Integer>> entry : offsets.entrySet()) {
            int avg = (int) Math.round(StatUtils.avg(entry.getValue()));
            offsetAvg.put(entry.getKey(), avg);
        }
        return offsetAvg;
    }


    public int calculateBaseLine(List<LetterBox> boxes) {
        List<LetterBox> selected = new LinkedList<LetterBox>();
        int i = 0;

        do {
            selected.addAll(selectBoxes(boxes, baselineChars[i]));
            i++;
        } while (i < baselineChars.length);

        if (selected.size() == 0) {
            return (int) Math.round(getBaselinesAvg(boxes));
        } else {
            return (int) Math.round(getBaselinesAvg(selected));
        }
    }


    private double getBaselinesAvg(List<LetterBox> boxes) {
        int sum = 0;

        for (LetterBox box : boxes) {
            sum += box.getY2();
        }
        return (double) sum / boxes.size();
    }


    private List<LetterBox> selectBoxes(List<LetterBox> list, char c) {
        List<LetterBox> selected = new LinkedList<LetterBox>();

        for (LetterBox box : list) {
            if (box.getGlyph() == c) {
                selected.add(box);
            }
        }
        return selected;
    }
}

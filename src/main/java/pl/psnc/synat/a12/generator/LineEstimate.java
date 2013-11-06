package pl.psnc.synat.a12.generator;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LineEstimate implements BoxLine {

    private LetterBox firstLetter;

    private Comparator<Sortable> lineOrder = new LeftToRightOrder();

    private List<LetterBox> boxes = new LinkedList<LetterBox>();


    public void add(LetterBox box) {
        updateEstimate(box);
        boxes.add(box);
    }


    public boolean match(LetterBox box) {
        LetterBox closest = getClosestX(box);
        return closest.overlapY(box);
    }


    public int distance(LetterBox box) {
        LetterBox closest = getClosestX(box);
        return closest.distanceY(box);
    }


    public void sort() {
        Collections.sort(boxes, lineOrder);
        firstLetter = boxes.get(0);
    }


    public void adjust() {
        Iterator<LetterBox> iterator = boxes.iterator();
        LetterBox previous = iterator.next();

        while (iterator.hasNext()) {
            LetterBox current = iterator.next();
            int delta = previous.getX2() - current.getX1();

            if (delta > 0) {
                current.moveX(delta);
            }
            previous = current;
        }
    }


    public List<LetterBox> getBoxes() {
        return boxes;
    }


    public int getY1() {
        return firstLetter.getY1();
    }


    public int getX1() {
        return firstLetter.getY2();
    }


    private LetterBox getClosestX(LetterBox box) {
        int min = Integer.MAX_VALUE;
        LetterBox closest = null;

        for (LetterBox aligned : boxes) {
            int d = aligned.distanceX(box);

            if (d < min) {
                min = d;
                closest = aligned;
            }
        }
        return closest;
    }


    private void updateEstimate(LetterBox box) {
        if (firstLetter == null || lineOrder.compare(firstLetter, box) > 0) {
            firstLetter = box;
        }
    }
}

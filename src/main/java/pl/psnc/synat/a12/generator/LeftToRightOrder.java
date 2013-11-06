package pl.psnc.synat.a12.generator;

import java.util.Comparator;

public class LeftToRightOrder implements Comparator<Sortable> {

    public int compare(Sortable box1, Sortable box2) {
        if (box1.getX1() < box2.getX1())
            return -1;

        if (box1.getX1() > box2.getX1())
            return 1;

        return 0;
    }
}

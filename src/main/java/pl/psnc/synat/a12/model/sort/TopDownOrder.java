package pl.psnc.synat.a12.model.sort;

import java.util.Comparator;

public class TopDownOrder implements Comparator<Sortable> {

    public int compare(Sortable box1, Sortable box2) {
        if (box1.getY1() < box2.getY1())
            return -1;

        if (box1.getY1() > box2.getY1())
            return 1;

        return 0;
    }
}

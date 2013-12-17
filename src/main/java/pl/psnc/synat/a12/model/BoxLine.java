package pl.psnc.synat.a12.model;

import java.util.List;
import pl.psnc.synat.a12.model.sort.Sortable;

public interface BoxLine extends Sortable {

    void add(LetterBox box);


    List<LetterBox> getBoxes();


    int getY1();


    int getX1();
}
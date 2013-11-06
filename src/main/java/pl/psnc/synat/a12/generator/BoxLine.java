package pl.psnc.synat.a12.generator;

import java.util.List;

public interface BoxLine extends Sortable {

    void add(LetterBox box);


    List<LetterBox> getBoxes();


    int getY1();


    int getX1();
}
package pl.psnc.synat.a12.generator;

import java.util.LinkedList;
import java.util.List;

public class BoxFilter {

    private boolean skipItalic;
    private boolean skipNoise;
    private boolean skipGothic;


    public BoxFilter() {
        // empty
    }


    public BoxFilter(boolean filterNoise, boolean filterItalics, boolean filterGothic) {
        skipItalic = filterItalics;
        skipNoise = filterNoise;
        setSkipGothic(filterGothic);
    }


    protected boolean skip(LetterBox box) {
        if (isSkipItalic() && box.isItalic() || isSkipNoise() && box.isNoised() || isSkipGothic() && box.isGothic())
            return true;
        return false;
    }


    /**
     * Returns filtered copy of a given list.
     * 
     * @param list
     * @return filtered copy
     */
    protected List<LetterBox> filter(List<LetterBox> list) {
        LinkedList<LetterBox> selected = new LinkedList<LetterBox>();

        for (LetterBox letterBox : list) {
            if (!skip(letterBox))
                selected.add(letterBox);
        }
        return selected;
    }


    public void setSkipItalic(boolean skipItalic) {
        this.skipItalic = skipItalic;
    }


    public boolean isSkipItalic() {
        return skipItalic;
    }


    public void setSkipNoise(boolean skipNoise) {
        this.skipNoise = skipNoise;
    }


    public boolean isSkipNoise() {
        return skipNoise;
    }


    public void setSkipGothic(boolean skipGothic) {
        this.skipGothic = skipGothic;
    }


    public boolean isSkipGothic() {
        return skipGothic;
    }

}
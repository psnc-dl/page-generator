package pl.psnc.synat.a12.common;

import pl.psnc.synat.a12.model.LetterBox;
import pl.psnc.synat.a12.model.BoxLine;
import pl.psnc.synat.a12.common.StatUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Calculates em width based on the given font.
 */
public class EmEstimate {

    private final static List<Character> emChars = Arrays.asList(new Character[] { 'M', 'W', 'm', 'w' });

    private final MultiMap<Character, Integer> lettersWidths = new MultiMap<Character, Integer>();

    private int emLetterCount;
    private int emValue;
    private char emLetter;


    public int calculateEm() {
        findEm();
        return emValue;
    }


    public void printInfo() {
        if (emValue == 0) {
            System.err.println("Em size couldn't be estimated, letters not found: " + emChars);
        } else {
            System.err.println("Estimated em size as " + emValue + " basing on " + emLetterCount + " instances of "
                    + emLetter);
        }
    }


    public int getEmValue() {
        return emValue;
    }


    public void setLetterBoxes(Collection<? extends BoxLine> lines) {
        Set<Character> requested = new HashSet<Character>(emChars);

        for (BoxLine line : lines) {
            for (LetterBox box : line.getBoxes()) {
                Character key = box.getGlyph();

                if (requested.contains(key)) {
                    lettersWidths.putElement(key, box.getWidth());
                }
            }
        }
    }


    public void setLetterBoxes(MultiMap<Character, LetterBox> alphabet) {
        for (Character emChar : emChars) {
            if (!alphabet.containsKey(emChar)) {
                continue;
            }

            for (LetterBox letterBox : alphabet.get(emChar)) {
                lettersWidths.putElement(emChar, letterBox.getWidth());
            }
        }
    }


    private void findEm() {
        Iterator<Character> requested = emChars.iterator();
        Character key = requested.next();
        int median = 0;

        while (!lettersWidths.containsKey(key) && requested.hasNext()) {
            key = requested.next();
        }

        if (lettersWidths.containsKey(key)) {
            median = StatUtils.median(lettersWidths.get(key));
            emLetter = key;
            emLetterCount = lettersWidths.get(key).size();
        }
        emValue = median;
    }
}

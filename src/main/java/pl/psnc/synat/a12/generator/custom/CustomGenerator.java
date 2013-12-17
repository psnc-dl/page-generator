package pl.psnc.synat.a12.generator.custom;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.psnc.synat.a12.generator.AbstractGenerator;
import pl.psnc.synat.a12.common.EmEstimate;
import pl.psnc.synat.a12.model.LetterBox;

public class CustomGenerator extends AbstractGenerator<CustomLine> {

    private LettersProvider letters;

    private EmEstimate emCalculator;

    private CustomLine currentLine;

    private Map<Character, Integer> offsets;


    public CustomGenerator(LettersProvider letters) {
        super(new LinkedList<CustomLine>());
        this.letters = letters;
        emCalculator = new EmEstimate();
        emCalculator.setLetterBoxes(letters.getAlphabet());
        emCalculator.calculateEm();
        emCalculator.printInfo();
    }


    public void setBaselineOffsets(Map<Character, Integer> offsets) {
        this.offsets = offsets;
    }


    public void generate(String[] text, int margin) {
        currentLine = new CustomLine(offsets, emCalculator.getEmValue(), margin, margin);

        for (int i = 0; i < text.length; i++) {
            addLine(text[i]);
            currentLine = currentLine.newLine();
        }
    }


    public void generate(String text, int pageWidth, int margin) {
        String[] words = text.split("\\s");
        currentLine = new CustomLine(offsets, emCalculator.getEmValue(), margin, margin, pageWidth - 2 * margin);
        lines.add(currentLine);

        for (String w : words) {
            addWord(w);
        }
    }


    private void addWord(String w) {
        List<LetterBox> wordBox = new LinkedList<LetterBox>();

        for (int i = 0; i < w.length(); i++) {
            LetterBox box = letters.get(w.charAt(i));
            wordBox.add(box);
        }

        if (!currentLine.enoughSpace(wordBox)) {
            currentLine = currentLine.newLine();
            lines.add(currentLine);
        }

        for (LetterBox letterBox : wordBox) {
            currentLine.add(letterBox);
        }
        currentLine.addWhiteSpace();
    }


    private void addLine(String string) {
        lines.add(currentLine);

        for (int i = 0; i < string.length(); i++) {
            final char charAt = string.charAt(i);

            if (Character.isWhitespace(charAt)) {
                currentLine.addWhiteSpace();
                continue;
            }
            LetterBox box = letters.get(charAt);
            currentLine.add(box);
        }
    }
}

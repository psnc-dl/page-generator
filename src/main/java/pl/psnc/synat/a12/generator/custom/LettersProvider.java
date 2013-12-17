package pl.psnc.synat.a12.generator.custom;

import pl.psnc.synat.a12.common.MultiMap;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pl.psnc.synat.a12.model.BoxFilter;
import pl.psnc.synat.a12.model.LetterBox;

public class LettersProvider extends BoxFilter {

    private File[] images;
    private File store;
    private MultiMap<Character, LetterBox> alphabet = new MultiMap<Character, LetterBox>();
    private Map<Character, Integer> index = new HashMap<Character, Integer>();


    public LettersProvider(String storePath) {
        store = new File(storePath);

        if (!store.isDirectory()) {
            throw new IllegalArgumentException("Provided file must be directory");
        }
    }


    public LettersProvider(File store) {
        this.store = store;

        if (!store.isDirectory()) {
            throw new IllegalArgumentException("Provided file must be directory");
        }
    }


    public LettersProvider(LettersProvider origin, boolean filterNoise, boolean filterItalics, boolean filterGothic) {
        super(filterNoise, filterItalics, filterGothic);

        for (Entry<Character, List<LetterBox>> entry : origin.alphabet.entrySet()) {
            List<LetterBox> copy = filter(entry.getValue());
            alphabet.put(entry.getKey(), copy);
        }
        init();
    }


    public void load(FilenameFilter selector)
            throws IOException {
        if (store == null) {
            throw new IllegalStateException();
        }
        images = store.listFiles(selector);

        for (File image : images) {
            LetterBox box = new LetterBox(image);

            if (!skip(box)) {
                alphabet.putElement(box.getGlyph(), box);
            }
        }
        init();
        randomize();
    }


    public LetterBox get(char c) {
        if (!alphabet.containsKey(c)) {
            throw new IllegalArgumentException("Specified alphabet does not contain glyph: " + c);
        }

        int i = index.get(c);
        index.put(c, (i + 1) % alphabet.size(c));
        return alphabet.get(c, i);
    }


    public MultiMap<Character, LetterBox> getAlphabet() {
        return alphabet;
    }


    private void init() {
        for (Character c : alphabet.keySet()) {
            index.put(c, 0);
        }
    }


    private void randomize() {
        for (List<LetterBox> glyps : alphabet.values()) {
            Collections.shuffle(glyps);
        }
    }
}

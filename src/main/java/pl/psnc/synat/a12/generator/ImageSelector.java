package pl.psnc.synat.a12.generator;

import java.io.File;
import java.io.FilenameFilter;

public class ImageSelector implements FilenameFilter {

    public static final ImageSelector FINAL = new ImageSelector("final");

    public static final ImageSelector ORIGIN = new ImageSelector("origin");

    public static final ImageSelector BINARIZED_ONLY = new ImageSelector("binarized");

    private final String selector;


    private ImageSelector(String selector) {
        this.selector = selector;
    }


    public boolean accept(File dir, String name) {
        return name.contains(selector);
    }
}
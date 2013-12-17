package pl.psnc.synat.a12.generator.cutouts;

import pl.psnc.synat.a12.model.ImageSelector;
import pl.psnc.synat.a12.model.LetterBox;
import pl.psnc.synat.a12.model.BoxLine;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import pl.psnc.synat.a12.generator.*;
import pl.psnc.synat.a12.model.TesseractBoxWriter;

public class CutoutsPageGenerator extends PageGenerator {

    private File[] metadataFiles;
    private Short fileHeight;
    private Short fileWidth;
    private HashMap<String, File> imageList = new HashMap<String, File>();
    private HashMap<String, File> metadataList = new HashMap<String, File>();


    public CutoutsPageGenerator(String path) {
        super(path);
        metadataFiles = store.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        for (File file : images) {
            String fileName = file.getName();
            int pos = fileName.lastIndexOf(".");
            if (pos > 0) {
                fileName = fileName.substring(0, pos);
            }
            String base = fileName.replace("_" + ImageSelector.FINAL.getSelector(), "");
            imageList.put(base, file);
        }
        for (File file : metadataFiles) {
            String fileName = file.getName();
            int pos = fileName.lastIndexOf(".");
            if (pos > 0) {
                fileName = fileName.substring(0, pos);
            }
            metadataList.put(fileName, file);
        }
    }


    /**
     * Extracts file width, heigh, image coordinates from xml. 
     * @throws JAXBException
     * @throws IOException 
     */
    @Override
    public void init()
            throws JAXBException, IOException {

        for (String key : metadataList.keySet()) {
            File metadataFile = metadataList.get(key);
            File image = imageList.get(key);
            CutoutsXmlReader reader = new CutoutsXmlReader(metadataFile.getPath());
            pl.psnc.synat.a12.generator.utils.jaxb.LetterBox cutoutsBox = reader.read();
            if (fileHeight == null) {
                fileHeight = cutoutsBox.getSize().getHeight();
            }
            if (fileWidth == null) {
                fileWidth = cutoutsBox.getSize().getWidth();
            }
            int x1 = cutoutsBox.getBoundingBox().getCoordinate().get(0).getX();
            int y1 = cutoutsBox.getBoundingBox().getCoordinate().get(0).getY();
            int x2 = cutoutsBox.getBoundingBox().getCoordinate().get(1).getX();
            int y2 = cutoutsBox.getBoundingBox().getCoordinate().get(1).getY();
            char glyph = cutoutsBox.getSymbol().charAt(0);

            LetterBox letterBox = new LetterBox(image, x1, y1, x2, y2, glyph, cutoutsBox.getFontType(), cutoutsBox.isUnreadable());
            addMatch(letterBox);
        }
        adjustLines();
    }


    @Override
    public File generateBoxFile(String boxFilename, int height)
            throws IOException {
        
        TesseractBoxWriter writer = new TesseractBoxWriter(boxFilename, height);
        writer.setStoreFontType(showFontType);

        try {
            for (BoxLine line : lines) {
                for (LetterBox box : line.getBoxes()) {
                    writer.write(box);
                }
            }
        } finally {
            writer.close();
        }

        return writer.getFile();
    }


    public Short getFileHeight() {
        return fileHeight;
    }


    public Short getFileWidth() {
        return fileWidth;
    }

}

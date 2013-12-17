package pl.psnc.synat.a12.aletheia;

import pl.psnc.synat.a12.common.CLIUtils;
import pl.psnc.synat.a12.aletheia.pagexml.PageXmlReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;

import pl.psnc.synat.a12.common.CLIUtils.Command;
import pl.psnc.synat.a12.aletheia.pagexml.PageXmlReader.Glyph;
import pl.psnc.synat.a12.common.ZipOutputFile;
import pl.psnc.synat.a12.model.BoundingBox;
import pl.psnc.synat.cutter.image.ImageProcesor;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.apache.log4j.Logger;
import pl.psnc.synat.a12.generator.CLI;

/**
 * Class which cuts out glyphs from IMPACT-based transcript in PAGE XML format. 
 * This is the first step, after reviewing the output of Cutter one can use
 * {@link CLI} to creating Tesseract training images. Check out project documentation 
 * for details.
 */
public class Cutter {

    private static final String TOKEN_GOTHIC = "_gothic";

    private static final String TOKEN_ITALIC = "_italic";

    private static final Object TOKEN_NOISE = "_noise";
    
    private static final Logger logger = Logger.getLogger(Cutter.class);
    
    public class CutterArguments extends AletheiaArguments implements Command {

        @Parameter(names = "--output", description = "name and path for output archive", required = true)
        private String outputFilename;

        @Parameter(names = "--image", description = "file name of a image input", required = true)
        private String inputImageFilename;

        @Parameter(names = "--font", description = "mark generated glyphs as a {gothic, italic} font")
        private String font;

        @Parameter(names = "--only-read-xml", description = "reads only xml file, does not generate anything")
        private Boolean onlyReadXml;

        public String getInputImageFilename() {
            return inputImageFilename;
        }


        public boolean isGothic() {
            return "gothic".equals(font);
        }


        public boolean isItalic() {
            return "italic".equals(font);
        }


        public boolean isReadXmlOnly() {
            return onlyReadXml == null ? false : onlyReadXml;
        }
        

        public String getOutputFilename() {
            return outputFilename;
        }
    }


    public CutterArguments arguments = new CutterArguments();
    private int counter;
    private ZipOutputFile outputZipArchive;

    public static void main(String[] args)
            throws IOException, JAXBException {
        Cutter proc = new Cutter();
        JCommander engine = CLIUtils.createEngine("pl.psnc.synat.a12.aletheia.Cutter", proc.arguments);

        try {
            engine.parse(args);
            CLIUtils.checkHelp(engine, proc.arguments);
        } catch (ParameterException e) {
            CLIUtils.handleParameterException(engine, e);
            System.exit(1);
        }
        proc.run();
    }
    
    private void run()
            throws IOException, JAXBException {
        
    	outputZipArchive = new ZipOutputFile(arguments.getOutputFilename());

        arguments.readNoiseList();
        logger.info("Noise words: " + arguments.getNoiseWords());
        logger.info("Tabu types: " + arguments.tabuTypes);

        PageXmlReader reader = new PageXmlReader(arguments.inputXmlFilename);
        reader.read(arguments.getNoiseWords(), arguments.tabuTypes);

        if (arguments.isReadXmlOnly()) {
            return;
        }

        InputStream input = new BufferedInputStream(new FileInputStream(arguments.inputImageFilename));
        ImageProcesor imageProcessor = ImageProcesor.read(input);
        imageProcessor.setGrayscale();
        imageProcessor.save();

        Iterator<Glyph> iterator = reader.getGlyphs().iterator();

        while (iterator.hasNext()) {
            Glyph glyph = iterator.next();

            if (counter % 100 == 0)
                logger.info("Processing: " + counter + " of " + reader.glyphsCount());

            counter++;
            BoundingBox box = new BoundingBox(glyph.xCoord(), glyph.yCoord());
            File boxFile = createFile(glyph, box);
            OutputStream output = new FileOutputStream(boxFile); 

            imageProcessor.crop(box);
            imageProcessor.setThresholdAuto();
            imageProcessor.binarize();
            imageProcessor.clip(normalize(glyph.xCoord(), box.getX1()), normalize(glyph.yCoord(), box.getY1()));
            imageProcessor.write(output, ImageProcesor.FORMAT_PNG);
            imageProcessor.load();
            
            outputZipArchive.addFile(boxFile);
            
            FileUtils.deleteQuietly(boxFile);
        }
        
        outputZipArchive.close();
    }


    private int[] normalize(int[] coords, int base) {
        int[] norm = new int[coords.length];

        for (int i = 0; i < norm.length; i++) {
            norm[i] = coords[i] - base;
        }
        return norm;
    }


    private File createFile(Glyph glyph, BoundingBox box)
            throws FileNotFoundException {
        StringBuilder filename = new StringBuilder();
        int unicodePoint = glyph.getUnicodePoint();
        boolean noise = glyph.isNoise();

        filename.append(glyph.getId());
        filename.append(',').append(box.getX1()).append(',').append(box.getY1()).append(',').append(unicodePoint);

        if (!noise && arguments.isGothic()) {
            filename.append(TOKEN_GOTHIC);
        }

        if (!noise && arguments.isItalic()) {
            filename.append(TOKEN_ITALIC);
        }

        if (noise) {
            filename.append(TOKEN_NOISE);
        }

        filename.append("_final.").append(ImageProcesor.FORMAT_PNG);

        File tempDirectory = FileUtils.getTempDirectory();
        return new File(tempDirectory, filename.toString());
    }

}

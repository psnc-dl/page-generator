package pl.psnc.synat.a12.generator.cutouts;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import pl.psnc.synat.a12.generator.Page;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.log4j.Logger;
import pl.psnc.synat.a12.aletheia.CLIUtils;
import pl.psnc.synat.a12.generator.BaseLinesFile;
import pl.psnc.synat.a12.generator.CommonCliArgs;

/**
 *
 */
public class CutoutsCLI {
    
    private static final Logger logger = Logger.getLogger(CutoutsCLI.class);

    public static void main(String[] args)
            throws IOException {
        try {
            CommonCliArgs cliArgs = parseArguments(args);

            if (cliArgs == null) {
                return;
            }

            CutoutsPageGenerator generator = configureGenerator(cliArgs);
            generator.init();
            generator.printStats();

            if (cliArgs.isSetPrintText()) {
                generator.getText().printText();
            }

            int height = generator.getFileHeight();
            int width = generator.getFileWidth();

            Path outputPath = Paths.get(cliArgs.output);
            File tmpDir = CLIUtils.createTmpDir(cliArgs.output);
            String tmpDirName =  tmpDir.getName();
            String boxFilePath = CLIUtils.getBoxName(outputPath, tmpDirName);
            String imageFilePath = CLIUtils.getImgName(outputPath, tmpDirName);
            
            File boxFile = generator.generateBoxFile(boxFilePath, height);
            if (cliArgs.isSetBaseLines()) {
                Map<Character, Integer> offsets = generator.getBaseLines();
                BaseLinesFile file = new BaseLinesFile(cliArgs.baseLinesFilename);
                file.write(offsets);
            }

            File pageFile = new File(imageFilePath);
            if (cliArgs.isSetGenerateImage()) {
                Page page = generator.getPage();
                page.draw(width, height);
                page.save(pageFile);
            }
            CLIUtils.saveZip(cliArgs.output, pageFile, boxFile);
            CLIUtils.deleteFiles(tmpDir, generator.getStore());
        } catch (JAXBException ex) {
            logger.error(ex);
        }
    }
    
    private static CutoutsPageGenerator configureGenerator(CommonCliArgs cliArgs) {
        String inputDirectory = CLIUtils.extractParams(cliArgs.input);
        CutoutsPageGenerator generator = new CutoutsPageGenerator(inputDirectory);
        generator.setEmRatio(cliArgs.emRatio);
        generator.setVerbose(cliArgs.isVerbose());
        generator.setSkipItalics(cliArgs.isSetItalics());
        generator.setSkipGothic(cliArgs.isSetGothic());
        generator.setSkipNoise(cliArgs.isSetSkipNoise());
        generator.setShowFontType(cliArgs.isSetFontType());
        generator.setOverlap(cliArgs.isOverlap());
        return generator;
    }


    private static CommonCliArgs parseArguments(String[] args) {
        CommonCliArgs cliArgs = new CommonCliArgs();
        JCommander commander = new JCommander(cliArgs);
        commander.setProgramName(CutoutsCLI.class.getCanonicalName());

        try {
            commander.parse(args);
        } catch (ParameterException e) {
            logger.error(e);
            commander.usage();
            return null;
        }
        return cliArgs;
    }
}

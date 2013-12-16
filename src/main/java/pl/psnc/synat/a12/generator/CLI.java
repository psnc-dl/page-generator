package pl.psnc.synat.a12.generator;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import pl.psnc.synat.a12.aletheia.CLIUtils;

/**
 *
 */
public class CLI {

    private final static Logger logger = Logger.getLogger(CLI.class);

    public static void main(String[] args)
            throws IOException, Exception {
        CommandLineArgs cliArgs = parseArguments(args);

        if (cliArgs == null) {
            return;
        }

        PageGenerator generator = configureGenerator(cliArgs);
        generator.init();
        generator.printStats();

        if (cliArgs.isSetPrintText()) {
            generator.getText().printText();
        }

        File outputPath = new File(cliArgs.output);
        File tmpDir = CLIUtils.createTmpDir(cliArgs.output);
        String tmpDirName = tmpDir.getName();
        String boxFilePath = CLIUtils.getBoxName(outputPath, tmpDirName);
        String imageFilePath = CLIUtils.getImgName(outputPath, tmpDirName);

        File boxFile = generator.generateBoxFile(boxFilePath, cliArgs.height);
        if (cliArgs.isSetBaseLines()) {
            Map<Character, Integer> offsets = generator.getBaseLines();
            BaseLinesFile file = new BaseLinesFile(cliArgs.baseLinesFilename);
            file.write(offsets);
        }

        File pageFile = new File(imageFilePath);
        if (cliArgs.isSetGenerateImage()) {
            Page page = generator.getPage();
            page.draw(cliArgs.width, cliArgs.height);
            page.save(pageFile);
        }

        CLIUtils.saveZip(cliArgs.output, pageFile, boxFile);
        CLIUtils.deleteFiles(tmpDir, generator.getStore());
    }

    private static PageGenerator configureGenerator(CommandLineArgs cliArgs) {
        String inputDirectory = CLIUtils.extractParams(cliArgs.input);
        PageGenerator generator = new PageGenerator(inputDirectory);
        generator.setEmRatio(cliArgs.emRatio);
        generator.setVerbose(cliArgs.isVerbose());
        generator.setSkipItalics(cliArgs.isSetItalics());
        generator.setSkipGothic(cliArgs.isSetGothic());
        generator.setSkipNoise(cliArgs.isSetSkipNoise());
        generator.setShowFontType(cliArgs.isSetFontType());
        generator.setOverlap(cliArgs.isOverlap());
        return generator;
    }

    private static CommandLineArgs parseArguments(String[] args) {
        CommandLineArgs cliArgs = new CommandLineArgs();
        JCommander commander = new JCommander(cliArgs);
        commander.setProgramName(CLI.class.getCanonicalName());

        try {
            commander.parse(args);
			// FIXME
            // checkParameters(cliArgs, commander);
        } catch (ParameterException e) {
            logger.error(e.getMessage());
            commander.usage();
            return null;
        }
        return cliArgs;
    }

    private static void checkParameters(CommandLineArgs commandLineArgs,
            JCommander commander) {
        if (commandLineArgs.isSetGenerateImage()
                && !Strings.isNullOrEmpty(commandLineArgs.output)) {
            throw new ParameterException("Main parameters are required (\""
                    + commander.getMainParameterDescription() + "\")");
        }
    }
}

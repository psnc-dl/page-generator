package pl.psnc.synat.a12.generator;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import pl.psnc.synat.a12.common.ZipExtractor;
import pl.psnc.synat.a12.common.ZipOutputFile;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.common.base.Strings;

/**
 *
 */
public class CLI {

	public static void main(String[] args)
            throws IOException {
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

        File boxFile = generator.generateBoxFile(cliArgs.output + "-box", cliArgs.height);
        

        if (cliArgs.isSetBaseLines()) {
            Map<Character, Integer> offsets = generator.getBaseLines();
            BaseLinesFile file = new BaseLinesFile(cliArgs.baseLinesFilename);
            file.write(offsets);
        }

        File pageFile = new File(cliArgs.output);
        if (cliArgs.isSetGenerateImage()) {
            Page page = generator.getPage();
            page.draw(cliArgs.width, cliArgs.height);
            page.save(pageFile);
        }
        
        ZipOutputFile result = new ZipOutputFile(cliArgs.output + ".zip");
        result.addFile(pageFile);
        result.addFile(boxFile);
        result.close();
        
        deleteFiles(pageFile, boxFile, generator.getStore());
    }

	private static void deleteFiles(File... files) {
		for (File file : files) {
			if (file != null) {
				FileUtils.deleteQuietly(file);
			}
		}
	}

	private static PageGenerator configureGenerator(CommandLineArgs cliArgs) {
		String inputDirectory = extractParams(cliArgs.input);
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
			System.err.println(e.getMessage());
			commander.usage();
			return null;
		}
		return cliArgs;
	}

	private static void checkParameters(CommandLineArgs commandLineArgs,
			JCommander commander) {
		if (commandLineArgs.isSetGenerateImage()
				&& !Strings.isNullOrEmpty(commandLineArgs.output))
			throw new ParameterException("Main parameters are required (\""
					+ commander.getMainParameterDescription() + "\")");
	}

	private static String extractParams(String archive) {
		File archiveFile = new File(archive);
		File output = new File(prepareOutputDirName(archiveFile));
		ZipExtractor extractor = new ZipExtractor();
		extractor.extract(archiveFile, output);
		return output.getPath();
	}

	private static String prepareOutputDirName(File archive) {
		StringBuilder sbResult = new StringBuilder(
				FileUtils.getTempDirectoryPath());
		sbResult.append(File.separator);
		sbResult.append(archive.getName().replace('.', '_'));
		return sbResult.toString();
	}
}

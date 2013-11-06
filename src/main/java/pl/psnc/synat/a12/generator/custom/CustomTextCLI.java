package pl.psnc.synat.a12.generator.custom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.psnc.synat.a12.generator.BaseLinesFile;
import pl.psnc.synat.a12.generator.ImageSelector;
import pl.psnc.synat.a12.generator.Page;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class CustomTextCLI {

    private int margin = 50;
    private CommandLineArgs args;
    private Integer width;
    private Integer height;
    private String textFull;
    private String[] textLines;


    public CustomTextCLI(CommandLineArgs cliArgs) {
        args = cliArgs;
    }


    private void run()
            throws IOException {
        LettersProvider alphabet = getAlphabet();
        CustomGenerator generator = new CustomGenerator(alphabet);

        computeMetrics();
        loadOffsets(generator);

        if (args.isSetDetectLines()) {
            loadText(args.paths.get(1));
            generator.generate(textFull, width, margin);
        } else {
            loadTextLines(args.paths.get(1));
            generator.generate(textLines, margin);
        }

        if (args.isSetGenerateBox()) {
            generator.generateBoxFile(args.boxFilename, args.height);
        }

        Page page = generator.getPage();
        page.draw(width, height);
        page.save(new File(args.paths.get(2)));
    }


    private void loadText(String path)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        StringBuffer sb = new StringBuffer();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(' ');
            }
        } finally {
            reader.close();
        }
        textFull = sb.toString();
    }


    private void loadTextLines(String path)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        List<String> lines = new LinkedList<String>();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } finally {
            reader.close();
        }
        textLines = lines.toArray(new String[0]);
    }


    private void computeMetrics() {
        if (args.isMarginSet()) {
            margin = args.margin;
        }
        width = args.width;
        height = args.height;
    }


    private LettersProvider getAlphabet()
            throws IOException {
        LettersProvider alphabet = new LettersProvider(args.paths.get(0));
        alphabet.setSkipItalic(args.isSetItalics());
        alphabet.setSkipNoise(args.isSetSkipNoise());
        alphabet.setSkipGothic(args.isSetGothic());
        alphabet.load(ImageSelector.FINAL);
        return alphabet;
    }


    private void loadOffsets(CustomGenerator generator)
            throws IOException {
        if (args.isSetBaseLines()) {
            BaseLinesFile file = new BaseLinesFile(args.baseLinesFilename);
            Map<Character, Integer> offsets = file.read();
            generator.setBaselineOffsets(offsets);
        }
    }


    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args)
            throws IOException {
        CommandLineArgs cliArgs = parseArguments(args);

        if (cliArgs != null) {
            new CustomTextCLI(cliArgs).run();
        }
    }


    private static CommandLineArgs parseArguments(String[] args) {
        CommandLineArgs cliArgs = new CommandLineArgs();
        JCommander commander = new JCommander(cliArgs);
        commander.setProgramName(CustomTextCLI.class.getCanonicalName());

        try {
            commander.parse(args);
            checkParameters(cliArgs, commander);
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            commander.usage();
            return null;
        }
        return cliArgs;
    }


    private static void checkParameters(CommandLineArgs commandLineArgs, JCommander commander) {
        if (commandLineArgs.paths.size() != 3)
            throw new ParameterException("Main parameters are required (\"" + commander.getMainParameterDescription()
                    + "\")");
    }
}

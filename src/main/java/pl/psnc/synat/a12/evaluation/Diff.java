package pl.psnc.synat.a12.evaluation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.log4j.Logger;
import pl.psnc.synat.a12.aletheia.XmlToTxt;

public class Diff {

    private final static Logger logger = Logger.getLogger(XmlToTxt.class);

    private String base;
    private String modified;
    private Levenshtein diff;


    private void loadFiles(String baseFile, String modifiedFile)
            throws IOException {
        base = readFileAsString(new File(baseFile));
        modified = readFileAsString(new File(modifiedFile));
        diff = new Levenshtein(base, modified);
    }


    private static String readFileAsString(File file)
            throws java.io.IOException {
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

        try {
            input.read(buffer);
        } finally {
            input.close();
        }
        return new String(buffer, Charset.forName("UTF-8"));
    }


    private void compute() {
        diff.compute();
    }


    private void printDistance() {
        logger.info("distance:\t" + diff.getDistance());
    }


    private void printDiff(DiffArgs diffArgs) {
        VT100ColorFormatter formatter = new VT100ColorFormatter();
        StringBuilder message = new StringBuilder();

        if (diffArgs.isDetailsSet()) {
            formatter.emphasize(diffArgs.isSubSet(), diffArgs.isInsSet(), diffArgs.isDelSet());
        }

        diff.getOperators(formatter);

        if (diffArgs.isDetailsSet()) {
            message.append(diff.getStatistics().getDistribution(diffArgs.isSubSet(), diffArgs.isInsSet(),
                diffArgs.isDelSet()));
        }

        if (diffArgs.isVisualSet()) {
            message.append(formatter.getOperators().toString());
        }

        if (diffArgs.isStatsSet()) {
            if (diffArgs.isOnelineSet()) {
                message.append(diff.getStatistics().asOneliner());
            } else {
                message.append(diff.getStatistics().toString());
            }
        } else {
            printDistance();
        }

        logger.info(message.toString());
    }


    private static void checkParameters(DiffArgs commandLineArgs, JCommander commander) {
        if (commandLineArgs.paths.size() != 2)
            throw new ParameterException("Main parameters are required (\"" + commander.getMainParameterDescription()
                    + "\")");
    }


    public static void main(String[] args)
            throws IOException {
        DiffArgs diffArgs = new DiffArgs();
        JCommander commander = new JCommander(diffArgs);
        commander.setProgramName(Diff.class.getCanonicalName());

        try {
            commander.parse(args);
            checkParameters(diffArgs, commander);
        } catch (ParameterException e) {
            logger.error(e.getMessage());
            commander.usage();
            return;
        }

        Diff diff = new Diff();
        diff.loadFiles(diffArgs.paths.get(0), diffArgs.paths.get(1));
        diff.compute();
        diff.printDiff(diffArgs);
    }
}

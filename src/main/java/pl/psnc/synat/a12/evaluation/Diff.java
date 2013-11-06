package pl.psnc.synat.a12.evaluation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class Diff {

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
        System.out.println("distance:\t" + diff.getDistance());
    }


    private void printDiff(DiffArgs diffArgs) {
        VT100ColorFormatter formatter = new VT100ColorFormatter();

        if (diffArgs.isDetailsSet()) {
            formatter.emphasize(diffArgs.isSubSet(), diffArgs.isInsSet(), diffArgs.isDelSet());
        }

        diff.getOperators(formatter);

        if (diffArgs.isDetailsSet()) {
            System.out.print(diff.getStatistics().getDistribution(diffArgs.isSubSet(), diffArgs.isInsSet(),
                diffArgs.isDelSet()));
        }

        if (diffArgs.isVisualSet()) {
            System.out.print(formatter.getOperators().toString());
        }

        if (diffArgs.isStatsSet()) {
            if (diffArgs.isOnelineSet()) {
                System.out.print(diff.getStatistics().asOneliner());
            } else {
                System.out.print(diff.getStatistics().toString());
            }
        } else {
            printDistance();
        }

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
            System.err.println(e.getMessage());
            commander.usage();
            return;
        }

        Diff diff = new Diff();
        diff.loadFiles(diffArgs.paths.get(0), diffArgs.paths.get(1));
        diff.compute();
        diff.printDiff(diffArgs);
    }
}

package pl.psnc.synat.a12.aletheia;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import pl.psnc.synat.a12.common.ZipExtractor;
import pl.psnc.synat.a12.common.ZipOutputFile;

public final class CLIUtils {
    
    private final static Logger logger = Logger.getLogger(CLIUtils.class);

    public interface Command {

        public String name();

        public boolean isHelp();
    }


    private CLIUtils() {
        // empty
    }


    public static void handleParameterException(JCommander engine, ParameterException e) {
        StringBuilder sb = new StringBuilder(e.getMessage());
        String commandName = engine.getParsedCommand();

        if (commandName != null) {
            String desc = engine.getCommandDescription(commandName);
            sb.append(" (\"").append(desc).append("\")");
        }
        logger.error(sb.toString());
        engine.usage();
    }


    public static JCommander createEngine(String name, Command main, Command... commands) {
        JCommander engine = new JCommander(main);

        engine.setProgramName(name);

        for (Command command : commands) {
            engine.addCommand(command.name(), command);
        }
        return engine;
    }


    public static void checkHelp(JCommander engine, Command arguments) {
        if (arguments.isHelp()) {
            engine.usage();
            System.exit(0);
        }
    }
    
     public static void deleteFiles(File... files) throws IOException {
        for (File file : files) {
            if (file != null) {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    FileUtils.deleteQuietly(file);
                }
            }
        }
    }
     
    public static String extractParams(String archive) {
        File archiveFile = new File(archive);
        File output = new File(prepareOutputDirName(archiveFile));
        ZipExtractor extractor = new ZipExtractor();
        extractor.extract(archiveFile, output);
        return output.getPath();
    }


    private static String prepareOutputDirName(File archive) {
        StringBuilder sbResult = new StringBuilder(FileUtils.getTempDirectoryPath());
        sbResult.append(File.separator);
        sbResult.append(archive.getName().replace('.', '_'));
        return sbResult.toString();
    }
    
    
    public static File createTmpDir(String output){
        String outputDir = output + "_results";
        File tmpDir = new File(outputDir);
        tmpDir.mkdir();
        return tmpDir;
    }
    
    public static String getImgName(File outputPath, String tmpDirName){
        return outputPath.getParent() + File.separator + tmpDirName + File.separator + outputPath.getName();
    }
    
    public static String getBoxName(File outputPath, String tmpDirName){
        return getImgName(outputPath, tmpDirName) + "-box";
    }
    
    public static void saveZip(String output, File pageFile, File boxFile) throws IOException {
        ZipOutputFile result = new ZipOutputFile(output);
        if(pageFile.exists())
            result.addFile(pageFile);
        if(boxFile.exists())
            result.addFile(boxFile);
        result.close();
    }
}

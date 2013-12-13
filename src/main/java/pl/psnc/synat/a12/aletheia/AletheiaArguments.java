package pl.psnc.synat.a12.aletheia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.psnc.synat.a12.aletheia.CLIUtils.Command;

import com.beust.jcommander.Parameter;
import org.apache.log4j.Logger;

public class AletheiaArguments implements Command {
    
    private static final Logger logger = Logger.getLogger(AletheiaArguments.class);

    @Parameter(names = "--xml", description = "file name of a page xml input", required = true)
    protected String inputXmlFilename;

    @Parameter(names = "--tabu-types", description = "list of TextRegion types to omit during processing")
    protected List<String> tabuTypes = new ArrayList<String>();

    @Parameter(names = "--help", description = "print this help")
    private Boolean help;

    private List<String> noiseWords = new ArrayList<String>();

    @Parameter(names = "--tabu", description = "file name of a text file containing list of noise word's ids")
    private String noiseFilename;


    public String getInputXmlFilename() {
        return inputXmlFilename;
    }


    public String getNoiseFilename() {
        return noiseFilename;
    }


    public List<String> getTabuTypes() {
        return tabuTypes;
    }


    public boolean isHelp() {
        return help == null ? false : help;
    }


    public void readNoiseList()
            throws IOException {
        if (noiseFilename == null)
            return;

        File tabu = new File(noiseFilename);

        if (!tabu.isFile()) {
            logger.error("Missing tabu-noise file " + noiseFilename);
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(tabu));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                noiseWords.add(line);
            }
        } finally {
            reader.close();
        }
    }


    public String name() {
        // TODO Auto-generated method stub
        return null;
    }


    public List<String> getNoiseWords() {
        return noiseWords;
    }

}

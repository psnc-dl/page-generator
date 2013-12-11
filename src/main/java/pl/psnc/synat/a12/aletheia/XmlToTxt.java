package pl.psnc.synat.a12.aletheia;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.log4j.Logger;

public class XmlToTxt {

    private final static Logger logger = Logger.getLogger(XmlToTxt.class);
   
    private AletheiaArguments arguments = new AletheiaArguments();

    private void run()
            throws JAXBException, IOException {
        arguments.readNoiseList();

        PageXmlReader reader = new PageXmlReader(arguments.getInputXmlFilename());
        reader.read(arguments.getNoiseWords(), arguments.getTabuTypes());
        print(reader);
    }


    private void print(PageXmlReader reader) {
        StringBuilder sb = new StringBuilder();

        printLines(reader.getHeader(), sb);

        for (String[][] region : reader.getOrderedRegions()) {
            printLines(region, sb);
        }
        for (String[][] region : reader.getUnOrderedRegions()) {
            printLines(region, sb);
        }
        printLines(reader.getSignature(), sb);
        logger.info(sb.toString());
    }


    private void printLines(String[][] lines, StringBuilder sb) {
        if (lines == null) {
            return;
        }

        for (String[] words : lines) {
            boolean first = true;

            for (String word : words) {
                if (!first) {
                    sb.append(' ');
                }
                sb.append(word);
                first = false;
            }
            sb.append('\n');
        }
    }


    public static void main(String[] args)
            throws JAXBException, IOException {
        XmlToTxt proc = new XmlToTxt();
        JCommander engine = CLIUtils.createEngine(XmlToTxt.class.getName(), proc.arguments);

        try {
            engine.parse(args);
            CLIUtils.checkHelp(engine, proc.arguments);
        } catch (ParameterException e) {
            CLIUtils.handleParameterException(engine, e);
            System.exit(1);
        }
        proc.run();
    }
}

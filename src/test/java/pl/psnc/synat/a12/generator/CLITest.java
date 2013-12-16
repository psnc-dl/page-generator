package pl.psnc.synat.a12.generator;

import java.io.File;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static pl.psnc.synat.a12.aletheia.CutterTest.getInputDataPath;
import pl.psnc.synat.a12.common.ZipExtractor;

public class CLITest {

    @Rule
    public TemporaryFolder outputTmpDir = new TemporaryFolder();
    
    

    @Test
    public void shouldCreateTrainingImageAndBoxFile() throws Exception {

        //given
        String outputFileName = "results.zip";
        File outputFile = outputTmpDir.newFile(outputFileName);
        String inputZip = getInputDataPath("00426982-results.zip");

        String[] args = {"--input", inputZip,
            "-w", "1000",
            "-h", "1000",
            "--output", outputFile.getAbsolutePath(),
        };
        
        //when
        CLI.main(args);
        
        //then
        ZipExtractor extractor = new ZipExtractor();
        File targetOfExtractionFolder =outputTmpDir.newFolder("CLIresult"); 
        extractor.extract(outputFile, targetOfExtractionFolder);
        
        File[] filesInResultArchive = targetOfExtractionFolder.listFiles();
        assertThat(filesInResultArchive.length, is(2));
        
        String expectedResultFileName = outputFileName;
        assertThat(filesInResultArchive[0].getName(), is(expectedResultFileName+".box"));
        assertThat(filesInResultArchive[1].getName(), is(expectedResultFileName+".png"));
    }

}

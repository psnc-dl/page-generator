package pl.psnc.synat.a12.aletheia;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import pl.psnc.synat.a12.common.ZipExtractor;

public class CutterTest {

    public static final int NUMBER_OF_GLYPHS_IN_TEST_DATA = 1063;

    @Rule
    public TemporaryFolder outputTmpDir = new TemporaryFolder();

    String inputImage;
    String inputXml;


    @Before
    public void setupFilePaths() {
        inputImage = getInputDataPath("00426982.png");
        inputXml = getInputDataPath("00426982.xml");
    }

    @Test
    public void shouldCutOriginalImageIntoSeparateImages() throws IOException, JAXBException {
        //given
        File outputFile = outputTmpDir.newFile("results");

        String[] args = {"--image", inputImage,
            "--output", outputFile.getAbsolutePath(),
            "--xml", inputXml
        };

        //when
        Cutter.main(args);

        //then
        ZipExtractor extractor = new ZipExtractor();
        File destFolder = outputTmpDir.newFolder("result");
        extractor.extract(outputFile, destFolder);
        int numberOfResultFiles = destFolder.list().length;
        
        assertThat(numberOfResultFiles, is(NUMBER_OF_GLYPHS_IN_TEST_DATA));
    }
    
    public static String getInputDataPath(String outputFileName) {
        return new StringBuilder()
                .append("src").append(File.separator)
                .append("test").append(File.separator)
                .append("resources").append(File.separator)
                .append("cutter").append(File.separator)
                .append(outputFileName)
                .toString();
    }

}

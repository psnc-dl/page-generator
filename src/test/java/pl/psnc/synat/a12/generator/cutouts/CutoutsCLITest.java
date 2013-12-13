package pl.psnc.synat.a12.generator.cutouts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;
import static org.hamcrest.core.Is.is;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.rules.TemporaryFolder;
import pl.psnc.synat.a12.common.ZipExtractor;

public class CutoutsCLITest {
    
    public static final String TEST_DATA_ARCHIVE_NAME = "input.zip";
    
    @Rule
    public TemporaryFolder outputTmpDir = new TemporaryFolder();

    @Test
    public void shouldConvertCutoutsToBoxAndImage() throws Exception {
        //given
        String outputFileName = "result";
        String inputData = getInputDataPath(TEST_DATA_ARCHIVE_NAME);
        File outputFile = outputTmpDir.newFile(outputFileName);

        String[] args = {
            "--input", inputData,
            "--output", outputFile.getAbsolutePath()
        };

        //when
        CutoutsCLI.main(args);

        //then
        File extractedResultArchiveFolder = outputTmpDir.newFolder("CUTOUTS_result");
        extractArchive(outputFile, extractedResultArchiveFolder);

        assertEquals(extractedResultArchiveFolder.listFiles().length, 2);
        checkResultCleanedImage(new File(extractedResultArchiveFolder, outputFileName));
        checkResultBoxFile(new File(extractedResultArchiveFolder, outputFileName + "-box"));

    }

    public final static void extractArchive(File zipArchive, File destFolder) {
        ZipExtractor extractor = new ZipExtractor();
        extractor.extract(zipArchive, destFolder);
    }

    private void checkResultBoxFile(File boxFile) {
        assertThat(boxFile.exists(), is(true));
        File expectedBox = new File(getInputDataPath("test_output-box"));
        compareFilesMD5(boxFile, expectedBox);
    }

    private void checkResultCleanedImage(File cleanedImageFile) {
        assertThat(cleanedImageFile.exists(), is(true));
        File expectedCleanedImage = new File(getInputDataPath("test_output"));
        compareFilesMD5(cleanedImageFile, expectedCleanedImage);
    }

    private String getInputDataPath(String outputFileName) {
        String expectedFilePath = new StringBuilder()
                .append("src").append(File.separator)
                .append("test").append(File.separator)
                .append("resources").append(File.separator)
                .append("cutouts").append(File.separator)
                .append(outputFileName).toString();
        return expectedFilePath;
    }

    private void compareFilesMD5(File given, File expected) {
        try {
            String boxMD5 = DigestUtils.md5Hex(new FileInputStream(given));
            String expectedBoxMD5 = DigestUtils.md5Hex(new FileInputStream(expected));
            assertEquals(given+" md5 does not much "+expected,boxMD5, expectedBoxMD5);
        } catch (IOException e) {
            fail("Something went wrong during md5 comparision");
        }
    }

}

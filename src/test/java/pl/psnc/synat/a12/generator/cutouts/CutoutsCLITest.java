package pl.psnc.synat.a12.generator.cutouts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.rules.TemporaryFolder;
import pl.psnc.synat.a12.common.ZipExtractor;

public class CutoutsCLITest {

    @Rule
    public TemporaryFolder outputTmpDir = new TemporaryFolder();
    
    /**
     * Test of main method, of class CutoutsCLI. Checks md5 checksums of files created by CutoutsCLI.
     * @throws java.lang.Exception
     */
    @Test
    public void shouldConvertCutoutsToBoxAndImage() throws Exception {
        String outputFileName = "test_output";
        Paths.get("src\\test\\resources\\");
        File expectedBoxFile = new File("src\\test\\resources\\" + outputFileName+ "-box");
        File expectedImgFile = new File("src\\test\\resources\\" + outputFileName);

        String inputDir ="src\\test\\resources\\input.zip";
        File outputFile = outputTmpDir.newFile(outputFileName);
        String[] args = {"--input", inputDir, "--output", outputFile.getAbsolutePath()};

        CutoutsCLI.main(args);
        
        ZipExtractor extractor = new ZipExtractor();
        File extractedFile =outputTmpDir.newFolder("result"); 
        extractor.extract(outputFile, extractedFile);
        
        File img = checkIfImgExist(extractedFile, outputFileName);
        File box = checkIfBoxExist(extractedFile,outputFileName);
        
        //check if archive contains only 2 files
        assertEquals(extractedFile.listFiles().length, 2);
        
        //compare md5 for image
        compareFilesMD5(img, expectedImgFile);
        //compare md5 for box
        compareFilesMD5(box, expectedBoxFile);
    }
    
    private void compareFilesMD5(File given, File expected) throws NoSuchAlgorithmException, IOException{
        String boxMD5 = DigestUtils.md5Hex(new FileInputStream(given));
        String expectedBoxMD5 = DigestUtils.md5Hex(new FileInputStream(expected));
        assertEquals(boxMD5, expectedBoxMD5);
    }
    
    /**
     * Checks if image file exists int the archive. If true returns it.
     */
    private File checkIfImgExist(File file, String outputName){
        File img = getFile(file, outputName);
        if(img == null){
            fail(String.format("Archive does not contain img file"));
        }
        return img;
    }
    
    /**
     * Checks if image file exists int the archive. If true returns it.
     */
    private File checkIfBoxExist(File file, String outputName){
        File box = getFile(file, outputName+"-box");
        if(box == null){
            fail(String.format("Archive does not contain box file"));
        }
        return box;
    }
    
    private File getFile(File zipFile, String name) {
        File result = null;
        for (File f : zipFile.listFiles()) {
            if (f.getName().equals(name)) {
                result = f;
            }
        }
        return result;
    }
} 
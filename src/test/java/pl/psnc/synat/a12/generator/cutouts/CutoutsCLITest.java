package pl.psnc.synat.a12.generator.cutouts;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.psnc.synat.a12.common.ZipExtractor;

public class CutoutsCLITest {
    
    public CutoutsCLITest() {
    }

    /**
     * Test of main method, of class CutoutsCLI. Checks md5 checksums of files created by CutoutsCLI.
     * @throws java.lang.Exception
     */
    @Test
    public void testMain() throws Exception {
        
        String outputFileName = "test_output";
        String expectedImgMD5 = DigestUtils.md5Hex(new FileInputStream(new File("src/test/resources/" + outputFileName)));
        String expectedBoxMD5 = DigestUtils.md5Hex(new FileInputStream(new File("src/test/resources/" + outputFileName+ "-box")));
        
        String inputDir ="src/test/resources/input.zip";
        
        String outputDir = System.getProperty("java.io.tmpdir") + "/" + outputFileName;
        String[] args = {"--input", inputDir, "--output", outputDir};
        
        
        CutoutsCLI.main(args);
        
        File result = new File(outputDir);
        if(!result.exists()) {
            fail("Output file does not exist");
        }
        ZipExtractor extractor = new ZipExtractor();
        File extractedFile = new File(System.getProperty("java.io.tmpdir")+"/result");
        extractor.extract(result, extractedFile);
        File[] files = extractedFile.listFiles();
        
        assertEquals(files.length,2);

        File img=null;
        File box=null;
        for(File f: files){
            if(f.getName().equals(outputFileName)){
                img = f;
            }
            if(f.getName().equals(outputFileName+"-box")){
                box = f;
            }
        }
        
        String imgMD5 = DigestUtils.md5Hex(new FileInputStream(img));
        assertEquals(imgMD5, expectedImgMD5);
        String boxMD5 = DigestUtils.md5Hex(new FileInputStream(box));
        assertEquals(boxMD5, expectedBoxMD5);
    }
}

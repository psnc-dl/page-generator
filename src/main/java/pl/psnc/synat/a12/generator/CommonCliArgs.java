
package pl.psnc.synat.a12.generator;

import pl.psnc.synat.a12.common.DoubleConverter;
import com.beust.jcommander.Parameter;

public class CommonCliArgs {
    @Parameter(names = { "-v", "--verbose" }, description = "turn on verbose mode")
    private Boolean verbose;

    @Parameter(names = { "-t", "--text" }, description = "generate and output text to stdio")
    private Boolean printText;

    @Parameter(names = { "-b", "--box" }, description = "generate and output \".box\" data to given filename")
    public String boxFilename;

    @Parameter(names = { "--baselines" }, description = "generate and output baselines offsets to given filename")
    public String baseLinesFilename;

    @Parameter(names = { "--no-image" }, description = "do not generate output image")
    private Boolean noImage;

    @Parameter(names = { "--font-type" }, description = "output font type in output box file")
    private Boolean fontType;

    @Parameter(names = { "--skip-noise" }, description = "skip noised letters in output")
    private Boolean skipNoise;

    @Parameter(names = { "--skip-gothic" }, description = "skip gothic letters in output")
    private Boolean skipGothic;

    @Parameter(names = { "--skip-italics" }, description = "skip italic letters in output")
    private Boolean skipItalics;

    @Parameter(names = { "--input"}, required = true, description = "Archive with boxes files")
    public String input;

    @Parameter(names = { "--output"}, required = true, description = "Archive with box file and image")
    public String output;
   
    @Parameter(names = { "--em-ratio" }, converter = DoubleConverter.class,
            description = "set space to em size ratio, used for spaces detection")
    public Double emRatio;

    @Parameter(names = { "--overlap" }, description = "allows letter bounding boxes to overlap (default no overlaping)")
    private boolean overlap = false;


    public boolean isSetGenerateImage() {
        return noImage == null;
    }


    public boolean isVerbose() {
        return verbose != null;
    }


    public boolean isSetPrintText() {
        return printText != null;
    }


    public boolean isSetBaseLines() {
        return baseLinesFilename != null;
    }


    public boolean isSetGenerateBox() {
        return boxFilename != null;
    }


    public boolean isSetFontType() {
        return fontType != null;
    }


    public boolean isSetSkipNoise() {
        return skipNoise != null;
    }


    public boolean isSetItalics() {
        return skipItalics != null;
    }


    public boolean isSetGothic() {
        return skipGothic != null;
    }


    public boolean isOverlap() {
        return overlap;
    }
}

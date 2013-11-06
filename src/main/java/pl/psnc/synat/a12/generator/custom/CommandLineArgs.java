package pl.psnc.synat.a12.generator.custom;

import java.util.List;

import pl.psnc.synat.a12.generator.DoubleConverter;

import com.beust.jcommander.Parameter;

public class CommandLineArgs {

    @Parameter(names = { "-v", "--verbose" }, description = "turn on verbose mode")
    private Boolean verbose;

    @Parameter(names = { "-b", "--box" }, description = "generate and output \".box\" data to given filename")
    public String boxFilename;

    @Parameter(names = { "--baselines" }, description = "specify baselines file")
    public String baseLinesFilename;

    @Parameter(names = { "--font-type" }, description = "output font type in output box file")
    private Boolean fontType;

    @Parameter(names = { "--skip-noise" }, description = "skip noised letters in output")
    private Boolean skipNoise;

    @Parameter(names = { "--skip-italics" }, description = "skip italic letters in output")
    private Boolean skipItalics;

    @Parameter(names = { "--skip-gothic" }, description = "skip gothic letters in output")
    private Boolean skipGothic;

    @Parameter(names = { "--detect-lines" }, description = "detect linebreaks instead of reading input literally")
    private Boolean detectLines;

    @Parameter(required = true, description = "Args: <box_dir_path> <custom_text_path> <image_filename>")
    public List<String> paths;

    @Parameter(names = { "-w", "--width" }, required = true, description = "set width of generated output image")
    public Integer width;

    @Parameter(names = { "-h", "--height" }, required = true, description = "set height of generated output image")
    public Integer height;

    @Parameter(names = { "-m", "--margin" }, description = "set margin size (same for top, left and right)")
    public Integer margin;

    @Parameter(names = { "--interline-ratio" }, converter = DoubleConverter.class,
            description = "set interline to em size ratio")
    public Double interlineRatio;

    @Parameter(names = { "--space-ratio" }, converter = DoubleConverter.class,
            description = "set space to em size ratio")
    public Double spaceRatio;

    @Parameter(names = { "--interletter-ratio" }, converter = DoubleConverter.class,
            description = "set interletter to em size ratio")
    public Double interletterRatio;


    public boolean isVerbose() {
        return verbose != null;
    }


    public boolean isSetBaseLines() {
        return baseLinesFilename != null;
    }


    public boolean isSetGenerateBox() {
        return boxFilename != null;
    }


    public boolean isSetDetectLines() {
        return detectLines != null;
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


    public boolean isMarginSet() {
        return margin != null;
    }
}

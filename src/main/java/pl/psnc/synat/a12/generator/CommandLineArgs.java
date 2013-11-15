package pl.psnc.synat.a12.generator;

import com.beust.jcommander.Parameter;

public class CommandLineArgs extends CommonCliArgs {

    @Parameter(names = { "-w", "--width" }, required = true, description = "set width of generated output image")
    public Integer width;

    @Parameter(names = { "-h", "--height" }, required = true, description = "set height of generated output image")
    public Integer height;
}

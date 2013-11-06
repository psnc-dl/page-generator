package pl.psnc.synat.a12.evaluation;

import java.util.List;

import com.beust.jcommander.Parameter;

public class DiffArgs {

    @Parameter(names = { "-v", "--visual" }, description = "print visual diff on std output")
    private Boolean visual;

    @Parameter(names = { "--stats" }, description = "print diff statistics on std output")
    private Boolean stats;

    @Parameter(names = { "--ins" }, description = "print insertions distribution on std output")
    private Boolean insertions;

    @Parameter(names = { "--sub" }, description = "print substitutions distribution on std output")
    private Boolean substitutions;

    @Parameter(names = { "--del" }, description = "print deletions distribution on std output")
    private Boolean deletions;

    @Parameter(names = { "--stats-one-line" }, description = "print diff statistics on std output as oneliner")
    private Boolean oneline;

    @Parameter(required = true, description = "Args: <base_filename> <modified_filename>")
    public List<String> paths;


    public boolean isVisualSet() {
        return visual != null;
    }


    public boolean isStatsSet() {
        return stats != null || oneline != null;
    }


    public boolean isOnelineSet() {
        return oneline != null;
    }


    public boolean isInsSet() {
        return insertions != null;
    }


    public boolean isDelSet() {
        return deletions != null;
    }


    public boolean isSubSet() {
        return substitutions != null;
    }


    public boolean isDetailsSet() {
        return insertions != null || deletions != null || substitutions != null;
    }
}

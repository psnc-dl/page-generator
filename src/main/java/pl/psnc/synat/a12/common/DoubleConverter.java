package pl.psnc.synat.a12.common;

import com.beust.jcommander.IStringConverter;

public class DoubleConverter implements IStringConverter<Double> {

    public Double convert(String arg0) {
        return Double.valueOf(arg0);
    }
}

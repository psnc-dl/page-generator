package pl.psnc.synat.cutter.image;

import java.awt.Polygon;
import java.awt.Shape;

public class OutlinePolygon extends Polygon implements Shape {

    private static final long serialVersionUID = -4014464863888898224L;


    public OutlinePolygon(int[] xnormalized, int[] ynormalized, int length) {
        super(xnormalized, ynormalized, length);
    }


    @Override
    public boolean contains(double x, double y) {
        return true;
    }

}

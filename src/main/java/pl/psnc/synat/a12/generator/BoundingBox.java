package pl.psnc.synat.a12.generator;

public class BoundingBox {

    protected int y1;
    protected int y2;
    protected int x1;
    protected int x2;


    public BoundingBox() {
        // empty
    }


    public BoundingBox(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    public BoundingBox(BoundingBox bounding) {
        x1 = bounding.x1;
        y1 = bounding.y1;
        x2 = bounding.x2;
        y2 = bounding.y2;
    }


    public BoundingBox(final int[] xcoordins, final int[] ycoordins) {
        if (xcoordins.length != ycoordins.length) {
            throw new IllegalArgumentException("Number of coordinates for each axis must be equal");
        }
        if (xcoordins.length < 3) {
            throw new IllegalArgumentException("There should be at least three point given");
        }

        x1 = StatUtils.minimum(xcoordins);
        y1 = StatUtils.minimum(ycoordins);
        x2 = StatUtils.maximum(xcoordins);
        y2 = StatUtils.maximum(ycoordins);
    }


    public int getX1() {
        return x1;
    }


    public int getY1() {
        return y1;
    }


    public int getY2() {
        return y2;
    }


    public int getX2() {
        return x2;
    }


    public int distanceX(LetterBox box) {
        final int left = Math.abs(getX1() - box.getX1());
        final int right = Math.abs(getX2() - box.getX2());

        return Math.min(left, right);
    }


    public int distanceY(LetterBox box) {
        final int top = Math.abs(getY1() - box.getY1());
        final int bottom = Math.abs(getY2() - box.getY2());

        return Math.min(top, bottom);
    }


    public boolean overlapY(LetterBox box) {
        return !(box.getY1() > getY2() || box.getY2() < getY1());
    }


    public boolean overlapX(LetterBox box) {
        return !(getX2() < box.getX1() || box.getX2() < getX1());
    }


    public void moveX(int offset) {
        x1 += offset;
        x2 += offset;
    }


    public void moveY(int offset) {
        y1 += offset;
        y2 += offset;
    }


    public int getHeight() {
        return getY2() - getY1();
    }


    public int getWidth() {
        return getX2() - getX1();
    }


    public void trim(int... coordins) {
        if (coordins.length == 4) {
            x1 += coordins[0];
            y1 += coordins[1];
            x2 = coordins[2];
            y2 = coordins[3];
        }
    }

}
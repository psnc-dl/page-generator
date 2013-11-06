package pl.psnc.synat.a12.evaluation;

public class VT100ColorFormatter implements OperatorsFotmatter {

    private static final String NORMAL = "\033[0;;m";
    private static final String GREEN_ON_BLACK = "\033[0;32;40m";
    private static final String RED_ON_BLACK = "\033[0;31;40m";
    private static final String WHITE_ON_BLUE = "\033[0;;44m";
    private static final String WHITE_ON_RED = "\033[0;;41m";

    private StringBuilder sb = new StringBuilder();
    private boolean emphSub = true;
    private boolean emphIns = true;
    private boolean emphDel = true;


    public StringBuilder getOperators() {
        return sb;
    }


    public OperatorsFotmatter deletion(char c) {
        color(sb, c, WHITE_ON_RED, emphDel);
        return this;
    }


    public OperatorsFotmatter insertion(char c) {
        color(sb, c, GREEN_ON_BLACK, emphIns);
        return this;
    }


    public OperatorsFotmatter substitution(char old, char c) {
        color(sb, old, WHITE_ON_BLUE, emphSub);
        color(sb, c, RED_ON_BLACK, emphSub);
        return this;
    }


    public OperatorsFotmatter same(char c) {
        sb.insert(0, c);
        return this;
    }


    private void color(StringBuilder sb, char c, String color, boolean emph) {
        if (emph) {
            StringBuilder tmp = new StringBuilder();
            tmp.append(color);
            tmp.append(c);
            tmp.append(NORMAL);
            sb.insert(0, tmp);
        } else {
            sb.insert(0, c);
        }
    }


    public void emphasize(boolean sub, boolean ins, boolean del) {
        emphSub = sub;
        emphIns = ins;
        emphDel = del;
    }
}

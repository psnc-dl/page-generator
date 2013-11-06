package pl.psnc.synat.a12.evaluation;

public class Levenshtein {

    private static final int DELETION_PENALTY = 1;
    private static final int INSERTION_PENALTY = 1;
    private static final int SUBSTITUTION_PENALTY = 1;

    private String base;
    private String mod;
    private int[][] space;
    private DiffStatisticsData data = new DiffStatisticsData();


    /**
     * Computes base to mod transition operators.
     * 
     * @param base
     * @param mod
     */
    public Levenshtein(String base, String mod) {
        this.base = base;
        this.mod = mod;

        space = new int[base.length() + 1][];

        for (int i = 0; i < space.length; i++) {
            space[i] = new int[mod.length() + 1];
        }

        // empty mod string requires deletions
        for (int i = 1; i < space.length; i++) {
            space[i][0] = i * DELETION_PENALTY;
        }

        // empty base string requires insertions
        for (int i = 1; i < space[0].length; i++) {
            space[0][i] = i * INSERTION_PENALTY;
        }
    }


    public void compute() {
        for (int i = 1; i < space.length; i++) {
            for (int j = 1; j < space[i].length; j++) {
                if (base.charAt(i - 1) == mod.charAt(j - 1)) {
                    space[i][j] = space[i - 1][j - 1];
                } else {
                    int deletion = space[i - 1][j] + DELETION_PENALTY;
                    int insertion = space[i][j - 1] + INSERTION_PENALTY;
                    int change = space[i - 1][j - 1] + SUBSTITUTION_PENALTY;

                    int min = Math.min(insertion, deletion);
                    space[i][j] = Math.min(min, change);
                }
            }
        }
    }


    public int getDistance() {
        return space[base.length()][mod.length()];
    }


    public DiffStatisticsData getStatistics() {
        return data;
    }


    public OperatorsFotmatter getOperators(OperatorsFotmatter formatter) {
        int i = base.length();
        int j = mod.length();

        while (i > 0 && j > 0) {

            if (space[i][j] == space[i - 1][j] + 1) {
                formatter.deletion(base.charAt(i - 1));
                data.addDeletion(base.charAt(i - 1));
                i--;
            } else if (space[i][j] == space[i][j - 1] + 1) {
                formatter.insertion(mod.charAt(j - 1));
                data.addInsertion(mod.charAt(j - 1));
                j--;
            } else if (space[i][j] == space[i - 1][j - 1] + 1) {
                formatter.substitution(base.charAt(i - 1), mod.charAt(j - 1));
                data.addSubstitution(base.charAt(i - 1), mod.charAt(j - 1));
                i--;
                j--;
            } else {
                formatter.same(base.charAt(i - 1));
                data.addSame(base.charAt(i - 1));
                i--;
                j--;
            }
        }

        while (i > 0) {
            formatter.deletion(base.charAt(i - 1));
            data.deletionsCount++;
            i--;
        }

        while (j > 0) {
            formatter.insertion(mod.charAt(j - 1));
            data.insertionsCount++;
            j--;
        }

        return formatter;
    }
}

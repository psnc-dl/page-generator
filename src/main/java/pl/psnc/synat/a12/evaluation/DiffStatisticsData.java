package pl.psnc.synat.a12.evaluation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DiffStatisticsData {

    public int substitutionsCount;
    public int deletionsCount;
    public int insertionsCount;
    public int sameCount;
    public Map<String, Integer> substitutions = new HashMap<String, Integer>();
    public Map<Character, Integer> deletions = new HashMap<Character, Integer>();
    public Map<Character, Integer> insertions = new HashMap<Character, Integer>();


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int sum = sameCount + substitutionsCount + deletionsCount + insertionsCount;
        sb.append("all:\t").append(sum).append('\n');
        sb.append("correct:\t").append(sameCount).append('\n');
        sb.append("substitutions:\t").append(substitutionsCount).append('\n');
        sb.append("deletions:\t").append(deletionsCount).append('\n');
        sb.append("insertions:\t").append(insertionsCount).append('\n');

        return sb.toString();
    }


    public String asOneliner() {
        StringBuilder sb = new StringBuilder();
        int sum = sameCount + substitutionsCount + insertionsCount;

        sb.append(sum).append('\t');
        sb.append(sameCount).append('\t');
        sb.append(substitutionsCount).append('\t');
        sb.append(deletionsCount).append('\t');
        sb.append(insertionsCount).append('\n');

        return sb.toString();
    }


    public String getDistribution(boolean sub, boolean ins, boolean del) {
        StringBuilder sb = new StringBuilder();

        if (sub) {
            print("Substitutions:", substitutions, sb);
        }
        if (ins) {
            print("Insertions:", insertions, sb);
        }
        if (del) {
            print("Deletions:", deletions, sb);
        }
        return sb.toString();
    }


    void addDeletion(char c) {
        deletionsCount++;
        put(deletions, c);
    }


    void addInsertion(char c) {
        insertionsCount++;
        put(insertions, c);
    }


    void addSubstitution(char c1, char c2) {
        substitutionsCount++;
        StringBuilder sb = new StringBuilder();
        sb.append(c1);
        sb.append('/');
        sb.append(c2);
        put(substitutions, sb.toString());
    }


    void addSame(char c) {
        sameCount++;
    }


    private static <T> void put(Map<T, Integer> counts, T c) {
        Integer value = counts.get(c);

        if (value == null) {
            value = 1;
        } else {
            value++;
        }
        counts.put(c, value);
    }


    private <T> void print(String title, Map<T, Integer> distr, StringBuilder sb) {
        sb.append(title).append('\n');

        for (Entry<T, Integer> d : distr.entrySet()) {
            sb.append(d.getKey());
            sb.append(" -> ");
            sb.append(d.getValue());
            sb.append('\n');
        }
    }
}

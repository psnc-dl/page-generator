package pl.psnc.synat.a12.generator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public final class StatUtils {
    
    private final static Logger logger = Logger.getLogger(StatUtils.class);

    private StatUtils() {
        // empty
    }


    public static int median(List<Integer> list) {
        int median = 0;
        Collections.sort(list);

        if (list.size() > 0) {
            int mid = list.size() / 2;
            median = list.get(mid);
        }
        return median;
    }


    public static double avg(List<Integer> list) {
        int sum = 0;

        for (Integer i : list) {
            sum += i;
        }
        return (double) sum / list.size();
    }


    public static int maximum(int[] xcoordins) {
        int value = xcoordins[0];

        for (int i = 1; i < xcoordins.length; i++) {
            if (value < xcoordins[i]) {
                value = xcoordins[i];
            }
        }
        return value;
    }


    public static int minimum(int[] xcoordins) {
        int value = xcoordins[0];

        for (int i = 1; i < xcoordins.length; i++) {
            if (value > xcoordins[i]) {
                value = xcoordins[i];
            }
        }
        return value;
    }


    public static void main(String[] args) {
        List<Integer>[] nums = new List[8];
        nums[0] = Arrays.asList();
        nums[1] = Arrays.asList(1);
        nums[2] = Arrays.asList(1, 2);
        nums[3] = Arrays.asList(1, 2, 3);
        nums[4] = Arrays.asList(1, 2, 3, 4);
        nums[5] = Arrays.asList(1, 2, 3, 4, 5);
        nums[6] = Arrays.asList(1, 2, 3, 4, 5, 6);
        nums[7] = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        for (List<Integer> list : nums) {
            logger.info(list + ": " + median(list));
        }
    }
}

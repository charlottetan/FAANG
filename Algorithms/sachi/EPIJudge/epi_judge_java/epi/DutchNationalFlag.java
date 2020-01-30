package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DutchNationalFlag {
    public static void dutchFlagPartition(int pivotIndex, List<Color> A) {

        // TODO - you fill in here.
        int pivot = A.get(pivotIndex).ordinal();
        int i = 0, a = 0, b = A.size();

        while (i < b) {
            int currColor = A.get(i).ordinal();
            if (currColor < pivot) {
                Collections.swap(A, a++, i++);
            } else if (currColor > pivot) {
                Collections.swap(A, i, --b);
            } else {
                i++;
            }
        }

        //Naive Solution
      /*int pivot = A.get(pivotIndex).ordinal();
        List<Color> small = new ArrayList<>();
        List<Color> med = new ArrayList<>();
        List<Color> large = new ArrayList<>();
        for (Color color : A) {
            int colorVal = color.ordinal();
            if (colorVal == pivot) {
                med.add(color);
            } else if (colorVal > pivot) {
                large.add(color);
            } else {
                small.add(color);
            }
        }
        A.clear();
        for (Color c : small) {
            A.add(c);
        }
        for (Color c : med) {
            A.add(c);
        }
        for (Color c : large) {
            A.add(c);
        }*/
    }

    @EpiTest(testDataFile = "dutch_national_flag.tsv")
    public static void dutchFlagPartitionWrapper(TimedExecutor executor,
                                                 List<Integer> A, int pivotIdx)
            throws Exception {
        List<Color> colors = new ArrayList<>();
        int[] count = new int[3];

        Color[] C = Color.values();
        for (int i = 0; i < A.size(); i++) {
            count[A.get(i)]++;
            colors.add(C[A.get(i)]);
        }

        Color pivot = colors.get(pivotIdx);
        executor.run(() -> dutchFlagPartition(pivotIdx, colors));

        int i = 0;
        while (i < colors.size() && colors.get(i).ordinal() < pivot.ordinal()) {
            count[colors.get(i).ordinal()]--;
            ++i;
        }

        while (i < colors.size() && colors.get(i).ordinal() == pivot.ordinal()) {
            count[colors.get(i).ordinal()]--;
            ++i;
        }

        while (i < colors.size() && colors.get(i).ordinal() > pivot.ordinal()) {
            count[colors.get(i).ordinal()]--;
            ++i;
        }

        if (i != colors.size()) {
            throw new TestFailure("Not partitioned after " + i +
                    "th element");
        } else if (count[0] != 0 || count[1] != 0 || count[2] != 0) {
            throw new TestFailure("Some elements are missing from original array");
        }
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "DutchNationalFlag.java",
                                new Object() {
                                }.getClass().getEnclosingClass())
                        .ordinal());
    }

    public enum Color {RED, WHITE, BLUE}
}

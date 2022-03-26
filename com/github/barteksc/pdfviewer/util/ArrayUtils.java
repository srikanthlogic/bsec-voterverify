package com.github.barteksc.pdfviewer.util;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ArrayUtils {
    private ArrayUtils() {
    }

    public static int[] deleteDuplicatedPages(int[] pages) {
        List<Integer> result = new ArrayList<>();
        int lastInt = -1;
        for (int i : pages) {
            Integer currentInt = Integer.valueOf(i);
            if (lastInt != currentInt.intValue()) {
                result.add(currentInt);
            }
            lastInt = currentInt.intValue();
        }
        int[] arrayResult = new int[result.size()];
        for (int i2 = 0; i2 < result.size(); i2++) {
            arrayResult[i2] = result.get(i2).intValue();
        }
        return arrayResult;
    }

    public static int[] calculateIndexesInDuplicateArray(int[] originalUserPages) {
        int[] result = new int[originalUserPages.length];
        if (originalUserPages.length == 0) {
            return result;
        }
        int index = 0;
        result[0] = 0;
        for (int i = 1; i < originalUserPages.length; i++) {
            if (originalUserPages[i] != originalUserPages[i - 1]) {
                index++;
            }
            result[i] = index;
        }
        return result;
    }

    public static String arrayToString(int[] array) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i != array.length - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}

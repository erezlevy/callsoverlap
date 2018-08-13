package com.credorax.callstask.utils;

import org.apache.commons.lang3.RandomUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static long getYearInMilis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2018);
        int daysInYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        return TimeUnit.DAYS.toMillis(daysInYear);
    }

    public static long[] splitIntoParts(long whole, int parts) {
        long[] arr = new long[parts];
        for (int i = 0; i < arr.length; i++)
            whole -= arr[i] = (whole + parts - i - 1) / (parts - i);
        return arr;
    }

    public static void fileGenerator(int phoneCallsNumber, String filename) {
        TreeSet<String> strings = new TreeSet<>((o1, o2) -> {
            int i = Long.valueOf(o1.substring(0, o1.indexOf("-"))).compareTo(Long.valueOf(o2.substring(0, o2.indexOf("-"))));
            if (i == 0) {
                return Long.valueOf(o2.substring(o2.indexOf("-"))).compareTo(Long.valueOf(o1.substring(o1.indexOf("-"))));
            }
            return i;

        });
        for (int i = 0; i < phoneCallsNumber; i++) {
            long start = RandomUtils.nextLong(0, getYearInMilis());
            long end = RandomUtils.nextLong(start + 1, getYearInMilis());
            strings.add(start + "-" + end);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            strings.forEach(s -> {
                try {
                    writer.append(s);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

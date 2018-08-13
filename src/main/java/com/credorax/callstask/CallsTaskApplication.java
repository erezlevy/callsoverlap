package com.credorax.callstask;

import com.credorax.callstask.entities.FileTreeSet;
import com.credorax.callstask.services.OverlapProcessingService;
import com.credorax.callstask.services.OverlapProcessingServiceImpl;
import com.credorax.callstask.utils.TimeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class CallsTaskApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome");
        System.out.println("======================================");
        System.out.println("1. Generate File");
        System.out.println("2. Process File");
        Integer menuOption = scanner.nextInt();

        switch (menuOption) {
            case 1:
                System.out.println("Choose how many phone calls :");
                int phoneCalls = scanner.nextInt();
                System.out.println("Choose filename :");
                String filename = scanner.next();
                TimeUtils.fileGenerator(phoneCalls, filename);
                break;
            case 2:
                System.out.println("Choose filename to process (with suffix):");
                String filenameToProcess = scanner.next();
                Set<Long> set = new FileTreeSet(499_000_000L); // 500 million longs equal to 4GB
                OverlapProcessingService overlapProcessingServiceImpl = new OverlapProcessingServiceImpl(set);
                try (Stream<String> lines = Files.lines(Paths.get(filenameToProcess))) {
                    String timeFrameRes = overlapProcessingServiceImpl.process(lines);
                    System.out.println("Maximum Calls Occurred during : " + timeFrameRes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                System.out.println("no such option run again");
        }

    }


}

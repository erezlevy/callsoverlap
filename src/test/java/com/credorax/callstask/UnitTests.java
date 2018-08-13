package com.credorax.callstask;

import com.credorax.callstask.entities.FileTreeSet;
import com.credorax.callstask.services.OverlapProcessingService;
import com.credorax.callstask.services.OverlapProcessingServiceImpl;
import com.credorax.callstask.utils.TimeUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.credorax.callstask.utils.TimeUtils.getYearInMilis;
import static org.assertj.core.api.Assertions.assertThat;

public class UnitTests {

    private static final long MILLIS_IN_SECOND = 1000;
    private static final long SECONDS_IN_MINUTE = 60;
    private static final long MINUTES_IN_HOUR = 60;
    private static final long HOURS_IN_DAY = 24;
    private static final long DAYS_IN_YEAR = 365;

    @Test
    public void oneYearInMilis() {
        long yearInMilis = getYearInMilis();
        System.out.println(yearInMilis);
        assertThat(yearInMilis).isEqualTo(MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_YEAR);
    }


    @Test
    public void yearTo50Parts() {
        long[] parts = TimeUtils.splitIntoParts(getYearInMilis(), 100);
        System.out.println(parts[0]);
        assertThat(Arrays.stream(parts).sum()).isEqualTo(getYearInMilis());
    }

    @Test
    public void testWithString() {
        String[] string = new String[]{"0-1", "0-2", "0-3", "0-4", "0-5", "1-2", "1-6", "1-7", "1-8", "1-9", "2-3"};
        Set<Long> set = new FileTreeSet(5L);
        OverlapProcessingService overlapProcessingServiceImpl = new OverlapProcessingServiceImpl(set);
        String timeFrameRes = overlapProcessingServiceImpl.process(Stream.of(string));
        assertThat(timeFrameRes).isEqualTo("1-2");
        Set<Long> reqularSet = new TreeSet<>(Long::compare);
        OverlapProcessingService processingService = new OverlapProcessingServiceImpl(reqularSet);
        String processRes = processingService.process(Stream.of(string));
        assertThat(timeFrameRes).isEqualTo(processRes);
    }

    @Test
    public void testWithString2() {
        String[] string = new String[]{"0-3", "0-5", "0-6", "0-7", "1-2", "1-3", "2-4", "2-5", "3-5", "4-6"};
        Set<Long> set = new FileTreeSet(5L);
        OverlapProcessingService overlapProcessingServiceImpl = new OverlapProcessingServiceImpl(set);
        String timeFrameRes = overlapProcessingServiceImpl.process(Stream.of(string));
        assertThat(timeFrameRes).isEqualTo("2-3");
        Set<Long> reqularSet = new TreeSet<>(Long::compare);
        OverlapProcessingService processingService = new OverlapProcessingServiceImpl(reqularSet);
        String processRes = processingService.process(Stream.of(string));
        assertThat(timeFrameRes).isEqualTo(processRes);
    }

    @Test
    public void testWithStringSame() {
        String[] string = new String[]{"1-9", "2-4", "6-8"};
        Set<Long> set = new TreeSet<>(Long::compare);
        OverlapProcessingService overlapProcessingServiceImpl = new OverlapProcessingServiceImpl(set);
        String timeFrameRes = overlapProcessingServiceImpl.process(Stream.of(string));
        assertThat(timeFrameRes).isEqualTo("2-4");
    }

    @Test
    public void testWithFile() {
        final Set<Long> set = new TreeSet<>(Long::compare);
        Set<Long> fileSet = new FileTreeSet(3L);
        Supplier<Stream<String>> sup = this::getLinesStream;

        OverlapProcessingService overlapProcessingServiceImpl = new OverlapProcessingServiceImpl(set);
        String timeFrameRes = overlapProcessingServiceImpl.process(sup.get());
        System.out.println(timeFrameRes);
        OverlapProcessingService overlapProcessingService = new OverlapProcessingServiceImpl(fileSet);
        String timeFrameResWithTreeSetFile = overlapProcessingService.process(sup.get());

        assertThat(timeFrameRes).isEqualTo(timeFrameResWithTreeSetFile);

    }

    private Stream<String> getLinesStream() {
        try {
            return Files.lines(Paths.get("generated.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }
}

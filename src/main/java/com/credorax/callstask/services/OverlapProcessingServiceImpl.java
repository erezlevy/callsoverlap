package com.credorax.callstask.services;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class OverlapProcessingServiceImpl implements OverlapProcessingService {
    private Set<Long> set;
    private Long max; //max overlap
    private String overlapTimeFrame;
    private Long checkRun = 0L;


    public OverlapProcessingServiceImpl(Set<Long> set) {
        this.set = set;
    }

    public Long getMax() {
        return max;
    }

    @Override
    public String process(Stream<String> stream) {
        AtomicReference<Long> counter = new AtomicReference<>(0L);
        max = 0L;
        overlapTimeFrame = null;
        stream.forEach(s -> {
            checkRun++;
            String[] split = s.split("-");
            Long value = Long.valueOf(split[1]);
            set.add(value);
            if (Long.valueOf(split[0]) >= set.iterator().next()) {
                set.remove(set.iterator().next());
            } else {
                counter.getAndSet(counter.get() + 1);
                if (counter.get() > max) {
                    overlapTimeFrame = split[0] + "-" + set.iterator().next();
                }
                max = Math.max(counter.get(), max);
            }
            if(checkRun % 10_000 == 0){
                System.out.println("passed :" + checkRun);
            }
        });
        return overlapTimeFrame;
    }
}
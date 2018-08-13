package com.credorax.callstask.entities;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeSet;

public class FileTreeSet extends TreeSet<Long> {
    private final Long maxCapacity; //since 500 million longs equal to 4gb ( that the limit )
    private final String filename;
    final Path path;

    public FileTreeSet(Long maxCapacity) {
        super(Long::compareTo);
        this.maxCapacity = maxCapacity;
        filename = "treeset_over_memory" + new Date().getTime() + ".txt";
        path = Paths.get(filename);
        try {
            Files.delete(path);
        } catch (IOException e) {
            //delete anyway
        }
    }

    @Override
    public boolean add(Long aLong) {
        if (this.size() < maxCapacity)
            return super.add(aLong);
        else {
            if (aLong.compareTo(this.last()) < 0) {
                Long last = this.last();
                super.remove(last);
                boolean added = super.add(aLong);
                if (added)
                    return writeToFile(last);
                else {
                    return super.add(last);
                }
            }
            return writeToFile(aLong);
        }
    }

    @Override
    public boolean remove(Object o) {
        boolean remove;
        if (this.size() <= maxCapacity && Files.exists(path) && !isEmptyFile()) {
            try {
                String s = removeFirstLine(filename);
                remove = super.remove(o);
                this.add(Long.valueOf(s));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            remove = super.remove(o);
        }
        return remove;
    }

    private boolean isEmptyFile() {
        try {
            return Files.size(path) == 0L;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean writeToFile(Long e) {
        try {
            Files.write(path, Arrays.asList(String.valueOf(e)), StandardCharsets.UTF_8,
                    Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    private String removeFirstLine(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        //Initial write position
        long writePosition = raf.getFilePointer();
        String s = raf.readLine();
        // Shift the next lines upwards.
        long readPosition = raf.getFilePointer();

        byte[] buff = new byte[1024];
        int n;
        while (-1 != (n = raf.read(buff))) {
            raf.seek(writePosition);
            raf.write(buff, 0, n);
            readPosition += n;
            writePosition += n;
            raf.seek(readPosition);
        }
        raf.setLength(writePosition);
        raf.close();
        return s;
    }
}

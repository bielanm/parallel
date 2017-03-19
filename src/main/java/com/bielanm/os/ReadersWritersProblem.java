package com.bielanm.os;

import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.os.readerswriters.*;
import com.bielanm.util.Randomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public class ReadersWritersProblem {

    public static final Randomizer randomizer = new Randomizer();
    public static final String characters = "abcdefghijklmnopqrstuvwxyz";

    public static final int THREAD_COUNT = 16;

    public static final int DATA_SET_COUNT = 100;
    public static final int MAX_KEY_LENGTH = 16;
    public static final int MAX_DATA_LENGTH = 16*16*16;

    public static final int MAX_READ_TIME = 10;
    public static final int MAX_WRITE_TIME = 2;

    public static void main(String[] args) {

        Storage storage = new KeyValueStorage();

        Reader reader = new UselessReader(storage);
        Writer writer = new UselessWriter(storage);

        List<String> keys = new ArrayList<>();
        List<String> data = new ArrayList<>();
        Stream.iterate(0, i -> {
            keys.add(randomStr((abs(randomizer.nextInt() % MAX_KEY_LENGTH) + 1)));
            data.add(randomStr((abs(randomizer.nextInt() % MAX_DATA_LENGTH) + 1)));
            return i + 1;
        }).limit(DATA_SET_COUNT).count();


        List<Runnable> tasks = new ArrayList<>();
        Stream.iterate(0, i -> {
            String rndKey = keys.get(abs(randomizer.nextInt() % keys.size()));
            String rndData = data.get(abs(randomizer.nextInt() % data.size()));
            tasks.add(() -> writer.write(rndKey, rndData));
            tasks.add(() -> reader.read(rndKey));
            return i + 1;
        }).limit(DATA_SET_COUNT).count();


        PoolExecutor executor = Cuncurent.blockingFixedPoolExecutor(THREAD_COUNT);
//        Thread observer = new Thread(() ->  {
//            storage.printStatus();
//        });
//        observer.setPriority(Thread.MAX_PRIORITY);
//        observer.start();
        executor.submit(tasks);
    }

    private static String randomStr(int n) {
        final StringBuilder builder = new StringBuilder();
        Stream.iterate(0, i -> {
            builder.append(characters.charAt(abs(randomizer.nextInt() % characters.length())));
            return i + 1;
        }).limit(n+1).count();
        return builder.toString();
    }

}

package com.bielanm.os;

import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.os.readerswriters.*;
import com.bielanm.util.Randomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public class ReadersWritersProblem {

    public static final Randomizer randomizer = new Randomizer();
    public static final String characters = "abcdefghijklmnopqrstuvwxyz";

    public static final int THREAD_COUNT = 16;

    public static final int DATA_SET_COUNT = 50;
    public static final int MAX_KEY_LENGTH = 16;
    public static final int MAX_DATA_LENGTH = 32;

    public static final int MAX_READ_TIME = 10;
    public static final int MAX_WRITE_TIME = 2;

    public static void main(String[] args) {

        Storage storage = new KeyValueStorage();

        List<Reader> readers = new CopyOnWriteArrayList<>();
        List<Writer> writers = new CopyOnWriteArrayList<>();
        List<String> keys = new CopyOnWriteArrayList<>();
        List<String> data = new CopyOnWriteArrayList<>();
        List<Runnable> tasks = new CopyOnWriteArrayList<>();

        Stream.iterate(0, i -> {
            writers.add(new UselessWriter(storage));
            keys.add(randomizer.nextInt() % DATA_SET_COUNT + "");
            data.add(randomStr((abs(randomizer.nextInt() % MAX_DATA_LENGTH) + 1)));

            final int num = i;
            String rndKey = keys.get(i);
            String rndData = data.get(i);

            tasks.add(() -> {
                try {
                    Thread.sleep(abs(randomizer.nextInt() % MAX_WRITE_TIME*70));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                writers.get(num).write(rndKey, rndData);
            });
            return i + 1;
        }).limit((DATA_SET_COUNT + 2)/2).count();

        Stream.iterate(0, i -> {
            readers.add(new UselessReader(storage));
            final int num = i;
            String rndKey = keys.get(abs(randomizer.nextInt(DATA_SET_COUNT/2)));

            tasks.add(() -> {
                try {
                    Thread.sleep(abs(randomizer.nextInt() % MAX_WRITE_TIME*70));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readers.get(num).read(rndKey);
            });
            return i + 1;
        }).limit(DATA_SET_COUNT).count();

        Collections.shuffle(tasks, new Random(59));

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

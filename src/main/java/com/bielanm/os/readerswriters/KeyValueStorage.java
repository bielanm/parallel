package com.bielanm.os.readerswriters;

import com.bielanm.cuncurency.AtomicInteger;
import com.bielanm.os.ReadersWritersProblem;
import com.bielanm.util.Randomizer;

import java.util.HashMap;
import java.util.Map;

import static com.bielanm.os.ReadersWritersProblem.MAX_READ_TIME;
import static com.bielanm.os.ReadersWritersProblem.MAX_WRITE_TIME;
import static java.lang.Math.abs;

public class KeyValueStorage implements Storage {

    public static final Randomizer randomizer = new Randomizer();
    Map<Query, Data> storage = new HashMap<>();

    private volatile AtomicInteger writeRequest = new AtomicInteger(0);
    private volatile AtomicInteger inWrite = new AtomicInteger(0);
    private volatile AtomicInteger inRead = new AtomicInteger(0);

    @Override
    public synchronized void save(Query query, Data data) {
        try {
            printStatus();
            writeRequest.incrementAndGet();
            while ((inWrite.get() > 0) || (inRead.get() > 0)) wait();
            writeRequest.decrementAndGet();
            inWrite.incrementAndGet();
            printStatus();
            storage.put(query, data);
            Thread.sleep(abs(randomizer.nextInt() % MAX_WRITE_TIME));
            inWrite.decrementAndGet();
            printStatus();
        } catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        } finally {
            notifyAll();
        }
    }

    @Override
    public Data read(Query query) {
        try {
            printStatus();
            synchronized (this) {
                while ((inWrite.get() > 0) || (writeRequest.get() > 0)) wait();
            }

            inRead.incrementAndGet();
            printStatus();
            Data data = storage.get(query);
            Thread.sleep(abs(randomizer.nextInt() % MAX_READ_TIME));
            inRead.decrementAndGet();
            printStatus();
            return data;
        } catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        } finally {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    @Override
    public void printStatus() {
        System.out.println("Wait to write: " + writeRequest.get() + ", write now: " + inWrite.get() + ", read now: " + inRead.get());
    }
}

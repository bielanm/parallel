package com.bielanm.os.readerswriters;

import com.bielanm.os.ReadersWritersProblem;
import com.bielanm.util.Randomizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.bielanm.os.ReadersWritersProblem.MAX_READ_TIME;
import static com.bielanm.os.ReadersWritersProblem.MAX_WRITE_TIME;
import static java.lang.Math.abs;

public class KeyValueStorage implements Storage {

    public static final Data nullableData = () -> "null";
    public static final Randomizer randomizer = new Randomizer();
    Map<Query, Data> storage = new HashMap<>();

    private volatile AtomicInteger writeRequest = new AtomicInteger(0);
    private volatile AtomicInteger readRequest = new AtomicInteger(0);
    private volatile AtomicInteger inWrite = new AtomicInteger(0);
    private volatile AtomicInteger inRead = new AtomicInteger(0);

    private final Object printMutex = new Object();
    @Override
    public synchronized void save(Query query, Data data, String name) {
        try {
            writeRequest.incrementAndGet();
            System.out.println(name + " come and write if library is empty.");
            printStatus();
            while ((inWrite.get() > 0) || (inRead.get() > 0)){
                wait();
            }
            writeRequest.decrementAndGet();
            inWrite.incrementAndGet();
            System.out.println(name + " come into library");
            printStatus();
            storage.put(query, data);
            Thread.sleep(abs(randomizer.nextInt() % MAX_WRITE_TIME));
            inWrite.decrementAndGet();
            System.out.println(name + " save data {");
            System.out.println("\tKey: " + query.getQuery() + ",");
            System.out.println("\tvalue: " + data.getData() + ";");
            System.out.println("}");
            System.out.println(name + " leave the library");
            printStatus();
        } catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        } finally {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    @Override
    public Data read(Query query, String name) {
        try {
            synchronized (this) {
                System.out.println(name + " come and read if there isn't writer and there isn't write request.");
                printStatus();
                while ((inWrite.get() > 0) || (writeRequest.get() > 0))
                    wait();
                inRead.incrementAndGet();
                System.out.println(name + " come into library.There are " + inRead.get() + " readers in the library now");
                printStatus();
            }

            Data data = storage.get(query);
            Thread.sleep(abs(randomizer.nextInt() % MAX_READ_TIME));
            synchronized (this) {
                inRead.decrementAndGet();
                System.out.println(name + " read data {\n\tKey: " + query.getQuery() + ",\n\tvalue: " + Optional.ofNullable(data).orElseGet(() -> nullableData).getData() + "\n}\nAnd leave the library" + "");
                printStatus();
                notifyAll();
            }
            return data;
        } catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        }

    }

    @Override
    public void printStatus() {
        System.out.println("Library status: wait to write: " + writeRequest.get() + ", write now: " + inWrite.get() + ", read now: " + inRead.get());
    }
}

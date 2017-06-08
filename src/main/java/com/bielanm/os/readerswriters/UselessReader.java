package com.bielanm.os.readerswriters;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UselessReader implements Reader {

    private static int count = 0;

    private Storage storage;
    private final int number = ++count;
    private final String name = "Reader_" + number;

    public UselessReader(Storage storage) {
        this.storage = storage;
    }


    @Override
    public String read(String key) {
        Data data = storage.read(new KeyQuery(key), name);
        return Objects.isNull(data) ? null : data.getData();
    }
}

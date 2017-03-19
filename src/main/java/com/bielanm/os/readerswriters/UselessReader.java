package com.bielanm.os.readerswriters;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UselessReader implements Reader {

    private Storage storage;

    public UselessReader(Storage storage) {
        this.storage = storage;
    }


    @Override
    public String read(String key) {
        Data data = storage.read(new KeyQuery(key));
        return Objects.isNull(data) ? null : data.getData();
    }
}

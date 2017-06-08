package com.bielanm.os.readerswriters;

public class UselessWriter implements Writer {

    private static int count = 0;

    private Storage storage;
    private final int number = ++count;
    private final String name = "Writer_" + number;

    public UselessWriter(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void write(String key, String value) {
        storage.save(new KeyQuery(key), new StringData(value), name);
    }
}

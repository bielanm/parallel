package com.bielanm.os.readerswriters;

public class UselessWriter implements Writer {

    private Storage storage;

    public UselessWriter(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void write(String key, String value) {
        storage.save(new KeyQuery(key), new StringData(value));
    }
}

package com.bielanm.os.readerswriters;

public interface Storage {

    void save(Query query, Data data, String name);
    Data read(Query query, String name);

    void printStatus();

}

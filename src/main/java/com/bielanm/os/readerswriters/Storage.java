package com.bielanm.os.readerswriters;

public interface Storage {

    void save(Query query, Data data);
    Data read(Query query);

    void printStatus();

}

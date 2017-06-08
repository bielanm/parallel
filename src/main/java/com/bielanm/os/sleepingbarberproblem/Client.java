package com.bielanm.os.sleepingbarberproblem;

public class Client {

    private static int count = 0;
    private String name = "Client_" + count++;

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

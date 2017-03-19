package com.bielanm.os.readerswriters;

public class StringData implements Data{

    private String data;

    public StringData(String data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }
}

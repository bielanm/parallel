package com.bielanm.os.readerswriters;

public class KeyQuery implements Query {

    private String key;

    public KeyQuery(String key) {
        this.key = key;

    }

    @Override
    public String getQuery() {
        return key;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyQuery query = (KeyQuery) o;

        return key != null ? key.equals(query.getQuery()) : query.key == null;

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}

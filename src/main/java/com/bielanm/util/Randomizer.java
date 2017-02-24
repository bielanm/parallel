package com.bielanm.util;

import java.util.Random;

public class Randomizer extends Random {

    public Randomizer() {
        super(System.currentTimeMillis());
    }
}

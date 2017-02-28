package com.bielanm.util.secondlab;


import com.bielanm.cuncurency.BlockingQueue;
import com.bielanm.cuncurency.FixedPoolExecutorImpl;

public class CPU extends FixedPoolExecutorImpl{

    public CPU(BlockingQueue queue) {
        super(1, queue);
    }
}

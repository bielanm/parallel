package com.bielanm.cuncurency;


public interface PoolExecutor {

    void submit(Runnable runnable);

}

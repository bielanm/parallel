package com.romanb.cuncurency;


import java.util.List;

public interface PoolExecutor {

    void submit(Runnable runnable);

    void submit(List<Runnable> runnables);

    void shutdown();

}

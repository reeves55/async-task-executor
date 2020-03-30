package com.xiaolee.async.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class AsyncTaskExecutor {
    ThreadPoolExecutor pool;
    private static final int DEFAULT_POOL_SIZE = 2;

    public AsyncTaskExecutor() {
//        this.corePoolSize = corePoolSize;
//        this.maximumPoolSize = maximumPoolSize;
//        this.workQueue = workQueue;
//        this.keepAliveTime = unit.toNanos(keepAliveTime);
//        this.threadFactory = threadFactory;
//        this.handler = handler;
//        pool = Executors.newFixedThreadPool(1,)
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);
        pool = new ThreadPoolExecutor(DEFAULT_POOL_SIZE, DEFAULT_POOL_SIZE, 60, TimeUnit.SECONDS, queue);
    }

    public TaskPromise execute(Runnable task) {
        DefaultTaskPromise promise = new DefaultTaskPromise(task);
        pool.execute(promise.newWrapper());
        return promise;
    }


}

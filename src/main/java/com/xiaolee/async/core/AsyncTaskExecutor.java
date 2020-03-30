package com.xiaolee.async.core;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class AsyncTaskExecutor {
    ThreadPoolExecutor pool;

    public AsyncTaskExecutor() {
//        this.corePoolSize = corePoolSize;
//        this.maximumPoolSize = maximumPoolSize;
//        this.workQueue = workQueue;
//        this.keepAliveTime = unit.toNanos(keepAliveTime);
//        this.threadFactory = threadFactory;
//        this.handler = handler;
//        pool = Executors.newFixedThreadPool(1,)
    }

    public TaskPromise execute(Runnable task) {
        DefaultTaskPromise promise = new DefaultTaskPromise(task);
        pool.execute(promise.newWrapper());
        return promise;
    }
}

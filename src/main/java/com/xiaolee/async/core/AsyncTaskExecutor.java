package com.xiaolee.async.core;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class AsyncTaskExecutor {
    ThreadPoolExecutor pool;
    private static final int DEFAULT_POOL_SIZE = 2;

    public AsyncTaskExecutor() {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);
        pool = new ThreadPoolExecutor(DEFAULT_POOL_SIZE, DEFAULT_POOL_SIZE, 60, TimeUnit.SECONDS, queue, new AsyncThreadFactory());
    }

    public TaskPromise execute(Runnable task) {
        DefaultTaskPromise<Void> promise = new DefaultTaskPromise<Void>(task);
        pool.execute(promise.newWrapper());
        return promise;
    }

    public <T> TaskPromise execute(Callable<T> task) {
        DefaultTaskPromise<T> promise = new DefaultTaskPromise<T>(task);
        pool.execute(promise.newWrapper());
        return promise;
    }

    class AsyncThreadFactory implements ThreadFactory {
        private AtomicInteger id = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "AsyncTaskThread-" + id.getAndIncrement());
            thread.setDaemon(false);
            return thread;
        }
    }
}

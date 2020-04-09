package com.xiaolee.async;

import com.xiaolee.async.promise.TaskPromise;

import java.util.concurrent.Callable;

public interface AsyncExecutorService {
    TaskPromise<Void> execute(Runnable task);

    <T> TaskPromise<T> execute(Callable<T> task);


}

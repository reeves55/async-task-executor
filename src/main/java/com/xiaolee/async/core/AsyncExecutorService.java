package com.xiaolee.async.core;

import java.util.concurrent.Callable;

public interface AsyncExecutorService {
    TaskPromise<Void> execute(Runnable task);

    <T> TaskPromise<T> execute(Callable<T> task);


}

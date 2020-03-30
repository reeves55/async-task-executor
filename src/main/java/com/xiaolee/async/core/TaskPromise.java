package com.xiaolee.async.core;

import java.util.concurrent.Callable;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public interface TaskPromise {
    TaskPromise sync() throws InterruptedException;

    TaskPromise addListener(TaskPromiseListener listener);

    Callable<Object> getTask();

    void setResult(Object result);
}

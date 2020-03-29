package com.xiaolee.async.core;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class DefaultTaskPromise implements TaskPromise{
    Object result;

    public TaskPromise sync() throws InterruptedException {
        wait();
        return this;
    }
}
